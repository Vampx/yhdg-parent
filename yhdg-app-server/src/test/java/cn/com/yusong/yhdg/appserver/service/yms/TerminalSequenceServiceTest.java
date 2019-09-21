package cn.com.yusong.yhdg.appserver.service.yms;

import cn.com.yusong.yhdg.appserver.BaseJunit4Test;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class TerminalSequenceServiceTest extends BaseJunit4Test {

    @Autowired
    TerminalSequenceService terminalSequenceService;

    @Test
    public void nextCode() {
        terminalSequenceService.nextCode();
    }
}
