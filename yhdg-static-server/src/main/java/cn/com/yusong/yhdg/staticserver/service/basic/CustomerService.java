
package cn.com.yusong.yhdg.staticserver.service.basic;

import cn.com.yusong.yhdg.common.domain.basic.Customer;
import cn.com.yusong.yhdg.staticserver.persistence.basic.CustomerMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * Created by ruanjian5 on 2017/11/4.
 */
@Service
public class CustomerService {
    @Autowired
    CustomerMapper customerMapper;

    public Customer find(long id) {
        return customerMapper.find(id);
    }
}


