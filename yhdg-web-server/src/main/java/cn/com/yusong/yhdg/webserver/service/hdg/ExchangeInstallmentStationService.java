package cn.com.yusong.yhdg.webserver.service.hdg;

import cn.com.yusong.yhdg.common.domain.basic.AgentInfo;
import cn.com.yusong.yhdg.common.domain.hdg.*;
import cn.com.yusong.yhdg.common.entity.AreaCache;
import cn.com.yusong.yhdg.common.entity.json.ExtResult;
import cn.com.yusong.yhdg.common.entity.pagination.Page;
import cn.com.yusong.yhdg.webserver.persistence.hdg.*;
import cn.com.yusong.yhdg.webserver.service.AbstractService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
public class ExchangeInstallmentStationService extends AbstractService {


    @Autowired
    ExchangeInstallmentStationMapper exchangeInstallmentStationMapper;
    @Autowired
    ExchangeInstallmentSettingMapper exchangeInstallmentSettingMapper;
    @Autowired
    CabinetMapper cabinetMapper;
    @Autowired
    StationMapper stationMapper;
    @Autowired
    AreaCache areaCache;
    @Autowired
    CabinetBoxMapper cabinetBoxMapper;

    public ExchangeInstallmentStation findCabinetId(ExchangeInstallmentStation exchangeInstallmentCabinet){
        return exchangeInstallmentStationMapper.findStationId(exchangeInstallmentCabinet);
    }


    public Page findPage(Station search) {
        Page page = search.buildPage();
        page.setTotalItems(stationMapper.findPageMentStationCount(search));
        search.setBeginIndex(page.getOffset());
        List<Station> list = stationMapper.findPageMentStationResult(search);
        for (Station station : list) {
            AgentInfo agentInfo = findAgentInfo(station.getAgentId());
            if (agentInfo != null) {
                station.setAgentName(agentInfo.getAgentName());
            }
        }
        page.setResult(setAreaProperties(areaCache, list));
        return page;
    }

    public Page findPageInstallmentStation(Station search) {
        Page page = search.buildPage();
        page.setTotalItems(stationMapper.findPageMentStationCountNum(search));
        search.setBeginIndex(page.getOffset());
        List<Station> list = stationMapper.findPageMentStationResultNum(search);
        for (Station station : list) {
            AgentInfo agentInfo = findAgentInfo(station.getAgentId());
            if (agentInfo != null) {
                station.setAgentName(agentInfo.getAgentName());
            }
        }
        page.setResult(setAreaProperties(areaCache, list));
        return page;
    }


    @Transactional(rollbackFor=Throwable.class)
    public ExtResult insert(Long settingId, String[] stationIds){

        if(settingId==null){
            return ExtResult.failResult("分期设置ID不能为空！");
        }
        if(stationIds.length==0){
            return ExtResult.failResult("添加站点ID为空！");
        }
        ExchangeInstallmentSetting exchangeInstallmentSetting = exchangeInstallmentSettingMapper.find(settingId);
        if(exchangeInstallmentSetting == null){
            return ExtResult.failResult("分期设置不存在！");
        }
        List<Station> ids = stationMapper.findIds(stationIds);
        Integer num =0;
        for (Station station: ids) {
            ExchangeInstallmentStation exchangeInstallmentStation = new ExchangeInstallmentStation();
            exchangeInstallmentStation.setSettingId(settingId);
            exchangeInstallmentStation.setStationId(station.getId());
            exchangeInstallmentStation.setStationName(station.getStationName());
            ExchangeInstallmentStation cabinetId = exchangeInstallmentStationMapper.findStationId(exchangeInstallmentStation);
            if (cabinetId==null){
                num += exchangeInstallmentStationMapper.insert(exchangeInstallmentStation);
            }else{
                num += exchangeInstallmentStationMapper.update(exchangeInstallmentStation);
            }

        }
        if(ids.size() !=num){
            return ExtResult.failResult("设备添加失败！");
        }
        return ExtResult.successResult("设备添加成功");

    }

    public int update(ExchangeInstallmentStation cabinet){
        return exchangeInstallmentStationMapper.update(cabinet);
    }

    @Transactional(rollbackFor=Throwable.class)
    public ExtResult deleteStationId(ExchangeInstallmentStation exchangeInstallmentStation){
        if(StringUtils.isBlank(exchangeInstallmentStation.getStationId())) {
            return ExtResult.failResult("站点ID不能为空！");
        }
        if(exchangeInstallmentStation.getSettingId()==null){
            return ExtResult.failResult("分期设置ID不能为空！");
        }

        int i = exchangeInstallmentStationMapper.deleteStationId(exchangeInstallmentStation);
        if(i==0){
            return ExtResult.failResult("解绑站点失败！");
        }

        return  ExtResult.successResult("解绑站点成功");
    }

    public List<ExchangeInstallmentStation> findSettingId(Long settingId){
        return exchangeInstallmentStationMapper.findSettingId(settingId);
    }
}
