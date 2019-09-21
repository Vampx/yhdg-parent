package cn.com.yusong.yhdg.agentserver.service.hdg;

import cn.com.yusong.yhdg.common.domain.hdg.KeepPutOrder;
import cn.com.yusong.yhdg.common.entity.pagination.Page;
import cn.com.yusong.yhdg.agentserver.persistence.hdg.KeepPutOrderMapper;
import cn.com.yusong.yhdg.agentserver.service.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class KeepPutOrderService extends AbstractService{
    @Autowired
    KeepPutOrderMapper keepPutOrderMapper;

    public int findOrderCount(Integer agentId,Date queryBeginTime, Date queryEndTime, String  cabinetId) {
        return keepPutOrderMapper.findOrderCount(agentId,queryBeginTime, queryEndTime, cabinetId);
    }

    public Page findPage(KeepPutOrder search) {
        Page page = search.buildPage();
        page.setTotalItems(keepPutOrderMapper.findPageCount(search));
        search.setBeginIndex(page.getOffset());
        List<KeepPutOrder> keepPutOrderList = keepPutOrderMapper.findPageResult(search);
        for (KeepPutOrder keepPutOrder: keepPutOrderList) {
            keepPutOrder.setAgentName(findAgentInfo(keepPutOrder.getAgentId()).getAgentName());
        }
        page.setResult(keepPutOrderList);
        return page;
    }

    public KeepPutOrder find(String id) {
        KeepPutOrder keepPutOrder = keepPutOrderMapper.find(id);
        keepPutOrder.setAgentName(findAgentInfo(keepPutOrder.getAgentId()).getAgentName());
        return keepPutOrder;
    }
}
