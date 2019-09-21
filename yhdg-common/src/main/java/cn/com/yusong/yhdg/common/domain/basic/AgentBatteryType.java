package cn.com.yusong.yhdg.common.domain.basic;

import cn.com.yusong.yhdg.common.annotation.Transient;
import cn.com.yusong.yhdg.common.domain.PageEntity;
import cn.com.yusong.yhdg.common.entity.json.DateTimeSerializer;
import lombok.Getter;
import lombok.Setter;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.util.Date;

/**
 * 运营商电池类型
 */
@Setter
@Getter
public class AgentBatteryType extends PageEntity {

	Integer agentId;//运营商id
	Integer batteryType;//电池类型
	String typeName;//类型名称

	@Transient
	Integer isActive;//是否启用
	String memo;//备注
	Integer ratedVoltage;//额定电压
	Integer ratedCapacity;//额定容量
	Date createTime;
	String agentName;
	Integer toBatteryType;
	String cabinetName;
	String cabinetId;//换电柜id
	Integer unbind;//是否未绑定换电站
    Integer activeSingleExchange;
	Integer timesPrice;

	@JsonSerialize(using = DateTimeSerializer.class)
	public Date getCreateTime() {
		return createTime;
	}

}
