package cn.com.yusong.yhdg.weixinserver.service.basic;

import cn.com.yusong.yhdg.common.domain.basic.Partner;
import cn.com.yusong.yhdg.weixinserver.persistence.basic.PartnerMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PartnerService {
    @Autowired
    PartnerMapper partnerMapper;

    public Partner find(int id) {
        return partnerMapper.find(id);
    }

    public List<Partner> findAll() {
        return partnerMapper.findAll();
    }
}
