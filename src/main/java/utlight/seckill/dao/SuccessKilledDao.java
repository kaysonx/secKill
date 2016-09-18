package utlight.seckill.dao;

import org.apache.ibatis.annotations.Param;

import utlight.seckill.entity.SuccessKilled;

public interface SuccessKilledDao {

	/**
	 * 插入购买 明细，可重复过滤
	 * @param seckillId
	 * @param userPhone
	 * @return 影响行数
	 */
	int insertSuccessKilled(@Param("seckillId")long seckillId,@Param("userPhone") long userPhone);
	
	/**
	 * 根据id查询SuccessKilled并携带秒杀产品对象
	 * @param seckillId
	 * @param userPhone
	 * @return
	 */
	SuccessKilled queryByIdWithSeckill(@Param("seckillId") long seckillId,@Param("userPhone") long userPhone);
}
