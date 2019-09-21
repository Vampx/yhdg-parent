package cn.com.yusong.yhdg.agentserver.service.yms;

import cn.com.yusong.yhdg.common.domain.yms.TerminalDownloadProgress;
import cn.com.yusong.yhdg.agentserver.service.BaseJunit4Test;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class TerminalDownloadProgressServiceTest extends BaseJunit4Test {

    @Autowired
    TerminalDownloadProgressService terminalDownloadProgressService;

    @Test
    public void findPlaylistProgressInfo() {
        TerminalDownloadProgress progress = newTerminalDownloadProgress();
        insertTerminalDownloadProgress(progress);

        assertNotNull(terminalDownloadProgressService.findPlaylistProgressInfo(progress.getId()));
    }

}