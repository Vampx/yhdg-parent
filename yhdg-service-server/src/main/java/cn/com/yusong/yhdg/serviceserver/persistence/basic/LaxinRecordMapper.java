package cn.com.yusong.yhdg.serviceserver.persistence.basic;

import cn.com.yusong.yhdg.common.domain.basic.LaxinRecord;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface LaxinRecordMapper extends MasterMapper {

    public List<LaxinRecord> findByStatus(@Param("status") int status, @Param("offset") int offset, @Param("limit") int limit);

    public int transfer(@Param("id") String id, @Param("fromStatus") int fromStatus, @Param("toStatus") int toStatus, @Param("transferTime") Date transferTime);
}
