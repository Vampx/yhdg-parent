package cn.com.yusong.yhdg.agentserver.service.hdg;

import cn.com.yusong.yhdg.agentserver.service.BaseJunit4Test;
import cn.com.yusong.yhdg.common.constant.ConstEnum;
import cn.com.yusong.yhdg.common.domain.basic.*;
import cn.com.yusong.yhdg.common.domain.hdg.*;
import cn.com.yusong.yhdg.common.domain.yms.Playlist;
import cn.com.yusong.yhdg.common.domain.yms.Terminal;
import cn.com.yusong.yhdg.common.domain.yms.TerminalStrategy;
import cn.com.yusong.yhdg.common.entity.AreaCache;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class CabinetServiceTest extends BaseJunit4Test {

    @Autowired
    CabinetService service;
    @Autowired
    AreaCache areaCache;
    @Autowired
    CabinetDynamicCodeCustomerService cabinetDynamicCodeCustomerService;


    @Test
    public void findPage() {
        Partner partner = newPartner(); insertPartner(partner); Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        Playlist playlist = newPlaylist(agent.getId());
        insertPlaylist(playlist);

        TerminalStrategy terminalStrategy = newTerminalStrategy(agent.getId());
        insertTerminalStrategy(terminalStrategy);

        Terminal terminal = newTerminal(agent.getId(), terminalStrategy.getId(), playlist.getId());
        insertTerminal(terminal);

        Cabinet cabinet = newCabinet(agent.getId(), terminal.getId());
        insertCabinet(cabinet);

        assertTrue(1 == service.findPage(cabinet).getTotalItems());
        assertTrue(1 == service.findPage(cabinet).getResult().size());
    }

    @Test
    public void find() {
        Partner partner = newPartner(); insertPartner(partner); Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        Playlist playlist = newPlaylist(agent.getId());
        insertPlaylist(playlist);

        TerminalStrategy terminalStrategy = newTerminalStrategy(agent.getId());
        insertTerminalStrategy(terminalStrategy);

        Terminal terminal = newTerminal(agent.getId(), terminalStrategy.getId(), playlist.getId());
        insertTerminal(terminal);

        Cabinet cabinet = newCabinet(agent.getId(),  terminal.getId());
        insertCabinet(cabinet);

        assertNotNull(service.find(cabinet.getId()));
    }
    @Test
    public void findPageControl() {
        Partner partner = newPartner(); insertPartner(partner); Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        Playlist playlist = newPlaylist(agent.getId());
        insertPlaylist(playlist);

        TerminalStrategy terminalStrategy = newTerminalStrategy(agent.getId());
        insertTerminalStrategy(terminalStrategy);

        Terminal terminal = newTerminal(agent.getId(), terminalStrategy.getId(), playlist.getId());
        insertTerminal(terminal);

        Cabinet cabinet = newCabinet(agent.getId(),  terminal.getId());
        insertCabinet(cabinet);

        assertTrue(1 == service.findPageControl(cabinet).getTotalItems());
        assertTrue(1 == service.findPageControl(cabinet).getResult().size());
    }
    @Test
    public void statsPage() {
        Partner partner = newPartner(); insertPartner(partner); Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        Playlist playlist = newPlaylist(agent.getId());
        insertPlaylist(playlist);

        TerminalStrategy terminalStrategy = newTerminalStrategy(agent.getId());
        insertTerminalStrategy(terminalStrategy);

        Terminal terminal = newTerminal(agent.getId(), terminalStrategy.getId(), playlist.getId());
        insertTerminal(terminal);

        Cabinet cabinet = newCabinet(agent.getId(),  terminal.getId());
        insertCabinet(cabinet);

        CabinetBox cabinetBox = newCabinetBox(cabinet.getId(),"0-1");
        insertCabinetBox(cabinetBox);

        assertTrue(1 == service.statsPage(cabinetBox).getResult().size());
    }

    @Test
    public void findCabinetCount() {
        Partner partner = newPartner(); insertPartner(partner); Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        Playlist playlist = newPlaylist(agent.getId());
        insertPlaylist(playlist);

        TerminalStrategy terminalStrategy = newTerminalStrategy(agent.getId());
        insertTerminalStrategy(terminalStrategy);

        Terminal terminal = newTerminal(agent.getId(), terminalStrategy.getId(), playlist.getId());
        insertTerminal(terminal);

        Cabinet cabinet = newCabinet(agent.getId(),  terminal.getId());
        insertCabinet(cabinet);
        assertTrue(service.findCabinetCount(agent.getId())==1);
    }
    @Test
    public void findBatteryStateList() {
        Partner partner = newPartner(); insertPartner(partner); Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        Playlist playlist = newPlaylist(agent.getId());
        insertPlaylist(playlist);

        TerminalStrategy terminalStrategy = newTerminalStrategy(agent.getId());
        insertTerminalStrategy(terminalStrategy);

        Terminal terminal = newTerminal(agent.getId(), terminalStrategy.getId(), playlist.getId());
        insertTerminal(terminal);

        Cabinet cabinet1 = newCabinet(agent.getId(),  terminal.getId());
        insertCabinet(cabinet1);
        Cabinet cabinet2 = newCabinet(agent.getId(),  terminal.getId());
        cabinet2.setId("001234");
        insertCabinet(cabinet2);
        String[] cabinetIds = {cabinet1.getId(), cabinet2.getId()};
        assertNotNull(service.findBatteryStateList(cabinetIds));

    }
    @Test
    public void findByTerminalId() {
        Partner partner = newPartner(); insertPartner(partner); Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        Playlist playlist = newPlaylist(agent.getId());
        insertPlaylist(playlist);

        TerminalStrategy terminalStrategy = newTerminalStrategy(agent.getId());
        insertTerminalStrategy(terminalStrategy);

        Terminal terminal = newTerminal(agent.getId(), terminalStrategy.getId(), playlist.getId());
        insertTerminal(terminal);

        Cabinet cabinet = newCabinet(agent.getId(),  terminal.getId());
        insertCabinet(cabinet);

        assertNotNull(service.findByTerminalId(terminal.getId()));
    }
    @Test
    public void delete() {
        Partner partner = newPartner(); insertPartner(partner); Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        Playlist playlist = newPlaylist(agent.getId());
        insertPlaylist(playlist);

        TerminalStrategy terminalStrategy = newTerminalStrategy(agent.getId());
        insertTerminalStrategy(terminalStrategy);

        Terminal terminal = newTerminal(agent.getId(), terminalStrategy.getId(), playlist.getId());
        insertTerminal(terminal);

        Cabinet cabinet = newCabinet(agent.getId(),  terminal.getId());
        insertCabinet(cabinet);

        assertTrue(service.delete(cabinet.getId()).isSuccess());
    }

    @Test
    public void findUnique(){
        Partner partner = newPartner(); insertPartner(partner); Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        Playlist playlist = newPlaylist(agent.getId());
        insertPlaylist(playlist);

        TerminalStrategy terminalStrategy = newTerminalStrategy(agent.getId());
        insertTerminalStrategy(terminalStrategy);

        Terminal terminal = newTerminal(agent.getId(), terminalStrategy.getId(), playlist.getId());
        insertTerminal(terminal);

        Cabinet cabinet = newCabinet(agent.getId(),  terminal.getId());
        insertCabinet(cabinet);
        Cabinet cabinet2 = newCabinet(agent.getId(),  terminal.getId());
        cabinet2.setId("22222");
        assertTrue(service.findUnique(cabinet2.getId()).isSuccess());
    }
    @Test
    public void updateBasic() {
        Partner partner = newPartner(); insertPartner(partner); Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        Playlist playlist = newPlaylist(agent.getId());
        insertPlaylist(playlist);

        TerminalStrategy terminalStrategy = newTerminalStrategy(agent.getId());
        insertTerminalStrategy(terminalStrategy);

        Terminal terminal = newTerminal(agent.getId(), terminalStrategy.getId(), playlist.getId());
        insertTerminal(terminal);

        Cabinet cabinet = newCabinet(agent.getId(),  terminal.getId());
        cabinet.setDynamicCode("sadf");
        insertCabinet(cabinet);
        cabinet.setCabinetName("000142");

        Customer customer = newCustomer(partner.getId());
        insertCustomer(customer);

        CabinetDynamicCodeCustomer cabinetDynamicCodeCustomer = newCabinetDynamicCodeCustomer(cabinet.getId(), customer.getId());
        insertCabinetDynamicCodeCustomer(cabinetDynamicCodeCustomer);

        cabinet.setDynamicCode("3423");
        assertTrue(service.updateBasic(cabinet).isSuccess());
        assertNull(cabinetDynamicCodeCustomerService.find(cabinet.getId(), customer.getId()));
    }


    @Test
    public void updateImage() {
        Partner partner = newPartner(); insertPartner(partner); Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        Playlist playlist = newPlaylist(agent.getId());
        insertPlaylist(playlist);

        TerminalStrategy terminalStrategy = newTerminalStrategy(agent.getId());
        insertTerminalStrategy(terminalStrategy);

        Terminal terminal = newTerminal(agent.getId(), terminalStrategy.getId(), playlist.getId());
        insertTerminal(terminal);

        Cabinet cabinet = newCabinet(agent.getId(),  terminal.getId());
        insertCabinet(cabinet);
        cabinet.setImagePath1("xxxx/path/.../image1");
        cabinet.setImagePath2("xxxx/path/.../image2");
        cabinet.setImagePath3("xxxx/path/.../image3");
        assertTrue(service.updateImage(cabinet).isSuccess());
    }
    @Test
    public void updateCustomerType() {
        Partner partner = newPartner(); insertPartner(partner); Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        Playlist playlist = newPlaylist(agent.getId());
        insertPlaylist(playlist);

        TerminalStrategy terminalStrategy = newTerminalStrategy(agent.getId());
        insertTerminalStrategy(terminalStrategy);

        Terminal terminal = newTerminal(agent.getId(), terminalStrategy.getId(), playlist.getId());
        insertTerminal(terminal);

        Cabinet cabinet = newCabinet(agent.getId(),  terminal.getId());
        insertCabinet(cabinet);

        assertTrue(service.updateCustomerType(cabinet).isSuccess());
    }
    @Test
    public void updateLocation() {
        Partner partner = newPartner(); insertPartner(partner); Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        Playlist playlist = newPlaylist(agent.getId());
        insertPlaylist(playlist);

        TerminalStrategy terminalStrategy = newTerminalStrategy(agent.getId());
        insertTerminalStrategy(terminalStrategy);

        Terminal terminal = newTerminal(agent.getId(), terminalStrategy.getId(), playlist.getId());
        insertTerminal(terminal);

        Dept dept = newDept(agent.getId());
        insertDept(dept);

        Role role = newRole(agent.getId());
        insertRole(role);

        User user = newUser(agent.getId(), role.getId(), dept.getId());
        insertUser(user);

        Cabinet cabinet = newCabinet(agent.getId(),  terminal.getId());
        insertCabinet(cabinet);
        cabinet.setDispatcherId(user.getId());
        cabinet.setLat(30.355539);
        cabinet.setLng(120.016421);
        assertTrue(service.updateLocation(cabinet).isSuccess());
    }

    @Test
    public void updateNewLocation() {
        Partner partner = newPartner(); insertPartner(partner); Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        Playlist playlist = newPlaylist(agent.getId());
        insertPlaylist(playlist);

        TerminalStrategy terminalStrategy = newTerminalStrategy(agent.getId());
        insertTerminalStrategy(terminalStrategy);

        Terminal terminal = newTerminal(agent.getId(), terminalStrategy.getId(), playlist.getId());
        insertTerminal(terminal);

        Cabinet cabinet = newCabinet(agent.getId(),  terminal.getId());

        cabinet.setProvinceName("广东省");
        cabinet.setCityName("深圳市");
        cabinet.setDistrictName("福田区");
        insertCabinet(cabinet);
        cabinet.setLat(30.355539);
        cabinet.setLng(120.016421);

        Integer provinceId = null;
        Integer cityId = null;
        Integer districtId = null;
        if (cabinet.getProvinceName() != null) {
            Area area = areaCache.getByName(cabinet.getProvinceName());
            provinceId = Integer.valueOf(area.getAreaCode());
        }
        if (cabinet.getCityName() != null) {
            Area area = areaCache.getByName(cabinet.getCityName());
            cityId = Integer.valueOf(area.getAreaCode());
        }
        if (cabinet.getDistrictName() != null) {
            Area area = areaCache.getByName(cabinet.getDistrictName());
            districtId = Integer.valueOf(area.getAreaCode());
        }
        assertTrue(service.updateNewLocation(cabinet).isSuccess());
        assertEquals(provinceId, service.find(cabinet.getId()).getProvinceId());
        assertEquals(cityId, service.find(cabinet.getId()).getCityId());
        assertEquals(districtId, service.find(cabinet.getId()).getDistrictId());

        String provinceName = null;
        String cityName = null;
        String districtName = null;
        Cabinet newCabinet = service.find(cabinet.getId());
        if (newCabinet.getProvinceId() != null) {
            provinceName = areaCache.get(newCabinet.getProvinceId()).getAreaName();
        }
        if (newCabinet.getCityId() != null) {
            cityName = areaCache.get(newCabinet.getCityId()).getAreaName();
        }
        if (newCabinet.getDistrictId() != null) {
            districtName = areaCache.get(newCabinet.getDistrictId()).getAreaName();
        }
        assertEquals(provinceName,"广东省");
        assertEquals(cityName, "深圳市");
        assertEquals(districtName, "福田区");
    }

    @Test
    public void findByAgent() {
        Partner partner = newPartner(); insertPartner(partner); Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        Playlist playlist = newPlaylist(agent.getId());
        insertPlaylist(playlist);

        TerminalStrategy terminalStrategy = newTerminalStrategy(agent.getId());
        insertTerminalStrategy(terminalStrategy);

        Terminal terminal = newTerminal(agent.getId(), terminalStrategy.getId(), playlist.getId());
        insertTerminal(terminal);

        Cabinet cabinet = newCabinet(agent.getId(),  terminal.getId());
        insertCabinet(cabinet);
        cabinet.setDispatcherId(1l);

        assertNotNull(service.findByAgent(agent.getId(),cabinet.getDispatcherId(),agent.getProvinceId(),agent.getCityId(),agent.getDistrictId()));
    }
    @Test
    public void updateDispatcherById() {
        Partner partner = newPartner(); insertPartner(partner); Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        Playlist playlist = newPlaylist(agent.getId());
        insertPlaylist(playlist);

        TerminalStrategy terminalStrategy = newTerminalStrategy(agent.getId());
        insertTerminalStrategy(terminalStrategy);

        Terminal terminal = newTerminal(agent.getId(), terminalStrategy.getId(), playlist.getId());
        insertTerminal(terminal);

        Cabinet cabinet = newCabinet(agent.getId(),  terminal.getId());
        insertCabinet(cabinet);
        cabinet.setDispatcherId(1l);
        assertTrue(service.updateDispatcherById(cabinet.getId()).isSuccess());
    }
    @Test
    public void batchUpdateCabinetPriceGroup() {
        Partner partner = newPartner(); insertPartner(partner); Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        Playlist playlist = newPlaylist(agent.getId());
        insertPlaylist(playlist);

        TerminalStrategy terminalStrategy = newTerminalStrategy(agent.getId());
        insertTerminalStrategy(terminalStrategy);

        Terminal terminal = newTerminal(agent.getId(), terminalStrategy.getId(), playlist.getId());
        insertTerminal(terminal);

        Cabinet cabinet = newCabinet(agent.getId(),  terminal.getId());
        insertCabinet(cabinet);

    }
    @Test
    public void updateCabinetPriceGroup() {
        Partner partner = newPartner(); insertPartner(partner); Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        Playlist playlist = newPlaylist(agent.getId());
        insertPlaylist(playlist);

        TerminalStrategy terminalStrategy = newTerminalStrategy(agent.getId());
        insertTerminalStrategy(terminalStrategy);

        Terminal terminal = newTerminal(agent.getId(), terminalStrategy.getId(), playlist.getId());
        insertTerminal(terminal);

        Cabinet cabinet = newCabinet(agent.getId(),  terminal.getId());
        insertCabinet(cabinet);

    }

    @Test
    public void updatePrice() {
        Partner partner = newPartner(); insertPartner(partner); Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        Playlist playlist = newPlaylist(agent.getId());
        insertPlaylist(playlist);

        TerminalStrategy terminalStrategy = newTerminalStrategy(agent.getId());
        insertTerminalStrategy(terminalStrategy);

        Terminal terminal = newTerminal(agent.getId(), terminalStrategy.getId(), playlist.getId());
        insertTerminal(terminal);

        VipPrice vipPrice = newVipPrice(agent.getId(), ConstEnum.Flag.TRUE.getValue());
        insertVipPrice(vipPrice);

        Cabinet cabinet = newCabinet(agent.getId(),  terminal.getId());
        insertCabinet(cabinet);

        VipPriceCabinet vipPriceCabinet = newVipPriceCabinet(vipPrice.getId(), cabinet.getId());
        insertVipPriceCabinet(vipPriceCabinet);

        VipExchangeBatteryForegift vipExchangeBatteryForegift = newVipExchangeBatteryForegift(agent.getId(), vipPrice.getId(),vipPrice.getId());
        insertVipExchangeBatteryForegift(vipExchangeBatteryForegift);

        ExchangeBatteryForegift exchangeBatteryForegift = newExchangeBatteryForegift(agent.getId(),ConstEnum.Flag.TRUE.getValue());
        insertExchangeBatteryForegift(exchangeBatteryForegift);

        VipPacketPeriodPrice vipPacketPeriodPrice = newVipPacketPeriodPrice(vipExchangeBatteryForegift.getId(), vipPrice.getId(),agent.getId(),vipExchangeBatteryForegift.getForegiftId());
        insertVipPacketPeriodPrice(vipPacketPeriodPrice);

        CabinetBatteryType cabinetBatteryType = newCabinetBatteryType(cabinet.getId(),ConstEnum.Flag.TRUE.getValue());
        insertCabinetBatteryType(cabinetBatteryType);

        PacketPeriodPrice packetPeriodPrice = newPacketPeriodPrice(agent.getId(), exchangeBatteryForegift.getId());
        insertPacketPeriodPrice(packetPeriodPrice);

        assertEquals(1, service.updatePrice(agent.getId(),cabinet.getId()));
    }

    @Test
    public void updateShopPrice() {
        Partner partner = newPartner(); insertPartner(partner); Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        Playlist playlist = newPlaylist(agent.getId());
        insertPlaylist(playlist);

        TerminalStrategy terminalStrategy = newTerminalStrategy(agent.getId());
        insertTerminalStrategy(terminalStrategy);

        Terminal terminal = newTerminal(agent.getId(), terminalStrategy.getId(), playlist.getId());
        insertTerminal(terminal);

        VipPrice vipPrice = newVipPrice(agent.getId(), ConstEnum.Flag.TRUE.getValue());
        insertVipPrice(vipPrice);

        Cabinet cabinet = newCabinet(agent.getId(),  terminal.getId());
        insertCabinet(cabinet);

        VipPriceCabinet vipPriceCabinet = newVipPriceCabinet(vipPrice.getId(), cabinet.getId());
        insertVipPriceCabinet(vipPriceCabinet);

        VipExchangeBatteryForegift vipExchangeBatteryForegift = newVipExchangeBatteryForegift(agent.getId(), vipPrice.getId(),vipPrice.getId());
        insertVipExchangeBatteryForegift(vipExchangeBatteryForegift);

        ExchangeBatteryForegift exchangeBatteryForegift = newExchangeBatteryForegift(agent.getId(),ConstEnum.Flag.TRUE.getValue());
        insertExchangeBatteryForegift(exchangeBatteryForegift);

        VipPacketPeriodPrice vipPacketPeriodPrice = newVipPacketPeriodPrice(vipExchangeBatteryForegift.getId(),vipPrice.getId(),agent.getId(),vipExchangeBatteryForegift.getForegiftId());
        insertVipPacketPeriodPrice(vipPacketPeriodPrice);

        CabinetBatteryType cabinetBatteryType = newCabinetBatteryType(cabinet.getId(),ConstEnum.Flag.TRUE.getValue());
        insertCabinetBatteryType(cabinetBatteryType);

        PacketPeriodPrice packetPeriodPrice = newPacketPeriodPrice(agent.getId(), exchangeBatteryForegift.getId());
        insertPacketPeriodPrice(packetPeriodPrice);

        Shop shop = newShop(agent.getId());
        insertShop(shop);

        assertEquals(1, service.updateShopPrice(vipPrice.getId(),shop.getId()));
    }

    @Test
    public void updateShopByCabinet() {
        Partner partner = newPartner();
        insertPartner(partner);

        Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        TerminalStrategy terminalStrategy = newTerminalStrategy(agent.getId());
        insertTerminalStrategy(terminalStrategy);

        Playlist playlist = newPlaylist(agent.getId());
        insertPlaylist(playlist);

        Terminal terminal = newTerminal(agent.getId(), terminalStrategy.getId(), playlist.getId());
        insertTerminal(terminal);

        Shop shop = newShop(agent.getId());
        insertShop(shop);

        Cabinet cabinet = newCabinet(agent.getId(),terminal.getId());
        cabinet.setShopId(shop.getId());
        cabinet.setMinPrice(1000);
        cabinet.setMaxPrice(20000);
        insertCabinet(cabinet);

        assertEquals(1, service.updateShopPriceByCabint(shop.getId()));
    }
}

