package cn.com.yusong.yhdg.appserver.service.basic;

import cn.com.yusong.yhdg.appserver.BaseJunit4Test;
import cn.com.yusong.yhdg.common.constant.CacheKey;
import cn.com.yusong.yhdg.common.constant.Constant;
import cn.com.yusong.yhdg.common.domain.basic.*;
import cn.com.yusong.yhdg.common.tool.memcached.MemCachedConfig;
import cn.com.yusong.yhdg.common.utils.CodecUtils;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

/**
 * Created by Administrator on 2017/11/4.
 */
public class CustomerServiceTest extends BaseJunit4Test {

    @Autowired
    CustomerService customerService;

    @Test
    public void find() {
        Partner partner = newPartner();
        insertPartner(partner);

        Customer customer = newCustomer(partner.getId());
        insertCustomer(customer);
        Customer customer1 = customerService.find(customer.getId());
        assertNotNull(customer1);
    }

    @Test
    public void findByMpOpenId() {
        Partner partner = newPartner();
        insertPartner(partner);

        Customer customer = newCustomer(partner.getId());
        insertCustomer(customer);
        assertNull(customerService.findByMpOpenId(partner.getId(), customer.getMpOpenId()));
    }

    @Test
    public void findByMobile() {
        Partner partner = newPartner();
        insertPartner(partner);

        Customer customer = newCustomer(partner.getId());
        insertCustomer(customer);
        Customer customer1 = customerService.findByMobile(partner.getId(), customer.getMobile());
        assertNotNull(customer1);
    }

    @Test
    public void findByFwOpenId() {
        Partner partner = newPartner();
        insertPartner(partner);

        Customer customer = newCustomer(partner.getId());
        insertCustomer(customer);
        Customer customer1 = customerService.findByFwOpenId(partner.getId(), customer.getMobile());
        assertNull(customer1);
    }

    @Test
    public void findFaceList() {
        Partner partner = newPartner();
        insertPartner(partner);

        Customer customer = newCustomer(partner.getId());
        insertCustomer(customer);
        assertNotNull(customerService.findFaceList(0, 10));
    }

    @Test
    public void insert() {
        Partner partner = newPartner();
        insertPartner(partner);

        Customer customer = newCustomer(partner.getId());
        int flag = customerService.insert(customer);
        assertEquals(1, flag);
    }

    @Test
    public void updateMobile() {
        Partner partner = newPartner();
        insertPartner(partner);

        Customer customer = newCustomer(partner.getId());
        insertCustomer(customer);
        customerService.updateMobile(customer.getId(), partner.getId(),"0000");
    }

    @Test
    public void updatePassword() {
        Partner partner = newPartner();
        insertPartner(partner);

        Customer customer = newCustomer(partner.getId());
        insertCustomer(customer);
        assertEquals(1, customerService.updatePassword(customer.getId(), CodecUtils.password(Constant.DEFAULT_PASSWORD), "654321"));
    }

    @Test
    public void updatePassword2() {
        Partner partner = newPartner();
        insertPartner(partner);

        Customer customer = newCustomer(partner.getId());
        insertCustomer(customer);
        assertEquals(1, customerService.updatePassword2(customer.getId(), CodecUtils.password("654321")));
    }

    @Test
    public void updateInfo() {
        Partner partner = newPartner();
        insertPartner(partner);

        Customer customer = newCustomer(partner.getId());
        insertCustomer(customer);
        assertEquals(1, customerService.updateInfo(customer.getId(), customer.getPhotoPath(), customer.getFacePath1(), customer.getFacePath2(), customer.getFacePath3()));
    }

    /*@Test
    public void updateIndependentCustomer() {
        Customer customer = newCustomer(partner.getId());
        insertCustomer(customer);
        assertEquals(1,customerService.updateIndependentCustomer(
                customer.getId(),customer.getCityName(),
                customer.getMobile(),customer.getIdCard(),
                customer.getIsActive(),1,
                1,2
        ));
    }*/

    @Test
    public void updateCertification() {
        Partner partner = newPartner();
        insertPartner(partner);

        Customer customer = newCustomer(partner.getId());
        insertCustomer(customer);

        customer.setFullname("BJ");
        customer.setIdCard("123456789");
        int flag = customerService.updateCertification(customer);
        assertEquals(1, flag);

    }

    /*@Test
    public  void updateBatteryType(){

        Customer customer = newCustomer(partner.getId());
        insertCustomer(customer);
        customerService.updateBatteryType(customer.getId(),2);

        assertEquals(2,jdbcTemplate.queryForInt("select battery_type from bas_customer where id = ?",customer.getId()));

    }*/

    @Test
    public void findLoginToken() {
        Partner partner = newPartner();
        insertPartner(partner);

        Customer customer = newCustomer(partner.getId());
        customer.setLoginToken("111");
        insertCustomer(customer);

        customerService.findLoginToken(customer.getId());
        String key = CacheKey.key(CacheKey.K_CUSTOMER_ID_V_LOGIN_TOKEN, customer.getId());
        assertNotNull(memCachedClient.get(key));
    }

    @Test
    public void updateLoginToken() {
        Partner partner = newPartner();
        insertPartner(partner);

        Customer customer = newCustomer(partner.getId());
        customer.setLoginToken("111");
        insertCustomer(customer);

        String key = CacheKey.key(CacheKey.K_CUSTOMER_ID_V_LOGIN_TOKEN, customer.getId());

        memCachedClient.set(key, "111", MemCachedConfig.CACHE_THREE_DAY);
        customerService.updateLoginToken(customer.getId(), "222", null, null);

        assertNull(memCachedClient.get(key));
    }

    @Test
    public void updateMpLoginToken() {
        Partner partner = newPartner();
        insertPartner(partner);

        Customer customer = newCustomer(partner.getId());
        customer.setMpLoginToken("111");
        insertCustomer(customer);
        assertEquals(1, customerService.updateMpLoginToken(customer.getId(), "222"));
    }

    @Test
    public void updateFwLoginToken() {
        Partner partner = newPartner();
        insertPartner(partner);

        Customer customer = newCustomer(partner.getId());
        customer.setFwLoginToken("111");
        insertCustomer(customer);
        assertEquals(1, customerService.updateFwLoginToken(customer.getId(), "222"));
    }

    @Test
    public void updateLoginTime() {
        Partner partner = newPartner();
        insertPartner(partner);

        Customer customer = newCustomer(partner.getId());
        insertCustomer(customer);
        assertEquals(1, customerService.updateLoginTime(customer.getId(), new Date(), customer.getLoginType()));
    }

    @Test
    public void mpBindMobile() {
        Partner partner = newPartner();
        insertPartner(partner);

        Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        Laxin laxin = newLaxin(partner.getId(), agent.getId());
        insertLaxin(laxin);

        LaxinCustomer laxinCustomer = newLaxinCustomer(partner.getId(), agent.getId(), laxin.getId());
        insertLaxinCustomer(laxinCustomer);

        Customer customer = newCustomer(partner.getId());
        customer.setMpOpenId("111");
        insertCustomer(customer);
        customerService.mpBindMobile(partner.getId(), customer.getMpOpenId(), customer.getMobile());
    }

    @Test
    public void fwBindMobile() {
        Partner partner = newPartner();
        insertPartner(partner);

        Customer customer = newCustomer(partner.getId());
        customer.setFwOpenId("111");
        insertCustomer(customer);
        customerService.fwBindMobile(partner.getId(), customer.getFwOpenId(), customer.getMobile());
    }

    @Test
    public void updatePushToken() {
        Partner partner = newPartner();
        insertPartner(partner);

        Customer customer = newCustomer(partner.getId());
        customer.setPushToken("111");
        insertCustomer(customer);
        assertEquals(1, customerService.updateLoginToken(customer.getId(), "222", new Date(), 1));
    }

    @Test
    public void mpUnbindMobile() {
        Partner partner = newPartner();
        insertPartner(partner);

        Customer customer = newCustomer(partner.getId());
        customer.setMpOpenId("111");
        insertCustomer(customer);
        assertEquals(1, customerService.mpUnbindMobile(customer.getId(), partner.getId(), customer.getMpOpenId()));
    }

    @Test
    public void fwUnbindMobile() {
        Partner partner = newPartner();
        insertPartner(partner);

        Customer customer = newCustomer(partner.getId());
        customer.setFwOpenId("111");
        insertCustomer(customer);
        assertEquals(1, customerService.fwUnbindMobile(customer.getId(), partner.getId(), customer.getMpOpenId()));
    }

    @Test
    public void setPayPassWord() {
        Partner partner = newPartner();
        insertPartner(partner);

        Customer customer = newCustomer(partner.getId());
        customer.setFwOpenId("111");
        insertCustomer(customer);
        assertEquals(1, customerService.setPayPassword(customer.getId(), "123"));
    }

    @Test
    public void updateAuthFacePath() {
        Partner partner = newPartner();
        insertPartner(partner);

        Customer customer = newCustomer(partner.getId());
        insertCustomer(customer);
        assertEquals(1, customerService.updateAuthFacePath(customer.getId(), "111"));
    }

    @Test
    public void updateCertification2() {
        Partner partner = newPartner();
        insertPartner(partner);

        Customer customer = newCustomer(partner.getId());
        insertCustomer(customer);
        assertEquals(1, customerService.updateCertification2(customer.getId(), "11", "111",1));
    }
}
