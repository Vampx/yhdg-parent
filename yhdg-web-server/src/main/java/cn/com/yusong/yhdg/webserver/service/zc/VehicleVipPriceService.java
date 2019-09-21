package cn.com.yusong.yhdg.webserver.service.zc;

import cn.com.yusong.yhdg.common.domain.basic.Agent;
import cn.com.yusong.yhdg.common.domain.basic.AgentInfo;
import cn.com.yusong.yhdg.common.domain.zc.*;
import cn.com.yusong.yhdg.common.entity.json.DataResult;
import cn.com.yusong.yhdg.common.entity.json.ExtResult;
import cn.com.yusong.yhdg.common.entity.pagination.Page;
import cn.com.yusong.yhdg.webserver.persistence.basic.AgentMapper;
import cn.com.yusong.yhdg.webserver.persistence.zc.*;
import cn.com.yusong.yhdg.webserver.service.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class VehicleVipPriceService extends AbstractService {

    @Autowired
    VehicleVipPriceMapper vehicleVipPriceMapper;
    @Autowired
    RentPriceMapper rentPriceMapper;
    @Autowired
    AgentMapper agentMapper;
    @Autowired
    VehicleModelMapper vehicleModelMapper;
    @Autowired
    PriceSettingMapper priceSettingMapper;
    @Autowired
    GroupOrderMapper groupOrderMapper;

    public VehicleVipPrice find(Integer id) {
        return vehicleVipPriceMapper.find(id);
    }

    public Page findPage(VehicleVipPrice search) {
        Page page = search.buildPage();
        page.setTotalItems(vehicleVipPriceMapper.findPageCount(search));
        search.setBeginIndex(page.getOffset());
        List<VehicleVipPrice> list = vehicleVipPriceMapper.findPageResult(search);
        for (VehicleVipPrice vehicleVipPrice : list) {
            if (vehicleVipPrice.getAgentId() != null) {
                AgentInfo agentInfo = findAgentInfo(vehicleVipPrice.getAgentId());
                if (agentInfo != null) {
                    vehicleVipPrice.setAgentName(agentInfo.getAgentName());
                }
            }
            PriceSetting priceSetting = priceSettingMapper.find(vehicleVipPrice.getPriceSettingId());
            if (priceSetting != null) {
                vehicleVipPrice.setVehicleName(priceSetting.getVehicleName());
                vehicleVipPrice.setCategoryName(PriceSetting.Category.getName(priceSetting.getCategory()));
            }
            VehicleModel vehicleModel = vehicleModelMapper.find(vehicleVipPrice.getModelId());
            if (vehicleModel != null) {
                vehicleVipPrice.setModelName(vehicleModel.getModelName());
            }
        }
        page.setResult(list);
        return page;
    }

    @Transactional(rollbackFor=Throwable.class)
    public ExtResult create(VehicleVipPrice entity) {
        if (vehicleVipPriceMapper.findByRentPriceId(entity.getRentPriceId()) != null) {
            return ExtResult.failResult("该租车套餐已设置为VIP套餐！");
        }
        if (entity.getVehicleForegiftPrice() + entity.getBatteryForegiftPrice() != entity.getForegiftPrice()) {
            return ExtResult.failResult("车辆或电池押金金额之和应等于套餐押金金额");
        }
        if (entity.getVehicleRentPrice() + entity.getBatteryRentPrice() != entity.getRentPrice()) {
            return ExtResult.failResult("车辆或电池租金金额之和应等于套餐租金金额");
        }
        if (entity.getCategory() == PriceSetting.Category.NOT_RENT.getValue()) {
            entity.setBatteryForegiftPrice(null);
            entity.setBatteryRentPrice(null);
        }
        entity.setCreateTime(new Date());
        vehicleVipPriceMapper.insert(entity);
        return ExtResult.successResult();
    }

    @Transactional(rollbackFor=Throwable.class)
    public ExtResult update(VehicleVipPrice entity) {
        if (entity.getVehicleForegiftPrice() + entity.getBatteryForegiftPrice() != entity.getForegiftPrice()) {
            return ExtResult.failResult("车辆或电池押金金额之和应等于套餐押金金额");
        }
        if (entity.getVehicleRentPrice() + entity.getBatteryRentPrice() != entity.getRentPrice()) {
            return ExtResult.failResult("车辆或电池租金金额之和应等于套餐租金金额");
        }
        vehicleVipPriceMapper.update(entity);
        return ExtResult.successResult();
    }

    @Transactional(rollbackFor = Throwable.class)
    public ExtResult delete(Integer id) {
        if (groupOrderMapper.findCountByPriceId(id.longValue()) > 0) {
            return ExtResult.failResult("存在该套餐对应的组合订单，无法删除！");
        }
        if (vehicleVipPriceMapper.delete(id) >= 1) {
            return ExtResult.successResult();
        } else {
            return ExtResult.failResult("删除失败");
        }
    }

}
