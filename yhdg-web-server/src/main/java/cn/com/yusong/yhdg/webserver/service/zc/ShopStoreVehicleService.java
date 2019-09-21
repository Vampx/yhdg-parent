package cn.com.yusong.yhdg.webserver.service.zc;


import cn.com.yusong.yhdg.common.domain.basic.Agent;
import cn.com.yusong.yhdg.common.domain.hdg.Battery;
import cn.com.yusong.yhdg.common.domain.hdg.Shop;
import cn.com.yusong.yhdg.common.domain.hdg.ShopStoreBattery;
import cn.com.yusong.yhdg.common.domain.zc.PriceSetting;
import cn.com.yusong.yhdg.common.domain.zc.ShopStoreVehicle;
import cn.com.yusong.yhdg.common.domain.zc.ShopStoreVehicleBattery;
import cn.com.yusong.yhdg.common.domain.zc.Vehicle;
import cn.com.yusong.yhdg.common.entity.json.DataResult;
import cn.com.yusong.yhdg.common.entity.json.ExtResult;
import cn.com.yusong.yhdg.common.entity.pagination.Page;
import cn.com.yusong.yhdg.webserver.persistence.basic.AgentMapper;
import cn.com.yusong.yhdg.webserver.persistence.hdg.BatteryMapper;
import cn.com.yusong.yhdg.webserver.persistence.hdg.ShopMapper;
import cn.com.yusong.yhdg.webserver.persistence.hdg.ShopStoreBatteryMapper;
import cn.com.yusong.yhdg.webserver.persistence.zc.PriceSettingMapper;
import cn.com.yusong.yhdg.webserver.persistence.zc.ShopStoreVehicleBatteryMapper;
import cn.com.yusong.yhdg.webserver.persistence.zc.ShopStoreVehicleMapper;
import cn.com.yusong.yhdg.webserver.persistence.zc.VehicleMapper;
import cn.com.yusong.yhdg.webserver.service.AbstractService;
import jxl.Sheet;
import jxl.Workbook;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.util.Date;
import java.util.List;

@Service
public class ShopStoreVehicleService extends AbstractService{

    static Logger log = LoggerFactory.getLogger(ShopStoreVehicleService.class);

    @Autowired
    ShopStoreVehicleMapper shopStoreVehicleMapper;
    @Autowired
    BatteryMapper batteryMapper;
    @Autowired
    VehicleMapper vehicleMapper;
    @Autowired
    AgentMapper agentMapper;
    @Autowired
    ShopMapper shopMapper;
    @Autowired
    PriceSettingMapper priceSettingMapper;
    @Autowired
    ShopStoreBatteryMapper shopStoreBatteryMapper;
    @Autowired
    ShopStoreVehicleBatteryMapper shopStoreVehicleBatteryMapper;

    public Page findPage(ShopStoreVehicle search) {
        Page page = search.buildPage();
        page.setTotalItems(shopStoreVehicleMapper.findPageCount(search));
        search.setBeginIndex(page.getOffset());
        List<ShopStoreVehicle> list = shopStoreVehicleMapper.findPageResult(search);
        for (ShopStoreVehicle shopStoreVehicle : list) {
            List<ShopStoreVehicleBattery> batteryList = shopStoreVehicleBatteryMapper.findByStoreVehicle(shopStoreVehicle.getId());
            String batteryCategory = "";
            String batteryTypeName = "";
            for (ShopStoreVehicleBattery shopStoreVehicleBattery : batteryList){
                Battery battery = batteryMapper.find(shopStoreVehicleBattery.getBatteryId());
                batteryCategory += Battery.Category.getName(battery.getCategory())+" ";
                batteryTypeName += findBatteryType(battery.getType()).getTypeName()+" ";
            }
            shopStoreVehicle.setBatteryCategory(batteryCategory);
            shopStoreVehicle.setBatteryTypeName(batteryTypeName);
        }
        page.setResult(list);
        return page;
    }
    public ShopStoreVehicle find(long id){
        ShopStoreVehicle shopStoreVehicle = shopStoreVehicleMapper.find(id);
        List<ShopStoreVehicleBattery> list = shopStoreVehicleBatteryMapper.findByStoreVehicle(id);
        return shopStoreVehicle;
    }

    @Transactional(rollbackFor = Throwable.class)
    public ExtResult insert(ShopStoreVehicle search) {
        Vehicle vehicle = vehicleMapper.findByVinNo(search.getVinNo());
        if (null == vehicle) {
            return ExtResult.failResult("车架号不存在");
        }
        if (shopStoreVehicleMapper.findByVehicle(vehicle.getId()) != null) {
            return ExtResult.failResult("车辆已存在");
        }

        Shop shop = shopMapper.find(search.getShopId());
        if (shop == null) {
            return ExtResult.failResult("门店不存在");
        }
        Agent agent = agentMapper.find(search.getAgentId());
        if (agent == null) {
            return ExtResult.failResult("运营商不存在");
        }
        vehicleMapper.setShopId(vehicle.getId(),search.getShopId(), shop.getShopName(),Vehicle.Status.IN_SHOP.getValue());
        search.setVehicleId(vehicle.getId());
        search.setAgentName(agent.getAgentName());
        search.setAgentCode(agent.getAgentCode());
        search.setShopName(shop.getShopName());
        search.setCreateTime(new Date());
        shopStoreVehicleMapper.insert(search);
        for (String id : search.getIds()) {
            if (shopStoreVehicleBatteryMapper.findByBatteryId(id) != null){
                return ExtResult.failResult("电池已存在");
            }
            Battery battery = batteryMapper.find(id);
            if (battery == null) {
                return ExtResult.failResult("电池不存在");
            }
            ShopStoreBattery shopStoreBattery = new ShopStoreBattery();
            shopStoreBattery.setCategory(battery.getCategory());
            shopStoreBattery.setAgentId(battery.getAgentId());
            shopStoreBattery.setAgentName(agent.getAgentName());
            shopStoreBattery.setShopId(search.getShopId());
            shopStoreBattery.setShopName(shop.getShopName());
            shopStoreBattery.setBatteryId(id);
            shopStoreBattery.setCreateTime(new Date());
            shopStoreBatteryMapper.insert(shopStoreBattery);
            batteryMapper.updateShopId(id,search.getShopId(),shop.getShopName());
            batteryMapper.updateStatus(id, Battery.Status.NOT_USE.getValue());
            ShopStoreVehicleBattery shopStoreVehicleBattery = new ShopStoreVehicleBattery();
            shopStoreVehicleBattery.setStoreVehicleId(search.getId());
            shopStoreVehicleBattery.setBatteryId(id);
            shopStoreVehicleBatteryMapper.insert(shopStoreVehicleBattery);
        }
        return ExtResult.successResult();
    }

    @Transactional(rollbackFor = Throwable.class)
    public ExtResult update(ShopStoreVehicle search) {
        Vehicle vehicle = vehicleMapper.findByVinNo(search.getVinNo());
        if (null == vehicle) {
            return ExtResult.failResult("车架号不存在");
        }
        ShopStoreVehicle resout = shopStoreVehicleMapper.findByVinNo(search.getVinNo());
        ShopStoreVehicle shopStoreVehicle = shopStoreVehicleMapper.find(search.getId());
        if (!search.getVinNo().equals(shopStoreVehicle.getVinNo()) && resout != null) {
            return ExtResult.failResult("车辆已存在");
        }
        List<ShopStoreVehicleBattery> list = shopStoreVehicleBatteryMapper.findByStoreVehicle(search.getId());
        shopStoreVehicleBatteryMapper.deleteByStoreVehicle(search.getId());
        for (String id : search.getIds()) {
            if (shopStoreVehicleBatteryMapper.findByBatteryId(id) != null){
                return ExtResult.failResult("电池已存在");
            }
            Shop shop = shopMapper.find(search.getShopId());
            if (shop == null) {
                return ExtResult.failResult("门店不存在");
            }
            Battery battery = batteryMapper.find(id);
            if (battery == null) {
                return ExtResult.failResult("电池不存在");
            }
            Agent agent = agentMapper.find(battery.getAgentId());
            if (agent == null) {
                return ExtResult.failResult("运营商不存在");
            }
            for (ShopStoreVehicleBattery storeBattery : list) {
                shopStoreBatteryMapper.deleteByShopBatteryId(search.getShopId(), id);
                batteryMapper.updateShopId(id,null,null);
                batteryMapper.updateStatus(id, Battery.Status.NOT_USE.getValue());
                shopStoreVehicleBatteryMapper.delete(search.getId(),id);
            }
            ShopStoreBattery shopStoreBattery = new ShopStoreBattery();
            shopStoreBattery.setCategory(battery.getCategory());
            shopStoreBattery.setAgentId(battery.getAgentId());
            shopStoreBattery.setAgentName(agent.getAgentName());
            shopStoreBattery.setShopId(search.getShopId());
            shopStoreBattery.setShopName(shop.getShopName());
            shopStoreBattery.setBatteryId(id);
            shopStoreBattery.setCreateTime(new Date());
            shopStoreBatteryMapper.insert(shopStoreBattery);
            batteryMapper.updateShopId(id,search.getShopId(),shop.getShopName());
            batteryMapper.updateStatus(id, Battery.Status.NOT_USE.getValue());
            ShopStoreVehicleBattery shopStoreVehicleBattery = new ShopStoreVehicleBattery();
            shopStoreVehicleBattery.setStoreVehicleId(search.getId());
            shopStoreVehicleBattery.setBatteryId(id);
            shopStoreVehicleBatteryMapper.insert(shopStoreVehicleBattery);
        }
        Agent agent = agentMapper.find(search.getAgentId());
        Shop shop = shopMapper.find(search.getShopId());
        Vehicle dbVehicle = vehicleMapper.findByVinNo(shopStoreVehicleMapper.find(search.getId()).getVinNo());
        vehicleMapper.clearShop(dbVehicle.getId());
        vehicleMapper.setShopId(vehicle.getId(),search.getShopId(), shop.getShopName(),Vehicle.Status.IN_SHOP.getValue());
        search.setVehicleId(vehicle.getId());
        search.setAgentName(agent.getAgentName());
        search.setAgentCode(agent.getAgentCode());
        search.setShopName(shop.getShopName());
        shopStoreVehicleMapper.update(search);
        return ExtResult.successResult();
    }

    @Transactional(rollbackFor = Throwable.class)
    public int delete(long id) {
        ShopStoreVehicle shopStoreVehicle = shopStoreVehicleMapper.find(id);
        Vehicle vehicle = vehicleMapper.findByVinNo(shopStoreVehicle.getVinNo());
        List<ShopStoreVehicleBattery> list = shopStoreVehicleBatteryMapper.findByStoreVehicle(id);
        for (ShopStoreVehicleBattery battery : list) {
            shopStoreBatteryMapper.deleteByShopBatteryId(shopStoreVehicle.getShopId(), battery.getBatteryId());
            batteryMapper.updateShopId(battery.getBatteryId(),null,null);
            batteryMapper.updateStatus(battery.getBatteryId(), Battery.Status.NOT_USE.getValue());
        }
        vehicleMapper.setShopId(vehicle.getId(),null, null, Vehicle.Status.NOT_USE.getValue());
        shopStoreVehicleBatteryMapper.deleteByStoreVehicle(id);
        return shopStoreVehicleMapper.delete(id);
    }

    @Transactional(rollbackFor = Throwable.class)
    public ExtResult btchImportBattery(File mFile, String shopId , long priceSettingId) {
        Workbook workbook = null;
        try {
            workbook = Workbook.getWorkbook(mFile);
        } catch (Exception e) {
            return ExtResult.failResult("操作失败");
        }
        Sheet sheet = workbook.getSheet(0);
        //获取总条数
        int rows = sheet.getRows() - 1;
        //成功条数
        int successCount = 0;
        int failCount = 0;
        StringBuilder failBuilder = new StringBuilder("");
        for (int row = 2; row <= rows; row++) {
            //获取车架号
            String vinNo = sheet.getCell(0, row).getContents().trim();
            if (!StringUtils.isNotEmpty(vinNo)) {
                continue;
            }
            //获取电池
            String batteryId1 = sheet.getCell(1, row).getContents().trim();
            String batteryId2 = sheet.getCell(2, row).getContents().trim();
            Vehicle vehicle = vehicleMapper.findByVinNo(vinNo);
            Shop shop = shopMapper.find(shopId);
            Battery battery1 = batteryMapper.find(batteryId1);
            Battery battery2 = batteryMapper.find(batteryId2);
            if (vehicle != null) {
                ShopStoreVehicle storeVehicle = shopStoreVehicleMapper.findByVehicle(vehicle.getId());
                PriceSetting priceSetting = priceSettingMapper.find(priceSettingId);
                ShopStoreBattery storeBattery1 = shopStoreBatteryMapper.findByBattery(batteryId1);
                ShopStoreBattery storeBattery2 = null;
                if (battery2 != null) {
                    storeBattery2 = shopStoreBatteryMapper.findByBattery(batteryId2);
                }
                try {
                    int total = 0;
                    if (battery1 != null && null == storeVehicle && battery1.getAgentId() == shop.getAgentId()
                            && battery1.getType() == priceSetting.getBatteryType() && vehicle.getAgentId() == shop.getAgentId()
                            && storeBattery1 == null) {
                        if (priceSetting.getBatteryCount() == 1) {
                            //添加门店库存电池
                            ShopStoreBattery shopStoreBattery = new ShopStoreBattery();
                            shopStoreBattery.setCategory(battery1.getCategory());
                            shopStoreBattery.setAgentId(battery1.getAgentId());
                            shopStoreBattery.setAgentName(priceSetting.getAgentName());
                            shopStoreBattery.setShopId(shopId);
                            shopStoreBattery.setShopName(shop.getShopName());
                            shopStoreBattery.setBatteryId(batteryId1);
                            shopStoreBattery.setCreateTime(new Date());
                            shopStoreBatteryMapper.insert(shopStoreBattery);
                            //添加库存套餐
                            ShopStoreVehicle shopStoreVehicle = new ShopStoreVehicle();
                            shopStoreVehicle.setCategory(priceSetting.getCategory());
                            shopStoreVehicle.setAgentId(priceSetting.getAgentId());
                            shopStoreVehicle.setAgentName(priceSetting.getAgentName());
                            shopStoreVehicle.setShopId(shopId);
                            shopStoreVehicle.setShopName(shop.getShopName());
                            shopStoreVehicle.setVehicleId(vehicle.getId());
                            shopStoreVehicle.setPriceSettingId(priceSettingId);
                            shopStoreVehicle.setVinNo(vinNo);
                            shopStoreVehicle.setBatteryCount(priceSetting.getBatteryCount());
                            shopStoreVehicle.setCreateTime(new Date());
                            total = shopStoreVehicleMapper.insert(shopStoreVehicle);
                            //添加库存套餐关联电池
                            ShopStoreVehicleBattery shopStoreVehicleBattery1 = new ShopStoreVehicleBattery();
                            shopStoreVehicleBattery1.setStoreVehicleId(shopStoreVehicle.getId());
                            shopStoreVehicleBattery1.setBatteryId(batteryId1);
                            shopStoreVehicleBatteryMapper.insert(shopStoreVehicleBattery1);
                            //修改电池状态
                            batteryMapper.updateShopId(batteryId1, shopId, shop.getShopName());
                            batteryMapper.updateStatus(batteryId1, Battery.Status.NOT_USE.getValue());
                            //修改车辆状态
                            vehicleMapper.setShopId(vehicle.getId(), shopId, shop.getShopName(), Vehicle.Status.IN_SHOP.getValue());
                        } else if (battery2 != null && battery2.getType() == priceSetting.getBatteryType()
                                && priceSetting.getBatteryCount() == 2 && battery1.getAgentId() == shop.getAgentId()
                                && vehicle.getAgentId() == shop.getAgentId() && battery2.getAgentId() == shop.getAgentId()
                                && storeBattery1 == null && storeBattery2 == null) {
                            //添加门店库存电池
                            ShopStoreBattery shopStoreBattery = new ShopStoreBattery();
                            shopStoreBattery.setCategory(battery1.getCategory());
                            shopStoreBattery.setAgentId(battery1.getAgentId());
                            shopStoreBattery.setAgentName(priceSetting.getAgentName());
                            shopStoreBattery.setShopId(shopId);
                            shopStoreBattery.setShopName(shop.getShopName());
                            shopStoreBattery.setBatteryId(batteryId1);
                            shopStoreBattery.setCreateTime(new Date());
                            shopStoreBatteryMapper.insert(shopStoreBattery);
                            //添加库存套餐
                            ShopStoreVehicle shopStoreVehicle = new ShopStoreVehicle();
                            shopStoreVehicle.setCategory(priceSetting.getCategory());
                            shopStoreVehicle.setAgentId(priceSetting.getAgentId());
                            shopStoreVehicle.setAgentName(priceSetting.getAgentName());
                            shopStoreVehicle.setShopId(shopId);
                            shopStoreVehicle.setShopName(shop.getShopName());
                            shopStoreVehicle.setVehicleId(vehicle.getId());
                            shopStoreVehicle.setPriceSettingId(priceSettingId);
                            shopStoreVehicle.setVinNo(vinNo);
                            shopStoreVehicle.setBatteryCount(priceSetting.getBatteryCount());
                            shopStoreVehicle.setCreateTime(new Date());
                            total = shopStoreVehicleMapper.insert(shopStoreVehicle);
                            //添加库存套餐关联电池
                            ShopStoreVehicleBattery shopStoreVehicleBattery1 = new ShopStoreVehicleBattery();
                            shopStoreVehicleBattery1.setStoreVehicleId(shopStoreVehicle.getId());
                            shopStoreVehicleBattery1.setBatteryId(batteryId1);
                            shopStoreVehicleBatteryMapper.insert(shopStoreVehicleBattery1);
                            ShopStoreVehicleBattery shopStoreVehicleBattery2 = new ShopStoreVehicleBattery();
                            shopStoreVehicleBattery2.setStoreVehicleId(shopStoreVehicle.getId());
                            shopStoreVehicleBattery2.setBatteryId(batteryId2);
                            shopStoreVehicleBatteryMapper.insert(shopStoreVehicleBattery2);
                            //修改电池状态
                            batteryMapper.updateShopId(batteryId1, shopId, shop.getShopName());
                            batteryMapper.updateStatus(batteryId1, Battery.Status.NOT_USE.getValue());
                            batteryMapper.updateShopId(batteryId2, shopId, shop.getShopName());
                            batteryMapper.updateStatus(batteryId2, Battery.Status.NOT_USE.getValue());
                            //修改车辆状态
                            vehicleMapper.setShopId(vehicle.getId(), shopId, shop.getShopName(), Vehicle.Status.IN_SHOP.getValue());
                        }
                    } else if (null == storeVehicle && vehicle.getAgentId() == shop.getAgentId() && priceSetting.getBatteryCount() == 0) {
                        ShopStoreVehicle shopStoreVehicle = new ShopStoreVehicle();
                        shopStoreVehicle.setCategory(priceSetting.getCategory());
                        shopStoreVehicle.setAgentId(priceSetting.getAgentId());
                        shopStoreVehicle.setAgentName(priceSetting.getAgentName());
                        shopStoreVehicle.setShopId(shopId);
                        shopStoreVehicle.setShopName(shop.getShopName());
                        shopStoreVehicle.setVehicleId(vehicle.getId());
                        shopStoreVehicle.setPriceSettingId(priceSettingId);
                        shopStoreVehicle.setVinNo(vinNo);
                        shopStoreVehicle.setBatteryCount(priceSetting.getBatteryCount());
                        shopStoreVehicle.setCreateTime(new Date());
                        total = shopStoreVehicleMapper.insert(shopStoreVehicle);
                        //修改车辆状态
                        vehicleMapper.setShopId(vehicle.getId(), shopId, shop.getShopName(), Vehicle.Status.IN_SHOP.getValue());
                    } else {
                        log.debug("第{}条数据导入失败", row);
                        failBuilder.append(row + ",");
                        failCount++;
                        continue;
                    }

                    if (total > 0) {
                        successCount++;
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    log.error("第{}条数据导入失败{}", row);
                    log.error("数据导入失败", e);
                    failBuilder.append(row + ",");
                    failCount++;
                    continue;
                }
            }
        }
        if (StringUtils.isNotEmpty(failBuilder.toString())) {
            failBuilder.append("失败" + failCount + "条包括行数(" + failBuilder.toString() + "),表头不计行数!");
        }
        return DataResult.successResult("总条数" + (rows-1) + "条," + "成功导入" + successCount + "条!" + failBuilder.toString());
    }
}
