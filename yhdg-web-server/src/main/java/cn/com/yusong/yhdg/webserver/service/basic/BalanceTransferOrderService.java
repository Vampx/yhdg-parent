package cn.com.yusong.yhdg.webserver.service.basic;

import cn.com.yusong.yhdg.common.domain.basic.AgentDayBalanceRecord;
import cn.com.yusong.yhdg.common.domain.basic.BalanceTransferOrder;
import cn.com.yusong.yhdg.common.domain.basic.BalanceTransferOrderLog;
import cn.com.yusong.yhdg.common.domain.basic.DayBalanceRecord;
import cn.com.yusong.yhdg.common.entity.json.ExtResult;
import cn.com.yusong.yhdg.common.entity.pagination.Page;
import cn.com.yusong.yhdg.webserver.persistence.basic.BalanceTransferOrderLogMapper;
import cn.com.yusong.yhdg.webserver.persistence.basic.BalanceTransferOrderMapper;
import cn.com.yusong.yhdg.webserver.persistence.basic.AgentDayBalanceRecordMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * Created by chen on 2017/7/7.
 */
@Service
public class BalanceTransferOrderService {
    @Autowired
    BalanceTransferOrderMapper balanceTransferOrderMapper;
    @Autowired
    BalanceTransferOrderLogMapper balanceTransferOrderLogMapper;
    @Autowired
    AgentDayBalanceRecordMapper dayBalanceRecordMapper;



    public BalanceTransferOrder find(String id) {
        return balanceTransferOrderMapper.find(id);
    }

    public Page findPage(BalanceTransferOrder search) {
         Page page = search.buildPage();
        page.setTotalItems(balanceTransferOrderMapper.findPageCount(search));
        search.setBeginIndex(page.getOffset());
        page.setResult(balanceTransferOrderMapper.findPageResult(search));
        return page;
    }


    @Transactional(rollbackFor=Throwable.class)
    public ExtResult insert(BalanceTransferOrder order, Long ids[])  {
        int money = 0;
        for (Long id : ids) {
            AgentDayBalanceRecord dayBalanceRecord = dayBalanceRecordMapper.find(id);
            order.setOrderType(BalanceTransferOrder.OrderType.OFFLINE.getValue());
            order.setAgentId(dayBalanceRecord.getAgentId());
            order.setAgentName(dayBalanceRecord.getAgentName());
            order.setMoney(dayBalanceRecord.getMoney());
            money = money + dayBalanceRecord.getMoney();

        }
        order.setMoney(money);
        order.setStatus(BalanceTransferOrder.Status.SUCCESS.getValue());
        order.setHandleTime(new Date());
        order.setCreateTime(new Date());

        if (balanceTransferOrderMapper.insert(order) == 0) {
            return ExtResult.failResult("操作失败");
        } else {
            for (Long id : ids){
                dayBalanceRecordMapper.updateInfo(id, order.getId(),DayBalanceRecord.Status.SUCCESS.getValue());
            }

            BalanceTransferOrderLog log = new BalanceTransferOrderLog();
            log.setOrderId(order.getId());
            log.setOperatorName(order.getHandleUser());
            log.setContent("转账成功");
            log.setCreateTime(new Date());
            balanceTransferOrderLogMapper.insert(log);

            return ExtResult.successResult();
        }
    }

    @Transactional(rollbackFor = Throwable.class)
    public ExtResult reset(String id, String openId, String fullName, String operatorName) {
        int effect = balanceTransferOrderMapper.reset(id, BalanceTransferOrder.Status.FAILURE.getValue(), BalanceTransferOrder.Status.WAIT.getValue(), openId, fullName);
        if(effect == 0) {
            return ExtResult.failResult("订单状态已经改变");
        }
        BalanceTransferOrderLog log = new BalanceTransferOrderLog();
        log.setOrderId(id);
        log.setOperatorName(operatorName);
        log.setContent(String.format("重置订单的状态为待处理, OpenId: %s, 姓名: %s", openId, fullName));
        log.setCreateTime(new Date());
        balanceTransferOrderLogMapper.insert(log);

        return ExtResult.successResult();
    }

}
