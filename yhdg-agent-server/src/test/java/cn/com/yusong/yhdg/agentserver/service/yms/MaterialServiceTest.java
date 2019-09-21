package cn.com.yusong.yhdg.agentserver.service.yms;

import cn.com.yusong.yhdg.common.domain.basic.*;
import cn.com.yusong.yhdg.common.domain.yms.Material;
import cn.com.yusong.yhdg.common.domain.yms.MaterialGroup;
import cn.com.yusong.yhdg.common.entity.pagination.Page;
import cn.com.yusong.yhdg.agentserver.service.BaseJunit4Test;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class MaterialServiceTest extends BaseJunit4Test {

    @Autowired
    MaterialService materialService;

    @Test
    public void find() {

        Partner partner = newPartner(); insertPartner(partner); Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        MaterialGroup materialGroup = newMaterialGroup(agent.getId());
        insertMaterialGroup(materialGroup);

        Role role = newRole(agent.getId());
        insertRole(role);

        Dept dept = newDept(agent.getId());
        insertDept(dept);

        User user = newUser(agent.getId(), role.getId(), dept.getId());
        insertUser(user);

        Material material = newMaterial(1, agent.getId(), materialGroup.getId(), user.getId());
        insertMaterial(material);

        assertNotNull(materialService.find(material.getId()));
    }

    @Test
    public void findPage() {
        Partner partner = newPartner(); insertPartner(partner); Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        MaterialGroup materialGroup = newMaterialGroup(agent.getId());
        insertMaterialGroup(materialGroup);

        Role role = newRole(agent.getId());
        insertRole(role);

        Dept dept = newDept(agent.getId());
        insertDept(dept);

        User user = newUser(agent.getId(), role.getId(), dept.getId());
        insertUser(user);

        Material material = newMaterial(1, agent.getId(), materialGroup.getId(), user.getId());
        insertMaterial(material);

        Page page = materialService.findPage(material);
        assertEquals(1, page.getTotalItems());
    }

    @Test
    public void updateBasicInfo(){
        Partner partner = newPartner(); insertPartner(partner); Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        MaterialGroup materialGroup = newMaterialGroup(agent.getId());
        insertMaterialGroup(materialGroup);

        Role role = newRole(agent.getId());
        insertRole(role);

        Dept dept = newDept(agent.getId());
        insertDept(dept);

        User user = newUser(agent.getId(), role.getId(), dept.getId());
        insertUser(user);

        Material material = newMaterial(1, agent.getId(), materialGroup.getId(), user.getId());
        insertMaterial(material);

        Material entity = new Material();
        entity.setId(material.getId());
        entity.setGroupId(materialGroup.getId());
        entity.setDuration(120);
        entity.setVersion(2);
        entity.setMaterialType(material.getMaterialType());
        assertTrue("update fail", materialService.updateBasicInfo(entity).isSuccess());
    }

    @Test
    public void delete(){
        Partner partner = newPartner(); insertPartner(partner); Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        MaterialGroup materialGroup = newMaterialGroup(agent.getId());
        insertMaterialGroup(materialGroup);

        Role role = newRole(agent.getId());
        insertRole(role);

        Dept dept = newDept(agent.getId());
        insertDept(dept);

        User user = newUser(agent.getId(), role.getId(), dept.getId());
        insertUser(user);

        Material material = newMaterial(1, agent.getId(), materialGroup.getId(), user.getId());
        insertMaterial(material);

        assertTrue("delete fail", materialService.delete(material.getId()).isSuccess());
    }

}
