package cn.com.yusong.yhdg.webserver.service.yms;

import cn.com.yusong.yhdg.common.domain.basic.Agent;import cn.com.yusong.yhdg.common.domain.basic.Partner;
import cn.com.yusong.yhdg.common.domain.yms.Playlist;
import cn.com.yusong.yhdg.common.domain.yms.PublishedPlaylist;
import cn.com.yusong.yhdg.webserver.service.BaseJunit4Test;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class PublishedPlaylistServiceTest extends BaseJunit4Test {
    @Autowired
    PublishedPlaylistService service;
//
//    @Test
//    public void find() {
//        Partner partner = newPartner(); insertPartner(partner); Agent agent = newAgent(partner.getId());
//        insertAgent(agent);
//
//        Playlist playlist = newPlaylist(agent.getId());
//        insertPlaylist(playlist);
//
//        PublishedPlaylist publishedPlaylist = newPublishedPlaylist(playlist);
//        insertPublishedPlaylist(publishedPlaylist);
//
//        assertNotNull(service.find(publishedPlaylist.getId()));
//    }
//
//    @Test
//    public void findByVersion() {
//        Partner partner = newPartner(); insertPartner(partner); Agent agent = newAgent(partner.getId());
//        insertAgent(agent);
//
//        Playlist playlist = newPlaylist(agent.getId());
//        insertPlaylist(playlist);
//
//        PublishedPlaylist publishedPlaylist = newPublishedPlaylist(playlist);
//        insertPublishedPlaylist(publishedPlaylist);
//
//        assertNotNull(service.findByVersion(publishedPlaylist.getPlaylistId(), publishedPlaylist.getVersion()));
//    }
//
//    @Test
//    public void findPage() {
//        Partner partner = newPartner(); insertPartner(partner); Agent agent = newAgent(partner.getId());
//        insertAgent(agent);
//
//        Playlist playlist = newPlaylist(agent.getId());
//        insertPlaylist(playlist);
//
//        PublishedPlaylist publishedPlaylist = newPublishedPlaylist(playlist);
//        insertPublishedPlaylist(publishedPlaylist);
//        assertEquals(1, service.findPage(publishedPlaylist).getTotalItems());
//    }
//
//    @Test
//    public void findByAgent() {
//        Partner partner = newPartner(); insertPartner(partner); Agent agent = newAgent(partner.getId());
//        insertAgent(agent);
//
//        Playlist playlist = newPlaylist(agent.getId());
//        insertPlaylist(playlist);
//
//        PublishedPlaylist publishedPlaylist = newPublishedPlaylist(playlist);
//        insertPublishedPlaylist(publishedPlaylist);
//        List<PublishedPlaylist> list = service.findByAgent(playlist.getAgentId());
//        assertFalse(list.isEmpty());
//    }
}
