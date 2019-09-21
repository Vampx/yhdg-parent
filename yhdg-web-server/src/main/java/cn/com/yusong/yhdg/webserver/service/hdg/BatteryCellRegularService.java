package cn.com.yusong.yhdg.webserver.service.hdg;

import cn.com.yusong.yhdg.common.domain.hdg.BatteryCellRegular;
import cn.com.yusong.yhdg.common.entity.json.ExtResult;
import cn.com.yusong.yhdg.webserver.persistence.hdg.*;
import cn.com.yusong.yhdg.webserver.service.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class BatteryCellRegularService extends AbstractService {

    @Autowired
    BatteryCellRegularMapper batteryCellRegularMapper;

    public BatteryCellRegular find(long id) {
        return batteryCellRegularMapper.find(id);
    }

    public BatteryCellRegular findByCellFormatIdAndType(long cellFormatId, int regularType) {
        return batteryCellRegularMapper.findByCellFormatIdAndType(cellFormatId, regularType);
    }

    public BatteryCellRegular findByBatteryFormatIdAndType(long batteryFormatId, int regularType) {
        return batteryCellRegularMapper.findByBatteryFormatIdAndType(batteryFormatId, regularType);
    }

    @Transactional(rollbackFor=Throwable.class)
    public ExtResult create(BatteryCellRegular batteryCellRegular) {
        Date date = new Date();
        batteryCellRegular.setCreateTime(date);
        batteryCellRegular.setUpdateTime(date);
        batteryCellRegular.setNum(0);
        batteryCellRegularMapper.insert(batteryCellRegular);
        return ExtResult.successResult();
    }

    public ExtResult checkParam(BatteryCellRegular batteryCellRegular) {
        List<BatteryCellRegular> batteryCellRegularList = batteryCellRegularMapper.findByRegularAndType(batteryCellRegular.getRegular(), batteryCellRegular.getRegularType());
        if (batteryCellRegular.getCellFormatId() == null && batteryCellRegular.getBatteryFormatId() == null) {//新建
            if (batteryCellRegularList.size() > 0) {
                return ExtResult.failResult("条码规则已存在");
            }
        } else {//修改
            if (batteryCellRegularList.size() > 1) {
                return ExtResult.failResult("条码规则已存在");
            }
            if (batteryCellRegularList.size() == 1 && batteryCellRegularList.get(0) != null) {
                if (batteryCellRegular.getRegularType() == BatteryCellRegular.RegularType.CELL_FORMAT.getValue()) {
                    BatteryCellRegular dbBatteryCellRegular = batteryCellRegularMapper.findByCellFormatIdAndType(batteryCellRegular.getCellFormatId(), batteryCellRegular.getRegularType());
                    if (batteryCellRegularList.get(0).getId().longValue() != dbBatteryCellRegular.getId().longValue()) {
                        return ExtResult.failResult("条码规则已存在");
                    }
                } else if (batteryCellRegular.getRegularType() == BatteryCellRegular.RegularType.BATTERY_FORMAT.getValue()) {
                    BatteryCellRegular dbBatteryCellRegular = batteryCellRegularMapper.findByBatteryFormatIdAndType(batteryCellRegular.getBatteryFormatId(), batteryCellRegular.getRegularType());
                    if (batteryCellRegularList.get(0).getId().longValue() != dbBatteryCellRegular.getId().longValue()) {
                        return ExtResult.failResult("条码规则已存在");
                    }
                }
            }
        }

        return ExtResult.successResult();
    }

    public ExtResult update(BatteryCellRegular batteryCellRegular) {
        BatteryCellRegular entity = null;
        if (batteryCellRegular.getCellFormatId() != null && batteryCellRegular.getBatteryFormatId() == null) {
            entity = batteryCellRegularMapper.findByCellFormatIdAndType(batteryCellRegular.getCellFormatId(), BatteryCellRegular.RegularType.CELL_FORMAT.getValue());
        }
        if (batteryCellRegular.getBatteryFormatId() != null && batteryCellRegular.getCellFormatId() == null) {
            entity = batteryCellRegularMapper.findByBatteryFormatIdAndType(batteryCellRegular.getBatteryFormatId(), BatteryCellRegular.RegularType.BATTERY_FORMAT.getValue());
        }
        if (entity.getRegular().equals(batteryCellRegular.getRegular()) && entity.getRegularName().equals(batteryCellRegular.getRegularName()) && entity.getResetType().equals(batteryCellRegular.getResetType())) {
            batteryCellRegular.setUpdateTime(entity.getUpdateTime());
        } else {
            batteryCellRegular.setUpdateTime(new Date());
        }
        if (batteryCellRegularMapper.update(batteryCellRegular) >= 1) {
            return ExtResult.successResult();
        }
        return ExtResult.failResult("条码规则修改失败");
    }

    public ExtResult updateNumByCellFormatId(long cellFormatId, int num) {
        batteryCellRegularMapper.updateNumByCellFormatId(cellFormatId, num);
        return ExtResult.successResult();
    }

    public ExtResult updateNumByBatteryFormatId(long batteryFormatId, int num) {
        batteryCellRegularMapper.updateNumByBatteryFormatId(batteryFormatId, num);
        return ExtResult.successResult();
    }

}
