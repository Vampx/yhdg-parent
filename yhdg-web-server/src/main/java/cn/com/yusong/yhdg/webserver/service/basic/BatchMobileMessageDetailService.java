package cn.com.yusong.yhdg.webserver.service.basic;

import cn.com.yusong.yhdg.common.domain.basic.BatchMobileMessageDetail;
import cn.com.yusong.yhdg.common.entity.pagination.Page;
import cn.com.yusong.yhdg.webserver.persistence.basic.BatchMobileMessageDetailMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by chen on 2017/10/30.
 */
@Service
public class BatchMobileMessageDetailService {
    @Autowired
    BatchMobileMessageDetailMapper batchMobileMessageDetailMapper;


    public Page findPage(BatchMobileMessageDetail search) {
        Page page = search.buildPage();
        page.setTotalItems(batchMobileMessageDetailMapper.findPageCount(search));
        search.setBeginIndex(page.getOffset());
        page.setResult(batchMobileMessageDetailMapper.findPageResult(search));
        return page;
    }
}
