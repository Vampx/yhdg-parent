package cn.com.yusong.yhdg.appserver.service.zc;

import cn.com.yusong.yhdg.appserver.BaseJunit4Test;
import cn.com.yusong.yhdg.common.domain.basic.Agent;
import cn.com.yusong.yhdg.common.domain.basic.Partner;
import cn.com.yusong.yhdg.common.domain.hdg.Shop;
import cn.com.yusong.yhdg.common.domain.zc.PriceSetting;
import cn.com.yusong.yhdg.common.domain.zc.VehicleModel;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class PriceSettingServiceTest extends BaseJunit4Test{


    @Autowired
    PriceSettingService priceSettingService;
    @Test
    public void findShopIdAll() throws Exception {

        Partner partner = newPartner();
        insertPartner(partner);
        Agent agent = newAgent(partner.getId());
        insertAgent(agent);
        Shop shop = newShop(agent.getId());
        insertShop(shop);
        VehicleModel vehicleModel = newVehicleModel(agent.getId());
        insertVehicleModel(vehicleModel);
        PriceSetting priceSetting = newPriceSetting(agent.getId(),vehicleModel.getId());
        insertPriceSetting(priceSetting);
        assertNotNull(priceSettingService.findShopIdAll(shop.getId()));

    }
    @Test
    public void findAgentIdAll() throws Exception {

        Partner partner =newPartner();
        insertPartner(partner);
        Agent agent = newAgent(partner.getId());
        insertAgent(agent);
        Agent agent1 = newAgent(partner.getId());
        insertAgent(agent1);
        Shop shop = newShop(agent.getId());
        insertShop(shop);
        VehicleModel vehicleModel = newVehicleModel(agent.getId());
        insertVehicleModel(vehicleModel);
        VehicleModel vehicleModel1 = newVehicleModel(agent.getId());
        insertVehicleModel(vehicleModel1);
        PriceSetting priceSetting = newPriceSetting(agent.getId(),vehicleModel.getId());
        insertPriceSetting(priceSetting);
        PriceSetting priceSetting1 = newPriceSetting(agent1.getId(),vehicleModel1.getId());
        insertPriceSetting(priceSetting1);
        List<Integer> list = new ArrayList<Integer>();
        list.add(agent.getId());
        list.add(agent1.getId());
        assertEquals(2,priceSettingService.findAgentIdAll(list).size());

    }

}