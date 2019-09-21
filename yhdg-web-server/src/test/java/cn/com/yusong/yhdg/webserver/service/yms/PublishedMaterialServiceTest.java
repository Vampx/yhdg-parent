package cn.com.yusong.yhdg.webserver.service.yms;

import cn.com.yusong.yhdg.common.domain.basic.Agent;import cn.com.yusong.yhdg.common.domain.basic.Partner;
import cn.com.yusong.yhdg.common.domain.basic.Dept;
import cn.com.yusong.yhdg.common.domain.basic.Role;
import cn.com.yusong.yhdg.common.domain.basic.User;
import cn.com.yusong.yhdg.common.domain.yms.*;
import cn.com.yusong.yhdg.webserver.service.BaseJunit4Test;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class PublishedMaterialServiceTest extends BaseJunit4Test {
    @Autowired
    PublishedMaterialService service;

//    @Test
//    public void findByAreaAndDetail() {
//        Partner partner = newPartner(); insertPartner(partner); Agent agent = newAgent(partner.getId());
//        insertAgent(agent);
//
//        Role role = newRole(agent.getId());
//        insertRole(role);
//
//        Dept dept = newDept(agent.getId());
//        insertDept(dept);
//
//        User user = newUser(agent.getId(), role.getId(), dept.getId());
//        insertUser(user);
//
//        Playlist playlist = newPlaylist(agent.getId());
//        insertPlaylist(playlist);
//
//        PlayListDetail playListDetail = newPlayListDetail(playlist.getId());
//        insertPlayListDetail(playListDetail);
//
//        MaterialGroup materialGroup = newMaterialGroup(agent.getId());
//        insertMaterialGroup(materialGroup);
//
//        Material material = newMaterial(1, agent.getId(), materialGroup.getId(), user.getId());
//        insertMaterial(material);
//
//        /********************发布库**************************/
//
//        PublishedMaterial publishedMaterial = newPublishedMaterial(material);
//        insertPublishedMaterial(publishedMaterial);
//
//        PublishedPlaylist publishedPlaylist = newPublishedPlaylist(playlist);
//        insertPublishedPlaylist(publishedPlaylist);
//
//        PublishedPlaylistDetail publishedPlaylistDetail = newPublishedPlaylistDetail(playListDetail.getId(),
//                publishedPlaylist.getId());
//        insertPublishedPlaylistDetail(publishedPlaylistDetail);
//
//        PublishedPlaylistDetailMaterial publishedPlaylistDetailMaterial = newPublishedPlaylistDetailMaterial
//                (publishedPlaylistDetail.getId(), publishedMaterial.getMaterialId());
//        insertPublishedPlaylistDetailMaterial(publishedPlaylistDetailMaterial);
//
//        List<PublishedMaterial> list = service.findByAreaAndDetail(publishedPlaylistDetail.getId());
//        assertTrue(list.size() > 0);
//    }

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

        Playlist playlist = newPlaylist(agent.getId());
        insertPlaylist(playlist);

        PlayListDetail playListDetail = newPlayListDetail(playlist.getId());
        insertPlayListDetail(playListDetail);

        MaterialGroup materialGroup = newMaterialGroup(agent.getId());
        insertMaterialGroup(materialGroup);

        Material material = newMaterial(1, agent.getId(), materialGroup.getId(), user.getId());
        insertMaterial(material);

        /********************发布库**************************/

        PublishedMaterial publishedMaterial = newPublishedMaterial(material);
        insertPublishedMaterial(publishedMaterial);

        assertNotNull(service.find(publishedMaterial.getMaterialId(), publishedMaterial.getVersion()));
    }

}
