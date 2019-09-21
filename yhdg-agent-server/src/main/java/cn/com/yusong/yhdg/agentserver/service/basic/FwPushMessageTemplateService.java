package cn.com.yusong.yhdg.agentserver.service.basic;

import cn.com.yusong.yhdg.common.domain.basic.FwPushMessageTemplate;
import cn.com.yusong.yhdg.common.entity.json.DataResult;
import cn.com.yusong.yhdg.common.entity.json.ExtResult;
import cn.com.yusong.yhdg.common.entity.pagination.Page;
import cn.com.yusong.yhdg.agentserver.persistence.basic.FwPushMessageTemplateMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class FwPushMessageTemplateService {
    @Autowired
    FwPushMessageTemplateMapper fwPushMessageTemplateMapper;

    public FwPushMessageTemplate find(int appId, int id) {
        return fwPushMessageTemplateMapper.find(appId, id);
    }

    public List<FwPushMessageTemplate> findByAppId(int appId) {
        return fwPushMessageTemplateMapper.findByAppId(appId);
    }

    public Page findPage(FwPushMessageTemplate search) {
        Page page = search.buildPage();
        page.setTotalItems(fwPushMessageTemplateMapper.findPageCount(search));
        search.setBeginIndex(page.getOffset());
        page.setResult(fwPushMessageTemplateMapper.findPageResult(search));
        return page;
    }

    public ExtResult update(Integer id, Integer appId, String templateName, String variable, String fwCode, Integer isActive, String memo) {
        if (fwPushMessageTemplateMapper.update(id, appId, templateName, variable, fwCode, isActive, memo) == 0) {
            return ExtResult.failResult("操作失败！");
        } else {
            return ExtResult.successResult();
        }
    }

    public DataResult insert(FwPushMessageTemplate template) {
        if (fwPushMessageTemplateMapper.insert(template) == 0) {
            return DataResult.failResult("添加失败！", null);
        } else {
            return DataResult.successResult(template.getId());
        }
    }

}
