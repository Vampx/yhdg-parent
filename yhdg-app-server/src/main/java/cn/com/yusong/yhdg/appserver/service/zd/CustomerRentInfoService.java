package cn.com.yusong.yhdg.appserver.service.zd;

import cn.com.yusong.yhdg.appserver.persistence.zd.CustomerRentInfoMapper;
import cn.com.yusong.yhdg.common.domain.zd.CustomerRentInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class CustomerRentInfoService {
    @Autowired
    CustomerRentInfoMapper customerRentInfoMapper;

    public CustomerRentInfo find(long id) {
        return customerRentInfoMapper.find(id);
    }
}
