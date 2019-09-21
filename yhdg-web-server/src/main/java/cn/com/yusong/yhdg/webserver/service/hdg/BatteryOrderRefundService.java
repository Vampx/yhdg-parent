package cn.com.yusong.yhdg.webserver.service.hdg;

import cn.com.yusong.yhdg.common.domain.basic.Customer;
import cn.com.yusong.yhdg.common.domain.basic.CustomerInOutMoney;
import cn.com.yusong.yhdg.common.domain.hdg.BatteryOrder;
import cn.com.yusong.yhdg.common.domain.hdg.BatteryOrderRefund;
import cn.com.yusong.yhdg.common.entity.json.ExtResult;
import cn.com.yusong.yhdg.common.entity.pagination.Page;
import cn.com.yusong.yhdg.webserver.persistence.basic.CustomerAgentBalanceMapper;
import cn.com.yusong.yhdg.webserver.persistence.basic.CustomerInOutMoneyMapper;
import cn.com.yusong.yhdg.webserver.persistence.basic.CustomerMapper;
import cn.com.yusong.yhdg.webserver.persistence.hdg.BatteryOrderMapper;
import cn.com.yusong.yhdg.webserver.persistence.hdg.BatteryOrderRefundMapper;
import cn.com.yusong.yhdg.webserver.service.AbstractService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class BatteryOrderRefundService extends AbstractService {

    @Autowired
    BatteryOrderRefundMapper batteryOrderRefundMapper;
    @Autowired
    BatteryOrderMapper batteryOrderMapper;
    @Autowired
    CustomerMapper customerMapper;
    @Autowired
    CustomerInOutMoneyMapper customerInOutMoneyMapper;
    @Autowired
    CustomerAgentBalanceMapper customerAgentBalanceMapper;

    public Page findPage(BatteryOrderRefund search) {
        Page page = search.buildPage();
        page.setTotalItems(batteryOrderRefundMapper.findPageCount(search));
        search.setBeginIndex(page.getOffset());
        List<BatteryOrderRefund> list = batteryOrderRefundMapper.findPageResult(search);
        for (BatteryOrderRefund batteryOrderRefund: list) {
            Integer agentId = batteryOrderRefund.getAgentId();
            //设置运营商名称
            if (agentId != null) {
                batteryOrderRefund.setAgentName(findAgentInfo(batteryOrderRefund.getAgentId()).getAgentName());
            }
        }
        page.setResult(list);
        return page;
    }

    public BatteryOrderRefund find(String id) {
        return batteryOrderRefundMapper.find(id);
    }

    @Transactional(rollbackFor=Throwable.class)
    public ExtResult insert(String userName, BatteryOrder batteryOrder) {
//
//        BatteryOrder entity = batteryOrderMapper.find(batteryOrder.getId());
//
//        if (entity.getMoney() == null||entity.getMoney().equals(0)) {
//            return ExtResult.failResult("退款失败,退款金额不能为0");
//        }
//        if (entity.getOrderStatus() != BatteryOrder.OrderStatus.PAY.getValue() ) {
//            return ExtResult.failResult("只有已付款状态才可退款！");
//        }
//        if (batteryOrder.getRefundMoney() > entity.getMoney()){
//            return ExtResult.failResult("退款金额不能大于订单金额");
//        }
//        if(StringUtils.isEmpty(batteryOrder.getRefundReason())){
//            return ExtResult.failResult("退款原因不能为空");
//        }
//
//        Customer customer = customerMapper.find(entity.getCustomerId());
//        if (customer == null) {
//            return ExtResult.failResult("客户不存在");
//        }
//
//        BatteryOrderRefund batteryOrderRefund = new BatteryOrderRefund();
//        batteryOrderRefund.setId(entity.getId());
//        batteryOrderRefund.setAgentId(entity.getAgentId());
//        batteryOrderRefund.setMoney(entity.getMoney());
//        batteryOrderRefund.setCustomerId(entity.getCustomerId());
//        batteryOrderRefund.setCustomerMobile(entity.getCustomerMobile());
//        batteryOrderRefund.setCustomerFullname(entity.getCustomerFullname());
//        batteryOrderRefund.setRefundTime(new Date());
//        batteryOrderRefund.setRefundStatus(BatteryOrderRefund.RefundStatus.REFUND_SUCCESS.getValue());
//        batteryOrderRefund.setCreateTime(new Date());
//        batteryOrderRefund.setRefundOperator(userName);
//        batteryOrderRefund.setRefundReason(batteryOrder.getRefundReason());
//        batteryOrderRefund.setRefundMoney(batteryOrder.getRefundMoney());
//        batteryOrderRefundMapper.insert(batteryOrderRefund);
//
//        CustomerInOutMoney money = new CustomerInOutMoney();
//        money.setCustomerId(entity.getCustomerId());
//        money.setBizType(CustomerInOutMoney.BizType.IN_CUSTOMER_EXCHANGE_REFUND.getValue());
//        money.setBizId(entity.getId());
//        money.setMoney(batteryOrder.getRefundMoney());
//        money.setCreateTime(new Date());
//        customerInOutMoneyMapper.insert(money);
//
//        customerMapper.updateBalance(entity.getCustomerId(), 0, batteryOrder.getRefundMoney());
//        if (customerAgentBalanceMapper.findByCustomerId(entity.getCustomerId()) != null) {
//            customerAgentBalanceMapper.updateBalance(entity.getCustomerId(), 0);
//        }
//        batteryOrderMapper.updateStatus(batteryOrder.getId(),
//                BatteryOrderRefund.RefundStatus.REFUND_SUCCESS.getValue(),
//                batteryOrder.getRefundMoney(), new Date(),
//                batteryOrder.getRefundReason());

        return ExtResult.successResult();
    }

}
