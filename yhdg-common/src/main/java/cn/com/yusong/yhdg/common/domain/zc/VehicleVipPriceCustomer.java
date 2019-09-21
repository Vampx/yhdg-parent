package cn.com.yusong.yhdg.common.domain.zc;

import cn.com.yusong.yhdg.common.annotation.Transient;
import cn.com.yusong.yhdg.common.domain.LongIdEntity;
import cn.com.yusong.yhdg.common.entity.json.DateTimeSerializer;
import lombok.Getter;
import lombok.Setter;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.util.Date;

/**
 * 租车VIP 套餐客户
 */
@Getter
@Setter
public class VehicleVipPriceCustomer extends LongIdEntity {
	Integer priceId;/*vip套餐Id*/
	String mobile;/*手机号*/
	Date createTime;

	@Transient
	Integer agentId;

	@JsonSerialize(using = DateTimeSerializer.class)
	public Date getCreateTime() {
		return createTime;
	}
}
