package cn.com.yusong.yhdg.serviceserver.service.basic;

import cn.com.yusong.yhdg.common.domain.basic.*;
import cn.com.yusong.yhdg.serviceserver.BaseJunit4Test;
import cn.com.yusong.yhdg.serviceserver.config.AppConfig;
import cn.com.yusong.yhdg.serviceserver.tool.mppay.MpPayUtils;
import org.apache.http.ProtocolVersion;
import org.apache.http.StatusLine;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;

public class LaxinRecordServiceTest extends BaseJunit4Test {
    @Autowired
    AppConfig config;
    @Autowired
    LaxinRecordService laxinRecordService;

    @Test
    public void transfer() {
        config.appDir = new File(this.getClass().getResource("/").getPath());

        Partner partner = newPartner();
        insertPartner(partner);
        Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        Laxin laxin = newLaxin(partner.getId(), agent.getId());
        laxin.setMobile("13777351251");
        insertLaxin(laxin);

        Customer customer = newCustomer(partner.getId());
        customer.setMobile("13777351251");
        customer.setMpOpenId("111");
        customer.setBalance(0);
        insertCustomer(customer);

        LaxinRecord laxinRecord = newLaxinRecord(agent.getId(), laxin.getId(), customer.getId());
        laxinRecord.setLaxinMobile("13777351251");
        laxinRecord.setStatus(LaxinRecord.Status.TRANSFER.getValue());
        laxinRecord.setLaxinMoney(100);
        insertLaxinRecord(laxinRecord);

        laxinRecordService.transfer();

        assertEquals(100, jdbcTemplate.queryForInt("select balance from bas_customer where id = ?", customer.getId()));
    }
}
