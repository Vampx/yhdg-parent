package cn.com.yusong.yhdg.agentserver.service.basic;

import cn.com.yusong.yhdg.agentserver.service.BaseJunit4Test;
import cn.com.yusong.yhdg.common.domain.basic.*;
import cn.com.yusong.yhdg.common.domain.hdg.Shop;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.*;

public class BalanceRecordServiceTest extends BaseJunit4Test {
    @Autowired
    private BalanceRecordService service;

    private BalanceRecord balanceRecord;

    @Before
    public void setUp() throws Exception {
        Partner partner = newPartner();
        insertPartner(partner);

        Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        Customer customer = newCustomer(partner.getId());
        insertCustomer(customer);

        Shop shop = newShop(agent.getId());
        insertShop(shop);

        PlatformAccount platformAccount = newPlatformAccount(partner.getId());
        insertPlatformAccount(platformAccount);

        balanceRecord = newBalanceRecord(partner.getId(), agent.getId(), shop.getId(), null);
    }

    @Test
    public void find() {
        insertBalanceRecord(balanceRecord);

        assertNotNull(service.find(balanceRecord.getId()));
    }

    @Test
    public void findPage() {
        insertBalanceRecord(balanceRecord);

        assertTrue(1 == service.findPage(balanceRecord).getTotalItems());
        assertTrue(1 == service.findPage(balanceRecord).getResult().size());
    }

    //运营商确认
    @Test
    public void confirm_1() {
        balanceRecord.setMoney(100);
        balanceRecord.setPacketPeriodMoney(100);
        balanceRecord.setExchangeMoney(100);
        balanceRecord.setInsuranceMoney(100);
        balanceRecord.setProvinceIncome(100);
        balanceRecord.setCityIncome(100);
        balanceRecord.setForegiftRemainMoney(100);
        balanceRecord.setRefundPacketPeriodMoney(100);
        balanceRecord.setRefundInsuranceMoney(100);
        balanceRecord.setDeductionTicketMoney(100);
        insertBalanceRecord(balanceRecord);

        Long[] ids = {balanceRecord.getId()};
        assertTrue(service.confirm(ids, "testPerson").isSuccess());

        assertEquals(100, jdbcTemplate.queryForInt("select balance from bas_agent where id = ?", balanceRecord.getAgentId()));
        assertEquals(BalanceRecord.Status.CONFIRM_OK.getValue(), jdbcTemplate.queryForInt("select status from bas_balance_record where id = ?", balanceRecord.getId()));
        assertEquals(100, jdbcTemplate.queryForInt("select money from bas_agent_in_out_money WHERE biz_type = ?", AgentInOutMoney.BizType.IN_PACKET_PERIOD_RATIO.getValue()));
        assertEquals(100, jdbcTemplate.queryForInt("select money from bas_agent_in_out_money WHERE biz_type = ?", AgentInOutMoney.BizType.IN_EXCHANGR_RATIO.getValue()));
        assertEquals(100, jdbcTemplate.queryForInt("select money from bas_agent_in_out_money WHERE biz_type = ?", AgentInOutMoney.BizType.IN_INSURANCE.getValue()));
//        assertEquals(100, jdbcTemplate.queryForInt("select money from bas_agent_in_out_money WHERE biz_type = ?", AgentInOutMoney.BizType.IN_PROVINCE.getValue()));
//        assertEquals(100, jdbcTemplate.queryForInt("select money from bas_agent_in_out_money WHERE biz_type = ?", AgentInOutMoney.BizType.IN_CITY.getValue()));
        assertEquals(100, jdbcTemplate.queryForInt("select money from bas_agent_in_out_money WHERE biz_type = ?", AgentInOutMoney.BizType.IN_FOREGIFT_REMAIN.getValue()));
        assertEquals(-100, jdbcTemplate.queryForInt("select money from bas_agent_in_out_money WHERE biz_type = ?", AgentInOutMoney.BizType.OUT_PACKET_PERIOD_RATIO.getValue()));
        assertEquals(-100, jdbcTemplate.queryForInt("select money from bas_agent_in_out_money WHERE biz_type = ?", AgentInOutMoney.BizType.OUT_INSURANCE.getValue()));
        assertEquals(-100, jdbcTemplate.queryForInt("select money from bas_agent_in_out_money WHERE biz_type = ?", AgentInOutMoney.BizType.OUT_DEDUCTION_TICKET.getValue()));
    }

    //门店确认
    @Test
    public void confirm_2() {
        balanceRecord.setMoney(100);
        balanceRecord.setPacketPeriodMoney(100);
        balanceRecord.setExchangeMoney(100);
        balanceRecord.setRefundPacketPeriodMoney(100);
        balanceRecord.setBizType(BalanceRecord.BizType.SHOP.getValue());
        insertBalanceRecord(balanceRecord);

        Long[] ids = {balanceRecord.getId()};
        assertTrue(service.confirm(ids, "testPerson").isSuccess());

        assertEquals(100 + 10, jdbcTemplate.queryForInt("select balance from hdg_shop where id = ?", balanceRecord.getShopId()));
        assertEquals(BalanceRecord.Status.CONFIRM_OK.getValue(), jdbcTemplate.queryForInt("select status from bas_balance_record where id = ?", balanceRecord.getId()));
        assertEquals(100, jdbcTemplate.queryForInt("select money from bas_shop_in_out_money WHERE biz_type = ?", ShopInOutMoney.BizType.PACKET_PERIOD_RATIO.getValue()));
        assertEquals(100, jdbcTemplate.queryForInt("select money from bas_shop_in_out_money WHERE biz_type = ?", ShopInOutMoney.BizType.EXCHANGR_RATIO.getValue()));
        assertEquals(-100, jdbcTemplate.queryForInt("select money from bas_shop_in_out_money WHERE biz_type = ?", ShopInOutMoney.BizType.REFUND_PACKET_PERIOD_RATIO.getValue()));
    }

    //门店确认
    @Test
    public void confirm_3() {
        balanceRecord.setMoney(100);
        balanceRecord.setPacketPeriodMoney(100);
        balanceRecord.setExchangeMoney(100);
        balanceRecord.setRefundPacketPeriodMoney(100);
        balanceRecord.setBizType(BalanceRecord.BizType.PARTNER.getValue());
        insertBalanceRecord(balanceRecord);

        Long[] ids = {balanceRecord.getId()};
        assertTrue(service.confirm(ids, "testPerson").isSuccess());

        assertEquals(100, jdbcTemplate.queryForInt("select balance from bas_platform_account where id = ?", balanceRecord.getPartnerId()));
        assertEquals(BalanceRecord.Status.CONFIRM_OK.getValue(), jdbcTemplate.queryForInt("select status from bas_balance_record where id = ?", balanceRecord.getId()));
        assertEquals(100, jdbcTemplate.queryForInt("select money from bas_platform_account_in_out_money WHERE biz_type = ?", PlatformAccountInOutMoney.BizType.IN_PACKET_PERIOD_RATIO.getValue()));
        assertEquals(100, jdbcTemplate.queryForInt("select money from bas_platform_account_in_out_money WHERE biz_type = ?", PlatformAccountInOutMoney.BizType.IN_EXCHANGR_RATIO.getValue()));
        assertEquals(-100, jdbcTemplate.queryForInt("select money from bas_platform_account_in_out_money WHERE biz_type = ?", PlatformAccountInOutMoney.BizType.OUT_PACKET_PERIOD_RATIO.getValue()));
    }

    @Test
    public void agentConfirm() {
        //内部方法不做测试
    }

    @Test
    public void shopConfirm() {
        //内部方法不做测试
    }

    @Test
    public void partnerConfirm() {
        //内部方法不做测试
    }
}