package cn.com.yusong.yhdg.serviceserver.service.basic;

import cn.com.yusong.yhdg.common.constant.ConstEnum;
import cn.com.yusong.yhdg.common.domain.basic.*;
import cn.com.yusong.yhdg.common.domain.hdg.*;
import cn.com.yusong.yhdg.common.domain.yms.Playlist;
import cn.com.yusong.yhdg.common.domain.yms.Terminal;
import cn.com.yusong.yhdg.common.domain.yms.TerminalStrategy;
import cn.com.yusong.yhdg.common.domain.zd.RentForegiftOrder;
import cn.com.yusong.yhdg.common.domain.zd.RentPeriodOrder;
import cn.com.yusong.yhdg.serviceserver.BaseJunit4Test;
import cn.com.yusong.yhdg.serviceserver.config.AppConfig;
import cn.com.yusong.yhdg.serviceserver.persistence.basic.PushMetaDataMapper;
import cn.com.yusong.yhdg.serviceserver.tool.voice.AliyunVoiceClient;
import org.apache.commons.lang.time.DateUtils;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;

public class InsertPushMessageServiceTest extends BaseJunit4Test {
    @Autowired
    InsertPushMessageService insertPushMessageService;
    @Autowired
    PushMetaDataMapper pushMetaDataMapper;

    @Test
    public void insertCustomerDepositOrderPushMessage() {
        Partner partner = newPartner();
        insertPartner(partner);

        Weixinmp weixinmp = newWeixinmp(partner.getId());
        insertWeixinmp(weixinmp);

        WeixinmpOpenId weixinmpOpenId = newWeixinmpOpenId(weixinmp.getId());
        insertWeixinmpOpenId(weixinmpOpenId);

        Agent agent = newAgent(partner.getId());
        agent.setWeixinmpId(weixinmp.getId());
        insertAgent(agent);

        Customer customer = newCustomer(partner.getId());
        customer.setAgentId(agent.getId());
        customer.setMpOpenId(weixinmpOpenId.getOpenId());
        insertCustomer(customer);



        Cabinet cabinet = newCabinet(agent.getId(), null);
        insertCabinet(cabinet);

        CustomerDepositOrder entity = newCustomerDepositOrder(partner.getId(), customer.getId());
        entity.setStatus(CustomerDepositOrder.Status.OK.getValue());
        insertCustomerDepositOrder(entity);

        PushMetaData pushMetaData = newPushMetaData(entity.getId(), PushMessage.SourceType.CUSTOMER_DEPOSIT_SUCCESS.getValue());
        insertPushMetaData(pushMetaData);

        insertPushMessageService.insertCustomerDepositOrderPushMessage(pushMetaData);

        assertEquals(1, jdbcTemplate.queryForInt("select count(1) from bas_weixinmp_template_message where open_id = ?", weixinmpOpenId.getSecondOpenId()));

    }

    @Test
    public void insertCustomerForegiftOrderPushMessage() {
        Partner partner = newPartner();
        insertPartner(partner);

        Weixinmp weixinmp = newWeixinmp(partner.getId());
        insertWeixinmp(weixinmp);

        WeixinmpOpenId weixinmpOpenId = newWeixinmpOpenId(weixinmp.getId());
        insertWeixinmpOpenId(weixinmpOpenId);

        Agent agent = newAgent(partner.getId());
        agent.setWeixinmpId(weixinmp.getId());
        insertAgent(agent);

        Customer customer = newCustomer(partner.getId());
        customer.setAgentId(agent.getId());
        customer.setMpOpenId(weixinmpOpenId.getOpenId());
        insertCustomer(customer);


        Cabinet cabinet = newCabinet(agent.getId(), null);
        insertCabinet(cabinet);

        CustomerForegiftOrder customerForegiftOrder = newCustomerForegiftOrder(partner.getId(), customer.getId(), agent.getId());
        customerForegiftOrder.setId("111");
        customerForegiftOrder.setStatus(CustomerForegiftOrder.Status.PAY_OK.getValue());
        insertCustomerForegiftOrder(customerForegiftOrder);


        PushMetaData pushMetaData = newPushMetaData(customerForegiftOrder.getId(), PushMessage.SourceType.CUSTOMER_FOREGIFT_PAY_SUCCESS.getValue());
        insertPushMetaData(pushMetaData);

        insertPushMessageService.insertCustomerForegiftOrderPushMessage(pushMetaData);

        assertEquals(1, jdbcTemplate.queryForInt("select count(1) from bas_weixinmp_template_message where open_id = ?", weixinmpOpenId.getSecondOpenId()));
    }

    @Test
    public void insertPacketPeriodOrderPushMessage() {
        Partner partner = newPartner();
        insertPartner(partner);

        Weixinmp weixinmp = newWeixinmp(partner.getId());
        insertWeixinmp(weixinmp);

        WeixinmpOpenId weixinmpOpenId = newWeixinmpOpenId(weixinmp.getId());
        insertWeixinmpOpenId(weixinmpOpenId);

        Agent agent = newAgent(partner.getId());
        agent.setWeixinmpId(weixinmp.getId());
        insertAgent(agent);

        Customer customer = newCustomer(partner.getId());
        customer.setAgentId(agent.getId());
        customer.setMpOpenId(weixinmpOpenId.getOpenId());
        insertCustomer(customer);


        Cabinet cabinet = newCabinet(agent.getId(), null);
        insertCabinet(cabinet);

        PacketPeriodOrder packetPeriodOrder = newPacketPeriodOrder(partner.getId(), customer.getId(), 1, agent.getId());
        packetPeriodOrder.setStatus(PacketPeriodOrder.Status.USED.getValue());
        packetPeriodOrder.setPayTime(new Date());
        packetPeriodOrder.setAgentId(agent.getId());
        packetPeriodOrder.setPayType(ConstEnum.PayType.BALANCE.getValue());
        packetPeriodOrder.setPrice(1000);
        packetPeriodOrder.setMoney(1000);
        packetPeriodOrder.setCabinetId(cabinet.getId());
        insertPacketPeriodOrder(packetPeriodOrder);


        PushMetaData pushMetaData = newPushMetaData(packetPeriodOrder.getId(), PushMessage.SourceType.CUSTOMER_PACKET_PERIOD_ORDER_PAY_SUCCESS.getValue());
        insertPushMetaData(pushMetaData);

        insertPushMessageService.insertPacketPeriodOrderPushMessage(pushMetaData);

        assertEquals(1, jdbcTemplate.queryForInt("select count(1) from bas_weixinmp_template_message where open_id = ?", weixinmpOpenId.getSecondOpenId()));
    }

    @Test
    public void insertCustomerGetCouponTicketPushMessage() {
        Partner partner = newPartner();
        insertPartner(partner);

        Weixinmp weixinmp = newWeixinmp(partner.getId());
        insertWeixinmp(weixinmp);

        WeixinmpOpenId weixinmpOpenId = newWeixinmpOpenId(weixinmp.getId());
        insertWeixinmpOpenId(weixinmpOpenId);

        Agent agent = newAgent(partner.getId());
        agent.setWeixinmpId(weixinmp.getId());
        insertAgent(agent);

        Customer customer = newCustomer(partner.getId());
        customer.setAgentId(agent.getId());
        customer.setMpOpenId(weixinmpOpenId.getOpenId());
        insertCustomer(customer);

        CustomerCouponTicket customerCouponTicket = newCustomerCouponTicket(partner.getId(), agent.getId(), customer.getMobile());;
        customerCouponTicket.setCustomerMobile(customer.getMobile());
        insertCustomerCouponTicket(customerCouponTicket);

        PushMetaData pushMetaData = newPushMetaData(customerCouponTicket.getId().toString(), PushMessage.SourceType.CUSTOMER_GET_COUPON_TICKET.getValue());
        insertPushMetaData(pushMetaData);

        insertPushMessageService.insertCustomerGetCouponTicketPushMessage(pushMetaData);

        assertEquals(1, jdbcTemplate.queryForInt("select count(1) from bas_weixinmp_template_message where open_id = ?", weixinmpOpenId.getSecondOpenId()));
    }

    @Test
    public void insertCustomerForegiftRefundOrderPushMessage() {
        Partner partner = newPartner();
        insertPartner(partner);

        Weixinmp weixinmp = newWeixinmp(partner.getId());
        insertWeixinmp(weixinmp);

        WeixinmpOpenId weixinmpOpenId = newWeixinmpOpenId(weixinmp.getId());
        insertWeixinmpOpenId(weixinmpOpenId);

        Agent agent = newAgent(partner.getId());
        agent.setWeixinmpId(weixinmp.getId());
        insertAgent(agent);

        Customer customer = newCustomer(partner.getId());
        customer.setAgentId(agent.getId());
        customer.setMpOpenId(weixinmpOpenId.getOpenId());
        insertCustomer(customer);

        CustomerRefundRecord customerRefundRecord = newCustomerRefundRecord(partner.getId(), customer.getId(), agent.getId(), "00000", 2);
        insertCustomerRefundRecord(customerRefundRecord);

        PushMetaData pushMetaData = newPushMetaData(customerRefundRecord.getOrderId(), PushMessage.SourceType.CUSTOMER_APPLY_FOREGIFT_REFUND_SUCCESS.getValue());
        insertPushMetaData(pushMetaData);

        insertPushMessageService.insertCustomerForegiftRefundOrderPushMessage(pushMetaData);

        assertEquals(1, jdbcTemplate.queryForInt("select count(1) from bas_weixinmp_template_message where open_id = ?", weixinmpOpenId.getSecondOpenId()));
    }

    @Test
    public void insertPacketPeriodOrderExpirePushMessage() {
        Partner partner = newPartner();
        insertPartner(partner);

        Weixinmp weixinmp = newWeixinmp(partner.getId());
        insertWeixinmp(weixinmp);

        WeixinmpOpenId weixinmpOpenId = newWeixinmpOpenId(weixinmp.getId());
        insertWeixinmpOpenId(weixinmpOpenId);

        Agent agent = newAgent(partner.getId());
        agent.setWeixinmpId(weixinmp.getId());
        insertAgent(agent);

        Customer customer = newCustomer(partner.getId());
        customer.setAgentId(agent.getId());
        customer.setMpOpenId(weixinmpOpenId.getOpenId());
        insertCustomer(customer);

        PacketPeriodOrder packetPeriodOrder= newPacketPeriodOrder(partner.getId(), customer.getId(), 1, agent.getId());
        packetPeriodOrder.setStatus(PacketPeriodOrder.Status.REFUND.getValue());
        packetPeriodOrder.setPayTime(new Date());
        packetPeriodOrder.setRefundTime(new Date());
        packetPeriodOrder.setAgentId(agent.getId());
        packetPeriodOrder.setBatteryType(1);
        packetPeriodOrder.setPayType(ConstEnum.PayType.BALANCE.getValue());
        packetPeriodOrder.setPrice(1000);
        packetPeriodOrder.setMoney(1000);
        packetPeriodOrder.setRefundMoney(100);
        insertPacketPeriodOrder(packetPeriodOrder);

        PushMetaData pushMetaData = newPushMetaData(packetPeriodOrder.getId(), PushMessage.SourceType.PACKET_PERIOD_ORDER_EXPIRE.getValue());
        insertPushMetaData(pushMetaData);

        insertPushMessageService.insertPacketPeriodOrderExpirePushMessage(pushMetaData);

        assertEquals(1, jdbcTemplate.queryForInt("select count(1) from bas_weixinmp_template_message where open_id = ?", weixinmpOpenId.getSecondOpenId()));
    }

    @Test
    public void insertCustomerInstallmentExpirePushMessage() {
        Partner partner = newPartner();
        insertPartner(partner);

        Weixinmp weixinmp = newWeixinmp(partner.getId());
        insertWeixinmp(weixinmp);

        WeixinmpOpenId weixinmpOpenId = newWeixinmpOpenId(weixinmp.getId());
        insertWeixinmpOpenId(weixinmpOpenId);

        Agent agent = newAgent(partner.getId());
        agent.setWeixinmpId(weixinmp.getId());
        insertAgent(agent);

        Customer customer = newCustomer(partner.getId());
        customer.setAgentId(agent.getId());
        customer.setMpOpenId(weixinmpOpenId.getOpenId());
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
        customerForegiftOrderDetail.setCategory(1);
        insertCustomerInstallmentRecordOrderDetail(customerForegiftOrderDetail);

        PacketPeriodOrder packetPeriodOrder = newPacketPeriodOrder(partner.getId(), customer.getId(), systemBatteryType.getId(), agent.getId());
        insertPacketPeriodOrder(packetPeriodOrder);

        CustomerInstallmentRecordOrderDetail packetPeriodOrderOrderDetail = newCustomerInstallmentRecordOrderDetail(customerInstallmentRecord.getId(), OrderId.OrderIdType.PACKET_PERIOD_ORDER.getValue(), packetPeriodOrder.getId());
        packetPeriodOrderOrderDetail.setNum(2);
        packetPeriodOrderOrderDetail.setCategory(1);
        insertCustomerInstallmentRecordOrderDetail(packetPeriodOrderOrderDetail);

        InsuranceOrder insuranceOrder = newInsuranceOrder(customer);
        insertInsuranceOrder(insuranceOrder);

        CustomerInstallmentRecordOrderDetail insuranceOrderOrderDetail = newCustomerInstallmentRecordOrderDetail(customerInstallmentRecord.getId(), OrderId.OrderIdType.INSURANCE_ORDER.getValue(), insuranceOrder.getId());
        insuranceOrderOrderDetail.setNum(3);
        insuranceOrderOrderDetail.setCategory(1);
        insertCustomerInstallmentRecordOrderDetail(insuranceOrderOrderDetail);

        CustomerInstallmentRecordPayDetail customerInstallmentRecordPayDetail = newCustomerInstallmentRecordPayDetail(customerInstallmentRecord.getId(), agent.getPartnerId(),customer.getId(), agent.getId());
        customerInstallmentRecordPayDetail.setMoney(100);
        insertCustomerInstallmentRecordPayDetail(customerInstallmentRecordPayDetail);

        PushMetaData pushMetaData = newPushMetaData(customerInstallmentRecordPayDetail.getId().toString(), PushMessage.SourceType.CUSTOMER_INSTALLMENT_EXPIRE.getValue());
        insertPushMetaData(pushMetaData);

        insertPushMessageService.insertCustomerInstallmentExpirePushMessage(pushMetaData);

        assertEquals(1, jdbcTemplate.queryForInt("select count(1) from bas_weixinmp_template_message where open_id = ?", weixinmpOpenId.getSecondOpenId()));
    }

    @Test
    public void insertCustomerGetCouponTicketExpirePushMessage() {
        Partner partner = newPartner();
        insertPartner(partner);

        Weixinmp weixinmp = newWeixinmp(partner.getId());
        insertWeixinmp(weixinmp);

        WeixinmpOpenId weixinmpOpenId = newWeixinmpOpenId(weixinmp.getId());
        insertWeixinmpOpenId(weixinmpOpenId);

        Agent agent = newAgent(partner.getId());
        agent.setWeixinmpId(weixinmp.getId());
        insertAgent(agent);

        Customer customer = newCustomer(partner.getId());
        customer.setAgentId(agent.getId());
        customer.setMpOpenId(weixinmpOpenId.getOpenId());
        insertCustomer(customer);


        CustomerCouponTicket customerCouponTicket = newCustomerCouponTicket(partner.getId(), agent.getId(), customer.getMobile());;
        customerCouponTicket.setCustomerMobile(customer.getMobile());
        insertCustomerCouponTicket(customerCouponTicket);

        PushMetaData pushMetaData = newPushMetaData(customerCouponTicket.getId().toString(), PushMessage.SourceType.CUSTOMER_COUPON_TICKET_EXPIRE.getValue());
        insertPushMetaData(pushMetaData);

        insertPushMessageService.insertCustomerGetCouponTicketExpirePushMessage(pushMetaData);

        assertEquals(1, jdbcTemplate.queryForInt("select count(1) from bas_weixinmp_template_message where open_id = ?", weixinmpOpenId.getSecondOpenId()));
    }


    @Test
    public void insertRentForegiftOrderPushMessage() {
        Partner partner = newPartner();
        insertPartner(partner);

        Weixinmp weixinmp = newWeixinmp(partner.getId());
        insertWeixinmp(weixinmp);

        WeixinmpOpenId weixinmpOpenId = newWeixinmpOpenId(weixinmp.getId());
        insertWeixinmpOpenId(weixinmpOpenId);

        Agent agent = newAgent(partner.getId());
        agent.setWeixinmpId(weixinmp.getId());
        insertAgent(agent);

        Customer customer = newCustomer(partner.getId());
        customer.setAgentId(agent.getId());
        customer.setMpOpenId(weixinmpOpenId.getOpenId());
        insertCustomer(customer);


        Cabinet cabinet = newCabinet(agent.getId(), null);
        insertCabinet(cabinet);

        RentForegiftOrder customerForegiftOrder = newRentForegiftOrder(partner.getId(), customer.getId(), agent.getId());
        customerForegiftOrder.setId("111");
        customerForegiftOrder.setStatus(CustomerForegiftOrder.Status.PAY_OK.getValue());
        insertRentForegiftOrder(customerForegiftOrder);


        PushMetaData pushMetaData = newPushMetaData(customerForegiftOrder.getId(), PushMessage.SourceType.RENT_FOREGIFT_PAY_SUCCESS.getValue());
        insertPushMetaData(pushMetaData);

        insertPushMessageService.insertRentForegiftOrderPushMessage(pushMetaData);

        assertEquals(1, jdbcTemplate.queryForInt("select count(1) from bas_weixinmp_template_message where open_id = ?", weixinmpOpenId.getSecondOpenId()));
    }

    @Test
    public void insertRentPeriodOrderPushMessage() {
        Partner partner = newPartner();
        insertPartner(partner);

        Weixinmp weixinmp = newWeixinmp(partner.getId());
        insertWeixinmp(weixinmp);

        WeixinmpOpenId weixinmpOpenId = newWeixinmpOpenId(weixinmp.getId());
        insertWeixinmpOpenId(weixinmpOpenId);

        Agent agent = newAgent(partner.getId());
        agent.setWeixinmpId(weixinmp.getId());
        insertAgent(agent);

        Customer customer = newCustomer(partner.getId());
        customer.setAgentId(agent.getId());
        customer.setMpOpenId(weixinmpOpenId.getOpenId());
        insertCustomer(customer);


        Cabinet cabinet = newCabinet(agent.getId(), null);
        insertCabinet(cabinet);

        RentPeriodOrder packetPeriodOrder = newRentPeriodOrder(partner.getId(), customer.getId(), 1, agent.getId());
        packetPeriodOrder.setStatus(PacketPeriodOrder.Status.USED.getValue());
        packetPeriodOrder.setPayTime(new Date());
        packetPeriodOrder.setAgentId(agent.getId());
        packetPeriodOrder.setPayType(ConstEnum.PayType.BALANCE.getValue());
        packetPeriodOrder.setPrice(1000);
        packetPeriodOrder.setMoney(1000);
        insertRentPeriodOrder(packetPeriodOrder);


        PushMetaData pushMetaData = newPushMetaData(packetPeriodOrder.getId(), PushMessage.SourceType.RENT_PERIOD_ORDER_PAY_SUCCESS.getValue());
        insertPushMetaData(pushMetaData);

        insertPushMessageService.insertRentPeriodOrderPushMessage(pushMetaData);

        assertEquals(1, jdbcTemplate.queryForInt("select count(1) from bas_weixinmp_template_message where open_id = ?", weixinmpOpenId.getSecondOpenId()));
    }

    @Test
    public void insertCustomerOpenNewBatterboxPushMessage() {
        Partner partner = newPartner();
        insertPartner(partner);

        Weixinmp weixinmp = newWeixinmp(partner.getId());
        insertWeixinmp(weixinmp);

        WeixinmpOpenId weixinmpOpenId = newWeixinmpOpenId(weixinmp.getId());
        insertWeixinmpOpenId(weixinmpOpenId);

        Agent agent = newAgent(partner.getId());
        agent.setWeixinmpId(weixinmp.getId());
        insertAgent(agent);

        Customer customer = newCustomer(partner.getId());
        customer.setAgentId(agent.getId());
        customer.setMpOpenId(weixinmpOpenId.getOpenId());
        insertCustomer(customer);


        Playlist playlist = newPlaylist(agent.getId());
        insertPlaylist(playlist);

        TerminalStrategy terminalStrategy = newTerminalStrategy(agent.getId());
        insertTerminalStrategy(terminalStrategy);

        Terminal terminal = newTerminal(agent.getId(), terminalStrategy.getId(), playlist.getId());
        insertTerminal(terminal);

        Cabinet cabinet = newCabinet(agent.getId(), terminal.getId());
        insertCabinet(cabinet);


        SystemBatteryType systemBatteryType = newSystemBatteryType();
        insertSystemBatteryType(systemBatteryType);
        Battery battery = newBattery(agent.getId(), systemBatteryType.getId());
        insertBattery(battery);

        BatteryOrder batteryOrder = newBatteryOrder(newOrderId(OrderId.OrderIdType.BATTERY_ORDER), systemBatteryType.getId(),partner.getId(),agent.getId(), battery.getId(), customer.getId());
        batteryOrder.setOrderStatus(BatteryOrder.OrderStatus.TAKE_OUT.getValue());
        insertBatteryOrder(batteryOrder);

        PushMetaData pushMetaData = newPushMetaData(batteryOrder.getId(), PushMessage.SourceType.CUSTOMER_OPEN_NEW_BATTER_BOX.getValue());
        insertPushMetaData(pushMetaData);

        insertPushMessageService.insertCustomerOpenNewBatterboxPushMessage(pushMetaData);

        assertEquals(1, jdbcTemplate.queryForInt("select count(1) from bas_weixinmp_template_message where open_id = ?", weixinmpOpenId.getSecondOpenId()));
    }

    @Test
    public void insertBatteryInBoxNoticePushMessage() {
        Partner partner = newPartner();
        insertPartner(partner);

        Weixinmp weixinmp = newWeixinmp(partner.getId());
        insertWeixinmp(weixinmp);

        WeixinmpOpenId weixinmpOpenId = newWeixinmpOpenId(weixinmp.getId());
        insertWeixinmpOpenId(weixinmpOpenId);

        Agent agent = newAgent(partner.getId());
        agent.setWeixinmpId(weixinmp.getId());
        insertAgent(agent);

        Customer customer = newCustomer(partner.getId());
        customer.setAgentId(agent.getId());
        customer.setMpOpenId(weixinmpOpenId.getOpenId());
        insertCustomer(customer);


        Playlist playlist = newPlaylist(agent.getId());
        insertPlaylist(playlist);

        TerminalStrategy terminalStrategy = newTerminalStrategy(agent.getId());
        insertTerminalStrategy(terminalStrategy);

        Terminal terminal = newTerminal(agent.getId(), terminalStrategy.getId(), playlist.getId());
        insertTerminal(terminal);

        Cabinet cabinet = newCabinet(agent.getId(), terminal.getId());
        insertCabinet(cabinet);


        SystemBatteryType systemBatteryType = newSystemBatteryType();
        insertSystemBatteryType(systemBatteryType);
        Battery battery = newBattery(agent.getId(), systemBatteryType.getId());
        insertBattery(battery);

        BatteryOrder batteryOrder = newBatteryOrder(newOrderId(OrderId.OrderIdType.BATTERY_ORDER), systemBatteryType.getId(),partner.getId(),agent.getId(), battery.getId(), customer.getId());
        batteryOrder.setOrderStatus(BatteryOrder.OrderStatus.TAKE_OUT.getValue());
        insertBatteryOrder(batteryOrder);

        PushMetaData pushMetaData = newPushMetaData(batteryOrder.getId(), PushMessage.SourceType.BATTERY_IN_BOX_NOTICE.getValue());
        insertPushMetaData(pushMetaData);

        insertPushMessageService.insertBatteryInBoxNoticePushMessage(pushMetaData);

        assertEquals(1, jdbcTemplate.queryForInt("select count(1) from bas_weixinmp_template_message where open_id = ?", weixinmpOpenId.getSecondOpenId()));
    }

    @Test
    public void insertNoCloseBoxPushMessage() {
        Partner partner = newPartner();
        insertPartner(partner);

        Weixinmp weixinmp = newWeixinmp(partner.getId());
        insertWeixinmp(weixinmp);

        WeixinmpOpenId weixinmpOpenId = newWeixinmpOpenId(weixinmp.getId());
        insertWeixinmpOpenId(weixinmpOpenId);

        Agent agent = newAgent(partner.getId());
        agent.setWeixinmpId(weixinmp.getId());
        insertAgent(agent);

        Customer customer = newCustomer(partner.getId());
        customer.setAgentId(agent.getId());
        customer.setMpOpenId(weixinmpOpenId.getOpenId());
        insertCustomer(customer);


        Playlist playlist = newPlaylist(agent.getId());
        insertPlaylist(playlist);

        TerminalStrategy terminalStrategy = newTerminalStrategy(agent.getId());
        insertTerminalStrategy(terminalStrategy);

        Terminal terminal = newTerminal(agent.getId(), terminalStrategy.getId(), playlist.getId());
        insertTerminal(terminal);

        Cabinet cabinet = newCabinet(agent.getId(), terminal.getId());
        insertCabinet(cabinet);


        SystemBatteryType systemBatteryType = newSystemBatteryType();
        insertSystemBatteryType(systemBatteryType);
        Battery battery = newBattery(agent.getId(), systemBatteryType.getId());
        insertBattery(battery);

        BatteryOrder batteryOrder = newBatteryOrder(newOrderId(OrderId.OrderIdType.BATTERY_ORDER), systemBatteryType.getId(),partner.getId(),agent.getId(), battery.getId(), customer.getId());
        batteryOrder.setOrderStatus(BatteryOrder.OrderStatus.TAKE_OUT.getValue());
        insertBatteryOrder(batteryOrder);

        PushMetaData pushMetaData = newPushMetaData(batteryOrder.getId(), PushMessage.SourceType.NO_CLOSE_BOX.getValue());
        insertPushMetaData(pushMetaData);

        insertPushMessageService.insertNoCloseBoxPushMessage(pushMetaData);

        assertEquals(1, jdbcTemplate.queryForInt("select count(1) from bas_weixinmp_template_message where open_id = ?", weixinmpOpenId.getSecondOpenId()));
    }

    @Test
    public void insertCustomerBatteryVolumeLowPushMessage() {
        Partner partner = newPartner();
        insertPartner(partner);

        Weixinmp weixinmp = newWeixinmp(partner.getId());
        insertWeixinmp(weixinmp);

        WeixinmpOpenId weixinmpOpenId = newWeixinmpOpenId(weixinmp.getId());
        insertWeixinmpOpenId(weixinmpOpenId);

        Agent agent = newAgent(partner.getId());
        agent.setWeixinmpId(weixinmp.getId());
        insertAgent(agent);

        Customer customer = newCustomer(partner.getId());
        customer.setAgentId(agent.getId());
        customer.setMpOpenId(weixinmpOpenId.getOpenId());
        insertCustomer(customer);


        Playlist playlist = newPlaylist(agent.getId());
        insertPlaylist(playlist);

        TerminalStrategy terminalStrategy = newTerminalStrategy(agent.getId());
        insertTerminalStrategy(terminalStrategy);

        Terminal terminal = newTerminal(agent.getId(), terminalStrategy.getId(), playlist.getId());
        insertTerminal(terminal);

        Cabinet cabinet = newCabinet(agent.getId(), terminal.getId());
        insertCabinet(cabinet);

        String orderId = newOrderId(OrderId.OrderIdType.BATTERY_ORDER);

        SystemBatteryType systemBatteryType = newSystemBatteryType();
        insertSystemBatteryType(systemBatteryType);
        Battery battery = newBattery(agent.getId(), systemBatteryType.getId());
        battery.setOrderId(orderId);
        insertBattery(battery);

        BatteryOrder batteryOrder = newBatteryOrder( orderId, systemBatteryType.getId(),partner.getId(),agent.getId(), battery.getId(), customer.getId());
        batteryOrder.setOrderStatus(BatteryOrder.OrderStatus.TAKE_OUT.getValue());
        insertBatteryOrder(batteryOrder);

        PushMetaData pushMetaData = newPushMetaData(battery.getId(), PushMessage.SourceType.CUSTOMER_BATTERY_VOLUME_LOW.getValue());
        insertPushMetaData(pushMetaData);

        insertPushMessageService.insertCustomerBatteryVolumeLowPushMessage(pushMetaData);

        assertEquals(1, jdbcTemplate.queryForInt("select count(1) from bas_weixinmp_template_message where open_id = ?", weixinmpOpenId.getSecondOpenId()));
    }

    /**
     * 电量低推送运营商
     */
    @Test
    public void insertAgentBatteryVolumeLowPushMessage() {
        Partner partner = newPartner();
        insertPartner(partner);

        Weixinmp weixinmp = newWeixinmp(partner.getId());
        insertWeixinmp(weixinmp);

        WeixinmpOpenId weixinmpOpenId = newWeixinmpOpenId(weixinmp.getId());
        insertWeixinmpOpenId(weixinmpOpenId);

        Agent agent = newAgent(partner.getId());
        agent.setWeixinmpId(weixinmp.getId());
        insertAgent(agent);

        Customer customer = newCustomer(partner.getId());
        customer.setAgentId(agent.getId());
        customer.setMpOpenId(weixinmpOpenId.getOpenId());
        insertCustomer(customer);

        Role role = newRole(agent.getId());
        insertRole(role);

        Dept dept = newDept(agent.getId());
        insertDept(dept);

        User user = newUser(agent.getId(), role.getId(), dept.getId());
        user.setMobile(customer.getMobile());
        insertUser(user);

        Playlist playlist = newPlaylist(agent.getId());
        insertPlaylist(playlist);

        TerminalStrategy terminalStrategy = newTerminalStrategy(agent.getId());
        insertTerminalStrategy(terminalStrategy);

        Terminal terminal = newTerminal(agent.getId(), terminalStrategy.getId(), playlist.getId());
        insertTerminal(terminal);

        Cabinet cabinet = newCabinet(agent.getId(), terminal.getId());
        insertCabinet(cabinet);

        String orderId = newOrderId(OrderId.OrderIdType.BATTERY_ORDER);

        SystemBatteryType systemBatteryType = newSystemBatteryType();
        insertSystemBatteryType(systemBatteryType);
        Battery battery = newBattery(agent.getId(), systemBatteryType.getId());
        battery.setOrderId(orderId);
        insertBattery(battery);

        BatteryOrder batteryOrder = newBatteryOrder( orderId, systemBatteryType.getId(),partner.getId(),agent.getId(), battery.getId(), customer.getId());
        batteryOrder.setOrderStatus(BatteryOrder.OrderStatus.TAKE_OUT.getValue());
        insertBatteryOrder(batteryOrder);

        PushMetaData pushMetaData = newPushMetaData(battery.getId(), PushMessage.SourceType.CUSTOMER_BATTERY_VOLUME_LOW_NOTICE_AGENT.getValue());
        insertPushMetaData(pushMetaData);

        insertPushMessageService.insertAgentBatteryVolumeLowPushMessage(pushMetaData);

        assertEquals(1, jdbcTemplate.queryForInt("select count(1) from bas_weixinmp_template_message where open_id = ?", weixinmpOpenId.getSecondOpenId()));
    }

    /**
     * 电池发生保护
     */
    @Test
    public void insertAgentFaultTypeCodeMessage() {
        Partner partner = newPartner();
        insertPartner(partner);

        Weixinmp weixinmp = newWeixinmp(partner.getId());
        insertWeixinmp(weixinmp);

        WeixinmpOpenId weixinmpOpenId = newWeixinmpOpenId(weixinmp.getId());
        insertWeixinmpOpenId(weixinmpOpenId);

        Agent agent = newAgent(partner.getId());
        agent.setWeixinmpId(weixinmp.getId());
        insertAgent(agent);

        Customer customer = newCustomer(partner.getId());
        customer.setAgentId(agent.getId());
        customer.setMpOpenId(weixinmpOpenId.getOpenId());
        insertCustomer(customer);

        Role role = newRole(agent.getId());
        insertRole(role);

        Dept dept = newDept(agent.getId());
        insertDept(dept);

        User user = newUser(agent.getId(), role.getId(), dept.getId());
        user.setMobile(customer.getMobile());
        insertUser(user);

        Playlist playlist = newPlaylist(agent.getId());
        insertPlaylist(playlist);

        TerminalStrategy terminalStrategy = newTerminalStrategy(agent.getId());
        insertTerminalStrategy(terminalStrategy);

        Terminal terminal = newTerminal(agent.getId(), terminalStrategy.getId(), playlist.getId());
        insertTerminal(terminal);

        Cabinet cabinet = newCabinet(agent.getId(), terminal.getId());
        insertCabinet(cabinet);

        String orderId = newOrderId(OrderId.OrderIdType.BATTERY_ORDER);

        SystemBatteryType systemBatteryType = newSystemBatteryType();
        insertSystemBatteryType(systemBatteryType);
        Battery battery = newBattery(agent.getId(), systemBatteryType.getId());
        battery.setOrderId(orderId);
        insertBattery(battery);

        BatteryParameter batteryParameter = newBatteryParameter(battery.getId());
        insertBatteryParameter(batteryParameter);

        BatteryOrder batteryOrder = newBatteryOrder( orderId, systemBatteryType.getId(),partner.getId(),agent.getId(), battery.getId(), customer.getId());
        batteryOrder.setOrderStatus(BatteryOrder.OrderStatus.TAKE_OUT.getValue());
        insertBatteryOrder(batteryOrder);

        PushMetaData pushMetaData = newPushMetaData(battery.getId(), PushMessage.SourceType.FAULT_TYPE_CODE_5.getValue());
        insertPushMetaData(pushMetaData);

        PushMetaData pushMetaData2 = newPushMetaData(battery.getId(), PushMessage.SourceType.FAULT_TYPE_CODE_6.getValue());
        insertPushMetaData(pushMetaData2);

        PushMetaData pushMetaData3 = newPushMetaData(battery.getId(), PushMessage.SourceType.FAULT_TYPE_CODE_7.getValue());
        insertPushMetaData(pushMetaData3);

        PushMetaData pushMetaData4 = newPushMetaData(battery.getId(), PushMessage.SourceType.FAULT_TYPE_CODE_8.getValue());
        insertPushMetaData(pushMetaData4);

        PushMetaData pushMetaData5 = newPushMetaData(battery.getId(), PushMessage.SourceType.FAULT_TYPE_CODE_9.getValue());
        insertPushMetaData(pushMetaData5);

        PushMetaData pushMetaData6 = newPushMetaData(battery.getId(), PushMessage.SourceType.FAULT_TYPE_CODE_10.getValue());
        insertPushMetaData(pushMetaData6);

        PushMetaData pushMetaData7 = newPushMetaData(battery.getId(), PushMessage.SourceType.FAULT_TYPE_CODE_11.getValue());
        insertPushMetaData(pushMetaData7);

        PushMetaData pushMetaData8 = newPushMetaData(battery.getId(), PushMessage.SourceType.FAULT_TYPE_CODE_12.getValue());
        insertPushMetaData(pushMetaData8);


       /* insertPushMessageService.insertAgentFaultTypeCodeMessage(pushMetaData);
        insertPushMessageService.insertAgentFaultTypeCodeMessage(pushMetaData2);
        insertPushMessageService.insertAgentFaultTypeCodeMessage(pushMetaData3);
        insertPushMessageService.insertAgentFaultTypeCodeMessage(pushMetaData4);
        insertPushMessageService.insertAgentFaultTypeCodeMessage(pushMetaData5);
        insertPushMessageService.insertAgentFaultTypeCodeMessage(pushMetaData6);
        insertPushMessageService.insertAgentFaultTypeCodeMessage(pushMetaData7);
        insertPushMessageService.insertAgentFaultTypeCodeMessage(pushMetaData8);*/

        assertEquals(8, jdbcTemplate.queryForInt("select count(1) from bas_weixinmp_template_message where open_id = ?", weixinmpOpenId.getSecondOpenId()));
    }

    /**
     * 压差过大异常
     */
    @Test
    public void insertAgentVolDiffHighMessage() {
        Partner partner = newPartner();
        insertPartner(partner);

        Weixinmp weixinmp = newWeixinmp(partner.getId());
        insertWeixinmp(weixinmp);

        WeixinmpOpenId weixinmpOpenId = newWeixinmpOpenId(weixinmp.getId());
        insertWeixinmpOpenId(weixinmpOpenId);

        Agent agent = newAgent(partner.getId());
        agent.setWeixinmpId(weixinmp.getId());
        insertAgent(agent);

        Customer customer = newCustomer(partner.getId());
        customer.setAgentId(agent.getId());
        customer.setMpOpenId(weixinmpOpenId.getOpenId());
        insertCustomer(customer);

        Role role = newRole(agent.getId());
        insertRole(role);

        Dept dept = newDept(agent.getId());
        insertDept(dept);

        User user = newUser(agent.getId(), role.getId(), dept.getId());
        user.setMobile(customer.getMobile());
        insertUser(user);


        String orderId = newOrderId(OrderId.OrderIdType.BATTERY_ORDER);

        SystemBatteryType systemBatteryType = newSystemBatteryType();
        insertSystemBatteryType(systemBatteryType);
        Battery battery = newBattery(agent.getId(), systemBatteryType.getId());
        battery.setOrderId(orderId);
        insertBattery(battery);

        BatteryParameter batteryParameter = newBatteryParameter(battery.getId());
        insertBatteryParameter(batteryParameter);

        BatteryOrder batteryOrder = newBatteryOrder( orderId, systemBatteryType.getId(),partner.getId(),agent.getId(), battery.getId(), customer.getId());
        batteryOrder.setOrderStatus(BatteryOrder.OrderStatus.TAKE_OUT.getValue());
        insertBatteryOrder(batteryOrder);

        PushMetaData pushMetaData = newPushMetaData(battery.getId(), PushMessage.SourceType.VOL_DIFF_HIGH.getValue());
        insertPushMetaData(pushMetaData);

        insertPushMessageService.insertAgentVolDiffHighMessage(pushMetaData);


        assertEquals(1, jdbcTemplate.queryForInt("select count(1) from bas_weixinmp_template_message where open_id = ?", weixinmpOpenId.getSecondOpenId()));
    }

    /**
     * 骑手租赁电池超时异常
     */
    @Test
    public void insertCustomerBatteryOverTimeMessage() {
        Partner partner = newPartner();
        insertPartner(partner);

        Weixinmp weixinmp = newWeixinmp(partner.getId());
        insertWeixinmp(weixinmp);

        WeixinmpOpenId weixinmpOpenId = newWeixinmpOpenId(weixinmp.getId());
        insertWeixinmpOpenId(weixinmpOpenId);

        Agent agent = newAgent(partner.getId());
        agent.setWeixinmpId(weixinmp.getId());
        insertAgent(agent);

        Customer customer = newCustomer(partner.getId());
        customer.setAgentId(agent.getId());
        customer.setMpOpenId(weixinmpOpenId.getOpenId());
        insertCustomer(customer);

        Role role = newRole(agent.getId());
        insertRole(role);

        Dept dept = newDept(agent.getId());
        insertDept(dept);

        User user = newUser(agent.getId(), role.getId(), dept.getId());
        user.setMobile(customer.getMobile());
        insertUser(user);

        Playlist playlist = newPlaylist(agent.getId());
        insertPlaylist(playlist);

        TerminalStrategy terminalStrategy = newTerminalStrategy(agent.getId());
        insertTerminalStrategy(terminalStrategy);

        Terminal terminal = newTerminal(agent.getId(), terminalStrategy.getId(), playlist.getId());
        insertTerminal(terminal);

        Cabinet cabinet = newCabinet(agent.getId(), terminal.getId());
        insertCabinet(cabinet);


        SystemBatteryType systemBatteryType = newSystemBatteryType();
        insertSystemBatteryType(systemBatteryType);
        Battery battery = newBattery(agent.getId(), systemBatteryType.getId());
        insertBattery(battery);

        BatteryOrder batteryOrder = newBatteryOrder(newOrderId(OrderId.OrderIdType.BATTERY_ORDER), systemBatteryType.getId(),partner.getId(),agent.getId(), battery.getId(), customer.getId());
        batteryOrder.setOrderStatus(BatteryOrder.OrderStatus.TAKE_OUT.getValue());
        batteryOrder.setCreateTime(DateUtils.addDays(new Date(), -20));
        insertBatteryOrder(batteryOrder);

        PushMetaData pushMetaData = newPushMetaData(batteryOrder.getId(), PushMessage.SourceType.CUSTOMER_BATTERY_OVERTIME.getValue());
        insertPushMetaData(pushMetaData);

        insertPushMessageService.insertCustomerBatteryOverTimeMessage(pushMetaData);

        assertEquals(1, jdbcTemplate.queryForInt("select count(1) from bas_weixinmp_template_message where open_id = ?", weixinmpOpenId.getSecondOpenId()));
    }

    /**
     *未绑定电池在外超时异常
     */
    @Test
    public void insertUnbindBatteryOverTimeMessage() {
        Partner partner = newPartner();
        insertPartner(partner);

        Weixinmp weixinmp = newWeixinmp(partner.getId());
        insertWeixinmp(weixinmp);

        WeixinmpOpenId weixinmpOpenId = newWeixinmpOpenId(weixinmp.getId());
        insertWeixinmpOpenId(weixinmpOpenId);

        Agent agent = newAgent(partner.getId());
        agent.setWeixinmpId(weixinmp.getId());
        insertAgent(agent);

        Customer customer = newCustomer(partner.getId());
        customer.setAgentId(agent.getId());
        customer.setMpOpenId(weixinmpOpenId.getOpenId());
        insertCustomer(customer);

        Role role = newRole(agent.getId());
        insertRole(role);

        Dept dept = newDept(agent.getId());
        insertDept(dept);

        User user = newUser(agent.getId(), role.getId(), dept.getId());
        user.setMobile(customer.getMobile());
        insertUser(user);


        String orderId = newOrderId(OrderId.OrderIdType.BATTERY_ORDER);

        SystemBatteryType systemBatteryType = newSystemBatteryType();
        insertSystemBatteryType(systemBatteryType);
        Battery battery = newBattery(agent.getId(), systemBatteryType.getId());
        battery.setOrderId(orderId);
        battery.setFreeOutTime(new Date());
        insertBattery(battery);

        BatteryParameter batteryParameter = newBatteryParameter(battery.getId());
        insertBatteryParameter(batteryParameter);

        BatteryOrder batteryOrder = newBatteryOrder( orderId, systemBatteryType.getId(),partner.getId(),agent.getId(), battery.getId(), customer.getId());
        batteryOrder.setOrderStatus(BatteryOrder.OrderStatus.TAKE_OUT.getValue());
        insertBatteryOrder(batteryOrder);

        PushMetaData pushMetaData = newPushMetaData(battery.getId(), PushMessage.SourceType.UNBIND__BATTERY_OUT_OVERTIME.getValue());
        insertPushMetaData(pushMetaData);

        insertPushMessageService.insertUnbindBatteryOverTimeMessage(pushMetaData);


        assertEquals(1, jdbcTemplate.queryForInt("select count(1) from bas_weixinmp_template_message where open_id = ?", weixinmpOpenId.getSecondOpenId()));
    }

    /**
     * 电池单体电压小于最小电压断电
     */
    @Test
    public void insertSignVolLowPushMessage() {
        Partner partner = newPartner();
        insertPartner(partner);

        Weixinmp weixinmp = newWeixinmp(partner.getId());
        insertWeixinmp(weixinmp);

        WeixinmpOpenId weixinmpOpenId = newWeixinmpOpenId(weixinmp.getId());
        insertWeixinmpOpenId(weixinmpOpenId);

        Agent agent = newAgent(partner.getId());
        agent.setWeixinmpId(weixinmp.getId());
        insertAgent(agent);

        Customer customer = newCustomer(partner.getId());
        customer.setAgentId(agent.getId());
        customer.setMpOpenId(weixinmpOpenId.getOpenId());
        insertCustomer(customer);

        Role role = newRole(agent.getId());
        insertRole(role);

        Dept dept = newDept(agent.getId());
        insertDept(dept);

        User user = newUser(agent.getId(), role.getId(), dept.getId());
        user.setMobile(customer.getMobile());
        insertUser(user);


        String orderId = newOrderId(OrderId.OrderIdType.BATTERY_ORDER);

        SystemBatteryType systemBatteryType = newSystemBatteryType();
        insertSystemBatteryType(systemBatteryType);
        Battery battery = newBattery(agent.getId(), systemBatteryType.getId());
        battery.setOrderId(orderId);
        insertBattery(battery);

        BatteryParameter batteryParameter = newBatteryParameter(battery.getId());
        batteryParameter.setOcvTable("10000");
        batteryParameter.setSingleVoltage("1000");
        insertBatteryParameter(batteryParameter);

        BatteryOrder batteryOrder = newBatteryOrder( orderId, systemBatteryType.getId(),partner.getId(),agent.getId(), battery.getId(), customer.getId());
        batteryOrder.setOrderStatus(BatteryOrder.OrderStatus.TAKE_OUT.getValue());
        insertBatteryOrder(batteryOrder);

        PushMetaData pushMetaData = newPushMetaData(battery.getId(), PushMessage.SourceType.SIGH_VOL_LOW.getValue());
        insertPushMetaData(pushMetaData);

        insertPushMessageService.insertSignVolLowPushMessage(pushMetaData);


        assertEquals(1, jdbcTemplate.queryForInt("select count(1) from bas_weixinmp_template_message where open_id = ?", weixinmpOpenId.getSecondOpenId()));
    }


































    @Autowired
    private VoiceMessageService voiceMessageService;

    @Test
    public void insertCustomerBatteryVolumeLowVoiceMessage() {
        System.setProperty("unit.test", "true");

        Partner partner = newPartner();
        insertPartner(partner);

        Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        Customer customer = newCustomer(partner.getId());
        customer.setMobile("13675608767");
        insertCustomer(customer);

        SystemBatteryType systemBatteryType = newSystemBatteryType();
        insertSystemBatteryType(systemBatteryType);

        Battery battery = newBattery(agent.getId(), systemBatteryType.getId());
        battery.setCategory(Battery.Category.RENT.getValue());
        battery.setLowVolumeNoticeVolume(20);
        insertBattery(battery);

        BatteryOrder batteryOrder = newBatteryOrder(newOrderId(OrderId.OrderIdType.BATTERY_ORDER),
                systemBatteryType.getId(),
                partner.getId(),agent.getId(),
                battery.getId(),
                customer.getId());
        batteryOrder.setOrderStatus(BatteryOrder.OrderStatus.TAKE_OUT.getValue());
        insertBatteryOrder(batteryOrder);

        CustomerExchangeBattery customerExchangeBattery = newCustomerExchangeBattery(customer.getId(), agent.getId(), battery.getId(), batteryOrder.getId());
        insertCustomerExchangeBattery(customerExchangeBattery);

        battery.setOrderId(batteryOrder.getId());
        jdbcTemplate.update("UPDATE hdg_battery SET order_id = ? WHERE id = ?", new Object[] {batteryOrder.getId(), battery.getId()});

        PushMetaData pushMetaData = newPushMetaData(battery.getId(), PushMessage.SourceType.CUSTOMER_BATTERY_VOLUME_LOW.getValue());
        insertPushMetaData(pushMetaData);

        insertPushMessageService.insertCustomerBatteryVolumeLowVoiceMessage(pushMetaData);

        VoiceConfig voiceConfig = newVoiceConfig(agent.getId());
        insertVoiceConfig(voiceConfig);
//        AppConfig appConfig = new AppConfig();
//        appConfig.aliyunVoiceClient = new AliyunVoiceClient(appConfig);

        voiceMessageService.scanMessage();
    }
}
