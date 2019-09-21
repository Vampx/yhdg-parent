package cn.com.yusong.yhdg.appserver.service.basic;

import cn.com.yusong.yhdg.appserver.persistence.basic.PhoneappMapper;
import cn.com.yusong.yhdg.common.domain.basic.Phoneapp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PhoneappService {
    @Autowired
    PhoneappMapper phoneappMapper;

    public Phoneapp find(int id) {
        return phoneappMapper.find(id);
    }
}
