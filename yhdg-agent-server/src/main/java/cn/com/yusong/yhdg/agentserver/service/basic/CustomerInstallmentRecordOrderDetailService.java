package cn.com.yusong.yhdg.agentserver.service.basic;

import cn.com.yusong.yhdg.agentserver.persistence.basic.CustomerInstallmentRecordOrderDetailMapper;
import cn.com.yusong.yhdg.agentserver.service.AbstractService;
import cn.com.yusong.yhdg.common.domain.basic.CustomerInstallmentRecordOrderDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerInstallmentRecordOrderDetailService extends AbstractService {

    @Autowired
    CustomerInstallmentRecordOrderDetailMapper customerInstallmentRecordOrderDetailMapper;

    public CustomerInstallmentRecordOrderDetail findOrderBySourceId(String sourceId, Integer category) {
        return customerInstallmentRecordOrderDetailMapper.findOrderBySourceId(sourceId, category);
    }

}
