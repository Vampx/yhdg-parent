package cn.com.yusong.yhdg.agentappserver.persistence.basic;

import cn.com.yusong.yhdg.common.domain.basic.IdCardAuthRecord;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface IdCardAuthRecordMapper extends MasterMapper {
    public List<IdCardAuthRecord> queryByDate(@Param("agentId") Integer agentId,
                                              @Param("keyword") String keyword,
                                              @Param("date") String date,
                                              @Param("offset") Integer offset,
                                              @Param("limit") Integer limit
                                              );
}
