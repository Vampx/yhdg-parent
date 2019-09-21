package cn.com.yusong.yhdg.common.domain.basic;

import cn.com.yusong.yhdg.common.annotation.Transient;
import cn.com.yusong.yhdg.common.domain.PageEntity;
import lombok.Getter;
import lombok.Setter;

/**
 * 升级的设备
 */

@Setter
@Getter
public class ChargerUpgradePackDetail extends PageEntity {

    public Integer upgradePackId;
    public String terminalId;

    @Transient
    public String cabinetId;
    public String cabinetName;
    public Integer agentId;
    public String agentName;
    public String cabinetAddress;
    public String cabinetVersion;
    public Integer cabinetIsOnline;

    public String terminalVersion;
    public Integer terminalIsOnline;

    public String cabinetChargerVersions;
}
