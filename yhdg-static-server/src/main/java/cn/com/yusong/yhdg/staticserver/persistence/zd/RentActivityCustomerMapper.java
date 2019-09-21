package cn.com.yusong.yhdg.staticserver.persistence.zd;

import cn.com.yusong.yhdg.common.domain.hdg.ActivityCustomer;
import cn.com.yusong.yhdg.common.domain.zd.RentActivityCustomer;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

public interface RentActivityCustomerMapper extends MasterMapper {
    public int exist(@Param("activityId") long activityId, @Param("customerId") long customerId);
    public int insert(RentActivityCustomer activityCustomer);
}
