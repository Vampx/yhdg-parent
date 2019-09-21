package cn.com.yusong.yhdg.agentappserver.service.basic;

import cn.com.yusong.yhdg.agentappserver.persistence.basic.CustomerForegiftOrderMapper;
import cn.com.yusong.yhdg.agentappserver.service.AbstractService;
import cn.com.yusong.yhdg.common.domain.basic.CustomerForegiftOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;


@Service
public class CustomerForegiftOrderService extends AbstractService {

    @Autowired
    CustomerForegiftOrderMapper customerForegiftOrderMapper;

    public CustomerForegiftOrder find(String id) {
        return customerForegiftOrderMapper.find(id);
    }

    public List<CustomerForegiftOrder> findListByCabinetId(int agentId, String cabinetId, String keyword, Date beginTime, Date endTime) {
        return customerForegiftOrderMapper.findListByCabinetId(agentId, cabinetId, keyword, beginTime, endTime);
    }

    public List<CustomerForegiftOrder> findByCabinetId(int agentId, String cabinetId, List<Integer> status, String keyword, int offset, int limit) {
        return customerForegiftOrderMapper.findByCabinetId(agentId, cabinetId, status, keyword, offset, limit);
    }

    public List<CustomerForegiftOrder> findIncrement(Integer agentId, Integer status, Date beginTime, Date endTime, int offset, int limit) {
        return customerForegiftOrderMapper.findIncrement(agentId, status, beginTime, endTime, offset, limit);
    }

    public List<CustomerForegiftOrder> findRefund(Integer agentId, Integer status, Date beginTime, Date endTime, int offset, int limit) {
        return customerForegiftOrderMapper.findRefund(agentId, status, beginTime, endTime, offset, limit);
    }

    public int countShopTodayOrderNum(String shopId, Date startTime, Date endTime) {
        return customerForegiftOrderMapper.countShopTodayOrderNum(shopId, startTime, endTime);
    }

    public int findCountByCabinetId(String cabinetId, Integer agentId, List<Integer> status, Date beginTime, Date endTime) {
        return customerForegiftOrderMapper.findCountByCabinetId(cabinetId, agentId, status, beginTime, endTime);
    }

    public CustomerForegiftOrder findOneEnabled(long customerId, int agentId) {
        CustomerForegiftOrder customerForegiftOrder = customerForegiftOrderMapper.findOneEnabled(customerId, CustomerForegiftOrder.Status.PAY_OK.getValue(), agentId);
        return customerForegiftOrder;
    }

}
