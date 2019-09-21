package cn.com.yusong.yhdg.webserver.service.hdg;

import cn.com.yusong.yhdg.common.constant.ConstEnum;
import cn.com.yusong.yhdg.common.constant.Constant;
import cn.com.yusong.yhdg.common.domain.hdg.*;
import cn.com.yusong.yhdg.common.entity.json.DataResult;
import cn.com.yusong.yhdg.common.entity.json.ExtResult;
import cn.com.yusong.yhdg.common.entity.pagination.Page;
import cn.com.yusong.yhdg.webserver.persistence.hdg.*;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class UnregisterBatteryService {
    @Autowired
    UnregisterBatteryMapper unregisterBatteryMapper;
    @Autowired
    BatteryMapper batteryMapper;
    @Autowired
    BatteryBarcodeMapper batteryBarcodeMapper;
    @Autowired
    BatteryFormatMapper batteryFormatMapper;
    @Autowired
    BatteryOrderMapper batteryOrderMapper;
    @Autowired
    BatteryOperateLogMapper batteryOperateLogMapper;
    @Autowired
    BatteryCellMapper batteryCellMapper;
    @Autowired
    BatterySequenceMapper batterySequenceMapper;

    public Page findPage(UnregisterBattery search) {
        Page page = search.buildPage();
        page.setTotalItems(unregisterBatteryMapper.findPageCount(search));
        search.setBeginIndex(page.getOffset());
        List<UnregisterBattery> list = unregisterBatteryMapper.findPageResult(search);
        for (UnregisterBattery unregisterBattery : list) {
            if (unregisterBattery.getShellCode() != null) {
                BatteryBarcode batteryBarcode = batteryBarcodeMapper.findByBarcode(unregisterBattery.getShellCode());
                if (batteryBarcode != null) {
                    BatteryFormat batteryFormat = batteryFormatMapper.find(batteryBarcode.getBatteryFormatId());
                    unregisterBattery.setBatteryFormat1(batteryFormat);
                    unregisterBattery.setBatteryFormat2(batteryFormat);
                    unregisterBattery.setBatteryFormat3(batteryFormat);
                    unregisterBattery.setBatteryFormat4(batteryFormat);
                    unregisterBattery.setBatteryFormat5(batteryFormat);
                    unregisterBattery.setBatteryFormat6(batteryFormat);
                    unregisterBattery.setExpectCellCount(batteryFormat.getCellCount());
                }
            }
        }
        page.setResult(list);
        return page;
    }

    public UnregisterBattery find(String id) {
        return unregisterBatteryMapper.find(id);
    }

    public ExtResult findByShellCode(String shellCode) {
        UnregisterBattery unregisterBattery = unregisterBatteryMapper.findByShellCode(shellCode);
        if (unregisterBattery == null) {
            return ExtResult.failResult("");
        } else {
            return DataResult.successResult(unregisterBattery);
        }
    }

    @Transactional(rollbackFor = Throwable.class)
    public ExtResult checkBindParam(String code, String shellCode) {
        UnregisterBattery unregisterBattery = unregisterBatteryMapper.findByCodeAndShellCode(code, shellCode);
        if (unregisterBattery == null) {
            UnregisterBattery byCode = unregisterBatteryMapper.findByCode(code);
            if (byCode != null) {
                return ExtResult.failResult("该BMS编号对应的电池已存在，请输入正确的条码");
            }
            int batteryCount = unregisterBatteryMapper.findCountByShellCode(shellCode);
            if (batteryCount >= 1) {
                return ExtResult.failResult("该条码对应的电池已存在，请不要输入错误的BMS编号");
            } else {
                UnregisterBattery newUnregisterBattery = new UnregisterBattery();
                newUnregisterBattery.setCode(code);
                newUnregisterBattery.setShellCode(code);
                return DataResult.successResult(newUnregisterBattery);
            }
        } else {
            return DataResult.successResult(unregisterBattery);
        }
    }

    @Transactional(rollbackFor = Throwable.class)
    public ExtResult createCheckedBattery(UnregisterBattery unregisterBattery) {
        UnregisterBattery dbBattery = unregisterBatteryMapper.findByCodeAndShellCode(unregisterBattery.getCode(), unregisterBattery.getShellCode());
        if (dbBattery != null) {
            return DataResult.successResult(dbBattery);
        }
        if (StringUtils.isEmpty(unregisterBattery.getId())) {
            unregisterBattery.setId(nextBatteryId());
        }
        if (StringUtils.isNotEmpty(unregisterBattery.getShellCode())) {
            int result2 = batteryMapper.findShellCode(unregisterBattery.getShellCode(), null);
            if (result2 != 0) {
                return ExtResult.failResult("电池外壳编号已存在");
            }
            int result3 = batteryMapper.findUniqueCode(unregisterBattery.getCode(), unregisterBattery.getId());
            if (result3 != 0 && !unregisterBattery.getCode().equals("")) {
                return ExtResult.failResult("BMS编号已存在");
            }
        }
        unregisterBattery.setQrcode("");
        unregisterBattery.setVolume(0);
        unregisterBattery.setUseCount(0);
        unregisterBattery.setCellCount(0);
        unregisterBattery.setIsOnline(ConstEnum.Flag.FALSE.getValue());
        unregisterBattery.setBatteryType(UnregisterBattery.BatteryType.UNREGISTER.getValue());
        unregisterBattery.setCreateTime(new Date());

        if (unregisterBatteryMapper.insert(unregisterBattery) >= 1) {
            return DataResult.successResult(unregisterBattery);
        } else {
            return ExtResult.failResult("创建失败");
        }
    }

    private String nextBatteryId() {
        BatterySequence entity = new BatterySequence();
        batterySequenceMapper.insert(entity);
        String result = "Z" + StringUtils.leftPad(Integer.toString(entity.getId(), 35), 7, '0').toUpperCase();
        return result;
    }


    @Transactional(rollbackFor = Throwable.class)
    public ExtResult delete(String id) {
        batteryCellMapper.unbindByBatteryId(id);
        if (unregisterBatteryMapper.delete(id) >= 1) {
            Battery battery = batteryMapper.find(id);
            if (battery != null) {
                batteryMapper.updateShellCode(id, "");
                batteryMapper.updateQrcode(id, "");
            }
            return ExtResult.successResult();
        } else {
            return ExtResult.failResult("删除失败");
        }
    }

    public ExtResult updateCellBind(String id, String shellCode, String newShellCode) {
        //查询新电池条码是否存在
        BatteryBarcode newBatteryBarcode = batteryBarcodeMapper.findByBarcode(newShellCode);
        if (newBatteryBarcode == null) {
            return ExtResult.failResult("新电池条码不存在");
        }
        //如果旧电池条码对应的电池没有绑定电芯，直接返回
        UnregisterBattery dbUnregisterBattery = unregisterBatteryMapper.find(id);
        if (dbUnregisterBattery.getCellCount() == 0) {
            return ExtResult.failResult("旧电池条码对应的电池没有绑定电芯");
        }
        //如果新电池条码已经可以找到对应的未注册电池，并且已绑定了电芯，提示新电池条码已绑定过电芯，无法获得转移电芯
        UnregisterBattery unregisterBattery = unregisterBatteryMapper.findByShellCode(newShellCode);
        if (unregisterBattery != null && unregisterBattery.getCellCount() != 0) {
            return ExtResult.failResult("新电池条码已绑定过电芯，无法获得转移电芯");
        }
        BatteryFormat newBatteryFormat = null;
        if (newBatteryBarcode.getBatteryFormatId() == null || (newBatteryFormat = batteryFormatMapper.find(newBatteryBarcode.getBatteryFormatId())) == null) {
            return ExtResult.failResult("新电池条码对应的电池规格不存在");
        }
        //判断新旧电池规格的电芯串数是否相同
        BatteryBarcode oldBatteryBarcode = batteryBarcodeMapper.findByBarcode(shellCode);
        BatteryFormat oldBatteryFormat = batteryFormatMapper.find(oldBatteryBarcode.getBatteryFormatId());
        if (oldBatteryFormat.getCellCount().intValue() != newBatteryFormat.getCellCount().intValue()) {
            return ExtResult.failResult("新旧电池条码对应的电芯串数不相同");
        }
        //判断新旧电池的电芯型号是否相同
        if (!oldBatteryFormat.getCellModel().equals(newBatteryFormat.getCellModel())) {
            return ExtResult.failResult("新旧电池条码对应的电芯型号不同");
        }
        //判断新电池的规格参数和旧电池上的电芯参数是否符合
        //1.利用旧电池的id查询所有绑定的电芯
        List<BatteryCell> batteryCellList = batteryCellMapper.findByBatteryId(id);
        for (BatteryCell batteryCell : batteryCellList) {
            //2.检验电芯参数

            int result1 = (batteryCell.getNominalCap() >= newBatteryFormat.getNominalCap() * 1000 - newBatteryFormat.getMinNominalCap() * 1000 &&
                    batteryCell.getNominalCap() <= newBatteryFormat.getNominalCap() * 1000 + newBatteryFormat.getMaxNominalCap() * 1000) ? 1 : 0;

            int result2 = (batteryCell.getAcResistance() >= newBatteryFormat.getAcResistance() - newBatteryFormat.getMinAcResistance() &&
                    batteryCell.getAcResistance() <= newBatteryFormat.getAcResistance() + newBatteryFormat.getMaxAcResistance()) ? 1 : 0;

            int result3 = (batteryCell.getResilienceVol() >= newBatteryFormat.getResilienceVol() * 1000 - newBatteryFormat.getMinResilienceVol() * 1000 &&
                    batteryCell.getResilienceVol() <= newBatteryFormat.getResilienceVol() * 1000 + newBatteryFormat.getMaxResilienceVol() * 1000) ? 1 : 0;

            int result4 = (batteryCell.getStaticVol() >= newBatteryFormat.getStaticVol() * 1000 - newBatteryFormat.getMinStaticVol() * 1000 &&
                    batteryCell.getStaticVol() <= newBatteryFormat.getStaticVol() * 1000 + newBatteryFormat.getMaxStaticVol() * 1000) ? 1 : 0;

            int result5 = (batteryCell.getCircle() >= newBatteryFormat.getCircle() - newBatteryFormat.getMinCircle() &&
                    batteryCell.getCircle() <= newBatteryFormat.getCircle() + newBatteryFormat.getMaxCircle()) ? 1 : 0;

            if (result1 + result2 + result3 + result4 + result5 != 5) {
                return ExtResult.failResult("有部分电芯参数不符合新电池条码对应的规格条件");
            }
        }
        //1.有未注册电池
        if (unregisterBattery != null && unregisterBattery.getCellCount() == 0) {
            //批量绑定电芯到新电池上
            for (BatteryCell batteryCell : batteryCellList) {
                batteryCellMapper.bindBattery(batteryCell.getId(), unregisterBattery.getId());
            }
            //更新电芯串数
            unregisterBatteryMapper.updateCellCount(unregisterBattery.getId(), batteryCellList.size());
            unregisterBatteryMapper.updateCellCount(id, 0);
            return ExtResult.successResult();
        } else {
            //2.利用新条码新建未注册电池
            UnregisterBattery newUnregisterBattery = new UnregisterBattery();
            newUnregisterBattery.setId(nextBatteryId());
            newUnregisterBattery.setCode("");
            newUnregisterBattery.setShellCode(newShellCode);
            newUnregisterBattery.setQrcode("");
            newUnregisterBattery.setVolume(0);
            newUnregisterBattery.setUseCount(0);
            newUnregisterBattery.setCellCount(0);
            newUnregisterBattery.setIsOnline(ConstEnum.Flag.FALSE.getValue());
            newUnregisterBattery.setBatteryType(UnregisterBattery.BatteryType.UNREGISTER.getValue());
            newUnregisterBattery.setCreateTime(new Date());

            unregisterBatteryMapper.insert(newUnregisterBattery);
            //批量绑定电芯到新电池上
            for (BatteryCell batteryCell : batteryCellList) {
                batteryCellMapper.bindBattery(batteryCell.getId(), newUnregisterBattery.getId());
            }
            //更新电芯串数
            unregisterBatteryMapper.updateCellCount(newUnregisterBattery.getId(), batteryCellList.size());
            unregisterBatteryMapper.updateCellCount(id, 0);
            return ExtResult.successResult();
        }
    }

    @Transactional(rollbackFor = Throwable.class)
    public ExtResult clearCode(String id) {
        UnregisterBattery unregisterBattery = unregisterBatteryMapper.find(id);
        if (unregisterBatteryMapper.clearCode(id, unregisterBattery.getCode(), "") >= 1) {
            Battery battery = batteryMapper.find(id);
            if (battery != null) {
                batteryMapper.updateShellCode(id, "");
                batteryMapper.updateQrcode(id, "");
            }
            return ExtResult.successResult();
        } else {
            return ExtResult.failResult("清空失败");
        }
    }

    @Transactional(rollbackFor = Throwable.class)
    public ExtResult updateQrcode(String id, String qrcode) {
        UnregisterBattery unregisterBattery = unregisterBatteryMapper.find(id);
        if (!unregisterBattery.getQrcode().equals(qrcode)) {
            if (unregisterBatteryMapper.findCountByQrcode(qrcode) > 0) {
                return ExtResult.failResult("电池二维码已存在，交换失败");
            }
        }
        //更新二维码
        unregisterBatteryMapper.updateQrcode(id, qrcode);

        Battery battery = batteryMapper.find(id);
        if (battery != null) {
            //换电电池更新二维码
            batteryMapper.updateQrcode(battery.getId(), qrcode);
        }
        return ExtResult.successResult();
    }

    public ExtResult checkBattery(UnregisterBattery unregisterBattery) {
        if (unregisterBatteryMapper.checkBattery(unregisterBattery.getId(), unregisterBattery.getCellMfr(), unregisterBattery.getCellModel()) >= 1) {
            return ExtResult.successResult();
        } else {
            return ExtResult.failResult("检验失败");
        }
    }

    public ExtResult updateCode(String id, String code) {
        UnregisterBattery unregisterBattery = unregisterBatteryMapper.find(id);

        //若表中已存在该BMS编号，仍然要返回false
        if (unregisterBatteryMapper.findCountByCode(code) == 1) {
            UnregisterBattery dbUnregisterBattery = unregisterBatteryMapper.findByCode(code);
            if (dbUnregisterBattery != null && !dbUnregisterBattery.getId().equals(id)) {
                return ExtResult.failResult("BMS编号已存在");
            }
        }

        if (!unregisterBattery.getCode().equals(code)) {
            if (batteryMapper.findCountByCode(code) == 1) {//换电电池表中存在这个code
                Battery battery = batteryMapper.findByCode(code);

                if (battery != null) {
                    //换电电池更新二维码
                    batteryMapper.updateQrcode(battery.getId(), unregisterBattery.getQrcode());
                    //智租电池特殊处理
                    if (code.startsWith("B") && code.contains("YS")) {
                        //换电电池更新条码编号
                        batteryMapper.updateShellCode(battery.getId(), code);
                    } else {
                        //换电电池更新条码编号
                        batteryMapper.updateShellCode(battery.getId(), unregisterBattery.getShellCode());
                    }
                }

                //将换电电池的id和code同步过来
                unregisterBatteryMapper.updateIdAndCode(id, battery.getId(), code);
                //将绑定过的电芯重新绑定batteryId
                List<BatteryCell> batteryCellList = batteryCellMapper.findByBatteryId(id);
                for (BatteryCell batteryCell : batteryCellList) {
                    batteryCellMapper.bindBattery(batteryCell.getId(), battery.getId());
                }
                return ExtResult.successResult();
            } else if (batteryMapper.findCountByCode(code) == 0) {
                Battery dbBattery = batteryMapper.find(id);
                if (dbBattery == null) {//插入一条新电池，带上id和code
                    //更新BMS编号
                    unregisterBatteryMapper.updateCode(id, code);

                    //插入一条初始化的数据，以保证两个表的数据一致，带上code
                    Battery battery = new Battery();
                    battery.setId(id);
                    battery.setCode(code);
                    battery.setQrcode(unregisterBattery.getQrcode());
                    //智租电池特殊处理
                    if (code.startsWith("B") && code.contains("YS")) {
                        battery.setShellCode(code);
                    } else {
                        battery.setShellCode(unregisterBattery.getShellCode());
                    }
                    battery.setType(2);//默认60C
                    battery.setCategory(Battery.Category.EXCHANGE.getValue());
                    battery.setAgentId(1);
                    battery.setOrderDistance(0);
                    battery.setTotalDistance(0L);
                    battery.setIsActive(ConstEnum.Flag.TRUE.getValue());//默认启用
                    battery.setExchangeAmount(0);
                    battery.setIsReportVoltage(ConstEnum.Flag.FALSE.getValue());
                    battery.setVolume(0);
                    battery.setUseCount(0);
                    battery.setCellCount(0);
                    battery.setIsOnline(ConstEnum.Flag.FALSE.getValue());
                    battery.setIsNormal(ConstEnum.Flag.TRUE.getValue());
                    battery.setStatus(Battery.Status.NOT_USE.getValue());
                    battery.setCreateTime(new Date());
                    battery.setStayHeartbeat(Constant.STAY_HEARTBEAT);
                    battery.setMoveHeartbeat(Constant.MOVE_HEARTBEAT);
                    battery.setElectrifyHeartbeat(Constant.ELECTRIFY_HEARTBEAT);
                    battery.setChargeCompleteVolume(95);
                    battery.setGpsSwitch(ConstEnum.Flag.FALSE.getValue());
                    battery.setLockSwitch(ConstEnum.Flag.FALSE.getValue());
                    battery.setGprsShutdown(ConstEnum.Flag.FALSE.getValue());
                    battery.setShutdownVoltage(42000);
                    battery.setAcceleretedSpeed(1);
                    battery.setRepairStatus(Battery.RepairStatus.NOT.getValue());
                    battery.setChargeStatus(Battery.ChargeStatus.NOT_CHARGE.getValue());
                    batteryMapper.insert(battery);
                } else {//已存在该电池，但是code不一样，那就把本地的code更新到换电电池表中
                    //更新BMS编号
                    unregisterBatteryMapper.updateCode(id, code);
                    //换电电池更新二维码
                    batteryMapper.updateQrcode(dbBattery.getId(), unregisterBattery.getQrcode());
                    //智租电池特殊处理
                    if (code.startsWith("B") && code.contains("YS")) {
                        //换电电池更新条码编号
                        batteryMapper.updateShellCode(dbBattery.getId(), code);
                    } else {
                        //换电电池更新条码编号
                        batteryMapper.updateShellCode(dbBattery.getId(), unregisterBattery.getShellCode());
                    }
                    //换电电池更新code
                    batteryMapper.updateCode(id, code);
                }
            }
        }
        return ExtResult.successResult();
    }
}
