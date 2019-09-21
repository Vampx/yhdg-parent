package cn.com.yusong.yhdg.webserver.service.hdg;

import cn.com.yusong.yhdg.common.domain.hdg.Cabinet;
import cn.com.yusong.yhdg.common.domain.hdg.Shop;
import cn.com.yusong.yhdg.common.domain.hdg.VipPriceCabinet;
import cn.com.yusong.yhdg.common.domain.hdg.VipPriceShop;
import cn.com.yusong.yhdg.common.entity.json.ExtResult;
import cn.com.yusong.yhdg.webserver.persistence.hdg.ShopMapper;
import cn.com.yusong.yhdg.webserver.persistence.hdg.VipPriceMapper;
import cn.com.yusong.yhdg.webserver.persistence.hdg.VipPriceShopMapper;
import cn.com.yusong.yhdg.webserver.service.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class VipPriceShopService extends AbstractService {

    @Autowired
    VipPriceShopMapper vipPriceShopMapper;
    @Autowired
    ShopMapper shopMapper;
    @Autowired
    VipPriceMapper vipPriceMapper;

    public List<VipPriceShop> findListByPriceId(Long priceId) {
        List<VipPriceShop> list = vipPriceShopMapper.findListByPriceId(priceId);
        for (VipPriceShop vipPriceShop : list) {
            Shop shop = shopMapper.find(vipPriceShop.getShopId());
            vipPriceShop.setShopName(shop.getShopName());
        }
        return list;
    }

    public VipPriceShop findByShopId(String shopId) {
        return vipPriceShopMapper.findByShopId(shopId);
    }

    @Transactional(rollbackFor=Throwable.class)
    public ExtResult create(VipPriceShop entity) {
        String[] shopIdArr = entity.getIds().split(",");
        for (String shopId : shopIdArr) {
            VipPriceShop vipPriceShop = vipPriceShopMapper.findByPriceId(entity.getPriceId(), shopId);
            if (vipPriceShop != null) {
                return ExtResult.failResult("包含已存在的门店");
            }
            VipPriceShop cct = new VipPriceShop();
            cct.setPriceId(entity.getPriceId());
            cct.setShopId(shopId);
            vipPriceShopMapper.insert(cct);
            updateShopPrice(entity.getPriceId(), shopId);
        }
        List<VipPriceShop> shoptList = vipPriceShopMapper.findListByPriceId(entity.getPriceId());
        vipPriceMapper.updateShopCount(entity.getPriceId(), shoptList.size());
        return ExtResult.successResult();
    }

    @Transactional(rollbackFor=Throwable.class)
    public ExtResult delete(Long id, Long priceId) {
        VipPriceShop vipPriceShop = vipPriceShopMapper.find(id);
        vipPriceShopMapper.delete(id);
        List<VipPriceShop> shopList = vipPriceShopMapper.findListByPriceId(priceId);
        vipPriceMapper.updateShopCount(priceId, shopList.size());
        updateShopPriceByCabint(vipPriceShop.getShopId());
        return ExtResult.successResult();
    }
}
