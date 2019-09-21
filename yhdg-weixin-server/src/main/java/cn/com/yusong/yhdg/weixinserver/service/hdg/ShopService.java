package cn.com.yusong.yhdg.weixinserver.service.hdg;

import cn.com.yusong.yhdg.common.domain.hdg.Shop;
import cn.com.yusong.yhdg.weixinserver.persistence.hdg.ShopMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ShopService {
    @Autowired
    ShopMapper shopMapper;

    public Shop find(String id) {
        return shopMapper.find(id);
    }
}
