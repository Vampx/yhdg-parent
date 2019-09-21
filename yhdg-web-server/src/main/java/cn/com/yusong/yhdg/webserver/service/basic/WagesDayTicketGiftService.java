package cn.com.yusong.yhdg.webserver.service.basic;

import cn.com.yusong.yhdg.common.constant.ConstEnum;
import cn.com.yusong.yhdg.common.domain.basic.*;
import cn.com.yusong.yhdg.common.domain.hdg.Cabinet;
import cn.com.yusong.yhdg.common.entity.json.ExtResult;
import cn.com.yusong.yhdg.common.entity.json.PageResult;
import cn.com.yusong.yhdg.common.entity.pagination.Page;
import cn.com.yusong.yhdg.webserver.persistence.basic.CustomerExchangeInfoMapper;
import cn.com.yusong.yhdg.webserver.persistence.basic.CustomerMapper;
import cn.com.yusong.yhdg.webserver.persistence.basic.CustomerNoticeMessageMapper;
import cn.com.yusong.yhdg.webserver.persistence.basic.WagesDayTicketGiftMapper;
import cn.com.yusong.yhdg.webserver.persistence.hdg.CabinetMapper;
import cn.com.yusong.yhdg.webserver.persistence.hdg.CustomerDayStatsMapper;
import cn.com.yusong.yhdg.webserver.service.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WagesDayTicketGiftService extends AbstractService {

    @Autowired
    WagesDayTicketGiftMapper wagesDayTicketGiftMapper;

    public Page findPage(WagesDayTicketGift wages){
        Page page = wages.buildPage();
        page.setTotalItems(wagesDayTicketGiftMapper.findPageCount(wages));
        wages.setBeginIndex(page.getOffset());
        List<WagesDayTicketGift> pageResult = wagesDayTicketGiftMapper.findPageResult(wages);
        for (WagesDayTicketGift wagesDayTicketGift: pageResult) {
            wagesDayTicketGift.setAgentName(findAgentInfo(wagesDayTicketGift.getAgentId()).getAgentName());
            wagesDayTicketGift.setNewType(CustomerCouponTicketGift.Type.getName(findCustomerCouponTicketGift(wagesDayTicketGift.getTicketGiftId().intValue()).getType()));
        }
        page.setResult(pageResult);
        return page;

    }
    public ExtResult del(Long id){
        WagesDayTicketGift wagesDayTicketGift = wagesDayTicketGiftMapper.find(id);
        if (wagesDayTicketGift==null){
            return ExtResult.failResult("没有此绑定信息");
        }
        int delete = wagesDayTicketGiftMapper.delete(wagesDayTicketGift.getId());
        if (delete==0){
            return ExtResult.failResult("解绑失败");
        }else{
            return ExtResult.successResult("解绑成功");
        }
    }
    public ExtResult insert(String mobile,CustomerCouponTicketGift customerCouponTicketGift){

        Integer agentId = customerCouponTicketGift.getAgentId();
        WagesDayTicketGift wagesDayTicketGift = new WagesDayTicketGift();
        wagesDayTicketGift.setAgentId(agentId);
        wagesDayTicketGift.setCategory(customerCouponTicketGift.getCategory());
        wagesDayTicketGift.setCustomerMobile(mobile);
        int pageCount = wagesDayTicketGiftMapper.findPageCount(wagesDayTicketGift);
        if(pageCount!=0){
            return ExtResult.failResult("此手机号已绑定此运营商其他工作日优惠卷");
        }else{
            wagesDayTicketGift.setCategory(customerCouponTicketGift.getCategory());
            wagesDayTicketGift.setTicketGiftId(customerCouponTicketGift.getId());
            int insert = wagesDayTicketGiftMapper.insert(wagesDayTicketGift);
            if (insert<=0){
                return ExtResult.failResult("绑定失败");
            }else {
                return ExtResult.successResult("绑定成功");
            }
        }

    }






}
