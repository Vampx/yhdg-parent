package cn.com.yusong.yhdg.agentappserver.service.hdg;

import cn.com.yusong.yhdg.agentappserver.persistence.basic.AgentMapper;
import cn.com.yusong.yhdg.agentappserver.persistence.hdg.CabinetIncomeTemplateMapper;
import cn.com.yusong.yhdg.agentappserver.persistence.hdg.CabinetInstallRecordMapper;
import cn.com.yusong.yhdg.agentappserver.persistence.hdg.CabinetMapper;
import cn.com.yusong.yhdg.agentappserver.service.AbstractService;
import cn.com.yusong.yhdg.common.constant.ConstEnum;
import cn.com.yusong.yhdg.common.domain.hdg.Cabinet;
import cn.com.yusong.yhdg.common.domain.hdg.CabinetIncomeTemplate;
import cn.com.yusong.yhdg.common.domain.hdg.CabinetInstallRecord;
import cn.com.yusong.yhdg.common.entity.json.ExtResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class CabinetInstallRecordService extends AbstractService {

    @Autowired
    CabinetInstallRecordMapper cabinetInstallRecordMapper;
    @Autowired
    CabinetIncomeTemplateMapper cabinetIncomeTemplateMapper;
    @Autowired
    CabinetMapper cabinetMapper;
    @Autowired
    AgentMapper agentMapper;

    public ExtResult insert(CabinetInstallRecord entity) {
        entity.setCreateTime(new Date());
        CabinetIncomeTemplate cabinetIncomeTemplate = cabinetIncomeTemplateMapper.findByAgentId(entity.getAgentId());
        if (cabinetIncomeTemplate == null){
            //直接上线
            entity.setForegiftMoney(0);
            entity.setRentMoney(0);
            entity.setRentPeriodType(0);
            entity.setRentExpireTime(null);
            entity.setStatus(CabinetInstallRecord.Status.APPROVE.getValue());
            cabinetInstallRecordMapper.insert(entity);


            Cabinet cabinet = new Cabinet();
            cabinet.setAgentId(entity.getAgentId());
            cabinet.setId(entity.getCabinetId());
            cabinet.setCabinetName(entity.getCabinetName());
            cabinet.setAddress(entity.getAddress());
            cabinet.setTerminalId(entity.getTerminalId());
            cabinet.setPrice(entity.getPrice());
            cabinet.setRentPeriodType(0);
            cabinet.setPermitExchangeVolume(entity.getPermitExchangeVolume());
            cabinet.setChargeFullVolume(entity.getChargeFullVolume());
            cabinet.setUpLineStatus(Cabinet.UpLineStatus.ONLINE.getValue());
            cabinet.setUpLineTime(new Date());
            cabinet.setImagePath1(entity.getImagePath1());
            cabinet.setImagePath2(entity.getImagePath2());
            cabinet.setMinExchangeVolume(entity.getMinExchangeVolume());
            cabinetMapper.update(cabinet);
        }else if (cabinetIncomeTemplate.getIsReview() == CabinetIncomeTemplate.IsReview.YES.getValue()) {
            //直接上线
            entity.setForegiftMoney(cabinetIncomeTemplate.getForegiftMoney());
            entity.setRentMoney(cabinetIncomeTemplate.getRentMoney());
            entity.setRentPeriodType(cabinetIncomeTemplate.getPeriodType());
            entity.setRentExpireTime(cabinetIncomeTemplate.getRentExpireTime());
            entity.setStatus(CabinetInstallRecord.Status.APPROVE.getValue());
            cabinetInstallRecordMapper.insert(entity);


            Cabinet cabinet = new Cabinet();
            cabinet.setAgentId(entity.getAgentId());
            cabinet.setId(entity.getCabinetId());
            cabinet.setCabinetName(entity.getCabinetName());
            cabinet.setAddress(entity.getAddress());
            cabinet.setTerminalId(entity.getTerminalId());
            cabinet.setPrice(entity.getPrice());
            cabinet.setRentPeriodType(cabinetIncomeTemplate.getPeriodType());
            cabinet.setPermitExchangeVolume(entity.getPermitExchangeVolume());
            cabinet.setChargeFullVolume(entity.getChargeFullVolume());
            cabinet.setUpLineStatus(Cabinet.UpLineStatus.ONLINE.getValue());
            cabinet.setUpLineTime(new Date());
            cabinet.setImagePath1(entity.getImagePath1());
            cabinet.setImagePath2(entity.getImagePath2());
            cabinet.setMinExchangeVolume(entity.getMinExchangeVolume());
            cabinetMapper.update(cabinet);
        }else {
            //需要审核
            entity.setForegiftMoney(cabinetIncomeTemplate.getForegiftMoney());
            entity.setRentMoney(cabinetIncomeTemplate.getRentMoney());
            entity.setRentPeriodType(cabinetIncomeTemplate.getPeriodType());
            entity.setRentExpireTime(cabinetIncomeTemplate.getRentExpireTime());
            entity.setStatus(CabinetInstallRecord.Status.UNREVIEWED.getValue());
            cabinetInstallRecordMapper.insert(entity);
        }
        return ExtResult.successResult();
    }

    public CabinetInstallRecord findByCabinetId(String cabinetId) {
        return cabinetInstallRecordMapper.findByCabinetId(cabinetId);
    }
}
