package cn.com.yusong.yhdg.webserver.service.basic;

import cn.com.yusong.yhdg.common.domain.basic.AgentInfo;
import cn.com.yusong.yhdg.common.domain.basic.ChargerUpgradePackDetail;
import cn.com.yusong.yhdg.common.domain.hdg.CabinetCharger;
import cn.com.yusong.yhdg.common.entity.json.ExtResult;
import cn.com.yusong.yhdg.common.entity.pagination.Page;
import cn.com.yusong.yhdg.webserver.persistence.basic.ChargerUpgradePackDetailMapper;
import cn.com.yusong.yhdg.webserver.persistence.hdg.CabinetChargerMapper;
import cn.com.yusong.yhdg.webserver.service.AbstractService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ChargerUpgradePackDetailService extends AbstractService {

    @Autowired
    ChargerUpgradePackDetailMapper chargerUpgradePackDetailMapper;
    @Autowired
    CabinetChargerMapper cabinetChargerMapper;

    public Page findPage(ChargerUpgradePackDetail chargerUpgradePackDetail) {
        Page page = chargerUpgradePackDetail.buildPage();
        page.setTotalItems(chargerUpgradePackDetailMapper.findPageCount(chargerUpgradePackDetail));
        chargerUpgradePackDetail.setBeginIndex(page.getOffset());
        List<ChargerUpgradePackDetail> list = chargerUpgradePackDetailMapper.findPageResult(chargerUpgradePackDetail);
        for (ChargerUpgradePackDetail upgradePackDetail : list) {
            StringBuilder builder = new StringBuilder();
            if (StringUtils.isNotEmpty(upgradePackDetail.getCabinetId())) {
                List<CabinetCharger> cabinetChargerList = cabinetChargerMapper.findByCabinetId(upgradePackDetail.getCabinetId());
                for (CabinetCharger cabinetCharger : cabinetChargerList) {
                    if (cabinetChargerList.indexOf(cabinetCharger) != cabinetChargerList.size() - 1) {
                        builder.append(cabinetCharger.getChargerVersion() + ",");
                    } else {
                        builder.append(cabinetCharger.getChargerVersion());
                    }
                }
            }
            String cabinetChargerVersions = builder.toString();
            upgradePackDetail.setCabinetChargerVersions(cabinetChargerVersions);
            if (upgradePackDetail.getAgentId() != null) {
                AgentInfo agentInfo = findAgentInfo(upgradePackDetail.getAgentId());
                if (agentInfo != null) {
                    chargerUpgradePackDetail.setAgentName(agentInfo.getAgentName());
                }
            }
        }
        page.setResult(list);
        return page;
    }

    @Transactional(rollbackFor = Throwable.class)
    public ExtResult create(int upgradePackId, String[] chargerId) {

        for (int a = 0; a < chargerId.length; a++) {
            ChargerUpgradePackDetail chargerUpgradePackDetail = chargerUpgradePackDetailMapper.find(upgradePackId, chargerId[a]);
            if (null == chargerUpgradePackDetail) {
                chargerUpgradePackDetailMapper.insert(upgradePackId, chargerId[a]);
            } else {
                continue;
                //return ExtResult.failResult("不可重复添加!");
            }
        }
        return ExtResult.successResult();

    }

    @Transactional(rollbackFor = Throwable.class)
    public ExtResult delete(int upgradePackId, String terminalId) {
        int result = chargerUpgradePackDetailMapper.delete(upgradePackId, terminalId);
        if (result == 0) {
            return ExtResult.failResult("删除失败！");
        }
        return ExtResult.successResult();
    }

}
