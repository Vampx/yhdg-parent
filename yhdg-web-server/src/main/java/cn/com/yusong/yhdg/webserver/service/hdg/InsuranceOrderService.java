package cn.com.yusong.yhdg.webserver.service.hdg;

import cn.com.yusong.yhdg.common.domain.basic.AgentBatteryType;
import cn.com.yusong.yhdg.common.domain.basic.Partner;
import cn.com.yusong.yhdg.common.domain.hdg.InsuranceOrder;
import cn.com.yusong.yhdg.common.entity.pagination.Page;
import cn.com.yusong.yhdg.common.entity.pagination.PageRequest;
import cn.com.yusong.yhdg.webserver.persistence.basic.AgentBatteryTypeMapper;
import cn.com.yusong.yhdg.webserver.persistence.basic.PartnerMapper;
import cn.com.yusong.yhdg.webserver.persistence.hdg.InsuranceOrderMapper;
import cn.com.yusong.yhdg.webserver.service.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author
 */
@Service
public class InsuranceOrderService extends AbstractService{
    @Autowired
    InsuranceOrderMapper insuranceOrderMapper;
    @Autowired
    AgentBatteryTypeMapper agentBatteryTypeMapper;
    @Autowired
    PartnerMapper partnerMapper;

    public InsuranceOrder find(String id) {
        return insuranceOrderMapper.find(id);
    }

    public Page findPage(InsuranceOrder insuranceOrder) {
        PageRequest pageRequest = new PageRequest(insuranceOrder.getPage(), insuranceOrder.getRows());
        Page page = new Page(pageRequest);
        page.setTotalItems(insuranceOrderMapper.findPageCount(insuranceOrder));
        insuranceOrder.setBeginIndex(page.getOffset());
        List<InsuranceOrder> insuranceOrderList = insuranceOrderMapper.findPageResult(insuranceOrder);
        for (InsuranceOrder order : insuranceOrderList) {
            AgentBatteryType batteryType = agentBatteryTypeMapper.findForName(order.getBatteryType(), order.getAgentId());
            if (batteryType != null) {
                order.setBatteryTypeName(batteryType.getTypeName());
            }
            Partner partner = partnerMapper.find(order.getPartnerId());
            if (partner != null) {
                order.setPartnerName(partner.getPartnerName());
            }
        }
        page.setResult(insuranceOrderList);
        return page;
    }

    public List<InsuranceOrder> findCanRefundByCustomerId(Long customerId) {
        return insuranceOrderMapper.findCanRefundByCustomerId(customerId);
    }
}
