package cn.com.yusong.yhdg.common.domain.hdg;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/*
* 设备配置历史
* */
@Setter
@Getter
public class CabinetConfigHistory{
    String cabinetId; /*设备id*/
    String statsDate; /*统计日期*/
    String cabinetName; /*设备名称*/
    Integer agentId; /*运营商id*/
    String agentName; /*运营商名称*/
    Date createTime;

    public static String getSuffix(String statsDate) {
        return statsDate.replace("-", "").substring(0, 6);
    }

    public String getSuffix() {
        return getSuffix(statsDate);
    }
}
