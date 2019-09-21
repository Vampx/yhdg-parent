package cn.com.yusong.yhdg.common.domain.hdg;

import cn.com.yusong.yhdg.common.annotation.Transient;
import cn.com.yusong.yhdg.common.domain.PageEntity;
import lombok.Getter;
import lombok.Setter;

/**
 * 柜子关联电池类型
 */
@Setter
@Getter
public class CabinetBatteryType extends PageEntity {
	String cabinetId;
	Integer batteryType;//电池类型

	@Transient
	String typeName;//类型名称
	String cabinetName;//设备名称
	String ids; //由逗号分割的Id字符串
	Integer agentId;
}
