package cn.com.yusong.yhdg.frontserver.persistence.yms;


import cn.com.yusong.yhdg.common.domain.yms.Terminal;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TerminalMapper extends MasterMapper {

    Terminal find(String id);
    int insert(Terminal terminal);
    int updateLoginInfo(@Param("id") String id, @Param("version") String version);
    List<Terminal> findByPlaylist(int playlistId);
    int offline(@Param("id") String id, @Param("isOnline") int isOnline);
}
