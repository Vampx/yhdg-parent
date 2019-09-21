package cn.com.yusong.yhdg.agentappserver.service.basic;

import cn.com.yusong.yhdg.agentappserver.persistence.basic.IdCardAuthRecordMapper;
import cn.com.yusong.yhdg.common.domain.basic.IdCardAuthRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class IdCardAuthRecordService {
    @Autowired
    IdCardAuthRecordMapper idCardAuthRecordMapper;

    public List<IdCardAuthRecord> queryByDate(Integer agentId,
                                              String keyword,
                                              String date,
                                              Integer offset,
                                              Integer limit) {
        return idCardAuthRecordMapper.queryByDate(agentId, keyword, date, offset, limit);

    }
}
