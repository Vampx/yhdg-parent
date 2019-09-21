package cn.com.yusong.yhdg.appserver.service.hdg;

import cn.com.yusong.yhdg.appserver.persistence.hdg.ExchangeInstallmentCustomerMapper;
import cn.com.yusong.yhdg.appserver.service.AbstractService;
import cn.com.yusong.yhdg.common.domain.hdg.ExchangeInstallmentCustomer;
import cn.com.yusong.yhdg.common.entity.json.ExtResult;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ExchangeInstallmentCustomerService extends AbstractService {
        @Autowired
        ExchangeInstallmentCustomerMapper exchangeInstallmentCustomerMapper;

    public ExchangeInstallmentCustomer findCustomerMobile(String customerMobile){
        return exchangeInstallmentCustomerMapper.findCustomerMobile(customerMobile);
    }

    public List<ExchangeInstallmentCustomer> findSettingId (long settingId){
        return exchangeInstallmentCustomerMapper.findSettingId(settingId);
    }


}
