package cn.com.yusong.yhdg.agentappserver.persistence.yms;

import cn.com.yusong.yhdg.common.domain.yms.Terminal;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TerminalMapper extends MasterMapper {
    public Terminal find(@Param("id") String id);
    public List<Terminal> findList(@Param("isOnline") Integer isOnline,
                                   @Param("offset") int offset,
                                   @Param("limit") int limit);
    public int findTerminalCount(@Param("isOnline") Integer isOnline);
}
