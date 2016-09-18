package utlight.seckill.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import utlight.seckill.entity.Seckill;

public interface SeckillDao {
	
	/**
	 * 键库存
	 * @param seckillId
	 * @param killTime
	 * @return 影响行数
	 * 使用@Param是为了给Mybatis 参数做映射作用。
	 */
	int reduceNumber(@Param("seckillId") long seckillId,@Param("killTime") Date killTime);
	
	/**
	 * 根据id查询秒杀对象
	 * @param seckillId
	 * @return 
	 */
	Seckill queryById(long seckillId);
	
	
	/**
	 * 分页查找秒杀商品列表
	 * @param offset
	 * @param limit
	 * @return
	 */
	List<Seckill> queryAll(@Param("offset")int offset, @Param("limit") int limit);
	
	/**
	 * 获取所有商品记录总数
	 * @return
	 */
	double getCount();
	
	/**
	 * 使用存储过程进行秒杀：主要解决网络延迟 GC、减少对行级锁的持有时间。
	 * @param paramMap
	 */
	void killByProcedure(Map<String, Object> paramMap);
}
