package cn.com.yusong.yhdg.webserver.service.hdg;

import cn.com.yusong.yhdg.common.domain.basic.Customer;
import cn.com.yusong.yhdg.common.domain.basic.CustomerInOutMoney;
import cn.com.yusong.yhdg.common.domain.hdg.OrderRefund;
import cn.com.yusong.yhdg.common.domain.hdg.PacketPeriodOrderRefund;
import cn.com.yusong.yhdg.common.entity.json.ExtResult;
import cn.com.yusong.yhdg.common.entity.pagination.Page;
import cn.com.yusong.yhdg.webserver.persistence.basic.CustomerInOutMoneyMapper;
import cn.com.yusong.yhdg.webserver.persistence.basic.CustomerMapper;
import cn.com.yusong.yhdg.webserver.persistence.hdg.OrderRefundMapper;
import cn.com.yusong.yhdg.webserver.persistence.hdg.PacketPeriodOrderRefundMapper;
import cn.com.yusong.yhdg.webserver.service.AbstractService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
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
