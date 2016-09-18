package utlight.seckill.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;
import org.apache.commons.collections.MapUtils;
import org.slf4j.Logger;


import utlight.seckill.dao.SeckillDao;
import utlight.seckill.dao.SuccessKilledDao;
import utlight.seckill.dao.cache.RedisDao;
import utlight.seckill.dto.Exposer;
import utlight.seckill.dto.SeckillExecution;
import utlight.seckill.entity.Seckill;
import utlight.seckill.entity.SuccessKilled;
import utlight.seckill.enums.SeckillStatusEnum;
import utlight.seckill.exception.RepeatKillException;
import utlight.seckill.exception.SeckillCloseException;
import utlight.seckill.exception.SeckillException;
import utlight.seckill.service.SeckillService;

@Service
public class SeckillServiceImpl implements SeckillService{

	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private SeckillDao seckillDao;
	
	@Autowired
	private RedisDao redisDao;
	
	@Autowired
	private SuccessKilledDao successkilledDao;
	
	//MD5加盐
	private final String  salt = "jfslkjfaijfalsjfsijfaijiaj!##@$#$#Ijiojsfa";
	
	@Override
	public List<Seckill> getSeckillList(int offset, int limit) {
		return seckillDao.queryAll(offset, limit);
	}

	@Override
	public Seckill getById(long seckillId) {
		return seckillDao.queryById(seckillId);
	}

	@Override
	public Exposer exportSeckillUrl(long seckillId) {
		//使用redis优化
		
		Seckill seckill = redisDao.getSeckill(seckillId);
		
		if (seckill == null) {
			
			seckill = seckillDao.queryById(seckillId);
			if (seckill == null) {
				
				return new Exposer(false, seckillId);
			}else {
				
				redisDao.putSeckill(seckill);
			}
		}
		
		

		Date startTime = seckill.getStartTime();
		Date endTime = seckill.getEndTime();
		Date nowTime = new Date();
		
		//秒杀未开启
		if (nowTime.getTime() < startTime.getTime()
				|| nowTime.getTime() > endTime.getTime()) {
			
			return new Exposer(false, seckillId,nowTime.getTime(),startTime.getTime(),endTime.getTime());
			
		}
		
		String md5 = getMD5(seckillId);
		return new Exposer(true,md5, seckillId);
	}

	
	private String getMD5(long seckillId){
		
		String base = seckillId + "/" + salt;
		String md5 = DigestUtils.md5DigestAsHex(base.getBytes());
		return md5;
	}
	
	
	
    /**
     * 使用注解控制事务方法的优点:
     * 1:开发团队达成一致约定,明确标注事务方法的编程风格 --- 给予开发人员提示性
     * 2:保证事务方法的执行时间尽可能短,不要穿插其他网络操作RPC/HTTP请求或者剥离到事务方法外部.
     * 3:不是所有的方法都需要事务,如只有一条修改操作,只读操作不需要事务控制.
     */
	
	@Override
	@Transactional
	public SeckillExecution executeSeckill(long seckillId, long userPhone, String md5) 
			throws SeckillException,RepeatKillException,SeckillCloseException{
		
		if(md5 == null || !md5.equals(getMD5(seckillId))){
			throw new SeckillException("seckill data rewrite");
		}
		
		//秒杀逻辑：减库存 + 记录秒杀成功信息
		Date nowTime = new Date();
		
		try {
			//记录秒杀信息
			int insertCount = successkilledDao.insertSuccessKilled(seckillId, userPhone);
			
			if (insertCount <= 0) {
				//重复秒杀
				throw new RepeatKillException("seckill repreated");
			}else {
				
				int updateCount = seckillDao.reduceNumber(seckillId, nowTime);
				if (updateCount <= 0) {
					//秒杀结束
					throw new SeckillCloseException("seckill is closed");
				}else {
					//秒杀成功
					SuccessKilled successKilled = successkilledDao.queryByIdWithSeckill(seckillId, userPhone);
					return new SeckillExecution(seckillId, SeckillStatusEnum.SUCCESS,successKilled);
				}
			}
		} catch (SeckillCloseException e1) {
			throw e1;
		} catch (RepeatKillException e2) {
			throw e2;
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			
			throw new SeckillException("seckill inner error: "+e.getMessage());
		}
	}

	@Override
	public double getCount() {
		return seckillDao.getCount();
	}

	@Override
	public SeckillExecution executeSeckillProcedure(long seckillId, long userPhone, String md5) {
		
		if(md5 == null || !md5.equals(getMD5(seckillId))){
			return new SeckillExecution(seckillId, SeckillStatusEnum.DATA_REWRITE);
		}
		
		Date killTime = new Date();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("seckillId", seckillId);
		map.put("phone", userPhone);
		map.put("killTime", killTime);
		map.put("result", null);//输出参数
		
		try {
			seckillDao.killByProcedure(map);
			int result = MapUtils.getInteger(map, "result",-2);
			if (result == 1) {
				SuccessKilled successKilled = successkilledDao.queryByIdWithSeckill(seckillId, userPhone);
				return new SeckillExecution(seckillId, SeckillStatusEnum.SUCCESS,successKilled);
			}else {
				return new SeckillExecution(seckillId, SeckillStatusEnum.statusOf(result));
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return new SeckillExecution(seckillId, SeckillStatusEnum.INNER_ERROR);
		}
	}

}
