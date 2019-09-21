package cn.com.yusong.yhdg.appserver.persistence.hdg;

import cn.com.yusong.yhdg.common.domain.hdg.FaultFeedback;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface FaultFeedBackMapper extends MasterMapper {
    public FaultFeedback find(Long id);

    public List<FaultFeedback> findListByCustomer(@Param("customerId") long customerId, @Param("offset") int offset, @Param("limit") int limit);

    public int insert(FaultFeedback faultFeedback);

}
