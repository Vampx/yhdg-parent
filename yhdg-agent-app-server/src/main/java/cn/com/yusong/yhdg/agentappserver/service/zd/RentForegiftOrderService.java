package cn.com.yusong.yhdg.agentappserver.service.zd;

import cn.com.yusong.yhdg.agentappserver.persistence.zd.RentForegiftOrderMapper;
import cn.com.yusong.yhdg.common.domain.basic.CustomerForegiftOrder;
import cn.com.yusong.yhdg.common.domain.zd.RentForegiftOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class RentForegiftOrderService {
    @Autowired
    RentForegiftOrderMapper rentForegiftOrderMapper;

    public RentForegiftOrder find(String id) {
        return rentForegiftOrderMapper.find(id);
    }

    public List<RentForegiftOrder> findListByShopId(Integer agentId, String shopId, String keyword, Date beginTime, Date endTime) {
        return rentForegiftOrderMapper.findListByShopId(agentId, shopId, keyword, beginTime, endTime);
    }

    public List<RentForegiftOrder> findByShopId(int agentId, String shopId, List<Integer> status, String keyword, int offset, int limit) {
        return rentForegiftOrderMapper.findByShopId(agentId, shopId, status, keyword, offset, limit);
    }

    public int countShopTodayOrderNum(String shopId, Date startTime, Date endTime) {
        return rentForegiftOrderMapper.countShopTodayOrderNum(shopId, startTime, endTime);
    }

    public List<RentForegiftOrder> findRefund(Integer agentId, Integer status, Date beginTime, Date endTime, int offset, int limit) {
        return rentForegiftOrderMapper.findRefund(agentId, status, beginTime, endTime, offset, limit);
    }

    public List<RentForegiftOrder> findIncrement(Integer agentId, Integer status, Date beginTime, Date endTime, int offset, int limit) {
        return rentForegiftOrderMapper.findIncrement(agentId, status, beginTime, endTime, offset, limit);
    }


    public int findCountByShopId(String shopId, Integer agentId, List<Integer> status, Date beginTime, Date endTime) {
        return rentForegiftOrderMapper.findCountByShopId(shopId, agentId, status, beginTime, endTime);
    }

    public RentForegiftOrder findLastEndTime(Long customerId) {
        return rentForegiftOrderMapper.findLastEndTime(customerId, RentForegiftOrder.Status.PAY_OK.getValue());
    }
}
