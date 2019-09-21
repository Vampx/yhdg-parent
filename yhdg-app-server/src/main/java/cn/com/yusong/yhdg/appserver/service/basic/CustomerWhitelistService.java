package cn.com.yusong.yhdg.appserver.service.basic;

import cn.com.yusong.yhdg.appserver.persistence.basic.CustomerWhitelistMapper;
import cn.com.yusong.yhdg.common.domain.basic.AgentInfo;
import cn.com.yusong.yhdg.common.domain.basic.CustomerWhitelist;
import cn.com.yusong.yhdg.common.entity.json.ExtResult;
import cn.com.yusong.yhdg.common.entity.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class CustomerWhitelistService {
    @Autowired
    CustomerWhitelistMapper customerWhitelistMapper;


    public int findByMobile(Integer partnerId, Integer agentId, String mobile) {
        return  customerWhitelistMapper.findByMobile(partnerId, agentId, mobile);
    }
}
