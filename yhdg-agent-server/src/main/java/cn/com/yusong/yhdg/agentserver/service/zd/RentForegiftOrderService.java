package cn.com.yusong.yhdg.agentserver.service.zd;

import cn.com.yusong.yhdg.agentserver.persistence.zd.RentForegiftOrderMapper;
import cn.com.yusong.yhdg.agentserver.service.AbstractService;
import cn.com.yusong.yhdg.common.domain.zd.RentForegiftOrder;
import cn.com.yusong.yhdg.common.entity.json.ExtResult;
import cn.com.yusong.yhdg.common.entity.pagination.Page;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class RentForegiftOrderService extends AbstractService {
    @Autowired
    private RentForegiftOrderMapper rentForegiftOrderMapper;

    public Page findPage(RentForegiftOrder search) {
        Page page = search.buildPage();
        page.setTotalItems(rentForegiftOrderMapper.findPageCount(search));
        search.setBeginIndex(page.getOffset());
        List<RentForegiftOrder> list = rentForegiftOrderMapper.findPageResult(search);
        for (RentForegiftOrder rentForegiftOrder : list) {
            if (rentForegiftOrder.getStatus() != null) {
                rentForegiftOrder.setStatusName(RentForegiftOrder.Status.getName(rentForegiftOrder.getStatus().intValue()));
            }
        }
        page.setResult(list);
        return page;
    }

    public RentForegiftOrder find(String id) {
        RentForegiftOrder rentForegiftOrder = rentForegiftOrderMapper.find(id);
        if (rentForegiftOrder != null) {
            if (rentForegiftOrder.getBatteryType() != null) {
                String type = findBatteryType(rentForegiftOrder.getBatteryType()).getTypeName();
                rentForegiftOrder.setBatteryTypeName(type);
            }
            if (rentForegiftOrder.getStatus() != null) {
                rentForegiftOrder.setStatusName(RentForegiftOrder.Status.getName(rentForegiftOrder.getStatus().intValue()));
            }
        }
        return rentForegiftOrder;
    }


    @Transactional(rollbackFor = Throwable.class)
    public ExtResult repulseRefund(RentForegiftOrder entity) {
        if (StringUtils.isEmpty(entity.getMemo())) {
            return ExtResult.failResult("取消退款原因不能为空");
        }
        rentForegiftOrderMapper.updateRefund(entity.getId(), null, entity.getMemo(), RentForegiftOrder.Status.PAY_OK.getValue(), RentForegiftOrder.Status.APPLY_REFUND.getValue());
        return ExtResult.successResult();
    }

    public List<RentForegiftOrder> findCanRefundByCustomerId(Long customerId) {
        return rentForegiftOrderMapper.findCanRefundByCustomerId(customerId);
    }
}
