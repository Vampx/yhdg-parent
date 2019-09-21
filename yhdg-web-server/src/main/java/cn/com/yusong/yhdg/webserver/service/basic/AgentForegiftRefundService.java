package cn.com.yusong.yhdg.webserver.service.basic;

import cn.com.yusong.yhdg.common.domain.basic.AgentBatteryType;
import cn.com.yusong.yhdg.common.domain.basic.AgentForegiftRefund;
import cn.com.yusong.yhdg.common.domain.basic.AgentInfo;
import cn.com.yusong.yhdg.common.domain.basic.Partner;
import cn.com.yusong.yhdg.common.domain.hdg.AgentCabinetRentRecord;
import cn.com.yusong.yhdg.common.domain.hdg.InsuranceOrder;
import cn.com.yusong.yhdg.common.entity.pagination.Page;
import cn.com.yusong.yhdg.common.entity.pagination.PageRequest;
import cn.com.yusong.yhdg.webserver.persistence.basic.AgentForegiftRefundMapper;
import cn.com.yusong.yhdg.webserver.service.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AgentForegiftRefundService extends AbstractService {

    @Autowired
    private AgentForegiftRefundMapper agentForegiftRefundMapper;

    public Page findPage(AgentForegiftRefund agentForegiftRefund) {
        PageRequest pageRequest = new PageRequest(agentForegiftRefund.getPage(), agentForegiftRefund.getRows());
        Page page = new Page(pageRequest);
        page.setTotalItems(agentForegiftRefundMapper.findPageCount(agentForegiftRefund));
        agentForegiftRefund.setBeginIndex(page.getOffset());
        List<AgentForegiftRefund> agentForegiftRefundList = agentForegiftRefundMapper.findPageResult(agentForegiftRefund);
        page.setResult(agentForegiftRefundList);
        return page;
    }
}
