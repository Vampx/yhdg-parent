package cn.com.yusong.yhdg.serviceserver.service.basic;

import cn.com.yusong.yhdg.common.constant.ConstEnum;
import cn.com.yusong.yhdg.common.constant.Constant;
import cn.com.yusong.yhdg.common.domain.basic.*;
import cn.com.yusong.yhdg.serviceserver.persistence.basic.*;
import cn.com.yusong.yhdg.serviceserver.service.AbstractService;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.lang.time.DateUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class CustomerCouponTicketService extends AbstractService {

    private static final Logger log = LogManager.getLogger(CustomerCouponTicketService.class);

    @Autowired
    CustomerMapper customerMapper;
    @Autowired
    CustomerCouponTicketGiftMapper customerCouponTicketGiftMapper;
    @Autowired
    CustomerCouponTicketMapper customerCouponTicketMapper;
    @Autowired
    CustomerNoticeMessageMapper customerNoticeMessageMapper;
    @Autowired
    PushMetaDataMapper pushMetaDataMapper;
    @Autowired
    WagesDayTicketGiftMapper wagesDayTicketGiftMapper;
    public void expire() {
        List<Integer> fromStatus = Arrays.asList(CustomerCouponTicket.Status.NOT_USER.getValue());
        int limit = 1000;
        Date date = new Date();
        while (true) {
            if (customerCouponTicketMapper.updateExpiredTicket(fromStatus, CustomerCouponTicket.Status.EXPIRED.getValue(), date, limit) < limit) {
                break;
            }
        }
    }

    public void wagesDay(){
        int offset = 0, limit = 1000;
        String statsDate = DateFormatUtils.format(new Date(), Constant.DATE_FORMAT);
        String wagesDay = statsDate.substring(statsDate.lastIndexOf("-") + 1, statsDate.length());
        wagesDay=wagesDay.replaceFirst("^0*", "");
        while (true) {
            List<CustomerCouponTicketGift> byTypeCategoryWagesDayAll = customerCouponTicketGiftMapper.findByTypeCategoryWagesDayAll(CustomerCouponTicketGift.Type.WAGES_GIVE.getValue(),wagesDay,offset,limit);
            if (byTypeCategoryWagesDayAll.isEmpty()) {
                break;
            }
            for (CustomerCouponTicketGift customerCouponTicketGift: byTypeCategoryWagesDayAll) {
                WagesDayTicketGift wagesDayTicketGift =new WagesDayTicketGift();
                wagesDayTicketGift.setTicketGiftId(customerCouponTicketGift.getId());
                List<WagesDayTicketGift> allWagesDay = wagesDayTicketGiftMapper.findAllWagesDay(wagesDayTicketGift);
                if(!allWagesDay.isEmpty()){
                    for (WagesDayTicketGift wagesDayTicketGift1: allWagesDay) {
                        super.giveCouponTicket(wagesDayTicketGift1.getCategory(), customerCouponTicketGift.getType(), wagesDayTicketGift1.getAgentId(), CustomerCouponTicket.TicketType.RENT.getValue(), wagesDayTicketGift1.getCustomerMobile(), customerCouponTicketGift.getId());
                    }
                }

            }
            offset += limit;
        }
    }

    public void willExpirePush() {
        //查询到期时间
        Date nowDate = DateUtils.addSeconds(DateUtils.truncate(DateUtils.addDays(new Date(), 1), Calendar.DAY_OF_MONTH),-1);
        Date expireDate = DateUtils.addDays(nowDate, 3);

        log.debug("要到期的优惠券推送...");
        int  offset = 0,limit = 1000;

        while (true) {
            List<CustomerCouponTicket> orderList = customerCouponTicketMapper.findWillExpire(CustomerCouponTicket.Status.NOT_USER.getValue(), expireDate, offset,  limit);
            if(orderList.isEmpty()){
                break;
            }

            for(CustomerCouponTicket ticket : orderList){
                Customer customer = customerMapper.findByMobile(ticket.getPartnerId(), ticket.getCustomerMobile());
                if(customer == null){
                    continue;
                }

                //如果当天有推送，不再推送
                CustomerNoticeMessage customerNoticeMessage = customerNoticeMessageMapper.findByToday(customer.getId(), CustomerNoticeMessage.Type.CUSTOMER_COUPON_TICKET_EXPIRE.getValue());
                if(customerNoticeMessage != null){
                    continue;
                }

                //推送客户
                PushMetaData pushMetaData = new PushMetaData();
                pushMetaData.setSourceType(PushMessage.SourceType.CUSTOMER_COUPON_TICKET_EXPIRE.getValue());
                pushMetaData.setSourceId(ticket.getId().toString());
                pushMetaData.setCreateTime(new Date());
                pushMetaDataMapper.insert(pushMetaData);
            }

            offset += limit;
        }

        log.debug("要到期的优惠券推送结束...");
    }

}
