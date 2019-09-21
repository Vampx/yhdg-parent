package cn.com.yusong.yhdg.common.domain.hdg;

import cn.com.yusong.yhdg.common.annotation.Transient;
import cn.com.yusong.yhdg.common.domain.IntIdEntity;
import cn.com.yusong.yhdg.common.domain.LongIdEntity;
import cn.com.yusong.yhdg.common.domain.PageEntity;
import cn.com.yusong.yhdg.common.entity.json.DateTimeSerializer;
import lombok.Getter;
import lombok.Setter;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.util.Date;

/**
 * 换电白名单
 */
@Setter
@Getter
public class ExchangeWhiteList extends IntIdEntity {
	Integer agentId;//运营商id
	Long customerId;//用户id
	String mobile;//手机号
	String fullname;//姓名
	Integer batteryType;//电池类型
	Date createTime;//创建时间

	@Transient
	String typeName;//类型名称
	Integer ratedVoltage;//额定电压
	Integer ratedCapacity;//额定容量
	String agentName;//运营商名称
	@JsonSerialize(using = DateTimeSerializer.class)
	public Date getCreateTime() {
		return createTime;
	}
}
