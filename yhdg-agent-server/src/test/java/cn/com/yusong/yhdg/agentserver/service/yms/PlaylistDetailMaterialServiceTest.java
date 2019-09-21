package cn.com.yusong.yhdg.agentserver.service.yms;

import cn.com.yusong.yhdg.common.domain.basic.*;
import cn.com.yusong.yhdg.common.domain.yms.*;
import cn.com.yusong.yhdg.agentserver.service.BaseJunit4Test;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class PlaylistDetailMaterialServiceTest extends BaseJunit4Test {
    @Autowired
    PlaylistDetailMaterialService service;

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

        Playlist playlist = newPlaylist(agent.getId());
        insertPlaylist(playlist);

        PlayListDetail playListDetail = newPlayListDetail(playlist.getId());
        insertPlayListDetail(playListDetail);

        PlaylistDetailMaterial playlistDetailMaterial = newPlaylistDetailMaterial(playListDetail.getId(), material.getId());
        insertPlaylistDetailMaterial(playlistDetailMaterial);

        assertNotNull(service.find(playlistDetailMaterial));
    }

    @Test
    public void insert() {
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

        PlaylistDetailMaterial playlistDetailMaterial = newPlaylistDetailMaterial(playListDetail.getId(), material.getId());

        assertEquals(1, service.insert(playlistDetailMaterial));
    }

    @Test
    public void findByAreaAndDetail() {
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

        PlaylistDetailMaterial playlistDetailMaterial = newPlaylistDetailMaterial(playListDetail.getId(), material.getId());
        insertPlaylistDetailMaterial(playlistDetailMaterial);

        assertTrue(service.findByAreaAndDetail(playListDetail.getId()).size() > 0);
    }

    @Test
    public void findDetailMaterials() {
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

        PlaylistDetailMaterial playlistDetailMaterial = newPlaylistDetailMaterial(playListDetail.getId(), material.getId());
        insertPlaylistDetailMaterial(playlistDetailMaterial);

        assertTrue(service.findDetailMaterials(playListDetail.getId()).size() > 0);
    }
}
