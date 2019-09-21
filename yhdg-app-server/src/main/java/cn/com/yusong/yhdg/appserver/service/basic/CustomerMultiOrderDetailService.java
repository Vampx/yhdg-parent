package cn.com.yusong.yhdg.appserver.service.basic;

import cn.com.yusong.yhdg.appserver.persistence.basic.CustomerMultiOrderDetailMapper;
import cn.com.yusong.yhdg.appserver.service.AbstractService;
import cn.com.yusong.yhdg.common.domain.basic.CustomerMultiOrderDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerMultiOrderDetailService extends AbstractService {
    @Autowired
    CustomerMultiOrderDetailMapper customerMultiOrderDetailMapper;

    public List<CustomerMultiOrderDetail> findListByOrderId(Long orderId) {
        return customerMultiOrderDetailMapper.findListByOrderId(orderId);
    }
}
