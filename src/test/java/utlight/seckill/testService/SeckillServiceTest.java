package utlight.seckill.testService;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import utlight.seckill.dto.Exposer;
import utlight.seckill.dto.SeckillExecution;
import utlight.seckill.entity.Seckill;
import utlight.seckill.exception.RepeatKillException;
import utlight.seckill.exception.SeckillCloseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import utlight.seckill.service.SeckillService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({
        "classpath:spring/spring-dao.xml",
        "classpath:spring/spring-service.xml"})
public class SeckillServiceTest {
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private SeckillService seckillService;
	
	
	   @Test
	    public void testGetSeckillList() throws Exception {
	        List<Seckill> list = seckillService.getSeckillList(0,5);
	        logger.info("list={}", list);
	    }

	    @Test
	    public void testGetById() throws Exception {
	        long id = 1000;
	        Seckill seckill = seckillService.getById(id);
	        logger.info("seckill={}", seckill);
	    }


	    //集成测试代码完整逻辑,注意可重复执行.
	    @Test
	    public void testSeckillLogic() throws Exception {
	        long id = 1003;
	        Exposer exposer = seckillService.exportSeckillUrl(id);
	        if (exposer.isExposed()) {
	            logger.info("exposer={}", exposer);
	            long phone = 13502171127L;
	            String md5 = exposer.getMd5();
	            try {
	                SeckillExecution execution = seckillService.executeSeckill(id, phone, md5);
	                logger.info("result={}", execution);
	            } catch (RepeatKillException e) {
	                logger.error(e.getMessage());
	            } catch (SeckillCloseException e) {
	                logger.error(e.getMessage());
	            }
	        } else {
	            //秒杀未开启
	            logger.warn("exposer={}", exposer);
	        }
	    }
}
