package cn.com.yusong.yhdg.agentserver.service.hdg;

import cn.com.yusong.yhdg.common.domain.hdg.*;
import cn.com.yusong.yhdg.common.domain.hdg.BatteryFormat;
import cn.com.yusong.yhdg.common.entity.json.DataResult;
import cn.com.yusong.yhdg.common.entity.json.ExtResult;
import cn.com.yusong.yhdg.common.entity.json.PageResult;
import cn.com.yusong.yhdg.common.entity.pagination.Page;
import cn.com.yusong.yhdg.agentserver.persistence.hdg.BatteryCellMapper;
import cn.com.yusong.yhdg.agentserver.persistence.hdg.BatteryFormatMapper;
import cn.com.yusong.yhdg.agentserver.persistence.hdg.BatteryMapper;
import cn.com.yusong.yhdg.agentserver.persistence.hdg.UnregisterBatteryMapper;
import cn.com.yusong.yhdg.agentserver.service.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
