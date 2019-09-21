package cn.com.yusong.yhdg.webserver.service.basic;

import cn.com.yusong.yhdg.common.constant.ConstEnum;
import cn.com.yusong.yhdg.common.constant.Constant;
import cn.com.yusong.yhdg.common.domain.basic.*;
import cn.com.yusong.yhdg.common.domain.hdg.*;
import cn.com.yusong.yhdg.common.entity.AreaCache;
import cn.com.yusong.yhdg.common.entity.json.DataResult;
import cn.com.yusong.yhdg.common.entity.json.ExtResult;
import cn.com.yusong.yhdg.common.entity.pagination.Page;
import cn.com.yusong.yhdg.common.tool.memcached.MemCachedClient;
import cn.com.yusong.yhdg.common.utils.CodecUtils;
import cn.com.yusong.yhdg.webserver.persistence.basic.*;
import cn.com.yusong.yhdg.webserver.persistence.hdg.*;
import cn.com.yusong.yhdg.webserver.service.AbstractService;
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
    CustomerInstallmentRecordMapper customerInstallmentRecordMapper;
    @Autowired
    CustomerMultiOrderMapper customerMultiOrderMapper;
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
    @Autowired
    ExchangeWhiteListMapper exchangeWhiteListMapper;
    @Autowired
    CustomerWhitelistMapper customerWhitelistMapper;
    @Autowired
    WeixinmpOpenIdMapper weixinmpOpenIdMapper;
    @Autowired
    PartnerMpOpenIdMapper partnerMpOpenIdMapper;
    @Autowired
    AlipayfwOpenIdMapper alipayfwOpenIdMapper;
    @Autowired
    PartnerFwOpenIdMapper partnerFwOpenIdMapper;
    @Autowired
    ForegiftPacketMoneyTransferRecordMapper foregiftPacketMoneyTransferRecordMapper;

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

        //未交押金的客户
        search.setUnPaidForegiftFlag(ConstEnum.Flag.TRUE.getValue());
        int countByUnPaidForegift = customerMapper.findPageCount(search);
        //已交换电押金的客户
        search.setUnPaidForegiftFlag(null);
        search.setHdPaidForegiftFlag(ConstEnum.Flag.TRUE.getValue());
        int countByHdPaidForegift = customerMapper.findPageCount(search);
        //已交租电押金的客户
        search.setHdPaidForegiftFlag(null);
        search.setZdPaidForegiftFlag(ConstEnum.Flag.TRUE.getValue());
        int countByZdPaidForegift = customerMapper.findPageCount(search);
        //已退换电押金的客户
        search.setZdPaidForegiftFlag(null);
        search.setHdRefundedForegiftFlag(ConstEnum.Flag.TRUE.getValue());
        int countByHdRefundedForegift = customerMapper.findPageCount(search);
        //已退租电押金的客户
        search.setHdRefundedForegiftFlag(null);
        search.setZdRefundedForegiftFlag(ConstEnum.Flag.TRUE.getValue());
        int countByZdRefundedForegift = customerMapper.findPageCount(search);

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
            CustomerExchangeInfo customerExchangeInfo = customerExchangeInfoMapper.find(customer.getId());
            if (customerExchangeInfo != null && customerExchangeInfo.getBalanceCabinetId() != null) {
                Cabinet cabinet = cabinetMapper.find(customerExchangeInfo.getBalanceCabinetId());
                if (cabinet != null) {
                    customer.setBalanceCabinetName(cabinet.getCabinetName());
                }
            }
        }
        Customer customer = null;
        if (customerList.size() >= 1) {
            customer = customerList.get(0);
        }
        if (customer != null) {
            customer.setFirstDataFlag(ConstEnum.Flag.TRUE.getValue());
            customer.setCountByUnPaidForegift(countByUnPaidForegift);
            customer.setCountByHdPaidForegift(countByHdPaidForegift);
            customer.setCountByZdPaidForegift(countByZdPaidForegift);
            customer.setCountByHdRefundedForegift(countByHdRefundedForegift);
            customer.setCountByZdRefundedForegift(countByZdRefundedForegift);
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

    public Page findTransferCustomerPage(Customer customer) {
        Page page = customer.buildPage();
        page.setTotalItems(customerMapper.findTransferCustomerPageCount(customer));
        customer.setBeginIndex(page.getOffset());
        List<Customer> customerList = customerMapper.findTransferCustomerPageResult(customer);
        page.setResult(customerList);
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

    public Customer findByMobile(String mobile) {
        return customerMapper.findByMobile(mobile);
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
//        }else if (total == 1) {
//            return ExtResult.successResult();
//        }
//        if (total == 1 && total1 == 1) {
//            return ExtResult.successResult();
//        } else {
            return ExtResult.failResult("扣款失败！");
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
//        mpOpenIdMapper.updateCustomerId(openId, null);
        int effect = customerMapper.updateMpOpenId(id, null, null, null);
        if (effect > 0) {
            weixinmpOpenIdMapper.deleteByOpenId(openId);
            partnerMpOpenIdMapper.deleteByOpenId(openId, id);
            Customer customer = customerMapper.find(id);
            String key = "token:" + customer.getMpLoginToken();
            memCachedClient.delete(key);
        }
        return effect;
    }

    @Transactional(rollbackFor = Throwable.class)
    public int fwUnbindMobile(long id, String openId) {
//        fwOpenIdMapper.updateCustomerId(openId, null);
        int effect = customerMapper.updateFwOpenId(id, null, null, null);
        if (effect > 0) {
            alipayfwOpenIdMapper.deleteByOpenId(openId);
            partnerFwOpenIdMapper.deleteByOpenId(openId, id);
            Customer customer = customerMapper.find(id);
            String key = "token:" + customer.getFwLoginToken();
            memCachedClient.delete(key);
        }
        return effect;
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

    @Transactional(rollbackFor = Throwable.class)
    public ExtResult clearAgent(Long[] idList) {
        int count = 0;
        for (Long id : idList) {
            Customer customer = customerMapper.find(id);
            if (customer == null) {
                continue;
            }
            CustomerExchangeInfo customerExchangeInfo = customerExchangeInfoMapper.find(id);
            if (customerExchangeInfo != null) {
                continue;
            }
            int result = customerMapper.clearAgentId(id);
            if (result == 1) {
                count++;
            }
        }
        return ExtResult.successResult(String.format("成功清空%d个用户的运营商id", count));

    }

    @Transactional(rollbackFor=Throwable.class)
    public ExtResult updateTransferPeople(long oldCustomerId, Integer partnerId, String mobile){
        Customer newCustomer = customerMapper.findByPartnerIdAndMobile(partnerId, mobile);
        if (newCustomer == null) {
            return ExtResult.failResult("被转让骑手不存在！");
        }
        CustomerForegiftOrder newCustomerForegiftOrder = customerForegiftOrderMapper.findByCustomerIdAndStatus(newCustomer.getId(), CustomerForegiftOrder.Status.PAY_OK.getValue());
        if (newCustomerForegiftOrder != null) {
            return ExtResult.failResult("被转让骑手已交纳押金，不能进行转让！");
        }
        List<CustomerInstallmentRecord> recordList = customerInstallmentRecordMapper.findListByCustomerId(newCustomer.getId(), ConstEnum.Category.EXCHANGE.getValue());
        if (recordList.size() > 0) {
            return ExtResult.failResult("被转让骑手存在未完成分期记录，不能进行转让！");
        }
        List<CustomerMultiOrder> orderList = customerMultiOrderMapper.findListByCustomerId(newCustomer.getId(), CustomerMultiOrder.Type.HD.getValue());
        if (orderList.size() > 0) {
            return ExtResult.failResult("被转让骑手存在未完成的多通道订单，不能进行转让！");
        }

        Customer oldCustomer = customerMapper.find(oldCustomerId);
        //老用户支付成功押金订单存在
        CustomerForegiftOrder oldCustomerForegiftOrder = customerForegiftOrderMapper.findByCustomerIdAndStatus(oldCustomer.getId(), CustomerForegiftOrder.Status.PAY_OK.getValue());
        //老用户押金订单只能转让一次
        ForegiftPacketMoneyTransferRecord transferRecord = foregiftPacketMoneyTransferRecordMapper.findByCustomerId(oldCustomerId, oldCustomerForegiftOrder.getId());
        if (transferRecord != null) {
            return ExtResult.failResult("该骑手押金已转让，不能重复转让！");
        }
        //押金订单转让 支付成功
        customerForegiftOrderMapper.updateOrder(oldCustomerForegiftOrder.getId(), newCustomer.getId(), newCustomer.getMobile(), newCustomer.getFullname(), ConstEnum.PayType.TRANSFER_PAY.getValue(), new Date());
        //租金订单转让 未付款 未使用
        List<Integer> packetStatus = Arrays.asList(
                PacketPeriodOrder.Status.NOT_PAY.getValue(),
                PacketPeriodOrder.Status.NOT_USE.getValue());
        List<PacketPeriodOrder> periodOrderList = packetPeriodOrderMapper.findListByCustomerIdAndStatus(oldCustomer.getId(), packetStatus);
        for (PacketPeriodOrder order : periodOrderList) {
            packetPeriodOrderMapper.updateOrder(order.getId(), newCustomer.getId(), newCustomer.getMobile(), newCustomer.getFullname(), ConstEnum.PayType.TRANSFER_PAY.getValue(), new Date());
        }
        //电池关联换电信息 转让
        CustomerExchangeInfo customerExchangeInfo = customerExchangeInfoMapper.find(oldCustomer.getId());
        if (customerExchangeInfo != null && customerExchangeInfo.getForegiftOrderId() != null) {
            customerExchangeInfoMapper.updateCustomerInfo(oldCustomer.getId(), newCustomer.getId());
        }

        //电池关联电池信息 转让
        if (customerExchangeBatteryMapper.exists(oldCustomer.getId()) > 0) {
            List<CustomerExchangeBattery> exchangeBatteryList = customerExchangeBatteryMapper.findByCustomerId(oldCustomer.getId());
            for (CustomerExchangeBattery customerExchangeBattery : exchangeBatteryList) {
                customerExchangeBatteryMapper.updateCustomerInfo(customerExchangeBattery.getCustomerId(), newCustomer.getId());
                //换电订单 转让
                batteryOrderMapper.updateBatteryOrder(customerExchangeBattery.getBatteryOrderId(), newCustomer.getId(), newCustomer.getMobile(), newCustomer.getFullname(), ConstEnum.PayType.TRANSFER_PAY.getValue(), new Date());
            }
        }
        //转让记录
        ForegiftPacketMoneyTransferRecord record = new ForegiftPacketMoneyTransferRecord();

        Agent agent = agentMapper.find(oldCustomerForegiftOrder.getAgentId());
        record.setAgentId(agent.getId());
        record.setAgentName(agent.getAgentName());
        record.setForegiftOrderId(oldCustomerForegiftOrder.getId());
        record.setForegiftMoney(oldCustomerForegiftOrder.getMoney());
        StringBuilder sbPacketPeriodOrderId = new StringBuilder();
        for (PacketPeriodOrder order : periodOrderList) {
            if (sbPacketPeriodOrderId.length() > 0) {
                sbPacketPeriodOrderId.append(",");
            }
            sbPacketPeriodOrderId.append(order.getId());
        }
        record.setPacketPeriodOrderId(sbPacketPeriodOrderId.toString());
        StringBuilder sbBatteryOrderId = new StringBuilder();
        List<CustomerExchangeBattery> exchangeBatteryList = customerExchangeBatteryMapper.findByCustomerId(oldCustomer.getId());
        for (CustomerExchangeBattery customerExchangeBattery : exchangeBatteryList) {
            if (sbBatteryOrderId.length() > 0) {
                sbBatteryOrderId.append(",");
            }
            sbBatteryOrderId.append(customerExchangeBattery.getBatteryOrderId());
        }
        record.setBatteryOrderId(sbBatteryOrderId.toString());
        record.setCustomerId(oldCustomerId);
        record.setCustomerFullname(oldCustomer.getFullname());
        record.setCustomerMobile(oldCustomer.getMobile());
        record.setTransferCustomerId(newCustomer.getId());
        record.setTransferCustomerFullname(newCustomer.getFullname());
        record.setTransferCustomerMobile(newCustomer.getMobile());
        record.setCreateTime(new Date());
        int effect = foregiftPacketMoneyTransferRecordMapper.insert(record);
        if (effect == 0) {
            return ExtResult.failResult("转让失败！");
        }
        return ExtResult.successResult();
    }


    @Transactional(rollbackFor = Throwable.class)
    public ExtResult updateIsWhiteList(long id, int isWhiteList) {
        customerMapper.updateIsWhiteList(id, isWhiteList);
        return ExtResult.successResult();
    }
}
