package cn.com.yusong.yhdg.appserver.service.basic;

import cn.com.yusong.yhdg.appserver.BaseJunit4Test;
import cn.com.yusong.yhdg.appserver.entity.result.RestResult;
import cn.com.yusong.yhdg.common.constant.ConstEnum;
import cn.com.yusong.yhdg.common.domain.basic.*;
import cn.com.yusong.yhdg.common.domain.hdg.*;
import cn.com.yusong.yhdg.common.domain.yms.Playlist;
import cn.com.yusong.yhdg.common.domain.yms.Terminal;
import cn.com.yusong.yhdg.common.domain.yms.TerminalStrategy;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;


public class CustomerInstallmentRecordServiceTest extends BaseJunit4Test {

    @Autowired
    CustomerInstallmentRecordService customerInstallmentRecordService;

    @Test
    public void payByThird() {
        Partner partner = newPartner();
        insertPartner(partner);
        Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        Customer customer = newCustomer(partner.getId());
        customer.setBatteryType(1);
        customer.setBalance(10000);
        insertCustomer(customer);

        SystemBatteryType systemBatteryType = newSystemBatteryType();
        insertSystemBatteryType(systemBatteryType);
        Battery battery = newBattery(agent.getId(), systemBatteryType.getId());
        insertBattery(battery);

        AgentBatteryType agentBatteryType = newAgentBatteryType(agent.getId(), battery.getType());
        insertAgentBatteryType(agentBatteryType);

        Insurance insurance = newInsurance(agent.getId(), agentBatteryType.getBatteryType());
        insertInsurance(insurance);

        ExchangeBatteryForegift exchangeBatteryForegift = newExchangeBatteryForegift(agent.getId(), systemBatteryType.getId());
        exchangeBatteryForegift.setMoney(100);
        insertExchangeBatteryForegift(exchangeBatteryForegift);

        PacketPeriodPrice packetPeriodPrice = newPacketPeriodPrice(agent.getId(), exchangeBatteryForegift.getId());
        insertPacketPeriodPrice(packetPeriodPrice);

        Shop shop = newShop(agent.getId());
        insertShop(shop);

        Playlist playlist = newPlaylist(agent.getId());
        insertPlaylist(playlist);

        TerminalStrategy terminalStrategy = newTerminalStrategy(agent.getId());
        insertTerminalStrategy(terminalStrategy);

        Terminal terminal = newTerminal(agent.getId(), terminalStrategy.getId(), playlist.getId());
        insertTerminal(terminal);

        Cabinet cabinet = newCabinet(agent.getId(),terminal.getId());
        insertCabinet(cabinet);

        ExchangeInstallmentSetting exchangeInstallmentSetting = newExchangeInstallmentSetting(agent.getId());
        insertExchangeInstallmentSetting(exchangeInstallmentSetting);

        ExchangeInstallmentDetail exchangeInstallmentDetail = newExchangeInstallmentDetail(exchangeInstallmentSetting.getId());
        exchangeInstallmentDetail.setNum(1);
        insetExchangeInstallmentDetail(exchangeInstallmentDetail);

        CustomerInstallmentRecord customerInstallmentRecord = newCustomerInstallmentRecord(agent.getPartnerId(),customer.getId(), agent.getId());
        insertCustomerInstallmentRecord(customerInstallmentRecord);

        RestResult restResult = customerInstallmentRecordService.payByThird(customer.getId(), battery.getId(), customerInstallmentRecord.getId(), agent.getId(), ConstEnum.PayType.WEIXIN_MP);
        assertEquals(0, restResult.getCode());

        assertEquals(1000, jdbcTemplate.queryForInt("select money from bas_weixinmp_pay_order where source_id = ?", customerInstallmentRecord.getId()));
    }


    @Test
    public void payBalance() {
        Partner partner = newPartner();
        insertPartner(partner);
        Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        Customer customer = newCustomer(partner.getId());
        customer.setBatteryType(1);
        customer.setBalance(10000);
        insertCustomer(customer);

        SystemBatteryType systemBatteryType = newSystemBatteryType();
        insertSystemBatteryType(systemBatteryType);
        Battery battery = newBattery(agent.getId(), systemBatteryType.getId());
        insertBattery(battery);

        AgentBatteryType agentBatteryType = newAgentBatteryType(agent.getId(), battery.getType());
        insertAgentBatteryType(agentBatteryType);

        Insurance insurance = newInsurance(agent.getId(), agentBatteryType.getBatteryType());
        insertInsurance(insurance);

        ExchangeBatteryForegift exchangeBatteryForegift = newExchangeBatteryForegift(agent.getId(), systemBatteryType.getId());
        exchangeBatteryForegift.setMoney(100);
        insertExchangeBatteryForegift(exchangeBatteryForegift);

        PacketPeriodPrice packetPeriodPrice = newPacketPeriodPrice(agent.getId(), exchangeBatteryForegift.getId());
        insertPacketPeriodPrice(packetPeriodPrice);

        Shop shop = newShop(agent.getId());
        insertShop(shop);

        Playlist playlist = newPlaylist(agent.getId());
        insertPlaylist(playlist);

        TerminalStrategy terminalStrategy = newTerminalStrategy(agent.getId());
        insertTerminalStrategy(terminalStrategy);

        Terminal terminal = newTerminal(agent.getId(), terminalStrategy.getId(), playlist.getId());
        insertTerminal(terminal);

        Cabinet cabinet = newCabinet(agent.getId(),terminal.getId());
        insertCabinet(cabinet);

        ExchangeInstallmentSetting exchangeInstallmentSetting = newExchangeInstallmentSetting(agent.getId());
        insertExchangeInstallmentSetting(exchangeInstallmentSetting);

        ExchangeInstallmentDetail exchangeInstallmentDetail = newExchangeInstallmentDetail(exchangeInstallmentSetting.getId());
        exchangeInstallmentDetail.setNum(1);
        insetExchangeInstallmentDetail(exchangeInstallmentDetail);

        CustomerInstallmentRecord customerInstallmentRecord = newCustomerInstallmentRecord(agent.getPartnerId(),customer.getId(), agent.getId());
        insertCustomerInstallmentRecord(customerInstallmentRecord);

        CustomerForegiftOrder customerForegiftOrder = newCustomerForegiftOrder(partner.getId(), customer.getId(), agent.getId());
        insertCustomerForegiftOrder(customerForegiftOrder);

        CustomerInstallmentRecordOrderDetail customerForegiftOrderDetail = newCustomerInstallmentRecordOrderDetail(customerInstallmentRecord.getId(), OrderId.OrderIdType.CUSTOMER_FORGIFT_ORDER.getValue(), customerForegiftOrder.getId());
        customerForegiftOrderDetail.setNum(1);
        insertCustomerInstallmentRecordOrderDetail(customerForegiftOrderDetail);

        PacketPeriodOrder packetPeriodOrder = newPacketPeriodOrder(partner.getId(), customer.getId(), systemBatteryType.getId(), agent.getId());
        insertPacketPeriodOrder(packetPeriodOrder);

        CustomerInstallmentRecordOrderDetail packetPeriodOrderOrderDetail = newCustomerInstallmentRecordOrderDetail(customerInstallmentRecord.getId(), OrderId.OrderIdType.PACKET_PERIOD_ORDER.getValue(), packetPeriodOrder.getId());
        packetPeriodOrderOrderDetail.setNum(2);
        insertCustomerInstallmentRecordOrderDetail(packetPeriodOrderOrderDetail);

        InsuranceOrder insuranceOrder = newInsuranceOrder(customer);
        insertInsuranceOrder(insuranceOrder);

        CustomerInstallmentRecordOrderDetail insuranceOrderOrderDetail = newCustomerInstallmentRecordOrderDetail(customerInstallmentRecord.getId(), OrderId.OrderIdType.INSURANCE_ORDER.getValue(), insuranceOrder.getId());
        insuranceOrderOrderDetail.setNum(3);
        insertCustomerInstallmentRecordOrderDetail(insuranceOrderOrderDetail);

        RestResult restResult = customerInstallmentRecordService.payBalance(customer.getId(), battery.getId(), agent.getId(), customerInstallmentRecord.getId());
        assertEquals(0, restResult.getCode());

//        assertEquals(10000L - customerInstallmentRecord.getFirstMoney(), jdbcTemplate.queryForInt("select balance from bas_customer where id = ?", customer.getId()));
        assertEquals(CustomerInstallmentRecord.Status.PAY_ING.getValue(), jdbcTemplate.queryForInt("select status from bas_customer_installment_record where id = ?", customerInstallmentRecord.getId()));

        assertEquals(PacketPeriodOrder.Status.NOT_USE.getValue(), jdbcTemplate.queryForInt("select status from hdg_packet_period_order where id = ?", packetPeriodOrderOrderDetail.getSourceId()));
        assertEquals(CustomerForegiftOrder.Status.PAY_OK.getValue(), jdbcTemplate.queryForInt("select status from bas_customer_foregift_order where id = ?", customerForegiftOrderDetail.getSourceId()));
        assertEquals(InsuranceOrder.Status.PAID.getValue(), jdbcTemplate.queryForInt("select status from hdg_insurance_order where id = ?", insuranceOrderOrderDetail.getSourceId()));

    }
}
