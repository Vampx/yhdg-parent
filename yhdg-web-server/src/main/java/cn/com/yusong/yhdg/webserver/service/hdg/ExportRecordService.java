package cn.com.yusong.yhdg.webserver.service.hdg;

import cn.com.yusong.yhdg.common.domain.basic.Agent;
import cn.com.yusong.yhdg.common.domain.hdg.*;
import cn.com.yusong.yhdg.common.entity.json.DataResult;
import cn.com.yusong.yhdg.common.entity.json.ExtResult;
import cn.com.yusong.yhdg.common.entity.pagination.Page;
import cn.com.yusong.yhdg.webserver.persistence.basic.AgentMapper;
import cn.com.yusong.yhdg.webserver.persistence.hdg.*;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class ExportRecordService extends AbstractService {
	static Logger log = LoggerFactory.getLogger(ExportRecordService.class);
	@Autowired
	ExportRecordMapper exportRecordMapper;
	@Autowired
	BatteryMapper batteryMapper;
	@Autowired
	CabinetMapper cabinetMapper;
	@Autowired
	AgentMapper agentMapper;

	public Page findPage(ExportRecord search) {
		Page page = search.buildPage();
		page.setTotalItems(exportRecordMapper.findPageCount(search));
		search.setBeginIndex(page.getOffset());
		List<ExportRecord> batteryExportList = exportRecordMapper.findPageResult(search);
		page.setResult(batteryExportList);
		return page;
	}

	public ExportRecord find(Integer id) {
		return exportRecordMapper.find(id);
	}

	@Transactional(rollbackFor = Throwable.class)
	public ExtResult batchImportExport(File mFile, ExportRecord exportRecord, String operator) {
		Agent agent = agentMapper.find(exportRecord.getAgentId());
		if (agent == null) {
			return ExtResult.failResult("请选择运营商");
		}
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
		Date date = new Date();
		for (int row = 1; row <= rows; row++) {
			//获取发货类型
			String exportTypeString = sheet.getCell(0, row).getContents().trim();
			if (!StringUtils.isNotEmpty(exportTypeString)) {
				failBuilder.append(row + ",");
				failCount++;
				continue;
			}
			Integer exportType = Integer.parseInt(exportTypeString);
			String shellCode = sheet.getCell(1, row).getContents().trim();
			String cabinetId = sheet.getCell(2, row).getContents().trim();

			if (exportType == ExportRecord.ExportType.BATTERY.getValue()) {
				if (StringUtils.isEmpty(shellCode) || StringUtils.isNotEmpty(cabinetId)) {
					failBuilder.append(row + ",");
					failCount++;
					continue;
				} else {
					Battery battery = batteryMapper.findByShellCode(shellCode);
					if (battery == null) {
						failBuilder.append(row + ",");
						failCount++;
						continue;
					} else {
						//如果电池的最新发货运营商与该运营商一致，不再计入发货
						ExportRecord dbExportRecord = exportRecordMapper.findLastByBattery(shellCode);
						if (dbExportRecord != null && dbExportRecord.getAgentId().intValue() == exportRecord.getAgentId()) {
							failBuilder.append(row + ",");
							failCount++;
							continue;
						} else {
							ExportRecord newExportRecord = new ExportRecord();
							newExportRecord.setAgentId(exportRecord.getAgentId());
							newExportRecord.setAgentName(agent.getAgentName());
							newExportRecord.setAgentCode(agent.getAgentCode());
							newExportRecord.setBatteryId(battery.getId());
							newExportRecord.setCode(battery.getCode());
							newExportRecord.setShellCode(battery.getShellCode());
							newExportRecord.setOperator(operator);
							newExportRecord.setCreateTime(date);
							exportRecordMapper.insert(newExportRecord);
							successCount++;
						}
					}
				}
			} else if (exportType == ExportRecord.ExportType.CABINET.getValue()) {
				if (StringUtils.isEmpty(cabinetId) || StringUtils.isNotEmpty(shellCode)) {
					failBuilder.append(row + ",");
					failCount++;
					continue;
				} else {
					Cabinet cabinet = cabinetMapper.find(cabinetId);
					if (cabinet == null) {
						failBuilder.append(row + ",");
						failCount++;
						continue;
					} else {
						//如果柜子的最新发货运营商与该运营商一致，不再计入发货
						ExportRecord dbExportRecord = exportRecordMapper.findLastByCabinet(cabinetId);
						if (dbExportRecord != null && dbExportRecord.getAgentId().intValue() == exportRecord.getAgentId()) {
							failBuilder.append(row + ",");
							failCount++;
							continue;
						} else {
							ExportRecord newExportRecord = new ExportRecord();
							newExportRecord.setAgentId(exportRecord.getAgentId());
							newExportRecord.setAgentName(agent.getAgentName());
							newExportRecord.setAgentCode(agent.getAgentCode());
							newExportRecord.setCabinetId(cabinet.getId());
							newExportRecord.setCabinetName(cabinet.getCabinetName());
							newExportRecord.setOperator(operator);
							newExportRecord.setCreateTime(date);
							exportRecordMapper.insert(newExportRecord);
							successCount++;
						}
					}
				}
			} else {
				failBuilder.append(row + ",");
				failCount++;
				continue;
			}
		}
		if (StringUtils.isNotEmpty(failBuilder.toString())) {
			failBuilder.append("失败" + failCount + "条包括行数(" + failBuilder.toString() + "),表头不计行数!");
		}
		return DataResult.successResult("总条数" + (rows) + "条," + "成功导入" + successCount + "条!" + failBuilder.toString());
	}

	@Transactional(rollbackFor = Throwable.class)
	public ExtResult delete(Integer id) {
		if (exportRecordMapper.delete(id) >= 1) {
			return ExtResult.successResult();
		} else {
			return ExtResult.failResult("删除失败");
		}
	}

}
