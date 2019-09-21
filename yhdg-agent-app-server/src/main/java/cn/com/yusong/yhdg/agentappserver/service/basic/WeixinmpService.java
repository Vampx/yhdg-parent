package cn.com.yusong.yhdg.agentappserver.service.basic;

import cn.com.yusong.yhdg.agentappserver.persistence.basic.WeixinmpMapper;
import cn.com.yusong.yhdg.common.domain.basic.Weixinmp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WeixinmpService {
    @Autowired
    WeixinmpMapper weixinmpMapper;

    public Weixinmp find(int id) {
        return weixinmpMapper.find(id);
    }

    public List<Weixinmp> findAll() {
        return weixinmpMapper.findAll();
    }
}
