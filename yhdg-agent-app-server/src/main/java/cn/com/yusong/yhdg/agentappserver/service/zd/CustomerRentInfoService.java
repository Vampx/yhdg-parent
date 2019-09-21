package cn.com.yusong.yhdg.agentappserver.service.zd;

import cn.com.yusong.yhdg.agentappserver.persistence.zd.CustomerRentInfoMapper;
import cn.com.yusong.yhdg.common.domain.zd.CustomerRentInfo;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CustomerRentInfoService {
    @Autowired
    CustomerRentInfoMapper customerRentInfoMapper;

    public CustomerRentInfo find(long id) {
        return customerRentInfoMapper.find(id);
    }
}
