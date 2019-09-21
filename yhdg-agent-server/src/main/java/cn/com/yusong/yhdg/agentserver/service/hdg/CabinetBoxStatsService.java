package cn.com.yusong.yhdg.agentserver.service.hdg;

import cn.com.yusong.yhdg.common.constant.ConstEnum;
import cn.com.yusong.yhdg.common.domain.hdg.Battery;
import cn.com.yusong.yhdg.common.domain.hdg.Cabinet;
import cn.com.yusong.yhdg.common.domain.hdg.CabinetBox;
import cn.com.yusong.yhdg.common.entity.CabinetBoxStats;
import cn.com.yusong.yhdg.common.entity.pagination.Page;
import cn.com.yusong.yhdg.agentserver.persistence.hdg.CabinetMapper;
import cn.com.yusong.yhdg.agentserver.persistence.hdg.CabinetBoxMapper;
import cn.com.yusong.yhdg.agentserver.service.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CabinetBoxStatsService extends AbstractService {
    @Autowired
    CabinetMapper cabinetMapper;
    @Autowired
    CabinetBoxMapper cabinetBoxMapper;

    public Page findPage(Cabinet search) {
        List<CabinetBoxStats> cabinetBoxStatsList = new ArrayList<CabinetBoxStats>();
        Page page = search.buildPage();
        List<Cabinet> list = cabinetMapper.findPageResultByStats(search);
        page.setTotalItems(cabinetMapper.findPageCountByStats(search));
        for (Cabinet c : list) {
            String id = c.getId();
            CabinetBoxStats cabinetBoxStats = new CabinetBoxStats();
            cabinetBoxStats.setAgentName(findAgentInfo(c.getAgentId()).getAgentName());
            cabinetBoxStats.setCabinetName(c.getCabinetName());
            cabinetBoxStats.setCabinetId(id);
            //换电柜格口数量
            cabinetBoxStats.setBoxCount(cabinetBoxMapper.statsBoxCount(c.getAgentId(), id));
            //空箱数量
            cabinetBoxStats.setEmptyCount(cabinetBoxMapper.statsBoxCountByStatus(id, CabinetBox.BoxStatus.EMPTY.getValue()));
            //门打开的数量
            cabinetBoxStats.setOpenCount(cabinetBoxMapper.statsOpenBoxCount(id, ConstEnum.Flag.TRUE.getValue()));
            //有电池的格口数量
            cabinetBoxStats.setBatteryCount(cabinetBoxMapper.statsBoxCountByStatus(id, CabinetBox.BoxStatus.FULL.getValue()));
            //正在充电的电池
            cabinetBoxStats.setChargingCount(cabinetBoxMapper.statsCountByChargeStatus(id, Battery.ChargeStatus.CHARGING.getValue()));
            //待充数量
            cabinetBoxStats.setWaitChargeCount(cabinetBoxMapper.statsCountByChargeStatus(id, Battery.ChargeStatus.WAIT_CHARGE.getValue()));
            //完成充电的数量
            cabinetBoxStats.setCompleteChargeCount(cabinetBoxMapper.statsCompleteChargeCount(id, Battery.Status.IN_BOX.getValue()));
            //客户换电未付款数量
            cabinetBoxStats.setNotPayCount(cabinetBoxMapper.statsCountByStatus(id, Battery.Status.IN_BOX_NOT_PAY.getValue()));
            //客户换电未取出数量
            cabinetBoxStats.setNotTakeCount(cabinetBoxMapper.statsCountByStatus(id, Battery.Status.IN_BOX_CUSTOMER_USE.getValue()));

            cabinetBoxStatsList.add(cabinetBoxStats);
        }

        page.setResult(cabinetBoxStatsList);
        return page;
    }

    public List<CabinetBoxStats> findList(Cabinet search) {
        List<CabinetBoxStats> cabinetBoxStatsList = new ArrayList<CabinetBoxStats>();
        List<Cabinet> list = cabinetMapper.findPageResultByStats(search);
        for (Cabinet c : list) {
            String id = c.getId();
            CabinetBoxStats cabinetBoxStats = new CabinetBoxStats();
            cabinetBoxStats.setAgentName(findAgentInfo(c.getAgentId()).getAgentName());
            cabinetBoxStats.setCabinetName(c.getCabinetName());
            cabinetBoxStats.setCabinetId(id);
            //换电柜格口数量
            cabinetBoxStats.setBoxCount(cabinetBoxMapper.statsBoxCount(c.getAgentId(), id));
            //空箱数量
            cabinetBoxStats.setEmptyCount(cabinetBoxMapper.statsBoxCountByStatus(id, CabinetBox.BoxStatus.EMPTY.getValue()));
            //门打开的数量
            cabinetBoxStats.setOpenCount(cabinetBoxMapper.statsOpenBoxCount(id, ConstEnum.Flag.TRUE.getValue()));
            //有电池的格口数量
            cabinetBoxStats.setBatteryCount(cabinetBoxMapper.statsBoxCountByStatus(id, CabinetBox.BoxStatus.FULL.getValue()));
            //正在充电的电池
            cabinetBoxStats.setChargingCount(cabinetBoxMapper.statsCountByChargeStatus(id, Battery.ChargeStatus.CHARGING.getValue()));
            //待充数量
            cabinetBoxStats.setWaitChargeCount(cabinetBoxMapper.statsCountByChargeStatus(id, Battery.ChargeStatus.WAIT_CHARGE.getValue()));
            //完成充电的数量
            cabinetBoxStats.setCompleteChargeCount(cabinetBoxMapper.statsCompleteChargeCount(id, Battery.Status.IN_BOX.getValue()));
            //客户换电未付款数量
            cabinetBoxStats.setNotPayCount(cabinetBoxMapper.statsCountByStatus(id, Battery.Status.IN_BOX_NOT_PAY.getValue()));
            //客户换电未取出数量
            cabinetBoxStats.setNotTakeCount(cabinetBoxMapper.statsCountByStatus(id, Battery.Status.IN_BOX_CUSTOMER_USE.getValue()));

            cabinetBoxStatsList.add(cabinetBoxStats);
        }

        return cabinetBoxStatsList;
    }


    public int statsBoxCountByStatusAndAgent(Integer agentId, Integer type) {
        return cabinetBoxMapper.statsBoxCountByStatusAndAgent(agentId, type, CabinetBox.BoxStatus.FULL.getValue());
    }

    public int statsCountByChargeStatusAndAgent(Integer agentId, Integer type) {
        return cabinetBoxMapper.statsCountByChargeStatusAndAgent(agentId, type, Battery.ChargeStatus.CHARGING.getValue());
    }

    public int statsCompleteChargeCountAndAgent(Integer agentId, Integer type) {
        return cabinetBoxMapper.statsCompleteChargeCountAndAgent(agentId, type, Battery.Status.IN_BOX.getValue());
    }

    public Page statsPage(CabinetBox cabinetBox) {
        Page<CabinetBox> page = new Page<CabinetBox>();
        String cabinetId = cabinetBox.getCabinetId();
        if (cabinetBox.getViewFlag() == 1) { //换电柜格口数量
            List<CabinetBox> list = cabinetBoxMapper.statsBoxPage(cabinetId);
            page.setResult(list);
        } else if (cabinetBox.getViewFlag() == 2) { //空箱数量
            List<CabinetBox> list = cabinetBoxMapper.statsBoxPageByStatus(cabinetId, CabinetBox.BoxStatus.EMPTY.getValue());
            page.setResult(list);
        } else if (cabinetBox.getViewFlag() == 3) { //门打开的数量
            List<CabinetBox> list = cabinetBoxMapper.statsOpenBoxPage(cabinetId, ConstEnum.Flag.TRUE.getValue());
            page.setResult(list);
        } else if (cabinetBox.getViewFlag() == 4) { //有电池的格口数量
            List<CabinetBox> list = cabinetBoxMapper.statsBoxPageByStatus(cabinetId, CabinetBox.BoxStatus.FULL.getValue());
            page.setResult(list);
        } else if (cabinetBox.getViewFlag() == 5) { //正在充电的电池
            List<CabinetBox> list = cabinetBoxMapper.statsCountByChargeStatusPage(cabinetId, Battery.ChargeStatus.CHARGING.getValue());
            page.setResult(list);
        } else if (cabinetBox.getViewFlag() == 6) { //待充数量
            List<CabinetBox> list = cabinetBoxMapper.statsCountByChargeStatusPage(cabinetId, Battery.ChargeStatus.WAIT_CHARGE.getValue());
            page.setResult(list);
        } else if (cabinetBox.getViewFlag() == 7) { //完成充电的数量
            List<CabinetBox> list = cabinetBoxMapper.statsCompleteChargePage(cabinetId, Battery.Status.IN_BOX.getValue());
            page.setResult(list);
        } else if (cabinetBox.getViewFlag() == 8) { //未付款的数量
            List<CabinetBox> list = cabinetBoxMapper.statsCountByStatusPage(cabinetId, Battery.Status.IN_BOX_NOT_PAY.getValue());
            page.setResult(list);
        } else if (cabinetBox.getViewFlag() == 9) { //未取出数量
            List<CabinetBox> list = cabinetBoxMapper.statsCountByStatusPage(cabinetId, Battery.Status.IN_BOX_CUSTOMER_USE.getValue());
            page.setResult(list);
        }

        return page;
    }
}
