package cn.com.yusong.yhdg.frontserver.service.yms;

import cn.com.yusong.yhdg.common.domain.yms.TerminalDownloadProgress;
import cn.com.yusong.yhdg.frontserver.BaseJunit4Test;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import javax.swing.plaf.PanelUI;

public class TerminalDownloadProgressServiceTest extends BaseJunit4Test {

    @Autowired
    TerminalDownloadProgressService service;

    @Test
    public void insert() {
        TerminalDownloadProgress progress = newTerminalDownloadProgress();
//        insertTerminalDownloadProgress(progress);

        assertEquals(1, service.insert(progress));
    }

    @Test
    public void update() {
        TerminalDownloadProgress progress = newTerminalDownloadProgress();
        insertTerminalDownloadProgress(progress);

        progress.setPlaylistProgressInfo("56dreahgraerh");
        assertEquals(1, service.update(progress));
    }

    @Test
    public void find() {
        TerminalDownloadProgress progress = newTerminalDownloadProgress();
        insertTerminalDownloadProgress(progress);

        assertNotNull(service.find(progress.getId()));
    }

}
