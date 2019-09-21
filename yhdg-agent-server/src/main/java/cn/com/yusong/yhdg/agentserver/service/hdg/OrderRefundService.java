package cn.com.yusong.yhdg.agentserver.service.hdg;

import cn.com.yusong.yhdg.common.domain.hdg.OrderRefund;
import cn.com.yusong.yhdg.common.entity.pagination.Page;
import cn.com.yusong.yhdg.agentserver.persistence.hdg.OrderRefundMapper;
import cn.com.yusong.yhdg.agentserver.service.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderRefundService extends AbstractService {

    @Autowired
    OrderRefundMapper orderRefundMapper;


    public Page findPageForBalance(OrderRefund search) {
        Page page = search.buildPage();
        page.setTotalItems(orderRefundMapper.findPageForBalanceCount(search));
        search.setBeginIndex(page.getOffset());

        List<OrderRefund> list = orderRefundMapper.findPageForBalanceResult(search);
        for (OrderRefund orderRefund: list) {
            if (orderRefund.getAgentId() != null) {
                orderRefund.setAgentName(findAgentInfo(orderRefund.getAgentId()).getAgentName());
            }
        }
        page.setResult(list);
        return page;
    }



}
