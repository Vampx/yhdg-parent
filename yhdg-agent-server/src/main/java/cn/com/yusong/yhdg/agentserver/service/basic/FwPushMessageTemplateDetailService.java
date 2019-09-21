package cn.com.yusong.yhdg.agentserver.service.basic;

import cn.com.yusong.yhdg.common.domain.basic.FwPushMessageTemplateDetail;
import cn.com.yusong.yhdg.common.entity.json.ExtResult;
import cn.com.yusong.yhdg.common.entity.pagination.Page;
import cn.com.yusong.yhdg.agentserver.persistence.basic.FwPushMessageTemplateDetailMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class FwPushMessageTemplateDetailService {
    @Autowired
    FwPushMessageTemplateDetailMapper fwPushMessageTemplateDetailMapper;


    public FwPushMessageTemplateDetail find(String id, int appId, int templateId){
        return fwPushMessageTemplateDetailMapper.find(id, appId, templateId);
    }

    public List<FwPushMessageTemplateDetail> findByTemplateId(int appId, int templateId){
        return fwPushMessageTemplateDetailMapper.findByTemplateId(appId, templateId);
    }

    public Page findPage(FwPushMessageTemplateDetail search) {
        Page page = search.buildPage();
        page.setTotalItems(fwPushMessageTemplateDetailMapper.findPageCount(search));
        search.setBeginIndex(page.getOffset());
        page.setResult(fwPushMessageTemplateDetailMapper.findPageResult(search));
        return page;
    }

    public ExtResult update(FwPushMessageTemplateDetail detail) {
        if(fwPushMessageTemplateDetailMapper.update(detail)  == 0){
            return ExtResult.failResult("操作失败！");
        } else {
            return ExtResult.successResult();
        }
    }

    public ExtResult insert(FwPushMessageTemplateDetail detail) {
        if(fwPushMessageTemplateDetailMapper.insert(detail)  == 0){
            return ExtResult.failResult("添加失败！");
        } else {
            return ExtResult.successResult();
        }
    }

}
