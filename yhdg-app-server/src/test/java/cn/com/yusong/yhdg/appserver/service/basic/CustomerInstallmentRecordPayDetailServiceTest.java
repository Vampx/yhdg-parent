package cn.com.yusong.yhdg.appserver.service.basic;

import cn.com.yusong.yhdg.appserver.BaseJunit4Test;
import cn.com.yusong.yhdg.appserver.entity.result.RestResult;
import cn.com.yusong.yhdg.common.constant.ConstEnum;
import cn.com.yusong.yhdg.common.domain.basic.*;
import cn.com.yusong.yhdg.common.domain.hdg.*;
import cn.com.yusong.yhdg.common.domain.yms.Playlist;
import cn.com.yusong.yhdg.common.domain.yms.Terminal;
import cn.com.yusong.yhdg.common.domain.yms.TerminalStrategy;
import org.apache.commons.lang.time.DateUtils;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;


public class CustomerInstallmentRecordPayDetailServiceTest extends BaseJunit4Test {


    @Autowired
    CustomerInstallmentRecordPayDetailService customerInstallmentRecordPayDetailService;

    @Test
    public void findList() {
        Partner partner = newPartner();
        insertPartner(partner);
        Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        SystemBatteryType systemBatteryType = newSystemBatteryType();
        insertSystemBatteryType(systemBatteryType);

        Customer customer = newCustomer(partner.getId());
        customer.setBatteryType(systemBatteryType.getId());
        customer.setAgentId(agent.getId());
        insertCustomer(customer);

        CustomerInstallmentRecord customerInstallmentRecord = newCustomerInstallmentRecord(agent.getPartnerId(),customer.getId(), agent.getId());
        insertCustomerInstallmentRecord(customerInstallmentRecord);

        CustomerInstallmentRecordPayDetail customerInstallmentRecordPayDetail = newCustomerInstallmentRecordPayDetail(customerInstallmentRecord.getId(), agent.getPartnerId(),customer.getId(), agent.getId());
        customerInstallmentRecordPayDetail.setCategory(Battery.Category.EXCHANGE.getValue());
        insertCustomerInstallmentRecordPayDetail(customerInstallmentRecordPayDetail);

        assertEquals(1,customerInstallmentRecordPayDetailService.findList(customerInstallmentRecord.getId(), customer.getId(), Battery.Category.EXCHANGE.getValue()).size());
    }

    @Test
    public void findCountByCustomerId() {
        Partner partner = newPartner();
        insertPartner(partner);
        Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        SystemBatteryType systemBatteryType = newSystemBatteryType();
        insertSystemBatteryType(systemBatteryType);

        Customer customer = newCustomer(partner.getId());
        customer.setBatteryType(systemBatteryType.getId());
        customer.setAgentId(agent.getId());
        insertCustomer(customer);

        CustomerInstallmentRecord customerInstallmentRecord = newCustomerInstallmentRecord(agent.getPartnerId(),customer.getId(), agent.getId());
        insertCustomerInstallmentRecord(customerInstallmentRecord);

        CustomerInstallmentRecordPayDetail customerInstallmentRecordPayDetail = newCustomerInstallmentRecordPayDetail(customerInstallmentRecord.getId(), agent.getPartnerId(),customer.getId(), agent.getId());
        customerInstallmentRecordPayDetail.setStatus(CustomerInstallmentRecordPayDetail.Status.WAIT_PAY.getValue());
        customerInstallmentRecordPayDetail.setExpireTime(DateUtils.addDays(new Date(), -1));
        insertCustomerInstallmentRecordPayDetail(customerInstallmentRecordPayDetail);

        assertEquals(1,customerInstallmentRecordPayDetailService.findCountByCustomerId(customer.getId(), CustomerInstallmentRecordPayDetail.Status.WAIT_PAY.getValue(), ConstEnum.Category.EXCHANGE.getValue(), new Date()));
    }

    @Test
    public void findCountByCustomerId1() throws Exception {
        Partner partner = newPartner();
        insertPartner(partner);
        Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        SystemBatteryType systemBatteryType = newSystemBatteryType();
        insertSystemBatteryType(systemBatteryType);

        Customer customer = newCustomer(partner.getId());
        customer.setBatteryType(systemBatteryType.getId());
        customer.setAgentId(agent.getId());
        insertCustomer(customer);

        CustomerInstallmentRecord customerInstallmentRecord = newCustomerInstallmentRecord(agent.getPartnerId(),customer.getId(), agent.getId());
        customerInstallmentRecord.setStatus(CustomerInstallmentRecord.Status.PAY_ING.getValue());
        insertCustomerInstallmentRecord(customerInstallmentRecord);

        CustomerInstallmentRecordPayDetail customerInstallmentRecordPayDetail = newCustomerInstallmentRecordPayDetail(customerInstallmentRecord.getId(), agent.getPartnerId(),customer.getId(), agent.getId());
        customerInstallmentRecordPayDetail.setStatus(CustomerInstallmentRecordPayDetail.Status.WAIT_PAY.getValue());
        customerInstallmentRecordPayDetail.setCategory(3);
        customerInstallmentRecordPayDetail.setExpireTime(DateUtils.addDays(new Date(), -1));
        customerInstallmentRecordPayDetail.setNum(5);
        customerInstallmentRecordPayDetail.setForegiftMoney(10000);
        customerInstallmentRecordPayDetail.setPacketMoney(10000);
        customerInstallmentRecordPayDetail.setInsuranceMoney(10000);
        insertCustomerInstallmentRecordPayDetail(customerInstallmentRecordPayDetail);

        assertEquals(1,customerInstallmentRecordPayDetailService.findCountByCustomerId(customer.getId(), CustomerInstallmentRecordPayDetail.Status.WAIT_PAY.getValue(), ConstEnum.Category.VEHICLE.getValue(), new Date()));

    }

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

        CustomerInstallmentRecordPayDetail customerInstallmentRecordPayDetail = newCustomerInstallmentRecordPayDetail(customerInstallmentRecord.getId(), agent.getPartnerId(),customer.getId(), agent.getId());
        customerInstallmentRecordPayDetail.setMoney(100);
        insertCustomerInstallmentRecordPayDetail(customerInstallmentRecordPayDetail);

        long [] id =new long[] {customerInstallmentRecordPayDetail.getId()};
        RestResult restResult = customerInstallmentRecordPayDetailService.payByThird(customer.getId(), id, ConstEnum.PayType.WEIXIN_MP);
        assertEquals(0, restResult.getCode());

        assertEquals(100, jdbcTemplate.queryForInt("select money from bas_weixinmp_pay_order where source_id = ?", customerInstallmentRecordPayDetail.getId()));
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
        customerInstallmentRecord.setTotalMoney(100);
        customerInstallmentRecord.setStatus(CustomerInstallmentRecord.Status.PAY_ING.getValue());
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

        CustomerInstallmentRecordPayDetail customerInstallmentRecordPayDetail = newCustomerInstallmentRecordPayDetail(customerInstallmentRecord.getId(), agent.getPartnerId(),customer.getId(), agent.getId());
        customerInstallmentRecordPayDetail.setMoney(100);
        insertCustomerInstallmentRecordPayDetail(customerInstallmentRecordPayDetail);

        long [] id =new long[] {customerInstallmentRecordPayDetail.getId()};
        RestResult restResult  = customerInstallmentRecordPayDetailService.payBalance(customer.getId(), id);
        assertEquals(0, restResult.getCode());

        assertEquals(10000L - customerInstallmentRecordPayDetail.getMoney(), jdbcTemplate.queryForInt("select balance from bas_customer where id = ?", customer.getId()));
        assertEquals(CustomerInstallmentRecord.Status.PAY_OK.getValue(), jdbcTemplate.queryForInt("select status from bas_customer_installment_record where id = ?", customerInstallmentRecord.getId()));

    }

}
