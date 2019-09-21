package cn.com.yusong.yhdg.webserver.service.hdg;

import cn.com.yusong.yhdg.common.constant.ConstEnum;
import cn.com.yusong.yhdg.common.domain.hdg.Battery;
import cn.com.yusong.yhdg.common.domain.hdg.Cabinet;
import cn.com.yusong.yhdg.common.domain.hdg.CabinetBox;
import cn.com.yusong.yhdg.common.entity.CabinetBoxStats;
import cn.com.yusong.yhdg.common.entity.pagination.Page;
import cn.com.yusong.yhdg.webserver.persistence.basic.AgentMapper;
import cn.com.yusong.yhdg.webserver.persistence.hdg.CabinetBoxMapper;
import cn.com.yusong.yhdg.webserver.persistence.hdg.CabinetMapper;
import cn.com.yusong.yhdg.webserver.service.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CabinetBoxProhibitStatsService extends AbstractService {


    @Autowired
    CabinetMapper cabinetMapper;
    @Autowired
    CabinetBoxMapper cabinetBoxMapper;
    @Autowired
    AgentMapper agentMapper;

    public List<CabinetBox> findList(CabinetBox search) {
        List<CabinetBox> byIsActiveAll = cabinetBoxMapper.findByIsActiveAll(search);
        for (CabinetBox box: byIsActiveAll) {
            if (org.apache.commons.lang3.StringUtils.isNotBlank(box.getCabinetId())){
                Cabinet cabinet = cabinetMapper.find(box.getCabinetId());
                if(cabinet!=null&&cabinet.getAgentId()!=null){
                    cabinet.setAgentName(agentMapper.find(cabinet.getAgentId()).getAgentName());
                }
                box.setCabinet(cabinet);
            }
        }



        return byIsActiveAll;
    }


}
