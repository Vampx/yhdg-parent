package cn.com.yusong.yhdg.frontserver.service.yms;

import cn.com.yusong.yhdg.common.domain.basic.*;
import cn.com.yusong.yhdg.common.domain.yms.*;
import cn.com.yusong.yhdg.frontserver.BaseJunit4Test;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class PublishedMaterialServiceTest extends BaseJunit4Test {
    @Autowired
    PublishedMaterialService service;

    @Test
    public void find() {

        Partner partner = newPartner(); insertPartner(partner); Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        Role role = newRole(agent.getId());
        insertRole(role);

        Dept dept = newDept(agent.getId());
        insertDept(dept);

        User user = newUser(agent.getId(), role.getId(), dept.getId());
        insertUser(user);

        MaterialGroup materialGroup = newMaterialGroup(agent.getId());
        insertMaterialGroup(materialGroup);

        Material material = newMaterial(1, agent.getId(), materialGroup.getId(), user.getId());
        insertMaterial(material);

        PublishedMaterial publishedMaterial = newPublishedMaterial(material);
        insertPublishedMaterial(publishedMaterial);

        assertNotNull(service.find(publishedMaterial.getMaterialId(), publishedMaterial.getVersion()));
    }

    @Test
    public void findByDetail() {
        Partner partner = newPartner(); insertPartner(partner); Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        Role role = newRole(agent.getId());
        insertRole(role);

        Dept dept = newDept(agent.getId());
        insertDept(dept);

        User user = newUser(agent.getId(), role.getId(), dept.getId());
        insertUser(user);

        MaterialGroup materialGroup = newMaterialGroup(agent.getId());
        insertMaterialGroup(materialGroup);

        Material material = newMaterial(1, agent.getId(), materialGroup.getId(), user.getId());
        insertMaterial(material);

        Playlist playlist = newPlaylist(agent.getId());
        insertPlaylist(playlist);

        PlayListDetail playListDetail = newPlayListDetail(playlist.getId());
        insertPlayListDetail(playListDetail);

        /********************发布库**************************/

        PublishedMaterial publishedMaterial = newPublishedMaterial(material);
        insertPublishedMaterial(publishedMaterial);

//        PublishedPlaylistDetailMaterial publishedPlaylistDetailMaterial = newPublishedPlaylistDetailMaterial(publishedPlaylistDetail.getDetailId(), publishedMaterial.getMaterialId());
//        insertPublishedPlaylistDetailMaterial(publishedPlaylistDetailMaterial);
//
//        assertTrue(service.findByDetail(publishedPlaylistDetail.getDetailId()).size() > 0);
    }

}
