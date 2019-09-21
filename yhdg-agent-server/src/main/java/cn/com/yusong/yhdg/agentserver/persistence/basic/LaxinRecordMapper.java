package cn.com.yusong.yhdg.agentserver.persistence.basic;

import cn.com.yusong.yhdg.common.domain.basic.LaxinRecord;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface LaxinRecordMapper extends MasterMapper {
    public LaxinRecord find(@Param("id") String id);
    public int findExistByLaxinId(@Param("laxinId") long laxinId);
    int findPageCount(LaxinRecord search);
    List<LaxinRecord> findPageResult(LaxinRecord search);
    public int resetAccount(@Param("id") String id, @Param("fromStatus") int fromStatus, @Param("toStatus") int toStatus, @Param("mpOpenId") String mpOpenId, @Param("accountName") String accountName);
    int updateCancel(LaxinRecord search);
}
