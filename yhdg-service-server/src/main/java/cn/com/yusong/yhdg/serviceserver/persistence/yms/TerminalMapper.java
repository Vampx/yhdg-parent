package cn.com.yusong.yhdg.serviceserver.persistence.yms;

import cn.com.yusong.yhdg.common.domain.hdg.AgentDayStats;
import cn.com.yusong.yhdg.common.domain.hdg.Cabinet;
import cn.com.yusong.yhdg.common.domain.yms.Terminal;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface TerminalMapper extends MasterMapper {

    public List<Terminal> findByHeartTime(@Param("heartTime") Date heartTime, @Param("limit") int limit);

    public int updateOnline(@Param("id") String id, @Param("isOnline") Integer isOnline);


}
