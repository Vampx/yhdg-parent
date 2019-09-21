package cn.com.yusong.yhdg.staticserver.persistence.hdg;

import cn.com.yusong.yhdg.common.domain.hdg.ActivityCustomer;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

public interface ActivityCustomerMapper extends MasterMapper {
    public int exist(@Param("activityId") long activityId, @Param("customerId") long customerId);
    public int insert(ActivityCustomer activityCustomer);
}
