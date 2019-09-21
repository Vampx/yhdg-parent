package cn.com.yusong.yhdg.agentappserver.persistence.basic;

import cn.com.yusong.yhdg.common.domain.basic.LaxinRecord;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface LaxinRecordMapper extends MasterMapper {
    public LaxinRecord find(@Param("id") String id);
    public int findExistByLaxinId(@Param("laxinId") long laxinId);
    public List<LaxinRecord> findByStatus(@Param("agentId")int agentId, @Param("status")int status, @Param("keyword")String keyword, @Param("offset")int offset, @Param("limit")int limit);
    public List<LaxinRecord> findByOrderId(@Param("orderId") String orderId);

    public int totalMoneyByPayTime(@Param("agentId") long agentId, @Param("beginTime") Date beginTime, @Param("endTime")Date endTime);

    public int totalCountByCreateTime(@Param("agentId") long agentId, @Param("beginTime") Date beginTime, @Param("endTime")Date endTime);

    public int updateStatus(@Param("id") String id, @Param("orderId") String orderId, @Param("fromStatus") int fromStatus, @Param("toStatus") int toStatus, @Param("payType") int payType, @Param("payTime") Date payTime);

    public int updateCancel(@Param("id")String id,@Param("cancelCanuse") String cancelCanuse,@Param("status") int status);
}
