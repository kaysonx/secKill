package utlight.seckill.dto;

import utlight.seckill.entity.SuccessKilled;
import utlight.seckill.enums.SeckillStatusEnum;

/**
 * 秒杀执行结果
 * @author liusha
 *
 */
public class SeckillExecution {

	private long seckillId;
	
	//秒杀结果状态
	private int status;
	
	//状态对应信息
	private String statusInfo;
	
	//秒杀成功对象
	private SuccessKilled successKilled;

	public SeckillExecution(long seckillId, SeckillStatusEnum statusEnum, SuccessKilled successKilled) {
		super();
		this.seckillId = seckillId;
		this.status = statusEnum.getStatus();
		this.statusInfo = statusEnum.getStatusInfo();
		this.successKilled = successKilled;
	}

	public SeckillExecution(long seckillId,SeckillStatusEnum statusEnum) {
		super();
		this.seckillId = seckillId;
		this.status = statusEnum.getStatus();
		this.statusInfo = statusEnum.getStatusInfo();
	}

	@Override
	public String toString() {
		return "SeckillExecution [seckillId=" + seckillId + ", status=" + status + ", statusInfo=" + statusInfo
				+ ", successKilled=" + successKilled + "]";
	}

	public long getSeckillId() {
		return seckillId;
	}

	public void setSeckillId(long seckillId) {
		this.seckillId = seckillId;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getStatusInfo() {
		return statusInfo;
	}

	public void setStatusInfo(String statusInfo) {
		this.statusInfo = statusInfo;
	}

	public SuccessKilled getSuccessKilled() {
		return successKilled;
	}

	public void setSuccessKilled(SuccessKilled successKilled) {
		this.successKilled = successKilled;
	}
	
	
	
	
}
