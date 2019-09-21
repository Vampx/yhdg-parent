package cn.com.yusong.yhdg.weixinserver.service.basic;

import cn.com.yusong.yhdg.common.domain.basic.Laxin;
import cn.com.yusong.yhdg.common.domain.basic.LaxinSetting;
import cn.com.yusong.yhdg.weixinserver.persistence.basic.LaxinSettingMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LaxinSettingService {
    @Autowired
    LaxinSettingMapper laxinSettingMapper;

    public LaxinSetting find(long id) {
        return laxinSettingMapper.find(id);
    }
}
