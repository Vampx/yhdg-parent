package cn.com.yusong.yhdg.agentappserver.service.hdg;

import cn.com.yusong.yhdg.agentappserver.BaseJunit4Test;
import cn.com.yusong.yhdg.agentappserver.web.controller.api.v1.agent.hdg.PriceGroupController;
import cn.com.yusong.yhdg.agentappserver.web.controller.api.v1.agent.hdg.VipPriceController;
import cn.com.yusong.yhdg.common.constant.Constant;
import cn.com.yusong.yhdg.common.domain.basic.*;
import cn.com.yusong.yhdg.common.domain.hdg.*;
import cn.com.yusong.yhdg.common.domain.yms.Playlist;
import cn.com.yusong.yhdg.common.domain.yms.Terminal;
import cn.com.yusong.yhdg.common.domain.yms.TerminalStrategy;
import cn.com.yusong.yhdg.common.domain.zd.*;
import org.apache.commons.lang.time.DateUtils;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

public class VipPriceServiceTest extends BaseJunit4Test {

    @Autowired
    VipPriceService vipPriceService;

    @Test
    public void list() throws ParseException {
        Partner partner = newPartner();
        insertPartner(partner);
        Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        SystemBatteryType systemBatteryType = newSystemBatteryType();
        insertSystemBatteryType(systemBatteryType);

        VipPrice vipPrice = newVipPrice(agent.getId(), systemBatteryType.getId());
        insertVipPrice(vipPrice);

        assertEquals(0, vipPriceService.list(agent.getId(), "", 0, 10).getCode());
        Date beginTime = DateUtils.parseDate("2019-07-12" + " 00:00:00", new String[]{Constant.DATE_TIME_FORMAT});
        Date endTime = DateUtils.parseDate("2019-07-15" + " 23:59:59", new String[]{Constant.DATE_TIME_FORMAT});

        System.out.println(daysBetween(beginTime, endTime));
    }

    //获取日期相差天数
    public static int daysBetween(Date date1,Date date2) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date1);
        long time1 = cal.getTimeInMillis();
        cal.setTime(date2);
        long time2 = cal.getTimeInMillis();
        long between_days=(time2-time1)/(1000*3600*24);
        return Integer.parseInt(String.valueOf(between_days));
    }

    @Test
    public void detail() {
        Partner partner = newPartner();
        insertPartner(partner);
        Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        SystemBatteryType systemBatteryType = newSystemBatteryType();
        insertSystemBatteryType(systemBatteryType);
        Battery battery = newBattery(agent.getId(), systemBatteryType.getId());
        insertBattery(battery);

        AgentBatteryType agentBatteryType = newAgentBatteryType(agent.getId(), battery.getType());
        insertAgentBatteryType(agentBatteryType);

        ExchangePriceTime exchangePriceTime = newExchangePriceTime(agent.getId(), agentBatteryType.getBatteryType());
        insertExchangePriceTime(exchangePriceTime);

        ExchangeBatteryForegift exchangeBatteryForegift = newExchangeBatteryForegift(agent.getId(), systemBatteryType.getId());
        insertExchangeBatteryForegift(exchangeBatteryForegift);

        VipPrice vipPrice = newVipPrice(agent.getId(), systemBatteryType.getId());
        insertVipPrice(vipPrice);

        VipExchangeBatteryForegift vipExchangeBatteryForegift = newVipExchangeBatteryForegift(agent.getId(), exchangeBatteryForegift.getId().longValue(), vipPrice.getId());
        insertVipExchangeBatteryForegift(vipExchangeBatteryForegift);

        VipPacketPeriodPrice vipPacketPeriodPrice = newVipPacketPeriodPrice(vipExchangeBatteryForegift.getId(),vipPrice.getId(), agent.getId(), exchangeBatteryForegift.getId().longValue());
        insertVipPacketPeriodPrice(vipPacketPeriodPrice);

        VipPriceCustomer vipPriceCustomer = newVipPriceCustomer(vipPrice.getId());
        insertVipPriceCustomer(vipPriceCustomer);

        Playlist playlist = newPlaylist(agent.getId());
        insertPlaylist(playlist);

        TerminalStrategy terminalStrategy = newTerminalStrategy(agent.getId());
        insertTerminalStrategy(terminalStrategy);

        Terminal terminal = newTerminal(agent.getId(), terminalStrategy.getId(), playlist.getId());
        insertTerminal(terminal);

        Cabinet cabinet = newCabinet(agent.getId(), terminal.getId());
        insertCabinet(cabinet);

        VipPriceCabinet vipPriceCabinet = newVipPriceCabinet(vipPrice.getId(), cabinet.getId());
        insertVipPriceCabinet(vipPriceCabinet);

        assertEquals(0, vipPriceService.detail(vipPrice.getId(), agent.getId()).getCode());

    }

    @Test
    public void create() {
        Partner partner = newPartner();
        insertPartner(partner);
        Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        SystemBatteryType systemBatteryType = newSystemBatteryType();
        insertSystemBatteryType(systemBatteryType);
        Battery battery = newBattery(agent.getId(), systemBatteryType.getId());
        insertBattery(battery);

        AgentBatteryType agentBatteryType = newAgentBatteryType(agent.getId(), battery.getType());
        insertAgentBatteryType(agentBatteryType);

        ExchangePriceTime exchangePriceTime = newExchangePriceTime(agent.getId(), agentBatteryType.getBatteryType());
        insertExchangePriceTime(exchangePriceTime);

        ExchangeBatteryForegift exchangeBatteryForegift = newExchangeBatteryForegift(agent.getId(), systemBatteryType.getId());
        insertExchangeBatteryForegift(exchangeBatteryForegift);

        VipPrice vipPrice = newVipPrice(agent.getId(), systemBatteryType.getId());

        VipPriceController.CreateParam.VipPacketPeriodPrice[] biza = new VipPriceController.CreateParam.VipPacketPeriodPrice[1];
        for (int i = 0; i < 1; i++) {
            VipPriceController.CreateParam.VipPacketPeriodPrice bizaForegift = new VipPriceController.CreateParam.VipPacketPeriodPrice();
            bizaForegift.foregiftId = 1L;
            bizaForegift.reduceMoney = 10;

            VipPriceController.CreateParam.VipPacketPeriodPrice.GroupBizaPrice bizaPacketPeriodPrice = new VipPriceController.CreateParam.VipPacketPeriodPrice.GroupBizaPrice();
            bizaPacketPeriodPrice.dayCount = 2;
            bizaPacketPeriodPrice.price = 3;
            bizaPacketPeriodPrice.memo = "xxxx";
            bizaPacketPeriodPrice.limitCount = 1;
            bizaPacketPeriodPrice.isTicket = 1;
            bizaPacketPeriodPrice.dayLimitCount = 1;

            VipPriceController.CreateParam.VipPacketPeriodPrice.GroupBizaPrice.GroupBizaPriceRenew bizaPacketPeriodPriceRenew = new VipPriceController.CreateParam.VipPacketPeriodPrice.GroupBizaPrice.GroupBizaPriceRenew();
            bizaPacketPeriodPriceRenew.dayCount = 12;
            bizaPacketPeriodPriceRenew.price = 13;
            bizaPacketPeriodPriceRenew.memo = "xzxxx";
            bizaPacketPeriodPriceRenew.isTicket = 1;
            bizaPacketPeriodPriceRenew.limitCount = 11;
            bizaPacketPeriodPriceRenew.dayLimitCount = 11;

            VipPriceController.CreateParam.VipPacketPeriodPrice.GroupBizaPrice.GroupBizaPriceRenew[] priceRenewList = new VipPriceController.CreateParam.VipPacketPeriodPrice.GroupBizaPrice.GroupBizaPriceRenew[]{bizaPacketPeriodPriceRenew};

            bizaPacketPeriodPrice.priceListRenew = priceRenewList;

            VipPriceController.CreateParam.VipPacketPeriodPrice.GroupBizaPrice[] priceList = new VipPriceController.CreateParam.VipPacketPeriodPrice.GroupBizaPrice[]{bizaPacketPeriodPrice};

            bizaForegift.priceList = priceList;

            biza[i] = bizaForegift;
        }

        String[] customerMobileList = new String[] {"15145252526"};
        String[] cabinetIdList = new String[] {"20190624001"};

        Station station = newStation(agent.getId());
        insertStation(station);

        String[] stationIdList = new String[] {station.getId()};

        assertEquals(0,
                vipPriceService.create(vipPrice,
                        agent.getId(), biza, customerMobileList,
                        cabinetIdList, stationIdList).getCode());

    }

    @Test
    public void update() {
        Partner partner = newPartner();
        insertPartner(partner);
        Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        SystemBatteryType systemBatteryType = newSystemBatteryType();
        insertSystemBatteryType(systemBatteryType);
        Battery battery = newBattery(agent.getId(), systemBatteryType.getId());
        insertBattery(battery);

        AgentBatteryType agentBatteryType = newAgentBatteryType(agent.getId(), battery.getType());
        insertAgentBatteryType(agentBatteryType);

        ExchangePriceTime exchangePriceTime = newExchangePriceTime(agent.getId(), agentBatteryType.getBatteryType());
        insertExchangePriceTime(exchangePriceTime);

        ExchangeBatteryForegift exchangeBatteryForegift = newExchangeBatteryForegift(agent.getId(), systemBatteryType.getId());
        insertExchangeBatteryForegift(exchangeBatteryForegift);

        VipPrice vipPrice = newVipPrice(agent.getId(), systemBatteryType.getId());
        insertVipPrice(vipPrice);

        VipPriceController.UpdateParam.VipPacketPeriodPrice[] biza = new VipPriceController.UpdateParam.VipPacketPeriodPrice[1];
        for (int i = 0; i < 1; i++) {
            VipPriceController.UpdateParam.VipPacketPeriodPrice bizaForegift = new VipPriceController.UpdateParam.VipPacketPeriodPrice();
            bizaForegift.foregiftId = 1L;
            bizaForegift.reduceMoney = 10;

            VipPriceController.UpdateParam.VipPacketPeriodPrice.GroupBizaPrice bizaPacketPeriodPrice = new VipPriceController.UpdateParam.VipPacketPeriodPrice.GroupBizaPrice();
            bizaPacketPeriodPrice.dayCount = 2;
            bizaPacketPeriodPrice.price = 3;
            bizaPacketPeriodPrice.memo = "xxxx";
            bizaPacketPeriodPrice.limitCount = 1;
            bizaPacketPeriodPrice.isTicket = 1;
            bizaPacketPeriodPrice.dayLimitCount = 1;

            VipPriceController.UpdateParam.VipPacketPeriodPrice.GroupBizaPrice.GroupBizaPriceRenew bizaPacketPeriodPriceRenew = new VipPriceController.UpdateParam.VipPacketPeriodPrice.GroupBizaPrice.GroupBizaPriceRenew();
            bizaPacketPeriodPriceRenew.dayCount = 12;
            bizaPacketPeriodPriceRenew.price = 13;
            bizaPacketPeriodPriceRenew.memo = "xzxxx";
            bizaPacketPeriodPriceRenew.isTicket = 1;
            bizaPacketPeriodPriceRenew.limitCount = 11;
            bizaPacketPeriodPriceRenew.dayLimitCount = 11;

            VipPriceController.UpdateParam.VipPacketPeriodPrice.GroupBizaPrice.GroupBizaPriceRenew[] priceRenewList = new VipPriceController.UpdateParam.VipPacketPeriodPrice.GroupBizaPrice.GroupBizaPriceRenew[]{bizaPacketPeriodPriceRenew};

            bizaPacketPeriodPrice.priceListRenew = priceRenewList;

            VipPriceController.UpdateParam.VipPacketPeriodPrice.GroupBizaPrice[] priceList = new VipPriceController.UpdateParam.VipPacketPeriodPrice.GroupBizaPrice[]{bizaPacketPeriodPrice};

            bizaForegift.priceList = priceList;

            biza[i] = bizaForegift;
        }

        String[] customerMobileList = new String[] {"15145252526", "15145252522"};
        String[] cabinetIdList = new String[] {"20190624001"};

        Station station = newStation(agent.getId());
        insertStation(station);

        String[] stationIdList = new String[] {station.getId()};

        assertEquals(0,
                vipPriceService.update(vipPrice,
                        agent.getId(), biza, customerMobileList,
                        cabinetIdList, stationIdList).getCode());

    }


    @Test
    public void delete() {
        Partner partner = newPartner();
        insertPartner(partner);
        Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        SystemBatteryType systemBatteryType = newSystemBatteryType();
        insertSystemBatteryType(systemBatteryType);
        Battery battery = newBattery(agent.getId(), systemBatteryType.getId());
        insertBattery(battery);

        AgentBatteryType agentBatteryType = newAgentBatteryType(agent.getId(), battery.getType());
        insertAgentBatteryType(agentBatteryType);

        ExchangePriceTime exchangePriceTime = newExchangePriceTime(agent.getId(), agentBatteryType.getBatteryType());
        insertExchangePriceTime(exchangePriceTime);

        ExchangeBatteryForegift exchangeBatteryForegift = newExchangeBatteryForegift(agent.getId(), systemBatteryType.getId());
        insertExchangeBatteryForegift(exchangeBatteryForegift);

        VipPrice vipPrice = newVipPrice(agent.getId(), systemBatteryType.getId());
        insertVipPrice(vipPrice);

        assertEquals(0, vipPriceService.delete(vipPrice.getId()).getCode());

    }

    @Test
    public void vipCabinetDelete() {
        Partner partner = newPartner();
        insertPartner(partner);
        Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        SystemBatteryType systemBatteryType = newSystemBatteryType();
        insertSystemBatteryType(systemBatteryType);
        Battery battery = newBattery(agent.getId(), systemBatteryType.getId());
        insertBattery(battery);

        AgentBatteryType agentBatteryType = newAgentBatteryType(agent.getId(), battery.getType());
        insertAgentBatteryType(agentBatteryType);

        ExchangePriceTime exchangePriceTime = newExchangePriceTime(agent.getId(), agentBatteryType.getBatteryType());
        insertExchangePriceTime(exchangePriceTime);

        ExchangeBatteryForegift exchangeBatteryForegift = newExchangeBatteryForegift(agent.getId(), systemBatteryType.getId());
        insertExchangeBatteryForegift(exchangeBatteryForegift);

        VipPrice vipPrice = newVipPrice(agent.getId(), systemBatteryType.getId());
        insertVipPrice(vipPrice);

        VipPriceCustomer vipPriceCustomer = newVipPriceCustomer(vipPrice.getId());
        insertVipPriceCustomer(vipPriceCustomer);

        Playlist playlist = newPlaylist(agent.getId());
        insertPlaylist(playlist);

        TerminalStrategy terminalStrategy = newTerminalStrategy(agent.getId());
        insertTerminalStrategy(terminalStrategy);

        Terminal terminal = newTerminal(agent.getId(), terminalStrategy.getId(), playlist.getId());
        insertTerminal(terminal);

        Cabinet cabinet = newCabinet(agent.getId(), terminal.getId());
        insertCabinet(cabinet);

        VipPriceCabinet vipPriceCabinet = newVipPriceCabinet(vipPrice.getId(), cabinet.getId());
        insertVipPriceCabinet(vipPriceCabinet);

        assertEquals(0, vipPriceService.vipCabinetDelete(vipPriceCabinet.getId()).getCode());

    }

    @Test
    public void vipStationDelete() {
        Partner partner = newPartner();
        insertPartner(partner);
        Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        SystemBatteryType systemBatteryType = newSystemBatteryType();
        insertSystemBatteryType(systemBatteryType);
        Battery battery = newBattery(agent.getId(), systemBatteryType.getId());
        insertBattery(battery);

        AgentBatteryType agentBatteryType = newAgentBatteryType(agent.getId(), battery.getType());
        insertAgentBatteryType(agentBatteryType);

        ExchangePriceTime exchangePriceTime = newExchangePriceTime(agent.getId(), agentBatteryType.getBatteryType());
        insertExchangePriceTime(exchangePriceTime);

        ExchangeBatteryForegift exchangeBatteryForegift = newExchangeBatteryForegift(agent.getId(), systemBatteryType.getId());
        insertExchangeBatteryForegift(exchangeBatteryForegift);

        VipPrice vipPrice = newVipPrice(agent.getId(), systemBatteryType.getId());
        insertVipPrice(vipPrice);

        VipPriceCustomer vipPriceCustomer = newVipPriceCustomer(vipPrice.getId());
        insertVipPriceCustomer(vipPriceCustomer);

        Playlist playlist = newPlaylist(agent.getId());
        insertPlaylist(playlist);

        TerminalStrategy terminalStrategy = newTerminalStrategy(agent.getId());
        insertTerminalStrategy(terminalStrategy);

        Terminal terminal = newTerminal(agent.getId(), terminalStrategy.getId(), playlist.getId());
        insertTerminal(terminal);

        Cabinet cabinet = newCabinet(agent.getId(), terminal.getId());
        insertCabinet(cabinet);

        VipPriceCabinet vipPriceCabinet = newVipPriceCabinet(vipPrice.getId(), cabinet.getId());
        insertVipPriceCabinet(vipPriceCabinet);

        Station station = newStation(agent.getId());
        insertStation(station);

        VipPriceStation vipPriceStation = newVipPriceStation(vipPrice.getId(), station.getId());
        insertVipPriceStation(vipPriceStation);

        assertEquals(0, vipPriceService.vipStationDelete(vipPriceStation.getId()).getCode());
    }

    @Test
    public void vipCustomerDelete() {
        Partner partner = newPartner();
        insertPartner(partner);
        Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        SystemBatteryType systemBatteryType = newSystemBatteryType();
        insertSystemBatteryType(systemBatteryType);
        Battery battery = newBattery(agent.getId(), systemBatteryType.getId());
        insertBattery(battery);

        AgentBatteryType agentBatteryType = newAgentBatteryType(agent.getId(), battery.getType());
        insertAgentBatteryType(agentBatteryType);

        ExchangePriceTime exchangePriceTime = newExchangePriceTime(agent.getId(), agentBatteryType.getBatteryType());
        insertExchangePriceTime(exchangePriceTime);

        ExchangeBatteryForegift exchangeBatteryForegift = newExchangeBatteryForegift(agent.getId(), systemBatteryType.getId());
        insertExchangeBatteryForegift(exchangeBatteryForegift);

        VipPrice vipPrice = newVipPrice(agent.getId(), systemBatteryType.getId());
        insertVipPrice(vipPrice);

        VipPriceCustomer vipPriceCustomer = newVipPriceCustomer(vipPrice.getId());
        insertVipPriceCustomer(vipPriceCustomer);

        assertEquals(0, vipPriceService.vipCustomerDelete(vipPriceCustomer.getId()).getCode());

    }
}
