package cn.com.yusong.yhdg.webserver.service.zc;

import cn.com.yusong.yhdg.common.domain.hdg.Shop;
import cn.com.yusong.yhdg.common.domain.zc.VehicleVipPriceShop;
import cn.com.yusong.yhdg.common.entity.json.ExtResult;
import cn.com.yusong.yhdg.webserver.persistence.hdg.ShopMapper;
import cn.com.yusong.yhdg.webserver.persistence.hdg.VipPriceMapper;
import cn.com.yusong.yhdg.webserver.persistence.zc.VehicleVipPriceMapper;
import cn.com.yusong.yhdg.webserver.persistence.zc.VehicleVipPriceShopMapper;
import cn.com.yusong.yhdg.webserver.service.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class VehicleVipPriceShopService extends AbstractService {

    @Autowired
    VehicleVipPriceShopMapper vehicleVipPriceShopMapper;
    @Autowired
    VehicleVipPriceMapper vehicleVipPriceMapper;
    @Autowired
    ShopMapper shopMapper;
    @Autowired
    VipPriceMapper vipPriceMapper;

    public List<VehicleVipPriceShop> findListByPriceId(Long priceId) {
        List<VehicleVipPriceShop> list = vehicleVipPriceShopMapper.findListByPriceId(priceId);
        for (VehicleVipPriceShop vehicleVipPriceShop : list) {
            Shop shop = shopMapper.find(vehicleVipPriceShop.getShopId());
            vehicleVipPriceShop.setShopName(shop.getShopName());
        }
        return list;
    }

    public VehicleVipPriceShop findByShopId(String shopId) {
        return vehicleVipPriceShopMapper.findByShopId(shopId);
    }

    @Transactional(rollbackFor=Throwable.class)
    public ExtResult create(VehicleVipPriceShop entity) {
        String[] shopIdArr = entity.getIds().split(",");
        for (String shopId : shopIdArr) {
            VehicleVipPriceShop vipPriceShop = vehicleVipPriceShopMapper.findByPriceId(entity.getPriceId(), shopId);
            if (vipPriceShop != null) {
                return ExtResult.failResult("包含已存在的门店");
            }
            VehicleVipPriceShop vehicleVipPriceShop = new VehicleVipPriceShop();
            vehicleVipPriceShop.setPriceId(entity.getPriceId());
            vehicleVipPriceShop.setShopId(shopId);
            vehicleVipPriceShopMapper.insert(vehicleVipPriceShop);
            updateShopPrice(entity.getPriceId(), shopId);
        }
        List<VehicleVipPriceShop> vipPriceShops = vehicleVipPriceShopMapper.findListByPriceId(entity.getPriceId());
        vehicleVipPriceMapper.updateShopCount(entity.getPriceId().longValue(), vipPriceShops.size());
        return ExtResult.successResult();
    }

    @Transactional(rollbackFor=Throwable.class)
    public ExtResult delete(Long id, Long priceId) {
        VehicleVipPriceShop vehicleVipPriceShop = vehicleVipPriceShopMapper.find(id);
        vehicleVipPriceShopMapper.delete(id);
        List<VehicleVipPriceShop> shopList = vehicleVipPriceShopMapper.findListByPriceId(priceId);
        vehicleVipPriceMapper.updateShopCount(priceId, shopList.size());
        updateShopPriceByCabint(vehicleVipPriceShop.getShopId());
        return ExtResult.successResult();
    }
}
