package cn.com.yusong.yhdg.webserver.service.hdg;


import cn.com.yusong.yhdg.common.domain.hdg.ShopUser;
import cn.com.yusong.yhdg.common.entity.json.ExtResult;
import cn.com.yusong.yhdg.common.entity.pagination.Page;
import cn.com.yusong.yhdg.webserver.persistence.hdg.ShopUserMapper;
import cn.com.yusong.yhdg.webserver.service.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class ShopUserService extends AbstractService {
    @Autowired
    ShopUserMapper shopUserMapper;

    public ShopUser find(String shopId, Integer userId){
       return shopUserMapper.find(shopId, userId);
    }


    public Page findPage(ShopUser search) {
        Page page = search.buildPage();
        page.setTotalItems(shopUserMapper.findPageCount(search));
        search.setBeginIndex(page.getOffset());
        page.setResult(shopUserMapper.findPageResult(search));
        return page;
    }

    public ExtResult create(ShopUser entity) {
        entity.setCreateTime(new Date());
        shopUserMapper.insert(entity);
        return ExtResult.successResult();
    }
    public ExtResult update(Integer fromUserId, Integer toUserId, String shopId) {
        Date createTime = new Date();
        shopUserMapper.update(fromUserId, toUserId, shopId, createTime);
        return ExtResult.successResult();
    }

    public int delete(String shopId, Integer userId) {
       return shopUserMapper.delete(shopId, userId);
    }
}
