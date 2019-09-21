package cn.com.yusong.yhdg.staticserver.persistence.basic;

import cn.com.yusong.yhdg.common.domain.basic.CustomerMultiOrderDetail;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;

import java.util.List;

public interface CustomerMultiOrderDetailMapper extends MasterMapper {
    int insert(CustomerMultiOrderDetail customerMultiOrderDetail);

    List<CustomerMultiOrderDetail> findListByOrderId(Long orderId);
}
