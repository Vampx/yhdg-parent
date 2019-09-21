package cn.com.yusong.yhdg.appserver.service.basic;

import cn.com.yusong.yhdg.appserver.persistence.basic.WeixinmaMapper;
import cn.com.yusong.yhdg.appserver.persistence.basic.WeixinmpMapper;
import cn.com.yusong.yhdg.common.domain.basic.Weixinma;
import cn.com.yusong.yhdg.common.domain.basic.Weixinmp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WeixinmaService {
    @Autowired
    WeixinmaMapper weixinmaMapper;

    public Weixinma find(int id) {
        return weixinmaMapper.find(id);
    }

    public List<Weixinma> findAll() {
        return weixinmaMapper.findAll();
    }
}
