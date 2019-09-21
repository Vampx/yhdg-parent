package cn.com.yusong.yhdg.appserver.service.basic;

import cn.com.yusong.yhdg.appserver.BaseJunit4Test;
import cn.com.yusong.yhdg.common.domain.basic.*;
import org.apache.commons.lang.time.DateUtils;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

public class LaxinRecordServiceTest extends BaseJunit4Test {
    @Autowired
    LaxinRecordService laxinRecordService;

    @Test
    public void findList() {
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

        assertEquals(1, laxinRecordService.findList(laxin.getId(), LaxinRecord.Status.WAIT.getValue(), 0, 100).size());
    }

    @Test
    public void totalMoneyByTransferTime() {
        Partner partner = newPartner();
        insertPartner(partner);
        Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        Laxin laxin = newLaxin(partner.getId(), agent.getId());
        insertLaxin(laxin);

        Customer customer = newCustomer(partner.getId());
        insertCustomer(customer);

        LaxinRecord laxinRecord = newLaxinRecord(agent.getId(), laxin.getId(), customer.getId());
        laxinRecord.setLaxinMoney(100);
        laxinRecord.setStatus(LaxinRecord.Status.SUCCESS.getValue());
        laxinRecord.setTransferTime(new Date());
        insertLaxinRecord(laxinRecord);

        Date beginTime = DateUtils.addDays(new Date(), -1);
        Date endTime = DateUtils.addDays(new Date(), 1);
        assertEquals(100, laxinRecordService.totalMoneyByTransferTime(laxin.getId(), LaxinRecord.Status.SUCCESS.getValue(), beginTime, endTime));
    }

    @Test
    public void totalCountByTransferTime() {
        Partner partner = newPartner();
        insertPartner(partner);
        Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        Laxin laxin = newLaxin(partner.getId(), agent.getId());
        insertLaxin(laxin);

        Customer customer = newCustomer(partner.getId());
        insertCustomer(customer);

        LaxinRecord laxinRecord = newLaxinRecord(agent.getId(), laxin.getId(), customer.getId());
        laxinRecord.setLaxinMoney(100);
        laxinRecord.setStatus(LaxinRecord.Status.SUCCESS.getValue());
        laxinRecord.setTransferTime(new Date());
        insertLaxinRecord(laxinRecord);

        Date beginTime = DateUtils.addDays(new Date(), -1);
        Date endTime = DateUtils.addDays(new Date(), 1);
        assertEquals(1, laxinRecordService.totalCountByTransferTime(laxin.getId(), LaxinRecord.Status.SUCCESS.getValue(), beginTime, endTime));
    }

    @Test
    public void totalMoneyByStatus() {
        Partner partner = newPartner();
        insertPartner(partner);
        Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        Laxin laxin = newLaxin(partner.getId(), agent.getId());
        insertLaxin(laxin);

        Customer customer = newCustomer(partner.getId());
        insertCustomer(customer);

        LaxinRecord laxinRecord = newLaxinRecord(agent.getId(), laxin.getId(), customer.getId());
        laxinRecord.setLaxinMoney(100);
        laxinRecord.setStatus(LaxinRecord.Status.SUCCESS.getValue());
        laxinRecord.setTransferTime(new Date());
        insertLaxinRecord(laxinRecord);

        assertEquals(100, laxinRecordService.totalMoneyByStatus(laxin.getId(), new int[]{LaxinRecord.Status.SUCCESS.getValue()}));
    }
}
