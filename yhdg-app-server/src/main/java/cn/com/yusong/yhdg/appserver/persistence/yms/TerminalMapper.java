package cn.com.yusong.yhdg.appserver.persistence.yms;

import cn.com.yusong.yhdg.common.domain.yms.Terminal;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

public interface TerminalMapper extends MasterMapper {
    public Terminal find(@Param("id") String id);

    public int insert(Terminal entity);

    public int updateOnline(Terminal entity);

    public int updateHeartPlaylistId(Terminal entity);

    public int updateHeartTime(Terminal entity);

}
