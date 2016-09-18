package utlight.seckill.testRedisDao;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import utlight.seckill.dao.SeckillDao;
import utlight.seckill.dao.cache.RedisDao;
import utlight.seckill.entity.Seckill;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:spring/spring-dao.xml"})
public class testRedisDao {
	
	private long id = 1001;
	@Autowired
	private RedisDao redisDao;
	@Autowired
	private SeckillDao seckillDao;
	
	@Test
	public void testSeckill() throws Exception{
		//test get and set

		Seckill seckill = redisDao.getSeckill(id);
		if(seckill == null){
			seckill = seckillDao.queryById(id);
			if(seckill != null){
				String result = redisDao.putSeckill(seckill);
				System.out.println(result);
				seckill = redisDao.getSeckill(id);
				/*
				 * 127.0.0.1:6379> get seckill:1001
					"\b\xe9\a\x12\x0f10\xe5\x85\x83\xe7\xa7\x92\xe6\x9d\x80ipad\x18B!\x00\xa4yMW\x01
					\x00\x00)\x00\xb8\xec\\W\x01\x00\x001h\x15\xd0\xafV\x01\x00\x00"
					127.0.0.1:6379>
				 * */
				System.out.println(seckill);
			}
		}
	}

}
