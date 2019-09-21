package cn.com.yusong.yhdg.webserver.service.hdg;

import cn.com.yusong.yhdg.common.constant.ConstEnum;
import cn.com.yusong.yhdg.common.constant.Constant;
import cn.com.yusong.yhdg.common.domain.basic.DictCategory;
import cn.com.yusong.yhdg.common.domain.hdg.*;
import cn.com.yusong.yhdg.common.entity.json.DataResult;
import cn.com.yusong.yhdg.common.entity.json.ExtResult;
import cn.com.yusong.yhdg.common.entity.pagination.Page;
import cn.com.yusong.yhdg.common.utils.ExportXlsUtils;
import cn.com.yusong.yhdg.common.utils.IdUtils;
import cn.com.yusong.yhdg.common.utils.YhdgUtils;
import cn.com.yusong.yhdg.webserver.persistence.basic.DictItemMapper;
import cn.com.yusong.yhdg.webserver.persistence.basic.SystemConfigMapper;
import cn.com.yusong.yhdg.webserver.persistence.hdg.*;
import cn.com.yusong.yhdg.webserver.service.AbstractService;
import jxl.Sheet;
import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class BatteryService extends AbstractService {

	static Logger log = LoggerFactory.getLogger(BatteryService.class);

	@Autowired
	BatteryMapper batteryMapper;
	@Autowired
	BatteryParameterMapper batteryParameterMapper;
	@Autowired
	BatterySequenceMapper batterySequenceMapper;
	@Autowired
	UnregisterBatteryMapper unregisterBatteryMapper;
	@Autowired
	SystemConfigMapper systemConfigMapper;
	@Autowired
	DictItemMapper dictItemMapper;
	@Autowired
	BatteryOperateLogMapper batteryOperateLogMapper;
	@Autowired
	CabinetMapper cabinetMapper;
	@Autowired
	ShopMapper shopMapper;
	@Autowired
	BatteryOrderMapper batteryOrderMapper;
	@Autowired
	BatteryCellMapper batteryCellMapper;
	@Autowired
	BatteryFormatMapper batteryFormatMapper;
	@Autowired
	BatteryBarcodeMapper batteryBarcodeMapper;
	@Autowired
	BatteryInstallRecordService batteryInstallRecordService;

	@Transactional(rollbackFor = Throwable.class)
	public ExtResult rescue(String id) {
		BatteryOrder order = batteryOrderMapper.find(id);
		if (order == null) {
			return ExtResult.failResult("订单不存在");
		}
		if (order.getOrderStatus() == BatteryOrder.OrderStatus.PAY.getValue() || order.getOrderStatus() == BatteryOrder.OrderStatus.MANUAL_COMPLETE.getValue()) {
			return ExtResult.failResult("订单已结束");
		}
        if (order.getOrderStatus() != BatteryOrder.OrderStatus.TAKE_OUT.getValue()) {
            return ExtResult.failResult("必须在使用中才能自救");
        }

		Battery battery=batteryMapper.find(order.getBatteryId());
		if(battery.getRescueStatus() != null && battery.getRescueStatus() != Battery.RescueStatus.NOT.getValue()){
			if(battery.getRescueStatus()==Battery.RescueStatus.WAIT.getValue()){
				return ExtResult.failResult("待下发自救");
			}
			if(battery.getRescueStatus()==Battery.RescueStatus.END.getValue()){
				return ExtResult.failResult("已经是自救状态");
			}
			if(battery.getRescueStatus()==Battery.RescueStatus.RECOVERED.getValue()) {
				return ExtResult.failResult("备用电池已启动，请不要重复操作");
			}
		}else{
			batteryMapper.updateRescueStatus(battery.getId(),Battery.RescueStatus.WAIT.getValue());
		}
		return  ExtResult.successResult();
	}
	public Page findPage(Battery search) {
		Map<String, String> batteryBrandMap = findDictItemMap(DictCategory.CategoryType.BATTERY_BRAND.getValue());
		Page page = search.buildPage();
		page.setTotalItems(batteryMapper.findPageCount(search));
		search.setBeginIndex(page.getOffset());
		List<Battery> batteryList = batteryMapper.findPageResult(search);
		//客户使用中的电池
		search.setCustomerUseFlag(ConstEnum.Flag.TRUE.getValue());
		int countByCustomerUse = batteryMapper.findCountByCustomerUse(search);
		//在柜子中的电池
		search.setCustomerUseFlag(null);
		search.setInCabinetFlag(ConstEnum.Flag.TRUE.getValue());
		int countByInCabinet = batteryMapper.findCountByInCabinet(search);
		//未使用的电池
		search.setInCabinetFlag(null);
		search.setNotUseFlag(ConstEnum.Flag.TRUE.getValue());
		int countByNotUse = batteryMapper.findCountByNotUse(search);

		for (Battery battery : batteryList) {
			if (battery != null) {
				if (battery.getAgentId() != null) {
					battery.setAgentName(findAgentInfo(battery.getAgentId()).getAgentName());
				}
				if (battery.getType() != null) {
					String type = findBatteryType(battery.getType()).getTypeName();
                    battery.setBatteryType(type);
				}
				if (battery.getBrand() != null) {
					battery.setBrandName(batteryBrandMap.get(battery.getBrand()));
				}
				if (StringUtils.isNotEmpty(battery.getCabinetId())) {
					Cabinet cabinet = cabinetMapper.find(battery.getCabinetId());
					battery.setCabinetName(cabinet.getCabinetName());
				}
			}
		}
		Battery battery = null;
		if (batteryList.size() >= 1) {
			battery = batteryList.get(0);
		}
		if (battery != null) {
			battery.setFirstDataFlag(ConstEnum.Flag.TRUE.getValue());
			battery.setCountByCustomerUse(countByCustomerUse);
			battery.setCountByInCabinet(countByInCabinet);
			battery.setCountByNotUse(countByNotUse);
		}
		page.setResult(batteryList);
		return page;
	}

	public Battery findByShellCode(String shellCode) {
		return batteryMapper.findByShellCode(shellCode);
	}

	public Page findNotStorePage(Battery search) {
		Map<String, String> batteryBrandMap = findDictItemMap(DictCategory.CategoryType.BATTERY_BRAND.getValue());
		Page page = search.buildPage();
		page.setTotalItems(batteryMapper.findNotStorePageCount(search));
		search.setBeginIndex(page.getOffset());
		List<Battery> batteryList = batteryMapper.findNotStorePageResult(search);
		for (Battery battery : batteryList) {
			if (battery != null) {
				if (battery.getAgentId() != null) {
					battery.setAgentName(findAgentInfo(battery.getAgentId()).getAgentName());
				}
				if (battery.getType() != null) {
					String type = findBatteryType(battery.getType()).getTypeName();
					battery.setBatteryType(type);
				}
				if (battery.getBrand() != null) {
					battery.setBrandName(batteryBrandMap.get(battery.getBrand()));
				}
				if (StringUtils.isNotEmpty(battery.getCabinetId())) {
					Cabinet cabinet = cabinetMapper.find(battery.getCabinetId());
					battery.setCabinetName(cabinet.getCabinetName());
				}
			}
		}
		page.setResult(batteryList);
		return page;
	}

	public Page pageUpgrade(Battery search) {
		Page page = search.buildPage();
		page.setTotalItems(batteryMapper.findPageUpgradeCount(search));
		search.setBeginIndex(page.getOffset());
		List<Battery> batteryList = batteryMapper.findPageUpgradeResult(search);
		page.setResult(batteryList);
		return page;
	}

	public List<Battery> findForExcel(Battery search) {
		List<Battery> batteryList = batteryMapper.findPageResult(search);
		for (Battery battery : batteryList) {
			if (battery != null) {
				if (battery.getAgentId() != null) {
					battery.setAgentName(findAgentInfo(battery.getAgentId()).getAgentName());
				}
				if (battery.getType() != null) {
					String type = findBatteryType(battery.getType()).getTypeName();
					battery.setBatteryType(type);
				}
			}
		}
		return batteryList;
	}


	public String findSingleVoltage(String id) {
		return batteryMapper.findSingleVoltage(id);
	}

	public int findCountByPositionState(Integer positionState, Integer isOnline, Integer agentId) {
		return batteryMapper.findCountByPositionState(positionState, isOnline, agentId);
	}

	public Page findList(Battery search) {
		Page page = search.buildPage();
		page.setTotalItems(batteryMapper.findPageCount(search));
		search.setBeginIndex(page.getOffset());
		List<Battery> batteries = batteryMapper.findPageResult(search);

		for (Battery battery : batteries) {
			if (battery.getPositionState() != null && battery.getIsOnline().equals(ConstEnum.Flag.TRUE.getValue())) {
				if (battery.getPositionState().equals(Battery.PositionState.MOVE.getValue())) {
					battery.setImageSuffix("1");
				} else {
					battery.setImageSuffix("2");
				}
			} else {
				battery.setImageSuffix("3");
			}
		}
		page.setResult(batteries);
		return page;

	}

	public Battery find(String id) {
		Map<String, String> batteryBrandMap = findDictItemMap(DictCategory.CategoryType.BATTERY_BRAND.getValue());
		Battery battery = batteryMapper.find(id);
		if (battery != null) {
			if (battery.getAgentId() != null) {
				battery.setAgentName(findAgentInfo(battery.getAgentId()).getAgentName());
			}
			if (battery.getBrand() != null) {
				battery.setBrandName(batteryBrandMap.get(battery.getBrand().toString()));
			}

			if (StringUtils.isNotEmpty(battery.getCabinetId())) {
				Cabinet cabinet = cabinetMapper.find(battery.getCabinetId());
				battery.setCabinetName(cabinet.getCabinetName());
			}
		}
		return battery;
	}



	private String nextBatteryId() {
		BatterySequence entity = new BatterySequence();
		batterySequenceMapper.insert(entity);
		String result = "Z" + StringUtils.leftPad(Integer.toString(entity.getId(), 35), 7, '0').toUpperCase();
		return result;
	}

	@Transactional(rollbackFor = Throwable.class)
	public ExtResult create(Battery battery) {
		if (StringUtils.isEmpty(battery.getId())) {
			battery.setId(nextBatteryId());
		}

		if (StringUtils.isEmpty(battery.getQrcode())) {
			battery.setQrcode(battery.getId());
		}
		int result = batteryMapper.findQrcode(battery.getQrcode(), null);
		if (result != 0) {
			return ExtResult.failResult("二维码已存在");
		}
		if (StringUtils.isNotEmpty(battery.getShellCode())) {
			int result2 = batteryMapper.findShellCode(battery.getShellCode(), null);
			if (result2 != 0) {
				return ExtResult.failResult("外壳编号已存在");
			}
			int result3 = batteryMapper.findUniqueCode(battery.getCode(), battery.getId());
			if (result3 != 0) {
				return ExtResult.failResult("电池唯一码已存在");
			}
		}

		battery.setOrderDistance(0);
		battery.setTotalDistance(0L);
		battery.setExchangeAmount(0);
		battery.setVolume(0);
		battery.setUseCount(0);
		battery.setIsOnline(ConstEnum.Flag.FALSE.getValue());
		battery.setStatus(Battery.Status.NOT_USE.getValue());
		battery.setChargeStatus(Battery.ChargeStatus.NOT_CHARGE.getValue());
		battery.setRepairStatus(Battery.RepairStatus.NOT.getValue());
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
		battery.setCategory(Battery.Category.EXCHANGE.getValue());

		if (batteryMapper.insert(battery) >= 1) {
			return ExtResult.successResult();
		} else {
			return ExtResult.failResult("创建失败");
		}
	}

	@Transactional(rollbackFor = Throwable.class)
	public ExtResult checkBindParam(String code, String shellCode) {
		Battery battery = batteryMapper.findByCodeAndShellCode(code, shellCode);
		if (battery == null) {
			Battery byCode = batteryMapper.findByCode(code);
			if (byCode != null) {
				return ExtResult.failResult("该BMS编号对应的电池已存在，请输入正确的条码");
			}
			int batteryCount = batteryMapper.findCountByShellCode(shellCode);
			if (batteryCount >= 1) {
				return ExtResult.failResult("该条码对应的电池已存在，请不要输入错误的BMS编号");
			} else {
				Battery newBattery = new Battery();
				newBattery.setCode(code);
				newBattery.setShellCode(code);
				return DataResult.successResult(newBattery);
			}
		}else {
			return DataResult.successResult(battery);
		}
	}

	@Transactional(rollbackFor = Throwable.class)
	public ExtResult createCheckedBattery(Battery battery) {
		Battery dbBattery = batteryMapper.findByCodeAndShellCode(battery.getCode(), battery.getShellCode());
		if (dbBattery != null) {
			return DataResult.successResult(dbBattery);
		}
		if (StringUtils.isEmpty(battery.getId())) {
			battery.setId(nextBatteryId());
		}
		if (StringUtils.isNotEmpty(battery.getShellCode())) {
			int result2 = batteryMapper.findShellCode(battery.getShellCode(), null);
			if (result2 != 0) {
				return ExtResult.failResult("电池外壳编号已存在");
			}
			int result3 = batteryMapper.findUniqueCode(battery.getCode(), battery.getId());
			if (result3 != 0 && !battery.getCode().equals("")) {
				return ExtResult.failResult("BMS编号已存在");
			}
		}
		battery.setQrcode("");
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

		if (batteryMapper.insert(battery) >= 1) {
			return DataResult.successResult(battery);
		} else {
			return ExtResult.failResult("创建失败");
		}
	}

	public ExtResult findUnique(String id) {
		Battery battery = batteryMapper.findUnique(id);
		if (battery == null) {
			return ExtResult.successResult();
		}
		return ExtResult.failResult("电池编号已存在");
	}

	public ExtResult findUniqueCode(String code, String id) {
		boolean unique = batteryMapper.findUniqueCode(code, id) == 0;
		if (unique) {
			return ExtResult.successResult();
		} else {
			return ExtResult.failResult("电池唯一码已存在");
		}
	}

	public ExtResult findQrcode(String code, String id) {
		boolean unique = batteryMapper.findQrcode(code, id) == 0;
		if (unique) {
			return ExtResult.successResult();
		} else {
			return ExtResult.failResult("电池二维码已存在");
		}
	}

	public ExtResult findShellCode(String code, String id) {
		if (StringUtils.isNotEmpty(code)) {
			boolean unique = batteryMapper.findShellCode(code, id) == 0;
			if (unique) {
				return ExtResult.successResult();
			} else {
				return ExtResult.failResult("电池外壳编号已存在");
			}
		} else {
			return ExtResult.successResult();
		}
	}

	public ExtResult update(Battery battery) {
		Battery entity = batteryMapper.find(battery.getId());
		if (battery.getAgentId() == null) {
			return ExtResult.failResult("运营商不能为空");
		}
		if(StringUtils.isNotEmpty(battery.getQrcode())){
			int result = batteryMapper.findQrcode(battery.getQrcode(), battery.getId());
			if (result != 0) {
				return ExtResult.failResult("电池二维码已存在");
			}
		}
		if (StringUtils.isNotEmpty(battery.getShellCode())) {
			int result2 = batteryMapper.findShellCode(battery.getShellCode(), battery.getId());
			if (result2 != 0) {
				return ExtResult.failResult("电池外壳编号已存在");
			}
		}
		int result3 = batteryMapper.findUniqueCode(battery.getCode(), battery.getId());
		if (result3 != 0) {
			return ExtResult.failResult("电池唯一码已存在");
		}
		if (batteryMapper.find(battery.getId()).getBelongCabinetId() == null) {
			battery.setBelongCabinetId(battery.getCabinetId());
		} else {
			battery.setBelongCabinetId(null);
		}
		if (battery.getUpLineStatus() == BatteryInstallRecord.Status.YESONLINE.getValue() && (entity.getUpLineStatus() == null || entity.getUpLineStatus() != BatteryInstallRecord.Status.YESONLINE.getValue())){
			BatteryInstallRecord batteryInstallRecord = new BatteryInstallRecord();
			batteryInstallRecord.setBatteryId(battery.getId());
			batteryInstallRecord.setBatteryType(battery.getType());
			batteryInstallRecord.setAgentId(battery.getAgentId());
			batteryInstallRecord.setStatus(BatteryInstallRecord.Status.YESONLINE.getValue());
			batteryInstallRecord.setCreateTime(new Date());
			battery.setUpLineTime(new Date());
			batteryInstallRecordService.insert(batteryInstallRecord);
		}
		if (battery.getUpLineStatus() == BatteryInstallRecord.Status.NOTONLINE.getValue() && (entity.getUpLineStatus() == null || entity.getUpLineStatus() == BatteryInstallRecord.Status.YESONLINE.getValue())) {
			battery.setUpLineTime(null);
			battery.setAgentId(1);
		}
		if (batteryMapper.update(battery) >= 1) {
			return ExtResult.successResult();
		} else {
			return ExtResult.failResult("修改失败");
		}
	}

	@Transactional(rollbackFor = Throwable.class)
	public ExtResult changeToNormal(String[] ids, Integer[] isNormalList) {
		for (int i = 0; i < ids.length; i++) {
			if (isNormalList[i] == ConstEnum.Flag.TRUE.getValue()) {
				continue;
			}
			batteryMapper.changeIsNormal(ids[i], isNormalList[i], null,null,null);
		}
		return ExtResult.successResult();
	}

	@Transactional(rollbackFor = Throwable.class)
	public ExtResult changeToAbnormal(String[] ids, Integer[] isNormalList, String abnormalCause, String operator,Date operatorTime) {
		for (int i = 0; i < ids.length; i++) {
			if (isNormalList[i] == ConstEnum.Flag.FALSE.getValue()) {
				continue;
			}
			batteryMapper.changeIsNormal(ids[i], isNormalList[i], abnormalCause, operator, operatorTime);
		}
		return ExtResult.successResult();
	}

	@Transactional(rollbackFor = Throwable.class)
	public ExtResult updateIsNormal(Battery battery) {
		batteryMapper.updateIsNormal(battery);
		return ExtResult.successResult();
	}

	@Transactional(rollbackFor = Throwable.class)
	public ExtResult updateUpLineStatus(Battery battery) {
		Battery dbBattery = null;
		if (StringUtils.isNotEmpty(battery.getId())) {
			dbBattery = batteryMapper.find(battery.getId());
		} else {
			dbBattery = batteryMapper.findByCode(battery.getCode());
		}
		if (dbBattery == null) {
			return ExtResult.failResult("电池不存在");
		}
		if (dbBattery.getUpLineStatus() == Battery.UpLineStatus.ONLINE.getValue()) {
			return ExtResult.failResult("该电池已上线");
		}
		int result = batteryMapper.updateUpLineStatus(dbBattery.getId(), battery.getAgentId(), Battery.UpLineStatus.ONLINE.getValue(), new Date());
		if (result == 0) {
			return ExtResult.failResult("电池上线失败");
		} else {
			BatteryInstallRecord batteryInstallRecord = new BatteryInstallRecord();
			batteryInstallRecord.setBatteryId(dbBattery.getId());
			batteryInstallRecord.setBatteryType(dbBattery.getType());
			batteryInstallRecord.setAgentId(battery.getAgentId());
			batteryInstallRecord.setStatus(Battery.UpLineStatus.ONLINE.getValue());
			batteryInstallRecord.setCreateTime(new Date());
			battery.setUpLineTime(new Date());
			batteryInstallRecordService.insert(batteryInstallRecord);
			return ExtResult.successResult();
		}
	}

	@Transactional(rollbackFor = Throwable.class)
	public ExtResult boxUpdateUpLineStatus(Battery battery) {
		Battery dbBattery = null;
		if (StringUtils.isNotEmpty(battery.getId())) {
			dbBattery = batteryMapper.find(battery.getId());
		}
		if (dbBattery == null) {
			return ExtResult.failResult("电池不存在");
		}
		if (dbBattery.getUpLineStatus() == Battery.UpLineStatus.ONLINE.getValue()) {
			return ExtResult.failResult("该电池已上线");
		}
		int result = batteryMapper.updateUpLineStatus(dbBattery.getId(), battery.getAgentId(), Battery.UpLineStatus.ONLINE.getValue(), new Date());
		if (result == 0) {
			return ExtResult.failResult("电池上线失败");
		} else {
			BatteryInstallRecord batteryInstallRecord = new BatteryInstallRecord();
			batteryInstallRecord.setBatteryId(dbBattery.getId());
			batteryInstallRecord.setBatteryType(dbBattery.getType());
			batteryInstallRecord.setAgentId(battery.getAgentId());
			batteryInstallRecord.setStatus(Battery.UpLineStatus.ONLINE.getValue());
			batteryInstallRecord.setCreateTime(new Date());
			battery.setUpLineTime(new Date());
			batteryInstallRecordService.insert(batteryInstallRecord);
			return ExtResult.successResult();
		}
	}

	@Transactional(rollbackFor = Throwable.class)
	public ExtResult delete(String id) {
		Battery battery = batteryMapper.find(id);
		if (Battery.Status.NOT_USE.getValue() != battery.getStatus()) {
			return ExtResult.failResult("电池状态不是未使用，删除失败");
		}
		batteryOrderMapper.deleteByBatteryId(id);
		batteryOperateLogMapper.delete(id);
		if (batteryMapper.delete(id) >= 1) {
			return ExtResult.successResult();
		} else {
			return ExtResult.failResult("删除失败");
		}
	}

	@Transactional(rollbackFor = Throwable.class)
	public ExtResult btchImportBattery(File mFile, Integer agentId) {
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
		for (int row = 1; row <= rows; row++) {
			Battery battery = new Battery();
			//获取电池编号
			String batteryId = sheet.getCell(0, row).getContents().trim();
			if (!StringUtils.isNotEmpty(batteryId)) {
				continue;
			}
			//获取电池类型
			int type = parseInt(sheet.getCell(1, row).getContents(), 1);
			//满电电量
			int fullVolume = parseInt(sheet.getCell(2, row).getContents(), 90);
			//sim卡信息
			String simMemo = sheet.getCell(3, row).getContents().trim();
			//品牌
			String brand = sheet.getCell(4, row).getContents().trim();

			String IMEI = sheet.getCell(10, row).getContents().trim();
			if (!StringUtils.isNotEmpty(IMEI)) {
				continue;
			}
			//二维码
			String qrcode = sheet.getCell(11, row).getContents().trim();
			//外壳编号
			String shellCode = sheet.getCell(12, row).getContents().trim();
			if (!StringUtils.isNotEmpty(shellCode)) {
				shellCode = batteryId;
			}

			battery.setId(batteryId);
			battery.setAgentId(agentId);
			battery.setType(type);
			battery.setCategory(Battery.Category.EXCHANGE.getValue());
			battery.setSimMemo(simMemo);
			battery.setBrand(brand);
			battery.setStatus(Battery.Status.NOT_USE.getValue());
			battery.setExchangeAmount(0);
			battery.setVolume(0);
			battery.setIsActive(ConstEnum.Flag.TRUE.getValue());
			battery.setStatus(Battery.Status.NOT_USE.getValue());
			battery.setCreateTime(new Date());
			battery.setStayHeartbeat(Constant.STAY_HEARTBEAT);
			battery.setMoveHeartbeat(Constant.MOVE_HEARTBEAT);
			battery.setElectrifyHeartbeat(Constant.ELECTRIFY_HEARTBEAT);
			battery.setOrderDistance(0);
			battery.setTotalDistance(0L);
			battery.setIsReportVoltage(ConstEnum.Flag.FALSE.getValue());
			battery.setCode(IMEI);
			battery.setQrcode(qrcode);
			battery.setShellCode(shellCode);
			battery.setChargeStatus(Battery.Status.NOT_USE.getValue());
			battery.setIsOnline(ConstEnum.Flag.FALSE.getValue());
			battery.setChargeCompleteVolume(95);
			battery.setGpsSwitch(ConstEnum.Flag.FALSE.getValue());
			battery.setLockSwitch(ConstEnum.Flag.FALSE.getValue());
			battery.setGprsShutdown(ConstEnum.Flag.FALSE.getValue());
			battery.setShutdownVoltage(42000);
			battery.setAcceleretedSpeed(1);

			try {
				int total = 0;
				Battery entity = batteryMapper.find(batteryId);
				Battery result = batteryMapper.findByCode(IMEI);
				if (entity != null && entity.getCode().equals(battery.getCode())) {
					total = batteryMapper.update(battery);
				} else if (entity != null && !(entity.getCode().equals(battery.getCode()))) {
					log.debug("第{}条数据导入失败, 电池编号存在，但IMEI不同", row);
					failBuilder.append(row + ",");
					failCount++;
					continue;
				} else if (entity == null && result != null) {
					log.debug("第{}条数据导入失败, IMEI已存在", row);
					failBuilder.append(row + ",");
					failCount++;
					continue;
				} else {
					total = batteryMapper.insert(battery);
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
		if (StringUtils.isNotEmpty(failBuilder.toString())) {
			failBuilder.append("失败" + failCount + "条包括行数(" + failBuilder.toString() + "),表头不计行数!");
		}
		return DataResult.successResult("总条数" + rows + "条," + "成功导入" + successCount + "条!" + failBuilder.toString());
	}

	private static int parseInt(String text, Integer defaultVal) {
		try {
			return Integer.parseInt(text);
		} catch (Exception e) {
			return defaultVal;
		}
	}

	public BatteryStatis findCount(int agentId, Integer chargeStatus) {
		List<Integer> statusList = new ArrayList<Integer>();
		statusList.add(Battery.Status.NOT_USE.getValue());
		statusList.add(Battery.Status.KEEPER_OUT.getValue());

		//未完成充电数量
		int chargingCount = batteryMapper.findChargingCount(agentId, statusList, chargeStatus);
		//满电数
		int fullCount = batteryMapper.findFullCount(agentId, statusList, chargeStatus);
		//未使用电池数量
		int waitChargeCount = batteryMapper.findWaitChargeCount(agentId, statusList, chargeStatus);
		//工厂总电池数
		int factoryBatteryCount = batteryMapper.findFactoryBatteryCount(agentId, statusList, chargeStatus);

		BatteryStatis batteryStatis = new BatteryStatis();
		batteryStatis.setChargingCount(chargingCount);
		batteryStatis.setFullCount(fullCount);
		batteryStatis.setWaitChargeCount(waitChargeCount);
		batteryStatis.setFactoryBatteryCount(factoryBatteryCount);

		return batteryStatis;
	}

	public List<Battery> findByAgent(Integer agentId, String id, Integer monitorStatus) {
		List<Integer> statuss = new ArrayList<Integer>();
		Integer chargeStatus = null, chargeCompleteFlag = null;
		final int NOT_USE = 1;
		final int CUSTOMER_OUT = 2;
		final int CHARGING = 3;
		final int COMPLETE = 4;
		if (monitorStatus == NOT_USE) {
			statuss.add(Battery.Status.NOT_USE.getValue());
			statuss.add(Battery.Status.IN_BOX.getValue());
			statuss.add(Battery.Status.KEEPER_OUT.getValue());
		} else if (monitorStatus == CUSTOMER_OUT) {
			statuss.add(Battery.Status.CUSTOMER_OUT.getValue());
		} else if (monitorStatus == CHARGING) {
			chargeStatus = Battery.ChargeStatus.CHARGING.getValue();
		} else if (monitorStatus == COMPLETE) {
			chargeCompleteFlag = ConstEnum.Flag.TRUE.getValue();
			chargeStatus = Battery.ChargeStatus.CHARGING.getValue();
		}
		List<Battery> batterys = batteryMapper.findByAgent(agentId, id, chargeStatus, statuss, chargeCompleteFlag);
		for (Battery battery : batterys) {
			if (battery.getStatus() == Battery.Status.CUSTOMER_OUT.getValue()) {
				battery.setMonitorStatus(CUSTOMER_OUT);
				battery.setMonitorStatusName("骑行中");
			} else if (battery.getChargeStatus() == Battery.ChargeStatus.CHARGING.getValue()) {
				if (battery.getVolume() >= battery.getChargeCompleteVolume()) {
					battery.setMonitorStatus(COMPLETE);
					battery.setMonitorStatusName(battery.getStatusName() + "(已充满)");
				} else {
					battery.setMonitorStatus(CHARGING);
					battery.setMonitorStatusName(battery.getStatusName() + "(充电中)");
				}
			} else {
				battery.setMonitorStatus(NOT_USE);
				battery.setMonitorStatusName(battery.getStatusName());
			}
		}
		return batterys;
	}

	@Transactional(rollbackFor = Throwable.class)
	public ExtResult boundCard(Battery entity) {
		if (StringUtils.isEmpty(entity.getQrcode())) {
			return ExtResult.failResult("二维码不能为空");
		}

		int result = batteryMapper.findQrcode(entity.getQrcode(), null);
		if (result != 0) {
			return ExtResult.failResult("二维码已存在");
		}

		String agentId = systemConfigMapper.findConfigValue(ConstEnum.SystemConfigKey.TEST_AGENT.getValue());

		entity.setAgentId(Integer.parseInt(agentId));
		entity.setOrderDistance(0);
		entity.setTotalDistance(0L);
		entity.setExchangeAmount(0);
		entity.setVolume(0);
		entity.setUseCount(0);
		entity.setIsOnline(ConstEnum.Flag.FALSE.getValue());
		entity.setStatus(Battery.Status.NOT_USE.getValue());
		entity.setChargeStatus(Battery.ChargeStatus.NOT_CHARGE.getValue());
		entity.setCreateTime(new Date());
		entity.setStayHeartbeat(Constant.STAY_HEARTBEAT);
		entity.setMoveHeartbeat(Constant.MOVE_HEARTBEAT);
		entity.setElectrifyHeartbeat(Constant.ELECTRIFY_HEARTBEAT);
		entity.setType(2);
		entity.setIsActive(ConstEnum.Flag.TRUE.getValue());
		entity.setIsReportVoltage(ConstEnum.Flag.FALSE.getValue());
		entity.setChargeCompleteVolume(95);
		entity.setGpsSwitch(ConstEnum.Flag.FALSE.getValue());
		entity.setLockSwitch(ConstEnum.Flag.FALSE.getValue());
		entity.setGprsShutdown(ConstEnum.Flag.FALSE.getValue());
		entity.setShutdownVoltage(42000);
		entity.setAcceleretedSpeed(1);
		entity.setRepairStatus(Battery.RepairStatus.NOT.getValue());
		entity.setCategory(Battery.Category.EXCHANGE.getValue());

		if (batteryMapper.insert(entity) >= 1) {
			unregisterBatteryMapper.delete(entity.getCode());
			return ExtResult.successResult();
		} else {
			return ExtResult.failResult("创建失败");
		}
	}

	public ExtResult updateFullVolume(String[] batteryIds, Integer chargeCompleteVolume) {
		for (int i = 0; i < batteryIds.length; i++) {
			batteryMapper.updateFullVolume(batteryIds[i], chargeCompleteVolume);
		}
		return ExtResult.successResult();
	}

	public File exportExcel(Battery search, Integer[] columns) {
		File file = new File(appConfig.tempDir, IdUtils.uuid());
		YhdgUtils.makeParentDir(file);
		FileOutputStream outputStream = null;
		try {
			outputStream = new FileOutputStream(file);
			WritableWorkbook wwb;
			wwb = Workbook.createWorkbook(outputStream);
			WritableSheet sheet = wwb.createSheet("电池信息", 0);
			sheet.getSettings().setDefaultColumnWidth(columns.length - 2);
			sheet.setRowView(0, 400, false);
			/**
			 * 设置题头
			 */
			sheet.addCell(new Label(0, 0,"电池信息", getTitleStyle()));
			sheet.mergeCells(0, 0, columns.length-1, 0);
			for (int i = 0; i < columns.length; i++) {
				sheet.addCell(new Label(i, 1,Battery.BatteryColumn.getName(columns[i]), getHeadStyle()));
			}

			int offset = 0, limit = 1000;
			while (true) {
				search.setBeginIndex(offset);
				search.setRows(limit);
				List<Battery> list = findForExcel(search);
				if (list.isEmpty()) {
					break;
				}
				ExportXlsUtils.writeBatteryBycolumns(offset, list, columns, sheet);
				offset += limit;
			}
			// 写入数据
			wwb.write();
			// 关闭文件
			wwb.close();
		} catch (Exception e) {
			log.error("upload_excel error", e);
		} finally {
			IOUtils.closeQuietly(outputStream);
		}

		return file;
	}

    public Page findByAbnormalAllPage(Battery search) {
        Map<String, String> batteryBrandMap = findDictItemMap(DictCategory.CategoryType.BATTERY_BRAND.getValue());

        Page page = search.buildPage();
        search.setIsNormal(0);//异常电池
        page.setTotalItems(batteryMapper.findPageCount(search));
        search.setBeginIndex(page.getOffset());

        List<Battery> batteryList = batteryMapper.findPageResult(search);
        for (Battery battery : batteryList) {
            if (battery != null) {
                if (battery.getAgentId() != null) {
                    battery.setAgentName(findAgentInfo(battery.getAgentId()).getAgentName());
                }
                if (battery.getType() != null) {
                    String type = findBatteryType(battery.getType()).getTypeName();
                    battery.setBatteryType(type);
                }
                if (battery.getBrand() != null) {
                    battery.setBrandName(batteryBrandMap.get(battery.getBrand()));
                }
                if (StringUtils.isNotEmpty(battery.getCabinetId())) {
                    Cabinet cabinet = cabinetMapper.find(battery.getCabinetId());
                    battery.setCabinetName(cabinet.getCabinetName());
                }
            }
        }
        page.setResult(batteryList);
        return page;
    }


}
