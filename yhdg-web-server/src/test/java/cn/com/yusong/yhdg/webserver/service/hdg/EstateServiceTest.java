package cn.com.yusong.yhdg.webserver.service.hdg;

import cn.com.yusong.yhdg.common.domain.basic.Agent;
import cn.com.yusong.yhdg.common.domain.basic.Customer;
import cn.com.yusong.yhdg.common.domain.basic.Partner;
import cn.com.yusong.yhdg.common.domain.hdg.Estate;
import cn.com.yusong.yhdg.webserver.service.BaseJunit4Test;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.*;

public class EstateServiceTest extends BaseJunit4Test {

    @Autowired
    EstateService estateService;
    @Test
    public void findPage() throws Exception {
        Partner partner = newPartner();
        insertPartner(partner);
        Agent agent = newAgent(partner.getId());
        insertAgent(agent);
        Estate estate= newEstate(agent.getId());
        insertEstate(estate);
        assertTrue(1 == estateService.findPage(estate).getTotalItems());
        assertTrue(1 ==  estateService.findPage(estate).getResult().size());
    }

    @Test
    public void find() throws Exception {
        Partner partner = newPartner();
        insertPartner(partner);
        Agent agent = newAgent(partner.getId());
        insertAgent(agent);
        Estate estate= newEstate(agent.getId());
        insertEstate(estate);
        assertNotNull(estateService.find(estate.getId()));
    }

    @Test
    public void delete() throws Exception {
        Partner partner = newPartner();
        insertPartner(partner);
        Agent agent = newAgent(partner.getId());
        insertAgent(agent);
        Estate estate= newEstate(agent.getId());
        insertEstate(estate);
        assertTrue(estateService.delete(estate.getId()).isSuccess());
       // assertNotNull(estateService.find(estate.getId()));
    }

    @Test
    public void insert() throws Exception {
        Partner partner = newPartner();
        insertPartner(partner);
        Agent agent = newAgent(partner.getId());
        insertAgent(agent);
        Estate estate= newEstate(agent.getId());
        insertEstate(estate);
        assertNotNull(estateService.insert(estate));
    }

    @Test
    public void updateBasic() throws Exception {
        Partner partner = newPartner();
        insertPartner(partner);
        Agent agent = newAgent(partner.getId());
        insertAgent(agent);
        Estate estate= newEstate(agent.getId());
        insertEstate(estate);
        assertNotNull(estateService.updateBasic(estate).isSuccess());
    }

    @Test
    public void tree() throws Exception {
    }

    @Test
    public void tree1() throws Exception {
    }

    @Test
    public void updatePayPeople() throws Exception {
        Partner partner = newPartner();
        insertPartner(partner);
        Agent agent = newAgent(partner.getId());
        insertAgent(agent);
        Estate estate= newEstate(agent.getId());
        insertEstate(estate);
        String payPeopleName="张鑫";
        String payPeopleMpOpenId="1111111";
        String payPeopleFwOpenId="1568574";
        String payPeopleMobile="18456954714";
        String payPassword="14563657895";
        estateService.updatePayPeople(estate.getId(),payPeopleName,payPeopleMpOpenId,payPeopleFwOpenId,payPeopleMobile,payPassword);
    }

    @Test
    public void setPayPeopleUpdate() throws Exception {
        Partner partner = newPartner();
        insertPartner(partner);
        Agent agent = newAgent(partner.getId());
        insertAgent(agent);
        Customer customer =newCustomer(partner.getId());
        insertCustomer(customer);
        Estate estate= newEstate(agent.getId());
        estate.setCustomerId(customer.getId());
        insertEstate(estate);
        assertTrue(estateService.setPayPeopleUpdate(estate).isSuccess());
    }

}