package cn.com.yusong.yhdg.serviceserver.service.hdg;

import cn.com.yusong.yhdg.common.constant.ConstEnum;
import cn.com.yusong.yhdg.common.domain.basic.*;
import cn.com.yusong.yhdg.common.domain.hdg.*;
import cn.com.yusong.yhdg.common.domain.yms.Playlist;
import cn.com.yusong.yhdg.common.domain.yms.Terminal;
import cn.com.yusong.yhdg.common.domain.yms.TerminalStrategy;
import cn.com.yusong.yhdg.serviceserver.BaseJunit4Test;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Calendar;

public class BatteryServiceTest extends BaseJunit4Test {
    @Autowired
    BatteryService batteryService;


    @Test
    public void findOnline() {
        Partner partner = newPartner(); insertPartner(partner); Agent agent = newAgent(partner.getId());
        insertAgent(agent);
        Battery battery = newBattery(agent.getId(), 1);
        battery.setIsOnline(ConstEnum.Flag.TRUE.getValue());
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MINUTE, -50);
        battery.setReportTime(calendar.getTime());
        insertBattery(battery);
        batteryService.refreshOnlineStatus();
        assertEquals(0, jdbcTemplate.queryForInt("select is_online from hdg_battery where id = ?", battery.getId()));
    }

}
