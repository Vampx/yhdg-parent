package cn.com.yusong.yhdg.serviceserver.service.zd;

import cn.com.yusong.yhdg.common.domain.zd.CustomerRentInfo;
import cn.com.yusong.yhdg.common.domain.zd.RentPeriodOrder;
import cn.com.yusong.yhdg.serviceserver.persistence.basic.PushMetaDataMapper;
import cn.com.yusong.yhdg.serviceserver.persistence.basic.SystemConfigMapper;
import cn.com.yusong.yhdg.serviceserver.persistence.zd.CustomerRentInfoMapper;
import cn.com.yusong.yhdg.serviceserver.persistence.zd.RentPeriodOrderMapper;
import cn.com.yusong.yhdg.serviceserver.service.AbstractService;
import org.apache.commons.lang.time.DateUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
public class RentPeriodOrderService extends AbstractService {
    final static Logger log = LogManager.getLogger(RentPeriodOrderService.class);
    @Autowired
    CustomerRentInfoMapper customerRentInfoMapper;
    @Autowired
    RentPeriodOrderMapper rentPeriodOrderMapper;
    @Autowired
    SystemConfigMapper systemConfigMapper;
    @Autowired
    PushMetaDataMapper pushMetaDataMapper;

    public void used() {
        log.debug("租电套餐状态未使用->已使用更新开始...");
        int offset = 0,limit = 100;

        while (true) {
            //先查询未使用的套餐，如果未使用的套餐存在，并且用户有押金，并且没有使用中的套餐，将未使用更新为已使用
            List<RentPeriodOrder>  orderList = rentPeriodOrderMapper.findListByStatus(RentPeriodOrder.Status.NOT_USE.getValue(), offset, limit);
            if(orderList.isEmpty()){
                break;
            }
            for(RentPeriodOrder rentPeriodOrder : orderList){
                CustomerRentInfo customerRentInfo = customerRentInfoMapper.find(rentPeriodOrder.getCustomerId());
                if(customerRentInfo != null){
                    RentPeriodOrder usedOrder = rentPeriodOrderMapper.findUsedByCustomer(customerRentInfo.getId(), RentPeriodOrder.Status.USED.getValue());
                    if(usedOrder == null){
                        Date beginTime = DateUtils.truncate(new Date(), Calendar.DAY_OF_MONTH);
                        Date endTime = DateUtils.addSeconds(DateUtils.truncate(DateUtils.addDays(beginTime, rentPeriodOrder.getDayCount()), Calendar.DAY_OF_MONTH), -1);
                        rentPeriodOrderMapper.updateUsedOrder(rentPeriodOrder.getId(), RentPeriodOrder.Status.NOT_USE.getValue(), RentPeriodOrder.Status.USED.getValue(), beginTime, endTime);
                    }
                }
            }
            offset += limit;
        }
        log.debug("租电套餐状态未使用->已使用更新结束...");
    }

    public void expire() {
        log.debug("租电套餐状态已使用->已过期更新开始...");
        int limit = 1000;

        while (true) {
            if(rentPeriodOrderMapper.updateExpiredOrder(RentPeriodOrder.Status.USED.getValue(), RentPeriodOrder.Status.EXPIRED.getValue(), new Date(), limit) < limit) {
                break;
            }
        }
        log.debug("租电套餐状态已使用->已过期更新结束...");
    }
}
