package cn.com.yusong.yhdg.appserver.service.basic;

import cn.com.yusong.yhdg.appserver.persistence.basic.LaxinRecordMapper;
import cn.com.yusong.yhdg.common.domain.basic.LaxinRecord;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class LaxinRecordService {
    @Autowired
    LaxinRecordMapper laxinRecordMapper;

    public List<LaxinRecord> findList (long laxinId, int status, int offset, int limit) {
        return laxinRecordMapper.findList(laxinId, status, offset, limit);
    }

    public int totalMoneyByTransferTime(long laxinId, int status, Date beginTime, Date endTime) {
        return laxinRecordMapper.totalMoneyByTransferTime(laxinId, status, beginTime, endTime);
    }

    public int totalCountByTransferTime(long laxinId, int status, Date beginTime, Date endTime) {
        return laxinRecordMapper.totalCountByTransferTime(laxinId, status, beginTime, endTime);
    }

    public int totalMoneyByStatus(long laxinId, int[] status) {
        return laxinRecordMapper.totalMoneyByStatus(laxinId, status);
    }

}
