
package cn.com.yusong.yhdg.appserver.service.basic;

import cn.com.yusong.yhdg.appserver.persistence.basic.*;
import cn.com.yusong.yhdg.appserver.service.AbstractService;
import cn.com.yusong.yhdg.common.constant.ConstEnum;
import cn.com.yusong.yhdg.common.constant.Constant;
import cn.com.yusong.yhdg.common.domain.basic.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created by ruanjian5 on 2017/11/4.
 */
@Service
public class ZhizuCustomerService extends AbstractService {
    @Autowired
    ZhizuCustomerMapper zhizuCustomerMapper;
    @Autowired
    ZhizuCustomerNoticeMapper zhizuCustomerNoticeMapper;

    public ZhizuCustomer find(long id) {
        return zhizuCustomerMapper.find(id);
    }

    public List<ZhizuCustomer> findAllList(Date updateTime) {
        return zhizuCustomerMapper.findAllList(updateTime);
    }

    public int findAllCount() {
        return zhizuCustomerMapper.findAllCount();
    }

    public List<ZhizuCustomer> findIncrementList(Date updateTime) {
        return zhizuCustomerMapper.findIncrementList(updateTime);
    }

    public int findIncrementCount(Date updateTime) {
        return zhizuCustomerMapper.findIncrementCount(updateTime);
    }

    public void save(Customer customer, Integer batteryType, String realMobile, String realIdCard, Long rentTime, Integer cityId){
        ZhizuCustomer zhizuCustomer = zhizuCustomerMapper.find(customer.getId());
        if(zhizuCustomer != null){
            zhizuCustomer.setMobile(customer.getMobile());
            zhizuCustomer.setIdCard(customer.getIdCard());
            zhizuCustomer.setRealMobile(realMobile);
            zhizuCustomer.setRealIdCard(realIdCard);
            if(rentTime == null || rentTime == 0){
                zhizuCustomer.setRentTime(null);
            }else{
                zhizuCustomer.setRentTime(new Date(rentTime));
            }
            zhizuCustomer.setBatteryType(batteryType);
            zhizuCustomer.setCityId(cityId);
            zhizuCustomer.setUpdateTime(new Date());
            zhizuCustomerMapper.update(zhizuCustomer);
        }else{
            zhizuCustomer = new ZhizuCustomer();
            zhizuCustomer.setId(customer.getId());
            zhizuCustomer.setMobile(customer.getMobile());
            zhizuCustomer.setIdCard(customer.getIdCard());
            zhizuCustomer.setRealMobile(realMobile);
            zhizuCustomer.setRealIdCard(realIdCard);
            if(rentTime == null || rentTime == 0){
                zhizuCustomer.setRentTime(null);
            }else{
                zhizuCustomer.setRentTime(new Date(rentTime));
            }
            zhizuCustomer.setBatteryType(batteryType);
            zhizuCustomer.setCityId(cityId);
            zhizuCustomer.setIsActive(ConstEnum.Flag.TRUE.getValue());
            zhizuCustomer.setUpdateTime(new Date());
            zhizuCustomer.setCreateTime(new Date());
            zhizuCustomerMapper.insert(zhizuCustomer);
        }

        //通知智租下面所有设备进行更新
        zhizuCustomerNoticeMapper.insertAll(Constant.ZHIZU_AGENT_ID);
    }

}


