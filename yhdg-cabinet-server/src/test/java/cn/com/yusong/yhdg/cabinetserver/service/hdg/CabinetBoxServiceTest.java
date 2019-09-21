package cn.com.yusong.yhdg.cabinetserver.service.hdg;

import cn.com.yusong.yhdg.cabinetserver.BaseJunit4Test;
import cn.com.yusong.yhdg.common.domain.hdg.*;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class CabinetBoxServiceTest extends BaseJunit4Test {
    @Autowired
    CabinetBoxService cabinetBoxService;

    @Test
    public void findBoxBatteryList() {
        char[] str = new StringBuilder(String.format("%0" + BatteryReportLog.ChargeStatusName.values().length + "d", Long.parseLong(Integer.toBinaryString(32)))).reverse().toString().toCharArray();
        System.out.println(str);
    }
}
