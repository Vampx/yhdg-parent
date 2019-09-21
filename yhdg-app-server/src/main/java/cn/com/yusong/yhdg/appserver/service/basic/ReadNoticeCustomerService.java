package cn.com.yusong.yhdg.appserver.service.basic;

import cn.com.yusong.yhdg.appserver.entity.result.RestResult;
import cn.com.yusong.yhdg.appserver.persistence.basic.ReadNoticeCustomerMapper;
import cn.com.yusong.yhdg.appserver.service.AbstractService;
import cn.com.yusong.yhdg.common.domain.basic.ReadNoticeCustomer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReadNoticeCustomerService extends AbstractService {

    @Autowired
    ReadNoticeCustomerMapper readNoticeCustomerMapper;

    public RestResult insert(int[] ids, Long customerId) {
        for (int id : ids) {
            ReadNoticeCustomer readNoticeCustomer = readNoticeCustomerMapper.find(customerId, (long) id);
            if (readNoticeCustomer != null) {
                continue;
            }
            readNoticeCustomerMapper.insert(customerId, (long) id);
        }
        return RestResult.SUCCESS;

    }
}
