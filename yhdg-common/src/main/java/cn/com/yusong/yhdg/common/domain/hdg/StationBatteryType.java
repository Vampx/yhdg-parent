package cn.com.yusong.yhdg.common.domain.hdg;

import cn.com.yusong.yhdg.common.annotation.Transient;
import cn.com.yusong.yhdg.common.domain.PageEntity;
import lombok.Getter;
import lombok.Setter;

/**
 * 站点关联电池类型
 */
@Setter
@Getter
public class StationBatteryType extends PageEntity {
	String stationId; //站点id
	Integer batteryType;//电池类型

	@Transient
	String typeName;//类型名称
	String stationName;//站点名称
	String ids; //由逗号分割的Id字符串
	Integer agentId;
}
