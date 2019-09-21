package cn.com.yusong.yhdg.agentserver.service.yms;

import cn.com.yusong.yhdg.common.domain.basic.Agent;
import cn.com.yusong.yhdg.common.domain.basic.Partner;
import cn.com.yusong.yhdg.common.domain.yms.MaterialGroup;
import cn.com.yusong.yhdg.agentserver.service.BaseJunit4Test;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class MaterialGroupServiceTest extends BaseJunit4Test {
    @Autowired
    MaterialGroupService service;

    @Test
    public void find() {
        Partner partner = newPartner(); insertPartner(partner); Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        MaterialGroup group = newMaterialGroup(agent.getId());
        insertMaterialGroup(group);

        assertNotNull(service.find(group.getId()));
    }

    @Test
    public void findPage() {
        Partner partner = newPartner(); insertPartner(partner); Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        MaterialGroup group = newMaterialGroup(agent.getId());
        insertMaterialGroup(group);

        assertTrue(1 == service.findPage(group).getTotalItems());
        assertTrue(1 == service.findPage(group).getResult().size());
    }

    @Test
    public void insert() {
        Partner partner = newPartner(); insertPartner(partner); Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        MaterialGroup group = newMaterialGroup(agent.getId());

        assertEquals(1, service.insert(group));
    }

    @Test
    public void update() {
        Partner partner = newPartner(); insertPartner(partner); Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        MaterialGroup group = newMaterialGroup(agent.getId());
        insertMaterialGroup(group);

        group.setGroupName("test1");
        assertEquals(1, service.update(group));

        String groupName = (String) jdbcTemplate.queryForMap("select group_name from yms_material_group where id = ?", group.getId()).get("group_name");
        assertEquals("test1", groupName);
    }

    @Test
    public void delete() {
        Partner partner = newPartner(); insertPartner(partner); Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        MaterialGroup group = newMaterialGroup(agent.getId());
        insertMaterialGroup(group);

        assertEquals(true, service.delete(group.getId()).isSuccess());
    }
}
