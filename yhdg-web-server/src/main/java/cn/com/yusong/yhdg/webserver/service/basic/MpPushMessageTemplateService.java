package cn.com.yusong.yhdg.webserver.service.basic;

import cn.com.yusong.yhdg.common.domain.basic.*;
import cn.com.yusong.yhdg.common.entity.json.DataResult;
import cn.com.yusong.yhdg.common.entity.json.ExtResult;
import cn.com.yusong.yhdg.common.entity.pagination.Page;
import cn.com.yusong.yhdg.common.utils.YhdgUtils;
import cn.com.yusong.yhdg.webserver.persistence.basic.*;
import cn.com.yusong.yhdg.webserver.service.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@Service
public class MpPushMessageTemplateService extends AbstractService {
    @Autowired
    MpPushMessageTemplateMapper mpPushMessageTemplateMapper;
    @Autowired
    MpPushMessageTemplateDetailMapper mpPushMessageTemplateDetailMapper;
    @Autowired
    private WeixinmpMapper weixinmpMapper;
    @Autowired
    UserMpPushMessageTemplateMapper userMpPushMessageTemplateMapper;
    @Autowired
    UserMapper userMapper;
    @Autowired
    AgentMapper agentMapper;

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
        List<MpPushMessageTemplate> list = mpPushMessageTemplateMapper.findPageResult(search);
        for (MpPushMessageTemplate mpPushMessageTemplate : list) {
            Weixinmp weixinmp = weixinmpMapper.find(mpPushMessageTemplate.getWeixinmpId());
            if(weixinmp != null){
                mpPushMessageTemplate.setWeixinmpName(weixinmp.getAppName());
            }
        }
        page.setResult(list);
        return page;
    }

    public ExtResult update(String data) throws IOException, ParseException {
        Map dataMap = (Map) YhdgUtils.decodeJson(data,Map.class);
        if(dataMap == null){
            return ExtResult.failResult("传参为空！");
        }
        MpPushMessageTemplate mpPushMessageTemplate = new MpPushMessageTemplate();
        Integer id = Integer.parseInt(dataMap.get("id").toString());
        Integer weixinmpId = Integer.parseInt(dataMap.get("weixinmpId").toString());
        String variable = dataMap.get("variable").toString();
        String mpCode = dataMap.get("mpCode").toString();
        Integer isActive = Integer.parseInt(dataMap.get("isActive").toString());
        mpPushMessageTemplate.setId(id);
        mpPushMessageTemplate.setWeixinmpId(weixinmpId);
        mpPushMessageTemplate.setVariable(variable);
        mpPushMessageTemplate.setVariable(variable);
        mpPushMessageTemplate.setMpCode(mpCode);
        mpPushMessageTemplate.setIsActive(isActive);

        List<Map> detailList = (List) dataMap.get("detailList");
        List<MpPushMessageTemplateDetail> mpPushMessageTemplateDetailList = new ArrayList<MpPushMessageTemplateDetail>();
        for (Map detail : detailList) {
            MpPushMessageTemplateDetail mpPushMessageTemplateDetail = new MpPushMessageTemplateDetail();
            String detailId = detail.get("detailId").toString();
            Integer templateId = Integer.parseInt(detail.get("templateId").toString());
            Integer templateWeixinmpId = Integer.parseInt(detail.get("weixinmpId").toString());
            String keywordValue = detail.get("keywordValue").toString();
            String color = detail.get("color").toString();
            mpPushMessageTemplateDetail.setId(detailId);
            mpPushMessageTemplateDetail.setTemplateId(templateId);
            mpPushMessageTemplateDetail.setWeixinmpId(templateWeixinmpId);
            mpPushMessageTemplateDetail.setKeywordValue(keywordValue);
            mpPushMessageTemplateDetail.setColor(color);
            mpPushMessageTemplateDetailList.add(mpPushMessageTemplateDetail);
        }
        mpPushMessageTemplateMapper.update(mpPushMessageTemplate);
        for (MpPushMessageTemplateDetail mpPushMessageTemplateDetail : mpPushMessageTemplateDetailList) {
            mpPushMessageTemplateDetailMapper.update(mpPushMessageTemplateDetail);
        }
        return ExtResult.successResult();
    }

    public DataResult insert(MpPushMessageTemplate template) {
        if (mpPushMessageTemplateMapper.insert(template) == 0) {
            return DataResult.failResult("添加失败！", null);
        } else {
            return DataResult.successResult(template.getId());
        }
    }

    public Page findByUserPage(MpPushMessageTemplate search) {
        search.setReceiver("运营商");
        User user = userMapper.find(search.getUserId());
        search.setWeixinmpId(agentMapper.find(user.getAgentId()).getWeixinmpId());
        Page page = search.buildPage();
        List<MpPushMessageTemplate> list = mpPushMessageTemplateMapper.findByUserPageResult(search);
        for (MpPushMessageTemplate mpPushMessageTemplate : list) {
            mpPushMessageTemplate.setId(mpPushMessageTemplate.getId()+mpPushMessageTemplate.getWeixinmpId());
            Weixinmp weixinmp = weixinmpMapper.find(mpPushMessageTemplate.getWeixinmpId());
            if(weixinmp != null){
                mpPushMessageTemplate.setWeixinmpName(weixinmp.getAppName());
            }
        }
        page.setResult(list);
        return page;
    }

    @Transactional(rollbackFor = Throwable.class)
    public ExtResult updateUserMpPush(String[] idList, long userId) {
        userMpPushMessageTemplateMapper.deleteByUser(userId);
        if(null != idList) {
            for (String ids : idList) {
                String[] id= ids.split("_");
                MpPushMessageTemplate resout = mpPushMessageTemplateMapper.find(Integer.parseInt(id[1]),Integer.parseInt(id[0])-Integer.parseInt(id[1]));
                UserMpPushMessageTemplate userMpPushMessageTemplate = new UserMpPushMessageTemplate();
                userMpPushMessageTemplate.setId(resout.getId());
                userMpPushMessageTemplate.setWeixinmpId(resout.getWeixinmpId());
                userMpPushMessageTemplate.setIsActive(resout.getIsActive());
                userMpPushMessageTemplate.setUserId(userId);
                userMpPushMessageTemplateMapper.insert(userMpPushMessageTemplate);
            }
        }
        return ExtResult.successResult();
    }

}
