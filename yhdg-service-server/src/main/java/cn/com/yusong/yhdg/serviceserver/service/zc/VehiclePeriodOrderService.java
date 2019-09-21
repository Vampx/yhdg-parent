package cn.com.yusong.yhdg.serviceserver.service.zc;

import cn.com.yusong.yhdg.common.domain.zc.CustomerVehicleInfo;
import cn.com.yusong.yhdg.common.domain.zc.VehiclePeriodOrder;
import cn.com.yusong.yhdg.common.domain.zd.CustomerRentInfo;
import cn.com.yusong.yhdg.common.domain.zd.RentPeriodOrder;
import cn.com.yusong.yhdg.serviceserver.persistence.basic.PushMetaDataMapper;
import cn.com.yusong.yhdg.serviceserver.persistence.basic.SystemConfigMapper;
import cn.com.yusong.yhdg.serviceserver.persistence.zc.CustomerVehicleInfoMapper;
import cn.com.yusong.yhdg.serviceserver.persistence.zc.VehiclePeriodOrderMapper;
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
public class VehiclePeriodOrderService extends AbstractService {
    final static Logger log = LogManager.getLogger(VehiclePeriodOrderService.class);
    @Autowired
    CustomerVehicleInfoMapper customerVehicleInfoMapper;
    @Autowired
    VehiclePeriodOrderMapper vehiclePeriodOrderMapper;
    @Autowired
    SystemConfigMapper systemConfigMapper;


    public void used() {
        log.debug("租车套餐状态未使用->已使用更新开始...");
        int offset = 0,limit = 100;

        while (true) {
            //先查询未使用的套餐，如果未使用的套餐存在，并且用户有押金，并且没有使用中的套餐，将未使用更新为已使用
            List<VehiclePeriodOrder>  orderList = vehiclePeriodOrderMapper.findListByStatus(VehiclePeriodOrder.Status.NOT_USE.getValue(), offset, limit);
            if(orderList.isEmpty()){
                break;
            }
            for(VehiclePeriodOrder vehiclePeriodOrder : orderList){
                CustomerVehicleInfo customerVehicleInfo = customerVehicleInfoMapper.find(vehiclePeriodOrder.getCustomerId());
                if(customerVehicleInfo != null && customerVehicleInfo.getVehicleId() != null){
                    VehiclePeriodOrder usedOrder = vehiclePeriodOrderMapper.findUsedByCustomer(customerVehicleInfo.getId(), VehiclePeriodOrder.Status.USED.getValue());
                    if(usedOrder == null){
                        Date beginTime = DateUtils.truncate(new Date(), Calendar.DAY_OF_MONTH);
                        Date endTime = DateUtils.addSeconds(DateUtils.truncate(DateUtils.addDays(beginTime, vehiclePeriodOrder.getDayCount()), Calendar.DAY_OF_MONTH), -1);
                        vehiclePeriodOrderMapper.updateUsedOrder(vehiclePeriodOrder.getId(), VehiclePeriodOrder.Status.NOT_USE.getValue(), VehiclePeriodOrder.Status.USED.getValue(), beginTime, endTime);
                    }
                }
            }
            offset += limit;
        }
        log.debug("租车套餐状态未使用->已使用更新结束...");
    }

    public void expire() {
        log.debug("租车套餐状态已使用->已过期更新开始...");
        int limit = 1000;

        while (true) {
            if(vehiclePeriodOrderMapper.updateExpiredOrder(VehiclePeriodOrder.Status.USED.getValue(), VehiclePeriodOrder.Status.EXPIRED.getValue(), new Date(), limit) < limit) {
                break;
            }
        }
        log.debug("租车套餐状态已使用->已过期更新结束...");
    }
}
