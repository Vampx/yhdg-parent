package cn.com.yusong.yhdg.agentserver.persistence.yms;

import cn.com.yusong.yhdg.common.persistence.MasterMapper;

public interface TerminalDownloadProgressMapper extends MasterMapper {
    String findPlaylistProgressInfo(String id);

    int delete(String id);
}
