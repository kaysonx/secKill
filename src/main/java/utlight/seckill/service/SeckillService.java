package utlight.seckill.service;

import java.util.List;

import utlight.seckill.dto.Exposer;
import utlight.seckill.dto.SeckillExecution;
import utlight.seckill.entity.Seckill;

public interface SeckillService {

	/**
	 * 查询所有秒杀记录
	 * @return
	 */
	List<Seckill> getSeckillList(int offset, int limit);

	/**
	 * 根据id查询单个秒杀产品记录
	 * @param seckillId
	 * @return
	 */
	Seckill getById(long seckillId);
	
	/**
	 * 导出秒杀接口地址
	 * 若 秒杀未开启则输出系统当前时间和秒杀时间
	 * @param seckillId
	 */
	Exposer exportSeckillUrl(long seckillId);
	
	/**
	 * 执行秒杀操作
	 * @param seckillId
	 * @param userPhone
	 * @param md5
	 */
	SeckillExecution executeSeckill(long seckillId, long userPhone, String md5);
	
	/**
	 * 获取秒杀产品总数 返回double是为了后面使用不用强转
	 * @return
	 */
	double getCount();
	
	/**
	 * 执行秒杀操作 -- 使用存储过程
	 * @param seckillId
	 * @param userPhone
	 * @param md5
	 * @return
	 */
	SeckillExecution executeSeckillProcedure(long seckillId, long userPhone, String md5);
	
}
