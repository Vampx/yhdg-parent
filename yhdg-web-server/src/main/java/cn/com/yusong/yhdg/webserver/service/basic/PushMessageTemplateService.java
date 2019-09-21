package cn.com.yusong.yhdg.webserver.service.basic;

import cn.com.yusong.yhdg.common.domain.basic.PushMessageTemplate;
import cn.com.yusong.yhdg.common.entity.json.ExtResult;
import cn.com.yusong.yhdg.common.entity.pagination.Page;
import cn.com.yusong.yhdg.webserver.persistence.basic.PushMessageTemplateMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PushMessageTemplateService {

    @Autowired
    PushMessageTemplateMapper pushMessageTemplateMapper;

    public PushMessageTemplate find(long id) {
        return pushMessageTemplateMapper.find(id);
    }

    public Page findPage(PushMessageTemplate search) {
        Page page = search.buildPage();
        page.setTotalItems(pushMessageTemplateMapper.findPageCount(search));
        search.setBeginIndex(page.getOffset());
        page.setResult(pushMessageTemplateMapper.findPageResult(search));
        return page;
    }

    public ExtResult update(PushMessageTemplate entity) {
        if(pushMessageTemplateMapper.update(entity) == 0) {
            return ExtResult.failResult("操作失败");
        }
        return ExtResult.successResult();
    }
}
