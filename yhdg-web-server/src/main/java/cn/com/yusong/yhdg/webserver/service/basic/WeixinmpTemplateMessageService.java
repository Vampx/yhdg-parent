package cn.com.yusong.yhdg.webserver.service.basic;

import cn.com.yusong.yhdg.common.domain.basic.WeixinmpTemplateMessage;
import cn.com.yusong.yhdg.common.entity.pagination.Page;
import cn.com.yusong.yhdg.webserver.persistence.basic.WeixinmpTemplateMessageMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;


@Service
public class WeixinmpTemplateMessageService {

    @Autowired
    private WeixinmpTemplateMessageMapper weixinmpTemplateMessageMapper;

    public WeixinmpTemplateMessage find(int id){
        return weixinmpTemplateMessageMapper.find(id);
    }

    public Page findPage(WeixinmpTemplateMessage search){
        Page page = search.buildPage();
        page.setTotalItems(weixinmpTemplateMessageMapper.findPageCount(search));
        search.setBeginIndex(page.getOffset());
        page.setResult(weixinmpTemplateMessageMapper.findPageResult(search));
        return page;
    }

    public int update(WeixinmpTemplateMessage entity){
        return weixinmpTemplateMessageMapper.update(entity);
    }

    public int findCount(Date beginTime, Date endTime, String variable) {
        return weixinmpTemplateMessageMapper.findCount(beginTime, endTime, variable);
    }
}
