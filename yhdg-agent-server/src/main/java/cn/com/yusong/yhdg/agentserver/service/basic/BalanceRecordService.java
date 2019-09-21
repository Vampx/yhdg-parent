package cn.com.yusong.yhdg.agentserver.service.basic;

import cn.com.yusong.yhdg.common.constant.ConstEnum;
import cn.com.yusong.yhdg.common.domain.basic.*;
import cn.com.yusong.yhdg.common.domain.hdg.Shop;
import cn.com.yusong.yhdg.common.entity.json.ExtResult;
import cn.com.yusong.yhdg.common.entity.pagination.Page;
import cn.com.yusong.yhdg.agentserver.persistence.basic.*;
import cn.com.yusong.yhdg.agentserver.persistence.hdg.ShopMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * Created by chen on 2017/7/9.
 */
@Service
public class BalanceRecordService {
    @Autowired
    BalanceRecordMapper balanceRecordMapper;
    @Autowired
    AgentMapper agentMapper;
    @Autowired
    ShopMapper shopMapper;
    @Autowired
    PlatformAccountMapper platformAccountMapper;
    @Autowired
    AgentInOutMoneyMapper agentInOutMoneyMapper;
    @Autowired
    ShopInOutMoneyMapper shopInOutMoneyMapper;
    @Autowired
    PlatformAccountInOutMoneyMapper platformAccountInOutMoneyMapper;
    @Autowired
    AgentCompanyInOutMoneyMapper agentCompanyInOutMoneyMapper;
    @Autowired
    AgentCompanyMapper agentCompanyMapper;

    public BalanceRecord find(Long id) {
        return balanceRecordMapper.find(id);
    }

    public Page findPage(BalanceRecord search) {
        Page page = search.buildPage();
        page.setTotalItems(balanceRecordMapper.findPageCount(search));
        search.setBeginIndex(page.getOffset());
        page.setResult(balanceRecordMapper.findPageResult(search));
        return page;
    }

    public ExtResult confirm(Long[] ids, String confirmOperator) {
        String message = "";

        Map<Integer, List<BalanceRecord>> agentRecordMap = new HashMap<Integer, List<BalanceRecord>>();
        Map<String, List<BalanceRecord>> shopRecordMap = new HashMap<String, List<BalanceRecord>>();
        Map<String, List<BalanceRecord>> agentCompanyRecordMap = new HashMap<String, List<BalanceRecord>>();
        Map<Integer, List<BalanceRecord>> partnerRecordMap = new HashMap<Integer, List<BalanceRecord>>();

        //数据汇总确认
        for (Long id : ids) {
            BalanceRecord balanceRecord = balanceRecordMapper.find(id);
            if (balanceRecord.getBizType() == BalanceRecord.BizType.AGENT.getValue()) {
                //运营商汇总
                if (agentRecordMap.get(balanceRecord.getAgentId()) == null) {
                    List<BalanceRecord> list = new ArrayList<BalanceRecord>();
                    agentRecordMap.put(balanceRecord.getAgentId(), list);
                }
                List<BalanceRecord> list = agentRecordMap.get(balanceRecord.getAgentId());
                list.add(balanceRecord);
            } else if (balanceRecord.getBizType() == BalanceRecord.BizType.SHOP.getValue()) {
                //门店汇总
                if (shopRecordMap.get(balanceRecord.getShopId()) == null) {
                    List<BalanceRecord> list = new ArrayList<BalanceRecord>();
                    shopRecordMap.put(balanceRecord.getShopId(), list);
                }
                List<BalanceRecord> list = shopRecordMap.get(balanceRecord.getShopId());
                list.add(balanceRecord);
            } else if (balanceRecord.getBizType() == BalanceRecord.BizType.AGENT_COMPANY.getValue()) {
                //运营公司汇总
                if (agentCompanyRecordMap.get(balanceRecord.getAgentCompanyId()) == null) {
                    List<BalanceRecord> list = new ArrayList<BalanceRecord>();
                    agentCompanyRecordMap.put(balanceRecord.getAgentCompanyId(), list);
                }
                List<BalanceRecord> list = agentCompanyRecordMap.get(balanceRecord.getAgentCompanyId());
                list.add(balanceRecord);
            } else if (balanceRecord.getBizType() == BalanceRecord.BizType.PARTNER.getValue()) {
                //商户汇总
                if (partnerRecordMap.get(balanceRecord.getPartnerId()) == null) {
                    List<BalanceRecord> list = new ArrayList<BalanceRecord>();
                    partnerRecordMap.put(balanceRecord.getPartnerId(), list);
                }
                List<BalanceRecord> list = partnerRecordMap.get(balanceRecord.getPartnerId());
                list.add(balanceRecord);
            }
        }

        //运营商确认
        for(Integer agentId : agentRecordMap.keySet()){
            ExtResult extResult = agentConfirm(agentId, agentRecordMap.get(agentId),  confirmOperator);
            if(extResult.getMessage() != null){
                message += extResult.getMessage();
            }
        }

        //门店确认
        for(String shopId : shopRecordMap.keySet()){
            ExtResult extResult = shopConfirm(shopId, shopRecordMap.get(shopId),  confirmOperator);
            if(extResult.getMessage() != null){
                message += extResult.getMessage();
            }
        }

        //运营公司确认
        for(String agentCompanyId : agentCompanyRecordMap.keySet()){
            ExtResult extResult = agentCompanyConfirm(agentCompanyId, agentCompanyRecordMap.get(agentCompanyId),  confirmOperator);
            if(extResult.getMessage() != null){
                message += extResult.getMessage();
            }
        }

        //平台确认
        for(Integer partnerId : partnerRecordMap.keySet()){
            ExtResult extResult = partnerConfirm(partnerId, partnerRecordMap.get(partnerId),  confirmOperator);
            if(extResult.getMessage() != null){
                message += extResult.getMessage();
            }
        }


        return ExtResult.successResult(message);
    }

    @Transactional(rollbackFor = Throwable.class)
    public ExtResult agentConfirm(int agentId, List<BalanceRecord> balanceRecordList, String confirmOperator){
        //运营商确认
        Agent agent = agentMapper.find(agentId);

        int agentRecordMoney = 0;
        for(BalanceRecord balanceRecord : balanceRecordList){
            agentRecordMoney +=balanceRecord.getMoney();
        }
        if(agentRecordMoney < 0){
            return ExtResult.failResult(String.format("运营商：%s 确认金额%d小于0，无法确认;",agent.getAgentName(),agentRecordMoney ));
        }

        //运营商余额变更
        if(agentRecordMoney != 0){
           agentMapper.updateBalance(agent.getId(),agentRecordMoney);
        }

        int balance = agent.getBalance();
        for(BalanceRecord balanceRecord : balanceRecordList){
            balanceRecordMapper.confirm(balanceRecord.getId(), BalanceRecord.Status.WAIT_CONFIRM.getValue(), BalanceRecord.Status.CONFIRM_OK.getValue(), new Date(), confirmOperator);

            //1 运营商流水收入 包时段收入金额
            if(balanceRecord.getPacketPeriodMoney() != 0){
                balance = balance + balanceRecord.getPacketPeriodMoney();
                saveAgentInOutMoney(agent.getId(), balanceRecord.getPacketPeriodMoney(), balance, AgentInOutMoney.BizType.IN_PACKET_PERIOD_RATIO.getValue(), balanceRecord.getId().toString(), AgentInOutMoney.Type.IN.getValue(), confirmOperator);
            }

            //2 运营商流水收入 按次收入金额
            if(balanceRecord.getExchangeMoney() != 0){
                balance = balance + balanceRecord.getExchangeMoney();
                saveAgentInOutMoney(agent.getId(), balanceRecord.getExchangeMoney(), balance, AgentInOutMoney.BizType.IN_EXCHANGR_RATIO.getValue(), balanceRecord.getId().toString(), AgentInOutMoney.Type.IN.getValue(), confirmOperator);
            }

            //3 运营商流水收入 保险收入金额
            if(balanceRecord.getInsuranceMoney() != 0){
                balance = balance + balanceRecord.getInsuranceMoney();
                saveAgentInOutMoney(agent.getId(), balanceRecord.getInsuranceMoney(), balance, AgentInOutMoney.BizType.IN_INSURANCE.getValue(), balanceRecord.getId().toString(),AgentInOutMoney.Type.IN.getValue(),  confirmOperator);
            }

//            //4 运营商流水收入 省代收入金额
//            if(balanceRecord.getProvinceIncome() != 0){
//                balance = balance + balanceRecord.getProvinceIncome();
//                saveAgentInOutMoney(agent.getId(), balanceRecord.getProvinceIncome(), balance, AgentInOutMoney.BizType.IN_PROVINCE.getValue(), balanceRecord.getId().toString(), AgentInOutMoney.Type.IN.getValue(), confirmOperator);
//            }
//
//            //5 运营商流水收入 市代收入金额
//            if(balanceRecord.getCityIncome() != 0){
//                balance = balance + balanceRecord.getCityIncome();
//                saveAgentInOutMoney(agent.getId(), balanceRecord.getCityIncome(), balance, AgentInOutMoney.BizType.IN_CITY.getValue(), balanceRecord.getId().toString(), AgentInOutMoney.Type.IN.getValue(), confirmOperator);
//            }

            //6 运营商流水收入 押金剩余收入金额
            if(balanceRecord.getForegiftRemainMoney() != 0){
                balance = balance + balanceRecord.getForegiftRemainMoney();
                saveAgentInOutMoney(agent.getId(), balanceRecord.getForegiftRemainMoney(), balance, AgentInOutMoney.BizType.IN_FOREGIFT_REMAIN.getValue(), balanceRecord.getId().toString(), AgentInOutMoney.Type.IN.getValue(), confirmOperator);
            }

            //7 运营商流水支出    包时段退款金额
            if(balanceRecord.getRefundPacketPeriodMoney() != 0){
                balance = balance - balanceRecord.getRefundPacketPeriodMoney();
                saveAgentInOutMoney(agent.getId(), -balanceRecord.getRefundPacketPeriodMoney(), balance, AgentInOutMoney.BizType.OUT_PACKET_PERIOD_RATIO.getValue(), balanceRecord.getId().toString(), AgentInOutMoney.Type.OUT.getValue(), confirmOperator);
            }

            //7 运营商流水支出   保险退款金额
            if(balanceRecord.getRefundInsuranceMoney() != 0){
                balance = balance - balanceRecord.getRefundInsuranceMoney();
                saveAgentInOutMoney(agent.getId(), -balanceRecord.getRefundInsuranceMoney(), balance, AgentInOutMoney.BizType.OUT_INSURANCE.getValue(), balanceRecord.getId().toString(), AgentInOutMoney.Type.OUT.getValue(), confirmOperator);
            }

            //7 运营商流水支出   抵扣券支出
            if(balanceRecord.getDeductionTicketMoney() != 0){
                balance = balance - balanceRecord.getDeductionTicketMoney();
                saveAgentInOutMoney(agent.getId(), -balanceRecord.getDeductionTicketMoney(), balance, AgentInOutMoney.BizType.OUT_DEDUCTION_TICKET.getValue(), balanceRecord.getId().toString(), AgentInOutMoney.Type.OUT.getValue(), confirmOperator);
            }
        }

        return ExtResult.successResult(String.format("运营商：%s 确认成功;",agent.getAgentName() ));
    }

    @Transactional(rollbackFor = Throwable.class)
    public ExtResult shopConfirm(String shopId, List<BalanceRecord> balanceRecordList, String confirmOperator){
        //门店确认
        Shop shop = shopMapper.find(shopId);

        int shopRecordMoney = 0;
        for(BalanceRecord balanceRecord : balanceRecordList){
            shopRecordMoney +=balanceRecord.getMoney();
        }
        if(shopRecordMoney < 0){
            return ExtResult.failResult(String.format("门店：%s 确认金额%d小于0，无法确认;",shop.getShopName(),shopRecordMoney ));
        }

        //门店余额变更
        if(shopRecordMoney != 0){
            shopMapper.updateBalance(shop.getId(),shopRecordMoney);
        }

        int balance = shop.getBalance();
        for(BalanceRecord balanceRecord : balanceRecordList){
            balanceRecordMapper.confirm(balanceRecord.getId(), BalanceRecord.Status.WAIT_CONFIRM.getValue(), BalanceRecord.Status.CONFIRM_OK.getValue(), new Date(), confirmOperator);

            //1 门店流水收入 包时段收入金额
            if(balanceRecord.getPacketPeriodMoney() != 0){
                balance = balance + balanceRecord.getPacketPeriodMoney();
                saveShopInOutMoney(shop.getId(), balanceRecord.getPacketPeriodMoney(), balance, ShopInOutMoney.BizType.PACKET_PERIOD_RATIO.getValue(), balanceRecord.getId().toString(),ShopInOutMoney.Type.INCOME.getValue(), confirmOperator);
            }

            //2 门店流水收入 按次收入金额
            if(balanceRecord.getExchangeMoney() != 0){
                balance = balance + balanceRecord.getExchangeMoney();
                saveShopInOutMoney(shop.getId(), balanceRecord.getExchangeMoney(), balance, ShopInOutMoney.BizType.EXCHANGR_RATIO.getValue(), balanceRecord.getId().toString(),ShopInOutMoney.Type.INCOME.getValue(), confirmOperator);
            }

            //3 门店流水支出    包时段退款金额
            if(balanceRecord.getRefundPacketPeriodMoney() != 0){
                balance = balance - balanceRecord.getRefundPacketPeriodMoney();
                saveShopInOutMoney(shop.getId(), -balanceRecord.getRefundPacketPeriodMoney(), balance, ShopInOutMoney.BizType.REFUND_PACKET_PERIOD_RATIO.getValue(), balanceRecord.getId().toString(),ShopInOutMoney.Type.PAY.getValue(), confirmOperator);
            }
        }

        return ExtResult.successResult(String.format("门店：%s 确认成功;",shop.getShopName() ));
    }

    @Transactional(rollbackFor = Throwable.class)
    public ExtResult agentCompanyConfirm(String agentCompanyId, List<BalanceRecord> balanceRecordList, String confirmOperator){
        //运营公司确认
        AgentCompany agentCompany = agentCompanyMapper.find(agentCompanyId);

        int agentCompanyRecordMoney = 0;
        for(BalanceRecord balanceRecord : balanceRecordList){
            agentCompanyRecordMoney +=balanceRecord.getMoney();
        }
        if(agentCompanyRecordMoney < 0){
            return ExtResult.failResult(String.format("运营公司：%s 确认金额%d小于0，无法确认;", agentCompany.getCompanyName(), agentCompanyRecordMoney));
        }

        //运营公司余额变更
        if(agentCompanyRecordMoney != 0){
            agentCompanyMapper.updateBalance(agentCompanyId, agentCompanyRecordMoney);
        }

        int balance = agentCompany.getBalance();
        for(BalanceRecord balanceRecord : balanceRecordList){
            balanceRecordMapper.confirm(balanceRecord.getId(), BalanceRecord.Status.WAIT_CONFIRM.getValue(), BalanceRecord.Status.CONFIRM_OK.getValue(), new Date(), confirmOperator);

            if(balanceRecord.getMoney() != 0){
                balance = balance + balanceRecord.getMoney();
                if(balanceRecord.getCategory() == ConstEnum.Category.EXCHANGE.getValue()){
                    saveAgentCompanyInOutMoney(agentCompany.getId(), balanceRecord.getMoney(), balance, AgentCompanyInOutMoney.BizType.IN_EXCHANGE_BALANCE_RECORD.getValue(), balanceRecord.getId().toString(), AgentCompanyInOutMoney.Type.INCOME.getValue(), confirmOperator);
                }else if(balanceRecord.getCategory() == ConstEnum.Category.RENT.getValue()){
                    saveAgentCompanyInOutMoney(agentCompany.getId(), balanceRecord.getMoney(), balance, AgentCompanyInOutMoney.BizType.IN_RENT_BALANCE_RECORD.getValue(), balanceRecord.getId().toString(), AgentCompanyInOutMoney.Type.INCOME.getValue(), confirmOperator);
                }
            }
        }

        return ExtResult.successResult(String.format("运营公司：%s 确认成功;",agentCompany.getCompanyName() ));
    }

    @Transactional(rollbackFor = Throwable.class)
    public ExtResult partnerConfirm(int partnerId, List<BalanceRecord> balanceRecordList, String confirmOperator){
        //平台确认
        PlatformAccount platformAccount = platformAccountMapper.find(partnerId);

        int partnerRecordMoney = 0;
        for(BalanceRecord balanceRecord : balanceRecordList){
            partnerRecordMoney +=balanceRecord.getMoney();
        }
        if(partnerRecordMoney < 0){
            return ExtResult.failResult(String.format("平台：%s 确认金额%d小于0，无法确认;",platformAccount.getPartnerName(),partnerRecordMoney ));
        }

        //平台余额变更
        if(partnerRecordMoney != 0){
            platformAccountMapper.updateBalance(platformAccount.getId(),partnerRecordMoney);
        }

        int balance = platformAccount.getBalance();
        for(BalanceRecord balanceRecord : balanceRecordList){
            balanceRecordMapper.confirm(balanceRecord.getId(), BalanceRecord.Status.WAIT_CONFIRM.getValue(), BalanceRecord.Status.CONFIRM_OK.getValue(), new Date(), confirmOperator);

            //1 平台流水收入 包时段收入金额
            if(balanceRecord.getPacketPeriodMoney() != 0){
                balance = balance + balanceRecord.getPacketPeriodMoney();
                savePlatformAccountInOutMoney(platformAccount.getId(), balanceRecord.getPacketPeriodMoney(), balance, PlatformAccountInOutMoney.BizType.IN_PACKET_PERIOD_RATIO.getValue(), balanceRecord.getId().toString(),PlatformAccountInOutMoney.Type.IN.getValue(), confirmOperator);
            }

            //2 平台流水收入 按次收入金额
            if(balanceRecord.getExchangeMoney() != 0){
                balance = balance + balanceRecord.getExchangeMoney();
                savePlatformAccountInOutMoney(platformAccount.getId(), balanceRecord.getExchangeMoney(), balance, PlatformAccountInOutMoney.BizType.IN_EXCHANGR_RATIO.getValue(), balanceRecord.getId().toString(),PlatformAccountInOutMoney.Type.IN.getValue(), confirmOperator);
            }

            //3 平台流水支出    包时段退款金额
            if(balanceRecord.getRefundPacketPeriodMoney() != 0){
                balance = balance - balanceRecord.getRefundPacketPeriodMoney();
                savePlatformAccountInOutMoney(platformAccount.getId(), -balanceRecord.getRefundPacketPeriodMoney(), balance, PlatformAccountInOutMoney.BizType.OUT_PACKET_PERIOD_RATIO.getValue(), balanceRecord.getId().toString(),PlatformAccountInOutMoney.Type.OUT.getValue(), confirmOperator);
            }
        }

        return ExtResult.successResult(String.format("平台：%s 确认成功;",platformAccount.getPartnerName() ));
    }

    private void saveAgentInOutMoney(int agentId, int money, int balance, int bizType, String bizId, int type, String operator){
        AgentInOutMoney agentInOutMoney = new AgentInOutMoney();
        agentInOutMoney.setMoney(money);
        agentInOutMoney.setBalance(balance);
        agentInOutMoney.setAgentId(agentId);
        agentInOutMoney.setBizId(bizId);
        agentInOutMoney.setCreateTime(new Date());
        agentInOutMoney.setBizType(bizType);
        agentInOutMoney.setType(type);
        agentInOutMoney.setOperator(operator);
        agentInOutMoneyMapper.insert(agentInOutMoney);
    }

    private void saveShopInOutMoney(String shopId, int money, int balance, int bizType, String bizId, int type, String operator){
        ShopInOutMoney inOutMoney = new ShopInOutMoney();
        inOutMoney.setShopId(shopId);
        inOutMoney.setBizType(bizType);
        inOutMoney.setBizId(bizId);
        inOutMoney.setType(type);
        inOutMoney.setMoney(money);
        inOutMoney.setBalance(balance);
        inOutMoney.setCreateTime(new Date());
        inOutMoney.setOperator(operator);

        shopInOutMoneyMapper.insert(inOutMoney);
    }

    public  void saveAgentCompanyInOutMoney(String agentCompanyId, int money, int balance, int bizType, String bizId, int type, String operator){
        AgentCompanyInOutMoney inOutMoney = new AgentCompanyInOutMoney();
        inOutMoney.setAgentCompanyId(agentCompanyId);
        inOutMoney.setBizType(bizType);
        inOutMoney.setBizId(bizId);
        inOutMoney.setType(type);
        inOutMoney.setMoney(money);
        inOutMoney.setBalance(balance);
        inOutMoney.setCreateTime(new Date());
        inOutMoney.setOperator(operator);

        agentCompanyInOutMoneyMapper.insert(inOutMoney);
    }

    private void savePlatformAccountInOutMoney(Integer platformAccountId, int money, int balance, int bizType, String bizId, int type, String operator){
        PlatformAccountInOutMoney inOutMoney = new PlatformAccountInOutMoney();
        inOutMoney.setPlatformAccountId(platformAccountId);
        inOutMoney.setBizType(bizType);
        inOutMoney.setBizId(bizId);
        inOutMoney.setType(type);
        inOutMoney.setMoney(money);
        inOutMoney.setBalance(balance);
        inOutMoney.setCreateTime(new Date());
        inOutMoney.setOperator(operator);

        platformAccountInOutMoneyMapper.insert(inOutMoney);
    }
}
