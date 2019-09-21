package cn.com.yusong.yhdg.agentserver.service.basic;

import cn.com.yusong.yhdg.common.domain.basic.MpPushMessageTemplate;
import cn.com.yusong.yhdg.common.entity.json.DataResult;
import cn.com.yusong.yhdg.common.entity.json.ExtResult;
import cn.com.yusong.yhdg.common.entity.pagination.Page;
import cn.com.yusong.yhdg.agentserver.persistence.basic.MpPushMessageTemplateMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class MpPushMessageTemplateService {
    @Autowired
    MpPushMessageTemplateMapper mpPushMessageTemplateMapper;

    public MpPushMessageTemplate find(int weixinmpId, int id) {
        return mpPushMessageTemplateMapper.find(weixinmpId, id);
    }

    public List<MpPushMessageTemplate> findByWeixinmpId(int weixinmpId) {
        return mpPushMessageTemplateMapper.findByWeixinmpId(weixinmpId);
    }

    public Page findPage(MpPushMessageTemplate search) {
        Page page = search.buildPage();
        page.setTotalItems(mpPushMessageTemplateMapper.findPageCount(search));
        search.setBeginIndex(page.getOffset());
        page.setResult(mpPushMessageTemplateMapper.findPageResult(search));
        return page;
    }

    public ExtResult update(Integer id, Integer weixinmpId, String templateName, String variable, String mpCode, Integer isActive, String memo) {
        if (mpPushMessageTemplateMapper.update(id, weixinmpId, templateName, variable, mpCode, isActive, memo) == 0) {
            return ExtResult.failResult("操作失败！");
        } else {
            return ExtResult.successResult();
        }
    }

    public DataResult insert(MpPushMessageTemplate template) {
        if (mpPushMessageTemplateMapper.insert(template) == 0) {
            return DataResult.failResult("添加失败！", null);
        } else {
            return DataResult.successResult(template.getId());
        }
    }

}
