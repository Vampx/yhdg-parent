package cn.com.yusong.yhdg.webserver.service.yms;

import cn.com.yusong.yhdg.common.domain.basic.Agent;import cn.com.yusong.yhdg.common.domain.basic.Partner;
import cn.com.yusong.yhdg.common.domain.yms.PlayListDetail;
import cn.com.yusong.yhdg.common.domain.yms.Playlist;
import cn.com.yusong.yhdg.common.domain.yms.PublishedPlaylist;
import cn.com.yusong.yhdg.common.domain.yms.PublishedPlaylistDetail;
import cn.com.yusong.yhdg.webserver.service.BaseJunit4Test;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class PublishedPlaylistDetailServiceTest extends BaseJunit4Test {
    @Autowired
    PublishedPlaylistDetailService service;

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
//        /*******************************************/
//
//        PublishedPlaylist publishedPlaylist = newPublishedPlaylist(playlist);
//        insertPublishedPlaylist(publishedPlaylist);
//
//        PublishedPlaylistDetail publishedPlaylistDetail = newPublishedPlaylistDetail(playListDetail.getId(), publishedPlaylist.getId());
//        insertPublishedPlaylistDetail(publishedPlaylistDetail);
//
//        assertTrue(service.findByPlaylist(publishedPlaylistDetail.getPlaylistId()).size() > 0);
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
//        /*******************************************/
//
//        PublishedPlaylist publishedPlaylist = newPublishedPlaylist(playlist);
//        insertPublishedPlaylist(publishedPlaylist);
//
//        PublishedPlaylistDetail publishedPlaylistDetail = newPublishedPlaylistDetail(playListDetail.getId(), publishedPlaylist.getId());
//        insertPublishedPlaylistDetail(publishedPlaylistDetail);
//
//        assertNotNull(service.find(publishedPlaylistDetail.getId()));
//    }

}
