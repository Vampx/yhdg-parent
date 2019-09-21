package cn.com.yusong.yhdg.agentappserver.service.basic;

import cn.com.yusong.yhdg.agentappserver.persistence.basic.*;
import cn.com.yusong.yhdg.agentappserver.service.AbstractService;
import cn.com.yusong.yhdg.common.constant.ConstEnum;
import cn.com.yusong.yhdg.common.domain.basic.*;
import cn.com.yusong.yhdg.common.exception.BalanceNotEnoughException;
import cn.com.yusong.yhdg.common.exception.OrderStatusExpireException;
import cn.com.yusong.yhdg.common.tool.weixin.WxMpServiceHolder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class LaxinPayOrderService extends AbstractService {

    public static final Logger log = LogManager.getLogger(LaxinPayOrderService.class);

    @Autowired
    OrderIdService orderIdService;
    @Autowired
    LaxinPayOrderMapper laxinPayOrderMapper;
    @Autowired
    LaxinPayOrderDetailMapper laxinPayOrderDetailMapper;
    @Autowired
    LaxinRecordMapper laxinRecordMapper;
    @Autowired
    AgentMapper agentMapper;
    @Autowired
    AgentInOutMoneyMapper agentInOutMoneyMapper;

    public LaxinPayOrder find(String id) {
        return laxinPayOrderMapper.find(id);
    }

    public List<LaxinPayOrder> findByStatus(int agentId, int status, int offset, int limit) {
        return laxinPayOrderMapper.findByStatus(agentId, status, offset, limit);
    }

    public int insert(LaxinPayOrder payOrder, List<LaxinRecord> recordList) {
        int effect = laxinPayOrderMapper.insert(payOrder);

        for (LaxinRecord record : recordList) {
            LaxinPayOrderDetail detail = new LaxinPayOrderDetail();
            detail.setOrderId(payOrder.getId());
            detail.setRecordId(record.getId());
            laxinPayOrderDetailMapper.insert(detail);
        }
        return effect;
    }

    @Transactional(rollbackFor = Throwable.class)
    public String payByBalance(Agent agent, int totalMoney, List<LaxinRecord> recordList, String operator) {
        Date now = new Date();

        if (agentMapper.updateBalance(agent.getId(), -totalMoney) == 0) {
            throw new BalanceNotEnoughException();
        }

        String orderId = orderIdService.newOrderId(OrderId.OrderIdType.LAXIN_PAY_ORDER);

        AgentInOutMoney inOutMoney = new AgentInOutMoney();
        inOutMoney.setAgentId(agent.getId());
        inOutMoney.setMoney(-totalMoney);
        inOutMoney.setBalance(agentMapper.find(agent.getId()).getBalance());
        inOutMoney.setBizType(AgentInOutMoney.BizType.OUT_AGENT_PAY_LAXIN_ORDER.getValue());
        inOutMoney.setType(AgentInOutMoney.Type.OUT.getValue());
        inOutMoney.setBizId(orderId);
        inOutMoney.setOperator(operator);
        inOutMoney.setCreateTime(now);
        agentInOutMoneyMapper.insert(inOutMoney);

        LaxinPayOrder laxinPayOrder = new LaxinPayOrder();
        laxinPayOrder.setId(orderId);
        laxinPayOrder.setAgentId(agent.getId());
        laxinPayOrder.setAgentName(agent.getAgentName());
        laxinPayOrder.setAgentCode(agent.getAgentCode());
        laxinPayOrder.setMoney(totalMoney);
        laxinPayOrder.setRecordCount(recordList.size());
        laxinPayOrder.setPayType(ConstEnum.PayType.BALANCE.getValue());
        laxinPayOrder.setStatus(LaxinPayOrder.Status.SUCCESS.getValue());
        laxinPayOrder.setPayTime(now);
        laxinPayOrder.setCreateTime(now);
        laxinPayOrderMapper.insert(laxinPayOrder);
        for (LaxinRecord record : recordList) {
            if (laxinRecordMapper.updateStatus(record.getId(), orderId, LaxinRecord.Status.WAIT.getValue(), LaxinRecord.Status.TRANSFER.getValue(), ConstEnum.PayType.BALANCE.getValue(), now) == 0) {
                throw new OrderStatusExpireException();
            }
        }



        for (LaxinRecord record : recordList) {
            LaxinPayOrderDetail detail = new LaxinPayOrderDetail();
            detail.setOrderId(laxinPayOrder.getId());
            detail.setRecordId(record.getId());
            laxinPayOrderDetailMapper.insert(detail);
        }

        return laxinPayOrder.getId();
    }
}
