package cn.com.yusong.yhdg.webserver.persistence.zd;

import cn.com.yusong.yhdg.common.domain.zd.RentActivityCustomer;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;

import java.util.List;

public interface RentActivityCustomerMapper extends MasterMapper {
    int findPageCount(RentActivityCustomer search);

    List<RentActivityCustomer> findPageResult(RentActivityCustomer search);

    int deleteByActivity(long activityId);
}
