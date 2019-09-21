package cn.com.yusong.yhdg.agentserver.service.basic;

import cn.com.yusong.yhdg.common.domain.basic.MpPushMessageTemplateDetail;
import cn.com.yusong.yhdg.common.entity.json.ExtResult;
import cn.com.yusong.yhdg.common.entity.pagination.Page;
import cn.com.yusong.yhdg.agentserver.persistence.basic.MpPushMessageTemplateDetailMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class MpPushMessageTemplateDetailService {
    @Autowired
    MpPushMessageTemplateDetailMapper mpPushMessageTemplateDetailMapper;


    public MpPushMessageTemplateDetail find(String id, int weixinmpId, int templateId) {
        return mpPushMessageTemplateDetailMapper.find(id, weixinmpId, templateId);
    }

    public List<MpPushMessageTemplateDetail> findByTemplateId(int weixinmpId, int templateId) {
        return mpPushMessageTemplateDetailMapper.findByTemplateId(weixinmpId, templateId);
    }

    public Page findPage(MpPushMessageTemplateDetail search) {
        Page page = search.buildPage();
        page.setTotalItems(mpPushMessageTemplateDetailMapper.findPageCount(search));
        search.setBeginIndex(page.getOffset());
        page.setResult(mpPushMessageTemplateDetailMapper.findPageResult(search));
        return page;
    }

    public ExtResult update(MpPushMessageTemplateDetail detail) {
        if (mpPushMessageTemplateDetailMapper.update(detail) == 0) {
            return ExtResult.failResult("操作失败！");
        } else {
            return ExtResult.successResult();
        }
    }

    public ExtResult insert(MpPushMessageTemplateDetail detail) {
        if (mpPushMessageTemplateDetailMapper.insert(detail) == 0) {
            return ExtResult.failResult("添加失败！");
        } else {
            return ExtResult.successResult();
        }
    }

}
