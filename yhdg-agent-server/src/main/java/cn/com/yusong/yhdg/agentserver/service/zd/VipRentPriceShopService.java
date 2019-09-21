package cn.com.yusong.yhdg.agentserver.service.zd;

import cn.com.yusong.yhdg.agentserver.persistence.hdg.ShopMapper;
import cn.com.yusong.yhdg.agentserver.persistence.zd.VipRentPriceMapper;
import cn.com.yusong.yhdg.agentserver.persistence.zd.VipRentPriceShopMapper;
import cn.com.yusong.yhdg.agentserver.service.AbstractService;
import cn.com.yusong.yhdg.common.domain.hdg.Shop;
import cn.com.yusong.yhdg.common.domain.zd.VipRentPriceShop;
import cn.com.yusong.yhdg.common.entity.json.ExtResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class VipRentPriceShopService extends AbstractService {

    @Autowired
    VipRentPriceShopMapper vipRentPriceShopMapper;
    @Autowired
    ShopMapper shopMapper;
    @Autowired
    VipRentPriceMapper vipRentPriceMapper;

    public List<VipRentPriceShop> findListByPriceId(Long priceId) {
        List<VipRentPriceShop> list = vipRentPriceShopMapper.findListByPriceId(priceId);
        for (VipRentPriceShop vipRentPriceShop : list) {
            Shop shop = shopMapper.find(vipRentPriceShop.getShopId());
            vipRentPriceShop.setShopName(shop.getShopName());
        }
        return list;
    }

    @Transactional(rollbackFor=Throwable.class)
    public ExtResult create(VipRentPriceShop entity) {
        String[] shopIdArr = entity.getIds().split(",");
        for (String shopId : shopIdArr) {
            VipRentPriceShop vipPriceShop = vipRentPriceShopMapper.findByPriceId(entity.getPriceId(), shopId);
            if (vipPriceShop != null) {
                return ExtResult.failResult("包含已存在的门店");
            }
            VipRentPriceShop cct = new VipRentPriceShop();
            cct.setPriceId(entity.getPriceId());
            cct.setShopId(shopId);
            vipRentPriceShopMapper.insert(cct);
        }
        List<VipRentPriceShop> shoptList = vipRentPriceShopMapper.findListByPriceId(entity.getPriceId());
        vipRentPriceMapper.updateShopCount(entity.getPriceId(), shoptList.size());
        return ExtResult.successResult();
    }

    @Transactional(rollbackFor=Throwable.class)
    public ExtResult delete(Long id, Long priceId) {
        vipRentPriceShopMapper.delete(id);
        List<VipRentPriceShop> shopList = vipRentPriceShopMapper.findListByPriceId(priceId);
        vipRentPriceMapper.updateShopCount(priceId, shopList.size());
        return ExtResult.successResult();
    }
}
