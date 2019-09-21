package cn.com.yusong.yhdg.webserver.service.hdg;

import cn.com.yusong.yhdg.common.domain.hdg.Cabinet;
import cn.com.yusong.yhdg.common.domain.hdg.CabinetInstallRecord;
import cn.com.yusong.yhdg.common.entity.json.ExtResult;
import cn.com.yusong.yhdg.common.entity.pagination.Page;
import cn.com.yusong.yhdg.webserver.persistence.basic.AgentMapper;
import cn.com.yusong.yhdg.webserver.persistence.hdg.CabinetIncomeTemplateMapper;
import cn.com.yusong.yhdg.webserver.persistence.hdg.CabinetInstallRecordMapper;
import cn.com.yusong.yhdg.webserver.persistence.hdg.CabinetMapper;
import cn.com.yusong.yhdg.webserver.service.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class CabinetInstallRecordService extends AbstractService{

    @Autowired
    CabinetInstallRecordMapper cabinetInstallRecordMapper;
    @Autowired
    CabinetIncomeTemplateMapper cabinetIncomeTemplateMapper;
    @Autowired
    CabinetMapper cabinetMapper;
    @Autowired
    AgentMapper agentMapper;

    public CabinetInstallRecord find(long id){
        return cabinetInstallRecordMapper.find(id);
    }

    public Page findPage(CabinetInstallRecord entity) {
        Page page = entity.buildPage();
        page.setTotalItems(cabinetInstallRecordMapper.findPageCount(entity));
        entity.setBeginIndex(page.getOffset());
        List<CabinetInstallRecord> list = cabinetInstallRecordMapper.findPageResult(entity);
        for (CabinetInstallRecord cabinetInstallRecord : list) {
            cabinetInstallRecord.setAgentName(agentMapper.find(cabinetInstallRecord.getAgentId()).getAgentName());
        }
        page.setResult(list);
        return page;
    }

    @Transactional(rollbackFor = Throwable.class)
    public ExtResult updateUpLine(CabinetInstallRecord entity) {
        cabinetInstallRecordMapper.updateUpLine(entity);
        CabinetInstallRecord cabinetInstallRecord = cabinetInstallRecordMapper.find(entity.getId());
        if (entity.getStatus() == CabinetInstallRecord.Status.APPROVE.getValue()) {
            Cabinet dbCabinet = cabinetMapper.find(cabinetInstallRecord.getCabinetId());
            if (dbCabinet != null) {
                dbCabinet.setAgentId(cabinetInstallRecord.getAgentId());
                dbCabinet.setId(cabinetInstallRecord.getCabinetId());
                dbCabinet.setTerminalId(cabinetInstallRecord.getTerminalId());
                dbCabinet.setAddress(cabinetInstallRecord.getAddress());
                dbCabinet.setPrice(cabinetInstallRecord.getPrice());
                dbCabinet.setPermitExchangeVolume(cabinetInstallRecord.getPermitExchangeVolume());
                dbCabinet.setUpLineStatus(Cabinet.UpLineStatus.ONLINE.getValue());
                dbCabinet.setChargeFullVolume(cabinetInstallRecord.getChargeFullVolume());
                dbCabinet.setUpLineTime(new Date());
                dbCabinet.setImagePath1(cabinetInstallRecord.getImagePath1());
                dbCabinet.setImagePath2(cabinetInstallRecord.getImagePath2());
                cabinetMapper.update(dbCabinet);
            }
        }else if (entity.getStatus() == CabinetInstallRecord.Status.UnAPPROVE.getValue()) {
            cabinetMapper.updateUplineStatus(1, entity.getCabinetId(), Cabinet.UpLineStatus.NOT_ONLINE.getValue());
        }
        return ExtResult.successResult();
    }

}
