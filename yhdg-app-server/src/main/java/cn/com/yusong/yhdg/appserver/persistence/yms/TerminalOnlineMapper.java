package cn.com.yusong.yhdg.appserver.persistence.yms;


import cn.com.yusong.yhdg.common.domain.yms.TerminalOnline;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;

public interface TerminalOnlineMapper extends MasterMapper{
    public int insert(TerminalOnline terminalOnline);
}
