package cn.com.yusong.yhdg.agentserver.service.hdg;

import cn.com.yusong.yhdg.common.domain.hdg.KeepOrder;
import cn.com.yusong.yhdg.common.entity.pagination.Page;
import cn.com.yusong.yhdg.agentserver.persistence.hdg.KeepOrderMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class KeepOrderService {

    @Autowired
    KeepOrderMapper keepOrderMapper;

    public Page findPage(KeepOrder search) {
        Page page = search.buildPage();
        page.setTotalItems(keepOrderMapper.findPageCount(search));
        search.setBeginIndex(page.getOffset());
        page.setResult(keepOrderMapper.findPageResult(search));
        return page;
    }

    public KeepOrder find(String id) {
        return keepOrderMapper.find(id);
    }
}
