package cn.com.yusong.yhdg.serviceserver.service.basic;

import cn.com.yusong.yhdg.common.constant.ConstEnum;
import cn.com.yusong.yhdg.common.constant.Constant;
import cn.com.yusong.yhdg.common.domain.basic.Agent;
import cn.com.yusong.yhdg.common.domain.basic.AgentInfo;
import cn.com.yusong.yhdg.common.domain.basic.Customer;
import cn.com.yusong.yhdg.common.domain.basic.IdCardAuthRecord;
import cn.com.yusong.yhdg.common.domain.hdg.*;
import cn.com.yusong.yhdg.common.utils.YhdgUtils;
import cn.com.yusong.yhdg.serviceserver.persistence.basic.AgentMapper;
import cn.com.yusong.yhdg.serviceserver.persistence.basic.CustomerMapper;
import cn.com.yusong.yhdg.serviceserver.persistence.basic.IdCardAuthRecordMapper;
import cn.com.yusong.yhdg.serviceserver.persistence.hdg.*;
import cn.com.yusong.yhdg.serviceserver.service.AbstractService;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class AgentPayService extends AbstractService{
    @Autowired
    AgentMapper agentMapper;
    @Autowired
    CustomerMapper customerMapper;
    @Autowired
    BatteryMapper batteryMapper;
    @Autowired
    CabinetMapper cabinetMapper;
    @Autowired
    AgentMaterialDayStatsMapper agentMaterialDayStatsMapper;
    @Autowired
    AgentCabinetForegiftRecordMapper agentCabinetForegiftRecordMapper;
    @Autowired
    AgentCabinetRentRecordMapper agentCabinetRentRecordMapper;
    @Autowired
    AgentBatteryRentRecordMapper agentBatteryRentRecordMapper;
    @Autowired
    IdCardAuthRecordMapper idCardAuthRecordMapper;

    Map<Integer, Agent> exchangeAgentMap = null;
    Map<Integer, Agent> rentAgentMap = null;

    /**
     * 运营商支出
     * 1、柜子押金 2、柜子租金 3、电池租金 4、认证费用
     */
    public void stats(Date date) {
        exchangeAgentMap = new HashMap<Integer, Agent>();
        rentAgentMap = new HashMap<Integer, Agent>();


        Map<Integer, List<AgentCabinetForegiftRecord>> agentCabinetForegiftRecordMap = new HashMap<Integer, List<AgentCabinetForegiftRecord>>();
        Map<Integer, List<AgentCabinetRentRecord>> agentCabinetRentRecordMap = new HashMap<Integer, List<AgentCabinetRentRecord>>();
        Map<Integer, List<IdCardAuthRecord>> idCardAuthRecorddMap = new HashMap<Integer, List<IdCardAuthRecord>>();
        Map<Integer, List<AgentBatteryRentRecord>> agentExchangeBatteryRentRecordMap = new HashMap<Integer, List<AgentBatteryRentRecord>>();
        Map<Integer, AgentMaterialDayStats> agentExchangeMaterialDayStatsMap = new HashMap<Integer, AgentMaterialDayStats>();

        Map<Integer, List<AgentBatteryRentRecord>> agentRentBatteryRentRecordMap = new HashMap<Integer, List<AgentBatteryRentRecord>>();
        Map<Integer, AgentMaterialDayStats> agentRentMaterialDayStatsMap = new HashMap<Integer, AgentMaterialDayStats>();

        String statsDate = DateFormatUtils.format(date.getTime(), Constant.DATE_FORMAT);
        Date statsTime = DateUtils.truncate(date, Calendar.DAY_OF_MONTH);
        Date statsEndTime = DateUtils.addDays(statsTime, 1);

        //柜子押金  查询已经上线并且押金金额不为空，并且在柜子的押金记录中没有记录的柜子
        List<Cabinet> agentNeedForegiftList = cabinetMapper.findNeedForegiftList(statsTime);
        //柜子租金 可以存在多个周期没有生成记录的情况
        List<Cabinet> agentNeedRentList = cabinetMapper.findNeedRentList(statsTime);
        //换电电池租金 可以存在多个周期没有生成记录的情况
        List<Battery> exchangeBatteryNeedRentList = batteryMapper.findNeedRentList(statsTime, ConstEnum.Category.EXCHANGE.getValue());
        //租电电池租金 可以存在多个周期没有生成记录的情况
        List<Battery> rentBatteryNeedRentList = batteryMapper.findNeedRentList(statsTime, ConstEnum.Category.RENT.getValue());
        //认证费用
        List<IdCardAuthRecord> needPayIdCardAuthRecordList = idCardAuthRecordMapper.findByStatus(ConstEnum.PayStatus.NO_PAY.getValue(), statsTime);

        //柜子的押金记录
        for (Cabinet cabinet : agentNeedForegiftList) {
            Agent agent = getExchangeAgent(cabinet.getAgentId());
            List<AgentCabinetForegiftRecord> v = agentCabinetForegiftRecordMap.get(cabinet.getAgentId());
            if (v == null) {
                v = new ArrayList<AgentCabinetForegiftRecord>();
                agentCabinetForegiftRecordMap.put(cabinet.getAgentId(), v);
            }
            AgentCabinetForegiftRecord record = new AgentCabinetForegiftRecord();
            record.setAgentId(cabinet.getAgentId());
            record.setAgentName(agent.getAgentName());
            record.setAgentCode(agent.getAgentCode());
            record.setCabinetId(cabinet.getId());
            record.setCabinetName(cabinet.getCabinetName());
            record.setMoney(cabinet.getForegiftMoney());
            record.setStatus(ConstEnum.PayStatus.NO_PAY.getValue());
            record.setCreateTime(new Date());
            v.add(record);
        }

        //柜子的租金记录
        for (Cabinet cabinet : agentNeedRentList) {
            //截止时间  默认为当日23:59:59
            Date rentExpireTime = null;
            if(cabinet.getRentExpireTime() != null){
                rentExpireTime =DateUtils.addSeconds(DateUtils.truncate(DateUtils.addDays(cabinet.getRentExpireTime(), 1), Calendar.DAY_OF_MONTH) ,-1);
                //如果截止时间 < 租金记录最后记录结束时间，不再生成租金记录
                if(cabinet.getRentRecordTime() != null && rentExpireTime.compareTo(cabinet.getRentRecordTime()) < 0){
                    continue;
                }
            }

            Agent agent = getExchangeAgent(cabinet.getAgentId());
            List<AgentCabinetRentRecord> v = agentCabinetRentRecordMap.get(cabinet.getAgentId());
            if (v == null) {
                v = new ArrayList<AgentCabinetRentRecord>();
                agentCabinetRentRecordMap.put(cabinet.getAgentId(), v);
            }
            Date rentRecordTime = cabinet.getRentRecordTime();
            if(rentRecordTime == null){
                rentRecordTime = DateUtils.truncate(cabinet.getUpLineTime(), Calendar.DAY_OF_MONTH) ;
            }

            while(rentRecordTime.compareTo(statsTime) <= 0 ){
                if(rentExpireTime != null && rentRecordTime.compareTo(rentExpireTime) >= 0){
                    break;
                }
                Date beginTime = rentRecordTime;

                rentRecordTime = DateUtils.truncate(DateUtils.addMonths(beginTime, cabinet.getRentPeriodType()), Calendar.DAY_OF_MONTH);
                Date endTime = DateUtils.addSeconds(rentRecordTime,-1);
                int money = cabinet.getRentMoney();

                //截止日期在结束时间前，特殊处理
                if(rentExpireTime != null && rentExpireTime.compareTo(endTime) < 0){
                    rentRecordTime = DateUtils.addSeconds(rentExpireTime,1);
                    endTime = rentExpireTime;
                    int days = (int)((rentRecordTime.getTime() -beginTime.getTime())/(1000*3600*24));
                    double dayMoney = cabinet.getRentMoney() * 1d / (cabinet.getRentPeriodType() * 30);
                    money = (int)(dayMoney * days);
                }
                AgentCabinetRentRecord record = new AgentCabinetRentRecord();
                record.setAgentId(cabinet.getAgentId());
                record.setAgentName(agent.getAgentName());
                record.setAgentCode(agent.getAgentCode());
                record.setCabinetId(cabinet.getId());
                record.setCabinetName(cabinet.getCabinetName());
                record.setMoney(money);
                record.setStatus(ConstEnum.PayStatus.NO_PAY.getValue());
                record.setPeriodType(cabinet.getRentPeriodType());
                record.setBeginTime(beginTime);
                record.setEndTime(endTime);
                record.setCreateTime(new Date());
                v.add(record);
            }
            cabinet.setRentRecordTime(rentRecordTime);
        }

        //换电电池的租金记录
        for (Battery battery : exchangeBatteryNeedRentList) {
            //截止时间  默认为当日23:59:59
            Date rentExpireTime = null;
            if(battery.getRentExpireTime() != null) {
                 rentExpireTime = DateUtils.addSeconds(DateUtils.truncate(DateUtils.addDays(battery.getRentExpireTime(), 1), Calendar.DAY_OF_MONTH), -1);
                //如果截止时间 < 租金记录最后记录结束时间，不再生成租金记录
                if (battery.getRentRecordTime() != null && rentExpireTime.compareTo(battery.getRentRecordTime()) < 0) {
                    continue;
                }
            }

            Agent agent = getExchangeAgent(battery.getAgentId());
            List<AgentBatteryRentRecord> v = agentExchangeBatteryRentRecordMap.get(battery.getAgentId());
            if (v == null) {
                v = new ArrayList<AgentBatteryRentRecord>();
                agentExchangeBatteryRentRecordMap.put(battery.getAgentId(), v);
            }

            Date rentRecordTime = battery.getRentRecordTime();
            if(rentRecordTime == null){
                rentRecordTime = DateUtils.truncate(battery.getUpLineTime(), Calendar.DAY_OF_MONTH) ;
            }

            while(rentRecordTime.compareTo(statsTime) <= 0 ){
                if(rentExpireTime != null && rentRecordTime.compareTo(rentExpireTime) >= 0){
                    break;
                }
                Date beginTime = rentRecordTime;

                rentRecordTime = DateUtils.truncate(DateUtils.addMonths(beginTime, battery.getRentPeriodType()), Calendar.DAY_OF_MONTH);
                Date endTime = DateUtils.addSeconds(rentRecordTime,-1);
                int money = battery.getRentPeriodMoney();

                //截止日期在结束时间前，特殊处理
                if(rentExpireTime != null && rentExpireTime.compareTo(endTime) < 0){
                    rentRecordTime = DateUtils.addSeconds(rentExpireTime,1);
                    endTime = rentExpireTime;
                    int days = (int)((rentRecordTime.getTime() -beginTime.getTime())/(1000*3600*24));
                    double dayMoney = battery.getRentPeriodMoney() * 1d / (battery.getRentPeriodType() * 30);
                    money = (int)(dayMoney * days);
                }

                AgentBatteryRentRecord record = new AgentBatteryRentRecord();
                record.setAgentId(battery.getAgentId());
                record.setAgentName(agent.getAgentName());
                record.setAgentCode(agent.getAgentCode());
                record.setBatteryId(battery.getId());
                record.setMoney(money);
                record.setStatus(ConstEnum.PayStatus.NO_PAY.getValue());
                record.setPeriodType(battery.getRentPeriodType());
                record.setBeginTime(beginTime);
                record.setEndTime(endTime);
                record.setCreateTime(new Date());
                v.add(record);
            }
            battery.setRentRecordTime(rentRecordTime);
        }

        //租电电池的租金记录
        for (Battery battery : rentBatteryNeedRentList) {
            //截止时间  默认为当日23:59:59
            Date rentExpireTime = null;
            if(battery.getRentExpireTime() != null) {
                rentExpireTime = DateUtils.addSeconds(DateUtils.truncate(DateUtils.addDays(battery.getRentExpireTime(), 1), Calendar.DAY_OF_MONTH), -1);
                //如果截止时间 < 租金记录最后记录结束时间，不再生成租金记录
                if (battery.getRentRecordTime() != null && rentExpireTime.compareTo(battery.getRentRecordTime()) < 0) {
                    continue;
                }
            }

            Agent agent = getRentAgent(battery.getAgentId());
            List<AgentBatteryRentRecord> v = agentRentBatteryRentRecordMap.get(battery.getAgentId());
            if (v == null) {
                v = new ArrayList<AgentBatteryRentRecord>();
                agentRentBatteryRentRecordMap.put(battery.getAgentId(), v);
            }

            Date rentRecordTime = battery.getRentRecordTime();
            if(rentRecordTime == null){
                rentRecordTime = DateUtils.truncate(battery.getUpLineTime(), Calendar.DAY_OF_MONTH) ;
            }

            while(rentRecordTime.compareTo(statsTime) <= 0 ){
                if(rentExpireTime != null && rentRecordTime.compareTo(rentExpireTime) >= 0){
                    break;
                }
                Date beginTime = rentRecordTime;

                rentRecordTime = DateUtils.truncate(DateUtils.addMonths(beginTime, battery.getRentPeriodType()), Calendar.DAY_OF_MONTH);
                Date endTime = DateUtils.addSeconds(rentRecordTime,-1);
                int money = battery.getRentPeriodMoney();

                //截止日期在结束时间前，特殊处理
                if(rentExpireTime != null && rentExpireTime.compareTo(endTime) < 0){
                    rentRecordTime = DateUtils.addSeconds(rentExpireTime,1);
                    endTime = rentExpireTime;
                    int days = (int)((rentRecordTime.getTime() -beginTime.getTime())/(1000*3600*24));
                    double dayMoney = battery.getRentPeriodMoney() * 1d / (battery.getRentPeriodType() * 30);
                    money = (int)(dayMoney * days);
                }

                AgentBatteryRentRecord record = new AgentBatteryRentRecord();
                record.setAgentId(battery.getAgentId());
                record.setAgentName(agent.getAgentName());
                record.setAgentCode(agent.getAgentCode());
                record.setBatteryId(battery.getId());
                record.setMoney(money);
                record.setStatus(ConstEnum.PayStatus.NO_PAY.getValue());
                record.setPeriodType(battery.getRentPeriodType());
                record.setBeginTime(beginTime);
                record.setEndTime(endTime);
                record.setCreateTime(new Date());
                v.add(record);
            }
            battery.setRentRecordTime(rentRecordTime);
        }

        //认证费用
        for (IdCardAuthRecord idCardAuthRecord : needPayIdCardAuthRecordList) {
            Agent agent = getExchangeAgent(idCardAuthRecord.getAgentId());
            List<IdCardAuthRecord> v = idCardAuthRecorddMap.get(idCardAuthRecord.getAgentId());
            if (v == null) {
                v = new ArrayList<IdCardAuthRecord>();
                idCardAuthRecorddMap.put(idCardAuthRecord.getAgentId(), v);
            }
            v.add(idCardAuthRecord);
        }

        //运营商换电日统计保存
        for (Integer agentId : exchangeAgentMap.keySet()) {
            Agent agent = exchangeAgentMap.get(agentId);
            AgentMaterialDayStats dayStats = agentExchangeMaterialDayStatsMap.get(agentId);
            if (dayStats == null) {
                dayStats = new AgentMaterialDayStats();
                dayStats.init();
                dayStats.setAgentId(agentId);
                dayStats.setAgentCode(agent.getAgentCode());
                dayStats.setAgentName(agent.getAgentName());
                dayStats.setStatsDate(statsDate);
                dayStats.setCategory(ConstEnum.Category.EXCHANGE.getValue());
            }

            //柜子押金
            List<AgentCabinetForegiftRecord> foregiftRecordList = agentCabinetForegiftRecordMap.get(agentId);
            if(foregiftRecordList != null && foregiftRecordList.size() > 0){
                for(AgentCabinetForegiftRecord foregiftRecord: foregiftRecordList){
                    dayStats.setCabinetForegiftCount(dayStats.getCabinetForegiftCount() + 1);
                    dayStats.setCabinetForegiftMoney(dayStats.getCabinetForegiftMoney() + foregiftRecord.getMoney());
                    //押金不计入统计
                    //dayStats.setMoney(dayStats.getMoney() + foregiftRecord.getMoney());
                }
            }

            //柜子租金
            List<AgentCabinetRentRecord> rentRecordList = agentCabinetRentRecordMap.get(agentId);
            if(rentRecordList != null && rentRecordList.size() > 0){
                for(AgentCabinetRentRecord rentRecord: rentRecordList){
                    dayStats.setCabinetRentCount(dayStats.getCabinetRentCount() + 1);
                    dayStats.setCabinetRentMoney(dayStats.getCabinetRentMoney() + rentRecord.getMoney());
                    dayStats.setMoney(dayStats.getMoney() + rentRecord.getMoney());
                }
            }

            //电池租金
            List<AgentBatteryRentRecord> batteryRentRecordList = agentExchangeBatteryRentRecordMap.get(agentId);
            if(batteryRentRecordList != null && batteryRentRecordList.size() > 0){
                for(AgentBatteryRentRecord rentRecord: batteryRentRecordList){
                    dayStats.setBatteryRentCount(dayStats.getBatteryRentCount() + 1);
                    dayStats.setBatteryRentMoney(dayStats.getBatteryRentMoney() + rentRecord.getMoney());
                    dayStats.setMoney(dayStats.getMoney() + rentRecord.getMoney());
                }
            }

            //认证费用
            List<IdCardAuthRecord> idCardAuthRecordList = idCardAuthRecorddMap.get(agentId);
            if(idCardAuthRecordList != null && idCardAuthRecordList.size() > 0){
                for(IdCardAuthRecord idCardAuthRecord: idCardAuthRecordList){
                    dayStats.setIdCardAuthCount(dayStats.getIdCardAuthCount() + 1);
                    dayStats.setIdCardAuthMoney(dayStats.getIdCardAuthMoney() + idCardAuthRecord.getMoney());
                    dayStats.setMoney(dayStats.getMoney() + idCardAuthRecord.getMoney());
                }
            }

            //保存日统计数据
            saveAgentMaterialDayStats(dayStats);
            if(dayStats.getId() != null){
                //保存柜子押金
                saveAgentCabinetForegiftRecord(dayStats.getId(), foregiftRecordList);
                //保存柜子租金
                saveAgentCabinetRentRecord(dayStats.getId(), rentRecordList);
                //更新柜子最后生成租金时间
                updateCabinetRentRecordTime(agentNeedRentList);
                //保存电池租金
                saveAgentBatteryRentRecord(dayStats.getId(), batteryRentRecordList);
                //更新电池最后生成租金时间
                updateBatteryRentRecordTime(exchangeBatteryNeedRentList);
                //更新客户认证对应日统计id
                updateIdCardAuthRecord(dayStats.getId(), idCardAuthRecordList);
            }
        }

        //运营商租电日统计保存
        for (Integer agentId : rentAgentMap.keySet()) {
            Agent agent = rentAgentMap.get(agentId);
            AgentMaterialDayStats dayStats = agentRentMaterialDayStatsMap.get(agentId);
            if (dayStats == null) {
                dayStats = new AgentMaterialDayStats();
                dayStats.init();
                dayStats.setAgentId(agentId);
                dayStats.setAgentCode(agent.getAgentCode());
                dayStats.setAgentName(agent.getAgentName());
                dayStats.setStatsDate(statsDate);
                dayStats.setCategory(ConstEnum.Category.RENT.getValue());
            }

            //电池租金
            List<AgentBatteryRentRecord> batteryRentRecordList = agentRentBatteryRentRecordMap.get(agentId);
            if(batteryRentRecordList != null && batteryRentRecordList.size() > 0){
                for(AgentBatteryRentRecord rentRecord: batteryRentRecordList){
                    dayStats.setBatteryRentCount(dayStats.getBatteryRentCount() + 1);
                    dayStats.setBatteryRentMoney(dayStats.getBatteryRentMoney() + rentRecord.getMoney());
                    dayStats.setMoney(dayStats.getMoney() + rentRecord.getMoney());
                }
            }

            //保存日统计数据
            saveAgentMaterialDayStats(dayStats);
            if(dayStats.getId() != null){
                //保存电池租金
                saveAgentBatteryRentRecord(dayStats.getId(), batteryRentRecordList);
                //更新电池最后生成租金时间
                updateBatteryRentRecordTime(rentBatteryNeedRentList);
            }
        }

    }

    private Agent getExchangeAgent(int id) {
        Agent agent = exchangeAgentMap.get(id);
        if (agent == null) {
            agent = agentMapper.find(id);
            if(agent != null){
                exchangeAgentMap.put(agent.getId(), agent);
            }
        }
        return agent;
    }

    private Agent getRentAgent(int id) {
        Agent agent = rentAgentMap.get(id);
        if (agent == null) {
            agent = agentMapper.find(id);
            if(agent != null){
                rentAgentMap.put(agent.getId(), agent);
            }
        }
        return agent;
    }

    private void saveAgentMaterialDayStats(AgentMaterialDayStats dayStats){
        AgentMaterialDayStats entity = agentMaterialDayStatsMapper.find(dayStats.getAgentId(), dayStats.getStatsDate(), dayStats.getCategory());
        if (entity == null) {
            agentMaterialDayStatsMapper.insert(dayStats);
        }
    }

    private void saveAgentCabinetForegiftRecord(int dayStatsId, List<AgentCabinetForegiftRecord> foregiftRecordList){
        if(foregiftRecordList != null && foregiftRecordList.size() > 0){
            for(AgentCabinetForegiftRecord agentCabinetForegiftRecord : foregiftRecordList){
                agentCabinetForegiftRecord.setMaterialDayStatsId(dayStatsId);
                agentCabinetForegiftRecordMapper.insert(agentCabinetForegiftRecord);
            }
        }
    }

    private void saveAgentCabinetRentRecord(int dayStatsId, List<AgentCabinetRentRecord> rentRecordList){
        if(rentRecordList != null && rentRecordList.size() > 0){
            for(AgentCabinetRentRecord agentCabinetRentRecord : rentRecordList){
                agentCabinetRentRecord.setMaterialDayStatsId(dayStatsId);
                agentCabinetRentRecordMapper.insert(agentCabinetRentRecord);
            }
        }
    }

    private void updateCabinetRentRecordTime(List<Cabinet> cabinetList){
        if(cabinetList != null && cabinetList.size() > 0){
            for(Cabinet cabinet : cabinetList){
                cabinetMapper.updateRentRecordTime(cabinet.getId(), cabinet.getRentRecordTime());
            }
        }
    }

    private void saveAgentBatteryRentRecord(int dayStatsId, List<AgentBatteryRentRecord> rentRecordList){
        if(rentRecordList != null && rentRecordList.size() > 0){
            for(AgentBatteryRentRecord agentBatteryRentRecord : rentRecordList){
                agentBatteryRentRecord.setMaterialDayStatsId(dayStatsId);
                agentBatteryRentRecordMapper.insert(agentBatteryRentRecord);
            }
        }
    }

    private void updateBatteryRentRecordTime(List<Battery> batteryList){
        if(batteryList != null && batteryList.size() > 0){
            for(Battery battery : batteryList){
                batteryMapper.updateRentRecordTime(battery.getId(), battery.getRentRecordTime());
            }
        }
    }

    private void updateIdCardAuthRecord(int dayStatsId, List<IdCardAuthRecord> idCardAuthRecordList ){
        if(idCardAuthRecordList != null && idCardAuthRecordList.size() > 0){
            for(IdCardAuthRecord idCardAuthRecord : idCardAuthRecordList){
                idCardAuthRecordMapper.updateDayStatsId(idCardAuthRecord.getId(), dayStatsId);
            }
        }
    }
}
