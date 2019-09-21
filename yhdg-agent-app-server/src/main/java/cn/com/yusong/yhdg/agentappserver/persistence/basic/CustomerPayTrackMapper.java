package cn.com.yusong.yhdg.agentappserver.persistence.basic;

import cn.com.yusong.yhdg.common.domain.basic.CustomerPayTrack;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CustomerPayTrackMapper extends MasterMapper {
    List<CustomerPayTrack> findList(@Param("agentId") Integer agentId, @Param("customerId") Long customerId, @Param("offset") int offset, @Param("limit") int limit);
}
