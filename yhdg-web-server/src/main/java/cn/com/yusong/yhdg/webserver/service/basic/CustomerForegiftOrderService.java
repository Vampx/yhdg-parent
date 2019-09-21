package cn.com.yusong.yhdg.webserver.service.basic;

import cn.com.yusong.yhdg.common.constant.ConstEnum;
import cn.com.yusong.yhdg.common.domain.basic.*;
import cn.com.yusong.yhdg.common.domain.zd.RentForegiftOrder;
import cn.com.yusong.yhdg.common.entity.json.ExtResult;
import cn.com.yusong.yhdg.common.entity.pagination.Page;
import cn.com.yusong.yhdg.webserver.config.AppConfig;
import cn.com.yusong.yhdg.webserver.persistence.basic.*;
import cn.com.yusong.yhdg.webserver.persistence.hdg.InsuranceOrderMapper;
import cn.com.yusong.yhdg.webserver.persistence.hdg.PacketPeriodOrderMapper;
import cn.com.yusong.yhdg.webserver.persistence.zc.VehicleModelMapper;
import cn.com.yusong.yhdg.webserver.persistence.zd.RentForegiftOrderMapper;
import cn.com.yusong.yhdg.webserver.service.AbstractService;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CustomerForegiftOrderService extends AbstractService {

    private static final Logger log = LogManager.getLogger(CustomerForegiftOrderService.class);

    @Autowired
    CustomerForegiftRefundDetailedMapper customerForegiftRefundDetailedMapper;
    @Autowired
    CustomerForegiftOrderMapper customerForegiftOrderMapper;
    @Autowired
    RentForegiftOrderMapper rentForegiftOrderMapper;
    @Autowired
    CustomerInstallmentRecordMapper customerInstallmentRecordMapper;
    @Autowired
    CustomerMultiOrderMapper customerMultiOrderMapper;
    @Autowired
    CustomerInOutMoneyMapper customerInOutMoneyMapper;
    @Autowired
    CustomerMapper customerMapper;
    @Autowired
    AlipayPayOrderMapper alipayPayOrderMapper;
    @Autowired
    WeixinPayOrderMapper weixinPayOrderMapper;
    @Autowired
    WeixinmpPayOrderMapper weixinmpPayOrderMapper;
    @Autowired
    AlipayfwPayOrderMapper alipayfwPayOrderMapper;
    @Autowired
    SystemConfigMapper systemConfigMapper;
    @Autowired
    VehicleModelMapper vehicleModelMapper;
    @Autowired
    AppConfig appConfig;
    @Autowired
    AgentSystemConfigMapper agentSystemConfigMapper;
    @Autowired
    CustomerAgentBalanceMapper customerAgentBalanceMapper;
    @Autowired
    CustomerExchangeBatteryMapper customerExchangeBatteryMapper;
    @Autowired
    CustomerExchangeInfoMapper customerExchangeInfoMapper;
    @Autowired
    PacketPeriodOrderMapper packetPeriodOrderMapper;
    @Autowired
    AgentMapper agentMapper;
    @Autowired
    AgentForegiftInOutMoneyMapper agentForegiftInOutMoneyMapper;
    @Autowired
    InsuranceOrderMapper insuranceOrderMapper;

    public Page findPage(CustomerForegiftOrder search) {
        Page page = search.buildPage();
        page.setTotalItems(customerForegiftOrderMapper.findPageCount(search));
        search.setBeginIndex(page.getOffset());
        List<CustomerForegiftOrder> list = customerForegiftOrderMapper.findPageResult(search);
        for (CustomerForegiftOrder customerForegiftOrder : list) {
            if (customerForegiftOrder.getStatus() != null) {
                customerForegiftOrder.setStatusName(CustomerForegiftOrder.Status.getName(customerForegiftOrder.getStatus().intValue()));
            }
        }
        page.setResult(list);
        return page;
    }

    public CustomerForegiftOrder find(String id) {
        CustomerForegiftOrder customerForegiftOrder = customerForegiftOrderMapper.find(id);
        if (customerForegiftOrder != null) {
            if (customerForegiftOrder.getBatteryType() != null) {
                String type = findBatteryType(customerForegiftOrder.getBatteryType()).getTypeName();
                customerForegiftOrder.setBatteryTypeName(type);
            }
            if (customerForegiftOrder.getStatus() != null) {
                customerForegiftOrder.setStatusName(CustomerForegiftOrder.Status.getName(customerForegiftOrder.getStatus().intValue()));
            }
        }
        return customerForegiftOrder;
    }

    @Transactional(rollbackFor = Throwable.class)
    public ExtResult repulseRefund(CustomerForegiftOrder customerForegiftOrder) {
        if (StringUtils.isEmpty(customerForegiftOrder.getMemo())) {
            return ExtResult.failResult("取消退款原因不能为空");
        }
        customerForegiftOrderMapper.updateRefund(customerForegiftOrder.getId(), null, customerForegiftOrder.getMemo(), CustomerForegiftOrder.Status.PAY_OK.getValue(), CustomerForegiftOrder.Status.APPLY_REFUND.getValue());
        return ExtResult.successResult();
    }

    public List<CustomerForegiftOrder> findCanRefundByCustomerId(Long customerId) {
        return customerForegiftOrderMapper.findCanRefundByCustomerId(customerId);
    }

    public ExtResult findByCustomerId(long customerId) {
        //换电押金
        CustomerForegiftOrder customerForegiftOrder = customerForegiftOrderMapper.findByCustomerIdAndStatus(customerId, CustomerForegiftOrder.Status.PAY_OK.getValue());
        //租电押金
        RentForegiftOrder rentForegiftOrder = rentForegiftOrderMapper.findByCustomerIdAndStatus(customerId, RentForegiftOrder.Status.PAY_OK.getValue());

        List<CustomerInstallmentRecord> recordList = customerInstallmentRecordMapper.findListByCustomerId(customerId, ConstEnum.Category.EXCHANGE.getValue());

        List<CustomerMultiOrder> orderList = customerMultiOrderMapper.findListByCustomerId(customerId, CustomerMultiOrder.Type.HD.getValue());
        if (customerForegiftOrder == null) {
            return ExtResult.failResult("该骑手尚未交纳换电押金，不能进行转让！");
        } else if (customerForegiftOrder != null && rentForegiftOrder != null){
            return ExtResult.successResult();
        } else if (rentForegiftOrder != null) {
            return ExtResult.failResult("该骑手已交纳租电押金，不能进行转让！");
        } else if (recordList.size() > 0) {
            return ExtResult.failResult("该骑手存在未完成分期记录，不能进行转让！");
        } else if (orderList.size() > 0) {
            return ExtResult.failResult("该骑手存在未完成的多通道订单，不能进行转让！");
        }
        return ExtResult.successResult();
    }
}
