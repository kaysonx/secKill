-- 本机数据库 版本
--mysql> select version();
--+-----------+
--| version() |
--+-----------+
--| 5.6.24    |
--+-----------+

-- 创建数据库
CREATE DATABASE seckill;
--
use seckill;
-- 创建秒杀库存表
CREATE TABLE seckill(
`seckill_id` bigint NOT NULL AUTO_INCREMENT COMMENT '商品库存id',
`name` varchar(120) NOT NULL COMMENT '商品名称',
`number` int NOT NULL COMMENT '库存数量',
`start_time` timestamp NOT NULL COMMENT '秒杀开启时间',
`end_time` timestamp NOT NULL COMMENT '秒杀结束时间',
`create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '记录创建时间',

PRIMARY KEY(seckill_id),
key idx_start_time(start_time),
key idx_end_time(end_time),
key idx_create_time(create_time)

)ENGINE=InnoDB AUTO_INCREMENT=1000 DEFAULT CHARSET=UTF8 COMMENT='秒杀库存表';

-- 初始化数据
insert into 
	seckill(name,number,start_time,end_time)
values
	('1元秒杀iPhone7',10,'2016-8-22 00:00:00','2016-8-25 00:00:00'),
	('10元秒杀ipad',66,'2016-9-22 00:00:00','2016-9-25 00:00:00'),
	('100元秒杀华为P9',100,'2016-8-24 00:00:00','2016-8-31 00:00:00'),
	('250元秒杀meizu e',200,'2016-8-21 00:00:00','2016-8-22 00:00:00'),
	('666元秒杀mac',400,'2016-8-22 00:00:00','2016-8-25 00:00:00');
	
	
-- 秒杀成功明细表
CREATE TABLE success_killed(
`seckill_id` bigint NOT NULL COMMENT '对应秒杀产品id',
`user_phone` bigint NOT NULL COMMENT '用户手机号-用户标识用户',
`status` tinyint NOT NULL DEFAULT -1 COMMENT '状态标识：-1:无效 0:成功 1:已付款...',
`create_time` timestamp NOT NULL COMMENT '记录创建时间',
-- 联合主键
PRIMARY KEY(seckill_id,user_phone),
key idx_create_time(create_time)
)ENGINE=InnoDB DEFAULT CHARSET=UTF8 COMMENT='秒杀成功明细表';


-- 手写DDL的目的是为了每次记录每次上线的DDL修改。