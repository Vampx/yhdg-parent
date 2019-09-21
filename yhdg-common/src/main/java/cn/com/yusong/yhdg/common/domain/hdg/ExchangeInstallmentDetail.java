package cn.com.yusong.yhdg.common.domain.hdg;

import cn.com.yusong.yhdg.common.annotation.Transient;
import cn.com.yusong.yhdg.common.domain.PageEntity;
import cn.com.yusong.yhdg.common.entity.json.DateTimeSerializer;
import lombok.Getter;
import lombok.Setter;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.util.Date;

@Getter
@Setter
public class ExchangeInstallmentDetail extends PageEntity {
	Long settingId;
	Integer num;
	Integer money;
	Integer foregiftMoney;
	Integer packetMoney;
	Integer insuranceMoney;
	Date expireTime;

	@Transient
	String payStatusName;//付款状态名称

	@JsonSerialize(using = DateTimeSerializer.class)
	public Date getExpireTime() {
		return expireTime;
	}
}
