package cn.com.yusong.yhdg.agentserver.service.zd;

import cn.com.yusong.yhdg.agentserver.persistence.basic.PartnerMapper;
import cn.com.yusong.yhdg.agentserver.persistence.zd.RentBatteryTypeMapper;
import cn.com.yusong.yhdg.agentserver.persistence.zd.RentInsuranceOrderMapper;
import cn.com.yusong.yhdg.common.domain.basic.Partner;
import cn.com.yusong.yhdg.common.domain.zd.RentBatteryType;
import cn.com.yusong.yhdg.common.domain.zd.RentInsuranceOrder;
import cn.com.yusong.yhdg.common.entity.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RentInsuranceOrderService {
    @Autowired
    private RentInsuranceOrderMapper rentInsuranceOrderMapper;
    @Autowired
    private RentBatteryTypeMapper rentBatteryTypeMapper;
    @Autowired
    private PartnerMapper partnerMapper;

    public RentInsuranceOrder find(String id) {
        return rentInsuranceOrderMapper.find(id);
    }

    public Page findPage(RentInsuranceOrder search) {
        Page page = search.buildPage();
        page.setTotalItems(rentInsuranceOrderMapper.findPageCount(search));
        search.setBeginIndex(page.getOffset());
        List<RentInsuranceOrder> insuranceOrderList = rentInsuranceOrderMapper.findPageResult(search);
        for (RentInsuranceOrder order : insuranceOrderList) {
            RentBatteryType batteryType = rentBatteryTypeMapper.findByBatteryTypeAndAgent(order.getBatteryType(), order.getAgentId());
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

    public List<RentInsuranceOrder> findCanRefundByCustomerId(Long customerId) {
        return rentInsuranceOrderMapper.findCanRefundByCustomerId(customerId);
    }
}
