package cn.com.yusong.yhdg.agentserver.service.basic;

import cn.com.yusong.yhdg.common.domain.basic.LaxinRecordTransferLog;
import cn.com.yusong.yhdg.common.entity.pagination.Page;
import cn.com.yusong.yhdg.agentserver.persistence.basic.LaxinRecordTransferLogMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LaxinRecordTransferLogService {
    @Autowired
    LaxinRecordTransferLogMapper laxinRecordTransferLogMapper;


    public LaxinRecordTransferLog find(long id) {
        return laxinRecordTransferLogMapper.find(id);
    }

    public Page findPage(LaxinRecordTransferLog search) {
        Page page = search.buildPage();
        page.setTotalItems(laxinRecordTransferLogMapper.findPageCount(search));
        search.setBeginIndex(page.getOffset());
        page.setResult(laxinRecordTransferLogMapper.findPageResult(search));
        return page;
    }
}
