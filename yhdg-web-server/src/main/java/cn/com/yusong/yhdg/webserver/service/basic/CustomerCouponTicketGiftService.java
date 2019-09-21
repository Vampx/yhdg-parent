package cn.com.yusong.yhdg.webserver.service.basic;

import cn.com.yusong.yhdg.common.domain.basic.Agent;
import cn.com.yusong.yhdg.common.domain.basic.CustomerCouponTicketGift;
import cn.com.yusong.yhdg.common.domain.basic.WagesDayTicketGift;
import cn.com.yusong.yhdg.common.entity.json.ExtResult;
import cn.com.yusong.yhdg.common.entity.pagination.Page;
import cn.com.yusong.yhdg.webserver.persistence.basic.AgentMapper;
import cn.com.yusong.yhdg.webserver.persistence.basic.CustomerCouponTicketGiftMapper;
import cn.com.yusong.yhdg.webserver.persistence.basic.WagesDayTicketGiftMapper;
import cn.com.yusong.yhdg.webserver.service.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CustomerCouponTicketGiftService extends AbstractService {

    @Autowired
    CustomerCouponTicketGiftMapper customerCouponTicketGiftMapper;
    @Autowired
    AgentMapper agentMapper;
    @Autowired
    WagesDayTicketGiftMapper wagesDayTicketGiftMapper;

    public Page findPage(CustomerCouponTicketGift search) {
        Page page = search.buildPage();
        page.setTotalItems(customerCouponTicketGiftMapper.findPageCount(search));
        search.setBeginIndex(page.getOffset());
        List<CustomerCouponTicketGift> list = customerCouponTicketGiftMapper.findPageResult(search);
        for (CustomerCouponTicketGift customerCouponTicketGift: list) {
            if (customerCouponTicketGift.getAgentId() != null) {
                customerCouponTicketGift.setAgentName(findAgentInfo(customerCouponTicketGift.getAgentId()).getAgentName());
            }
        }
        page.setResult(list);
        return page;
    }

    public ExtResult create(CustomerCouponTicketGift entity) {
        if(entity.getAgentId() == null || entity.getAgentId() <= 0){
            return ExtResult.failResult("运营商不能为空");
        }
        if(entity.getDayCount() == null || entity.getDayCount() <= 0){
            return ExtResult.failResult("请输入正确的天数");
        }
        if(entity.getMoney() == null || entity.getMoney() <= 0){
            return ExtResult.failResult("请输入正确的金额");
        }
        if (entity.getType() == CustomerCouponTicketGift.Type.BUY_FOREGIFT.getValue()) {
            if (customerCouponTicketGiftMapper.findByAgentId(entity.getAgentId(), entity.getType(), entity.getCategory(), null, null,null) != null) {
                return ExtResult.failResult("存在同类型套餐！");
            }
        } else if (entity.getType() == CustomerCouponTicketGift.Type.BUY_RENT.getValue()){
            if (customerCouponTicketGiftMapper.findByAgentId(entity.getAgentId(), entity.getType(), entity.getCategory(), entity.getPayCount(), null,null) != null) {
                return ExtResult.failResult("存在同类型套餐！");
            }
        } else if (entity.getType() == CustomerCouponTicketGift.Type.WAGES_GIVE.getValue()){
            if (customerCouponTicketGiftMapper.findByAgentId(entity.getAgentId(), entity.getType(), entity.getCategory(), null, entity.getDayCount(),entity.getWagesDay()) != null) {
                return ExtResult.failResult("存在同类型套餐！");
            }
        }
        //非支付租金 购买天数默认为0
        if (entity.getPayCount() == null && entity.getType() != CustomerCouponTicketGift.Type.BUY_RENT.getValue()) {
            entity.setPayCount(0);
        }
        int total = customerCouponTicketGiftMapper.insert(entity);
        if (total == 0) {
            ExtResult.failResult("操作失败！");
        }
        return ExtResult.successResult();
    }

    public CustomerCouponTicketGift find(long id) {
        CustomerCouponTicketGift customerCouponTicketGift = customerCouponTicketGiftMapper.find(id);
        Agent agent = agentMapper.find(customerCouponTicketGift.getAgentId());
        if (agent != null) {
            customerCouponTicketGift.setAgentName(agent.getAgentName());
        }
        return customerCouponTicketGift;
    }

    public ExtResult update(CustomerCouponTicketGift entity) {
        if(entity.getDayCount() == null || entity.getDayCount() <= 0){
            return ExtResult.failResult("请输入正确的天数");
        }
        if(entity.getMoney() == null || entity.getMoney() <= 0){
            return ExtResult.failResult("请输入正确的金额");
        }
        if (entity.getType() == CustomerCouponTicketGift.Type.BUY_RENT.getValue()){
            CustomerCouponTicketGift byAgentId = customerCouponTicketGiftMapper.findByAgentId(entity.getAgentId(), entity.getType(), entity.getCategory(), entity.getPayCount(), null, null);
            if (byAgentId != null&&!byAgentId.getId().equals(entity.getId())) {
                    return ExtResult.failResult("存在同类型套餐！");
            }
        } else if (entity.getType() == CustomerCouponTicketGift.Type.WAGES_GIVE.getValue()){
            CustomerCouponTicketGift byAgentId = customerCouponTicketGiftMapper.findByAgentId(entity.getAgentId(), entity.getType(), entity.getCategory(), null, entity.getDayCount(), entity.getWagesDay());
            if (byAgentId != null&&!byAgentId.getId().equals(entity.getId())) {
                return ExtResult.failResult("存在同类型套餐！");
            }
        }
        //非支付租金 购买天数默认为0
        if (entity.getType() != CustomerCouponTicketGift.Type.BUY_RENT.getValue()) {
            entity.setPayCount(0);
        }
        int total = customerCouponTicketGiftMapper.update(entity);
        if (total == 0) {
            ExtResult.failResult("操作失败！");
        }
        return ExtResult.successResult();
    }

    public ExtResult delete(Long id) {
        WagesDayTicketGift wagesDayTicketGift=new WagesDayTicketGift();
        wagesDayTicketGift.setTicketGiftId(id);
        List<WagesDayTicketGift> allWagesDay = wagesDayTicketGiftMapper.findAllWagesDay(wagesDayTicketGift);

       List <Integer> list =new ArrayList<Integer>();
        for (WagesDayTicketGift wages: allWagesDay) {
            list.add(wages.getId().intValue());
        }
        if(list.size()>0){
            int delete = wagesDayTicketGiftMapper.deleteAll(list);
            if (delete ==0){
                ExtResult.failResult("配置操作失败！");
            }
        }

        int total = customerCouponTicketGiftMapper.delete(id);
        if (total == 0) {
            ExtResult.failResult("操作失败！");
        }

        return ExtResult.successResult();
    }
}
