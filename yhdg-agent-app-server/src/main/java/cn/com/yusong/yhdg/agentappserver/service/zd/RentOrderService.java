package cn.com.yusong.yhdg.agentappserver.service.zd;

import cn.com.yusong.yhdg.agentappserver.persistence.zd.RentOrderMapper;
import cn.com.yusong.yhdg.agentappserver.service.AbstractService;
import cn.com.yusong.yhdg.common.domain.zd.RentOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class RentOrderService extends AbstractService {
    @Autowired
    RentOrderMapper rentOrderMapper;

    public RentOrder find(String id) {
        return rentOrderMapper.find(id);
    }

    public List<RentOrder> findListByShop(Integer agentId, String shopId, String keyword, int offset, int limit) {
        List<RentOrder> list = rentOrderMapper.findListByShop(agentId, shopId, keyword, offset, limit);
        for (RentOrder order : list) {
            order.setBatteryTypeName(findBatteryType(order.getBatteryType()).getTypeName());
        }

        return list;
    }

    public int findCountByShopId(Integer agentId, String shopId) {
        return rentOrderMapper.findCountByShopId(agentId, shopId);
    }

    public int findActiveUserCountByShopId(Integer agentId, String shopId, List<Integer> orderStatus) {
        return rentOrderMapper.findActiveUserCountByShopId(agentId, shopId, orderStatus);
    }


    public List<RentOrder> findListByAgent(int agentId, String keyword, int offset, int limit) {
        List<RentOrder> list = rentOrderMapper.findListByAgent(agentId, keyword, offset, limit);
        for (RentOrder order : list) {
            order.setBatteryTypeName(findBatteryType(order.getBatteryType()).getTypeName());
        }

        return list;
    }

    public int countShopTodayOrderNum(String shopId, Date startTime, Date endTime) {
        return rentOrderMapper.countShopTodayOrderNum(shopId, startTime, endTime);
    }
    public List<RentOrder> findListByBatteryId(String shopId, String batteryId, int offset, int limit) {
        return rentOrderMapper.findListByBatteryId(shopId, batteryId, offset, limit);
    }

    public List<RentOrder> findListByAgentBatteryId(Integer agentId, String batteryId, int offset, int limit) {
        return rentOrderMapper.findListByAgentBatteryId(agentId, batteryId, offset, limit);
    }

}
