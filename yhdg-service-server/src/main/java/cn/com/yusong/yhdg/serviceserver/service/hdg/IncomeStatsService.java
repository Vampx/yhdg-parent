package cn.com.yusong.yhdg.serviceserver.service.hdg;

import cn.com.yusong.yhdg.common.constant.ConstEnum;
import cn.com.yusong.yhdg.common.constant.Constant;
import cn.com.yusong.yhdg.common.domain.basic.*;
import cn.com.yusong.yhdg.common.domain.hdg.*;
import cn.com.yusong.yhdg.common.domain.zd.RentForegiftOrder;
import cn.com.yusong.yhdg.common.domain.zd.RentInsuranceOrder;
import cn.com.yusong.yhdg.common.domain.zd.RentPeriodOrder;
import cn.com.yusong.yhdg.common.domain.zd.RentPeriodOrderAllot;
import cn.com.yusong.yhdg.serviceserver.persistence.basic.*;
import cn.com.yusong.yhdg.serviceserver.persistence.hdg.*;
import cn.com.yusong.yhdg.serviceserver.persistence.zd.*;
import cn.com.yusong.yhdg.serviceserver.service.AbstractService;
import com.sun.javafx.binding.StringFormatter;
import javafx.scene.Parent;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.lang.time.DateUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.util.*;

@Service
public class IncomeStatsService extends AbstractService {
    static final Logger log = LogManager.getLogger(IncomeStatsService.class);
    @Autowired
    BatteryOrderMapper batteryOrderMapper;
    @Autowired
    RentOrderMapper rentOrderMapper;
    @Autowired
    PacketPeriodOrderMapper packetPeriodOrderMapper;
    @Autowired
    RentPeriodOrderMapper rentPeriodOrderMapper;
    @Autowired
    AgentDayStatsMapper agentDayStatsMapper;
    @Autowired
    PartnerDayStatsMapper partnerDayStatsMapper;
    @Autowired
    AgentMapper agentMapper;
    @Autowired
    PartnerMapper partnerMapper;
    @Autowired
    BatteryOrderRefundMapper batteryOrderRefundMapper;
    @Autowired
    AgentMonthStatsMapper agentMonthStatsMapper;
    @Autowired
    AgentTotalStatsMapper agentTotalStatsMapper;
    @Autowired
    PlatformDayStatsMapper platformDayStatsMapper;
    @Autowired
    PlatformMonthStatsMapper platformMonthStatsMapper;
    @Autowired
    CabinetDayStatsMapper cabinetDayStatsMapper;
    @Autowired
    CabinetMapper cabinetMapper;
    @Autowired
    ShopMapper shopMapper;
    @Autowired
    AgentCompanyMapper agentCompanyMapper;
    @Autowired
    BatteryMapper batteryMapper;
    @Autowired
    CustomerMapper customerMapper;
    @Autowired
    CabinetMonthStatsMapper cabinetMonthStatsMapper;
    @Autowired
    CabinetTotalStatsMapper cabinetTotalStatsMapper;
    @Autowired
    CustomerForegiftOrderMapper customerForegiftOrderMapper;
    @Autowired
    RentForegiftOrderMapper rentForegiftOrderMapper;
    @Autowired
    InsuranceOrderMapper insuranceOrderMapper;
    @Autowired
    RentInsuranceOrderMapper rentInsuranceOrderMapper;
    @Autowired
    CustomerDepositOrderMapper customerDepositOrderMapper;
    @Autowired
    FeedbackMapper feedbackMapper;
    @Autowired
    CustomerDayStatsMapper customerDayStatsMapper;
    @Autowired
    ShopDayStatsMapper shopDayStatsMapper;
    @Autowired
    AgentCompanyDayStatsMapper agentCompanyDayStatsMapper;
    @Autowired
    AgentCompanyCustomerMapper agentCompanyCustomerMapper;
    @Autowired
    ShopTotalStatsMapper shopTotalStatsMapper;
    @Autowired
    AgentCompanyTotalStatsMapper agentCompanyTotalStatsMapper;
    @Autowired
    CabinetConfigHistoryMapper cabinetConfigHistoryMapper;
    @Autowired
    IncomeRatioHistoryMapper incomeRatioHistoryMapper;
    @Autowired
    ShopIncomeRatioHistoryMapper shopIncomeRatioHistoryMapper;
    @Autowired
    AgentCompanyIncomeRatioHistoryMapper agentCompanyIncomeRatioHistoryMapper;
    @Autowired
    BalanceRecordMapper balanceRecordMapper;
    @Autowired
    BatteryOrderAllotMapper batteryOrderAllotMapper;
    @Autowired
    PacketPeriodOrderAllotMapper packetPeriodOrderAllotMapper;
    @Autowired
    RentPeriodOrderAllotMapper rentPeriodOrderAllotMapper;
    @Autowired
    BatteryTypeIncomeRatioMapper batteryTypeIncomeRatioMapper;
    @Autowired
    CabinetDayDegreeStatsMapper cabinetDayDegreeStatsMapper;
    @Autowired
    AgentForegiftRefundMapper agentForegiftRefundMapper;
    @Autowired
    RentForegiftRefundMapper rentForegiftRefundMapper;
    @Autowired
    DeductionTicketOrderMapper deductionTicketOrderMapper;
    @Autowired
    AgentInOutMoneyMapper agentInOutMoneyMapper;
    @Autowired
    LaxinPayOrderMapper laxinPayOrderMapper;
    @Autowired
    AgentMaterialDayStatsMapper agentMaterialDayStatsMapper;

    public Map<String, Cabinet> cabinetMap = null;
    public Map<String, Shop> shopMap = null;
    public Map<String, AgentCompany> agentCompanyMap = null;
    public Map<Integer, Agent> agentMap = null;
    public Map<Integer, Partner> partnerMap = null;
    public Map<String, Map<Integer, IncomeRatioHistory>> agentIncomeRatioHistoryMap = null;
    public Map<String, Map<Integer, IncomeRatioHistory>> agentCompanyIncomeRatioHistoryMap = null;

    @Transactional(rollbackFor = Throwable.class)
    public void stats(Date date, boolean balance) throws ParseException {
        cabinetMap = new HashMap<String, Cabinet>();
        shopMap = new HashMap<String, Shop>();
        agentCompanyMap = new HashMap<String, AgentCompany>();
        agentMap = new HashMap<Integer, Agent>();
        partnerMap = new HashMap<Integer, Partner>();
        agentIncomeRatioHistoryMap = new HashMap<String, Map<Integer, IncomeRatioHistory>>();
        agentCompanyIncomeRatioHistoryMap = new HashMap<String, Map<Integer, IncomeRatioHistory>>();


        log.debug("日收入统计开始...");
        //天格式日期2019-01-19
        String statsDate = DateFormatUtils.format(date.getTime(), Constant.DATE_FORMAT);
        //月格式日期2019-01
        String statsMonth = DateFormatUtils.format(date.getTime(), "yyyy-MM");
        //2019-01-19 00:00:00
        Date beginTime = DateUtils.truncate(date, Calendar.DAY_OF_MONTH);
        //2019-01-19 23:59:59
        Date endTime = DateUtils.addMilliseconds(DateUtils.addDays(beginTime, 1), -1);

        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        String suffix = String.format("%d%02d", calendar.get(Calendar.YEAR), calendar.get(Calendar.WEEK_OF_YEAR));

        //有效支付类型
        List<Integer> payTypeList = Arrays.asList(ConstEnum.PayType.BALANCE.getValue(),
                ConstEnum.PayType.ALIPAY.getValue(),
                ConstEnum.PayType.WEIXIN.getValue(),
                ConstEnum.PayType.WEIXIN_MP.getValue(),
                ConstEnum.PayType.ALIPAY_FW.getValue(),
                ConstEnum.PayType.INSTALLMENT.getValue(),
                ConstEnum.PayType.MULTI_CHANNEL.getValue());
        //查询所有终端押金
        List<CabinetDayStats> cabinetForegiftList= customerForegiftOrderMapper.findIncrementCabinetExchange(CustomerForegiftOrder.Status.PAY_OK.getValue(), beginTime, endTime);

        //查询所有终端押金退款
        List<CabinetDayStats> cabinetRefundForegiftList= customerForegiftOrderMapper.findRefundCabinetExchange(CustomerForegiftOrder.Status.PAY_OK.getValue(), beginTime, endTime);

        //查询所有终端保险
        List<CabinetDayStats> cabinetInsuranceList = insuranceOrderMapper.findCabinetIncrement(beginTime, endTime);

        //查询所有终端保险退款
        List<CabinetDayStats> cabinetRefundInsuranceList = insuranceOrderMapper.findCabinetRefund(beginTime, endTime);

        //查询所有门店押金
        List<ShopDayStats> shopForegiftList= customerForegiftOrderMapper.findIncrementShopExchange(CustomerForegiftOrder.Status.PAY_OK.getValue(), beginTime, endTime);

        //查询所有门店押金退款
        List<ShopDayStats> shopRefundForegiftList= customerForegiftOrderMapper.findRefundShopExchange(CustomerForegiftOrder.Status.PAY_OK.getValue(), beginTime, endTime);

        //查询所有运营公司押金
        List<AgentCompanyDayStats> agentCompanyForegiftList= customerForegiftOrderMapper.findIncrementAgentCompanyExchange(CustomerForegiftOrder.Status.PAY_OK.getValue(), beginTime, endTime);

        //查询所有运营公司押金退款
        List<AgentCompanyDayStats> agentCompanyRefundForegiftList= customerForegiftOrderMapper.findRefundAgentCompanyExchange(CustomerForegiftOrder.Status.PAY_OK.getValue(), beginTime, endTime);

        //查询所有门店保险
        //List<ShopDayStats> shopInsuranceList = insuranceOrderMapper.findShopIncrement(beginTime, endTime);

        //查询所有门店保险退款
        //List<ShopDayStats> shopRefundInsuranceList = insuranceOrderMapper.findShopRefund(beginTime, endTime);

        //查询所有终端活跃人数
        List<CabinetDayStats> cabinetActiveCustomerList = batteryOrderMapper.findActiveCustomerCount( beginTime, endTime);

        //查询所有终端当日电价
        List<CabinetDayStats> cabinetElectricList = cabinetDayDegreeStatsMapper.findIncrement(statsDate);

        //查询所有当日支付(已付款)押金
        List<Map> agentForegiftList= customerForegiftOrderMapper.findIncrementExchange(CustomerForegiftOrder.Status.PAY_OK.getValue(), beginTime, endTime);

        //查询所有当日退款(已付款)押金
        List<Map> agentForegiftRefundList= customerForegiftOrderMapper.findRefund(CustomerForegiftOrder.Status.REFUND_SUCCESS.getValue(), beginTime, endTime);

        //查询所有当日支付(已付款)保险
        List<Map> agentInsuranceList = insuranceOrderMapper.findIncrement(InsuranceOrder.Status.PAID.getValue(), beginTime, endTime);

        //查询所有当日退款(已付款)保险
        List<Map> agentInsuranceRefundList = insuranceOrderMapper.findRefund(InsuranceOrder.Status.REFUND_SUCCESS.getValue(), beginTime, endTime);

        //查询所有当日支付(已付款)换电订单
        List<BatteryOrder> incrementExchangeList = batteryOrderMapper.findIncrementExchange(BatteryOrder.OrderStatus.PAY.getValue(), beginTime, endTime, payTypeList);

        //查询当日新增包时段订单数
        List<Map> incrementPacketPeriodCount = batteryOrderMapper.findIncrementPacketPeriodCount(BatteryOrder.OrderStatus.PAY.getValue(), beginTime, endTime, ConstEnum.PayType.PACKET.getValue());

        //查询所有当日支付(已付款)包月订单
        List<PacketPeriodOrder> incrementPacketPeriodList = packetPeriodOrderMapper.findIncrementExchange(beginTime, endTime, payTypeList);

        //查询当日退款包月订单
        List<PacketPeriodOrder> packetPeriodRefundList = packetPeriodOrderMapper.findRefund(PacketPeriodOrder.Status.REFUND.getValue(), beginTime, endTime, payTypeList);

        //查询运营商押金剩余金额
        List<Map> agentForegiftRemainList= agentForegiftRefundMapper.findByAgent(beginTime, endTime);

        //查询运营商抵扣券
        List<Map> deductionTicketOrderList = deductionTicketOrderMapper.findByAgent(ConstEnum.Category.EXCHANGE.getValue(), beginTime, endTime);

        //查询运营商拉新
        List<Map> laxinPayOrderList = laxinPayOrderMapper.findByAgent( beginTime, endTime);


        //查询运营商当日活跃客户数
        List<AgentDayStats> agentActiveCustomerList = batteryOrderMapper.findAgentActiveCustomerCount(BatteryOrder.OrderStatus.PAY.getValue(), beginTime, endTime);

        //租电统计相关
        //查询所有门店押金
        List<ShopDayStats> shopRentForegiftList= rentForegiftOrderMapper.findIncrementShopExchange(beginTime, endTime);

        //查询所有门店押金退款
        List<ShopDayStats> shopRentRefundForegiftList= rentForegiftOrderMapper.findRefundShopExchange(beginTime, endTime);

        //查询所有运营公司押金
        List<AgentCompanyDayStats> agentCompanyRentForegiftList= rentForegiftOrderMapper.findIncrementAgentCompanyExchange(beginTime, endTime);

        //查询所有运营公司押金退款
        List<AgentCompanyDayStats> agentCompanyRentRefundForegiftList= rentForegiftOrderMapper.findRefundAgentCompanyExchange(beginTime, endTime);

        //查询所有门店保险
        //List<ShopDayStats> shopRentInsuranceList = rentInsuranceOrderMapper.findShopIncrement(beginTime, endTime);

        //查询所有门店保险退款
        //List<ShopDayStats> shopRentRefundInsuranceList = rentInsuranceOrderMapper.findShopRefund(beginTime, endTime);


        //查询所有当日支付(已付款)押金
        List<Map> agentRentForegiftList= rentForegiftOrderMapper.findIncrementExchange(RentForegiftOrder.Status.PAY_OK.getValue(), beginTime, endTime);

        //查询所有当日退款(已付款)押金
        List<Map> agentRentForegiftRefundList= rentForegiftOrderMapper.findRefund(RentForegiftOrder.Status.REFUND_SUCCESS.getValue(), beginTime, endTime);

        //查询所有当日支付(已付款)包月订单
        List<RentPeriodOrder> incrementRentPeriodList = rentPeriodOrderMapper.findIncrementExchange(beginTime, endTime, payTypeList);

        //查询当日退款包月订单
        List<RentPeriodOrder> rentPeriodRefundList = rentPeriodOrderMapper.findRefund(RentPeriodOrder.Status.REFUND.getValue(), beginTime, endTime, payTypeList);

        //查询所有当日支付(已付款)保险
        List<Map> agentRentInsuranceList = rentInsuranceOrderMapper.findIncrement(RentInsuranceOrder.Status.PAID.getValue(), beginTime, endTime);

        //查询所有当日退款(已付款)保险
        List<Map> agentRentInsuranceRefundList = rentInsuranceOrderMapper.findRefund(RentInsuranceOrder.Status.REFUND_SUCCESS.getValue(), beginTime, endTime);

        //查询运营商押金剩余金额
        List<Map> rentForegiftRemainList= rentForegiftRefundMapper.findByAgent(beginTime, endTime);

        //查询运营商抵扣券
        List<Map> rentTicketOrderList = deductionTicketOrderMapper.findByAgent(ConstEnum.Category.RENT.getValue(), beginTime, endTime);

        //查询运营商当日设备数
        List<AgentDayStats> agentCabinetList = cabinetMapper.findAgentIncrement(beginTime, endTime);

        //查询运营商换电电池数
        List<AgentDayStats> agentExchangeBatteryList = batteryMapper.findAgentIncrement(ConstEnum.Category.EXCHANGE.getValue());

        //查询运营商租电电池数
        List<AgentDayStats> agentRentBatteryList = batteryMapper.findAgentIncrement(ConstEnum.Category.RENT.getValue());

        //查询所有客户统计
        List<CustomerDayStats> customerDayStats = batteryOrderMapper.findIncrementByCustomer(BatteryOrder.OrderStatus.PAY.getValue(), beginTime, endTime);

        //终端当日统计map
        Map<String, CabinetDayStats> cabinetDayStatsMap = new HashMap<String, CabinetDayStats>();

        //门店当日换电统计map
        Map<String, ShopDayStats> shopExchangeDayStatsMap = new HashMap<String, ShopDayStats>();

        //运营公司当日换电统计map
        Map<String, AgentCompanyDayStats> agentCompanyExchangeDayStatsMap = new HashMap<String, AgentCompanyDayStats>();

        //运营商当日换电统计map
        Map<Integer, AgentDayStats> agentExchangeDayStatsMap = new HashMap<Integer, AgentDayStats>();

        //商户当日换电统计map
        Map<Integer, PartnerDayStats> partnerExchangeDayStatsMap = new HashMap<Integer, PartnerDayStats>();

        //门店当日租电统计map
        Map<String, ShopDayStats> shopRentDayStatsMap = new HashMap<String, ShopDayStats>();

        //运营公司当日租电统计map
        Map<String, AgentCompanyDayStats> agentCompanyRentDayStatsMap = new HashMap<String, AgentCompanyDayStats>();

        //运营商当日租电统计map
        Map<Integer, AgentDayStats> agentRentDayStatsMap = new HashMap<Integer, AgentDayStats>();

        //商户当日租电统计map
        Map<Integer, PartnerDayStats> partnerRentDayStatsMap = new HashMap<Integer, PartnerDayStats>();

        //订单统计表处理
        creatNoExistsTables(suffix);

        //（按次）
        for (BatteryOrder order : incrementExchangeList) {
            //保存对应终端
            getCabinet(order.getPutCabinetId());

            //查询分成系数
            Map<Integer, IncomeRatioHistory> map = incomeRatioHistoryPlatformMap(order.getAgentId(), order.getPutCabinetId(), statsDate);

//            if (StringUtils.isNotEmpty(order.getTakeAgentCompanyId())) {
//                //保存对应运营公司
//                AgentCompany agentCompany = getAgentCompany(order.getTakeAgentCompanyId());
//                Map<Integer, IncomeRatioHistory> companyMap = agentCompanyIncomeRatioHistoryPlatformMap(order.getAgentId(), order.getTakeAgentCompanyId(), statsDate);
//                List<Integer> orgTypeList = Arrays.asList(
//                        IncomeRatioHistory.OrgType.PLATFORM.getValue(),
//                        IncomeRatioHistory.OrgType.PROVINCE_AGENT.getValue(),
//                        IncomeRatioHistory.OrgType.CITY_AGENT.getValue(),
//                        IncomeRatioHistory.OrgType.SHOP.getValue(),
//                        IncomeRatioHistory.OrgType.AGENT_COMPANY.getValue(),
//                        IncomeRatioHistory.OrgType.AGENT.getValue()
//                );
//
//                IncomeRatioHistory companyRatioHistory = companyMap.get(IncomeRatioHistory.OrgType.AGENT_COMPANY.getValue());
//                Integer ratioBaseMoney = companyRatioHistory.getRatioBaseMoney();
//
//                Map<Integer, IncomeRatioHistory> agentCompanyMap = new LinkedHashMap<Integer, IncomeRatioHistory>();
//                if (agentCompany.getKeepShopRatio() == null || agentCompany.getKeepShopRatio() == ConstEnum.Flag.TRUE.getValue()) {
//                    //正常情况给门店分成
//                    for (Integer integer : orgTypeList) {
//                        agentCompanyMap.put(integer, map.get(integer));
//                        if (ratioBaseMoney == null || (ratioBaseMoney <= order.getMoney())) {
//                            //订单金额大于等于下限金额、下限金额为空，给运营公司分成
//                            if (integer == IncomeRatioHistory.OrgType.AGENT_COMPANY.getValue()) {
//                                for (Integer integer1 : companyMap.keySet()) {
//                                    agentCompanyMap.put(integer, companyMap.get(integer1));
//                                }
//                            }
//                        } else {
//                            IncomeRatioHistory incomeRatioHistory = companyMap.get(integer);
//                            if (incomeRatioHistory != null) {
//                                IncomeRatioHistory newIncomeRatioHistory = new IncomeRatioHistory();
//                                BeanUtils.copyProperties(incomeRatioHistory, newIncomeRatioHistory);
//                                newIncomeRatioHistory.setRatio(0);
//                                newIncomeRatioHistory.setAgentCompanyFixedMoney(0);
//                                agentCompanyMap.put(integer, newIncomeRatioHistory);
//                            }
//                        }
//                    }
//                } else if (agentCompany.getKeepShopRatio() == ConstEnum.Flag.FALSE.getValue()) {
//                    //运营公司骑手不给门店分成
//                    for (Integer integer : orgTypeList) {
//                        if (integer != IncomeRatioHistory.OrgType.SHOP.getValue()) {
//                            agentCompanyMap.put(integer, map.get(integer));
//                            if (ratioBaseMoney == null || (ratioBaseMoney <= order.getMoney())) {
//                                //订单金额大于等于下限金额、下限金额为空，给运营公司分成
//                                if (integer == IncomeRatioHistory.OrgType.AGENT_COMPANY.getValue()) {
//                                    for (Integer integer1 : companyMap.keySet()) {
//                                        agentCompanyMap.put(integer, companyMap.get(integer1));
//                                    }
//                                }
//                            } else {
//                                IncomeRatioHistory incomeRatioHistory = companyMap.get(integer);
//                                if (incomeRatioHistory != null) {
//                                    IncomeRatioHistory newIncomeRatioHistory = new IncomeRatioHistory();
//                                    BeanUtils.copyProperties(incomeRatioHistory, newIncomeRatioHistory);
//                                    newIncomeRatioHistory.setRatio(0);
//                                    newIncomeRatioHistory.setAgentCompanyFixedMoney(0);
//                                    agentCompanyMap.put(integer, newIncomeRatioHistory);
//                                }
//                            }
//                        } else {
//                            IncomeRatioHistory incomeRatioHistory = map.get(integer);
//                            if (incomeRatioHistory != null) {
//                                IncomeRatioHistory newIncomeRatioHistory = new IncomeRatioHistory();
//                                BeanUtils.copyProperties(incomeRatioHistory, newIncomeRatioHistory);
//                                newIncomeRatioHistory.setRatio(0);
//                                newIncomeRatioHistory.setShopFixedMoney(0);
//                                agentCompanyMap.put(integer, newIncomeRatioHistory);
//                            }
//                        }
//                    }
//                }
//
//                map = agentCompanyMap;
//            }

            //查询订单是否已经分配金额,已分配,不再作处理
            List<BatteryOrderAllot> allotList = batteryOrderAllotMapper.findByOrder(order.getAgentId(), BatteryOrderAllot.ServiceType.INCOME.getValue(), order.getId(), suffix, statsDate);
            if(allotList.size() > 0){
                continue;
            }
            //分成处理
            double orderMoney = order.getMoney() * 1d;//订单金额
            double totalAllot = 0d;//已分摊金额
            BatteryOrderAllot allot = new BatteryOrderAllot();
            if(map != null){
                for(Integer orgType : map.keySet()) {
                    //如果省代或市代没有分成金额了，就不再进行分摊
                    if (orgType != IncomeRatioHistory.OrgType.AGENT.getValue() && (orderMoney - totalAllot) <= 0) {
                        continue;
                    }
                    IncomeRatioHistory ratioHistory = map.get(orgType);
                    if (ratioHistory != null) {
                        double money = orderMoney * ratioHistory.getRatio() * 1d / 100.0;//分配给代理的金额
                        if((orderMoney - totalAllot) < money){
                            money = orderMoney - totalAllot;
                        }
                        //保存分配金额
                        allot.setPartnerId(order.getPartnerId());
                        allot.setAgentId(order.getAgentId());
                        allot.setOrderId(order.getId());
                        allot.setCustomerName(order.getCustomerFullname());
                        allot.setCustomerMobile(order.getCustomerMobile());
                        allot.setCabinetId(order.getPutCabinetId());
                        allot.setCabinetName(order.getPutCabinetName());
                        allot.setOrderMoney(order.getMoney());
                        allot.setServiceType(BatteryOrderAllot.ServiceType.INCOME.getValue());
                        allot.setRatio(ratioHistory.getRatio());
                        allot.setOrgType(orgType);
                        allot.setOrgId(ratioHistory.getOrgId());
                        allot.setShopId(ratioHistory.getShopId());
                        allot.setAgentCompanyId(ratioHistory.getAgentCompanyId());
                        allot.setOrgName(ratioHistory.getOrgName());
                        allot.setMoney(money);
                        allot.setStatsDate(statsDate);
                        allot.setPayTime(order.getPayTime());
                        allot.setCreateTime(new Date());
                        allot.setSuffix(BatteryOrderAllot.getSuffixByString(statsDate));
                        batteryOrderAllotMapper.insert(allot);
                        totalAllot += money;
                    }
                }
            }
        }

        //（包月）
        for (PacketPeriodOrder order : incrementPacketPeriodList) {
            //查询分成系数
            Map<Integer, IncomeRatioHistory> map = null;
            if(StringUtils.isNotEmpty(order.getCabinetId())){
                //保存对应终端
                getCabinet(order.getCabinetId());
                map = incomeRatioHistoryPlatformMap(order.getAgentId(), order.getCabinetId(), statsDate);
            }else if(StringUtils.isNotEmpty(order.getShopId())){
                //保存对应门店
                getShop(order.getShopId());
                map = shopIncomeRatioHistoryPlatformMap(order.getAgentId(), order.getShopId(), statsDate);
            }

            if (StringUtils.isNotEmpty(order.getAgentCompanyId())) {
                //保存对应运营公司
                AgentCompany agentCompany = getAgentCompany(order.getAgentCompanyId());
                Map<Integer, IncomeRatioHistory> companyMap = agentCompanyIncomeRatioHistoryPlatformMap(order.getAgentId(), order.getAgentCompanyId(), statsDate);
                List<Integer> orgTypeList = Arrays.asList(
                        IncomeRatioHistory.OrgType.PLATFORM.getValue(),
                        IncomeRatioHistory.OrgType.PROVINCE_AGENT.getValue(),
                        IncomeRatioHistory.OrgType.CITY_AGENT.getValue(),
                        IncomeRatioHistory.OrgType.SHOP.getValue(),
                        IncomeRatioHistory.OrgType.AGENT_COMPANY.getValue(),
                        IncomeRatioHistory.OrgType.AGENT.getValue()
                );

                IncomeRatioHistory companyRatioHistory = companyMap.get(IncomeRatioHistory.OrgType.AGENT_COMPANY.getValue());
                Integer ratioBaseMoney = companyRatioHistory.getRatioBaseMoney();

                Map<Integer, IncomeRatioHistory> agentCompanyMap = new LinkedHashMap<Integer, IncomeRatioHistory>();
                if (agentCompany.getKeepShopRatio() == null || agentCompany.getKeepShopRatio() == ConstEnum.Flag.TRUE.getValue()) {
                    //正常情况给门店分成
                    for (Integer integer : orgTypeList) {
                        agentCompanyMap.put(integer, map.get(integer));
                        if (ratioBaseMoney == null || (ratioBaseMoney <= order.getMoney())) {
                            //订单金额大于等于下限金额、下限金额为空，给运营公司分成
                            if (integer == IncomeRatioHistory.OrgType.AGENT_COMPANY.getValue()) {
                                for (Integer integer1 : companyMap.keySet()) {
                                    agentCompanyMap.put(integer, companyMap.get(integer1));
                                }
                            }
                        } else {
                            IncomeRatioHistory incomeRatioHistory = companyMap.get(integer);
                            if (incomeRatioHistory != null) {
                                IncomeRatioHistory newIncomeRatioHistory = new IncomeRatioHistory();
                                BeanUtils.copyProperties(incomeRatioHistory, newIncomeRatioHistory);
                                newIncomeRatioHistory.setRatio(0);
                                newIncomeRatioHistory.setAgentCompanyFixedMoney(0);
                                agentCompanyMap.put(integer, newIncomeRatioHistory);
                            }
                        }
                    }
                } else if (agentCompany.getKeepShopRatio() == ConstEnum.Flag.FALSE.getValue()) {
                    //运营公司骑手不给门店分成
                    for (Integer integer : orgTypeList) {
                        if (integer != IncomeRatioHistory.OrgType.SHOP.getValue()) {
                            agentCompanyMap.put(integer, map.get(integer));
                            if (ratioBaseMoney == null || (ratioBaseMoney <= order.getMoney())) {
                                //订单金额大于等于下限金额、下限金额为空，给运营公司分成
                                if (integer == IncomeRatioHistory.OrgType.AGENT_COMPANY.getValue()) {
                                    for (Integer integer1 : companyMap.keySet()) {
                                        agentCompanyMap.put(integer, companyMap.get(integer1));
                                    }
                                }
                            } else {
                                IncomeRatioHistory incomeRatioHistory = companyMap.get(integer);
                                if (incomeRatioHistory != null) {
                                    IncomeRatioHistory newIncomeRatioHistory = new IncomeRatioHistory();
                                    BeanUtils.copyProperties(incomeRatioHistory, newIncomeRatioHistory);
                                    newIncomeRatioHistory.setRatio(0);
                                    newIncomeRatioHistory.setAgentCompanyFixedMoney(0);
                                    agentCompanyMap.put(integer, newIncomeRatioHistory);
                                }
                            }
                        } else {
                            IncomeRatioHistory incomeRatioHistory = map.get(integer);
                            if (incomeRatioHistory != null) {
                                IncomeRatioHistory newIncomeRatioHistory = new IncomeRatioHistory();
                                BeanUtils.copyProperties(incomeRatioHistory, newIncomeRatioHistory);
                                newIncomeRatioHistory.setRatio(0);
                                newIncomeRatioHistory.setShopFixedMoney(0);
                                agentCompanyMap.put(integer, newIncomeRatioHistory);
                            }
                        }
                    }
                }

                map = agentCompanyMap;
            }

            //查询订单是否已经分配金额,已分配,不再作处理
            List<PacketPeriodOrderAllot> allotList = packetPeriodOrderAllotMapper.findByOrder(order.getAgentId(), PacketPeriodOrderAllot.ServiceType.INCOME.getValue(), order.getId(), suffix, statsDate);
            if(allotList.size() > 0){
                continue;
            }
            //分成处理
            double orderMoney = order.getMoney() * 1d;//订单金额
            double totalAllot = 0d;//已分成金额
            if(map != null){
                for(Integer orgType : map.keySet()){
                    if(orgType != IncomeRatioHistory.OrgType.AGENT.getValue() &&  (orderMoney - totalAllot) <= 0){
                        continue;
                    }
                    IncomeRatioHistory ratioHistory = map.get(orgType);
                    if(ratioHistory != null){
                        //如果是平台分成，如果存在每笔提成，优先扣除
                        double platformDeductMoney = 0;
                        if(ratioHistory.getPlatformDeductMoney() != null && ratioHistory.getPlatformDeductMoney() > 0){
                            platformDeductMoney = ratioHistory.getPlatformDeductMoney();
                            if((orderMoney - totalAllot) < platformDeductMoney){
                                platformDeductMoney = orderMoney - totalAllot;
                            }
                            totalAllot += platformDeductMoney;
                        }

                        double money = orderMoney * ratioHistory.getRatio() * 1d /100.0;//分配给代理的金额
                        if((orderMoney - totalAllot) < money){
                            money = orderMoney - totalAllot;
                        }

                        totalAllot += money;

                        //门店固定分成
                        double shopFixedMoney = 0;
                        if(orgType == IncomeRatioHistory.OrgType.SHOP.getValue()){
                            if(ratioHistory.getShopFixedMoney() != null && ratioHistory.getShopFixedMoney() > 0){
                                shopFixedMoney = (int)Math.round(ratioHistory.getShopFixedMoney() * 1d / 30 * dealDays(order.getDayCount()));
                                if((orderMoney - totalAllot) < shopFixedMoney){
                                    shopFixedMoney = orderMoney - totalAllot;
                                }
                                totalAllot += shopFixedMoney;
                            }
                        }

                        //运营公司固定分成
                        double agentCompanyFixedMoney = 0;
                        if (orgType == IncomeRatioHistory.OrgType.AGENT_COMPANY.getValue()) {
                            if (ratioHistory.getAgentCompanyFixedMoney() != null && ratioHistory.getAgentCompanyFixedMoney() > 0) {
                                agentCompanyFixedMoney = (int) Math.round(ratioHistory.getAgentCompanyFixedMoney() * 1d / 30 * dealDays(order.getDayCount()));
                                if ((orderMoney - totalAllot) < agentCompanyFixedMoney) {
                                    agentCompanyFixedMoney = orderMoney - totalAllot;
                                }
                                totalAllot += agentCompanyFixedMoney;
                            }
                        }


                        //保存分配金额
                        PacketPeriodOrderAllot allot = new PacketPeriodOrderAllot();
                        allot.setPartnerId(order.getPartnerId());
                        allot.setAgentId(order.getAgentId());
                        allot.setCabinetId(order.getCabinetId());
                        allot.setOrderId(order.getId());
                        allot.setCustomerName(order.getCustomerFullname());
                        allot.setCustomerMobile(order.getCustomerMobile());
                        allot.setOrderMoney(order.getMoney());
                        allot.setServiceType(PacketPeriodOrderAllot.ServiceType.INCOME.getValue());
                        allot.setRatio(ratioHistory.getRatio());
                        allot.setOrgType(orgType);
                        allot.setOrgId(ratioHistory.getOrgId());
                        allot.setShopId(ratioHistory.getShopId());
                        allot.setAgentCompanyId(ratioHistory.getAgentCompanyId());
                        allot.setOrgName(ratioHistory.getOrgName());
                        allot.setPlatformDeductMoney(platformDeductMoney);
                        allot.setShopFixedMoney(shopFixedMoney);
                        allot.setAgentCompanyFixedMoney(agentCompanyFixedMoney);
                        allot.setMoney(platformDeductMoney + shopFixedMoney + agentCompanyFixedMoney + money);
                        allot.setDayCount(order.getDayCount());
                        allot.setBeginTime(order.getBeginTime());
                        allot.setEndTime(order.getEndTime());
                        allot.setPayTime(order.getPayTime());
                        allot.setStatsDate(statsDate);
                        allot.setCreateTime(new Date());
                        allot.setSuffix(BatteryOrderAllot.getSuffixByString(statsDate));
                        packetPeriodOrderAllotMapper.insert(allot);
                    }
                }
            }
        }

        //（包月退款）
        for (PacketPeriodOrder order : packetPeriodRefundList) {
            //查询订单是否已经分配金额,已分配,不再作处理
            List<PacketPeriodOrderAllot> allotList = packetPeriodOrderAllotMapper.findByOrder(order.getAgentId(), PacketPeriodOrderAllot.ServiceType.REFUND.getValue(), order.getId(), suffix, statsDate);
            if(allotList.size() > 0){
                continue;
            }
            String payStatsDate = DateFormatUtils.format(order.getPayTime(), Constant.DATE_FORMAT);
            calendar.setTime(order.getPayTime());
            String paySuffix = String.format("%d%02d", calendar.get(Calendar.YEAR), calendar.get(Calendar.WEEK_OF_YEAR));
            //如果退款金额与订单金额相同，查找之前已经分配过的订单表，返还金额
            boolean refundFlag = false;
            if(order.getMoney().intValue() == order.getRefundMoney().intValue()){
                List<PacketPeriodOrderAllot> payAllotList = packetPeriodOrderAllotMapper.findByOrder(order.getAgentId(), BatteryOrderAllot.ServiceType.INCOME.getValue(), order.getId(), paySuffix, payStatsDate);
                //查找之前已经分配过的订单表，返还金额
                if(payAllotList != null && payAllotList.size() > 0){
                    for(PacketPeriodOrderAllot packetPeriodOrderAllot : payAllotList){
                        packetPeriodOrderAllot.setServiceType(BatteryOrderAllot.ServiceType.REFUND.getValue());
                        packetPeriodOrderAllot.setStatsDate(statsDate);
                        packetPeriodOrderAllot.setCreateTime(new Date());
                        packetPeriodOrderAllot.setSuffix(BatteryOrderAllot.getSuffixByString(statsDate));
                        packetPeriodOrderAllotMapper.insert(packetPeriodOrderAllot);
                    }
                    refundFlag = true;
                }
            }
            //取购买订单时的分成比例进行计算
            if(!refundFlag){
                //查询分成系数
                Map<Integer, IncomeRatioHistory> map = null;
                if(StringUtils.isNotEmpty(order.getCabinetId())){
                    //保存对应终端
                    getCabinet(order.getCabinetId());
                    map = incomeRatioHistoryPlatformMap(order.getAgentId(), order.getCabinetId(), statsDate);
                }else if(StringUtils.isNotEmpty(order.getShopId())){
                    //保存对应门店
                    getShop(order.getShopId());
                    map = shopIncomeRatioHistoryPlatformMap(order.getAgentId(), order.getShopId(), statsDate);
                }

                if (StringUtils.isNotEmpty(order.getAgentCompanyId())) {
                    //保存对应运营公司
                    AgentCompany agentCompany = getAgentCompany(order.getAgentCompanyId());
                    Map<Integer, IncomeRatioHistory> companyMap = agentCompanyIncomeRatioHistoryPlatformMap(order.getAgentId(), order.getAgentCompanyId(), statsDate);
                    List<Integer> orgTypeList = Arrays.asList(
                            IncomeRatioHistory.OrgType.PLATFORM.getValue(),
                            IncomeRatioHistory.OrgType.PROVINCE_AGENT.getValue(),
                            IncomeRatioHistory.OrgType.CITY_AGENT.getValue(),
                            IncomeRatioHistory.OrgType.SHOP.getValue(),
                            IncomeRatioHistory.OrgType.AGENT_COMPANY.getValue(),
                            IncomeRatioHistory.OrgType.AGENT.getValue()
                    );

                    IncomeRatioHistory companyRatioHistory = companyMap.get(IncomeRatioHistory.OrgType.AGENT_COMPANY.getValue());
                    Integer ratioBaseMoney = companyRatioHistory.getRatioBaseMoney();

                    Map<Integer, IncomeRatioHistory> agentCompanyMap = new LinkedHashMap<Integer, IncomeRatioHistory>();
                    if (agentCompany.getKeepShopRatio() == null || agentCompany.getKeepShopRatio() == ConstEnum.Flag.TRUE.getValue()) {
                        //正常情况给门店分成
                        for (Integer integer : orgTypeList) {
                            agentCompanyMap.put(integer, map.get(integer));
                            if (ratioBaseMoney == null || (ratioBaseMoney <= order.getMoney())) {
                                //订单金额大于等于下限金额、下限金额为空，给运营公司分成
                                if (integer == IncomeRatioHistory.OrgType.AGENT_COMPANY.getValue()) {
                                    for (Integer integer1 : companyMap.keySet()) {
                                        agentCompanyMap.put(integer, companyMap.get(integer1));
                                    }
                                }
                            } else {
                                IncomeRatioHistory incomeRatioHistory = companyMap.get(integer);
                                if (incomeRatioHistory != null) {
                                    IncomeRatioHistory newIncomeRatioHistory = new IncomeRatioHistory();
                                    BeanUtils.copyProperties(incomeRatioHistory, newIncomeRatioHistory);
                                    newIncomeRatioHistory.setRatio(0);
                                    newIncomeRatioHistory.setAgentCompanyFixedMoney(0);
                                    agentCompanyMap.put(integer, newIncomeRatioHistory);
                                }
                            }
                        }
                    } else if (agentCompany.getKeepShopRatio() == ConstEnum.Flag.FALSE.getValue()) {
                        //运营公司骑手不给门店分成
                        for (Integer integer : orgTypeList) {
                            if (integer != IncomeRatioHistory.OrgType.SHOP.getValue()) {
                                agentCompanyMap.put(integer, map.get(integer));
                                if (ratioBaseMoney == null || (ratioBaseMoney <= order.getMoney())) {
                                    //订单金额大于等于下限金额、下限金额为空，给运营公司分成
                                    if (integer == IncomeRatioHistory.OrgType.AGENT_COMPANY.getValue()) {
                                        for (Integer integer1 : companyMap.keySet()) {
                                            agentCompanyMap.put(integer, companyMap.get(integer1));
                                        }
                                    }
                                } else {
                                    IncomeRatioHistory incomeRatioHistory = companyMap.get(integer);
                                    if (incomeRatioHistory != null) {
                                        IncomeRatioHistory newIncomeRatioHistory = new IncomeRatioHistory();
                                        BeanUtils.copyProperties(incomeRatioHistory, newIncomeRatioHistory);
                                        newIncomeRatioHistory.setRatio(0);
                                        newIncomeRatioHistory.setAgentCompanyFixedMoney(0);
                                        agentCompanyMap.put(integer, newIncomeRatioHistory);
                                    }
                                }
                            } else {
                                IncomeRatioHistory incomeRatioHistory = map.get(integer);
                                if (incomeRatioHistory != null) {
                                    IncomeRatioHistory newIncomeRatioHistory = new IncomeRatioHistory();
                                    BeanUtils.copyProperties(incomeRatioHistory, newIncomeRatioHistory);
                                    newIncomeRatioHistory.setRatio(0);
                                    newIncomeRatioHistory.setShopFixedMoney(0);
                                    agentCompanyMap.put(integer, newIncomeRatioHistory);
                                }
                            }
                        }
                    }

                    map = agentCompanyMap;
                }

                double orderMoney = order.getRefundMoney() * 1d;//订单金额
                double totalAllot = 0d;//已分成金额
                if(map != null){
                    for(Integer orgType : map.keySet()){
                        if(orgType != IncomeRatioHistory.OrgType.AGENT.getValue() &&  (orderMoney - totalAllot) <= 0){
                            break;
                        }
                        IncomeRatioHistory ratioHistory = map.get(orgType);
                        if(ratioHistory != null){
                            //如果是平台分成，如果存在每笔提成，优先扣除
                            double platformDeductMoney = 0;
                            if(ratioHistory.getPlatformDeductMoney() != null && ratioHistory.getPlatformDeductMoney() > 0){
                                platformDeductMoney = ratioHistory.getPlatformDeductMoney();
                                if((orderMoney - totalAllot) < platformDeductMoney){
                                    platformDeductMoney = orderMoney - totalAllot;
                                }
                                totalAllot += platformDeductMoney;
                            }

                            double money = orderMoney * ratioHistory.getRatio() * 1d /100.0;//分配给代理的金额
                            if((orderMoney - totalAllot) < money){
                                money = orderMoney - totalAllot;
                            }

                            totalAllot += money;

                            //门店固定分成
                            double shopFixedMoney = 0;
                            if(orgType == IncomeRatioHistory.OrgType.SHOP.getValue()){
                                if(ratioHistory.getShopFixedMoney() != null && ratioHistory.getShopFixedMoney() > 0){
                                    shopFixedMoney = (int)Math.round(ratioHistory.getShopFixedMoney() * 1d / 30 * dealDays(order.getDayCount()));
                                    if((orderMoney - totalAllot) < shopFixedMoney){
                                        shopFixedMoney = orderMoney - totalAllot;
                                    }
                                    totalAllot += shopFixedMoney;
                                }
                            }

                            //运营公司固定分成
                            double agentCompanyFixedMoney = 0;
                            if (orgType == IncomeRatioHistory.OrgType.AGENT_COMPANY.getValue()) {
                                if (ratioHistory.getAgentCompanyFixedMoney() != null && ratioHistory.getAgentCompanyFixedMoney() > 0) {
                                    agentCompanyFixedMoney = (int) Math.round(ratioHistory.getAgentCompanyFixedMoney() * 1d / 30 * dealDays(order.getDayCount()));
                                    if ((orderMoney - totalAllot) < agentCompanyFixedMoney) {
                                        agentCompanyFixedMoney = orderMoney - totalAllot;
                                    }
                                    totalAllot += agentCompanyFixedMoney;
                                }
                            }

                            //保存分配金额
                            PacketPeriodOrderAllot allot = new PacketPeriodOrderAllot();
                            allot.setPartnerId(order.getPartnerId());
                            allot.setAgentId(order.getAgentId());
                            allot.setCabinetId(order.getCabinetId());
                            allot.setOrderId(order.getId());
                            allot.setCustomerName(order.getCustomerFullname());
                            allot.setCustomerMobile(order.getCustomerMobile());
                            allot.setOrderMoney(order.getRefundMoney());
                            allot.setServiceType(PacketPeriodOrderAllot.ServiceType.REFUND.getValue());
                            allot.setRatio(ratioHistory.getRatio());
                            allot.setOrgType(orgType);
                            allot.setOrgId(ratioHistory.getOrgId());
                            allot.setShopId(ratioHistory.getShopId());
                            allot.setAgentCompanyId(ratioHistory.getAgentCompanyId());
                            allot.setOrgName(ratioHistory.getOrgName());
                            allot.setPlatformDeductMoney(platformDeductMoney);
                            allot.setShopFixedMoney(shopFixedMoney);
                            allot.setAgentCompanyFixedMoney(agentCompanyFixedMoney);
                            allot.setMoney(platformDeductMoney + shopFixedMoney + agentCompanyFixedMoney + money);
                            allot.setDayCount(order.getDayCount());
                            allot.setBeginTime(order.getBeginTime());
                            allot.setEndTime(order.getEndTime());
                            allot.setPayTime(order.getPayTime());
                            allot.setStatsDate(statsDate);
                            allot.setCreateTime(new Date());
                            allot.setSuffix(BatteryOrderAllot.getSuffixByString(statsDate));
                            packetPeriodOrderAllotMapper.insert(allot);
                        }
                    }
                }
            }
        }

        //（租电包月）
        for (RentPeriodOrder order : incrementRentPeriodList) {
            //查询分成系数
            Map<Integer, IncomeRatioHistory> map = null;
            if (StringUtils.isNotEmpty(order.getShopId())) {
                map = shopIncomeRatioHistoryPlatformMap(order.getAgentId(), order.getShopId(), statsDate);
            }
            if (StringUtils.isNotEmpty(order.getAgentCompanyId())) {
                //保存对应运营公司
                AgentCompany agentCompany = getAgentCompany(order.getAgentCompanyId());
                Map<Integer, IncomeRatioHistory> companyMap = agentCompanyIncomeRatioHistoryPlatformMap(order.getAgentId(), order.getAgentCompanyId(), statsDate);
                List<Integer> orgTypeList = Arrays.asList(
                        IncomeRatioHistory.OrgType.PLATFORM.getValue(),
                        IncomeRatioHistory.OrgType.PROVINCE_AGENT.getValue(),
                        IncomeRatioHistory.OrgType.CITY_AGENT.getValue(),
                        IncomeRatioHistory.OrgType.SHOP.getValue(),
                        IncomeRatioHistory.OrgType.AGENT_COMPANY.getValue(),
                        IncomeRatioHistory.OrgType.AGENT.getValue()
                );

                IncomeRatioHistory companyRatioHistory = companyMap.get(IncomeRatioHistory.OrgType.AGENT_COMPANY.getValue());
                Integer ratioBaseMoney = companyRatioHistory.getRatioBaseMoney();

                Map<Integer, IncomeRatioHistory> agentCompanyMap = new LinkedHashMap<Integer, IncomeRatioHistory>();
                if (agentCompany.getKeepShopRatio() == null || agentCompany.getKeepShopRatio() == ConstEnum.Flag.TRUE.getValue()) {
                    //正常情况给门店分成
                    for (Integer integer : orgTypeList) {
                        agentCompanyMap.put(integer, map.get(integer));
                        if (ratioBaseMoney == null || (ratioBaseMoney <= order.getMoney())) {
                            //订单金额大于等于下限金额、下限金额为空，给运营公司分成
                            if (integer == IncomeRatioHistory.OrgType.AGENT_COMPANY.getValue()) {
                                for (Integer integer1 : companyMap.keySet()) {
                                    agentCompanyMap.put(integer, companyMap.get(integer1));
                                }
                            }
                        } else {
                            IncomeRatioHistory incomeRatioHistory = companyMap.get(integer);
                            if (incomeRatioHistory != null) {
                                IncomeRatioHistory newIncomeRatioHistory = new IncomeRatioHistory();
                                BeanUtils.copyProperties(incomeRatioHistory, newIncomeRatioHistory);
                                newIncomeRatioHistory.setRatio(0);
                                newIncomeRatioHistory.setAgentCompanyFixedMoney(0);
                                agentCompanyMap.put(integer, newIncomeRatioHistory);
                            }
                        }
                    }
                } else if (agentCompany.getKeepShopRatio() == ConstEnum.Flag.FALSE.getValue()) {
                    //运营公司骑手不给门店分成
                    for (Integer integer : orgTypeList) {
                        if (integer != IncomeRatioHistory.OrgType.SHOP.getValue()) {
                            agentCompanyMap.put(integer, map.get(integer));
                            if (ratioBaseMoney == null || (ratioBaseMoney <= order.getMoney())) {
                                //订单金额大于等于下限金额、下限金额为空，给运营公司分成
                                if (integer == IncomeRatioHistory.OrgType.AGENT_COMPANY.getValue()) {
                                    for (Integer integer1 : companyMap.keySet()) {
                                        agentCompanyMap.put(integer, companyMap.get(integer1));
                                    }
                                }
                            } else {
                                IncomeRatioHistory incomeRatioHistory = companyMap.get(integer);
                                if (incomeRatioHistory != null) {
                                    IncomeRatioHistory newIncomeRatioHistory = new IncomeRatioHistory();
                                    BeanUtils.copyProperties(incomeRatioHistory, newIncomeRatioHistory);
                                    newIncomeRatioHistory.setRatio(0);
                                    newIncomeRatioHistory.setAgentCompanyFixedMoney(0);
                                    agentCompanyMap.put(integer, newIncomeRatioHistory);
                                }
                            }
                        } else {
                            IncomeRatioHistory incomeRatioHistory = map.get(integer);
                            if (incomeRatioHistory != null) {
                                IncomeRatioHistory newIncomeRatioHistory = new IncomeRatioHistory();
                                BeanUtils.copyProperties(incomeRatioHistory, newIncomeRatioHistory);
                                newIncomeRatioHistory.setRatio(0);
                                newIncomeRatioHistory.setShopFixedMoney(0);
                                agentCompanyMap.put(integer, newIncomeRatioHistory);
                            }
                        }
                    }
                }

                map = agentCompanyMap;
            }

            //查询订单是否已经分配金额,已分配,不再作处理
            List<RentPeriodOrderAllot> allotList = rentPeriodOrderAllotMapper.findByOrder(order.getAgentId(), RentPeriodOrderAllot.ServiceType.INCOME.getValue(), order.getId(), suffix, statsDate);
            if(allotList.size() > 0){
                continue;
            }
            //分成处理
            double orderMoney = order.getMoney() * 1d;//订单金额
            double totalAllot = 0d;//已分成金额
            if(map != null){
                for(Integer orgType : map.keySet()){
                    if(orgType != IncomeRatioHistory.OrgType.AGENT.getValue() &&  (orderMoney - totalAllot) <= 0){
                        continue;
                    }
                    IncomeRatioHistory ratioHistory = map.get(orgType);
                    if(ratioHistory != null){
                        //如果是平台分成，如果存在每笔提成，优先扣除
//                        double platformDeductMoney = 0;
//                        if(ratioHistory.getPlatformDeductMoney() != null && ratioHistory.getPlatformDeductMoney() > 0){
//                            platformDeductMoney = ratioHistory.getPlatformDeductMoney();
//                            if((orderMoney - totalAllot) < platformDeductMoney){
//                                platformDeductMoney = orderMoney - totalAllot;
//                            }
//                            totalAllot += platformDeductMoney;
//                        }

                        double money = orderMoney * ratioHistory.getRatio() * 1d /100.0;//分配给代理的金额
                        if((orderMoney - totalAllot) < money){
                            money = orderMoney - totalAllot;
                        }

                        totalAllot += money;

                        //门店固定分成
                        double shopFixedMoney = 0;
                        if(orgType == IncomeRatioHistory.OrgType.SHOP.getValue()){
                            if(ratioHistory.getShopFixedMoney() != null && ratioHistory.getShopFixedMoney() > 0){
                                shopFixedMoney = (int)Math.round(ratioHistory.getShopFixedMoney() * 1d / 30 * dealDays(order.getDayCount()));
                                if((orderMoney - totalAllot) < shopFixedMoney){
                                    shopFixedMoney = orderMoney - totalAllot;
                                }
                                totalAllot += shopFixedMoney;
                            }
                        }

                        //运营公司固定分成
                        double agentCompanyFixedMoney = 0;
                        if (orgType == IncomeRatioHistory.OrgType.AGENT_COMPANY.getValue()) {
                            if (ratioHistory.getAgentCompanyFixedMoney() != null && ratioHistory.getAgentCompanyFixedMoney() > 0) {
                                agentCompanyFixedMoney = (int) Math.round(ratioHistory.getAgentCompanyFixedMoney() * 1d / 30 * dealDays(order.getDayCount()));
                                if ((orderMoney - totalAllot) < agentCompanyFixedMoney) {
                                    agentCompanyFixedMoney = orderMoney - totalAllot;
                                }
                                totalAllot += agentCompanyFixedMoney;
                            }
                        }

                        //保存分配金额
                        RentPeriodOrderAllot allot = new RentPeriodOrderAllot();
                        allot.setPartnerId(order.getPartnerId());
                        allot.setAgentId(order.getAgentId());
                        allot.setShopId(order.getShopId());
                        allot.setAgentCompanyId(order.getAgentCompanyId());
                        allot.setOrderId(order.getId());
                        allot.setCustomerName(order.getCustomerFullname());
                        allot.setCustomerMobile(order.getCustomerMobile());
                        allot.setOrderMoney(order.getMoney());
                        allot.setServiceType(RentPeriodOrderAllot.ServiceType.INCOME.getValue());
                        allot.setRatio(ratioHistory.getRatio());
                        allot.setOrgType(orgType);
                        allot.setOrgId(ratioHistory.getOrgId());
                        allot.setShopId(ratioHistory.getShopId());
                        allot.setAgentCompanyId(ratioHistory.getAgentCompanyId());
                        allot.setOrgName(ratioHistory.getOrgName());
                        allot.setPlatformDeductMoney(0d);
                        allot.setShopFixedMoney(shopFixedMoney);
                        allot.setAgentCompanyFixedMoney(agentCompanyFixedMoney);
                        allot.setMoney(shopFixedMoney + agentCompanyFixedMoney + money);
                        allot.setDayCount(order.getDayCount());
                        allot.setBeginTime(order.getBeginTime());
                        allot.setEndTime(order.getEndTime());
                        allot.setPayTime(order.getPayTime());
                        allot.setStatsDate(statsDate);
                        allot.setCreateTime(new Date());
                        allot.setSuffix(BatteryOrderAllot.getSuffixByString(statsDate));
                        rentPeriodOrderAllotMapper.insert(allot);

                    }
                }
            }
        }

        //（租电包月退款）
        for (RentPeriodOrder order : rentPeriodRefundList) {
            //查询订单是否已经分配金额,已分配,不再作处理
            List<RentPeriodOrderAllot> allotList = rentPeriodOrderAllotMapper.findByOrder(order.getAgentId(), RentPeriodOrderAllot.ServiceType.REFUND.getValue(), order.getId(), suffix, statsDate);
            if(allotList.size() > 0){
                continue;
            }
            String payStatsDate = DateFormatUtils.format(order.getPayTime(), Constant.DATE_FORMAT);
            calendar.setTime(order.getPayTime());
            String paySuffix = String.format("%d%02d", calendar.get(Calendar.YEAR), calendar.get(Calendar.WEEK_OF_YEAR));
            //如果退款金额与订单金额相同，查找之前已经分配过的订单表，返还金额
            boolean refundFlag = false;
            if(order.getMoney().intValue() == order.getRefundMoney().intValue()){
                List<RentPeriodOrderAllot> payAllotList = rentPeriodOrderAllotMapper.findByOrder(order.getAgentId(), BatteryOrderAllot.ServiceType.INCOME.getValue(), order.getId(), paySuffix, payStatsDate);
                //查找之前已经分配过的订单表，返还金额
                if(payAllotList != null && payAllotList.size() > 0){
                    for(RentPeriodOrderAllot rentPeriodOrderAllot : payAllotList){
                        rentPeriodOrderAllot.setServiceType(BatteryOrderAllot.ServiceType.REFUND.getValue());
                        rentPeriodOrderAllot.setStatsDate(statsDate);
                        rentPeriodOrderAllot.setCreateTime(new Date());
                        rentPeriodOrderAllot.setSuffix(BatteryOrderAllot.getSuffixByString(statsDate));
                        rentPeriodOrderAllotMapper.insert(rentPeriodOrderAllot);
                    }
                    refundFlag = true;
                }
            }
            //取购买订单时的分成比例进行计算
            if(!refundFlag){
                //查询分成系数
                Map<Integer, IncomeRatioHistory> map = null;
                if (StringUtils.isNotEmpty(order.getShopId())) {
                    map = shopIncomeRatioHistoryPlatformMap(order.getAgentId(), order.getShopId(), statsDate);
                }
                if (StringUtils.isNotEmpty(order.getAgentCompanyId())) {
                    //保存对应运营公司
                    AgentCompany agentCompany = getAgentCompany(order.getAgentCompanyId());
                    Map<Integer, IncomeRatioHistory> companyMap = agentCompanyIncomeRatioHistoryPlatformMap(order.getAgentId(), order.getAgentCompanyId(), statsDate);
                    List<Integer> orgTypeList = Arrays.asList(
                            IncomeRatioHistory.OrgType.PLATFORM.getValue(),
                            IncomeRatioHistory.OrgType.PROVINCE_AGENT.getValue(),
                            IncomeRatioHistory.OrgType.CITY_AGENT.getValue(),
                            IncomeRatioHistory.OrgType.SHOP.getValue(),
                            IncomeRatioHistory.OrgType.AGENT_COMPANY.getValue(),
                            IncomeRatioHistory.OrgType.AGENT.getValue()
                    );

                    IncomeRatioHistory companyRatioHistory = companyMap.get(IncomeRatioHistory.OrgType.AGENT_COMPANY.getValue());
                    Integer ratioBaseMoney = companyRatioHistory.getRatioBaseMoney();

                    Map<Integer, IncomeRatioHistory> agentCompanyMap = new LinkedHashMap<Integer, IncomeRatioHistory>();
                    if (agentCompany.getKeepShopRatio() == null || agentCompany.getKeepShopRatio() == ConstEnum.Flag.TRUE.getValue()) {
                        //正常情况给门店分成
                        for (Integer integer : orgTypeList) {
                            agentCompanyMap.put(integer, map.get(integer));
                            if (ratioBaseMoney == null || (ratioBaseMoney <= order.getMoney())) {
                                //订单金额大于等于下限金额、下限金额为空，给运营公司分成
                                if (integer == IncomeRatioHistory.OrgType.AGENT_COMPANY.getValue()) {
                                    for (Integer integer1 : companyMap.keySet()) {
                                        agentCompanyMap.put(integer, companyMap.get(integer1));
                                    }
                                }
                            } else {
                                IncomeRatioHistory incomeRatioHistory = companyMap.get(integer);
                                if (incomeRatioHistory != null) {
                                    IncomeRatioHistory newIncomeRatioHistory = new IncomeRatioHistory();
                                    BeanUtils.copyProperties(incomeRatioHistory, newIncomeRatioHistory);
                                    newIncomeRatioHistory.setRatio(0);
                                    newIncomeRatioHistory.setAgentCompanyFixedMoney(0);
                                    agentCompanyMap.put(integer, newIncomeRatioHistory);
                                }
                            }
                        }
                    } else if (agentCompany.getKeepShopRatio() == ConstEnum.Flag.FALSE.getValue()) {
                        //运营公司骑手不给门店分成
                        for (Integer integer : orgTypeList) {
                            if (integer != IncomeRatioHistory.OrgType.SHOP.getValue()) {
                                agentCompanyMap.put(integer, map.get(integer));
                                if (ratioBaseMoney == null || (ratioBaseMoney <= order.getMoney())) {
                                    //订单金额大于等于下限金额、下限金额为空，给运营公司分成
                                    if (integer == IncomeRatioHistory.OrgType.AGENT_COMPANY.getValue()) {
                                        for (Integer integer1 : companyMap.keySet()) {
                                            agentCompanyMap.put(integer, companyMap.get(integer1));
                                        }
                                    }
                                } else {
                                    IncomeRatioHistory incomeRatioHistory = companyMap.get(integer);
                                    if (incomeRatioHistory != null) {
                                        IncomeRatioHistory newIncomeRatioHistory = new IncomeRatioHistory();
                                        BeanUtils.copyProperties(incomeRatioHistory, newIncomeRatioHistory);
                                        newIncomeRatioHistory.setRatio(0);
                                        newIncomeRatioHistory.setAgentCompanyFixedMoney(0);
                                        agentCompanyMap.put(integer, newIncomeRatioHistory);
                                    }
                                }
                            } else {
                                IncomeRatioHistory incomeRatioHistory = map.get(integer);
                                if (incomeRatioHistory != null) {
                                    IncomeRatioHistory newIncomeRatioHistory = new IncomeRatioHistory();
                                    BeanUtils.copyProperties(incomeRatioHistory, newIncomeRatioHistory);
                                    newIncomeRatioHistory.setRatio(0);
                                    newIncomeRatioHistory.setShopFixedMoney(0);
                                    agentCompanyMap.put(integer, newIncomeRatioHistory);
                                }
                            }
                        }
                    }

                    map = agentCompanyMap;
                }
                double orderMoney = order.getRefundMoney() * 1d;//订单金额
                double totalAllot = 0d;//已分成金额
                if(map != null){
                    for(Integer orgType : map.keySet()){
                        if(orgType != IncomeRatioHistory.OrgType.AGENT.getValue() &&  (orderMoney - totalAllot) <= 0){
                            break;
                        }
                        IncomeRatioHistory ratioHistory = map.get(orgType);
                        if(ratioHistory != null){
                            //如果是平台分成，如果存在每笔提成，优先扣除
                            double platformDeductMoney = 0;
//                            if(ratioHistory.getPlatformDeductMoney() != null && ratioHistory.getPlatformDeductMoney() > 0){
//                                platformDeductMoney = ratioHistory.getPlatformDeductMoney();
//                                if((orderMoney - totalAllot) < platformDeductMoney){
//                                    platformDeductMoney = orderMoney - totalAllot;
//                                }
//                                totalAllot += platformDeductMoney;
//                            }

                            double money = orderMoney * ratioHistory.getRatio() * 1d /100.0;//分配给代理的金额
                            if((orderMoney - totalAllot) < money){
                                money = orderMoney - totalAllot;
                            }

                            totalAllot += money;

                            //门店固定分成
                            double shopFixedMoney = 0;
                            if(orgType == IncomeRatioHistory.OrgType.SHOP.getValue()){
                                if(ratioHistory.getShopFixedMoney() != null && ratioHistory.getShopFixedMoney() > 0){
                                    shopFixedMoney = (int)Math.round(ratioHistory.getShopFixedMoney() * 1d / 30 * dealDays(order.getDayCount()));
                                    if((orderMoney - totalAllot) < shopFixedMoney){
                                        shopFixedMoney = orderMoney - totalAllot;
                                    }
                                    totalAllot += shopFixedMoney;
                                }
                            }

                            //运营公司固定分成
                            double agentCompanyFixedMoney = 0;
                            if (orgType == IncomeRatioHistory.OrgType.AGENT_COMPANY.getValue()) {
                                if (ratioHistory.getAgentCompanyFixedMoney() != null && ratioHistory.getAgentCompanyFixedMoney() > 0) {
                                    agentCompanyFixedMoney = (int) Math.round(ratioHistory.getAgentCompanyFixedMoney() * 1d / 30 * dealDays(order.getDayCount()));
                                    if ((orderMoney - totalAllot) < agentCompanyFixedMoney) {
                                        agentCompanyFixedMoney = orderMoney - totalAllot;
                                    }
                                    totalAllot += agentCompanyFixedMoney;
                                }
                            }

                            //保存分配金额
                            RentPeriodOrderAllot allot = new RentPeriodOrderAllot();
                            allot.setPartnerId(order.getPartnerId());
                            allot.setAgentId(order.getAgentId());
                            allot.setShopId(order.getShopId());
                            allot.setAgentCompanyId(order.getAgentCompanyId());
                            allot.setOrderId(order.getId());
                            allot.setCustomerName(order.getCustomerFullname());
                            allot.setCustomerMobile(order.getCustomerMobile());
                            allot.setOrderMoney(order.getRefundMoney());
                            allot.setServiceType(RentPeriodOrderAllot.ServiceType.REFUND.getValue());
                            allot.setRatio(ratioHistory.getRatio());
                            allot.setOrgType(orgType);
                            allot.setOrgId(ratioHistory.getOrgId());
                            allot.setShopId(ratioHistory.getShopId());
                            allot.setAgentCompanyId(ratioHistory.getAgentCompanyId());
                            allot.setOrgName(ratioHistory.getOrgName());
                            allot.setPlatformDeductMoney(platformDeductMoney);
                            allot.setShopFixedMoney(shopFixedMoney);
                            allot.setAgentCompanyFixedMoney(agentCompanyFixedMoney);
                            allot.setMoney(platformDeductMoney + shopFixedMoney + agentCompanyFixedMoney + money);
                            allot.setDayCount(order.getDayCount());
                            allot.setBeginTime(order.getBeginTime());
                            allot.setEndTime(order.getEndTime());
                            allot.setPayTime(order.getPayTime());
                            allot.setStatsDate(statsDate);
                            allot.setCreateTime(new Date());
                            allot.setSuffix(BatteryOrderAllot.getSuffixByString(statsDate));
                            rentPeriodOrderAllotMapper.insert(allot);
                        }
                    }
                }
            }
        }

        //终端日统计
        for (String cabinetId : cabinetMap.keySet()) {
            Cabinet cabinet = cabinetMap.get(cabinetId);
            String key = String.format("%s_%d",cabinetId,cabinet.getAgentId());
            CabinetDayStats cabinetDayStats = cabinetDayStatsMap.get(key);
            if (cabinetDayStats == null) {
                cabinetDayStats = new CabinetDayStats();
                cabinetDayStats.init();
                cabinetDayStats.setCabinetId(cabinetId);
                cabinetDayStats.setCabinetName(cabinet.getCabinetName());
                cabinetDayStats.setAgentId(cabinet.getAgentId());
                Double price = (cabinet.getPrice() == null ? 0 : cabinet.getPrice() * 100);
                cabinetDayStats.setUnitPrice( price.intValue());
                cabinetDayStatsMap.put(key, cabinetDayStats);
            }

            List<Integer> orgTypeList = Arrays.asList(
                    IncomeRatioHistory.OrgType.AGENT.getValue());

            //当日换电金额(分成前)
            Double exchangeMoney = batteryOrderAllotMapper.sumOrderMoney(null, cabinetDayStats.getAgentId(), cabinetId, BatteryOrderAllot.ServiceType.INCOME.getValue(), IncomeRatioHistory.OrgType.AGENT.getValue(), statsDate, suffix);
            //运营商当日新增换电金额(分成后)
            Double agentExchangeMoney = batteryOrderAllotMapper.sumMoney(null, cabinetDayStats.getAgentId(), cabinetId,  BatteryOrderAllot.ServiceType.INCOME.getValue(), orgTypeList, statsDate, suffix);
            //门店当日新增换电金额(分成后)
            Double shopExchangeMoney = batteryOrderAllotMapper.sumShopMoney(null, cabinetId, null, BatteryOrderAllot.ServiceType.INCOME.getValue(), IncomeRatioHistory.OrgType.SHOP.getValue(), statsDate, suffix);
            //运营公司新增换电金额(分成后)
            Double agentCompanyExchangeMoney = batteryOrderAllotMapper.sumAgentCompanyMoney(null, cabinetId, null, BatteryOrderAllot.ServiceType.INCOME.getValue(), IncomeRatioHistory.OrgType.AGENT_COMPANY.getValue(), statsDate, suffix);


            //当日包时段订单(分成前)
            Double packetPeriodMoney = packetPeriodOrderAllotMapper.sumOrderMoney(null, cabinetDayStats.getAgentId(), cabinetId, PacketPeriodOrderAllot.ServiceType.INCOME.getValue(), IncomeRatioHistory.OrgType.AGENT.getValue(), statsDate, suffix);
            //运营商当日新增包时段订单(分成后)
            Double agentPacketPeriodMoney = packetPeriodOrderAllotMapper.sumMoney(null, cabinetDayStats.getAgentId(), cabinetId, PacketPeriodOrderAllot.ServiceType.INCOME.getValue(), orgTypeList, statsDate, suffix);
            //门店当日新增包时段订单(分成后)
            Double shopPacketPeriodMoney = packetPeriodOrderAllotMapper.sumShopMoney(null, cabinetId, null, PacketPeriodOrderAllot.ServiceType.INCOME.getValue(), IncomeRatioHistory.OrgType.SHOP.getValue(),  statsDate, suffix);
            //运营公司当日新增包时段订单(分成后)
            Double agentCompanyPacketPeriodMoney = packetPeriodOrderAllotMapper.sumAgentCompanyMoney(null, cabinetId, null, PacketPeriodOrderAllot.ServiceType.INCOME.getValue(), IncomeRatioHistory.OrgType.AGENT_COMPANY.getValue(),  statsDate, suffix);

            //当日退款包时段订单(分成前)
            Double refundPacketPeriodMoney = packetPeriodOrderAllotMapper.sumOrderMoney(null, cabinetDayStats.getAgentId(), cabinetId, PacketPeriodOrderAllot.ServiceType.REFUND.getValue(), IncomeRatioHistory.OrgType.AGENT.getValue(), statsDate, suffix);
            //运营商退款包时段订单(分成后)
            Double agentRefundPacketPeriodMoney = packetPeriodOrderAllotMapper.sumMoney(null, cabinetDayStats.getAgentId(), cabinetId, PacketPeriodOrderAllot.ServiceType.REFUND.getValue(), orgTypeList,  statsDate, suffix);
            //门店退款包时段订单(分成后)
            Double shopRefundPacketPeriodMoney = packetPeriodOrderAllotMapper.sumShopMoney(null, cabinetId, null, PacketPeriodOrderAllot.ServiceType.REFUND.getValue(),  IncomeRatioHistory.OrgType.SHOP.getValue(),  statsDate, suffix);
            //运营公司退款包时段订单(分成后)
            Double agentCompanyRefundPacketPeriodMoney = packetPeriodOrderAllotMapper.sumAgentCompanyMoney(null, cabinetId, null, PacketPeriodOrderAllot.ServiceType.REFUND.getValue(),  IncomeRatioHistory.OrgType.AGENT_COMPANY.getValue(),  statsDate, suffix);

            //当日新增换电订单数
            Integer exchangeCount = batteryOrderAllotMapper.exchangeCount(null,cabinetDayStats.getAgentId(), cabinetId, null, BatteryOrderAllot.ServiceType.INCOME.getValue(), IncomeRatioHistory.OrgType.AGENT.getValue(), statsDate, suffix);

            //当日购买包时段订单次数
            Integer packetPeriodOrderCount = packetPeriodOrderAllotMapper.packetPeriodOrderCount(null,cabinetDayStats.getAgentId(), cabinetId, null, BatteryOrderAllot.ServiceType.INCOME.getValue(), IncomeRatioHistory.OrgType.AGENT.getValue(), statsDate, suffix);

            //当日退款包时段订单次数
            Integer refundPacketPeriodCount = packetPeriodOrderAllotMapper.packetPeriodOrderCount(null,cabinetDayStats.getAgentId(), cabinetId, null, BatteryOrderAllot.ServiceType.REFUND.getValue(), IncomeRatioHistory.OrgType.AGENT.getValue(), statsDate, suffix);

            //保存数据
            cabinetDayStats.setExchangeMoney((int)Math.round(exchangeMoney));
            cabinetDayStats.setPacketPeriodMoney((int)Math.round(packetPeriodMoney));
            cabinetDayStats.setRefundPacketPeriodMoney((int)Math.round(refundPacketPeriodMoney));

            cabinetDayStats.setAgentMoney((int)Math.round(agentExchangeMoney + agentPacketPeriodMoney - agentRefundPacketPeriodMoney));
            cabinetDayStats.setAgentExchangeMoney((int)Math.round(agentExchangeMoney));
            cabinetDayStats.setAgentPacketPeriodMoney((int)Math.round(agentPacketPeriodMoney));
            cabinetDayStats.setAgentRefundPacketPeriodMoney((int)Math.round(agentRefundPacketPeriodMoney));

            cabinetDayStats.setShopMoney((int)Math.round(shopExchangeMoney + shopPacketPeriodMoney - shopRefundPacketPeriodMoney));
            cabinetDayStats.setShopExchangeMoney((int)Math.round(shopExchangeMoney));
            cabinetDayStats.setShopPacketPeriodMoney((int)Math.round(shopPacketPeriodMoney));
            cabinetDayStats.setShopRefundPacketPeriodMoney((int)Math.round(shopRefundPacketPeriodMoney));

            cabinetDayStats.setAgentCompanyMoney((int) Math.round(agentCompanyExchangeMoney + agentCompanyPacketPeriodMoney - agentCompanyRefundPacketPeriodMoney));
            cabinetDayStats.setAgentCompanyExchangeMoney((int) Math.round(agentCompanyExchangeMoney));
            cabinetDayStats.setAgentCompanyPacketPeriodMoney((int) Math.round(agentCompanyPacketPeriodMoney));
            cabinetDayStats.setAgentCompanyRefundPacketPeriodMoney((int) Math.round(agentCompanyRefundPacketPeriodMoney));

            cabinetDayStats.setPacketPeriodCount(packetPeriodOrderCount);
            cabinetDayStats.setOrderCount(exchangeCount);
            cabinetDayStats.setRefundPacketPeriodCount(refundPacketPeriodCount);

            cabinetDayStats.setMoney(cabinetDayStats.getExchangeMoney() + cabinetDayStats.getPacketPeriodMoney() - cabinetDayStats.getRefundPacketPeriodMoney());
        }

        //插入所有终端收入（押金）
        for (CabinetDayStats e : cabinetForegiftList) {
            String key = String.format("%s_%d",e.getCabinetId(),e.getAgentId());
            CabinetDayStats v = cabinetDayStatsMap.get(key);
            if (v == null) {
                Cabinet cabinet = getCabinet(e.getCabinetId());
                if(cabinet == null){
                    continue;
                }
                v = new CabinetDayStats();
                v.init();
                v.setCabinetId(e.getCabinetId());
                v.setCabinetName(cabinet.getCabinetName());
                v.setAgentId(e.getAgentId());
                Double price = (cabinet.getPrice() == null ? 0 : cabinet.getPrice() * 100);
                v.setUnitPrice( price.intValue());
                cabinetDayStatsMap.put(key, v);
            }
            v.setForegiftMoney(e.getForegiftMoney());
            v.setForegiftCount(e.getForegiftCount());
        }

        //插入所有终端收入（押金退款）
        for (CabinetDayStats e : cabinetRefundForegiftList) {
            String key = String.format("%s_%d",e.getCabinetId(),e.getAgentId());
            CabinetDayStats v = cabinetDayStatsMap.get(key);
            if (v == null) {
                Cabinet cabinet = getCabinet(e.getCabinetId());
                if(cabinet == null){
                    continue;
                }
                v = new CabinetDayStats();
                v.init();
                v.setCabinetId(e.getCabinetId());
                v.setCabinetName(cabinet.getCabinetName());
                v.setAgentId(e.getAgentId());
                Double price = (cabinet.getPrice() == null ? 0 : cabinet.getPrice() * 100);
                v.setUnitPrice( price.intValue());
                cabinetDayStatsMap.put(key, v);
            }
            v.setRefundForegiftMoney(e.getRefundForegiftMoney());
            v.setRefundForegiftCount(e.getRefundForegiftCount());
        }

        //插入所有终端收入（保险）
        for (CabinetDayStats e : cabinetInsuranceList) {
            String key = String.format("%s_%d",e.getCabinetId(),e.getAgentId());
            CabinetDayStats v = cabinetDayStatsMap.get(key);
            if (v == null) {
                Cabinet cabinet = getCabinet(e.getCabinetId());
                if(cabinet == null){
                    continue;
                }
                v = new CabinetDayStats();
                v.init();
                v.setCabinetId(e.getCabinetId());
                v.setCabinetName(cabinet.getCabinetName());
                v.setAgentId(e.getAgentId());
                Double price = (cabinet.getPrice() == null ? 0 : cabinet.getPrice() * 100);
                v.setUnitPrice( price.intValue());
                cabinetDayStatsMap.put(key, v);
            }
            v.setInsuranceMoney(e.getInsuranceMoney());
            v.setInsuranceCount(e.getInsuranceCount());
        }

        //插入所有终端退款（保险）
        for (CabinetDayStats e : cabinetRefundInsuranceList) {
            String key = String.format("%s_%d",e.getCabinetId(),e.getAgentId());
            CabinetDayStats v = cabinetDayStatsMap.get(key);
            if (v == null) {
                Cabinet cabinet = getCabinet(e.getCabinetId());
                if(cabinet == null){
                    continue;
                }
                v = new CabinetDayStats();
                v.init();
                v.setCabinetId(e.getCabinetId());
                v.setCabinetName(cabinet.getCabinetName());
                v.setAgentId(e.getAgentId());
                Double price = (cabinet.getPrice() == null ? 0 : cabinet.getPrice() * 100);
                v.setUnitPrice( price.intValue());
                cabinetDayStatsMap.put(key, v);
            }
            v.setRefundInsuranceMoney(e.getRefundInsuranceMoney());
            v.setRefundInsuranceCount(e.getRefundInsuranceCount());
        }

        //插入所有终端（活跃人数）
        for (CabinetDayStats e : cabinetActiveCustomerList) {
            String key = String.format("%s_%d",e.getCabinetId(),e.getAgentId());
            CabinetDayStats v = cabinetDayStatsMap.get(key);
            if (v == null) {
                Cabinet cabinet = getCabinet(e.getCabinetId());
                if(cabinet == null){
                    continue;
                }
                v = new CabinetDayStats();
                v.init();
                v.setCabinetId(e.getCabinetId());
                v.setCabinetName(cabinet.getCabinetName());
                v.setAgentId(e.getAgentId());
                Double price = (cabinet.getPrice() == null ? 0 : cabinet.getPrice() * 100);
                v.setUnitPrice( price.intValue());
                cabinetDayStatsMap.put(key, v);
            }
            v.setActiveCustomerCount(e.getActiveCustomerCount());
        }

        //插入所有终端电价
        for (CabinetDayStats e : cabinetElectricList) {
            String key = String.format("%s_%d",e.getCabinetId(),e.getAgentId());
            CabinetDayStats v = cabinetDayStatsMap.get(key);
            if (v == null) {
                Cabinet cabinet = getCabinet(e.getCabinetId());
                if(cabinet == null){
                    continue;
                }
                v = new CabinetDayStats();
                v.init();
                v.setCabinetId(e.getCabinetId());
                v.setCabinetName(cabinet.getCabinetName());
                v.setAgentId(e.getAgentId());
                Double price = (cabinet.getPrice() == null ? 0 : cabinet.getPrice() * 100);
                v.setUnitPrice( price.intValue());
                cabinetDayStatsMap.put(key, v);
            }
            v.setElectricDegree(e.getElectricDegree());
            v.setElectricPrice(e.getElectricDegree() * v.getUnitPrice());
        }

        //终端统计保存（日月总统计）
        for (CabinetDayStats e : cabinetDayStatsMap.values()) {
            e.setStatsDate(statsDate);
            Agent agent = getAgent(e.getAgentId());
            if(agent == null){
                continue;
            }
            e.setAgentId(agent.getId());
            e.setAgentName(agent.getAgentName());
            CabinetDayStats dayStats = cabinetDayStatsMapper.find(e.getCabinetId(), e.getAgentId(), e.getStatsDate());
            if (dayStats == null) {
                cabinetDayStatsMapper.insert(e);
            } else {
                cabinetDayStatsMapper.update(e);
            }

            CabinetMonthStats cabinetMonthStats = cabinetDayStatsMapper.sumMonth(e.getCabinetId(), e.getAgentId(), statsMonth);

            cabinetMonthStats.setCabinetId(e.getCabinetId());
            cabinetMonthStats.setCabinetName(e.getCabinetName());
            cabinetMonthStats.setAgentId(e.getAgentId());
            cabinetMonthStats.setAgentName(e.getAgentName());
            cabinetMonthStats.setStatsMonth(statsMonth);
            cabinetMonthStats.setUpdateTime(new Date());

            CabinetMonthStats monthStats = cabinetMonthStatsMapper.find(e.getCabinetId(), e.getAgentId(), statsMonth);
            if (monthStats == null) {
                cabinetMonthStatsMapper.insert(cabinetMonthStats);
            } else {
                cabinetMonthStatsMapper.update(cabinetMonthStats);
            }

            CabinetTotalStats cabinetTotalStats = cabinetDayStatsMapper.sumTotal(e.getCabinetId(), e.getAgentId());

            //结算人均电费 设备每天人均(电费/活跃人数)/天数
            int perElectric = cabinetDayStatsMapper.perElectric(e.getCabinetId(), e.getAgentId());
            cabinetTotalStats.setPerElectric(perElectric);

            cabinetTotalStats.setCabinetId(e.getCabinetId());
            cabinetTotalStats.setCabinetName(e.getCabinetName());
            cabinetTotalStats.setAgentId(e.getAgentId());
            cabinetTotalStats.setAgentName(e.getAgentName());
            cabinetTotalStats.setUpdateTime(new Date());

            CabinetTotalStats totalStats = cabinetTotalStatsMapper.find(e.getCabinetId(), e.getAgentId());
            if (totalStats == null) {
                cabinetTotalStatsMapper.insert(cabinetTotalStats);
            } else {
                cabinetTotalStatsMapper.update(cabinetTotalStats);
            }
        }

        //门店换电日统计
        for (String shopId : shopMap.keySet()) {
            ShopDayStats shopDayStats = shopExchangeDayStatsMap.get(shopId);
            if (shopDayStats == null) {
                shopDayStats = new ShopDayStats();
                shopDayStats.init();
                Shop shop = shopMap.get(shopId);
                shopDayStats.setShopId(shopId);
                shopDayStats.setShopName(shop.getShopName());
                shopExchangeDayStatsMap.put(shopId, shopDayStats);
            }

            //门店当日新增换电金额
            Double exchangeMoney = batteryOrderAllotMapper.sumShopMoney(null, null, shopId, BatteryOrderAllot.ServiceType.INCOME.getValue(), IncomeRatioHistory.OrgType.SHOP.getValue(),  statsDate, suffix);

            //门店当日新增包时段订单
            Double packetPeriodMoney = packetPeriodOrderAllotMapper.sumShopMoney(null, null, shopId, PacketPeriodOrderAllot.ServiceType.INCOME.getValue(), IncomeRatioHistory.OrgType.SHOP.getValue(),  statsDate, suffix);

            //门店退款包时段订单
            Double refundPacketPeriodMoney = packetPeriodOrderAllotMapper.sumShopMoney(null, null, shopId, PacketPeriodOrderAllot.ServiceType.REFUND.getValue(),  IncomeRatioHistory.OrgType.SHOP.getValue(),  statsDate, suffix);

            //当日新增换电订单数
            Integer exchangeCount = batteryOrderAllotMapper.exchangeCount(null,null, null, shopId, BatteryOrderAllot.ServiceType.INCOME.getValue(), IncomeRatioHistory.OrgType.SHOP.getValue(), statsDate, suffix);

            //当日购买包时段订单次数
            Integer packetPeriodOrderCount = packetPeriodOrderAllotMapper.packetPeriodOrderCount(null,null, null, shopId, BatteryOrderAllot.ServiceType.INCOME.getValue(), IncomeRatioHistory.OrgType.SHOP.getValue(), statsDate, suffix);

            //当日退款包时段订单次数
            Integer refundPacketPeriodCount = packetPeriodOrderAllotMapper.packetPeriodOrderCount(null,null, null, shopId, BatteryOrderAllot.ServiceType.REFUND.getValue(), IncomeRatioHistory.OrgType.SHOP.getValue(), statsDate, suffix);

            //运营商维度统计
            Double agentPacketPeriodMoney = packetPeriodOrderAllotMapper.sumShopAgentMoney(shopId, PacketPeriodOrderAllot.ServiceType.INCOME.getValue(), IncomeRatioHistory.OrgType.SHOP.getValue(),  statsDate, suffix);
            Double agentRefundPacketPeriodMoney = packetPeriodOrderAllotMapper.sumShopAgentMoney( shopId, PacketPeriodOrderAllot.ServiceType.REFUND.getValue(),  IncomeRatioHistory.OrgType.SHOP.getValue(),  statsDate, suffix);
            Integer agentPacketPeriodOrderCount = packetPeriodOrderAllotMapper.shopPacketPeriodOrderCount( shopId, BatteryOrderAllot.ServiceType.INCOME.getValue(), IncomeRatioHistory.OrgType.SHOP.getValue(), statsDate, suffix);
            Integer agentRefundPacketPeriodCount = packetPeriodOrderAllotMapper.shopPacketPeriodOrderCount( shopId, BatteryOrderAllot.ServiceType.REFUND.getValue(), IncomeRatioHistory.OrgType.SHOP.getValue(), statsDate, suffix);

            //保存数据
            shopDayStats.setMoney((int)Math.round(exchangeMoney + packetPeriodMoney  - refundPacketPeriodMoney));
            shopDayStats.setExchangeMoney((int)Math.round(exchangeMoney));
            shopDayStats.setPacketPeriodMoney((int)Math.round(packetPeriodMoney));
            shopDayStats.setRefundPacketPeriodMoney((int)Math.round(refundPacketPeriodMoney));

            shopDayStats.setOrderCount(exchangeCount);
            shopDayStats.setPacketPeriodCount(packetPeriodOrderCount);
            shopDayStats.setRefundPacketPeriodCount(refundPacketPeriodCount);

            //运营商维度统计
            shopDayStats.setAgentPacketPeriodMoney((int)Math.round(agentPacketPeriodMoney));
            shopDayStats.setAgentRefundPacketPeriodMoney((int)Math.round(agentRefundPacketPeriodMoney));
            shopDayStats.setAgentPacketPeriodCount(agentPacketPeriodOrderCount);
            shopDayStats.setAgentRefundPacketPeriodCount(agentRefundPacketPeriodCount);

        }

        //插入所有门店收入（押金）
        for (ShopDayStats e : shopForegiftList) {
            ShopDayStats shopDayStats = shopExchangeDayStatsMap.get(e.getShopId());
            if (shopDayStats == null) {
                shopDayStats = new ShopDayStats();
                shopDayStats.init();
                Shop shop = shopMap.get(e.getShopId());
                if(shop == null){
                    continue;
                }
                shopDayStats.setShopId(e.getShopId());
                shopDayStats.setShopName(shop.getShopName());
                shopExchangeDayStatsMap.put(e.getShopId(), shopDayStats);
            }
            shopDayStats.setAgentForegiftMoney(e.getAgentForegiftMoney());
            shopDayStats.setAgentForegiftCount(e.getAgentForegiftCount());
        }

        //插入所有门店收入（押金退款）
        for (ShopDayStats e : shopRefundForegiftList) {
            ShopDayStats shopDayStats = shopExchangeDayStatsMap.get(e.getShopId());
            if (shopDayStats == null) {
                shopDayStats = new ShopDayStats();
                shopDayStats.init();
                Shop shop = shopMap.get(e.getShopId());
                if(shop == null){
                    continue;
                }
                shopDayStats.setShopId(e.getShopId());
                shopDayStats.setShopName(shop.getShopName());
                shopExchangeDayStatsMap.put(e.getShopId(), shopDayStats);
            }
            shopDayStats.setAgentRefundForegiftMoney(e.getAgentRefundForegiftMoney());
            shopDayStats.setAgentRefundForegiftCount(e.getAgentRefundForegiftCount());
        }


//        //插入所有门店收入（保险）
//        for (ShopDayStats e : shopInsuranceList) {
//            ShopDayStats shopDayStats = shopExchangeDayStatsMap.get(e.getShopId());
//            if (shopDayStats == null) {
//                shopDayStats = new ShopDayStats();
//                shopDayStats.init();
//                Shop shop = shopMap.get(e.getShopId());
//                shopDayStats.setShopId(e.getShopId());
//                shopDayStats.setShopName(shop.getShopName());
//                shopExchangeDayStatsMap.put(e.getShopId(), shopDayStats);
//            }
//            shopDayStats.setAgentInsuranceMoney(e.getAgentInsuranceMoney());
//            shopDayStats.setAgentInsuranceCount(e.getAgentInsuranceCount());
//        }
//
//        //插入所有门店退款（保险）
//        for (ShopDayStats e : shopRefundInsuranceList) {
//            ShopDayStats shopDayStats = shopExchangeDayStatsMap.get(e.getShopId());
//            if (shopDayStats == null) {
//                shopDayStats = new ShopDayStats();
//                shopDayStats.init();
//                Shop shop = shopMap.get(e.getShopId());
//                shopDayStats.setShopId(e.getShopId());
//                shopDayStats.setShopName(shop.getShopName());
//                shopExchangeDayStatsMap.put(e.getShopId(), shopDayStats);
//            }
//            shopDayStats.setAgentRefundInsuranceMoney(e.getAgentRefundInsuranceMoney());
//            shopDayStats.setAgentRefundInsuranceCount(e.getAgentRefundInsuranceCount());
//        }

        //门店换电统计保存（日总统计）
        for (ShopDayStats e : shopExchangeDayStatsMap.values()) {
            e.setStatsDate(statsDate);
            e.setCategory(ConstEnum.Category.EXCHANGE.getValue());
            Shop shop = getShop(e.getShopId());
            Agent agent = getAgent(shop.getAgentId());
            if(agent == null){
                continue;
            }
            e.setAgentId(agent.getId());
            e.setAgentName(agent.getAgentName());
            Partner partner = getPartner(agent.getPartnerId());
            e.setPartnerId(partner.getId());
            e.setPartnerName(partner.getPartnerName());

            ShopDayStats dayStats = shopDayStatsMapper.find(e.getShopId(), e.getStatsDate(), e.getCategory());
            if (dayStats == null) {
                shopDayStatsMapper.insert(e);
            } else {
                shopDayStatsMapper.update(e);
            }

            ShopTotalStats shopTotalStats = shopDayStatsMapper.sumTotal(e.getShopId(), e.getCategory());

            shopTotalStats.setCategory(e.getCategory());
            shopTotalStats.setShopId(e.getShopId());
            shopTotalStats.setShopName(e.getShopName());
            shopTotalStats.setAgentId(e.getAgentId());
            shopTotalStats.setAgentName(e.getAgentName());
            shopTotalStats.setPartnerId(e.getPartnerId());
            shopTotalStats.setPartnerName(e.getPartnerName());
            shopTotalStats.setUpdateTime(new Date());

            ShopTotalStats totalStats = shopTotalStatsMapper.find(e.getShopId(), e.getCategory());
            if (totalStats == null) {
                shopTotalStatsMapper.insert(shopTotalStats);
            } else {
                shopTotalStatsMapper.update(shopTotalStats);
            }
        }

        //门店租电日统计
        for (String shopId : shopMap.keySet()) {
            ShopDayStats shopDayStats = shopRentDayStatsMap.get(shopId);
            if (shopDayStats == null) {
                shopDayStats = new ShopDayStats();
                shopDayStats.init();
                Shop shop = shopMap.get(shopId);
                if(shop == null){
                    continue;
                }
                shopDayStats.setShopId(shopId);
                shopDayStats.setShopName(shop.getShopName());
                shopRentDayStatsMap.put(shopId, shopDayStats);
            }

            //租电门店当日新增包时段订单
            Double rentPeriodMoney = rentPeriodOrderAllotMapper.sumShopMoney(null,  shopId, PacketPeriodOrderAllot.ServiceType.INCOME.getValue(), IncomeRatioHistory.OrgType.SHOP.getValue(),  statsDate, suffix);

            //租电门店退款包时段订单
            Double refundRentPeriodMoney = rentPeriodOrderAllotMapper.sumShopMoney(null,  shopId, PacketPeriodOrderAllot.ServiceType.REFUND.getValue(),  IncomeRatioHistory.OrgType.SHOP.getValue(),  statsDate, suffix);

            //租电当日购买包时段订单次数
            Integer rentPeriodOrderCount = rentPeriodOrderAllotMapper.rentPeriodOrderCount(null, null, shopId, BatteryOrderAllot.ServiceType.INCOME.getValue(), IncomeRatioHistory.OrgType.SHOP.getValue(), statsDate, suffix);

            //租电当日退款包时段订单次数
            Integer refundRentPeriodCount = rentPeriodOrderAllotMapper.rentPeriodOrderCount(null, null, shopId, BatteryOrderAllot.ServiceType.REFUND.getValue(), IncomeRatioHistory.OrgType.SHOP.getValue(), statsDate, suffix);

            //运营商维度统计
            Double agentPacketPeriodMoney = rentPeriodOrderAllotMapper.sumShopAgentMoney(shopId, PacketPeriodOrderAllot.ServiceType.INCOME.getValue(), IncomeRatioHistory.OrgType.AGENT.getValue(),  statsDate, suffix);
            Double agentRefundPacketPeriodMoney = rentPeriodOrderAllotMapper.sumShopAgentMoney( shopId, PacketPeriodOrderAllot.ServiceType.REFUND.getValue(),  IncomeRatioHistory.OrgType.AGENT.getValue(),  statsDate, suffix);
            Integer agentPacketPeriodOrderCount = rentPeriodOrderAllotMapper.shopRentPeriodOrderCount( shopId, BatteryOrderAllot.ServiceType.INCOME.getValue(), IncomeRatioHistory.OrgType.AGENT.getValue(), statsDate, suffix);
            Integer agentRefundPacketPeriodCount = rentPeriodOrderAllotMapper.shopRentPeriodOrderCount( shopId, BatteryOrderAllot.ServiceType.REFUND.getValue(), IncomeRatioHistory.OrgType.AGENT.getValue(), statsDate, suffix);


            //保存数据
            shopDayStats.setMoney((int)Math.round(rentPeriodMoney - refundRentPeriodMoney));
            shopDayStats.setPacketPeriodMoney((int)Math.round(rentPeriodMoney));
            shopDayStats.setRefundPacketPeriodMoney((int)Math.round(refundRentPeriodMoney));

            shopDayStats.setPacketPeriodCount(rentPeriodOrderCount);
            shopDayStats.setRefundPacketPeriodCount(refundRentPeriodCount);

            //运营商维度统计
            shopDayStats.setAgentPacketPeriodMoney((int)Math.round(agentPacketPeriodMoney));
            shopDayStats.setAgentRefundPacketPeriodMoney((int)Math.round(agentRefundPacketPeriodMoney));
            shopDayStats.setAgentPacketPeriodCount(agentPacketPeriodOrderCount);
            shopDayStats.setAgentRefundPacketPeriodCount(agentRefundPacketPeriodCount);

        }


        //插入所有门店收入（押金）
        for (ShopDayStats e : shopRentForegiftList) {
            ShopDayStats shopDayStats = shopRentDayStatsMap.get(e.getShopId());
            if (shopDayStats == null) {
                shopDayStats = new ShopDayStats();
                shopDayStats.init();
                Shop shop = shopMap.get(e.getShopId());
                if(shop == null){
                    continue;
                }
                shopDayStats.setShopId(e.getShopId());
                shopDayStats.setShopName(shop.getShopName());
                shopRentDayStatsMap.put(e.getShopId(), shopDayStats);
            }
            shopDayStats.setAgentForegiftMoney(e.getAgentForegiftMoney());
            shopDayStats.setAgentForegiftCount(e.getAgentForegiftCount());
        }

        //插入所有门店收入（押金退款）
        for (ShopDayStats e : shopRentRefundForegiftList) {
            ShopDayStats shopDayStats = shopRentDayStatsMap.get(e.getShopId());
            if (shopDayStats == null) {
                shopDayStats = new ShopDayStats();
                shopDayStats.init();
                Shop shop = shopMap.get(e.getShopId());
                if(shop == null){
                    continue;
                }
                shopDayStats.setShopId(e.getShopId());
                shopDayStats.setShopName(shop.getShopName());
                shopRentDayStatsMap.put(e.getShopId(), shopDayStats);
            }
            shopDayStats.setAgentRefundForegiftMoney(e.getAgentRefundForegiftMoney());
            shopDayStats.setAgentRefundForegiftCount(e.getAgentRefundForegiftCount());
        }

//        //插入所有门店收入（保险）
//        for (ShopDayStats e : shopRentInsuranceList) {
//            ShopDayStats shopDayStats = shopRentDayStatsMap.get(e.getShopId());
//            if (shopDayStats == null) {
//                shopDayStats = new ShopDayStats();
//                shopDayStats.init();
//                Shop shop = shopMap.get(e.getShopId());
//                shopDayStats.setShopId(e.getShopId());
//                shopDayStats.setShopName(shop.getShopName());
//                shopRentDayStatsMap.put(e.getShopId(), shopDayStats);
//            }
//            shopDayStats.setAgentInsuranceMoney(e.getAgentInsuranceMoney());
//            shopDayStats.setAgentInsuranceCount(e.getAgentInsuranceCount());
//        }
//
//        //插入所有门店退款（保险）
//        for (ShopDayStats e : shopRentRefundInsuranceList) {
//            ShopDayStats shopDayStats = shopRentDayStatsMap.get(e.getShopId());
//            if (shopDayStats == null) {
//                shopDayStats = new ShopDayStats();
//                shopDayStats.init();
//                Shop shop = shopMap.get(e.getShopId());
//                shopDayStats.setShopId(e.getShopId());
//                shopDayStats.setShopName(shop.getShopName());
//                shopRentDayStatsMap.put(e.getShopId(), shopDayStats);
//            }
//            shopDayStats.setAgentRefundInsuranceMoney(e.getAgentRefundInsuranceMoney());
//            shopDayStats.setAgentRefundInsuranceCount(e.getAgentRefundInsuranceCount());
//        }

        //门店租电统计保存（日总统计）
        for (ShopDayStats e : shopRentDayStatsMap.values()) {
            e.setStatsDate(statsDate);
            e.setCategory(ConstEnum.Category.RENT.getValue());
            Shop shop = getShop(e.getShopId());
            Agent agent = getAgent(shop.getAgentId());
            if(agent == null){
                continue;
            }
            e.setAgentId(agent.getId());
            e.setAgentName(agent.getAgentName());
            Partner partner = getPartner(agent.getPartnerId());
            e.setPartnerId(partner.getId());
            e.setPartnerName(partner.getPartnerName());

            ShopDayStats dayStats = shopDayStatsMapper.find(e.getShopId(), e.getStatsDate(), e.getCategory());
            if (dayStats == null) {
                shopDayStatsMapper.insert(e);
            } else {
                shopDayStatsMapper.update(e);
            }

            ShopTotalStats shopTotalStats = shopDayStatsMapper.sumTotal(e.getShopId(), e.getCategory());

            shopTotalStats.setCategory(e.getCategory());
            shopTotalStats.setShopId(e.getShopId());
            shopTotalStats.setShopName(e.getShopName());
            shopTotalStats.setAgentId(e.getAgentId());
            shopTotalStats.setAgentName(e.getAgentName());
            shopTotalStats.setPartnerId(e.getPartnerId());
            shopTotalStats.setPartnerName(e.getPartnerName());
            shopTotalStats.setUpdateTime(new Date());

            ShopTotalStats totalStats = shopTotalStatsMapper.find(e.getShopId(), e.getCategory());
            if (totalStats == null) {
                shopTotalStatsMapper.insert(shopTotalStats);
            } else {
                shopTotalStatsMapper.update(shopTotalStats);
            }
        }


        //运营公司换电日统计
        for (String agentCompanyId : agentCompanyMap.keySet()) {
            AgentCompanyDayStats agentCompanyDayStats = agentCompanyExchangeDayStatsMap.get(agentCompanyId);
            if (agentCompanyDayStats == null) {
                agentCompanyDayStats = new AgentCompanyDayStats();
                agentCompanyDayStats.init();
                AgentCompany agentCompany = agentCompanyMap.get(agentCompanyId);
                agentCompanyDayStats.setAgentCompanyId(agentCompanyId);
                agentCompanyDayStats.setAgentCompanyName(agentCompany.getCompanyName());
                agentCompanyExchangeDayStatsMap.put(agentCompanyId, agentCompanyDayStats);
            }

            //运营公司当日新增换电金额
            Double agentCompanyExchangeMoney = batteryOrderAllotMapper.sumAgentCompanyMoney(null, null, agentCompanyId, BatteryOrderAllot.ServiceType.INCOME.getValue(), IncomeRatioHistory.OrgType.AGENT_COMPANY.getValue(),  statsDate, suffix);

            //运营公司当日新增包时段订单
            Double agentCompanyPacketPeriodMoney = packetPeriodOrderAllotMapper.sumAgentCompanyMoney(null, null, agentCompanyId, PacketPeriodOrderAllot.ServiceType.INCOME.getValue(), IncomeRatioHistory.OrgType.AGENT_COMPANY.getValue(),  statsDate, suffix);

            //运营公司退款包时段订单
            Double agentCompanyRefundPacketPeriodMoney = packetPeriodOrderAllotMapper.sumAgentCompanyMoney(null, null, agentCompanyId, PacketPeriodOrderAllot.ServiceType.REFUND.getValue(),  IncomeRatioHistory.OrgType.AGENT_COMPANY.getValue(),  statsDate, suffix);

            //当日新增换电订单数
            Integer agentCompanyExchangeCount = batteryOrderAllotMapper.agentCompanyExchangeCount(null,null, null, agentCompanyId, BatteryOrderAllot.ServiceType.INCOME.getValue(), IncomeRatioHistory.OrgType.AGENT_COMPANY.getValue(), statsDate, suffix);

            //当日购买包时段订单次数
            Integer agentCompanyPacketPeriodOrderCount = packetPeriodOrderAllotMapper.agentCompanyPacketPeriodOrderCount(null,null, null, agentCompanyId, BatteryOrderAllot.ServiceType.INCOME.getValue(), IncomeRatioHistory.OrgType.AGENT_COMPANY.getValue(), statsDate, suffix);

            //当日退款包时段订单次数
            Integer agentCompanyRefundPacketPeriodCount = packetPeriodOrderAllotMapper.agentCompanyPacketPeriodOrderCount(null,null, null, agentCompanyId, BatteryOrderAllot.ServiceType.REFUND.getValue(), IncomeRatioHistory.OrgType.AGENT_COMPANY.getValue(), statsDate, suffix);

            //运营商维度统计
            Double agentPacketPeriodMoney = packetPeriodOrderAllotMapper.sumAgentCompanyAgentMoney(agentCompanyId, PacketPeriodOrderAllot.ServiceType.INCOME.getValue(), IncomeRatioHistory.OrgType.AGENT.getValue(), statsDate, suffix);
            Double agentRefundPacketPeriodMoney = packetPeriodOrderAllotMapper.sumAgentCompanyAgentMoney(agentCompanyId, PacketPeriodOrderAllot.ServiceType.REFUND.getValue(), IncomeRatioHistory.OrgType.AGENT.getValue(), statsDate, suffix);
            Integer agentPacketPeriodOrderCount = packetPeriodOrderAllotMapper.companyPacketPeriodOrderCount(agentCompanyId, BatteryOrderAllot.ServiceType.INCOME.getValue(), IncomeRatioHistory.OrgType.AGENT.getValue(), statsDate, suffix);
            Integer agentRefundPacketPeriodCount = packetPeriodOrderAllotMapper.companyPacketPeriodOrderCount(agentCompanyId, BatteryOrderAllot.ServiceType.REFUND.getValue(), IncomeRatioHistory.OrgType.AGENT.getValue(), statsDate, suffix);

            //保存数据
            agentCompanyDayStats.setMoney((int)Math.round(agentCompanyExchangeMoney + agentCompanyPacketPeriodMoney  - agentCompanyRefundPacketPeriodMoney));
            agentCompanyDayStats.setExchangeMoney((int)Math.round(agentCompanyExchangeMoney));
            agentCompanyDayStats.setPacketPeriodMoney((int)Math.round(agentCompanyPacketPeriodMoney));
            agentCompanyDayStats.setRefundPacketPeriodMoney((int)Math.round(agentCompanyRefundPacketPeriodMoney));

            agentCompanyDayStats.setOrderCount(agentCompanyExchangeCount);
            agentCompanyDayStats.setPacketPeriodCount(agentCompanyPacketPeriodOrderCount);
            agentCompanyDayStats.setRefundPacketPeriodCount(agentCompanyRefundPacketPeriodCount);

            //运营商维度统计
            agentCompanyDayStats.setAgentPacketPeriodMoney((int)Math.round(agentPacketPeriodMoney));
            agentCompanyDayStats.setAgentRefundPacketPeriodMoney((int)Math.round(agentRefundPacketPeriodMoney));
            agentCompanyDayStats.setAgentPacketPeriodCount(agentPacketPeriodOrderCount);
            agentCompanyDayStats.setAgentRefundPacketPeriodCount(agentRefundPacketPeriodCount);

        }

        //插入所有运营公司收入（押金）
        for (AgentCompanyDayStats e : agentCompanyForegiftList) {
            AgentCompanyDayStats agentCompanyDayStats= agentCompanyExchangeDayStatsMap.get(e.getAgentCompanyId());
            if (agentCompanyDayStats == null) {
                agentCompanyDayStats = new AgentCompanyDayStats();
                agentCompanyDayStats.init();
                AgentCompany agentCompany = agentCompanyMap.get(e.getAgentCompanyId());
                if(agentCompany == null){
                    continue;
                }
                agentCompanyDayStats.setAgentCompanyId(e.getAgentCompanyId());
                agentCompanyDayStats.setAgentCompanyName(agentCompany.getCompanyName());
                agentCompanyExchangeDayStatsMap.put(e.getAgentCompanyId(), agentCompanyDayStats);
            }
            agentCompanyDayStats.setAgentForegiftMoney(e.getAgentForegiftMoney());
            agentCompanyDayStats.setAgentForegiftCount(e.getAgentForegiftCount());
        }

        //插入所有运营公司收入（押金退款）
        for (AgentCompanyDayStats e : agentCompanyRefundForegiftList) {
            AgentCompanyDayStats agentCompanyDayStats = agentCompanyExchangeDayStatsMap.get(e.getAgentCompanyId());
            if (agentCompanyDayStats == null) {
                agentCompanyDayStats = new AgentCompanyDayStats();
                agentCompanyDayStats.init();
                AgentCompany agentCompany = agentCompanyMap.get(e.getAgentCompanyId());
                if(agentCompany == null){
                    continue;
                }
                agentCompanyDayStats.setAgentCompanyId(e.getAgentCompanyId());
                agentCompanyDayStats.setAgentCompanyName(agentCompany.getCompanyName());
                agentCompanyExchangeDayStatsMap.put(e.getAgentCompanyId(), agentCompanyDayStats);
            }
            agentCompanyDayStats.setAgentRefundForegiftMoney(e.getAgentRefundForegiftMoney());
            agentCompanyDayStats.setAgentRefundForegiftCount(e.getAgentRefundForegiftCount());
        }

        //运营公司换电统计保存（日总统计）
        for (AgentCompanyDayStats e : agentCompanyExchangeDayStatsMap.values()) {
            e.setStatsDate(statsDate);
            e.setCategory(ConstEnum.Category.EXCHANGE.getValue());
            AgentCompany agentCompany = getAgentCompany(e.getAgentCompanyId());
            Agent agent = getAgent(agentCompany.getAgentId());
            if(agent == null){
                continue;
            }
            e.setAgentId(agent.getId());
            e.setAgentName(agent.getAgentName());
            Partner partner = getPartner(agent.getPartnerId());
            e.setPartnerId(partner.getId());
            e.setPartnerName(partner.getPartnerName());

            AgentCompanyDayStats dayStats = agentCompanyDayStatsMapper.find(e.getAgentCompanyId(), e.getStatsDate(), e.getCategory());
            if (dayStats == null) {
                agentCompanyDayStatsMapper.insert(e);
            } else {
                agentCompanyDayStatsMapper.update(e);
            }

            AgentCompanyTotalStats agentCompanyTotalStats = agentCompanyDayStatsMapper.sumTotal(e.getAgentCompanyId(), e.getCategory());

            agentCompanyTotalStats.setCategory(e.getCategory());
            agentCompanyTotalStats.setAgentCompanyId(e.getAgentCompanyId());
            agentCompanyTotalStats.setAgentCompanyName(e.getAgentCompanyName());
            agentCompanyTotalStats.setAgentId(e.getAgentId());
            agentCompanyTotalStats.setAgentName(e.getAgentName());
            agentCompanyTotalStats.setPartnerId(e.getPartnerId());
            agentCompanyTotalStats.setPartnerName(e.getPartnerName());
            agentCompanyTotalStats.setUpdateTime(new Date());

            AgentCompanyTotalStats totalStats = agentCompanyTotalStatsMapper.find(e.getAgentCompanyId(), e.getCategory());
            if (totalStats == null) {
                agentCompanyTotalStatsMapper.insert(agentCompanyTotalStats);
            } else {
                agentCompanyTotalStatsMapper.update(agentCompanyTotalStats);
            }
        }

        //运营公司租电日统计
        for (String agentCompanyId : agentCompanyMap.keySet()) {
            AgentCompanyDayStats agentCompanyDayStats = agentCompanyRentDayStatsMap.get(agentCompanyId);
            if (agentCompanyDayStats == null) {
                agentCompanyDayStats = new AgentCompanyDayStats();
                agentCompanyDayStats.init();
                AgentCompany agentCompany = agentCompanyMap.get(agentCompanyId);
                if(agentCompany == null){
                    continue;
                }
                agentCompanyDayStats.setAgentCompanyId(agentCompanyId);
                agentCompanyDayStats.setAgentCompanyName(agentCompany.getCompanyName());
                agentCompanyRentDayStatsMap.put(agentCompanyId, agentCompanyDayStats);
            }

            //租电运营公司当日新增包时段订单
            Double rentPeriodMoney = rentPeriodOrderAllotMapper.sumAgentCompanyMoney(null,  agentCompanyId, PacketPeriodOrderAllot.ServiceType.INCOME.getValue(), IncomeRatioHistory.OrgType.AGENT_COMPANY.getValue(),  statsDate, suffix);

            //租电门店退款包时段订单
            Double refundRentPeriodMoney = rentPeriodOrderAllotMapper.sumAgentCompanyMoney(null,  agentCompanyId, PacketPeriodOrderAllot.ServiceType.REFUND.getValue(),  IncomeRatioHistory.OrgType.AGENT_COMPANY.getValue(),  statsDate, suffix);

            //租电当日购买包时段订单次数
            Integer rentPeriodOrderCount = rentPeriodOrderAllotMapper.agentCompanyRentPeriodOrderCount(null, null, agentCompanyId, BatteryOrderAllot.ServiceType.INCOME.getValue(), IncomeRatioHistory.OrgType.AGENT_COMPANY.getValue(), statsDate, suffix);

            //租电当日退款包时段订单次数
            Integer refundRentPeriodCount = rentPeriodOrderAllotMapper.agentCompanyRentPeriodOrderCount(null, null, agentCompanyId, BatteryOrderAllot.ServiceType.REFUND.getValue(), IncomeRatioHistory.OrgType.AGENT_COMPANY.getValue(), statsDate, suffix);

            //运营商维度统计
            Double agentPacketPeriodMoney = rentPeriodOrderAllotMapper.sumAgentCompanyAgentMoney(agentCompanyId, PacketPeriodOrderAllot.ServiceType.INCOME.getValue(), IncomeRatioHistory.OrgType.AGENT.getValue(),  statsDate, suffix);
            Double agentRefundPacketPeriodMoney = rentPeriodOrderAllotMapper.sumAgentCompanyAgentMoney(agentCompanyId, PacketPeriodOrderAllot.ServiceType.REFUND.getValue(), IncomeRatioHistory.OrgType.AGENT.getValue(), statsDate, suffix);
            Integer agentPacketPeriodOrderCount = rentPeriodOrderAllotMapper.companyRentPeriodOrderCount(agentCompanyId, BatteryOrderAllot.ServiceType.INCOME.getValue(), IncomeRatioHistory.OrgType.AGENT.getValue(), statsDate, suffix);
            Integer agentRefundPacketPeriodCount = rentPeriodOrderAllotMapper.companyRentPeriodOrderCount(agentCompanyId, BatteryOrderAllot.ServiceType.REFUND.getValue(), IncomeRatioHistory.OrgType.AGENT.getValue(), statsDate, suffix);


            //保存数据
            agentCompanyDayStats.setMoney((int)Math.round(rentPeriodMoney - refundRentPeriodMoney));
            agentCompanyDayStats.setPacketPeriodMoney((int)Math.round(rentPeriodMoney));
            agentCompanyDayStats.setRefundPacketPeriodMoney((int)Math.round(refundRentPeriodMoney));

            agentCompanyDayStats.setPacketPeriodCount(rentPeriodOrderCount);
            agentCompanyDayStats.setRefundPacketPeriodCount(refundRentPeriodCount);

            //运营商维度统计
            agentCompanyDayStats.setAgentPacketPeriodMoney((int)Math.round(agentPacketPeriodMoney));
            agentCompanyDayStats.setAgentRefundPacketPeriodMoney((int)Math.round(agentRefundPacketPeriodMoney));
            agentCompanyDayStats.setAgentPacketPeriodCount(agentPacketPeriodOrderCount);
            agentCompanyDayStats.setAgentRefundPacketPeriodCount(agentRefundPacketPeriodCount);

        }

        //插入所有运营公司收入（押金）
        for (AgentCompanyDayStats e : agentCompanyRentForegiftList) {
            AgentCompanyDayStats agentCompanyDayStats = agentCompanyRentDayStatsMap.get(e.getAgentCompanyId());
            if (agentCompanyDayStats == null) {
                agentCompanyDayStats = new AgentCompanyDayStats();
                agentCompanyDayStats.init();
                AgentCompany agentCompany = agentCompanyMap.get(e.getAgentCompanyId());
                if(agentCompany == null){
                    continue;
                }
                agentCompanyDayStats.setAgentCompanyId(e.getAgentCompanyId());
                agentCompanyDayStats.setAgentCompanyName(agentCompany.getCompanyName());
                agentCompanyRentDayStatsMap.put(e.getAgentCompanyId(), agentCompanyDayStats);
            }
            agentCompanyDayStats.setAgentForegiftMoney(e.getAgentForegiftMoney());
            agentCompanyDayStats.setAgentForegiftCount(e.getAgentForegiftCount());
        }

        //插入所有运营公司收入（押金退款）
        for (AgentCompanyDayStats e : agentCompanyRentRefundForegiftList) {
            AgentCompanyDayStats agentCompanyDayStats = agentCompanyRentDayStatsMap.get(e.getAgentCompanyId());
            if (agentCompanyDayStats == null) {
                agentCompanyDayStats = new AgentCompanyDayStats();
                agentCompanyDayStats.init();
                AgentCompany agentCompany = agentCompanyMap.get(e.getAgentCompanyId());
                if(agentCompany == null){
                    continue;
                }
                agentCompanyDayStats.setAgentCompanyId(e.getAgentCompanyId());
                agentCompanyDayStats.setAgentCompanyName(agentCompany.getCompanyName());
                agentCompanyRentDayStatsMap.put(e.getAgentCompanyId(), agentCompanyDayStats);
            }
            agentCompanyDayStats.setAgentRefundForegiftMoney(e.getAgentRefundForegiftMoney());
            agentCompanyDayStats.setAgentRefundForegiftCount(e.getAgentRefundForegiftCount());
        }

        //运营公司租电统计保存（日总统计）
        for (AgentCompanyDayStats e : agentCompanyRentDayStatsMap.values()) {
            e.setStatsDate(statsDate);
            e.setCategory(ConstEnum.Category.RENT.getValue());
            AgentCompany agentCompany = getAgentCompany(e.getAgentCompanyId());
            Agent agent = getAgent(agentCompany.getAgentId());
            if(agent == null){
                continue;
            }
            e.setAgentId(agent.getId());
            e.setAgentName(agent.getAgentName());
            Partner partner = getPartner(agent.getPartnerId());
            e.setPartnerId(partner.getId());
            e.setPartnerName(partner.getPartnerName());

            AgentCompanyDayStats dayStats = agentCompanyDayStatsMapper.find(e.getAgentCompanyId(), e.getStatsDate(), e.getCategory());
            if (dayStats == null) {
                agentCompanyDayStatsMapper.insert(e);
            } else {
                agentCompanyDayStatsMapper.update(e);
            }

            AgentCompanyTotalStats agentCompanyTotalStats = agentCompanyDayStatsMapper.sumTotal(e.getAgentCompanyId(), e.getCategory());

            agentCompanyTotalStats.setCategory(e.getCategory());
            agentCompanyTotalStats.setAgentCompanyId(e.getAgentCompanyId());
            agentCompanyTotalStats.setAgentCompanyName(e.getAgentCompanyName());
            agentCompanyTotalStats.setAgentId(e.getAgentId());
            agentCompanyTotalStats.setAgentName(e.getAgentName());
            agentCompanyTotalStats.setPartnerId(e.getPartnerId());
            agentCompanyTotalStats.setPartnerName(e.getPartnerName());
            agentCompanyTotalStats.setUpdateTime(new Date());

            AgentCompanyTotalStats totalStats = agentCompanyTotalStatsMapper.find(e.getAgentCompanyId(), e.getCategory());
            if (totalStats == null) {
                agentCompanyTotalStatsMapper.insert(agentCompanyTotalStats);
            } else {
                agentCompanyTotalStatsMapper.update(agentCompanyTotalStats);
            }
        }

        //运营商换电日统计
        for (Integer agentId : agentMap.keySet()) {
            AgentDayStats agentDayStats = agentExchangeDayStatsMap.get(agentId);
            if (agentDayStats == null) {
                agentDayStats = new AgentDayStats();
                agentDayStats.init();
                agentDayStats.setAgentId(agentId);
                agentExchangeDayStatsMap.put(agentId, agentDayStats);
            }

            List<Integer> orgTypeList = Arrays.asList(
                    IncomeRatioHistory.OrgType.AGENT.getValue(),
                    IncomeRatioHistory.OrgType.PROVINCE_AGENT.getValue(),
                    IncomeRatioHistory.OrgType.CITY_AGENT.getValue()
            );

            //当日换电金额(分成前)
            Double exchangeMoney = batteryOrderAllotMapper.sumOrderMoney(null, agentId, null, BatteryOrderAllot.ServiceType.INCOME.getValue(), IncomeRatioHistory.OrgType.AGENT.getValue(), statsDate, suffix);
            //运营商当日新增换电金额(分成后)
            Double agentExchangeMoney = batteryOrderAllotMapper.sumMoney(null, agentId, null, BatteryOrderAllot.ServiceType.INCOME.getValue(), orgTypeList, statsDate, suffix);
            //门店当日新增换电金额(分成后)
            Double shopExchangeMoney = batteryOrderAllotMapper.sumShopMoney(agentId, null, null, BatteryOrderAllot.ServiceType.INCOME.getValue(), IncomeRatioHistory.OrgType.SHOP.getValue(),  statsDate, suffix);
            //运营公司当日新增换电金额(分成后)
            Double agentCompanyExchangeMoney = batteryOrderAllotMapper.sumAgentCompanyMoney(agentId, null, null, BatteryOrderAllot.ServiceType.INCOME.getValue(), IncomeRatioHistory.OrgType.AGENT_COMPANY.getValue(),  statsDate, suffix);

            Double refundExchangeMoney = 0d;
            Double agentRefundExchangeMoney = 0d;

            //当日包时段订单(分成前)
            Double packetPeriodMoney = packetPeriodOrderAllotMapper.sumOrderMoney(null, agentId, null, PacketPeriodOrderAllot.ServiceType.INCOME.getValue(), IncomeRatioHistory.OrgType.AGENT.getValue(), statsDate, suffix);
            //运营商当日新增包时段订单(分成后)
            Double agentPacketPeriodMoney = packetPeriodOrderAllotMapper.sumMoney(null, agentId,null,  PacketPeriodOrderAllot.ServiceType.INCOME.getValue(),orgTypeList, statsDate, suffix);
            //门店当日新增包时段订单(分成后)
            Double shopPacketPeriodMoney = packetPeriodOrderAllotMapper.sumShopMoney(agentId, null, null, PacketPeriodOrderAllot.ServiceType.INCOME.getValue(), IncomeRatioHistory.OrgType.SHOP.getValue(),  statsDate, suffix);
            //运营公司当日新增包时段订单(分成后)
            Double agentCompanyPacketPeriodMoney = packetPeriodOrderAllotMapper.sumAgentCompanyMoney(agentId, null, null, PacketPeriodOrderAllot.ServiceType.INCOME.getValue(), IncomeRatioHistory.OrgType.AGENT_COMPANY.getValue(),  statsDate, suffix);

            //当日退款包时段订单(分成前)
            Double refundPacketPeriodMoney = packetPeriodOrderAllotMapper.sumOrderMoney(null, agentId, null, PacketPeriodOrderAllot.ServiceType.REFUND.getValue(), IncomeRatioHistory.OrgType.AGENT.getValue(), statsDate, suffix);
            //运营商退款包时段订单(分成后)
            Double agentRefundPacketPeriodMoney = packetPeriodOrderAllotMapper.sumMoney(null, agentId, null, PacketPeriodOrderAllot.ServiceType.REFUND.getValue(), orgTypeList, statsDate, suffix);
            //门店退款包时段订单(分成后)
            Double shopRefundPacketPeriodMoney = packetPeriodOrderAllotMapper.sumShopMoney(agentId, null, null, PacketPeriodOrderAllot.ServiceType.REFUND.getValue(),  IncomeRatioHistory.OrgType.SHOP.getValue(),  statsDate, suffix);
            //运营公司退款包时段订单(分成后)
            Double agentCompanyRefundPacketPeriodMoney = packetPeriodOrderAllotMapper.sumAgentCompanyMoney(agentId, null, null, PacketPeriodOrderAllot.ServiceType.REFUND.getValue(),  IncomeRatioHistory.OrgType.AGENT_COMPANY.getValue(),  statsDate, suffix);

            //当日新增换电订单数
            Integer exchangeCount = batteryOrderAllotMapper.exchangeCount(null, agentId,null, null,  BatteryOrderAllot.ServiceType.INCOME.getValue(), IncomeRatioHistory.OrgType.AGENT.getValue(), statsDate, suffix);
            //当日新增包时段类型订单数
            Integer packetPeriodCount = 0;
            for(Map packetPeriodCountMap : incrementPacketPeriodCount){
                if(packetPeriodCountMap.get("agentId") != null
                        && Integer.parseInt(packetPeriodCountMap.get("agentId").toString()) == agentId){
                    packetPeriodCount = Integer.parseInt(packetPeriodCountMap.get("orderCount").toString());
                    break;
                }
            }
            //当日购买包时段订单次数
            Integer packetPeriodOrderCount = packetPeriodOrderAllotMapper.packetPeriodOrderCount(null, agentId, null, null, BatteryOrderAllot.ServiceType.INCOME.getValue(), IncomeRatioHistory.OrgType.AGENT.getValue(), statsDate, suffix);
            /*订单次数*/
            Integer orderCount = exchangeCount;

            //当日新增退款换电订单数
            Integer refundExchangeCount = batteryOrderAllotMapper.exchangeCount(null, agentId, null, null,  BatteryOrderAllot.ServiceType.REFUND.getValue(), IncomeRatioHistory.OrgType.AGENT.getValue(), statsDate, suffix);
            //当日退款包时段订单次数
            Integer refundPacketPeriodOrderCount = packetPeriodOrderAllotMapper.packetPeriodOrderCount(null, agentId, null, null, BatteryOrderAllot.ServiceType.REFUND.getValue(), IncomeRatioHistory.OrgType.AGENT.getValue(), statsDate, suffix);


            /*平台收入*/
            Double platformIncome = batteryOrderAllotMapper.sumMoneyByOrgType(agentId, BatteryOrderAllot.ServiceType.INCOME.getValue(), IncomeRatioHistory.OrgType.PLATFORM.getValue(), statsDate, suffix);
            platformIncome += packetPeriodOrderAllotMapper.sumMoneyByOrgType(agentId, BatteryOrderAllot.ServiceType.INCOME.getValue(), IncomeRatioHistory.OrgType.PLATFORM.getValue(), statsDate, suffix);
            platformIncome -= batteryOrderAllotMapper.sumMoneyByOrgType(agentId, BatteryOrderAllot.ServiceType.REFUND.getValue(), IncomeRatioHistory.OrgType.PLATFORM.getValue(), statsDate, suffix);
            platformIncome -= packetPeriodOrderAllotMapper.sumMoneyByOrgType(agentId, BatteryOrderAllot.ServiceType.REFUND.getValue(), IncomeRatioHistory.OrgType.PLATFORM.getValue(), statsDate, suffix);
            /*省代收入*/
            Double provinceIncome = batteryOrderAllotMapper.sumMoneyByOrgType(agentId, BatteryOrderAllot.ServiceType.INCOME.getValue(), IncomeRatioHistory.OrgType.PROVINCE_AGENT.getValue(), statsDate, suffix);
            provinceIncome += packetPeriodOrderAllotMapper.sumMoneyByOrgType(agentId, BatteryOrderAllot.ServiceType.INCOME.getValue(), IncomeRatioHistory.OrgType.PROVINCE_AGENT.getValue(), statsDate, suffix);
            provinceIncome -= batteryOrderAllotMapper.sumMoneyByOrgType(agentId, BatteryOrderAllot.ServiceType.REFUND.getValue(), IncomeRatioHistory.OrgType.PROVINCE_AGENT.getValue(), statsDate, suffix);
            provinceIncome -= packetPeriodOrderAllotMapper.sumMoneyByOrgType(agentId, BatteryOrderAllot.ServiceType.REFUND.getValue(), IncomeRatioHistory.OrgType.PROVINCE_AGENT.getValue(), statsDate, suffix);
            /*市代收入*/
            Double cityIncome = batteryOrderAllotMapper.sumMoneyByOrgType(agentId, BatteryOrderAllot.ServiceType.INCOME.getValue(), IncomeRatioHistory.OrgType.CITY_AGENT.getValue(), statsDate, suffix);
            cityIncome += packetPeriodOrderAllotMapper.sumMoneyByOrgType(agentId, BatteryOrderAllot.ServiceType.INCOME.getValue(), IncomeRatioHistory.OrgType.CITY_AGENT.getValue(), statsDate, suffix);
            cityIncome -= batteryOrderAllotMapper.sumMoneyByOrgType(agentId, BatteryOrderAllot.ServiceType.REFUND.getValue(), IncomeRatioHistory.OrgType.CITY_AGENT.getValue(), statsDate, suffix);
            cityIncome -= packetPeriodOrderAllotMapper.sumMoneyByOrgType(agentId, BatteryOrderAllot.ServiceType.REFUND.getValue(), IncomeRatioHistory.OrgType.CITY_AGENT.getValue(), statsDate, suffix);


            //保存数据
            agentDayStats.setExchangeMoney((int)Math.round(exchangeMoney));//当日换电金额(分成前) 按分计算
            agentDayStats.setPacketPeriodMoney((int)Math.round(packetPeriodMoney.intValue()));
            agentDayStats.setRefundExchangeMoney((int)Math.round(refundExchangeMoney));
            agentDayStats.setRefundPacketPeriodMoney((int)Math.round(refundPacketPeriodMoney));

            agentDayStats.setAgentExchangeMoney((int)Math.round(agentExchangeMoney));//当日换电金额(分成后) 按分计算
            agentDayStats.setAgentPacketPeriodMoney((int)Math.round(agentPacketPeriodMoney));
            agentDayStats.setAgentRefundExchangeMoney((int)Math.round(agentRefundExchangeMoney));
            agentDayStats.setAgentRefundPacketPeriodMoney((int)Math.round(agentRefundPacketPeriodMoney));

            agentDayStats.setShopMoney((int)Math.round(shopExchangeMoney + shopPacketPeriodMoney - shopRefundPacketPeriodMoney));
            agentDayStats.setShopExchangeMoney((int)Math.round(shopExchangeMoney));//当日换电金额(分成后) 按分计算
            agentDayStats.setShopPacketPeriodMoney((int)Math.round(shopPacketPeriodMoney));
            agentDayStats.setShopRefundPacketPeriodMoney((int)Math.round(shopRefundPacketPeriodMoney));

            agentDayStats.setAgentCompanyMoney((int)Math.round(agentCompanyExchangeMoney + agentCompanyPacketPeriodMoney - agentCompanyRefundPacketPeriodMoney));
            agentDayStats.setAgentCompanyExchangeMoney((int)Math.round(agentCompanyExchangeMoney));//当日换电金额(分成后) 按分计算
            agentDayStats.setAgentCompanyPacketPeriodMoney((int)Math.round(agentCompanyPacketPeriodMoney));
            agentDayStats.setAgentCompanyRefundPacketPeriodMoney((int)Math.round(agentCompanyRefundPacketPeriodMoney));

            agentDayStats.setExchangeCount(exchangeCount);
            agentDayStats.setPacketPeriodCount(packetPeriodCount);
            agentDayStats.setPacketPeriodOrderCount(packetPeriodOrderCount);
            agentDayStats.setOrderCount(orderCount);
            agentDayStats.setRefundExchangeCount(refundExchangeCount);
            agentDayStats.setRefundPacketPeriodOrderCount(refundPacketPeriodOrderCount);
            agentDayStats.setPlatformIncome((int)Math.round(platformIncome));
            agentDayStats.setProvinceIncome((int)Math.round(provinceIncome));
            agentDayStats.setCityIncome((int)Math.round(cityIncome));


            //运营商当日新增换电金额(分成后) == 当日新增运营收入(income)
            agentDayStats.setIncome(agentDayStats.getAgentExchangeMoney() + agentDayStats.getAgentPacketPeriodMoney() - agentDayStats.getAgentRefundExchangeMoney() - agentDayStats.getAgentRefundPacketPeriodMoney()
            );
            agentDayStats.setMoney(agentDayStats.getExchangeMoney() + agentDayStats.getPacketPeriodMoney()- agentDayStats.getRefundExchangeMoney() - agentDayStats.getRefundPacketPeriodMoney()
            );
        }

        //运营商换电日统计（押金收入）
        for (Map map : agentForegiftList) {
            if (map.get("agentId") != null) {
                Integer agentId = Integer.parseInt(map.get("agentId").toString());
                Agent agent = getAgent(agentId);
                if(agent == null){
                    continue;
                }
                AgentDayStats agentDayStats = agentExchangeDayStatsMap.get(agentId);
                if (agentDayStats == null) {
                    agentDayStats = new AgentDayStats();
                    agentDayStats.init();
                    agentDayStats.setAgentId(agentId);
                    agentExchangeDayStatsMap.put(agentDayStats.getAgentId(), agentDayStats);
                }
                agentDayStats.setForegiftMoney(Integer.parseInt(map.get("incrementForegiftMoney").toString()));
                agentDayStats.setForegiftCount(Integer.parseInt(map.get("incrementForegiftCount").toString()));
            }
        }

        //运营商换电日统计（押金退款）
        for (Map map : agentForegiftRefundList) {
            if (map.get("agentId") != null) {
                Integer agentId = Integer.parseInt(map.get("agentId").toString());
                Agent agent = getAgent(agentId);
                if(agent == null){
                    continue;
                }
                AgentDayStats agentDayStats = agentExchangeDayStatsMap.get(agentId);
                if (agentDayStats == null) {
                    agentDayStats = new AgentDayStats();
                    agentDayStats.init();
                    agentDayStats.setAgentId(agentId);
                    agentExchangeDayStatsMap.put(agentDayStats.getAgentId(), agentDayStats);
                }
                agentDayStats.setForegiftRefundMoney(Integer.parseInt(map.get("foregiftRefundMoney").toString()));
                agentDayStats.setForegiftRefundCount(Integer.parseInt(map.get("foregiftRefundCount").toString()));
            }
        }

        //运营商换电日统计（保险收入）
        for (Map map : agentInsuranceList) {
            if (map.get("agentId") != null) {
                Integer agentId = Integer.parseInt(map.get("agentId").toString());
                Agent agent = getAgent(agentId);
                if(agent == null){
                    continue;
                }
                AgentDayStats agentDayStats = agentExchangeDayStatsMap.get(agentId);
                if (agentDayStats == null) {
                    agentDayStats = new AgentDayStats();
                    agentDayStats.init();
                    agentDayStats.setAgentId(agentId);
                    agentExchangeDayStatsMap.put(agentDayStats.getAgentId(), agentDayStats);
                }
                agentDayStats.setInsuranceMoney(Integer.parseInt(map.get("incrementInsuranceMoney").toString()));
                agentDayStats.setInsuranceCount(Integer.parseInt(map.get("incrementInsuranceCount").toString()));

                agentDayStats.setIncome(agentDayStats.getIncome() + agentDayStats.getInsuranceMoney());
                agentDayStats.setMoney(agentDayStats.getMoney()  + agentDayStats.getInsuranceMoney());
            }
        }

        //运营商换电日统计（保险退款）
        for (Map map : agentInsuranceRefundList) {
            if (map.get("agentId") != null) {
                Integer agentId = Integer.parseInt(map.get("agentId").toString());
                Agent agent = getAgent(agentId);
                if(agent == null){
                    continue;
                }
                AgentDayStats agentDayStats = agentExchangeDayStatsMap.get(agentId);
                if (agentDayStats == null) {
                    agentDayStats = new AgentDayStats();
                    agentDayStats.init();
                    agentDayStats.setAgentId(agentId);
                    agentExchangeDayStatsMap.put(agentDayStats.getAgentId(), agentDayStats);
                }
                agentDayStats.setInsuranceRefundMoney(Integer.parseInt(map.get("insuranceRefundMoney").toString()));
                agentDayStats.setInsuranceRefundCount(Integer.parseInt(map.get("insuranceRefundCount").toString()));

                agentDayStats.setIncome(agentDayStats.getIncome() - agentDayStats.getInsuranceRefundMoney());
                agentDayStats.setMoney(agentDayStats.getMoney()  - agentDayStats.getInsuranceRefundMoney());
            }
        }

        //运营商换电日统计（押金剩余金额 收入）
        for (Map map : agentForegiftRemainList) {
            if (map.get("agentId") != null) {
                Integer agentId = Integer.parseInt(map.get("agentId").toString());
                Agent agent = getAgent(agentId);
                if(agent == null){
                    continue;
                }
                AgentDayStats agentDayStats = agentExchangeDayStatsMap.get(agentId);
                if (agentDayStats == null) {
                    agentDayStats = new AgentDayStats();
                    agentDayStats.init();
                    agentDayStats.setAgentId(agentId);
                    agentExchangeDayStatsMap.put(agentDayStats.getAgentId(), agentDayStats);
                }
                agentDayStats.setForegiftRemainMoney(Integer.parseInt(map.get("remainMoney").toString()));
                agentDayStats.setMoney(agentDayStats.getMoney() + agentDayStats.getForegiftRemainMoney());
                agentDayStats.setIncome(agentDayStats.getIncome() + agentDayStats.getForegiftRemainMoney());
            }
        }

        //运营商换电日统计（抵扣券 支出）
        for (Map map : deductionTicketOrderList) {
            if (map.get("agentId") != null) {
                Integer agentId = Integer.parseInt(map.get("agentId").toString());
                Agent agent = getAgent(agentId);
                if(agent == null){
                    continue;
                }
                AgentDayStats agentDayStats = agentExchangeDayStatsMap.get(agentId);
                if (agentDayStats == null) {
                    agentDayStats = new AgentDayStats();
                    agentDayStats.init();
                    agentDayStats.setAgentId(agentId);
                    agentExchangeDayStatsMap.put(agentDayStats.getAgentId(), agentDayStats);
                }
                agentDayStats.setDeductionTicketMoney(Integer.parseInt(map.get("money").toString()));
                agentDayStats.setMoney(agentDayStats.getMoney() - agentDayStats.getDeductionTicketMoney());
                agentDayStats.setIncome(agentDayStats.getIncome() - agentDayStats.getDeductionTicketMoney());
            }
        }

        //运营商换电日统计（拉新 支出）
        for (Map map : laxinPayOrderList) {
            if (map.get("agentId") != null) {
                Integer agentId = Integer.parseInt(map.get("agentId").toString());
                Agent agent = getAgent(agentId);
                if(agent == null){
                    continue;
                }
                AgentDayStats agentDayStats = agentExchangeDayStatsMap.get(agentId);
                if (agentDayStats == null) {
                    agentDayStats = new AgentDayStats();
                    agentDayStats.init();
                    agentDayStats.setAgentId(agentId);
                    agentExchangeDayStatsMap.put(agentDayStats.getAgentId(), agentDayStats);
                }
                agentDayStats.setLaxinPayMoney(Integer.parseInt(map.get("money").toString()));
            }
        }

        //运营商换电日统计（电价）
        for (CabinetDayStats e : cabinetDayStatsMap.values()) {
            AgentDayStats agentDayStats = agentExchangeDayStatsMap.get(e.getAgentId());
            if (agentDayStats == null) {
                agentDayStats = new AgentDayStats();
                agentDayStats.init();
                agentDayStats.setAgentId(e.getAgentId());
                agentExchangeDayStatsMap.put(agentDayStats.getAgentId(), agentDayStats);
            }
            agentDayStats.setElectricDegree(agentDayStats.getElectricDegree() + e.getElectricDegree());
            agentDayStats.setElectricPrice(agentDayStats.getElectricPrice() + e.getElectricPrice());
        }

        //运营商换电日统计（活跃用户数）
        for (AgentDayStats e : agentActiveCustomerList) {
            AgentDayStats agentDayStats = agentExchangeDayStatsMap.get(e.getAgentId());
            if (agentDayStats == null) {
                agentDayStats = new AgentDayStats();
                agentDayStats.init();
                agentDayStats.setAgentId(e.getAgentId());
                agentExchangeDayStatsMap.put(agentDayStats.getAgentId(), agentDayStats);
            }
            agentDayStats.setActiveCustomerCount(e.getActiveCustomerCount());
        }

        //运营商换电日统计（设备数）
        for (AgentDayStats e : agentCabinetList) {
            AgentDayStats agentDayStats = agentExchangeDayStatsMap.get(e.getAgentId());
            if (agentDayStats == null) {
                agentDayStats = new AgentDayStats();
                agentDayStats.init();
                agentDayStats.setAgentId(e.getAgentId());
                agentExchangeDayStatsMap.put(agentDayStats.getAgentId(), agentDayStats);
            }
            agentDayStats.setCabinetCount(e.getCabinetCount());
        }

        //运营商换电日统计（电池数）
        for (AgentDayStats e : agentExchangeBatteryList) {
            AgentDayStats agentDayStats = agentExchangeDayStatsMap.get(e.getAgentId());
            if (agentDayStats == null) {
                agentDayStats = new AgentDayStats();
                agentDayStats.init();
                agentDayStats.setAgentId(e.getAgentId());
                agentExchangeDayStatsMap.put(agentDayStats.getAgentId(), agentDayStats);
            }
            agentDayStats.setBatteryCount(e.getBatteryCount());
        }

        //运营商日统计(日月总保存)
        for (AgentDayStats e : agentExchangeDayStatsMap.values()) {
            Agent agent = getAgent(e.getAgentId());
            if(agent == null){
                continue;
            }
            e.setStatsDate(statsDate);
            e.setCategory(ConstEnum.Category.EXCHANGE.getValue());
            e.setAgentId(e.getAgentId());
            e.setAgentName(agent.getAgentName());
            Partner partner = getPartner(agent.getPartnerId());
            e.setPartnerId(partner.getId());
            e.setPartnerName(partner.getPartnerName());
            e.setSuffix(suffix);
            e.setUpdateTime(new Date());
            if (agentDayStatsMapper.find(e.getAgentId(), statsDate, e.getCategory()) == null) {
                agentDayStatsMapper.insert(e);
            } else {
                agentDayStatsMapper.update(e);
            }

            AgentMonthStats agentMonthStats = agentDayStatsMapper.sumMonth(e.getAgentId(), statsMonth, e.getCategory());
            agentMonthStats.setPartnerId(e.getPartnerId());
            agentMonthStats.setPartnerName(e.getPartnerName());
            agentMonthStats.setAgentId(e.getAgentId());
            agentMonthStats.setAgentName(agent.getAgentName());
            agentMonthStats.setStatsMonth(statsMonth);
            agentMonthStats.setCategory(ConstEnum.Category.EXCHANGE.getValue());
            agentMonthStats.setActiveCustomerCount(e.getActiveCustomerCount());
            agentMonthStats.setCabinetCount(e.getCabinetCount());
            agentMonthStats.setBatteryCount(e.getBatteryCount());
            agentMonthStats.setUpdateTime(new Date());

            //支出统计
            AgentMaterialDayStats agentMaterialDayStats =  agentMaterialDayStatsMapper.sumMonth(e.getAgentId(), statsMonth, e.getCategory());
            if(agentMaterialDayStats != null){
                agentMonthStats.setCabinetForegiftMoney(agentMaterialDayStats.getCabinetForegiftMoney());
                agentMonthStats.setCabinetRentMoney(agentMaterialDayStats.getCabinetRentMoney());
                agentMonthStats.setBatteryRentMoney(agentMaterialDayStats.getBatteryRentMoney());
                agentMonthStats.setIdCardAuthMoney(agentMaterialDayStats.getIdCardAuthMoney());
            }else{
                agentMonthStats.setCabinetForegiftMoney(0);
                agentMonthStats.setCabinetRentMoney(0);
                agentMonthStats.setBatteryRentMoney(0);
                agentMonthStats.setIdCardAuthMoney(0);
            }

            AgentMonthStats monthStats = agentMonthStatsMapper.find(e.getAgentId(), statsMonth, e.getCategory());
            if (monthStats == null) {
                agentMonthStatsMapper.insert(agentMonthStats);
            } else {
                agentMonthStatsMapper.update(agentMonthStats);
            }

            AgentTotalStats agentTotalStats = agentDayStatsMapper.sumTotal(e.getAgentId(), e.getCategory());

            //结算人均电费 设备人均电费/设备数
            int perElectric = cabinetTotalStatsMapper.perElectric(e.getAgentId());
            agentTotalStats.setPerElectric(perElectric);

            agentTotalStats.setPartnerId(e.getPartnerId());
            agentTotalStats.setPartnerName(e.getPartnerName());
            agentTotalStats.setAgentId(e.getAgentId());
            agentTotalStats.setAgentName(agent.getAgentName());
            agentTotalStats.setCategory(e.getCategory());
            agentTotalStats.setCabinetCount(e.getCabinetCount());
            agentTotalStats.setBatteryCount(e.getBatteryCount());
            agentTotalStats.setUpdateTime(new Date());

            //支出统计
            agentMaterialDayStats =  agentMaterialDayStatsMapper.sumTotal(e.getAgentId(), e.getCategory());
            if(agentMaterialDayStats != null){
                agentTotalStats.setCabinetForegiftMoney(agentMaterialDayStats.getCabinetForegiftMoney());
                agentTotalStats.setCabinetRentMoney(agentMaterialDayStats.getCabinetRentMoney());
                agentTotalStats.setBatteryRentMoney(agentMaterialDayStats.getBatteryRentMoney());
                agentTotalStats.setIdCardAuthMoney(agentMaterialDayStats.getIdCardAuthMoney());
            }else{
                agentTotalStats.setCabinetForegiftMoney(0);
                agentTotalStats.setCabinetRentMoney(0);
                agentTotalStats.setBatteryRentMoney(0);
                agentTotalStats.setIdCardAuthMoney(0);
            }

            AgentTotalStats totalStats = agentTotalStatsMapper.find(e.getAgentId(), e.getCategory());
            if (totalStats == null) {
                agentTotalStatsMapper.insert(agentTotalStats);
            } else {
                agentTotalStatsMapper.update(agentTotalStats);
            }
        }

        //运营商租电日统计
        for (Integer agentId : agentMap.keySet()) {
            AgentDayStats agentDayStats = agentRentDayStatsMap.get(agentId);
            if (agentDayStats == null) {
                agentDayStats = new AgentDayStats();
                agentDayStats.init();
                agentDayStats.setAgentId(agentId);
                agentRentDayStatsMap.put(agentId, agentDayStats);
            }

            List<Integer> orgTypeList = Arrays.asList(
                    IncomeRatioHistory.OrgType.AGENT.getValue(),
                    IncomeRatioHistory.OrgType.PROVINCE_AGENT.getValue(),
                    IncomeRatioHistory.OrgType.CITY_AGENT.getValue()
            );

            //当日租电包时段订单(分成前)
            Double rentPeriodMoney = rentPeriodOrderAllotMapper.sumOrderMoney(null, agentId, RentPeriodOrderAllot.ServiceType.INCOME.getValue(), IncomeRatioHistory.OrgType.AGENT.getValue(), statsDate, suffix);
            //运营商租电当日新增包时段订单(分成后)
            Double agentRentPeriodMoney = rentPeriodOrderAllotMapper.sumMoney(null, agentId, RentPeriodOrderAllot.ServiceType.INCOME.getValue(),orgTypeList, statsDate, suffix);
            //门店当日租电新增包时段订单(分成后)
            Double shopRentPeriodMoney = rentPeriodOrderAllotMapper.sumShopMoney(agentId, null, RentPeriodOrderAllot.ServiceType.INCOME.getValue(), IncomeRatioHistory.OrgType.SHOP.getValue(),  statsDate, suffix);
            //运营公司当日租电新增包时段订单(分成后)
            Double agentCompanyRentPeriodMoney = rentPeriodOrderAllotMapper.sumAgentCompanyMoney(agentId, null, RentPeriodOrderAllot.ServiceType.INCOME.getValue(), IncomeRatioHistory.OrgType.AGENT_COMPANY.getValue(),  statsDate, suffix);

            //当日租电退款包时段订单(分成前)
            Double refundRentPeriodMoney = rentPeriodOrderAllotMapper.sumOrderMoney(null, agentId, RentPeriodOrderAllot.ServiceType.REFUND.getValue(), IncomeRatioHistory.OrgType.AGENT.getValue(), statsDate, suffix);
            //运营商租电退款包时段订单(分成后)
            Double agentRefundRentPeriodMoney = rentPeriodOrderAllotMapper.sumMoney(null, agentId, RentPeriodOrderAllot.ServiceType.REFUND.getValue(), orgTypeList, statsDate, suffix);
            //门店租电退款包时段订单(分成后)
            Double shopRefundRentPeriodMoney = rentPeriodOrderAllotMapper.sumShopMoney(agentId, null, RentPeriodOrderAllot.ServiceType.REFUND.getValue(),  IncomeRatioHistory.OrgType.SHOP.getValue(),  statsDate, suffix);
            //运营公司租电退款包时段订单(分成后)
            Double agentCompanyRefundRentPeriodMoney = rentPeriodOrderAllotMapper.sumAgentCompanyMoney(agentId, null, RentPeriodOrderAllot.ServiceType.REFUND.getValue(),  IncomeRatioHistory.OrgType.AGENT_COMPANY.getValue(),  statsDate, suffix);

            //当日租电购买包时段订单次数
            Integer rentPeriodOrderCount = rentPeriodOrderAllotMapper.rentPeriodOrderCount(null, agentId, null, BatteryOrderAllot.ServiceType.INCOME.getValue(), IncomeRatioHistory.OrgType.AGENT.getValue(), statsDate, suffix);
            //当日租电退款包时段订单次数
            Integer refundRentPeriodOrderCount = rentPeriodOrderAllotMapper.rentPeriodOrderCount(null, agentId, null, BatteryOrderAllot.ServiceType.REFUND.getValue(), IncomeRatioHistory.OrgType.AGENT.getValue(), statsDate, suffix);

            /*租电平台收入*/
            Double rentPlatformIncome =  rentPeriodOrderAllotMapper.sumMoneyByOrgType(agentId, BatteryOrderAllot.ServiceType.INCOME.getValue(), IncomeRatioHistory.OrgType.PLATFORM.getValue(), statsDate, suffix);
            rentPlatformIncome -= rentPeriodOrderAllotMapper.sumMoneyByOrgType(agentId, BatteryOrderAllot.ServiceType.REFUND.getValue(), IncomeRatioHistory.OrgType.PLATFORM.getValue(), statsDate, suffix);
            /*租电省代收入*/
            Double rentProvinceIncome =  rentPeriodOrderAllotMapper.sumMoneyByOrgType(agentId, BatteryOrderAllot.ServiceType.INCOME.getValue(), IncomeRatioHistory.OrgType.PROVINCE_AGENT.getValue(), statsDate, suffix);
            rentProvinceIncome -= rentPeriodOrderAllotMapper.sumMoneyByOrgType(agentId, BatteryOrderAllot.ServiceType.REFUND.getValue(), IncomeRatioHistory.OrgType.PROVINCE_AGENT.getValue(), statsDate, suffix);
            /*租电市代收入*/
            Double rentCityIncome = rentPeriodOrderAllotMapper.sumMoneyByOrgType(agentId, BatteryOrderAllot.ServiceType.INCOME.getValue(), IncomeRatioHistory.OrgType.CITY_AGENT.getValue(), statsDate, suffix);
            rentCityIncome -= rentPeriodOrderAllotMapper.sumMoneyByOrgType(agentId, BatteryOrderAllot.ServiceType.REFUND.getValue(), IncomeRatioHistory.OrgType.CITY_AGENT.getValue(), statsDate, suffix);


            //保存数据
            agentDayStats.setPacketPeriodMoney((int)Math.round(rentPeriodMoney));
            agentDayStats.setRefundPacketPeriodMoney((int)Math.round(refundRentPeriodMoney));

            agentDayStats.setAgentPacketPeriodMoney((int)Math.round(agentRentPeriodMoney));
            agentDayStats.setAgentRefundPacketPeriodMoney((int)Math.round(agentRefundRentPeriodMoney));

            agentDayStats.setShopMoney((int)Math.round(shopRentPeriodMoney - shopRefundRentPeriodMoney));
            agentDayStats.setShopPacketPeriodMoney((int)Math.round(shopRentPeriodMoney));
            agentDayStats.setShopRefundPacketPeriodMoney((int)Math.round(shopRefundRentPeriodMoney));

            agentDayStats.setAgentCompanyMoney((int) Math.round(agentCompanyRentPeriodMoney - agentCompanyRefundRentPeriodMoney));
            agentDayStats.setAgentCompanyPacketPeriodMoney((int) Math.round(agentCompanyRentPeriodMoney));
            agentDayStats.setAgentCompanyRefundPacketPeriodMoney((int) Math.round(agentCompanyRefundRentPeriodMoney));

            agentDayStats.setPacketPeriodOrderCount(rentPeriodOrderCount);
            agentDayStats.setRefundPacketPeriodOrderCount(refundRentPeriodOrderCount);

            agentDayStats.setPlatformIncome((int)Math.round(rentPlatformIncome));
            agentDayStats.setProvinceIncome((int)Math.round(rentProvinceIncome));
            agentDayStats.setCityIncome((int)Math.round(rentCityIncome));

            //运营商当日新增换电金额(分成后) == 当日新增运营收入(income)
            agentDayStats.setIncome(agentDayStats.getAgentPacketPeriodMoney() - agentDayStats.getAgentRefundPacketPeriodMoney());
            agentDayStats.setMoney(agentDayStats.getPacketPeriodMoney() - agentDayStats.getRefundPacketPeriodMoney() );
        }

        //运营商租电日统计（押金收入）
        for (Map map : agentRentForegiftList) {
            if (map.get("agentId") != null) {
                Integer agentId = Integer.parseInt(map.get("agentId").toString());
                Agent agent = getAgent(agentId);
                if(agent == null){
                    continue;
                }
                AgentDayStats agentDayStats = agentRentDayStatsMap.get(agentId);
                if (agentDayStats == null) {
                    agentDayStats = new AgentDayStats();
                    agentDayStats.init();
                    agentDayStats.setAgentId(agentId);
                    agentRentDayStatsMap.put(agentDayStats.getAgentId(), agentDayStats);
                }
                agentDayStats.setForegiftMoney(Integer.parseInt(map.get("incrementForegiftMoney").toString()));
                agentDayStats.setForegiftCount(Integer.parseInt(map.get("incrementForegiftCount").toString()));
            }
        }

        //运营商租电日统计（押金退款）
        for (Map map : agentRentForegiftRefundList) {
            if (map.get("agentId") != null) {
                Integer agentId = Integer.parseInt(map.get("agentId").toString());
                Agent agent = getAgent(agentId);
                if(agent == null){
                    continue;
                }
                AgentDayStats agentDayStats = agentRentDayStatsMap.get(agentId);
                if (agentDayStats == null) {
                    agentDayStats = new AgentDayStats();
                    agentDayStats.init();
                    agentDayStats.setAgentId(agentId);
                    agentRentDayStatsMap.put(agentDayStats.getAgentId(), agentDayStats);
                }
                agentDayStats.setForegiftRefundMoney(Integer.parseInt(map.get("foregiftRefundMoney").toString()));
                agentDayStats.setForegiftRefundCount(Integer.parseInt(map.get("foregiftRefundCount").toString()));
            }
        }

        //运营商租电日统计（保险收入）
        for (Map map : agentRentInsuranceList) {
            if (map.get("agentId") != null) {
                Integer agentId = Integer.parseInt(map.get("agentId").toString());
                Agent agent = getAgent(agentId);
                if(agent == null){
                    continue;
                }
                AgentDayStats agentDayStats = agentRentDayStatsMap.get(agentId);
                if (agentDayStats == null) {
                    agentDayStats = new AgentDayStats();
                    agentDayStats.init();
                    agentDayStats.setAgentId(agentId);
                    agentRentDayStatsMap.put(agentDayStats.getAgentId(), agentDayStats);
                }
                agentDayStats.setInsuranceMoney(Integer.parseInt(map.get("incrementInsuranceMoney").toString()));
                agentDayStats.setInsuranceCount(Integer.parseInt(map.get("incrementInsuranceCount").toString()));

                agentDayStats.setIncome(agentDayStats.getIncome() + agentDayStats.getInsuranceMoney());
                agentDayStats.setMoney(agentDayStats.getMoney()  + agentDayStats.getInsuranceMoney());
            }
        }

        //运营商租电日统计（保险退款）
        for (Map map : agentRentInsuranceRefundList) {
            if (map.get("agentId") != null) {
                Integer agentId = Integer.parseInt(map.get("agentId").toString());
                Agent agent = getAgent(agentId);
                if(agent == null){
                    continue;
                }
                AgentDayStats agentDayStats = agentRentDayStatsMap.get(agentId);
                if (agentDayStats == null) {
                    agentDayStats = new AgentDayStats();
                    agentDayStats.init();
                    agentDayStats.setAgentId(agentId);
                    agentRentDayStatsMap.put(agentDayStats.getAgentId(), agentDayStats);
                }
                agentDayStats.setInsuranceRefundMoney(Integer.parseInt(map.get("insuranceRefundMoney").toString()));
                agentDayStats.setInsuranceRefundCount(Integer.parseInt(map.get("insuranceRefundCount").toString()));

                agentDayStats.setIncome(agentDayStats.getIncome() - agentDayStats.getInsuranceRefundMoney());
                agentDayStats.setMoney(agentDayStats.getMoney()  - agentDayStats.getInsuranceRefundMoney());
            }
        }

        //运营商租电日统计（押金剩余金额 收入）
        for (Map map : rentForegiftRemainList) {
            if (map.get("agentId") != null) {
                Integer agentId = Integer.parseInt(map.get("agentId").toString());
                Agent agent = getAgent(agentId);
                if(agent == null){
                    continue;
                }
                AgentDayStats agentDayStats = agentRentDayStatsMap.get(agentId);
                if (agentDayStats == null) {
                    agentDayStats = new AgentDayStats();
                    agentDayStats.init();
                    agentDayStats.setAgentId(agentId);
                    agentRentDayStatsMap.put(agentDayStats.getAgentId(), agentDayStats);
                }
                agentDayStats.setForegiftRemainMoney(Integer.parseInt(map.get("remainMoney").toString()));
                agentDayStats.setMoney(agentDayStats.getMoney() + agentDayStats.getForegiftRemainMoney());
                agentDayStats.setIncome(agentDayStats.getIncome() + agentDayStats.getForegiftRemainMoney());
            }
        }

        //运营商租电日统计（抵扣券 支出）
        for (Map map : rentTicketOrderList) {
            if (map.get("agentId") != null) {
                Integer agentId = Integer.parseInt(map.get("agentId").toString());
                Agent agent = getAgent(agentId);
                if(agent == null){
                    continue;
                }
                AgentDayStats agentDayStats = agentRentDayStatsMap.get(agentId);
                if (agentDayStats == null) {
                    agentDayStats = new AgentDayStats();
                    agentDayStats.init();
                    agentDayStats.setAgentId(agentId);
                    agentRentDayStatsMap.put(agentDayStats.getAgentId(), agentDayStats);
                }
                agentDayStats.setDeductionTicketMoney(Integer.parseInt(map.get("money").toString()));
                agentDayStats.setMoney(agentDayStats.getMoney() - agentDayStats.getDeductionTicketMoney());
                agentDayStats.setIncome(agentDayStats.getIncome() - agentDayStats.getDeductionTicketMoney());
            }
        }


        //运营商租电日统计（电池数）
        for (AgentDayStats e : agentRentBatteryList) {
            AgentDayStats agentDayStats = agentRentDayStatsMap.get(e.getAgentId());
            if (agentDayStats == null) {
                agentDayStats = new AgentDayStats();
                agentDayStats.init();
                agentDayStats.setAgentId(e.getAgentId());
                agentRentDayStatsMap.put(agentDayStats.getAgentId(), agentDayStats);
            }
            agentDayStats.setBatteryCount(e.getBatteryCount());
        }


        //运营商租电日统计(日月总保存)
        for (AgentDayStats e : agentRentDayStatsMap.values()) {
            Agent agent = getAgent(e.getAgentId());
            e.setStatsDate(statsDate);
            e.setCategory(ConstEnum.Category.RENT.getValue());
            e.setAgentId(e.getAgentId());
            e.setAgentName(agent.getAgentName());
            Partner partner = getPartner(agent.getPartnerId());
            e.setPartnerId(partner.getId());
            e.setPartnerName(partner.getPartnerName());
            e.setSuffix(suffix);
            e.setUpdateTime(new Date());
            if (agentDayStatsMapper.find(e.getAgentId(), statsDate, e.getCategory()) == null) {
                agentDayStatsMapper.insert(e);
            } else {
                agentDayStatsMapper.update(e);
            }

            AgentMonthStats agentMonthStats = agentDayStatsMapper.sumMonth(e.getAgentId(), statsMonth, e.getCategory());
            agentMonthStats.setPartnerId(e.getPartnerId());
            agentMonthStats.setPartnerName(e.getPartnerName());
            agentMonthStats.setAgentId(e.getAgentId());
            agentMonthStats.setAgentName(agent.getAgentName());
            agentMonthStats.setStatsMonth(statsMonth);
            agentMonthStats.setCategory(ConstEnum.Category.RENT.getValue());
            agentMonthStats.setActiveCustomerCount(e.getActiveCustomerCount());
            agentMonthStats.setCabinetCount(e.getCabinetCount());
            agentMonthStats.setBatteryCount(e.getBatteryCount());
            agentMonthStats.setUpdateTime(new Date());

            //支出统计
            AgentMaterialDayStats agentMaterialDayStats =  agentMaterialDayStatsMapper.sumMonth(e.getAgentId(), statsMonth, e.getCategory());
            if(agentMaterialDayStats != null){
                agentMonthStats.setCabinetForegiftMoney(agentMaterialDayStats.getCabinetForegiftMoney());
                agentMonthStats.setCabinetRentMoney(agentMaterialDayStats.getCabinetRentMoney());
                agentMonthStats.setBatteryRentMoney(agentMaterialDayStats.getBatteryRentMoney());
                agentMonthStats.setIdCardAuthMoney(agentMaterialDayStats.getIdCardAuthMoney());
            }else{
                agentMonthStats.setCabinetForegiftMoney(0);
                agentMonthStats.setCabinetRentMoney(0);
                agentMonthStats.setBatteryRentMoney(0);
                agentMonthStats.setIdCardAuthMoney(0);
            }

            AgentMonthStats monthStats = agentMonthStatsMapper.find(e.getAgentId(), statsMonth, e.getCategory());
            if (monthStats == null) {
                agentMonthStatsMapper.insert(agentMonthStats);
            } else {
                agentMonthStatsMapper.update(agentMonthStats);
            }

            AgentTotalStats agentTotalStats = agentDayStatsMapper.sumTotal(e.getAgentId(), e.getCategory());
            agentTotalStats.setPartnerId(e.getPartnerId());
            agentTotalStats.setPartnerName(e.getPartnerName());
            agentTotalStats.setAgentId(e.getAgentId());
            agentTotalStats.setAgentName(agent.getAgentName());
            agentTotalStats.setCategory(e.getCategory());
            agentTotalStats.setCabinetCount(e.getCabinetCount());
            agentTotalStats.setBatteryCount(e.getBatteryCount());
            agentTotalStats.setUpdateTime(new Date());

            //支出统计
            agentMaterialDayStats =  agentMaterialDayStatsMapper.sumTotal(e.getAgentId(), e.getCategory());
            if(agentMaterialDayStats != null){
                agentTotalStats.setCabinetForegiftMoney(agentMaterialDayStats.getCabinetForegiftMoney());
                agentTotalStats.setCabinetRentMoney(agentMaterialDayStats.getCabinetRentMoney());
                agentTotalStats.setBatteryRentMoney(agentMaterialDayStats.getBatteryRentMoney());
                agentTotalStats.setIdCardAuthMoney(agentMaterialDayStats.getIdCardAuthMoney());
            }else{
                agentTotalStats.setCabinetForegiftMoney(0);
                agentTotalStats.setCabinetRentMoney(0);
                agentTotalStats.setBatteryRentMoney(0);
                agentTotalStats.setIdCardAuthMoney(0);
            }

            AgentTotalStats totalStats = agentTotalStatsMapper.find(e.getAgentId(), e.getCategory());
            if (totalStats == null) {
                agentTotalStatsMapper.insert(agentTotalStats);
            } else {
                agentTotalStatsMapper.update(agentTotalStats);
            }
        }


        //商户换电日统计
        for (Integer partnerId : partnerMap.keySet()) {
            PartnerDayStats partnerDayStats = partnerExchangeDayStatsMap.get(partnerId);
            if (partnerDayStats == null) {
                partnerDayStats = new PartnerDayStats();
                partnerDayStats.init();
                Partner partner = partnerMap.get(partnerId);
                partnerDayStats.setPartnerId(partnerId);
                partnerDayStats.setPartnerName(partner.getPartnerName());
                partnerExchangeDayStatsMap.put(partnerId, partnerDayStats);
            }

            List<Integer> orgTypeList = Arrays.asList(
                    IncomeRatioHistory.OrgType.PLATFORM.getValue()
            );

            //当日换电金额(分成前)
            Double exchangeMoney = batteryOrderAllotMapper.sumOrderMoney(partnerId, null, null, BatteryOrderAllot.ServiceType.INCOME.getValue(), IncomeRatioHistory.OrgType.AGENT.getValue(), statsDate, suffix);
            //商户当日新增换电金额(分成后)
            Double partnerExchangeMoney = batteryOrderAllotMapper.sumMoney(partnerId, null, null, BatteryOrderAllot.ServiceType.INCOME.getValue(), orgTypeList,statsDate, suffix);

            //当日包时段订单(分成前)
            Double packetPeriodMoney = packetPeriodOrderAllotMapper.sumOrderMoney(partnerId, null, null, PacketPeriodOrderAllot.ServiceType.INCOME.getValue(), IncomeRatioHistory.OrgType.AGENT.getValue(), statsDate, suffix);
            //运营商当日新增包时段订单(分成后)
            Double partnerPacketPeriodMoney = packetPeriodOrderAllotMapper.sumMoney(partnerId, null,null,  PacketPeriodOrderAllot.ServiceType.INCOME.getValue(), orgTypeList, statsDate, suffix);

            //当日退款包时段订单(分成前)
            Double refundPacketPeriodMoney = packetPeriodOrderAllotMapper.sumOrderMoney(partnerId, null, null, PacketPeriodOrderAllot.ServiceType.REFUND.getValue(), IncomeRatioHistory.OrgType.AGENT.getValue(), statsDate, suffix);
            //运营商退款包时段订单(分成后)
            Double partnerRefundPacketPeriodMoney = packetPeriodOrderAllotMapper.sumMoney(partnerId, null, null, PacketPeriodOrderAllot.ServiceType.REFUND.getValue(), orgTypeList, statsDate, suffix);

            //当日新增换电订单数
            Integer exchangeCount = batteryOrderAllotMapper.exchangeCount(partnerId, null,null, null, BatteryOrderAllot.ServiceType.INCOME.getValue(), IncomeRatioHistory.OrgType.AGENT.getValue(), statsDate, suffix);
            /*订单次数*/
            Integer orderCount = exchangeCount;

            //当日购买包时段订单次数
            Integer packetPeriodOrderCount = packetPeriodOrderAllotMapper.packetPeriodOrderCount(partnerId, null, null, null, BatteryOrderAllot.ServiceType.INCOME.getValue(), IncomeRatioHistory.OrgType.AGENT.getValue(), statsDate, suffix);

            //当日退款包时段订单次数
            Integer refundPacketPeriodOrderCount = packetPeriodOrderAllotMapper.packetPeriodOrderCount(partnerId, null, null, null, BatteryOrderAllot.ServiceType.REFUND.getValue(), IncomeRatioHistory.OrgType.AGENT.getValue(), statsDate, suffix);

            //保存数据
            partnerDayStats.setExchangeMoney((int)Math.round(exchangeMoney));//当日换电金额(分成前) 按分计算
            partnerDayStats.setPacketPeriodMoney((int)Math.round(packetPeriodMoney));
            partnerDayStats.setRefundPacketPeriodMoney((int)Math.round(refundPacketPeriodMoney));

            partnerDayStats.setPartnerExchangeMoney((int)Math.round(partnerExchangeMoney));//当日换电金额(分成后) 按分计算
            partnerDayStats.setPartnerPacketPeriodMoney((int)Math.round(partnerPacketPeriodMoney));
            partnerDayStats.setPartnerRefundPacketPeriodMoney((int)Math.round(partnerRefundPacketPeriodMoney));


            partnerDayStats.setOrderCount(orderCount);
            partnerDayStats.setPacketPeriodOrderCount(packetPeriodOrderCount);
            partnerDayStats.setRefundPacketPeriodOrderCount(refundPacketPeriodOrderCount);


            //运营商当日新增换电金额(分成后) == 当日新增运营收入(income)
            partnerDayStats.setIncome(partnerDayStats.getPartnerExchangeMoney() + partnerDayStats.getPartnerPacketPeriodMoney() - partnerDayStats.getPartnerRefundPacketPeriodMoney());
            partnerDayStats.setMoney(partnerDayStats.getExchangeMoney() + partnerDayStats.getPacketPeriodMoney() - partnerDayStats.getRefundPacketPeriodMoney());
        }

        //商户日统计保存
        for (PartnerDayStats e : partnerExchangeDayStatsMap.values()) {
            Partner partner = getPartner(e.getPartnerId());
            e.setStatsDate(statsDate);
            e.setCategory(ConstEnum.Category.EXCHANGE.getValue());
            e.setPartnerName(partner.getPartnerName());
            e.setSuffix(suffix);
            e.setUpdateTime(new Date());
            if (partnerDayStatsMapper.find(e.getPartnerId(), statsDate, e.getCategory()) == null) {
                partnerDayStatsMapper.insert(e);
            } else {
                partnerDayStatsMapper.update(e);
            }
        }


        //商户租电日统计
        for (Integer partnerId : partnerMap.keySet()) {
            PartnerDayStats partnerDayStats = partnerRentDayStatsMap.get(partnerId);
            if (partnerDayStats == null) {
                partnerDayStats = new PartnerDayStats();
                partnerDayStats.init();
                Partner partner = partnerMap.get(partnerId);
                partnerDayStats.setPartnerId(partnerId);
                partnerDayStats.setPartnerName(partner.getPartnerName());
                partnerRentDayStatsMap.put(partnerId, partnerDayStats);
            }

            List<Integer> orgTypeList = Arrays.asList(
                    IncomeRatioHistory.OrgType.PLATFORM.getValue()
            );


            //当日包时段订单(分成前)
            Double rentPeriodMoney = rentPeriodOrderAllotMapper.sumOrderMoney(partnerId, null, RentPeriodOrderAllot.ServiceType.INCOME.getValue(), IncomeRatioHistory.OrgType.AGENT.getValue(), statsDate, suffix);
            //运营商当日新增包时段订单(分成后)
            Double partnerRentPeriodMoney = rentPeriodOrderAllotMapper.sumMoney(partnerId, null,  RentPeriodOrderAllot.ServiceType.INCOME.getValue(), orgTypeList, statsDate, suffix);

            //当日退款包时段订单(分成前)
            Double refundRentPeriodMoney = rentPeriodOrderAllotMapper.sumOrderMoney(partnerId,  null, RentPeriodOrderAllot.ServiceType.REFUND.getValue(), IncomeRatioHistory.OrgType.AGENT.getValue(), statsDate, suffix);
            //运营商退款包时段订单(分成后)
            Double partnerRefundRentPeriodMoney = rentPeriodOrderAllotMapper.sumMoney(partnerId, null, RentPeriodOrderAllot.ServiceType.REFUND.getValue(), orgTypeList, statsDate, suffix);

            //当日购买包时段订单次数
            Integer rentPeriodOrderCount = rentPeriodOrderAllotMapper.rentPeriodOrderCount(partnerId,  null, null, BatteryOrderAllot.ServiceType.INCOME.getValue(), IncomeRatioHistory.OrgType.AGENT.getValue(), statsDate, suffix);
            //当日退款包时段订单次数
            Integer refundRentPeriodOrderCount = rentPeriodOrderAllotMapper.rentPeriodOrderCount(partnerId, null, null, BatteryOrderAllot.ServiceType.REFUND.getValue(), IncomeRatioHistory.OrgType.AGENT.getValue(), statsDate, suffix);



            //保存数据
            partnerDayStats.setPacketPeriodMoney((int)Math.round(rentPeriodMoney));
            partnerDayStats.setRefundPacketPeriodMoney((int)Math.round(refundRentPeriodMoney));

            partnerDayStats.setPartnerPacketPeriodMoney((int)Math.round(partnerRentPeriodMoney));
            partnerDayStats.setPartnerRefundPacketPeriodMoney((int)Math.round(partnerRefundRentPeriodMoney));


            partnerDayStats.setPacketPeriodOrderCount(rentPeriodOrderCount);
            partnerDayStats.setRefundPacketPeriodOrderCount(refundRentPeriodOrderCount);



            //运营商当日新增换电金额(分成后) == 当日新增运营收入(income)
            partnerDayStats.setIncome( + partnerDayStats.getPartnerPacketPeriodMoney() - partnerDayStats.getPartnerRefundPacketPeriodMoney());
            partnerDayStats.setMoney(partnerDayStats.getPacketPeriodMoney() - partnerDayStats.getRefundPacketPeriodMoney());
        }

        //商户日统计保存
        for (PartnerDayStats e : partnerRentDayStatsMap.values()) {
            Partner partner = getPartner(e.getPartnerId());
            e.setStatsDate(statsDate);
            e.setCategory(ConstEnum.Category.RENT.getValue());
            e.setPartnerName(partner.getPartnerName());
            e.setSuffix(suffix);
            e.setUpdateTime(new Date());
            if (partnerDayStatsMapper.find(e.getPartnerId(), statsDate, e.getCategory()) == null) {
                partnerDayStatsMapper.insert(e);
            } else {
                partnerDayStatsMapper.update(e);
            }
        }

        //日结保存
        if(balance){
            //运营商换电日结
            for(AgentDayStats e : agentExchangeDayStatsMap.values()) {
                if(balanceRecordMapper.findByBalanceDate(null, e.getAgentId(), null,  BalanceRecord.BizType.AGENT.getValue(), e.getStatsDate(), e.getCategory()) == null) {
                    BalanceRecord record = new BalanceRecord();
                    record.init();
                    record.setBalanceDate(e.getStatsDate());
                    record.setCategory(e.getCategory());
                    record.setBizType(BalanceRecord.BizType.AGENT.getValue());
                    record.setPartnerId(e.getPartnerId());
                    record.setPartnerName(e.getPartnerName());
                    record.setAgentId(e.getAgentId());
                    record.setAgentName(e.getAgentName());
                    record.setMoney(e.getIncome());
                    record.setPacketPeriodMoney(e.getAgentPacketPeriodMoney());
                    record.setExchangeMoney(e.getAgentExchangeMoney());
                    record.setInsuranceMoney(e.getInsuranceMoney());
                    record.setProvinceIncome(e.getProvinceIncome());
                    record.setCityIncome(e.getCityIncome());
                    record.setForegiftRemainMoney(e.getForegiftRemainMoney());
                    record.setRefundInsuranceMoney(e.getInsuranceRefundMoney());
                    record.setRefundPacketPeriodMoney(e.getAgentRefundPacketPeriodMoney());
                    record.setDeductionTicketMoney(e.getDeductionTicketMoney());

                    balanceRecordMapper.insert(record);

                    //运营商日结算 插入运营商通知
                    insertPushMetaData(PushMessage.SourceType.AGENT_SETTLEMENT_NOTICE.getValue(), e.getAgentId().toString());
                }
            }

            //门店换电日结
            for(ShopDayStats e : shopExchangeDayStatsMap.values()) {
                if(balanceRecordMapper.findByBalanceDate(null, null, e.getShopId(), BalanceRecord.BizType.SHOP.getValue(), e.getStatsDate(), e.getCategory()) == null) {
                    BalanceRecord record = new BalanceRecord();
                    record.init();
                    record.setBalanceDate(e.getStatsDate());
                    record.setCategory(e.getCategory());
                    record.setBizType(BalanceRecord.BizType.SHOP.getValue());
                    record.setPartnerId(e.getPartnerId());
                    record.setPartnerName(e.getPartnerName());
                    record.setAgentId(e.getAgentId());
                    record.setAgentName(e.getAgentName());
                    record.setShopId(e.getShopId());
                    record.setShopName(e.getShopName());
                    record.setMoney(e.getMoney());
                    record.setPacketPeriodMoney(e.getPacketPeriodMoney());
                    record.setExchangeMoney(e.getExchangeMoney());
                    record.setRefundPacketPeriodMoney(e.getRefundPacketPeriodMoney());

                    balanceRecordMapper.insert(record);
                }
            }

            //运营公司换电日结
            for(AgentCompanyDayStats e : agentCompanyExchangeDayStatsMap.values()) {
                if(balanceRecordMapper.findByAgentCompanyBalanceDate(null, null, e.getAgentCompanyId(), BalanceRecord.BizType.AGENT_COMPANY.getValue(), e.getStatsDate(), e.getCategory()) == null) {
                    BalanceRecord record = new BalanceRecord();
                    record.init();
                    record.setBalanceDate(e.getStatsDate());
                    record.setCategory(e.getCategory());
                    record.setBizType(BalanceRecord.BizType.AGENT_COMPANY.getValue());
                    record.setPartnerId(e.getPartnerId());
                    record.setPartnerName(e.getPartnerName());
                    record.setAgentId(e.getAgentId());
                    record.setAgentName(e.getAgentName());
                    record.setAgentCompanyId(e.getAgentCompanyId());
                    record.setAgentCompanyName(e.getAgentCompanyName());
                    record.setMoney(e.getMoney());
                    record.setPacketPeriodMoney(e.getPacketPeriodMoney());
                    record.setExchangeMoney(e.getExchangeMoney());
                    record.setRefundPacketPeriodMoney(e.getRefundPacketPeriodMoney());

                    balanceRecordMapper.insert(record);
                }
            }

            //商户换电日结
            for(PartnerDayStats e : partnerExchangeDayStatsMap.values()) {
                if(balanceRecordMapper.findByBalanceDate(e.getPartnerId(), null, null, BalanceRecord.BizType.PARTNER.getValue(), e.getStatsDate(), e.getCategory()) == null) {
                    BalanceRecord record = new BalanceRecord();
                    record.init();
                    record.setPartnerId(e.getPartnerId());
                    record.setPartnerName(e.getPartnerName());
                    record.setBalanceDate(e.getStatsDate());
                    record.setCategory(e.getCategory());
                    record.setBizType(BalanceRecord.BizType.PARTNER.getValue());
                    record.setMoney(e.getIncome());
                    record.setPacketPeriodMoney(e.getPartnerPacketPeriodMoney());
                    record.setExchangeMoney(e.getPartnerExchangeMoney());
                    record.setRefundPacketPeriodMoney(e.getPartnerRefundPacketPeriodMoney());

                    balanceRecordMapper.insert(record);
                }
            }


            //运营商租电日结
            for(AgentDayStats e : agentRentDayStatsMap.values()) {
                if(balanceRecordMapper.findByBalanceDate(null, e.getAgentId(), null,  BalanceRecord.BizType.AGENT.getValue(), e.getStatsDate(), e.getCategory()) == null) {
                    BalanceRecord record = new BalanceRecord();
                    record.init();
                    record.setBalanceDate(e.getStatsDate());
                    record.setCategory(e.getCategory());
                    record.setBizType(BalanceRecord.BizType.AGENT.getValue());
                    record.setPartnerId(e.getPartnerId());
                    record.setPartnerName(e.getPartnerName());
                    record.setAgentId(e.getAgentId());
                    record.setAgentName(e.getAgentName());
                    record.setMoney(e.getIncome());
                    record.setPacketPeriodMoney(e.getAgentPacketPeriodMoney());
                    record.setExchangeMoney(e.getAgentExchangeMoney());
                    record.setInsuranceMoney(e.getInsuranceMoney());
                    record.setProvinceIncome(e.getProvinceIncome());
                    record.setCityIncome(e.getCityIncome());
                    record.setForegiftRemainMoney(e.getForegiftRemainMoney());
                    record.setRefundInsuranceMoney(e.getInsuranceRefundMoney());
                    record.setRefundPacketPeriodMoney(e.getAgentRefundPacketPeriodMoney());
                    record.setDeductionTicketMoney(e.getDeductionTicketMoney());

                    balanceRecordMapper.insert(record);

                    //运营商日结算 插入运营商通知
                    //insertPushMetaData(PushMessage.SourceType.AGENT_SETTLEMENT_NOTICE.getValue(), e.getAgentId().toString());
                }
            }

            //门店租电日结
            for(ShopDayStats e : shopRentDayStatsMap.values()) {
                if(balanceRecordMapper.findByBalanceDate(null, null, e.getShopId(), BalanceRecord.BizType.SHOP.getValue(), e.getStatsDate(), e.getCategory()) == null) {
                    BalanceRecord record = new BalanceRecord();
                    record.init();
                    record.setBalanceDate(e.getStatsDate());
                    record.setCategory(e.getCategory());
                    record.setBizType(BalanceRecord.BizType.SHOP.getValue());
                    record.setPartnerId(e.getPartnerId());
                    record.setPartnerName(e.getPartnerName());
                    record.setAgentId(e.getAgentId());
                    record.setAgentName(e.getAgentName());
                    record.setShopId(e.getShopId());
                    record.setShopName(e.getShopName());
                    record.setMoney(e.getMoney());
                    record.setPacketPeriodMoney(e.getPacketPeriodMoney());
                    record.setExchangeMoney(e.getExchangeMoney());
                    record.setRefundPacketPeriodMoney(e.getRefundPacketPeriodMoney());

                    balanceRecordMapper.insert(record);
                }
            }

            //运营公司租电日结
            for(AgentCompanyDayStats e : agentCompanyRentDayStatsMap.values()) {
                if(balanceRecordMapper.findByAgentCompanyBalanceDate(null, null, e.getAgentCompanyId(), BalanceRecord.BizType.AGENT_COMPANY.getValue(), e.getStatsDate(), e.getCategory()) == null) {
                    BalanceRecord record = new BalanceRecord();
                    record.init();
                    record.setBalanceDate(e.getStatsDate());
                    record.setCategory(e.getCategory());
                    record.setBizType(BalanceRecord.BizType.AGENT_COMPANY.getValue());
                    record.setPartnerId(e.getPartnerId());
                    record.setPartnerName(e.getPartnerName());
                    record.setAgentId(e.getAgentId());
                    record.setAgentName(e.getAgentName());
                    record.setAgentCompanyId(e.getAgentCompanyId());
                    record.setAgentCompanyName(e.getAgentCompanyName());
                    record.setMoney(e.getMoney());
                    record.setPacketPeriodMoney(e.getPacketPeriodMoney());
                    record.setExchangeMoney(e.getExchangeMoney());
                    record.setRefundPacketPeriodMoney(e.getRefundPacketPeriodMoney());

                    balanceRecordMapper.insert(record);
                }
            }

            //商户租电日结
            for(PartnerDayStats e : partnerRentDayStatsMap.values()) {
                if(balanceRecordMapper.findByBalanceDate(e.getPartnerId(), null, null, BalanceRecord.BizType.PARTNER.getValue(), e.getStatsDate(), e.getCategory()) == null) {
                    BalanceRecord record = new BalanceRecord();
                    record.init();
                    record.setPartnerId(e.getPartnerId());
                    record.setPartnerName(e.getPartnerName());
                    record.setBalanceDate(e.getStatsDate());
                    record.setCategory(e.getCategory());
                    record.setBizType(BalanceRecord.BizType.PARTNER.getValue());
                    record.setMoney(e.getIncome());
                    record.setPacketPeriodMoney(e.getPartnerPacketPeriodMoney());
                    record.setExchangeMoney(e.getPartnerExchangeMoney());
                    record.setRefundPacketPeriodMoney(e.getPartnerRefundPacketPeriodMoney());

                    balanceRecordMapper.insert(record);
                }
            }
        }

        //平台统计
        //当日新增数据
        PlatformDayStats platformDayStats = agentDayStatsMapper.sumDay(statsDate);
        platformDayStats.setStatsDate(statsDate);
        /*数据来源运营商日统计*/
        platformDayStats.setIncrementExchangeMoney(platformDayStats.getIncrementExchangeMoney());
        platformDayStats.setIncrementPacketPeriodMoney(platformDayStats.getIncrementPacketPeriodMoney());
        platformDayStats.setIncrementExchangeCount(platformDayStats.getIncrementExchangeCount());
        //当日新增退款金额(运营商日统计缺少字段)
        // platformDayStats.setIncrementRefundMoney(platformDayStats.getIncrementRefundMoney());
        platformDayStats.setIncrementRefundMoney(0l);
        platformDayStats.setIncrementRefundExchangeMoney(platformDayStats.getIncrementRefundExchangeMoney());
        platformDayStats.setIncrementRefundPacketPeriodMoney(platformDayStats.getIncrementRefundPacketPeriodMoney());
        platformDayStats.setIncrementRefundExchangeCount(platformDayStats.getIncrementRefundExchangeCount());
        platformDayStats.setAgentIncome(platformDayStats.getAgentIncome());
        platformDayStats.setIncrementPlatformIncome(platformDayStats.getIncrementPlatformIncome());

        platformDayStats.setIncrementForegiftMoney(platformDayStats.getIncrementForegiftMoney());
        platformDayStats.setIncrementForegiftCount(platformDayStats.getIncrementForegiftCount());

        platformDayStats.setIncrementRefundForegiftMoney(platformDayStats.getIncrementRefundForegiftMoney());
        platformDayStats.setIncrementRefundForegiftCount(platformDayStats.getIncrementRefundForegiftCount());

        //总数据
        PlatformDayStats totalPlatformDayStats = agentTotalStatsMapper.sum();
        /*数据来源运营商总统计*/
        platformDayStats.setTotalExchangeMoney(totalPlatformDayStats.getTotalExchangeMoney());
        platformDayStats.setTotalExchangeCount(totalPlatformDayStats.getTotalExchangeCount());
        platformDayStats.setTotalPacketPeriodMoney(totalPlatformDayStats.getTotalPacketPeriodMoney());
        platformDayStats.setTotalRefundExchangeMoney(totalPlatformDayStats.getTotalRefundExchangeMoney());
        platformDayStats.setTotalRefundExchangeCount(totalPlatformDayStats.getTotalRefundExchangeCount());
        platformDayStats.setTotalRefundPacketPeriodMoney(totalPlatformDayStats.getTotalRefundPacketPeriodMoney());
        platformDayStats.setTotalPlatformIncome(totalPlatformDayStats.getTotalPlatformIncome());
        platformDayStats.setTotalForegiftMoney(totalPlatformDayStats.getTotalForegiftMoney());
        platformDayStats.setTotalForegiftCount(totalPlatformDayStats.getTotalForegiftCount());
        platformDayStats.setTotalRefundForegiftMoney(totalPlatformDayStats.getTotalRefundForegiftMoney());
        platformDayStats.setTotalRefundForegiftCount(totalPlatformDayStats.getTotalRefundForegiftCount());
        platformDayStats.setTotalCabinetCount(totalPlatformDayStats.getTotalCabinetCount());

        //新增充值金额
        //PlatformDayStats incrementDeposit = customerDepositOrderMapper.findIncrement(CustomerDepositOrder.Status.OK.getValue(), beginTime, endTime);
        platformDayStats.setIncrementDepositMoney(0L);
        platformDayStats.setIncrementDepositCount(0L);
        //总充值金额
        //PlatformDayStats totalDeposit = customerDepositOrderMapper.findTotal(CustomerDepositOrder.Status.OK.getValue());
        platformDayStats.setTotalDepositMoney(0L);
        platformDayStats.setTotalDepositCount(0L);
        //新增押金金额
        //PlatformDayStats incrementForegift = customerForegiftOrderMapper.findIncrement(CustomerForegiftOrder.Status.PAY_OK.getValue(), beginTime, endTime);

        //总押金金额
        //PlatformDayStats totalForegift = customerForegiftOrderMapper.findTotal(CustomerForegiftOrder.Status.PAY_OK.getValue());



        //新增退款充值
        //PlatformDayStats incrementRefundDeposit = customerDepositOrderMapper.findIncrementRefund(CustomerDepositOrder.Status.REFUND.getValue(), beginTime, endTime);
        platformDayStats.setIncrementRefundDepositMoney(0L);
        platformDayStats.setIncrementRefundDepositCount(0L);
        //总退款充值
        //PlatformDayStats totalRefundDeposit = customerDepositOrderMapper.findTotalRefund(CustomerDepositOrder.Status.REFUND.getValue());
        platformDayStats.setTotalRefundDepositMoney(0L);
        platformDayStats.setTotalRefundDepositCount(0L);
        //新增退款押金
        //PlatformDayStats incrementRefundForegift = customerForegiftOrderMapper.findIncrementRefund( CustomerForegiftOrder.Status.REFUND_SUCCESS.getValue(), beginTime, endTime);

        //总退款押金
        //PlatformDayStats totalRefundForegift = customerForegiftOrderMapper.findTotalRefund(CustomerForegiftOrder.Status.REFUND_SUCCESS.getValue());


        //新增退款金额
        platformDayStats.setIncrementRefundMoney(platformDayStats.getIncrementRefundForegiftMoney() + platformDayStats.getIncrementRefundExchangeMoney() + platformDayStats.getIncrementRefundPacketPeriodMoney() + platformDayStats.getIncrementRefundDepositMoney());
        //总退款金额
        platformDayStats.setTotalRefundMoney(platformDayStats.getTotalRefundForegiftMoney() + platformDayStats.getTotalRefundExchangeMoney() + platformDayStats.getTotalRefundPacketPeriodMoney() + platformDayStats.getTotalRefundDepositMoney());

        //柜子数量
        platformDayStats.setIncrementCabinetCount(0L);//暂不统计

        //用户数
        long totalCustomerCount = customerMapper.findTotal();
        long incrementCustomerCount = customerMapper.findIncrement(beginTime, endTime);
        platformDayStats.setTotalCustomerCount(totalCustomerCount);
        platformDayStats.setIncrementCustomerCount(incrementCustomerCount);

        //意见反馈
//        long totalFeedbackCount = feedbackMapper.findTotal();
//        long incrementFeedbackCount = feedbackMapper.findIncrement(beginTime, endTime);
        platformDayStats.setTotalFeedbackCount(0L);
        platformDayStats.setIncrementFeedbackCount(0L);

        //当日未使用人数
        //long notUseCountDay = totalCustomerCount - customerDayStatsMapper.findDayCount(statsDate);
        platformDayStats.setNotUseCount(0L);//暂不统计

        platformDayStats.setUpdateTime(new Date());
        if (platformDayStatsMapper.find(statsDate) == null) {
            platformDayStatsMapper.insert(platformDayStats);
        } else {
            platformDayStatsMapper.update(platformDayStats);
        }

        PlatformMonthStats platformMonthStats = platformDayStatsMapper.sumMonth(statsMonth);
        //long notUseCountMonth = totalCustomerCount - customerDayStatsMapper.findMonthCount(statsMonth);
        platformMonthStats.setNotUseCount(0L);
        platformMonthStats.setStatsMonth(statsMonth);
        platformMonthStats.setUpdateTime(new Date());

        if (platformMonthStatsMapper.find(statsMonth) == null) {
            platformMonthStatsMapper.insert(platformMonthStats);
        } else {
            platformMonthStatsMapper.update(platformMonthStats);
        }

//        //用户日统计
//        for (CustomerDayStats e : customerDayStats) {
//            e.setStatsDate(statsDate);
//            CustomerDayStats dayStats = customerDayStatsMapper.find(e.getCustomerId(), e.getStatsDate());
//            e.setUpdateTime(new Date());
//            if (dayStats == null) {
//                customerDayStatsMapper.insert(e);
//            } else {
//                customerDayStatsMapper.update(e);
//            }
//        }

        log.debug("日收入统计结束...");
    }

    public Cabinet getCabinet(String id) {
        Cabinet cabinet = cabinetMap.get(id);
        if (cabinet == null) {
            cabinet = cabinetMapper.find(id);
            if(cabinet != null){
                cabinetMap.put(cabinet.getId(), cabinet);
            }
        }
        return cabinet;
    }

    public Shop getShop(String id) {
        Shop shop = shopMap.get(id);
        if (shop == null) {
            shop = shopMapper.find(id);
            if(shop != null){
                shopMap.put(shop.getId(), shop);
            }
        }
        return shop;
    }

    public AgentCompany getAgentCompany(String id) {
        AgentCompany agentCompany = agentCompanyMap.get(id);
        if (agentCompany == null) {
            agentCompany = agentCompanyMapper.find(id);
            if (agentCompany != null) {
                agentCompanyMap.put(agentCompany.getId(), agentCompany);
            }
        }
        return agentCompany;
    }

    public Agent getAgent(int id) {
        Agent agent = agentMap.get(id);
        if (agent == null) {
            agent = agentMapper.find(id);
            if(agent != null){
                agentMap.put(agent.getId(), agent);
            }
        }
        return agent;
    }

    public Partner getPartner(int id) {
        Partner partner =partnerMap.get(id);
        if (partner == null) {
            partner = partnerMapper.find(id);
            if(partner != null){
                partnerMap.put(partner.getId(), partner);
            }
        }
        return partner;
    }

    public Map<Integer, IncomeRatioHistory> incomeRatioHistoryPlatformMap(int agentId, String cabinetId,  String statsDate) throws ParseException {
        Agent agent = getAgent(agentId);
        Partner partener = getPartner(agent.getPartnerId());
        Cabinet cabinet = getCabinet(cabinetId);
        Shop shop = null;
        if(cabinet.getShopId() != null){
            shop = getShop(cabinet.getShopId());
        }

        String key = String.format("cabinet_%d_%s_%s", agentId, cabinetId, statsDate);
        Map incomeRatioHistoryMap = agentIncomeRatioHistoryMap.get(key);
        if (incomeRatioHistoryMap == null ) {
            String suffix = IncomeRatioHistory.getSuffix(statsDate);
            if (StringUtils.isEmpty(incomeRatioHistoryMapper.exist(suffix))) {
                incomeRatioHistoryMapper.createTable(suffix);
            }
            //查询数据是否存在
            List<IncomeRatioHistory> incomeRatioHistorylist  = incomeRatioHistoryMapper.findList(agentId, cabinetId, statsDate, suffix);
            incomeRatioHistoryMap = new LinkedHashMap<Integer, IncomeRatioHistory>();
            if(incomeRatioHistorylist != null && incomeRatioHistorylist.size() >0){
                for(IncomeRatioHistory incomeRatioHistory : incomeRatioHistorylist){
                    incomeRatioHistoryMap.put(incomeRatioHistory.getOrgType(), incomeRatioHistory);
                }
            }else {
                List<Integer> orgTypeList = Arrays.asList(
                        IncomeRatioHistory.OrgType.PLATFORM.getValue(),
                        IncomeRatioHistory.OrgType.PROVINCE_AGENT.getValue(),
                        IncomeRatioHistory.OrgType.CITY_AGENT.getValue(),
                        IncomeRatioHistory.OrgType.SHOP.getValue(),
                        IncomeRatioHistory.OrgType.AGENT.getValue()
                );

                for(Integer orgType : orgTypeList){
                    IncomeRatioHistory incomeRatioHistory = new IncomeRatioHistory();

                    if (orgType == IncomeRatioHistory.OrgType.PLATFORM.getValue()) {  //平台分成
                        if(cabinet.getPlatformRatio() != null ) {
                            incomeRatioHistory.setRatio(cabinet.getPlatformRatio());
                        }
                        //判断是否有扣除金额
                        if(cabinet.getActivePlatformDeduct() != null && cabinet.getActivePlatformDeduct() == ConstEnum.Flag.TRUE.getValue()
                                && cabinet.getPlatformDeductMoney() != null  && cabinet.getPlatformDeductMoney() > 0
                                && (cabinet.getPlatformDeductExpireTime() == null ||
                                cabinet.getPlatformDeductExpireTime().compareTo(DateUtils.parseDate(statsDate, new String[]{"yyyy-MM-dd"})) < 0)){
                            if(incomeRatioHistory.getRatio() == null){
                                incomeRatioHistory.setRatio(0);
                            }
                            incomeRatioHistory.setPlatformDeductMoney(cabinet.getPlatformDeductMoney());
                        }
                        incomeRatioHistory.setOrgType(orgType);
                        incomeRatioHistory.setOrgId(partener.getId());
                        incomeRatioHistory.setOrgName(partener.getPartnerName());
                    } else if (orgType == IncomeRatioHistory.OrgType.AGENT.getValue()) {  //运营商分成
                        incomeRatioHistory.setRatio(100);//最后分摊运营商，分摊剩下的金额就全部归属运营商
                        incomeRatioHistory.setOrgType(orgType);
                        incomeRatioHistory.setOrgId(agentId);
                        incomeRatioHistory.setOrgName(agent.getAgentName());
                    } else if (orgType == IncomeRatioHistory.OrgType.PROVINCE_AGENT.getValue()) { //省代分成
                        if(cabinet.getProvinceAgentRatio() != null  && agent.getProvinceAgentId() != null){
                            incomeRatioHistory.setRatio(cabinet.getProvinceAgentRatio());
                            incomeRatioHistory.setOrgType(orgType);
                            incomeRatioHistory.setOrgId(agent.getProvinceAgentId());
                            Agent provinceAgent = getAgent(agent.getProvinceAgentId());
                            if(provinceAgent != null){
                                incomeRatioHistory.setOrgName(provinceAgent.getAgentName());
                            }
                        }
                    } else if(orgType == IncomeRatioHistory.OrgType.CITY_AGENT.getValue()){ //市代分成
                        if(cabinet.getCityAgentRatio() != null && agent.getCityAgentId() != null){
                            incomeRatioHistory.setRatio(cabinet.getCityAgentRatio());
                            incomeRatioHistory.setOrgType(orgType);
                            incomeRatioHistory.setOrgId(agent.getCityAgentId());
                            Agent cityAgent = getAgent(agent.getCityAgentId());
                            if(cityAgent != null){
                                incomeRatioHistory.setOrgName(cityAgent.getAgentName());
                            }
                        }
                    } else if(orgType == IncomeRatioHistory.OrgType.SHOP.getValue()){ //门店分成
                        if(cabinet.getShopRatio() != null && shop != null){
                            incomeRatioHistory.setRatio(cabinet.getShopRatio());
                            incomeRatioHistory.setOrgType(orgType);
                            incomeRatioHistory.setShopId(shop.getId());
                            incomeRatioHistory.setOrgName(shop.getShopName());

                            //门店固定分成
                            if(cabinet.getShopFixedMoney() != null && cabinet.getShopFixedMoney() > 0){
                                incomeRatioHistory.setShopFixedMoney(cabinet.getShopFixedMoney());
                            }
                        }
                    }
                    if(incomeRatioHistory.getRatio() != null  ){
                        incomeRatioHistory.setStatsDate(statsDate);
                        incomeRatioHistory.setAgentId(agent.getId());
                        incomeRatioHistory.setAgentName(agent.getAgentName());
                        incomeRatioHistory.setCabinetId(cabinet.getId());
                        incomeRatioHistory.setCreateTime(new Date());
                        incomeRatioHistoryMapper.insert(incomeRatioHistory);
                    }

                    if(incomeRatioHistory.getRatio() != null){
                        incomeRatioHistoryMap.put(orgType, incomeRatioHistory);
                    }
                }
            }


            agentIncomeRatioHistoryMap.put(key, incomeRatioHistoryMap);
        }
        return incomeRatioHistoryMap;
    }

    public Map<Integer, IncomeRatioHistory> shopIncomeRatioHistoryPlatformMap(int agentId, String shopId, String statsDate) throws ParseException {
        Agent agent = getAgent(agentId);
        Partner partener = getPartner(agent.getPartnerId());

        Shop shop = getShop(shopId);
        if (shop == null)
            return null;

        String key = String.format("shop_%d_%s_%s", agentId, shopId, statsDate);
        Map incomeRatioHistoryMap = agentIncomeRatioHistoryMap.get(key);
        if (incomeRatioHistoryMap == null ) {
            String suffix = IncomeRatioHistory.getSuffix(statsDate);
            if (StringUtils.isEmpty(shopIncomeRatioHistoryMapper.exist(suffix))) {
                shopIncomeRatioHistoryMapper.createTable(suffix);
            }
            //查询数据是否存在
            List<ShopIncomeRatioHistory> shopIncomeRatioHistorylist  = shopIncomeRatioHistoryMapper.findList(agentId, shopId, statsDate, suffix);
            incomeRatioHistoryMap = new LinkedHashMap<Integer, IncomeRatioHistory>();
            if(shopIncomeRatioHistorylist != null && shopIncomeRatioHistorylist.size() >0){
                for(IncomeRatioHistory incomeRatioHistory : shopIncomeRatioHistorylist){
                    incomeRatioHistoryMap.put(incomeRatioHistory.getOrgType(), incomeRatioHistory);
                }
            }else {
                List<Integer> orgTypeList = Arrays.asList(
                        IncomeRatioHistory.OrgType.PLATFORM.getValue(),
                        IncomeRatioHistory.OrgType.PROVINCE_AGENT.getValue(),
                        IncomeRatioHistory.OrgType.CITY_AGENT.getValue(),
                        IncomeRatioHistory.OrgType.SHOP.getValue(),
                        IncomeRatioHistory.OrgType.AGENT.getValue()
                );

                for(Integer orgType : orgTypeList){
                    ShopIncomeRatioHistory shopIncomeRatioHistory = new ShopIncomeRatioHistory();

                    if (orgType == ShopIncomeRatioHistory.OrgType.PLATFORM.getValue()) {  //平台分成
                        if(shop.getPlatformRatio() != null ) {
                            shopIncomeRatioHistory.setRatio(shop.getPlatformRatio());
                        }
                        //判断是否有扣除金额
//                        if(cabinet.getActivePlatformDeduct() != null && cabinet.getActivePlatformDeduct() == ConstEnum.Flag.TRUE.getValue()
//                                && cabinet.getPlatformDeductMoney() != null  && cabinet.getPlatformDeductMoney() > 0
//                                && (cabinet.getPlatformDeductExpireTime() == null ||
//                                cabinet.getPlatformDeductExpireTime().compareTo(DateUtils.parseDate(statsDate, new String[]{"yyyy-MM-dd"})) < 0)){
//                            if(shopIncomeRatioHistory.getRatio() == null){
//                                shopIncomeRatioHistory.setRatio(0);
//                            }
//                            shopIncomeRatioHistory.setPlatformDeductMoney(cabinet.getPlatformDeductMoney());
//                        }
                        shopIncomeRatioHistory.setOrgType(orgType);
                        shopIncomeRatioHistory.setOrgId(partener.getId());
                        shopIncomeRatioHistory.setOrgName(partener.getPartnerName());
                    } else if (orgType == ShopIncomeRatioHistory.OrgType.AGENT.getValue()) {  //运营商分成
                        shopIncomeRatioHistory.setRatio(100);//最后分摊运营商，分摊剩下的金额就全部归属运营商
                        shopIncomeRatioHistory.setOrgType(orgType);
                        shopIncomeRatioHistory.setOrgId(agentId);
                        shopIncomeRatioHistory.setOrgName(agent.getAgentName());
                    } else if (orgType == ShopIncomeRatioHistory.OrgType.PROVINCE_AGENT.getValue()) { //省代分成
                        if(shop.getProvinceAgentRatio() != null  && agent.getProvinceAgentId() != null){
                            shopIncomeRatioHistory.setRatio(shop.getProvinceAgentRatio());
                            shopIncomeRatioHistory.setOrgType(orgType);
                            shopIncomeRatioHistory.setOrgId(agent.getProvinceAgentId());
                            Agent provinceAgent = getAgent(agent.getProvinceAgentId());
                            if(provinceAgent != null){
                                shopIncomeRatioHistory.setOrgName(provinceAgent.getAgentName());
                            }
                        }
                    } else if(orgType == ShopIncomeRatioHistory.OrgType.CITY_AGENT.getValue()){ //市代分成
                        if(shop.getCityAgentRatio() != null && agent.getCityAgentId() != null){
                            shopIncomeRatioHistory.setRatio(shop.getCityAgentRatio());
                            shopIncomeRatioHistory.setOrgType(orgType);
                            shopIncomeRatioHistory.setOrgId(agent.getCityAgentId());
                            Agent cityAgent = getAgent(agent.getCityAgentId());
                            if(cityAgent != null){
                                shopIncomeRatioHistory.setOrgName(cityAgent.getAgentName());
                            }
                        }
                    } else if(orgType == ShopIncomeRatioHistory.OrgType.SHOP.getValue()){ //门店分成
                        if(shop.getShopRatio() != null){
                            shopIncomeRatioHistory.setRatio(shop.getShopRatio());
                            shopIncomeRatioHistory.setOrgType(orgType);
                            shopIncomeRatioHistory.setShopId(shop.getId());
                            shopIncomeRatioHistory.setOrgName(shop.getShopName());
                            //门店固定分成
                            if(shop.getShopFixedMoney() != null && shop.getShopFixedMoney() > 0){
                                shopIncomeRatioHistory.setShopFixedMoney(shop.getShopFixedMoney());
                            }
                        }
                    }
                    if(shopIncomeRatioHistory.getRatio() != null  ){
                        shopIncomeRatioHistory.setStatsDate(statsDate);
                        shopIncomeRatioHistory.setAgentId(agent.getId());
                        shopIncomeRatioHistory.setAgentName(agent.getAgentName());
                        shopIncomeRatioHistory.setShopId(shop.getId());
                        shopIncomeRatioHistory.setCreateTime(new Date());
                        shopIncomeRatioHistoryMapper.insert(shopIncomeRatioHistory);
                    }

                    if(shopIncomeRatioHistory.getRatio() != null){
                        incomeRatioHistoryMap.put(orgType, shopIncomeRatioHistory);
                    }
                }
            }


            agentIncomeRatioHistoryMap.put(key, incomeRatioHistoryMap);
        }
        return incomeRatioHistoryMap;
    }

    public Map<Integer, IncomeRatioHistory> agentCompanyIncomeRatioHistoryPlatformMap(int agentId, String agentCompanyId, String statsDate) throws ParseException {
        Agent agent = getAgent(agentId);

        AgentCompany agentCompany = getAgentCompany(agentCompanyId);
        if (agentCompany == null)
            return null;

        String key = String.format("agentcompany_%d_%s_%s", agentId, agentCompanyId, statsDate);
        Map incomeRatioHistoryMap = agentCompanyIncomeRatioHistoryMap.get(key);
        if (incomeRatioHistoryMap == null) {
            String suffix = IncomeRatioHistory.getSuffix(statsDate);
            if (StringUtils.isEmpty(agentCompanyIncomeRatioHistoryMapper.exist(suffix))) {
                agentCompanyIncomeRatioHistoryMapper.createTable(suffix);
            }
            //查询数据是否存在
            List<AgentCompanyIncomeRatioHistory> agentCompanyIncomeRatioHistoryList = agentCompanyIncomeRatioHistoryMapper.findList(agentId, agentCompanyId, statsDate, suffix);
            incomeRatioHistoryMap = new LinkedHashMap<Integer, IncomeRatioHistory>();
            if (agentCompanyIncomeRatioHistoryList != null && agentCompanyIncomeRatioHistoryList.size() > 0) {
                for (IncomeRatioHistory incomeRatioHistory : agentCompanyIncomeRatioHistoryList) {
                    incomeRatioHistoryMap.put(incomeRatioHistory.getOrgType(), incomeRatioHistory);
                }
            } else {

                    AgentCompanyIncomeRatioHistory agentCompanyIncomeRatioHistory = new AgentCompanyIncomeRatioHistory();

                        if (agentCompany.getCompanyRatio() != null) {
                            agentCompanyIncomeRatioHistory.setRatio(agentCompany.getCompanyRatio());
                            agentCompanyIncomeRatioHistory.setOrgType(IncomeRatioHistory.OrgType.AGENT_COMPANY.getValue());
                            agentCompanyIncomeRatioHistory.setAgentCompanyId(agentCompany.getId());
                            agentCompanyIncomeRatioHistory.setOrgName(agentCompany.getCompanyName());
                            //运营公司固定分成
                            if (agentCompany.getCompanyFixedMoney() != null && agentCompany.getCompanyFixedMoney() > 0) {
                                agentCompanyIncomeRatioHistory.setAgentCompanyFixedMoney(agentCompany.getCompanyFixedMoney());
                            }
                            //运营公司分成下限金额
                            if (agentCompany.getRatioBaseMoney() != null && agentCompany.getRatioBaseMoney() > 0) {
                                agentCompanyIncomeRatioHistory.setRatioBaseMoney(agentCompany.getRatioBaseMoney());
                            }
                        }
                    if (agentCompanyIncomeRatioHistory.getRatio() != null) {
                        agentCompanyIncomeRatioHistory.setStatsDate(statsDate);
                        agentCompanyIncomeRatioHistory.setAgentId(agent.getId());
                        agentCompanyIncomeRatioHistory.setAgentName(agent.getAgentName());
                        agentCompanyIncomeRatioHistory.setAgentCompanyId(agentCompany.getId());
                        agentCompanyIncomeRatioHistory.setCreateTime(new Date());
                        agentCompanyIncomeRatioHistoryMapper.insert(agentCompanyIncomeRatioHistory);
                        incomeRatioHistoryMap.put(IncomeRatioHistory.OrgType.AGENT_COMPANY.getValue(), agentCompanyIncomeRatioHistory);
                    }
                }

            agentCompanyIncomeRatioHistoryMap.put(key, incomeRatioHistoryMap);
        }
        return incomeRatioHistoryMap;
    }

    private void creatNoExistsTables(String suffix) throws ParseException {
        if(!StringUtils.isNotEmpty(batteryOrderAllotMapper.exist(suffix))) {
            batteryOrderAllotMapper.createTable(suffix);
        }
        if(!StringUtils.isNotEmpty(packetPeriodOrderAllotMapper.exist(suffix))) {
            packetPeriodOrderAllotMapper.createTable(suffix);
        }

        if(!StringUtils.isNotEmpty(rentPeriodOrderAllotMapper.exist(suffix))) {
            rentPeriodOrderAllotMapper.createTable(suffix);
        }
    }

    private int dealDays(int days){
        int returnDays = 0;
        if(days < 30){
            returnDays = days;
        }else if(days >= 30 && days < 60){
            returnDays = 30;
        }else if(days >= 60 && days < 90){
            returnDays = 60;
        }else if(days >= 90 && days < 180){
            returnDays = 90;
        }else if(days >= 180 && days < 360){
            returnDays = 180;
        }else if(days >= 360){
            returnDays = 360;
        }

        return returnDays;
    }

}
