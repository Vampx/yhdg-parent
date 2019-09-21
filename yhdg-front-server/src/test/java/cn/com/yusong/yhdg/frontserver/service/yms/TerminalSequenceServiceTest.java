package cn.com.yusong.yhdg.frontserver.service.yms;

import cn.com.yusong.yhdg.frontserver.BaseJunit4Test;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.*;

public class TerminalSequenceServiceTest extends BaseJunit4Test {
    @Autowired
    TerminalSequenceService terminalSequenceService;

    @Test
    public void nextCode() {
        String nextCode = terminalSequenceService.nextCode();
        assertNotNull(nextCode);
    }
}