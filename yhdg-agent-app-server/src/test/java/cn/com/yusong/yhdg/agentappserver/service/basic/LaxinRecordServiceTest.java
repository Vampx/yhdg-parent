package cn.com.yusong.yhdg.agentappserver.service.basic;

import cn.com.yusong.yhdg.agentappserver.BaseJunit4Test;
import cn.com.yusong.yhdg.common.domain.basic.Agent;
import cn.com.yusong.yhdg.common.domain.basic.Partner;
import cn.com.yusong.yhdg.common.domain.basic.Customer;
import cn.com.yusong.yhdg.common.domain.basic.Laxin;
import cn.com.yusong.yhdg.common.domain.basic.LaxinRecord;
import org.apache.commons.lang.time.DateUtils;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

public class LaxinRecordServiceTest extends BaseJunit4Test {
    @Autowired
    LaxinRecordService laxinRecordService;

    @Test
    public void find() {
        Partner partner = newPartner();
        insertPartner(partner);
        Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        Laxin laxin = newLaxin(partner.getId(), agent.getId());
        insertLaxin(laxin);

        Customer customer = newCustomer(partner.getId());
        insertCustomer(customer);

        LaxinRecord laxinRecord = newLaxinRecord(agent.getId(), laxin.getId(), customer.getId());
        insertLaxinRecord(laxinRecord);

        assertNotNull(laxinRecordService.find(laxinRecord.getId()));
    }

    @Test
    public void findByStatus() {
        Partner partner = newPartner();
        insertPartner(partner);
        Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        Laxin laxin = newLaxin(partner.getId(), agent.getId());
        insertLaxin(laxin);

        Customer customer = newCustomer(partner.getId());
        insertCustomer(customer);

        LaxinRecord laxinRecord = newLaxinRecord(agent.getId(), laxin.getId(), customer.getId());
        laxinRecord.setLaxinMobile("13777351251");
        laxinRecord.setStatus(LaxinRecord.Status.WAIT.getValue());
        insertLaxinRecord(laxinRecord);

        assertEquals(1, laxinRecordService.findByStatus(agent.getId(), LaxinRecord.Status.WAIT.getValue(), "13777351251", 0, 100).size());
    }

    @Test
    public void totalMoneyByPayTime() {
        Partner partner = newPartner();
        insertPartner(partner);
        Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        Laxin laxin = newLaxin(partner.getId(), agent.getId());
        insertLaxin(laxin);

        Customer customer = newCustomer(partner.getId());
        insertCustomer(customer);

        LaxinRecord laxinRecord = newLaxinRecord(agent.getId(), laxin.getId(), customer.getId());
        laxinRecord.setStatus(LaxinRecord.Status.TRANSFER.getValue());
        laxinRecord.setLaxinMoney(100);
        laxinRecord.setPayTime(new Date());
        insertLaxinRecord(laxinRecord);

        Date beginTime = DateUtils.addDays(new Date(), -1);
        Date endTime = DateUtils.addDays(new Date(), 1);
        assertEquals(100, laxinRecordService.totalMoneyByPayTime(agent.getId(), beginTime, endTime));
    }

    @Test
    public void totalCountByCreateTime() {
        Partner partner = newPartner();
        insertPartner(partner);
        Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        Laxin laxin = newLaxin(partner.getId(), agent.getId());
        insertLaxin(laxin);

        Customer customer = newCustomer(partner.getId());
        insertCustomer(customer);

        LaxinRecord laxinRecord = newLaxinRecord(agent.getId(), laxin.getId(), customer.getId());
        laxinRecord.setStatus(LaxinRecord.Status.TRANSFER.getValue());
        laxinRecord.setLaxinMoney(100);
        laxinRecord.setCreateTime(new Date());
        insertLaxinRecord(laxinRecord);

        Date beginTime = DateUtils.addDays(new Date(), -1);
        Date endTime = DateUtils.addDays(new Date(), 1);
        assertEquals(1, laxinRecordService.totalCountByCreateTime(agent.getId(), beginTime, endTime));
    }

}
