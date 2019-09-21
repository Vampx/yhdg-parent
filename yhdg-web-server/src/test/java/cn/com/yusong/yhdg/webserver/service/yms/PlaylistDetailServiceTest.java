package cn.com.yusong.yhdg.webserver.service.yms;

import cn.com.yusong.yhdg.common.domain.basic.Agent;import cn.com.yusong.yhdg.common.domain.basic.Partner;
import cn.com.yusong.yhdg.common.domain.basic.Dept;
import cn.com.yusong.yhdg.common.domain.basic.Role;
import cn.com.yusong.yhdg.common.domain.basic.User;
import cn.com.yusong.yhdg.common.domain.yms.MaterialGroup;
import cn.com.yusong.yhdg.common.domain.yms.PlayListDetail;
import cn.com.yusong.yhdg.common.domain.yms.Playlist;
import cn.com.yusong.yhdg.common.domain.yms.PlaylistDetailMaterial;
import cn.com.yusong.yhdg.webserver.service.BaseJunit4Test;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

public class PlaylistDetailServiceTest extends BaseJunit4Test {

    @Autowired
    PlaylistDetailService playlistDetailService;

    @Test
    public void findPage() {
        Partner partner = newPartner(); insertPartner(partner); Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        Playlist playlist = newPlaylist(agent.getId());
        insertPlaylist(playlist);

        PlayListDetail playListDetail = newPlayListDetail(playlist.getId());
        insertPlayListDetail(playListDetail);

        assertEquals(1, playlistDetailService.findPage(playListDetail).getTotalItems());
    }

//    @Test
//    public void findByPlaylist() {
//        Partner partner = newPartner(); insertPartner(partner); Agent agent = newAgent(partner.getId());
//        insertAgent(agent);
//
//        Playlist playlist = newPlaylist(agent.getId());
//        insertPlaylist(playlist);
//
//        PlayListDetail playListDetail = newPlayListDetail(playlist.getId());
//        insertPlayListDetail(playListDetail);
//
//        List<PlayListDetail> list = playlistDetailService.findByPlaylist(playlist.getId());
//        assertFalse(list.isEmpty());
//    }
//
//    @Test
//    public void find() {
//        Partner partner = newPartner(); insertPartner(partner); Agent agent = newAgent(partner.getId());
//        insertAgent(agent);
//
//        Playlist playlist = newPlaylist(agent.getId());
//        insertPlaylist(playlist);
//
//        PlayListDetail playListDetail = newPlayListDetail(playlist.getId());
//        insertPlayListDetail(playListDetail);
//
//        assertNotNull(playlistDetailService.find(playListDetail.getId()));
//    }
//
//    @Test
//    public void updata() {
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
//        PlayListDetail entity = new PlayListDetail();
//        entity.setPlaylistId(playlist.getId());
//        entity.setDetailName("播放明细一");
//        entity.setId(playListDetail.getId());
//        entity.setMaterials(new ArrayList<PlaylistDetailMaterial>());
//        assertTrue("updata fail", playlistDetailService.updata(entity).isSuccess());
//    }
//
//    @Test
//    public void delete() {
//        Partner partner = newPartner(); insertPartner(partner); Agent agent = newAgent(partner.getId());
//        insertAgent(agent);
//
//        Playlist playlist = newPlaylist(agent.getId());
//        insertPlaylist(playlist);
//
//        PlayListDetail playListDetail = newPlayListDetail(playlist.getId());
//        insertPlayListDetail(playListDetail);
//        assertTrue("delete fail", playlistDetailService.delete(playListDetail.getId()).isSuccess());
//    }

    @Test
    public void insert() {
        Partner partner = newPartner(); insertPartner(partner); Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        Playlist playlist = newPlaylist(agent.getId());
        insertPlaylist(playlist);

        PlayListDetail playListDetail = newPlayListDetail(playlist.getId());
        assertEquals(1, playlistDetailService.insert(playListDetail));
    }


}
