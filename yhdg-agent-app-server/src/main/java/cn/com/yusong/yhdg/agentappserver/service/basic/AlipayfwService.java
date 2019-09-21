package cn.com.yusong.yhdg.agentappserver.service.basic;

import cn.com.yusong.yhdg.agentappserver.persistence.basic.AlipayfwMapper;
import cn.com.yusong.yhdg.common.domain.basic.Alipayfw;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AlipayfwService {
    @Autowired
    AlipayfwMapper alipayfwMapper;

    public Alipayfw find(int id) {
        return alipayfwMapper.find(id);
    }

    public List<Alipayfw> findAll() {
        return alipayfwMapper.findAll();
    }
}
