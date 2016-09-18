package utlight.seckill.web;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import utlight.seckill.dto.Exposer;
import utlight.seckill.dto.Pager;
import utlight.seckill.dto.SeckillExecution;
import utlight.seckill.dto.SeckillResult;
import utlight.seckill.entity.Seckill;
import utlight.seckill.enums.SeckillStatusEnum;
import utlight.seckill.exception.RepeatKillException;
import utlight.seckill.exception.SeckillCloseException;
import utlight.seckill.service.SeckillService;

@Controller
@RequestMapping("/seckill")
public class SeckillController {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private SeckillService seckillService;
	
	//列表页
	@RequestMapping(value="/list",method=RequestMethod.GET)
	public String list(@RequestParam(value="pageSize",defaultValue="3")Integer pageSize, @RequestParam(value="pageIndex",defaultValue="1")Integer pageIndex,Model model){
		
		int offset = (pageIndex - 1) * pageSize;			
		List<Seckill> list = seckillService.getSeckillList(offset, pageSize);
		model.addAttribute("list", list);
		
		double totalCount = seckillService.getCount();
		Pager pager = new Pager(pageIndex, pageSize, totalCount);
		model.addAttribute("pager", pager);
		
		return "list";
	}
	
	//详情页
	@RequestMapping(value="/{seckillId}/detail",method=RequestMethod.GET)
	public String detail(@PathVariable("seckillId") Long seckillId, Model model){
		
		if (seckillId == null) {
			return "redirect:/seckill/list";
		}
		
		Seckill seckill = seckillService.getById(seckillId);
		if (seckill == null) {
			return "forward:/seckill/list";
		}
		
		model.addAttribute("seckill", seckill);
		return "detail";
		
	}
	
	//ajax  暴露秒杀接口
	@RequestMapping(value="/{seckillId}/exposer",
			method = RequestMethod.POST,
			produces = {"application/json;charset=utf-8"})
	@ResponseBody
	public SeckillResult<Exposer> exposer(@PathVariable("seckillId")Long seckillId){
		SeckillResult<Exposer> result = null;
		
		try {
			Exposer exposer = seckillService.exportSeckillUrl(seckillId);
			result = new SeckillResult<Exposer>(true, exposer);
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			result = new SeckillResult<Exposer>(false, e.getMessage());
		}
		
		return result;
	}
	
	
	//获取系统当前时间
	@RequestMapping(value="/time/now", method = RequestMethod.GET)
	@ResponseBody
	public SeckillResult<Long> time(){
		Date now = new Date();
		return new SeckillResult<Long>(true, now.getTime());
	}
	
	//执行秒杀
	@RequestMapping(value="/{seckillId}/{md5}/execution",
			method = RequestMethod.POST,
			produces = {"application/json;charset=utf-8"})
	@ResponseBody
	public SeckillResult<SeckillExecution> execute(@PathVariable("seckillId")Long seckillId,@PathVariable("md5") String md5,@CookieValue(value = "killPhone", required = false) Long phone){
		 
		if (phone == null) {
			return new SeckillResult<SeckillExecution>(false, "未注册");
		}
		
		//秒杀处理
		try {
			
			//SeckillExecution execution = seckillService.executeSeckill(seckillId, phone, md5);
			//调用存储过程的方式
			SeckillExecution execution = seckillService.executeSeckillProcedure(seckillId, phone, md5);
			return new SeckillResult<SeckillExecution>(true, execution);
		} catch (RepeatKillException e) {
			SeckillExecution execution = new SeckillExecution(seckillId, SeckillStatusEnum.REPEAT_KILL);
			return new SeckillResult<SeckillExecution>(true, execution);
		} catch (SeckillCloseException e) {
			SeckillExecution execution = new SeckillExecution(seckillId, SeckillStatusEnum.END);
			return new SeckillResult<SeckillExecution>(true, execution);
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			SeckillExecution execution = new SeckillExecution(seckillId, SeckillStatusEnum.INNER_ERROR);
			return new SeckillResult<SeckillExecution>(true, execution);
		}
	}
}
