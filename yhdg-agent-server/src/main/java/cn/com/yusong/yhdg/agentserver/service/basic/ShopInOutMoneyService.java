package cn.com.yusong.yhdg.agentserver.service.basic;

import cn.com.yusong.yhdg.common.domain.basic.AgentInOutMoney;
import cn.com.yusong.yhdg.common.domain.basic.ShopInOutMoney;
import cn.com.yusong.yhdg.common.domain.hdg.Shop;
import cn.com.yusong.yhdg.common.entity.pagination.Page;
import cn.com.yusong.yhdg.agentserver.persistence.basic.AgentInOutMoneyMapper;
import cn.com.yusong.yhdg.agentserver.persistence.basic.ShopInOutMoneyMapper;
import cn.com.yusong.yhdg.agentserver.persistence.hdg.ShopMapper;
import cn.com.yusong.yhdg.agentserver.service.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ShopInOutMoneyService extends AbstractService{

    @Autowired
    ShopInOutMoneyMapper shopInOutMoneyMapper;
    @Autowired
    ShopMapper shopMapper;


    public Page findPage(ShopInOutMoney search) {
        Page page = search.buildPage();
        page.setTotalItems(shopInOutMoneyMapper.findPageCount(search));
        search.setBeginIndex(page.getOffset());
        List<ShopInOutMoney> pageResult = shopInOutMoneyMapper.findPageResult(search);
        for (ShopInOutMoney shopInOutMoney : pageResult) {
            if (shopInOutMoney.getShopId() != null) {
                Shop shop = shopMapper.find(shopInOutMoney.getShopId());
                if (shop != null) {
                    shopInOutMoney.setShopName(shop.getShopName());
                }
            }
        }
        page.setResult(pageResult);
        return page;
    }

    public ShopInOutMoney find(Long id) {
        ShopInOutMoney shopInOutMoney = shopInOutMoneyMapper.find(id);
        if (shopInOutMoney.getShopId() != null) {
            Shop shop = shopMapper.find(shopInOutMoney.getShopId());
            if (shop != null && shop.getAgentId() != null) {
                shopInOutMoney.setAgentId(shop.getAgentId());
            }
        }
        return shopInOutMoney;
    }
}
