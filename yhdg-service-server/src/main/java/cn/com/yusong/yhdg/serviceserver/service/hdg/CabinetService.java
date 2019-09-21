package cn.com.yusong.yhdg.serviceserver.service.hdg;

import cn.com.yusong.yhdg.common.constant.ConstEnum;
import cn.com.yusong.yhdg.common.domain.basic.AgentInfo;
import cn.com.yusong.yhdg.common.domain.basic.PushMessage;
import cn.com.yusong.yhdg.common.domain.hdg.*;
import cn.com.yusong.yhdg.common.entity.AreaCache;
import cn.com.yusong.yhdg.serviceserver.persistence.basic.AgentMapper;
import cn.com.yusong.yhdg.serviceserver.persistence.hdg.*;
import cn.com.yusong.yhdg.serviceserver.service.AbstractService;
import cn.com.yusong.yhdg.serviceserver.service.basic.SystemConfigService;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class CabinetService extends AbstractService {
    static final Logger log = LogManager.getLogger(CabinetService.class);
    @Autowired
    CabinetMapper cabinetMapper;
    @Autowired
    AgentMapper agentMapper;
    @Autowired
    CabinetBoxMapper cabinetBoxMapper;
    @Autowired
    CabinetOnlineStatsMapper cabinetOnlineStatsMapper;
    @Autowired
    BatteryMapper batteryMapper;
    @Autowired
    SystemConfigService systemConfigService;
    @Autowired
    AreaCache areaCache;
    @Autowired
    CabinetDayDegreeStatsMapper cabinetDayDegreeStatsMapper;

    /**
     * 统计柜子用电量
     */
    public void SyncVolume() {
        log.debug("统计换电柜使用电量开始...");
        int offset = 0, limit = 1000;
        while (true) {
            List<Cabinet> cabinetList =  (List<Cabinet>) setAreaProperties(areaCache,cabinetMapper.findAll( offset, limit));
            if (cabinetList.isEmpty()) {
                break;
            }
            for(Cabinet cabinet : cabinetList){
                Integer volume = cabinetDayDegreeStatsMapper.findUseVolume(cabinet.getId());
                cabinetMapper.updateUseVolume(cabinet.getId(), volume);
            }

            offset += limit;
        }
        log.debug("统计换电柜使用电量结束...");
    }

    /**
     * 柜子心跳3分钟不上报 设置离线
     */
    public void refreshOnline() {
        while (true) {
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.MINUTE, -3);
            List<Cabinet> cabinetList = cabinetMapper.findByHeartTime(calendar.getTime(), 500);

            for (Cabinet cabinet : cabinetList) {
                CabinetOnlineStats stats = cabinetOnlineStatsMapper.findMaxRecord(cabinet.getId());
                if (stats != null && stats.getEndTime() == null) {
                    cabinetOnlineStatsMapper.updateEndTime(cabinet.getId(), new Date());
                }
                cabinetMapper.updateOnline(cabinet.getId(), ConstEnum.Flag.FALSE.getValue());
                cabinetBoxMapper.updateOnline(cabinet.getId(), ConstEnum.Flag.FALSE.getValue());
            }

            if (cabinetList.isEmpty()) {
                break;
            }
        }
    }

    /**
     * 离线时间超过5分钟 就发推送出来
     */
    public void refreshOfflineMessageTime() {
        while (true) {
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.MINUTE, -5);
            List<Cabinet> cabinetList = cabinetMapper.findByOfflineMessageTime(calendar.getTime(), 500);

            for (Cabinet cabinet : cabinetList) {
                insertPushMetaData(PushMessage.SourceType.CABINET_OFFLINE.getValue(), cabinet.getId());

                if(!StringUtils.isEmpty(cabinet.getId())) {
                    if(cabinet != null) {
                        AgentInfo agent = findAgentInfo(cabinet.getAgentId());
                        FaultLog faultLog = insertFaultLog(FaultLog.FaultLevel.IMPORTANCE.getValue(), null, cabinet.getProvinceId(), cabinet.getCityId(), cabinet.getDistrictId(), cabinet.getDispatcherId(), agent.getId(), agent.getAgentName(), null, cabinet.getId(), cabinet.getCabinetName(), cabinet.getAddress(), null, FaultLog.FaultType.CODE_21.getValue(), PushMessage.SourceType.CABINET_OFFLINE.getName());
                        cabinetMapper.updateOfflineMessageTime(cabinet.getId(), faultLog.getId());
                    }
                }
            }

            if (cabinetList.isEmpty()) {
                break;
            }
        }
    }

}
