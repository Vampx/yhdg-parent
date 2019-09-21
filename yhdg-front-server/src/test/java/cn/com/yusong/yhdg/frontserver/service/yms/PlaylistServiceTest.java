package cn.com.yusong.yhdg.frontserver.service.yms;

import cn.com.yusong.yhdg.common.domain.basic.Agent;
import cn.com.yusong.yhdg.common.domain.basic.Partner;
import cn.com.yusong.yhdg.common.domain.yms.Playlist;
import cn.com.yusong.yhdg.frontserver.BaseJunit4Test;
import cn.com.yusong.yhdg.frontserver.service.yms.PlaylistService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class PlaylistServiceTest extends BaseJunit4Test {

    @Autowired
    PlaylistService service;

    @Test
    public void find() {
        Partner partner = newPartner(); insertPartner(partner); Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        Playlist playlist = newPlaylist(agent.getId());
        insertPlaylist(playlist);

        assertNotNull(service.find(playlist.getId()));
    }
}
