package cn.com.yusong.yhdg.agentserver.service.basic;

import cn.com.yusong.yhdg.common.domain.basic.MobileMessageTemplate;
import cn.com.yusong.yhdg.common.entity.json.ExtResult;
import cn.com.yusong.yhdg.common.entity.pagination.Page;
import cn.com.yusong.yhdg.agentserver.persistence.basic.MobileMessageTemplateMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MobileMessageTemplateService {

    @Autowired
    MobileMessageTemplateMapper mobileMessageTemplateMapper;

    public MobileMessageTemplate find(int partnerId, long id) {
        return mobileMessageTemplateMapper.find(partnerId, id);
    }

    public List<MobileMessageTemplate> findAll(){
        return mobileMessageTemplateMapper.findAll();
    }

    public Page findPage(MobileMessageTemplate search) {
        Page page = search.buildPage();
        page.setTotalItems(mobileMessageTemplateMapper.findPageCount(search));
        search.setBeginIndex(page.getOffset());
        page.setResult(mobileMessageTemplateMapper.findPageResult(search));
        return page;
    }

    public ExtResult update(MobileMessageTemplate entity) {
        if(mobileMessageTemplateMapper.update(entity) == 0) {
            return ExtResult.failResult("操作失败");
        }
        return ExtResult.successResult();
    }
}
