package cn.com.yusong.yhdg.agentserver.persistence.hdg;


import cn.com.yusong.yhdg.common.domain.hdg.FaultFeedback;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface FaultFeedbackMapper extends MasterMapper {
    FaultFeedback find(long id);

    int findCountByCabinet(@Param("cabinetId") String cabinetId);

    int findPageCount(FaultFeedback search);

    List<FaultFeedback> findPageResult(FaultFeedback search);

    int updateBasicInfo(FaultFeedback entity);

    int delete(long id);

    int deleteByCustomerId(@Param("customerId") long customerId);

    int findFaultFeedbackCount(@Param("faultType") int faultType, @Param("handleStatus") int handleStatus);
}
