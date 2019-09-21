package cn.com.yusong.yhdg.webserver.service.basic;

import cn.com.yusong.yhdg.common.domain.basic.AgentBatteryType;
import cn.com.yusong.yhdg.common.domain.basic.DeductionTicketOrder;
import cn.com.yusong.yhdg.common.domain.basic.Partner;
import cn.com.yusong.yhdg.common.domain.hdg.InsuranceOrder;
import cn.com.yusong.yhdg.common.entity.pagination.Page;
import cn.com.yusong.yhdg.common.entity.pagination.PageRequest;
import cn.com.yusong.yhdg.webserver.persistence.basic.DeductionTicketOrderMapper;
import cn.com.yusong.yhdg.webserver.service.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DeductionTicketOrderService extends AbstractService {

    @Autowired
    private DeductionTicketOrderMapper deductionTicketOrderMapper;

    public Page findPage(DeductionTicketOrder deductionTicketOrder) {
        PageRequest pageRequest = new PageRequest(deductionTicketOrder.getPage(), deductionTicketOrder.getRows());
        Page page = new Page(pageRequest);
        page.setTotalItems(deductionTicketOrderMapper.findPageCount(deductionTicketOrder));
        deductionTicketOrder.setBeginIndex(page.getOffset());
        List<DeductionTicketOrder> deductionTicketOrderList = deductionTicketOrderMapper.findPageResult(deductionTicketOrder);
        page.setResult(deductionTicketOrderList);
        return page;
    }
}
