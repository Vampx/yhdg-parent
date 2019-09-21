package cn.com.yusong.yhdg.agentserver.service.hdg;

import cn.com.yusong.yhdg.common.domain.basic.*;
import cn.com.yusong.yhdg.common.domain.hdg.Shop;
import cn.com.yusong.yhdg.common.domain.hdg.ShopUser;
import cn.com.yusong.yhdg.agentserver.service.BaseJunit4Test;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class ShopUserServiceTest extends BaseJunit4Test {
    @Autowired
    ShopUserService service;

    @Test
    public void find() {
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
        shopUser.setFullname("asdfqwer");
        insertShopUser(shopUser);

        assertNotNull(service.find(shop.getId(), user.getId().intValue()));
    }

    @Test
    public void findPage() {
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

        assertTrue(1 == service.findPage(shopUser).getTotalItems());
        assertTrue(1 == service.findPage(shopUser).getResult().size());
    }

    @Test
    public void create() {
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

        assertTrue(service.create(shopUser).isSuccess());
    }

    @Test
    public void delete() {
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

        assertTrue(1 == service.delete(shop.getId(), user.getId().intValue()));

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

        User user = newUser(agent.getId(), role.getId(), dept.getId());
        insertUser(user);

        User user1 = newUser(agent.getId(), role.getId(), dept.getId());
        user1.setLoginName("demo");
        insertUser(user1);

        ShopUser shopUser = newShopUser(shop.getId(), agent.getId(), user.getId());
        insertShopUser(shopUser);

        shopUser.setUserId(user1.getId().intValue());
        assertTrue(service.update(user.getId().intValue(), user1.getId().intValue(), shop.getId()).isSuccess());
    }
}
