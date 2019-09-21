package cn.com.yusong.yhdg.frontserver.persistence.yms;

import cn.com.yusong.yhdg.common.domain.yms.TerminalCrashLog;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;

public interface TerminalCrashLogMapper extends MasterMapper {
    int insert(TerminalCrashLog crashLog);
}
