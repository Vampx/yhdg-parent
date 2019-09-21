package cn.com.yusong.yhdg.webserver.service.basic;

import cn.com.yusong.yhdg.common.domain.basic.FwPushMessageTemplate;
import cn.com.yusong.yhdg.common.domain.basic.FwPushMessageTemplateDetail;
import cn.com.yusong.yhdg.common.domain.basic.MpPushMessageTemplate;
import cn.com.yusong.yhdg.common.domain.basic.MpPushMessageTemplateDetail;
import cn.com.yusong.yhdg.common.entity.json.DataResult;
import cn.com.yusong.yhdg.common.entity.json.ExtResult;
import cn.com.yusong.yhdg.common.entity.pagination.Page;
import cn.com.yusong.yhdg.common.utils.YhdgUtils;
import cn.com.yusong.yhdg.webserver.persistence.basic.FwPushMessageTemplateDetailMapper;
import cn.com.yusong.yhdg.webserver.persistence.basic.FwPushMessageTemplateMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@Service
public class FwPushMessageTemplateService {
    @Autowired
    FwPushMessageTemplateMapper fwPushMessageTemplateMapper;
    @Autowired
    FwPushMessageTemplateDetailMapper fwPushMessageTemplateDetailMapper;

    public FwPushMessageTemplate find(int alipayfwId, int id) {
        return fwPushMessageTemplateMapper.find(alipayfwId, id);
    }

    public Page findPage(FwPushMessageTemplate search) {
        Page page = search.buildPage();
        page.setTotalItems(fwPushMessageTemplateMapper.findPageCount(search));
        search.setBeginIndex(page.getOffset());
        page.setResult(fwPushMessageTemplateMapper.findPageResult(search));
        return page;
    }

    public ExtResult update(String data) throws IOException, ParseException {
        Map dataMap = (Map) YhdgUtils.decodeJson(data,Map.class);
        if(dataMap == null){
            return ExtResult.failResult("传参为空！");
        }
        FwPushMessageTemplate fwPushMessageTemplate = new FwPushMessageTemplate();
        Integer id = Integer.parseInt(dataMap.get("id").toString());
        Integer alipayfwId = Integer.parseInt(dataMap.get("alipayfwId").toString());
        String variable = dataMap.get("variable").toString();
        String fwCode = dataMap.get("fwCode").toString();
        Integer isActive = Integer.parseInt(dataMap.get("isActive").toString());
        fwPushMessageTemplate.setId(id);
        fwPushMessageTemplate.setAlipayfwId(alipayfwId);
        fwPushMessageTemplate.setVariable(variable);
        fwPushMessageTemplate.setVariable(variable);
        fwPushMessageTemplate.setFwCode(fwCode);
        fwPushMessageTemplate.setIsActive(isActive);

        List<Map> detailList = (List) dataMap.get("detailList");
        List<FwPushMessageTemplateDetail> fwPushMessageTemplateDetailList = new ArrayList<FwPushMessageTemplateDetail>();
        for (Map detail : detailList) {
            FwPushMessageTemplateDetail fwPushMessageTemplateDetail = new FwPushMessageTemplateDetail();
            String detailId = detail.get("detailId").toString();
            Integer templateId = Integer.parseInt(detail.get("templateId").toString());
            Integer templateAlipayfwId = Integer.parseInt(detail.get("alipayfwId").toString());
            String keywordValue = detail.get("keywordValue").toString();
            String color = detail.get("color").toString();
            fwPushMessageTemplateDetail.setId(detailId);
            fwPushMessageTemplateDetail.setTemplateId(templateId);
            fwPushMessageTemplateDetail.setAlipayfwId(templateAlipayfwId);
            fwPushMessageTemplateDetail.setKeywordValue(keywordValue);
            fwPushMessageTemplateDetail.setColor(color);
            fwPushMessageTemplateDetailList.add(fwPushMessageTemplateDetail);
        }
        fwPushMessageTemplateMapper.update(fwPushMessageTemplate);
        for (FwPushMessageTemplateDetail fwPushMessageTemplateDetail : fwPushMessageTemplateDetailList) {
            fwPushMessageTemplateDetailMapper.update(fwPushMessageTemplateDetail);
        }
        return ExtResult.successResult();
    }

    public DataResult insert(FwPushMessageTemplate template) {
        if (fwPushMessageTemplateMapper.insert(template) == 0) {
            return DataResult.failResult("添加失败！", null);
        } else {
            return DataResult.successResult(template.getId());
        }
    }

}
