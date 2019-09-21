package cn.com.yusong.yhdg.agentserver.service.basic;

import cn.com.yusong.yhdg.agentserver.persistence.basic.CancelLaxinCustomerMapper;
import cn.com.yusong.yhdg.agentserver.persistence.basic.CustomerCouponTicketMapper;
import cn.com.yusong.yhdg.agentserver.persistence.basic.LaxinCustomerMapper;
import cn.com.yusong.yhdg.agentserver.persistence.basic.PartnerMapper;
import cn.com.yusong.yhdg.common.domain.basic.CancelLaxinCustomer;
import cn.com.yusong.yhdg.common.domain.basic.CustomerCouponTicket;
import cn.com.yusong.yhdg.common.domain.basic.LaxinCustomer;
import cn.com.yusong.yhdg.common.domain.basic.Partner;
import cn.com.yusong.yhdg.common.entity.json.ExtResult;
import cn.com.yusong.yhdg.common.entity.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class LaxinCustomerService {

    @Autowired
    LaxinCustomerMapper laxinCustomerMapper;
    @Autowired
    PartnerMapper partnerMapper;
    @Autowired
    CancelLaxinCustomerMapper cancelLaxinCustomerMapper;
    @Autowired
    CustomerCouponTicketMapper customerCouponTicketMapper;

    public LaxinCustomer find(String id) {
        return laxinCustomerMapper.find(id);
    }

    public Page findPage(LaxinCustomer search) {
        Page page = search.buildPage();
        page.setTotalItems(laxinCustomerMapper.findPageCount(search));
        search.setBeginIndex(page.getOffset());
        List<LaxinCustomer> laxinCustomerList = laxinCustomerMapper.findPageResult(search);
        for (LaxinCustomer laxinCustomer : laxinCustomerList) {
            Partner partner = partnerMapper.find(laxinCustomer.getPartnerId());
            if (partner != null) {
                laxinCustomer.setPartnerName(partner.getPartnerName());
            }
        }
        page.setResult(laxinCustomerList);
        return page;
    }

    @Transactional(rollbackFor = Throwable.class)
    public ExtResult cancel(String id, String cancelCanuse) {
        LaxinCustomer laxinCustomer = laxinCustomerMapper.find(id);
        if (laxinCustomer != null) {
            CancelLaxinCustomer cancelLaxinCustomer = new CancelLaxinCustomer();
            cancelLaxinCustomer.setAgentId(laxinCustomer.getAgentId());
            cancelLaxinCustomer.setLaxinMobile(laxinCustomer.getLaxinMobile());
            cancelLaxinCustomer.setTargetCustomerId(laxinCustomer.getTargetCustomerId());
            cancelLaxinCustomer.setTargetMobile(laxinCustomer.getTargetMobile());
            cancelLaxinCustomer.setTargetFullname(laxinCustomer.getTargetFullname());
            cancelLaxinCustomer.setCancelCanuse(cancelCanuse);
            cancelLaxinCustomer.setCreateTime(new Date());
            customerCouponTicketMapper.updateStatusByMobile(laxinCustomer.getTargetMobile(), CustomerCouponTicket.Status.EXPIRED.getValue());
            laxinCustomerMapper.delete(id);
            cancelLaxinCustomerMapper.insert(cancelLaxinCustomer);
            return ExtResult.successResult();
        }else {
            return ExtResult.failResult("取消失败！");
        }
    }

}
