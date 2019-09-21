package cn.com.yusong.yhdg.webserver.service.basic;

import cn.com.yusong.yhdg.common.domain.basic.PushMessage;
import cn.com.yusong.yhdg.common.entity.pagination.Page;
import cn.com.yusong.yhdg.webserver.persistence.basic.PushMessageMapper;
import cn.com.yusong.yhdg.webserver.service.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PushMessageService extends AbstractService {
    @Autowired
    PushMessageMapper pushMessageMapper;

    public PushMessage find(long id) {
        return pushMessageMapper.find(id);
    }

    public Page findPage(PushMessage search) {
        Page page = search.buildPage();
        page.setTotalItems(pushMessageMapper.findPageCount(search));
        search.setBeginIndex(page.getOffset());
        List<PushMessage> pushMessageList = pushMessageMapper.findPageResult(search);
        page.setResult(pushMessageList);
        return page;
    }

}
