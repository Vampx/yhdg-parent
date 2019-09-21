package cn.com.yusong.yhdg.webserver.service.hdg;

import cn.com.yusong.yhdg.common.domain.hdg.*;
import cn.com.yusong.yhdg.common.entity.json.DataResult;
import cn.com.yusong.yhdg.common.entity.json.ExtResult;
import cn.com.yusong.yhdg.common.entity.json.PageResult;
import cn.com.yusong.yhdg.common.entity.pagination.Page;
import cn.com.yusong.yhdg.webserver.persistence.hdg.*;
import cn.com.yusong.yhdg.webserver.service.AbstractService;
import jxl.Sheet;
import jxl.Workbook;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class BatteryCellService extends AbstractService {
	@Autowired
	BatteryCellMapper batteryCellMapper;
	@Autowired
	BatteryFormatMapper batteryFormatMapper;
	@Autowired
	BatteryMapper batteryMapper;
	@Autowired
	UnregisterBatteryMapper unregisterBatteryMapper;
	@Autowired
	BatteryCellBarcodeMapper batteryCellBarcodeMapper;
	@Autowired
	BatteryCellFormatMapper batteryCellFormatMapper;

	public BatteryCell find(Long id) {
		return batteryCellMapper.find(id);
	}

	public ExtResult findByBarcode(String barcode) {
		BatteryCell batteryCell = batteryCellMapper.findByBarcode(barcode);
		if (batteryCell == null) {
			return ExtResult.failResult("该电芯条码还未通过检验");
		}
		return DataResult.successResult(batteryCell);
	}

	public Page findPage(BatteryCell search) {
		Page page = search.buildPage();
		page.setTotalItems(batteryCellMapper.findPageCount(search));
		search.setBeginIndex(page.getOffset());
		List<BatteryCell> batteryCellList = batteryCellMapper.findPageResult(search);
		page.setResult(batteryCellList);
		return page;
	}

	public Page findBoundPage(BatteryCell search) {
		if (search.getBatteryId().isEmpty()) {
			Page page = search.buildPage();
			page.setTotalItems(batteryCellMapper.findEmptyPageCount(search));
			search.setBeginIndex(page.getOffset());
			page.setResult(batteryCellMapper.findEmptyPageResult(search));
			return page;
		}
		Page page = search.buildPage();
		page.setTotalItems(batteryCellMapper.findPageCount(search));
		search.setBeginIndex(page.getOffset());
		List<BatteryCell> batteryCellList = batteryCellMapper.findPageResult(search);
		page.setResult(batteryCellList);
		return page;
	}

	@Transactional(rollbackFor = Throwable.class)
	public ExtResult create(BatteryCell batteryCell) {
		if (batteryCellMapper.findByBarcode(batteryCell.getBarcode()) != null) {
			return ExtResult.failResult("该电芯条码已存在，新建失败");
		}
		batteryCell.setCreateTime(new Date());
		if (batteryCellMapper.insert(batteryCell) >= 1) {
			return ExtResult.successResult();
		} else {
			return ExtResult.failResult("新建失败");
		}
	}

	@Transactional(rollbackFor = Throwable.class)
	public ExtResult batchImportBatteryCell(File mFile, String operator) {
		Workbook workbook = null;
		try {
			workbook = Workbook.getWorkbook(mFile);
		} catch (Exception e) {
			return ExtResult.failResult("操作失败");
		}
		Sheet sheet = workbook.getSheet(0);
		//获取总条数
		int rows = sheet.getRows() - 1;
		int failCount = 0;
		StringBuilder failBuilder = new StringBuilder("");
		List<BatteryCell> batteryCellList = new ArrayList<BatteryCell>();
		for (int row = 1; row <= rows; row++) {
			//获取条码编号
			String barcode = sheet.getCell(0, row).getContents().trim();
			if (!StringUtils.isNotEmpty(barcode)) {
				failBuilder.append(row + ",");
				failCount++;
				continue;
			}
			if (batteryCellMapper.findByBarcode(barcode) != null) {
				failBuilder.append(row + ",");
				failCount++;
				continue;
			}
			//查询电芯条码表
			BatteryCellBarcode batteryCellBarcode = batteryCellBarcodeMapper.findByBarcode(barcode);
			BatteryCellFormat batteryCellFormat = null;
			if (batteryCellBarcode != null) {
				batteryCellFormat = batteryCellFormatMapper.find(batteryCellBarcode.getCellFormatId());
			}
			//查询电芯规格表
			if (batteryCellFormat == null) {
				failBuilder.append(row + ",");
				failCount++;
				continue;
			}
			BatteryCell batteryCell = new BatteryCell();
			batteryCell.setBarcode(barcode);
			batteryCell.setCellModel(batteryCellFormat.getCellModel());
			batteryCell.setCellMfr(batteryCellFormat.getCellMfr());
			batteryCell.setOperator(operator);
			batteryCell.setCreateTime(new Date());
			//获取组包容量29.790
			double nominalCap = (Double.parseDouble(sheet.getCell(1, row).getContents().trim()) * 1000);
			//交流内阻0.980
			double acResistance = (Double.parseDouble(sheet.getCell(2, row).getContents().trim()));
			//回弹电压3.443
			double resilienceVol = (Double.parseDouble(sheet.getCell(3, row).getContents().trim()) * 1000);
			//静置电压4.149
			double staticVol = (Double.parseDouble(sheet.getCell(4, row).getContents().trim()) * 1000);
			//循环次数0
			Integer circle = (Integer.parseInt(sheet.getCell(5, row).getContents().trim()));

			int result1 = (nominalCap >= batteryCellFormat.getNominalCap() * 1000 - batteryCellFormat.getMinNominalCap() * 1000 &&
					nominalCap <= batteryCellFormat.getNominalCap() * 1000 + batteryCellFormat.getMaxNominalCap() * 1000) ? 1 : 0;

			int result2 = (acResistance >= batteryCellFormat.getAcResistance() - batteryCellFormat.getMinAcResistance() &&
					acResistance <= batteryCellFormat.getAcResistance() + batteryCellFormat.getMaxAcResistance()) ? 1 : 0;

			int result3 = (resilienceVol >= batteryCellFormat.getResilienceVol() * 1000 - batteryCellFormat.getMinResilienceVol() * 1000 &&
					resilienceVol <= batteryCellFormat.getResilienceVol() * 1000 + batteryCellFormat.getMaxResilienceVol() * 1000) ? 1 : 0;

			int result4 = (staticVol >= batteryCellFormat.getStaticVol() * 1000 - batteryCellFormat.getMinStaticVol() * 1000 &&
					staticVol <= batteryCellFormat.getStaticVol() * 1000 + batteryCellFormat.getMaxStaticVol() * 1000) ? 1 : 0;

			int result5 = (circle >= batteryCellFormat.getCircle() - batteryCellFormat.getMinCircle() &&
					circle <= batteryCellFormat.getCircle() + batteryCellFormat.getMaxCircle()) ? 1 : 0;

			if (result1 + result2 + result3 + result4 + result5 != 5) {
				failBuilder.append(row + ",");
				failCount++;
				continue;
			}
			batteryCell.setNominalCap((int) (nominalCap));
			batteryCell.setAcResistance(acResistance);
			batteryCell.setResilienceVol((int) (resilienceVol));
			batteryCell.setStaticVol((int) (staticVol));
			batteryCell.setCircle(circle);
			batteryCellList.add(batteryCell);
		}
		if (StringUtils.isNotEmpty(failBuilder.toString())) {
			failBuilder.append("失败" + failCount + "条包括行数(" + failBuilder.toString() + "),表头不计行数!");
		}
		//成功条数
		int successCount = 0;
		if (failCount == 0) {
			//成功条数
			successCount = importBatteryCellList(batteryCellList);
		}
		return DataResult.successResult("总条数" + rows + "条," + "成功导入" + successCount + "条!" + failBuilder.toString());
	}

	@Transactional(rollbackFor = Throwable.class)
	public int importBatteryCellList(List<BatteryCell> batteryCellList) {
		//成功条数
		int successCount = 0;
		for (BatteryCell batteryCell : batteryCellList) {
			int total = batteryCellMapper.insert(batteryCell);
			if (total > 0) {
				successCount++;
			}
		}
		return successCount;
	}

	@Transactional(rollbackFor = Throwable.class)
	public ExtResult update(BatteryCell batteryCell) {
		if (batteryCellMapper.update(batteryCell) >= 1) {
			return ExtResult.successResult();
		} else {
			return ExtResult.failResult("修改失败");
		}
	}

	@Transactional(rollbackFor = Throwable.class)
	public ExtResult unbind(Long id, String batteryId) {
		UnregisterBattery unregisterBattery = unregisterBatteryMapper.find(batteryId);
		unregisterBatteryMapper.updateCellCount(batteryId, unregisterBattery.getCellCount().intValue() - 1);
		if (batteryCellMapper.unbind(id) >= 1) {
			return ExtResult.successResult();
		} else {
			return ExtResult.failResult("解绑失败");
		}
	}

	@Transactional(rollbackFor = Throwable.class)
	public ExtResult bindBattery(Long id, String batteryId, Integer formatId) {
		BatteryCell batteryCell = batteryCellMapper.find(id);
		if (batteryCell.getBatteryId() != null) {
			return ExtResult.failResult("该电芯已绑定电池");
		}
		BatteryFormat batteryFormat = batteryFormatMapper.find(formatId);
		UnregisterBattery unregisterBattery = unregisterBatteryMapper.find(batteryId);
		if (unregisterBattery.getCellCount() != null && unregisterBattery.getCellCount().intValue() >= batteryFormat.getCellCount().intValue()) {
			return ExtResult.failResult("电池绑定电芯条数已达上限");
		} else {
			unregisterBatteryMapper.updateCellCount(batteryId, unregisterBattery.getCellCount() + 1);
		}
		if (batteryCellMapper.bindBattery(id, batteryId) >= 1) {
			return DataResult.successResult(unregisterBattery);
		} else {
			return ExtResult.failResult("绑定失败");
		}
	}


}
