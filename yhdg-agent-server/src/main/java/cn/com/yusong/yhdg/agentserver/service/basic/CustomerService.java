package cn.com.yusong.yhdg.agentserver.service.basic;

import cn.com.yusong.yhdg.common.constant.ConstEnum;
import cn.com.yusong.yhdg.common.constant.Constant;
import cn.com.yusong.yhdg.common.domain.basic.*;
import cn.com.yusong.yhdg.common.domain.hdg.BatteryOrder;
import cn.com.yusong.yhdg.common.domain.hdg.PacketPeriodOrder;
import cn.com.yusong.yhdg.common.entity.AreaCache;
import cn.com.yusong.yhdg.common.entity.json.DataResult;
import cn.com.yusong.yhdg.common.entity.json.ExtResult;
import cn.com.yusong.yhdg.common.entity.pagination.Page;
import cn.com.yusong.yhdg.common.tool.memcached.MemCachedClient;
import cn.com.yusong.yhdg.common.utils.CodecUtils;
import cn.com.yusong.yhdg.agentserver.persistence.basic.*;
import cn.com.yusong.yhdg.agentserver.persistence.hdg.*;
import cn.com.yusong.yhdg.agentserver.service.AbstractService;
import jxl.Sheet;
import jxl.Workbook;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

@Service
public class CustomerService extends AbstractService {
    @Autowired
    MemCachedClient memCachedClient;
    @Autowired
    CustomerMapper customerMapper;
    @Autowired
    CustomerDayStatsMapper customerDayStatsMapper;
    @Autowired
    CustomerNoticeMessageMapper customerNoticeMessageMapper;
    @Autowired
    CabinetAddressCorrectionMapper cabinetAddressCorrectionMapper;
    @Autowired
    AlipayPayOrderMapper alipayPayOrderMapper;
    @Autowired
    AlipayfwPayOrderMapper alipayfwPayOrderMapper;
    @Autowired
    WeixinPayOrderMapper weixinPayOrderMapper;
    @Autowired
    WeixinmpPayOrderMapper weixinmpPayOrderMapper;
    @Autowired
    FaultFeedbackMapper faultFeedbackMapper;
    @Autowired
    PacketPeriodOrderRefundMapper packetPeriodOrderRefundMapper;
    @Autowired
    PacketPeriodOrderMapper packetPeriodOrderMapper;
    @Autowired
    BatteryMapper batteryMapper;
    @Autowired
    BackBatteryOrderMapper backBatteryOrderMapper;
    @Autowired
    BatteryOrderRefundMapper batteryOrderRefundMapper;
    @Autowired
    BatteryOrderMapper batteryOrderMapper;
    @Autowired
    CustomerCouponTicketMapper customerCouponTicketMapper;
    @Autowired
    CustomerForegiftOrderMapper customerForegiftOrderMapper;
    @Autowired
    CustomerInOutMoneyMapper customerInOutMoneyMapper;
    @Autowired
    CustomerDepositOrderMapper customerDepositOrderMapper;
    @Autowired
    FeedbackMapper feedbackMapper;
    @Autowired
    AgentInOutMoneyMapper agentInOutMoneyMapper;
    @Autowired
    CustomerForegiftRefundDetailedMapper customerForegiftRefundDetailedMapper;
    @Autowired
    AreaCache areaCache;
    @Autowired
    CabinetMapper cabinetMapper;
    @Autowired
    AgentMapper agentMapper;
    @Autowired
    CustomerBalanceDeductMapper customerBalanceDeductMapper;
    @Autowired
    MpOpenIdMapper mpOpenIdMapper;
    @Autowired
    FwOpenIdMapper fwOpenIdMapper;
    @Autowired
    CustomerAgentBalanceMapper customerAgentBalanceMapper;
    @Autowired
    CustomerExchangeInfoMapper customerExchangeInfoMapper;
    @Autowired
    CustomerExchangeBatteryMapper customerExchangeBatteryMapper;

    public Customer findOpenId(String mobile) {
        return customerMapper.findOpenId(mobile);
    }

    public List<Customer> findList(Customer customer) {
        return customerMapper.findPageResult(customer);
    }

    public Customer find(long id) {
        Customer customer = customerMapper.find(id);
        return customer;
    }

    public Page findPage(Customer search) {
        Page page = search.buildPage();
        page.setTotalItems(customerMapper.findPageCount(search));
        search.setBeginIndex(page.getOffset());
        List<Customer> customerList = customerMapper.findPageResult(search);
        for (Customer customer : customerList) {
            if (customer.getAgentId() != null) {
                AgentInfo agentInfo = findAgentInfo(customer.getAgentId());
                if (agentInfo != null) {
                    customer.setAgentName(agentInfo.getAgentName());
                }
            }
            if (customer.getPartnerId() != null) {
                Partner partner = findPartner(customer.getPartnerId());
                if (partner != null) {
                    customer.setPartnerName(partner.getPartnerName());
                }
            }
        }
        page.setResult(customerList);
        return page;
    }


    public Page findPayeePage(Customer search) {
        Page page = search.buildPage();
        page.setTotalItems(customerMapper.findPayeePageCount(search));
        search.setBeginIndex(page.getOffset());
        List<Customer> list = customerMapper.findPayeePageResult(search);
        page.setResult(list);
        return page;
    }

    public Page findWhitelistCustomerPage(Customer customer) {
        Page page = customer.buildPage();
        page.setTotalItems(customerMapper.findWhitelistCustomerPageCount(customer));
        customer.setBeginIndex(page.getOffset());
        List<Customer> customerList = customerMapper.findWhitelistCustomerPageResult(customer);
        page.setResult(customerList);
        return page;
    }

    public Page findPageByBindTime(Customer customer) {
        Page page = customer.buildPage();
        page.setTotalItems(customerMapper.findPageCountByBindTime(customer));
        customer.setBeginIndex(page.getOffset());

        List<Customer> customerList = customerMapper.findPageResultByBindTime(customer);
        page.setResult(customerList);
        return page;
    }

    public List<Customer> findResult(List<Customer> customerList) {
        return customerList;
    }

    public Page findPages(Customer customer) {
        Page page = customer.buildPage();
        page.setTotalItems(customerMapper.findPageCounts(customer));
        customer.setBeginIndex(page.getOffset());
        page.setResult(setAreaProperties(areaCache, customerMapper.findPageResults(customer)));
        return page;
    }


    public boolean findUnique(String mobile) {
        return customerMapper.findUnique(mobile) == 0;
    }

    @Transactional(rollbackFor = Throwable.class)
    public ExtResult create(Customer customer) throws IOException {

        boolean mobile = customerMapper.findUnique(customer.getMobile()) == 0;
        if (!mobile) {
            return ExtResult.failResult("该手机号码已存在");
        }

        if (StringUtils.isEmpty(customer.getPassword())) {
            customer.setPassword(CodecUtils.password(Constant.DEFAULT_PASSWORD));
        } else {
            customer.setPassword(CodecUtils.password(customer.getPassword()));
        }

        customer.setBalance(0);
        customer.setGiftBalance(0);
        customer.setRegisterType(Customer.RegisterType.WEB.getValue());
        customer.setHdForegiftStatus(Customer.HdForegiftStatus.UN_PAID.getValue());
        customer.setZdForegiftStatus(Customer.ZdForegiftStatus.UN_PAID.getValue());
        customer.setAuthStatus(Customer.AuthStatus.NOT.getValue());
        customer.setCreateTime(new Date());

        customerMapper.insert(customer);

        return ExtResult.successResult();
    }


    @Transactional(rollbackFor = Throwable.class)
    public ExtResult update(Customer customer) {

        if (StringUtils.isEmpty(customer.getPassword())) {
            customer.setPassword(null);
        } else {
            customer.setPassword(CodecUtils.password(customer.getPassword()));
        }
        int total = customerMapper.update(customer);
        if (total == 1) {
            return ExtResult.successResult();
        } else {
            return ExtResult.failResult("修改失败！");
        }
    }

    @Transactional(rollbackFor = Throwable.class)
    public ExtResult resignation(Long customerId, String operator) {
//        Customer customer = customerMapper.find(customerId);
//        CustomerAgentBalance agentBalance = customerAgentBalanceMapper.findByCustomerId(customerId);
//        if (customer == null) {
//            return ExtResult.failResult("骑手不存在!");
//        }
//        if (customerExchangeBatteryMapper.findByCustomerId(customerId) != null) {
//            return ExtResult.failResult("骑手下存在未退租电池,不可离职!");
//        }
//        if (agentBalance != null) {
//            return ExtResult.failResult("骑手运营商资金小于0,不可离职!");
//        }
//        CustomerExchangeInfo customerExchangeInfo = customerExchangeInfoMapper.find(customerId);
//        CustomerForegiftOrder customerForegiftOrder = null;
//        if (customerExchangeInfo != null) {
//             customerForegiftOrder = customerForegiftOrderMapper.find(customerExchangeInfo.getForegiftOrderId());
//            if (customerForegiftOrder == null) {
//                return ExtResult.failResult("押金订单不存在");
//            }
//        }
//        Agent agent = null;
//        if (customer != null) {
//            if (agentBalance != null) {
//                agent = agentMapper.find(agentBalance.getAgentId());
//                if (agent == null) {
//                    return ExtResult.failResult("骑手所属运营商不存在!");
//                }
//            }
//        }
//
//        int total = customerAgentBalanceMapper.resignation(customer.getId(), agentBalance.getAgentBalance());
//
//        if (customerForegiftOrder != null) {
//            List<Integer> statusList = Arrays.asList(CustomerForegiftOrder.Status.PAY_OK.getValue(), CustomerForegiftOrder.Status.APPLY_REFUND.getValue());
//            customerForegiftOrderMapper.updateStatus(customerForegiftOrder.getId(),
//                    CustomerForegiftOrder.Status.REFUND_SUCCESS.getValue(),
//                    0, operator, new Date(),
//                    null,
//                    null, new Date());
//
//            customerExchangeInfoMapper.clearBatteryForegiftOrderId(customer.getId(), statusList);
//        }
//
//        if (total != 1) {
//            return ExtResult.failResult("修改失败");
//        }
//        if (agentBalance != null) {
//            CustomerInOutMoney customerInOutMoney = new CustomerInOutMoney();
//            customerInOutMoney.setMoney(-agentBalance.getAgentBalance().intValue());
//            customerInOutMoney.setCustomerId(customer.getId());
//            customerInOutMoney.setBizId("");
//            customerInOutMoney.setCreateTime(new Date());
//            customerInOutMoney.setBizType(CustomerInOutMoney.BizType.OUT_CUSTOMER_RESIGNATION.getValue());
//            customerInOutMoneyMapper.insert(customerInOutMoney);
//
//            agentMapper.updateBalance(agentBalance.getAgentId(), agentBalance.getAgentBalance());
//
//            AgentInOutMoney agentInOutMoney = new AgentInOutMoney();
//            agentInOutMoney.setMoney(agentBalance.getAgentBalance().intValue());
//            if (agent != null && customer != null) {
//                agentInOutMoney.setBalance(agent.getBalance() + agentBalance.getAgentBalance().intValue());
//            }
//            if (agent != null) {
//                agentInOutMoney.setAgentId(agent.getId());
//            }
//            agentInOutMoney.setBizId("骑手(" + customer.getMobile() + ")离职退款");
//            agentInOutMoney.setCreateTime(new Date());
//            agentInOutMoney.setBizType(AgentInOutMoney.BizType.IN_AGENT_CUSTOMER_ROLLBACK.getValue());
//            agentInOutMoney.setType(AgentInOutMoney.Type.IN.getValue());
//            agentInOutMoney.setOperator(operator);
//            agentInOutMoneyMapper.insert(agentInOutMoney);
//        }
        return ExtResult.successResult();
    }

    @Transactional(rollbackFor = Throwable.class)
    public ExtResult refund(Customer customer, Integer giftBalance, String handleName, String memo) {
//        Date now = new Date();
//        CustomerInOutMoney money = new CustomerInOutMoney();
//        money.setCustomerId(customer.getId());
//        money.setBizType(CustomerInOutMoney.BizType.OUT_CUSTOMER_BALANCE_REFUND.getValue());
//        money.setBizId(customer.getMobile());
//        money.setMoney(-customer.getBalance().intValue());
//        money.setCreateTime(now);
//        customerInOutMoneyMapper.insert(money);
//
//        CustomerBalanceDeduct balanceDeduct = new CustomerBalanceDeduct();
//        balanceDeduct.setCustomerId(customer.getId().intValue());
//        balanceDeduct.setMobile(customer.getMobile());
//        balanceDeduct.setFullname(customer.getFullname());
//        balanceDeduct.setMoney(customer.getBalance().intValue());
//        balanceDeduct.setHandlerName(handleName);
//        balanceDeduct.setMemo(memo);
//        balanceDeduct.setCreateTime(now);
//        customerBalanceDeductMapper.insert(balanceDeduct);
//        int total = customerMapper.updateBalance(customer.getId(), -customer.getBalance().intValue(), -giftBalance);
//        int total1 = 0;
//        if (customerAgentBalanceMapper.findByCustomerId(customer.getId()) != null) {
//           total1 += customerAgentBalanceMapper.updateBalance(customer.getId(), 0);
//        }
//        if (total == 1 && total1 == 1) {
            return ExtResult.successResult();
//        } else {
//            return ExtResult.failResult("扣款失败！");
//        }
    }

    @Transactional(rollbackFor = Throwable.class)
    public ExtResult delete(long id) {
        List<CustomerDepositOrder> DepositOrderList = customerDepositOrderMapper.findList(id);
        List<PacketPeriodOrder> periodOrderList = packetPeriodOrderMapper.findList(id);
        List<BatteryOrder> BatteryOrderList = batteryOrderMapper.findList(id);
        if (DepositOrderList.size() > 0 || periodOrderList.size() > 0 || BatteryOrderList.size() > 0) {
            return ExtResult.failResult("用户已产生订单数据, 不允许删除！");
        } else {
            //客户
            customerAgentBalanceMapper.deleteByCustomerId(id);
            customerExchangeBatteryMapper.deleteByCustomerId(id);
            customerExchangeInfoMapper.delete(id);
            customerMapper.delete(id);
        }
        return ExtResult.successResult();
    }

    @Transactional(rollbackFor = Throwable.class)
    public ExtResult batchRemove(long[] ids) {
        for (long id : ids) {
            List<CustomerCouponTicket> ticketList = customerCouponTicketMapper.findList(id);
            List<CustomerDepositOrder> DepositOrderList = customerDepositOrderMapper.findList(id);
            List<PacketPeriodOrder> periodOrderList = packetPeriodOrderMapper.findList(id);
            List<BatteryOrder> BatteryOrderList = batteryOrderMapper.findList(id);
            if (ticketList.size()> 0 || DepositOrderList.size() > 0 || periodOrderList.size() > 0 || BatteryOrderList.size() > 0) {
                return ExtResult.failResult("用户已产生订单数据, 不允许删除！");
            } else {
                //客户
                customerAgentBalanceMapper.deleteByCustomerId(id);
                customerExchangeBatteryMapper.deleteByCustomerId(id);
                customerExchangeInfoMapper.delete(id);
                customerMapper.delete(id);
            }
        }

        return ExtResult.successResult();
    }

    public int findCount(Date beginTime, Date endTime) {
        return customerMapper.findCount(beginTime, endTime);
    }

    @Transactional(rollbackFor = Throwable.class)
    public ExtResult batchActive(long[] id) {
        for (long rId : id) {
            Customer customer = customerMapper.find(rId);
            if (customer.getIsActive() == ConstEnum.Flag.FALSE.getValue()) { //启用
                customerMapper.updateActive(rId, ConstEnum.Flag.TRUE.getValue());
            } else {
                customerMapper.updateActive(rId, ConstEnum.Flag.FALSE.getValue());
            }
        }
        return ExtResult.successResult();
    }

    @Transactional(rollbackFor = Throwable.class)
    public ExtResult batchImportCustomer(File mFile, Integer company, Integer batteryType) {

        Workbook workbook = null;
        try {
            workbook = Workbook.getWorkbook(mFile);
        } catch (Exception e) {
            return ExtResult.failResult("操作失败");
        }
         Sheet sheet = workbook.getSheet(0);
        //获取总条数
        int rows = sheet.getRows();
        //成功条数
        int successCount = 0;
        int failCount = 0;
        StringBuilder repeatBuilder = new StringBuilder("");
        StringBuilder errorBuilder = new StringBuilder("");
        StringBuilder failBuilder = new StringBuilder("");
        for (int row = 1; row < rows; row++) {
            Customer customer = new Customer();
            //获取姓名
            String name = sheet.getCell(0, row).getContents().trim();
            //获取身份证
            String icCard = sheet.getCell(1, row).getContents().trim();
            //获取手机号
            String mobile = sheet.getCell(2, row).getContents().trim();
            if (StringUtils.isEmpty(mobile) || StringUtils.isEmpty(icCard) || StringUtils.isEmpty(name)) {
                errorBuilder.append((row + 1) + ",");
                failCount++;
                continue;
            }
            if (!Pattern.matches(Constant.VALIDATOR_PATTERN_MOBILE, mobile)) {
                errorBuilder.append((row + 1) + ",");
                failCount++;
                continue;
            }
            boolean m = customerMapper.findUnique(mobile) == 0;
            if (!m) {
                repeatBuilder.append((row + 1) + ",");
                failCount++;
                continue;
            }
            customer.setPartnerId(Constant.SYSTEM_PARTNER_ID);
            customer.setMobile(mobile);
            customer.setPassword(CodecUtils.password(Constant.DEFAULT_PASSWORD));
            customer.setBalance(0);
            customer.setGiftBalance(0);
            customer.setFullname(name);
            customer.setNickname(name);
            customer.setIdCard(icCard);
            customer.setIsActive(ConstEnum.Flag.TRUE.getValue());
            customer.setRegisterType(Customer.RegisterType.WEB.getValue());
            customer.setHdForegiftStatus(Customer.HdForegiftStatus.UN_PAID.getValue());
            customer.setZdForegiftStatus(Customer.ZdForegiftStatus.UN_PAID.getValue());
            customer.setAuthStatus(Customer.AuthStatus.NOT.getValue());
            customer.setCreateTime(new Date());
            try {
                int total = 0;
                total = customerMapper.insert(customer);
                if (total > 0) {
                    successCount++;
                }

            } catch (Exception e) {
                errorBuilder.append((row + 1) + ",");
                failCount++;
                continue;
            }
        }
        if (failCount > 0) {
            failBuilder.append("失败" + failCount + "条!");
        }
        if (StringUtils.isNotEmpty(repeatBuilder.toString())) {
            failBuilder.append("行(" + repeatBuilder.toString() + ")数据已存在!");
        }
        if (StringUtils.isNotEmpty(errorBuilder.toString())) {
            failBuilder.append("行(" + errorBuilder.toString() + ")数据添加错误!");
        }
        return DataResult.successResult("总条数" + --rows + "条," + "成功导入" + successCount + "条!" + failBuilder.toString());
    }

    @Transactional(rollbackFor = Throwable.class)
    public int mpUnbindMobile(long id, String openId) {
        mpOpenIdMapper.updateCustomerId(openId, null);
        int effect = customerMapper.updateMpOpenId(id, null, null, null);
        if (effect > 0) {
            Customer customer = customerMapper.find(id);
            String key = "token:" + customer.getMpLoginToken();
            memCachedClient.delete(key);
        }
        return effect;
    }

    @Transactional(rollbackFor = Throwable.class)
    public int fwUnbindMobile(long id, String openId) {
        fwOpenIdMapper.updateCustomerId(openId, null);
        int effect = customerMapper.updateFwOpenId(id, null, null, null);
        if (effect > 0) {
            Customer customer = customerMapper.find(id);
            String key = "token:" + customer.getFwLoginToken();
            memCachedClient.delete(key);
        }
        return effect;
    }


    public ExtResult deleteWhitelistPriceGroupId(long id) {
        Customer customer = customerMapper.find(id);
        customerAgentBalanceMapper.updateWhitelistPriceGroupId(id, customerAgentBalanceMapper.findByCustomerId(id).getAgentId());
        /*CustomerExchangeInfo customerExchangeInfo = customerExchangeInfoMapper.find(id);
        if (customerExchangeInfo != null) {
            CustomerForegiftOrder foregiftOrder = customerForegiftOrderMapper.find(customerExchangeInfoMapper.find(id).getForegiftOrderId());
            customerMapper.updatePriceGroupId(id, foregiftOrder.getPriceGroupId());
        }*/
        return ExtResult.successResult();
    }

    @Transactional(rollbackFor = Throwable.class)
    public ExtResult appUnbindMobile(Long[] idList) {
        int count = 0;
        for (Long id : idList) {
            Customer customer = customerMapper.find(id);
            if (customer == null) {
                return ExtResult.failResult("骑手不存在！");
            }
            List<CustomerExchangeBattery> batteryList =  customerExchangeBatteryMapper.findByCustomerId(id);
            if (batteryList != null && batteryList.size() > 0) {
                return ExtResult.failResult("骑手下存在未退租电池,不可解绑!");
            }
            if (customer.getBalance() > 0 && customer.getBalance() != null) {
                return ExtResult.failResult("骑手余额不为空,不可解绑!");
            }
            if (customerExchangeInfoMapper.find(id) != null) {
                return ExtResult.failResult("骑手押金不为空,不可解绑!");
            }
            packetPeriodOrderMapper.updateStatus(id, PacketPeriodOrder.Status.EXPIRED.getValue());
            if (customer.getFwOpenId() != null) {
                fwUnbindMobile(id, customer.getFwOpenId());
            }
            if (customer.getMpOpenId() != null) {
                mpUnbindMobile(id, customer.getMpOpenId());
            }
            PartnerMpOpenId mpOpenId = mpOpenIdMapper.fingCustomerId(customer.getId());
            if (mpOpenId != null) {
                mpOpenIdMapper.updateCustomerId(mpOpenId.getOpenId(), null);
            }
        }
        return ExtResult.successResult(String.format("成功解绑%d个用户", count));

    }
}
