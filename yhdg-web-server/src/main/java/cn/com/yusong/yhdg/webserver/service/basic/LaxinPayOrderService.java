package cn.com.yusong.yhdg.webserver.service.basic;

import cn.com.yusong.yhdg.common.domain.basic.LaxinPayOrder;
import cn.com.yusong.yhdg.common.domain.basic.LaxinRecord;
import cn.com.yusong.yhdg.common.entity.pagination.Page;
import cn.com.yusong.yhdg.webserver.persistence.basic.LaxinPayOrderMapper;
import cn.com.yusong.yhdg.webserver.persistence.basic.LaxinRecordMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class LaxinPayOrderService {
    @Autowired
    LaxinPayOrderMapper laxinPayOrderMapper;

    public LaxinPayOrder find(String id) {
        return laxinPayOrderMapper.find(id);
    }

    public Page findPage(LaxinPayOrder search) {
        Page page = search.buildPage();
        page.setTotalItems(laxinPayOrderMapper.findPageCount(search));
        search.setBeginIndex(page.getOffset());
        page.setResult(laxinPayOrderMapper.findPageResult(search));
        return page;
    }
}
