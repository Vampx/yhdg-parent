package cn.com.yusong.yhdg.agentserver.service.hdg;

import cn.com.yusong.yhdg.common.domain.basic.AgentInfo;
import cn.com.yusong.yhdg.common.domain.basic.PushMessage;
import cn.com.yusong.yhdg.common.domain.basic.PushMetaData;
import cn.com.yusong.yhdg.common.domain.hdg.FaultFeedback;
import cn.com.yusong.yhdg.common.entity.json.ExtResult;
import cn.com.yusong.yhdg.common.entity.pagination.Page;
import cn.com.yusong.yhdg.agentserver.persistence.basic.PushMetaDataMapper;
import cn.com.yusong.yhdg.agentserver.persistence.hdg.FaultFeedbackMapper;
import cn.com.yusong.yhdg.agentserver.service.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class FaultFeedbackService extends AbstractService {

    @Autowired
    FaultFeedbackMapper faultFeedbackMapper;
    @Autowired
    PushMetaDataMapper pushMetaDataMapper;

    public FaultFeedback find(long id) {
        FaultFeedback faultFeedback = faultFeedbackMapper.find(id);
        AgentInfo agentInfo = findAgentInfo(faultFeedback.getAgentId());
        if (agentInfo != null) {
            faultFeedback.setAgentName(agentInfo.getAgentName());
        }
        return faultFeedback;
    }

    public Page findPage(FaultFeedback search) {
        Page page = search.buildPage();
        page.setTotalItems(faultFeedbackMapper.findPageCount(search));
        search.setBeginIndex(page.getOffset());
        List<FaultFeedback> feedbackList = faultFeedbackMapper.findPageResult(search);
        for (FaultFeedback faultFeedback : feedbackList) {
            AgentInfo agentInfo = findAgentInfo(faultFeedback.getAgentId());
            if (agentInfo != null) {
                faultFeedback.setAgentName(agentInfo.getAgentName());
            }
        }
        page.setResult(feedbackList);
        return page;
    }

    public ExtResult update(FaultFeedback faultFeedback) {
        FaultFeedback feedback = find(faultFeedback.getId());
        if (feedback.getHandleStatus() == FaultFeedback.HandleStatus.HANDLED.getValue()) {
            return ExtResult.failResult("此故障已处理");
        }
        faultFeedbackMapper.updateBasicInfo(faultFeedback);

        return ExtResult.successResult();
    }

    public ExtResult delete(long id) {
        int total = faultFeedbackMapper.delete(id);
        if (total > 0) {
            return ExtResult.successResult();
        } else {
            return ExtResult.failResult("删除失败");
        }
    }

    public int findFaultFeedbackCount(int faultType) {
        return faultFeedbackMapper.findFaultFeedbackCount(faultType,FaultFeedback.HandleStatus.UNHANDLED.getValue());
    }

}
