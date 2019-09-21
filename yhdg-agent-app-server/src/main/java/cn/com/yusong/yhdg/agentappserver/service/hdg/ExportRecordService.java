package cn.com.yusong.yhdg.agentappserver.service.hdg;

import cn.com.yusong.yhdg.agentappserver.constant.RespCode;
import cn.com.yusong.yhdg.agentappserver.entity.result.RestResult;
import cn.com.yusong.yhdg.agentappserver.persistence.basic.AgentMapper;
import cn.com.yusong.yhdg.agentappserver.persistence.hdg.BatteryMapper;
import cn.com.yusong.yhdg.agentappserver.persistence.hdg.CabinetBoxMapper;
import cn.com.yusong.yhdg.agentappserver.persistence.hdg.CabinetMapper;
import cn.com.yusong.yhdg.agentappserver.persistence.hdg.ExportRecordMapper;
import cn.com.yusong.yhdg.agentappserver.service.AbstractService;
import cn.com.yusong.yhdg.common.constant.ConstEnum;
import cn.com.yusong.yhdg.common.constant.Constant;
import cn.com.yusong.yhdg.common.domain.basic.Agent;
import cn.com.yusong.yhdg.common.domain.hdg.Battery;
import cn.com.yusong.yhdg.common.domain.hdg.Cabinet;
import cn.com.yusong.yhdg.common.domain.hdg.CabinetBox;
import cn.com.yusong.yhdg.common.domain.hdg.ExportRecord;
import cn.com.yusong.yhdg.common.entity.NotNullMap;
import cn.com.yusong.yhdg.common.entity.json.DataResult;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sun.management.resources.agent;

import java.text.ParseException;
import java.util.*;


@Service
public class ExportRecordService extends AbstractService {
    @Autowired
    AgentMapper agentMapper;
    @Autowired
    BatteryMapper batteryMapper;
    @Autowired
    CabinetMapper cabinetMapper;
    @Autowired
    ExportRecordMapper exportRecordMapper;

    public RestResult sendToAgent(Integer agentId, List<Map> list, Integer personId, String operator) {
        Agent agent = agentMapper.find(agentId);
        if (agent == null) {
            return RestResult.result(RespCode.CODE_2.getValue(), "运营商不存在");
        }
        Date date = new Date();
        //成功条数
        int successCount = 0;
        int failCount = 0;
        StringBuilder failBuilder = new StringBuilder("");
        for (Map map : list) {
            Integer exportType = (Integer) map.get("exportType");
            String shellCode = (String) map.get("shellCode");
            String cabinetId = (String) map.get("cabinetId");

            if (exportType == ExportRecord.ExportType.BATTERY.getValue()) {
                if (StringUtils.isEmpty(shellCode) || (StringUtils.isNotEmpty(cabinetId))) {
                    failBuilder.append(list.indexOf(map) + 1 + ",");
                    failCount++;
                    continue;
                } else {
                    //发过来的是外壳编号
                    Battery battery = batteryMapper.findByShellCode(shellCode);
                    if (battery == null) {
                        //发过来的是IMEI
                        battery= batteryMapper.findByImei(shellCode);
                    }
                    if (battery == null) {
                        failBuilder.append(list.indexOf(map) + 1 + ",");
                        failCount++;
                        continue;
                    } else {
                        //如果电池的最新发货运营商与该运营商一致，不再计入发货
                        ExportRecord dbExportRecord = exportRecordMapper.findLastByBattery(shellCode);
                        if (dbExportRecord != null && dbExportRecord.getAgentId().intValue() == agentId) {
                            failBuilder.append(list.indexOf(map) + 1 + ",");
                            failCount++;
                            continue;
                        } else {
                            ExportRecord newExportRecord = new ExportRecord();
                            newExportRecord.setAgentId(agentId);
                            newExportRecord.setAgentName(agent.getAgentName());
                            newExportRecord.setAgentCode(agent.getAgentCode());
                            newExportRecord.setBatteryId(battery.getId());
                            newExportRecord.setCode(battery.getCode());
                            newExportRecord.setShellCode(battery.getShellCode());
                            newExportRecord.setPersonId(personId);
                            newExportRecord.setOperator(operator);
                            newExportRecord.setCreateTime(date);
                            exportRecordMapper.insert(newExportRecord);
                            successCount++;
                        }
                    }
                }
            } else if (exportType == ExportRecord.ExportType.CABINET.getValue()) {
                if (StringUtils.isEmpty(cabinetId) || StringUtils.isNotEmpty(shellCode)) {
                    failBuilder.append(list.indexOf(map) + 1 + ",");
                    failCount++;
                    continue;
                } else {
                    Cabinet cabinet = cabinetMapper.find(cabinetId);
                    if (cabinet == null) {
                        failBuilder.append(list.indexOf(map) + 1 + ",");
                        failCount++;
                        continue;
                    } else {
                        //如果柜子的最新发货运营商与该运营商一致，不再计入发货
                        ExportRecord dbExportRecord = exportRecordMapper.findLastByCabinet(cabinetId);
                        if (dbExportRecord != null && dbExportRecord.getAgentId().intValue() == agentId) {
                            failBuilder.append(list.indexOf(map) + 1 + ",");
                            failCount++;
                            continue;
                        } else {
                            ExportRecord newExportRecord = new ExportRecord();
                            newExportRecord.setAgentId(agentId);
                            newExportRecord.setAgentName(agent.getAgentName());
                            newExportRecord.setAgentCode(agent.getAgentCode());
                            newExportRecord.setCabinetId(cabinet.getId());
                            newExportRecord.setCabinetName(cabinet.getCabinetName());
                            newExportRecord.setPersonId(personId);
                            newExportRecord.setOperator(operator);
                            newExportRecord.setCreateTime(date);
                            exportRecordMapper.insert(newExportRecord);
                            successCount++;
                        }
                    }
                }
            } else {
                failBuilder.append(list.indexOf(map) + 1 + ",");
                failCount++;
                continue;
            }
        }
        if (StringUtils.isNotEmpty(failBuilder.toString())) {
            failBuilder.append("失败" + failCount + "条包括行数(" + failBuilder.toString() + "),表头不计行数!");
        }
        return RestResult.result(RespCode.CODE_0.getValue(), "总条数" + list.size() + "条," + "成功导入" + successCount + "条!" + failBuilder.toString());
    }

    public void findFirstInfo(NotNullMap data, Long personId) {
        List<ExportRecord> exportRecordList = exportRecordMapper.findByPersonId(personId);
        List<ExportRecord> totalBatteryRecordList = new ArrayList<ExportRecord>();
        List<ExportRecord> totalCabinetRecordList = new ArrayList<ExportRecord>();
        List<ExportRecord> todayBatteryRecordList = new ArrayList<ExportRecord>();
        List<ExportRecord> todayCabinetRecordList = new ArrayList<ExportRecord>();
        //今日数据
        Date date = new Date();
        //2019-01-19 00:00:00
        Date beginTime = DateUtils.truncate(date, Calendar.DAY_OF_MONTH);
        //2019-01-19 23:59:59
        Date endTime = DateUtils.addMilliseconds(DateUtils.addDays(beginTime, 1), -1);
        for (ExportRecord exportRecord : exportRecordList) {
            if (StringUtils.isNotEmpty(exportRecord.getBatteryId())) {
                totalBatteryRecordList.add(exportRecord);
                if (exportRecord.getCreateTime().getTime() >= beginTime.getTime() && exportRecord.getCreateTime().getTime() < endTime.getTime()) {
                    todayBatteryRecordList.add(exportRecord);
                }
            }
            if (StringUtils.isNotEmpty(exportRecord.getCabinetId())) {
                totalCabinetRecordList.add(exportRecord);
                if (exportRecord.getCreateTime().getTime() >= beginTime.getTime() && exportRecord.getCreateTime().getTime() < endTime.getTime()) {
                    todayCabinetRecordList.add(exportRecord);
                }
            }
        }
        data.put("totalBatteryCount", totalBatteryRecordList.size());
        data.put("totalCabinetCount", totalCabinetRecordList.size());
        data.put("todayBatteryCount", todayBatteryRecordList.size());
        data.put("todayCabinetCount", todayCabinetRecordList.size());
    }


    public void findFirstListInfo(List<Map> list, Long personId, Integer exportType, int offset, int limit) {
        List<ExportRecord> exportRecordList = exportRecordMapper.findList(personId, exportType, offset, limit);
        for (ExportRecord exportRecord : exportRecordList) {
            NotNullMap map = new NotNullMap();
            if (exportType == ExportRecord.ExportType.BATTERY.getValue()) {
                map.put("id", exportRecord.getId());
                map.put("batteryId", exportRecord.getBatteryId());
                map.put("shellCode", exportRecord.getShellCode());
                map.put("code", exportRecord.getCode());
                map.put("agentName", exportRecord.getAgentName());
                map.putDateTime("createTime", exportRecord.getCreateTime());
                list.add(map);
            } else if (exportType == ExportRecord.ExportType.CABINET.getValue()) {
                map.put("id", exportRecord.getId());
                map.put("cabinetId", exportRecord.getCabinetId());
                map.put("cabinetName", exportRecord.getCabinetName());
                map.put("agentName", exportRecord.getAgentName());
                map.putDateTime("createTime", exportRecord.getCreateTime());
                list.add(map);
            }
        }
    }

    public ExportRecord find(Integer id) {
        return exportRecordMapper.find(id);
    }

    public void delete(int id) {
        exportRecordMapper.delete(id);
    }

}
