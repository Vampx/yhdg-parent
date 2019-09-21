
package cn.com.yusong.yhdg.appserver.service.basic;

import cn.com.yusong.yhdg.appserver.persistence.basic.CustomerOfflineBatteryMapper;
import cn.com.yusong.yhdg.appserver.persistence.basic.ZhizuCustomerMapper;
import cn.com.yusong.yhdg.appserver.persistence.basic.ZhizuCustomerNoticeMapper;
import cn.com.yusong.yhdg.appserver.service.AbstractService;
import cn.com.yusong.yhdg.common.constant.ConstEnum;
import cn.com.yusong.yhdg.common.constant.Constant;
import cn.com.yusong.yhdg.common.domain.basic.Customer;
import cn.com.yusong.yhdg.common.domain.basic.CustomerOfflineBattery;
import cn.com.yusong.yhdg.common.domain.basic.ZhizuCustomer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * Created by ruanjian5 on 2017/11/4.
 */
@Service
public class CustomerOfflineBatteryService extends AbstractService {
    @Autowired
    CustomerOfflineBatteryMapper customerOfflineBatteryMapper;


    public Integer save(CustomerOfflineBattery customerOfflineBattery){
        customerOfflineBattery.setStatus(CustomerOfflineBattery.Status.NO.getValue());
        customerOfflineBattery.setCreateTime(new Date());
        customerOfflineBatteryMapper.insert(customerOfflineBattery);
        return customerOfflineBattery.getId();
    }

}


