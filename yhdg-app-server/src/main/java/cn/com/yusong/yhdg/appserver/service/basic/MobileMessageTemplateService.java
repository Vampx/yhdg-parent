package cn.com.yusong.yhdg.appserver.service.basic;


import cn.com.yusong.yhdg.appserver.persistence.basic.MobileMessageTemplateMapper;
import cn.com.yusong.yhdg.common.domain.basic.MobileMessageTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MobileMessageTemplateService {

    @Autowired
    MobileMessageTemplateMapper mobileMessageTemplateMapper;

    public MobileMessageTemplate find(int partnerId, int id) {
        return mobileMessageTemplateMapper.find(partnerId, id);
    }
}
