package cn.com.yusong.yhdg.webserver.service.hdg;

import cn.com.yusong.yhdg.common.domain.basic.*;
import cn.com.yusong.yhdg.common.domain.hdg.ExchangeWhiteList;
import cn.com.yusong.yhdg.common.entity.json.ExtResult;
import cn.com.yusong.yhdg.common.entity.pagination.Page;
import cn.com.yusong.yhdg.webserver.constant.RespCode;
import cn.com.yusong.yhdg.webserver.entity.result.RestResult;
import cn.com.yusong.yhdg.webserver.persistence.basic.*;
import cn.com.yusong.yhdg.webserver.persistence.hdg.ExchangeWhiteListMapper;
import cn.com.yusong.yhdg.webserver.service.AbstractService;
import cn.com.yusong.yhdg.webserver.service.basic.CustomerService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class ExchangeWhiteListService extends AbstractService {

    @Autowired
    ExchangeWhiteListMapper exchangeWhiteListMapper;
    @Autowired
    CustomerMapper customerMapper;
    @Autowired
    AgentBatteryTypeMapper agentBatteryTypeMapper;
    @Autowired
    CustomerExchangeInfoMapper customerExchangeInfoMapper;
    @Autowired
    CustomerForegiftOrderMapper customerForegiftOrderMapper;
    @Autowired
    CustomerExchangeBatteryMapper customerExchangeBatteryMapper;
    @Autowired
    CustomerInOutMoneyMapper customerInOutMoneyMapper;
    @Autowired
    CustomerAgentBalanceMapper customerAgentBalanceMapper;
    @Autowired
    AgentMapper agentMapper;
    @Autowired
    AgentForegiftInOutMoneyMapper agentForegiftInOutMoneyMapper;

    public ExchangeWhiteList find(Long id) {
        return exchangeWhiteListMapper.find(id);
    }

    public Page findPage(ExchangeWhiteList exchangeWhiteList) {
        Page page = exchangeWhiteList.buildPage();
        page.setTotalItems(exchangeWhiteListMapper.findPageCount(exchangeWhiteList));
        exchangeWhiteList.setBeginIndex(page.getOffset());
        List<ExchangeWhiteList> exchangeWhiteListList = exchangeWhiteListMapper.findPageResult(exchangeWhiteList);
        for (ExchangeWhiteList whiteList : exchangeWhiteListList) {
            AgentInfo agentInfo = findAgentInfo(whiteList.getAgentId());
            if (agentInfo != null) {
                whiteList.setAgentName(agentInfo.getAgentName());
            }
            if (whiteList.getBatteryType() != null) {
                AgentBatteryType agentBatteryType = agentBatteryTypeMapper.find(whiteList.getBatteryType(), whiteList.getAgentId());
                if (agentBatteryType != null) {
                    whiteList.setTypeName(agentBatteryType.getTypeName());
                }
            }
        }
        page.setResult(exchangeWhiteListList);

        return page;
    }

    @Transactional(rollbackFor = Throwable.class)
    public ExtResult create(ExchangeWhiteList exchangeWhiteList) {
        ExchangeWhiteList dbExchangeWhiteList = exchangeWhiteListMapper.findByCustomerId(exchangeWhiteList.getCustomerId());
        if (dbExchangeWhiteList != null) {
            return ExtResult.failResult("白名单已存在该用户，加入失败");
        } else {
            //存在电池，不能加入白名单
            if (customerExchangeBatteryMapper.exists(exchangeWhiteList.getCustomerId()) > 0) {
                return ExtResult.failResult("客户已存在电池，无法加入白名单");
            }
            //如果存在押金订单
            CustomerExchangeInfo customerExchangeInfo = customerExchangeInfoMapper.find(exchangeWhiteList.getCustomerId());
            if (customerExchangeInfo != null) {
                if (customerExchangeInfo.getForegiftOrderId() != null) {
                    CustomerForegiftOrder customerForegiftOrder = customerForegiftOrderMapper.find(customerExchangeInfo.getForegiftOrderId());
                    if (customerForegiftOrder != null && customerForegiftOrder.getStatus() == CustomerForegiftOrder.Status.PAY_OK.getValue()) {
                        return ExtResult.failResult("客户存在押金，无法加入白名单");
                    }
                }
            }

            //加入白名单
            ExchangeWhiteList newExchangeWhiteList = new ExchangeWhiteList();
            newExchangeWhiteList.setAgentId(exchangeWhiteList.getAgentId());
            newExchangeWhiteList.setCustomerId(exchangeWhiteList.getCustomerId());
            newExchangeWhiteList.setBatteryType(exchangeWhiteList.getBatteryType());
            newExchangeWhiteList.setMobile(exchangeWhiteList.getMobile());
            newExchangeWhiteList.setFullname(exchangeWhiteList.getFullname());
            newExchangeWhiteList.setCreateTime(new Date());
            exchangeWhiteListMapper.insert(newExchangeWhiteList);

            //更新用户的运营商id
            customerMapper.updateAgentId(exchangeWhiteList.getCustomerId(), exchangeWhiteList.getAgentId());
            return ExtResult.successResult();
        }
    }

    @Transactional(rollbackFor = Throwable.class)
    public ExtResult update(ExchangeWhiteList exchangeWhiteList) {
        ExchangeWhiteList dbExchangeWhiteList = exchangeWhiteListMapper.findByCustomerId(exchangeWhiteList.getCustomerId());
        if (dbExchangeWhiteList != null && !dbExchangeWhiteList.getId().equals(exchangeWhiteList.getId())) {
            return ExtResult.failResult("该用户已加入其他运营商的白名单，加入失败");
        } else {
            //存在电池，不能加入白名单
            if (customerExchangeBatteryMapper.exists(exchangeWhiteList.getCustomerId()) > 0) {
                return ExtResult.failResult("客户已存在电池，无法加入白名单");
            }
            //如果存在押金订单
            CustomerExchangeInfo customerExchangeInfo = customerExchangeInfoMapper.find(exchangeWhiteList.getCustomerId());
            if (customerExchangeInfo != null) {
                if (customerExchangeInfo.getForegiftOrderId() != null) {
                    CustomerForegiftOrder customerForegiftOrder = customerForegiftOrderMapper.find(customerExchangeInfo.getForegiftOrderId());
                    if (customerForegiftOrder != null && customerForegiftOrder.getStatus() == CustomerForegiftOrder.Status.PAY_OK.getValue()) {
                        return ExtResult.failResult("客户存在押金，无法加入白名单");
                    }
                }
            }

            //加入白名单
            ExchangeWhiteList newExchangeWhiteList = new ExchangeWhiteList();
            newExchangeWhiteList.setId(exchangeWhiteList.getId());
            newExchangeWhiteList.setAgentId(exchangeWhiteList.getAgentId());
            newExchangeWhiteList.setBatteryType(exchangeWhiteList.getBatteryType());
            newExchangeWhiteList.setCustomerId(exchangeWhiteList.getCustomerId());
            newExchangeWhiteList.setMobile(exchangeWhiteList.getMobile());
            newExchangeWhiteList.setFullname(exchangeWhiteList.getFullname());
            newExchangeWhiteList.setCreateTime(new Date());
            if (exchangeWhiteListMapper.update(newExchangeWhiteList) >= 1) {
                //更新用户的运营商id
                customerMapper.updateAgentId(exchangeWhiteList.getCustomerId(), exchangeWhiteList.getAgentId());
                return ExtResult.successResult();
            } else {
                return ExtResult.failResult("加入白名单失败");
            }
        }
    }

    public ExtResult delete(long id) {
        exchangeWhiteListMapper.delete(id);
        return ExtResult.successResult();
    }

}
