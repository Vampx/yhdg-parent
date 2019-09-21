
package cn.com.yusong.yhdg.appserver.service.basic;

import cn.com.yusong.yhdg.appserver.persistence.basic.CustomerOfflineBatteryMapper;
import cn.com.yusong.yhdg.appserver.persistence.basic.CustomerOfflineExchangeRecordMapper;
import cn.com.yusong.yhdg.appserver.service.AbstractService;
import cn.com.yusong.yhdg.common.domain.basic.CustomerOfflineBattery;
import cn.com.yusong.yhdg.common.domain.basic.CustomerOfflineExchangeRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * Created by ruanjian5 on 2017/11/4.
 */
@Service
public class CustomerOfflineExchangeRecordService extends AbstractService {
    @Autowired
    CustomerOfflineExchangeRecordMapper customerOfflineExchangeRecordMapper;


    public Integer save(CustomerOfflineExchangeRecord customerOfflineExchangeRecord){
        customerOfflineExchangeRecord.setStatus(CustomerOfflineExchangeRecord.Status.NO.getValue());
        customerOfflineExchangeRecord.setCreateTime(new Date());
        customerOfflineExchangeRecordMapper.insert(customerOfflineExchangeRecord);
        return customerOfflineExchangeRecord.getId();
    }

}


