package utlight.seckill.enums;



/**
 * 使用枚举 来表示常量数据字段
 * 
 * @author liusha
 *
 */
public enum SeckillStatusEnum {

	END(0, "秒杀结束"), 
	SUCCESS(1, "秒杀成功"), 
	REPEAT_KILL(-1, "重复秒杀"),
	INNER_ERROR(-2, "系统异常"), 
	DATA_REWRITE(-3, "数据篡改");

	private int status;
	private String statusInfo;

	SeckillStatusEnum(int status, String statusInfo) {
		this.status = status;
		this.statusInfo = statusInfo;
	}

	public int getStatus() {
		return status;
	}

	public String getStatusInfo() {
		return statusInfo;
	}

	public static SeckillStatusEnum statusOf(int index) {
		for (SeckillStatusEnum status : values()) {
			if (status.getStatus() == index) {
				return status;
			}
		}
		return null;
	}
}
