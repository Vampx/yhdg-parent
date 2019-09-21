package cn.com.yusong.yhdg.agentappserver.service.basic;

import cn.com.yusong.yhdg.agentappserver.persistence.basic.LaxinRecordMapper;
import cn.com.yusong.yhdg.agentappserver.service.AbstractService;
import cn.com.yusong.yhdg.common.domain.basic.LaxinRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class LaxinRecordService extends AbstractService {
    @Autowired
    LaxinRecordMapper laxinRecordMapper;

    public LaxinRecord find(String id) {
        return laxinRecordMapper.find(id);
    }

    public List<LaxinRecord> findByStatus(int agentId, int status, String keyword, int offset, int limit) {
        return laxinRecordMapper.findByStatus(agentId, status, keyword, offset, limit);
    }

    public List<LaxinRecord> findByOrderId(String orderId) {
        return laxinRecordMapper.findByOrderId(orderId);
    }

    public int totalMoneyByPayTime(int agentId, Date beginTime, Date endTime) {
        return laxinRecordMapper.totalMoneyByPayTime(agentId, beginTime, endTime);
    }

    public int totalCountByCreateTime(int agentId, Date beginTime, Date endTime) {
        return laxinRecordMapper.totalCountByCreateTime(agentId, beginTime, endTime);
    }

    @Transactional(rollbackFor = Throwable.class)
    public int updateCancel(String id, String cancelCanuse) {
        return laxinRecordMapper.updateCancel(id, cancelCanuse, LaxinRecord.Status.CANCEL.getValue());
    }
}
