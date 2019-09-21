package cn.com.yusong.yhdg.common.domain.basic;

import cn.com.yusong.yhdg.common.domain.PageEntity;
import lombok.Getter;
import lombok.Setter;

/**
 * 升级的设备
 */

@Setter
@Getter
public class BatteryUpgradePackDetail extends PageEntity {

    public String id;
    public Integer upgradePackId;
    public String batteryId;
    public String batteryName;
    public String version;


    public String code;
    public String bmsModel;

}
