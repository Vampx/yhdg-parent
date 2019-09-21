package cn.com.yusong.yhdg.agentserver.service.basic;

import cn.com.yusong.yhdg.common.domain.basic.AlipayfwTemplateMessage;
import cn.com.yusong.yhdg.common.entity.pagination.Page;
import cn.com.yusong.yhdg.agentserver.persistence.basic.AlipayfwTemplateMessageMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class AlipayfwTemplateMessageService {
    @Autowired
    AlipayfwTemplateMessageMapper alipayfwTemplateMessageMapper;

    public AlipayfwTemplateMessage find(Long id){
        return alipayfwTemplateMessageMapper.find(id);
    }

    public Page findPage(AlipayfwTemplateMessage search){
        Page page = search.buildPage();
        page.setTotalItems(alipayfwTemplateMessageMapper.findPageCount(search));
        search.setBeginIndex(page.getOffset());
        page.setResult(alipayfwTemplateMessageMapper.findPageResult(search));
        return page;
    }

}
