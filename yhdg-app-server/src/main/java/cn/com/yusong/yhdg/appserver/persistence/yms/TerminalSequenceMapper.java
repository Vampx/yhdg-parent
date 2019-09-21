package cn.com.yusong.yhdg.appserver.persistence.yms;


import cn.com.yusong.yhdg.common.domain.yms.TerminalSequence;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;

public interface TerminalSequenceMapper extends MasterMapper{
    public int insert(TerminalSequence entity);
}
