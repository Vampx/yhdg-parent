package cn.com.yusong.yhdg.agentserver.service.basic;

import cn.com.yusong.yhdg.common.domain.basic.BatchWeixinmpTemplateMessageDetail;
import cn.com.yusong.yhdg.common.entity.pagination.Page;
import cn.com.yusong.yhdg.agentserver.persistence.basic.BatchWeixinmpTemplateMessageDetailMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class BatchWeixinmpTemplateMessageDetailService {
    @Autowired
    BatchWeixinmpTemplateMessageDetailMapper batchWeixinmpTemplateMessageDetailMapper;

    public Page findPage(BatchWeixinmpTemplateMessageDetail search) {
        Page page = search.buildPage();
        page.setTotalItems(batchWeixinmpTemplateMessageDetailMapper.findPageCount(search));
        search.setBeginIndex(page.getOffset());
        page.setResult(batchWeixinmpTemplateMessageDetailMapper.findPageResult(search));
        return page;
    }
}
