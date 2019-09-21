package cn.com.yusong.yhdg.webserver.service.hdg;

import cn.com.yusong.yhdg.common.constant.ConstEnum;
import cn.com.yusong.yhdg.common.domain.basic.Agent;
import cn.com.yusong.yhdg.common.domain.hdg.DistributionOperate;
import cn.com.yusong.yhdg.common.domain.hdg.Station;
import cn.com.yusong.yhdg.common.domain.hdg.StationDistribution;
import cn.com.yusong.yhdg.common.entity.json.ExtResult;
import cn.com.yusong.yhdg.webserver.persistence.basic.AgentMapper;
import cn.com.yusong.yhdg.webserver.persistence.hdg.DistributionOperateMapper;
import cn.com.yusong.yhdg.webserver.persistence.hdg.StationDistributionMapper;
import cn.com.yusong.yhdg.webserver.persistence.hdg.StationMapper;
import cn.com.yusong.yhdg.webserver.service.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class StationDistributionService extends AbstractService {

    @Autowired
    StationDistributionMapper stationDistributionMapper;
    @Autowired
    DistributionOperateMapper distributionOperateMapper;
    @Autowired
    StationMapper stationMapper;
    @Autowired
    AgentMapper agentMapper;

    public List<StationDistribution> findByStationId(String stationId) {
        List<StationDistribution> list = stationDistributionMapper.findByStationId(stationId);
        for (StationDistribution stationDistribution : list) {
            if (stationDistribution.getOperateId() != null && stationDistribution.getOperateId() != 0) {
                DistributionOperate operate = distributionOperateMapper.find(stationDistribution.getOperateId());
                stationDistribution.setOperateName(operate.getDistributionName());
            }
         }
        return list;
    }

    public List<StationDistribution> findByStationIdReport(String stationId) {
        List<StationDistribution> list = stationDistributionMapper.findByStationId(stationId);
        for (StationDistribution stationDistribution : list) {
            stationDistribution.setAgentName(findAgentInfo(stationDistribution.getAgentId()).getAgentName());
            stationDistribution.setStationName(stationMapper.find(stationDistribution.getStationId()).getStationName());
            stationDistribution.setDeptName(StationDistribution.DeptType.getName(stationDistribution.getDeptType()));
            stationDistribution.setIsNotFixedName(ConstEnum.Flag.getName(stationDistribution.getIsNotFixed()));
            stationDistribution.setIsFixedName(ConstEnum.Flag.getName(stationDistribution.getIsFixed()));
            stationDistribution.setIsFixedPercentName(ConstEnum.Flag.getName(stationDistribution.getIsFixedPercent()));
        }
        return list;
    }

    @Transactional(rollbackFor=Throwable.class)
    public ExtResult createOrUpdate(String stationId,
                                    Integer [] deptTypeList,
                                    Integer [] isNotFixedList,
                                    Integer [] isFixedList,
                                    Integer [] isFixedPercentList,
                                    Double [] moneyList,
                                    Double [] percentList,
                                    Integer [] numList,
                                    Integer [] operateIdList) {

        Station station = stationMapper.find(stationId);

        Agent agent = agentMapper.find(station.getAgentId());

        List<StationDistribution> list = stationDistributionMapper.findByStationId(stationId);
        if (list.size() > 0) {
            stationDistributionMapper.deleteByStationId(stationId);
        }

        for(int i = 0; i < numList.length; i++) {
            StationDistribution stationDistribution = new StationDistribution();
            stationDistribution.setStationId(stationId);
            stationDistribution.setAgentId(station.getAgentId());
            stationDistribution.setAgentName(agent.getAgentName());
            stationDistribution.setAgentCode(agent.getAgentCode());
            stationDistribution.setDeptType(deptTypeList[i]);
            stationDistribution.setIsNotFixed(isNotFixedList[i]);
            stationDistribution.setIsFixed(isFixedList[i]);
            stationDistribution.setIsFixedPercent(isFixedPercentList[i]);
            if (numList[i] != 0) {
                stationDistribution.setNum(numList[i]);
            } else {
                stationDistribution.setNum(0);
            }
            if (operateIdList[i] != null && operateIdList[i] != 0) {
                stationDistribution.setOperateId(Long.valueOf(operateIdList[i]));
            } else {
                stationDistribution.setOperateId(0l);
            }
            if (!moneyList[i].isNaN()) {
                stationDistribution.setMoney((int) (moneyList[i] * 100));
            } else {
                stationDistribution.setMoney(0);
            }
            if (!percentList[i].isNaN()) {
                stationDistribution.setPercent((int) (percentList[i]*1));
            } else {
                stationDistribution.setPercent(0);
            }
            stationDistribution.setCreateTime(new Date());
            stationDistributionMapper.insert(stationDistribution);
        }
        return ExtResult.successResult();
    }

}
