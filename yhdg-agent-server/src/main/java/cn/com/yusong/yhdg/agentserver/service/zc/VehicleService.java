package cn.com.yusong.yhdg.agentserver.service.zc;

import cn.com.yusong.yhdg.agentserver.persistence.basic.AgentMapper;
import cn.com.yusong.yhdg.agentserver.persistence.zc.CustomerVehicleInfoMapper;
import cn.com.yusong.yhdg.agentserver.persistence.zc.VehicleMapper;
import cn.com.yusong.yhdg.agentserver.persistence.zc.VehicleModelMapper;
import cn.com.yusong.yhdg.agentserver.service.AbstractService;
import cn.com.yusong.yhdg.common.constant.ConstEnum;
import cn.com.yusong.yhdg.common.domain.basic.AgentInfo;
import cn.com.yusong.yhdg.common.domain.zc.CustomerVehicleInfo;
import cn.com.yusong.yhdg.common.domain.zc.Vehicle;
import cn.com.yusong.yhdg.common.domain.zc.VehicleModel;
import cn.com.yusong.yhdg.common.entity.json.DataResult;
import cn.com.yusong.yhdg.common.entity.json.ExtResult;
import cn.com.yusong.yhdg.common.entity.pagination.Page;
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
public class VehicleService extends AbstractService {
    static Logger log = LoggerFactory.getLogger(VehicleService.class);
    @Autowired
    VehicleMapper vehicleMapper;
    @Autowired
    AgentMapper agentMapper;
    @Autowired
    VehicleModelMapper vehicleModelMapper;
    @Autowired
    CustomerVehicleInfoMapper customerVehicleInfoMapper;

    public Vehicle find(Integer id) {
        return vehicleMapper.find(id);
    }

    public Page findPage(Vehicle search) {
        Page page = search.buildPage();
        page.setTotalItems(vehicleMapper.findPageCount(search));
        search.setBeginIndex(page.getOffset());
        List<Vehicle> list = vehicleMapper.findPageResult(search);
        for (Vehicle vehicle : list) {
            VehicleModel model = vehicleModelMapper.find(vehicle.getModelId());
            if (model != null) {
                vehicle.setModelName(model.getModelName());
            }
        }
        Vehicle vehicle = null;
        if (list.size() >= 1) {
            vehicle = list.get(0);
        }
        if (vehicle != null) {
            vehicle.setFirstDataFlag(ConstEnum.Flag.TRUE.getValue());
            vehicle.setLeisure(vehicleMapper.findLeisure(search));
            vehicle.setVehicleCommon(vehicleMapper.findPageCount(search));
            vehicle.setInShop(vehicleMapper.findInShop(search));
            vehicle.setInUse(vehicleMapper.findInUse(search));
        }
        page.setResult(list);
        return page;
    }

    public Page findByShopPage(Vehicle search) {
        Page page = search.buildPage();
        page.setTotalItems(vehicleMapper.findByShopPageCount(search));
        search.setBeginIndex(page.getOffset());
        List<Vehicle> list = vehicleMapper.findByShopPageResult(search);
        for (Vehicle vehicle : list) {
            VehicleModel model = vehicleModelMapper.find(vehicle.getModelId());
            if (model != null) {
                vehicle.setModelName(model.getModelName());
            }
        }
        Vehicle vehicle = null;
        if (list.size() >= 1) {
            vehicle = list.get(0);
        }
        if (vehicle != null) {
            vehicle.setFirstDataFlag(ConstEnum.Flag.TRUE.getValue());
            vehicle.setLeisure(vehicleMapper.findByShopLeisure(search));
            vehicle.setVehicleCommon(vehicleMapper.findByShopPageCount(search));
            vehicle.setInShop(vehicleMapper.findByShopInShop(search));
            vehicle.setInUse(vehicleMapper.findByShopInUse(search));
        }
        page.setResult(list);
        return page;
    }

    @Transactional(rollbackFor=Throwable.class)
    public ExtResult create(Vehicle vehicle) {
        if (vehicleMapper.findByVinNo(vehicle.getVinNo()) != null) {
            return ExtResult.failResult("车架号重复");
        }
        if (vehicle.getUpLineStatus() == ConstEnum.Flag.TRUE.getValue()) {
            vehicle.setUpLineTime(new Date());
        }
        vehicle.setLockStatus(ConstEnum.Flag.FALSE.getValue());
        vehicle.setLockSwitch(ConstEnum.Flag.FALSE.getValue());
        vehicle.setAgentName(agentMapper.find(vehicle.getAgentId()).getAgentName());
        vehicle.setIsOnline(ConstEnum.Flag.FALSE.getValue());
        vehicle.setStatus(Vehicle.Status.NOT_USE.getValue());
        vehicle.setActiveStatus(ConstEnum.Flag.FALSE.getValue());
        vehicle.setCreateTime(new Date());
        int result = vehicleMapper.insert(vehicle);
        if (result == 0) {
            return ExtResult.failResult("操作失败");
        }else {
            return ExtResult.successResult();
        }
    }

    @Transactional(rollbackFor=Throwable.class)
    public ExtResult update(Vehicle vehicle) {
        Vehicle dbVehicle = vehicleMapper.find(vehicle.getId());
        if (dbVehicle.getUpLineStatus() == ConstEnum.Flag.TRUE.getValue() && vehicle.getUpLineStatus() == ConstEnum.Flag.FALSE.getValue()) {
            vehicleMapper.clearUpLineTime(vehicle.getId());
        } else if (dbVehicle.getUpLineStatus() == ConstEnum.Flag.FALSE.getValue() && vehicle.getUpLineStatus() == ConstEnum.Flag.TRUE.getValue()){
            vehicle.setUpLineTime(new Date());
        }
        vehicle.setAgentName(agentMapper.find(vehicle.getAgentId()).getAgentName());
        int result = vehicleMapper.update(vehicle);
        if (result == 0) {
            return ExtResult.failResult("操作失败");
        }else {
            return ExtResult.successResult();
        }
    }

    @Transactional(rollbackFor=Throwable.class)
    public int updateLockStatus(Vehicle vehicle) {

        return  vehicleMapper.updateLockStatus(vehicle);

    }

    public ExtResult batchUpdateUpLineStatus(Integer[] vehicleIds, Integer modelId, Integer agentId, Integer upLineStatus, Date upLineTime) {
        int successCount = 0;
        AgentInfo agentInfo = findAgentInfo(agentId);
        String agentName = null;
        if (agentInfo != null) {
            agentName = agentInfo.getAgentName();
        }
        for (int i = 0; i < vehicleIds.length; i++) {
            if (vehicleMapper.updateUpLineStatus(vehicleIds[i], modelId, agentId, agentName, upLineStatus, upLineTime) == 1) {
                successCount++;
            }
        }
        return DataResult.successResult("成功上线" + successCount + "个车辆");
    }

    @Transactional(rollbackFor=Throwable.class)
    public ExtResult delete(Integer id) {
        CustomerVehicleInfo customerVehicleInfo = customerVehicleInfoMapper.findByVehicle(id);
        if (customerVehicleInfo != null) {
            return ExtResult.failResult("客户使用中不可删除");
        }
        int result = vehicleMapper.delete(id);
        if (result == 0) {
            return ExtResult.failResult("操作失败");
        }else {
            return ExtResult.successResult();
        }
    }

    @Transactional(rollbackFor=Throwable.class)
    public ExtResult clearShop(Integer id) {
        int result = vehicleMapper.clearShop(id);
        if (result == 0) {
            return ExtResult.failResult("操作失败");
        }else {
            return ExtResult.successResult();
        }
    }

    public int clearUpLineTime(int id) {
        return vehicleMapper.clearUpLineTime(id);
    }

    @Transactional(rollbackFor = Throwable.class)
    public ExtResult btchImportBattery(File mFile, Vehicle vehicle) {
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
            Vehicle dbVehicle = vehicleMapper.findByVinNo(vinNo);
            if (null == dbVehicle) {
                if (vehicle.getUpLineStatus() == ConstEnum.Flag.TRUE.getValue()) {
                    vehicle.setUpLineTime(new Date());
                }
                vehicle.setVinNo(vinNo);
                vehicle.setLockStatus(ConstEnum.Flag.FALSE.getValue());
                vehicle.setLockSwitch(ConstEnum.Flag.FALSE.getValue());
                vehicle.setAgentName(agentMapper.find(vehicle.getAgentId()).getAgentName());
                vehicle.setIsOnline(ConstEnum.Flag.FALSE.getValue());
                vehicle.setStatus(Vehicle.Status.NOT_USE.getValue());
                vehicle.setActiveStatus(ConstEnum.Flag.FALSE.getValue());
                vehicle.setCreateTime(new Date());
                try {
                    int total = 0;
                    total = vehicleMapper.insert(vehicle);
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
            }else {
                log.error("第{}条数据导入失败{}, 车架号已存在。", row);
                failCount++;
            }

        }
        if (StringUtils.isNotEmpty(failBuilder.toString())) {
            failBuilder.append("失败" + failCount + "条包括行数(" + failBuilder.toString() + "),表头不计行数!");
        }
        return DataResult.successResult("总条数" + (rows-1) + "条," + "成功导入" + successCount + "条!" + failBuilder.toString());
    }
}
