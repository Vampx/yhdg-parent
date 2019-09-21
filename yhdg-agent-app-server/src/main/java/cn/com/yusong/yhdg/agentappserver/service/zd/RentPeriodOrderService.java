package cn.com.yusong.yhdg.agentappserver.service.zd;

import cn.com.yusong.yhdg.agentappserver.persistence.zd.RentPeriodOrderMapper;
import cn.com.yusong.yhdg.agentappserver.service.AbstractService;
import cn.com.yusong.yhdg.common.domain.hdg.PacketPeriodOrder;
import cn.com.yusong.yhdg.common.domain.zd.RentPeriodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class RentPeriodOrderService extends AbstractService {
    @Autowired
    RentPeriodOrderMapper rentPeriodOrderMapper;

    public RentPeriodOrder find(String id) {
        return rentPeriodOrderMapper.find(id);
    }

    public List<RentPeriodOrder> findListByShop(String shopId, String keyword, int offset, int limit) {
        List<RentPeriodOrder> list = rentPeriodOrderMapper.findListByShop(shopId, keyword, offset, limit);
        for (RentPeriodOrder order : list) {
            order.setBatteryTypeName(findBatteryType(order.getBatteryType()).getTypeName());
        }

        return list;
    }

    public List<RentPeriodOrder> findListByShopId(Integer agentId, String shopId, String keyword, Date beginTime, Date endTime) {
        return rentPeriodOrderMapper.findListByShopId(agentId, shopId, keyword, beginTime, endTime);
    }

    public RentPeriodOrder findLastEndTime(Long customerId) {
        return rentPeriodOrderMapper.findLastEndTime(customerId, RentPeriodOrder.Status.USED.getValue());
    }

    public int countShopTodayOrderMoney(String shopId, Date startTime, Date endTime) {
        return rentPeriodOrderMapper.countShopTodayOrderMoney(shopId, startTime, endTime);
    }

    public int findCountByShopAndStatus(String shopId, List<Integer> statusList) {
        return rentPeriodOrderMapper.findCountByShopAndStatus(shopId, statusList);
    }

    public RentPeriodOrder findOneEnabled(long customerId, int agentId) {
        RentPeriodOrder rentPeriodOrder = rentPeriodOrderMapper.findOneEnabled(customerId, RentPeriodOrder.Status.USED.getValue(), agentId);
        if (rentPeriodOrder == null) {
            rentPeriodOrder = rentPeriodOrderMapper.findOneEnabled(customerId, RentPeriodOrder.Status.NOT_USE.getValue(), agentId);
        }
        return rentPeriodOrder;
    }

}
