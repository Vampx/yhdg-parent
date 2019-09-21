package cn.com.yusong.yhdg.agentappserver.service.hdg;

import cn.com.yusong.yhdg.agentappserver.BaseJunit4Test;
import cn.com.yusong.yhdg.common.domain.basic.*;
import cn.com.yusong.yhdg.common.domain.hdg.*;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;

public class ShopServiceTest extends BaseJunit4Test {
    @Autowired
    ShopService service;

    @Test
    public void find() {
        Partner partner = newPartner();
        insertPartner(partner);
        Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        Shop shop = newShop(agent.getId());
        insertShop(shop);

        assertNotNull(service.find(shop.getId()));
    }

    @Test
    public void findList() {
        Partner partner = newPartner();
        insertPartner(partner);
        Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        Shop shop = newShop(agent.getId());
        insertShop(shop);

        assertTrue(1 == service.findList(agent.getId(), "", 0, 20).size());
    }

    @Test
    public void findDetail() {
        Partner partner = newPartner();
        insertPartner(partner);
        Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        Shop shop = newShop(agent.getId());
        insertShop(shop);

        Role role = newRole(agent.getId());
        insertRole(role);

        Dept dept = newDept(agent.getId());
        insertDept(dept);

        User user = newUser(agent.getId(), role.getId(), dept.getId());
        insertUser(user);

        ShopUser shopUser = newShopUser(shop.getId(), agent.getId(), user.getId());
        insertShopUser(shopUser);

        Map<String, Object> map = service.findDetail(shop.getId());
        assertEquals(map.get("id"), shop.getId());
        assertEquals(map.get("address"), shop.getAddress());
        assertEquals(map.get("shopName"), shop.getShopName());
        assertEquals(map.get("provinceId"), shop.getProvinceId());
        assertEquals(map.get("cityId"), shop.getCityId());
        assertEquals(map.get("districtId"), shop.getDistrictId());
        assertEquals(map.get("street"), shop.getStreet());
        assertEquals(map.get("openTime"), shop.getWorkTime());

    }


    @Test
    public void create() {
        Partner partner = newPartner();
        insertPartner(partner);
        Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        Shop shop = newShop(agent.getId());
        Long[] userList = {200L, 201L, 202L};

        Role role = newRole(agent.getId());
        insertRole(role);

        Dept dept = newDept(agent.getId());
        insertDept(dept);

        User user = newUser(agent.getId(), role.getId(), dept.getId());
        user.setId(200L);
        insertUser(user);
        user.setLoginName("3ede23");
        user.setId(201L);
        insertUser(user);
        user.setId(202L);
        user.setLoginName("d3rt45t");
        insertUser(user);

        String[] imagePath = {"1", "2", null};
        Integer areaId = 110107;
        assertTrue(service.create(shop.getShopName(), areaId, shop.getStreet(), shop.getWorkTime(), userList, shop.getLng(), shop.getLat(), imagePath, agent.getId()).isSuccess());
    }

    @Test
    public void update() {
        Partner partner = newPartner();
        insertPartner(partner);
        Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        Shop shop = newShop(agent.getId());
        insertShop(shop);

        Role role = newRole(agent.getId());
        insertRole(role);

        Dept dept = newDept(agent.getId());
        insertDept(dept);

        Long[] userList = {200L, 201L, 202L};
        User user = newUser(agent.getId(), role.getId(), dept.getId());
        user.setId(200L);
        insertUser(user);
        user.setLoginName("3ede23");
        user.setId(201L);
        insertUser(user);
        user.setId(202L);
        user.setLoginName("d3rt45t");
        insertUser(user);
        Integer areaId = 110107;
        String[] imagePath = {"1", "2", null};

        service.update("111", "111", "sss", "111", "sss");
    }

    @Test
    public void delete() {
        Partner partner = newPartner();
        insertPartner(partner);
        Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        Shop shop = newShop(agent.getId());
        insertShop(shop);

        assertTrue(service.delete(shop.getId()).isSuccess());
        assertNull(service.find(shop.getId()));

    }

}
