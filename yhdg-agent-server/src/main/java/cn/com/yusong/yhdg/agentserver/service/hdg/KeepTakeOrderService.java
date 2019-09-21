package cn.com.yusong.yhdg.agentserver.service.hdg;

import cn.com.yusong.yhdg.common.domain.hdg.KeepTakeOrder;
import cn.com.yusong.yhdg.common.entity.pagination.Page;
import cn.com.yusong.yhdg.agentserver.persistence.hdg.KeepTakeOrderMapper;
import cn.com.yusong.yhdg.agentserver.service.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class KeepTakeOrderService extends AbstractService {
    @Autowired
    KeepTakeOrderMapper keepTakeOrderMapper;

    public Page findPage(KeepTakeOrder search) {
        Page page = search.buildPage();
        page.setTotalItems(keepTakeOrderMapper.findPageCount(search));
        search.setBeginIndex(page.getOffset());
        List<KeepTakeOrder> keepTakeOrderList = keepTakeOrderMapper.findPageResult(search);
        for (KeepTakeOrder keepTakeOrder: keepTakeOrderList) {
            keepTakeOrder.setAgentName(findAgentInfo(keepTakeOrder.getAgentId()).getAgentName());
        }
        page.setResult(keepTakeOrderList);
        return page;
    }

    public KeepTakeOrder find(String id) {
        KeepTakeOrder keepTakeOrder = keepTakeOrderMapper.find(id);
        keepTakeOrder.setAgentName(findAgentInfo(keepTakeOrder.getAgentId()).getAgentName());
        return keepTakeOrder;
    }
}
