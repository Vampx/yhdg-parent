package cn.com.yusong.yhdg.frontserver.service.yms;

import cn.com.yusong.yhdg.common.domain.yms.TerminalCode;
import cn.com.yusong.yhdg.frontserver.BaseJunit4Test;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.*;

public class TerminalCodeServiceTest extends BaseJunit4Test {
    @Autowired
    TerminalCodeService terminalCodeService;

    @Test
    public void find() {
        TerminalCode terminalCode = newTerminalCode();
        insertTerminalCode(terminalCode);

        assertNotNull(terminalCodeService.find(terminalCode.getId()));
    }

    @Test
    public void findByCode() {
        TerminalCode terminalCode = newTerminalCode();
        insertTerminalCode(terminalCode);

        assertNotNull(terminalCodeService.findByCode(terminalCode.getCode()));
    }

    @Test
    public void insert() {
        TerminalCode terminalCode = newTerminalCode();

        assertEquals(1, terminalCodeService.insert(terminalCode));
    }
}