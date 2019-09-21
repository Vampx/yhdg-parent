package cn.com.yusong.yhdg.webserver.service.basic;

import cn.com.yusong.yhdg.common.constant.ConstEnum;
import cn.com.yusong.yhdg.common.domain.basic.*;
import cn.com.yusong.yhdg.common.domain.hdg.Shop;
import cn.com.yusong.yhdg.common.entity.json.ExtResult;
import cn.com.yusong.yhdg.common.entity.pagination.Page;
import cn.com.yusong.yhdg.webserver.persistence.basic.*;
import cn.com.yusong.yhdg.webserver.persistence.hdg.ShopMapper;
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
    AgentCompanyMapper agentCompanyMapper;
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

            if(balanceRecord.getMoney() != 0){
                balance = balance + balanceRecord.getMoney();
                if(balanceRecord.getCategory() == ConstEnum.Category.EXCHANGE.getValue()){
                    saveAgentInOutMoney(agent.getId(), balanceRecord.getMoney(), balance, AgentInOutMoney.BizType.IN_EXCHANGE_BALANCE_RECORD.getValue(), balanceRecord.getId().toString(), AgentInOutMoney.Type.IN.getValue(), confirmOperator);
                }else if(balanceRecord.getCategory() == ConstEnum.Category.RENT.getValue()){
                    saveAgentInOutMoney(agent.getId(), balanceRecord.getMoney(), balance, AgentInOutMoney.BizType.IN_RENT_BALANCE_RECORD.getValue(), balanceRecord.getId().toString(), AgentInOutMoney.Type.IN.getValue(), confirmOperator);
                }
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

            if(balanceRecord.getMoney() != 0){
                balance = balance + balanceRecord.getMoney();
                if(balanceRecord.getCategory() == ConstEnum.Category.EXCHANGE.getValue()){
                    saveShopInOutMoney(shop.getId(), balanceRecord.getMoney(), balance, ShopInOutMoney.BizType.IN_EXCHANGE_BALANCE_RECORD.getValue(), balanceRecord.getId().toString(), ShopInOutMoney.Type.INCOME.getValue(), confirmOperator);
                }else if(balanceRecord.getCategory() == ConstEnum.Category.RENT.getValue()){
                    saveShopInOutMoney(shop.getId(), balanceRecord.getMoney(), balance, ShopInOutMoney.BizType.IN_RENT_BALANCE_RECORD.getValue(), balanceRecord.getId().toString(), ShopInOutMoney.Type.INCOME.getValue(), confirmOperator);
                }
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

            if(balanceRecord.getMoney() != 0){
                balance = balance + balanceRecord.getMoney();
                if(balanceRecord.getCategory() == ConstEnum.Category.EXCHANGE.getValue()){
                    savePlatformAccountInOutMoney(platformAccount.getId(), balanceRecord.getMoney(), balance, PlatformAccountInOutMoney.BizType.IN_EXCHANGE_BALANCE_RECORD.getValue(), balanceRecord.getId().toString(), PlatformAccountInOutMoney.Type.IN.getValue(), confirmOperator);
                }else if(balanceRecord.getCategory() == ConstEnum.Category.RENT.getValue()){
                    savePlatformAccountInOutMoney(platformAccount.getId(), balanceRecord.getMoney(), balance, PlatformAccountInOutMoney.BizType.IN_RENT_BALANCE_RECORD.getValue(), balanceRecord.getId().toString(), PlatformAccountInOutMoney.Type.IN.getValue(), confirmOperator);
                }
            }
        }

        return ExtResult.successResult(String.format("平台：%s 确认成功;",platformAccount.getPartnerName() ));
    }

    public  void saveAgentInOutMoney(int agentId, int money, int balance, int bizType, String bizId, int type, String operator){
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

    public  void saveShopInOutMoney(String shopId, int money, int balance, int bizType, String bizId, int type, String operator){
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

    public  void savePlatformAccountInOutMoney(Integer platformAccountId, int money, int balance, int bizType, String bizId, int type, String operator){

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

    public ExtResult confirmStatus(Long[] ids, Date confirmTime, String confirmUser) {
        int effect = 0;
        for (Long id : ids) {
            effect += balanceRecordMapper.confirm(id, BalanceRecord.Status.WAIT_CONFIRM.getValue(), BalanceRecord.Status.CONFIRM_OK.getValue(), confirmTime, confirmUser);
        }
        return ExtResult.successResult(String.format("成功确认%d条", effect));
    }

    public ExtResult confirmStatusOffline(Long[] ids, Date confirmTime, String confirmOperator, String memo, String imagePath) {
        int effect = 0;
        for (Long id : ids) {
            effect += balanceRecordMapper.confirmStatusOffline(id, BalanceRecord.Status.WAIT_CONFIRM.getValue(), BalanceRecord.Status.CONFIRM_OK_BY_OFFLINE.getValue(), confirmTime, confirmOperator, memo, imagePath);
        }
        return ExtResult.successResult(String.format("成功确认%d条", effect));
    }
}
