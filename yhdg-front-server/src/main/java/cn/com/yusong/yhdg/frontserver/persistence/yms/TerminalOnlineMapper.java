package cn.com.yusong.yhdg.frontserver.persistence.yms;

import cn.com.yusong.yhdg.common.domain.yms.TerminalOnline;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;


public interface TerminalOnlineMapper extends MasterMapper {
    int insert(TerminalOnline terminalOnline);
    int updateHeartInfo(TerminalOnline online);
    int updateLoginInfo(@Param("id") String id, @Param("isOnline") Integer isOnline, @Param("heartTime") Date heartTime);
    int updatePlayFile(@Param("id") String id, @Param("playFile") String playFile);
    int updateDownloadPlaylistProgress(@Param("id") String id, @Param("speed") float speed, @Param("downloadProgress") float downloadProgress);
    int offline(@Param("id") String id, @Param("isOnline") int isOnline);
}
