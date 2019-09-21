package cn.com.yusong.yhdg.appserver.service.basic;

import cn.com.yusong.yhdg.appserver.constant.RespCode;
import cn.com.yusong.yhdg.appserver.entity.result.RestResult;
import cn.com.yusong.yhdg.appserver.persistence.basic.CustomerMapper;
import cn.com.yusong.yhdg.appserver.persistence.basic.PublicNoticeMapper;
import cn.com.yusong.yhdg.appserver.persistence.basic.ReadNoticeCustomerMapper;
import cn.com.yusong.yhdg.appserver.service.AbstractService;
import cn.com.yusong.yhdg.common.constant.Constant;
import cn.com.yusong.yhdg.common.domain.basic.Customer;
import cn.com.yusong.yhdg.common.domain.basic.CustomerNoticeMessage;
import cn.com.yusong.yhdg.common.domain.basic.PublicNotice;
import org.apache.tools.ant.util.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PublicNoticeService extends AbstractService {

    @Autowired
    PublicNoticeMapper publicNoticeMapper;
    @Autowired
    ReadNoticeCustomerMapper readNoticeCustomerMapper;
    @Autowired
    CustomerMapper customerMapper;

    public RestResult findList(long customerId, int offset, int limit) {
        Long[] ids = readNoticeCustomerMapper.findByCustomer(customerId);
        //只能查看空运营商或本运营商的公告
        Customer customer = customerMapper.find(customerId);
        Integer agentId = customer.getAgentId();

        List<PublicNotice> publicNotices = publicNoticeMapper.findListByUnread(ids, agentId, PublicNotice.NoticeType.CUSTOMER_NOTICE.getValue(), offset, limit);
        List<Map> mapList = new ArrayList<Map>();
        for (PublicNotice publicNotice : publicNotices) {
            Map map = new HashMap();
            map.put("id", publicNotice.getId());
            map.put("title", publicNotice.getTitle());
            map.put("content", publicNotice.getContent());
            map.put("type", CustomerNoticeMessage.Type.NOTICE.getValue());
            map.put("createTime", DateUtils.format(publicNotice.getCreateTime(), Constant.DATE_TIME_FORMAT));
            mapList.add(map);
        }
        return RestResult.dataResult(RespCode.CODE_0.getValue(), null, mapList);

    }
}
