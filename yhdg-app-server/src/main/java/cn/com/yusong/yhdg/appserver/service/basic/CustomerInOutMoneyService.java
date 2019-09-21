package cn.com.yusong.yhdg.appserver.service.basic;

import cn.com.yusong.yhdg.appserver.persistence.basic.CustomerInOutMoneyMapper;
import cn.com.yusong.yhdg.common.domain.basic.CustomerInOutMoney;
import cn.com.yusong.yhdg.common.domain.basic.PayOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * Created by ruanjian5 on 2017/11/4.
 */
@Service
public class CustomerInOutMoneyService {

    @Autowired
    CustomerInOutMoneyMapper customerInOutMoneyMappser;

    public List<CustomerInOutMoney> findList(Long customerId, int offset, int limit){
        List<CustomerInOutMoney> list = customerInOutMoneyMappser.findList(customerId, offset, limit);
        return list;
    }

    public int insert(CustomerInOutMoney customerInOutMoney){

        return  customerInOutMoneyMappser.insert(customerInOutMoney);
    }
}
