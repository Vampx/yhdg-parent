package cn.com.yusong.yhdg.agentserver.persistence.hdg;

import cn.com.yusong.yhdg.common.domain.hdg.ActivityCustomer;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;

import java.util.List;

public interface ActivityCustomerMapper extends MasterMapper {
    int findPageCount(ActivityCustomer activityCustomer);
    List<ActivityCustomer> findPageResult(ActivityCustomer activityCustomer);
    int deleteByActivityId(long activityId);
}
