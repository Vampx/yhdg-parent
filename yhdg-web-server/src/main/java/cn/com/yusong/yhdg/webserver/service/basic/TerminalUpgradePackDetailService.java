package cn.com.yusong.yhdg.webserver.service.basic;

import cn.com.yusong.yhdg.common.domain.basic.AgentInfo;
import cn.com.yusong.yhdg.common.domain.basic.TerminalUpgradePackDetail;
import cn.com.yusong.yhdg.common.entity.json.ExtResult;
import cn.com.yusong.yhdg.common.entity.pagination.Page;
import cn.com.yusong.yhdg.webserver.persistence.basic.TerminalUpgradePackDetailMapper;
import cn.com.yusong.yhdg.webserver.service.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TerminalUpgradePackDetailService extends AbstractService {

    @Autowired
    TerminalUpgradePackDetailMapper terminalUpgradePackDetailMapper;

    public Page findPage(TerminalUpgradePackDetail terminalUpgradePackDetail) {
        Page page = terminalUpgradePackDetail.buildPage();
        page.setTotalItems(terminalUpgradePackDetailMapper.findPageCount(terminalUpgradePackDetail));
        terminalUpgradePackDetail.setBeginIndex(page.getOffset());
        List<TerminalUpgradePackDetail> list = terminalUpgradePackDetailMapper.findPageResult(terminalUpgradePackDetail);
        for (TerminalUpgradePackDetail upgradePackDetail : list) {
            if (upgradePackDetail.getAgentId() != null) {
                AgentInfo agentInfo = findAgentInfo(upgradePackDetail.getAgentId());
                if (agentInfo != null) {
                    terminalUpgradePackDetail.setAgentName(agentInfo.getAgentName());
                }
            }
        }
        page.setResult(list);
        return page;
    }

    public Page findPageScreen(TerminalUpgradePackDetail terminalUpgradePackDetail) {
        Page page = terminalUpgradePackDetail.buildPage();
        page.setTotalItems(terminalUpgradePackDetailMapper.findPageScreenCount(terminalUpgradePackDetail));
        terminalUpgradePackDetail.setBeginIndex(page.getOffset());
        List<TerminalUpgradePackDetail> list = terminalUpgradePackDetailMapper.findPageScreenResult(terminalUpgradePackDetail);
        for (TerminalUpgradePackDetail upgradePackDetail : list) {
            if (upgradePackDetail.getAgentId() != null) {
                AgentInfo agentInfo = findAgentInfo(upgradePackDetail.getAgentId());
                if (agentInfo != null) {
                    terminalUpgradePackDetail.setAgentName(agentInfo.getAgentName());
                }
            }
        }
        page.setResult(list);
        return page;
    }

    @Transactional(rollbackFor = Throwable.class)
    public ExtResult create(int upgradePackId, String[] terminalId) {

        for (int a = 0; a < terminalId.length; a++) {
            TerminalUpgradePackDetail terminalUpgradePackDetail = terminalUpgradePackDetailMapper.find(upgradePackId, terminalId[a]);
            if (null == terminalUpgradePackDetail) {
                terminalUpgradePackDetailMapper.insert(upgradePackId, terminalId[a]);
            } else {
                continue;
                //return ExtResult.failResult("不可重复添加!");
            }
        }
        return ExtResult.successResult();

    }

    @Transactional(rollbackFor = Throwable.class)
    public ExtResult delete(int upgradePackId, String terminalId) {
        int result = terminalUpgradePackDetailMapper.delete(upgradePackId, terminalId);
        if (result == 0) {
            return ExtResult.failResult("删除失败！");
        }
        return ExtResult.successResult();
    }

}
