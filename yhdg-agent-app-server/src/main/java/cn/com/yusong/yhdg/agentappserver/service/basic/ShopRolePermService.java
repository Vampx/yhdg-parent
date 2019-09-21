package cn.com.yusong.yhdg.agentappserver.service.basic;

import cn.com.yusong.yhdg.agentappserver.persistence.basic.ShopRolePermMapper;
import cn.com.yusong.yhdg.common.service.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class ShopRolePermService extends AbstractService {

    @Autowired
    ShopRolePermMapper shopRolePermMapper;

    public List<String> findShopRoleAll(Integer shopRoleId) {
        return shopRolePermMapper.findShopRoleAll(shopRoleId);
    }
}
