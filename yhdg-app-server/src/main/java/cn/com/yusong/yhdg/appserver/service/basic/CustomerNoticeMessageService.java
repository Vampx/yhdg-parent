package cn.com.yusong.yhdg.appserver.service.basic;

import cn.com.yusong.yhdg.appserver.entity.result.RestResult;
import cn.com.yusong.yhdg.appserver.persistence.basic.CustomerMapper;
import cn.com.yusong.yhdg.appserver.persistence.basic.CustomerNoticeMessageMapper;
import cn.com.yusong.yhdg.appserver.persistence.basic.PublicNoticeMapper;
import cn.com.yusong.yhdg.appserver.service.AbstractService;
import cn.com.yusong.yhdg.common.domain.basic.Customer;
import cn.com.yusong.yhdg.common.domain.basic.CustomerNoticeMessage;
import cn.com.yusong.yhdg.common.domain.basic.PublicNotice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class CustomerNoticeMessageService extends AbstractService {
    @Autowired
    CustomerNoticeMessageMapper customerNoticeMessageMapper;
    @Autowired
    PublicNoticeMapper publicNoticeMapper;
    @Autowired
    CustomerMapper customerMapper;

    public CustomerNoticeMessage find(Integer type,Long id) {
        if (CustomerNoticeMessage.Type.NOTICE.getValue() == type) {
            return publicNoticeMapper.find(PublicNotice.NoticeType.CUSTOMER_NOTICE.getValue(), id);
        }
        return customerNoticeMessageMapper.find(id, type);
    }

    public List<CustomerNoticeMessage> findListByCustomerId(Long customerId, Integer type, Integer offset, Integer limit) {
        //只能查看空运营商或本运营商的公告
        Customer customer = customerMapper.find(customerId);
        Integer agentId = customer.getAgentId();


        if (CustomerNoticeMessage.Type.NOTICE.getValue() == type) {
            return publicNoticeMapper.findList(agentId, PublicNotice.NoticeType.CUSTOMER_NOTICE.getValue(), offset, limit);
        }
        return customerNoticeMessageMapper.findListByCustomerId(customerId, type, offset, limit);
    }

    public List<CustomerNoticeMessage> findUnreadListByCustomerId(Long customerId, Integer type, Integer offset, Integer limit) {
        return customerNoticeMessageMapper.findUnreadListByCustomerId(customerId, type, offset, limit);
    }

    public RestResult read(int[] ids) {
        Date now = new Date();
        for (int id : ids) {
            customerNoticeMessageMapper.updateReceiveTime((long) id, now);
        }
        return RestResult.SUCCESS;

    }
}
