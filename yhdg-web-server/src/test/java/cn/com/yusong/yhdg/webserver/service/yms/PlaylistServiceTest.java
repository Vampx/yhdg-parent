package cn.com.yusong.yhdg.webserver.service.yms;

import cn.com.yusong.yhdg.common.domain.basic.Agent;import cn.com.yusong.yhdg.common.domain.basic.Partner;
import cn.com.yusong.yhdg.common.domain.basic.Dept;
import cn.com.yusong.yhdg.common.domain.basic.Role;
import cn.com.yusong.yhdg.common.domain.basic.User;
import cn.com.yusong.yhdg.common.domain.yms.Material;
import cn.com.yusong.yhdg.common.domain.yms.MaterialGroup;
import cn.com.yusong.yhdg.common.domain.yms.PlayListDetail;
import cn.com.yusong.yhdg.common.domain.yms.Playlist;
import cn.com.yusong.yhdg.webserver.service.BaseJunit4Test;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class PlaylistServiceTest extends BaseJunit4Test {

    @Autowired
    PlaylistService playlistService;

    @Test
    public void find() {
        Partner partner = newPartner(); insertPartner(partner); Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        Playlist playlist = newPlaylist(agent.getId());
        insertPlaylist(playlist);
        assertNotNull(playlistService.find(playlist.getId()));
    }

    @Test
    public void findAll() {
        Partner partner = newPartner(); insertPartner(partner); Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        Playlist playlist = newPlaylist(agent.getId());
        insertPlaylist(playlist);

        List<Integer> list = playlistService.findAll();
        assertFalse(list.isEmpty());
    }

    @Test
    public void findPage() {
        Partner partner = newPartner(); insertPartner(partner); Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        Playlist playlist = newPlaylist(agent.getId());
        insertPlaylist(playlist);
        assertEquals(1, playlistService.findPage(playlist).getTotalItems());
    }

    @Test
    public void findByAgent() {
        Partner partner = newPartner(); insertPartner(partner); Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        Playlist playlist = newPlaylist(agent.getId());
        insertPlaylist(playlist);

        List<Playlist> list = playlistService.findByAgent(agent.getId());
        assertFalse(list.isEmpty());
    }

    @Test
    public void insert() {
        Partner partner = newPartner(); insertPartner(partner); Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        Playlist playlist = newPlaylist(agent.getId());
        assertTrue("updata fail", playlistService.insert(playlist, "admin").isSuccess());
    }

    @Test
    public void delete() {
        Partner partner = newPartner(); insertPartner(partner); Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        Playlist playlist = newPlaylist(agent.getId());
        insertPlaylist(playlist);
        assertTrue("updata fail", playlistService.delete(playlist.getId()).isSuccess());
    }

//    @Test
//    public void updateStatus() {
//        Partner partner = newPartner(); insertPartner(partner); Agent agent = newAgent(partner.getId());
//        insertAgent(agent);
//
//        Playlist playlist = newPlaylist(agent.getId());
//        insertPlaylist(playlist);
//
//        assertTrue("updateStatus fail", playlistService.updateStatus(1, playlist.getId()).isSuccess());
//    }
//
//    @Test
//    public void auditPass() {
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
//        assertTrue("auditPass fail", playlistService.auditPass(playlist).isSuccess());
//    }
//
//    @Test
//    public void auditReject() {
//        Partner partner = newPartner(); insertPartner(partner); Agent agent = newAgent(partner.getId());
//        insertAgent(agent);
//
//        Playlist playlist = newPlaylist(agent.getId());
//        insertPlaylist(playlist);
//
//        Playlist entity = new Playlist();
//        entity.setId(playlist.getId());
//        entity.setVersion(playlist.getVersion());
//        assertTrue("auditPass fail", playlistService.auditReject(playlist).isSuccess());
//    }

}
