package cn.com.yusong.yhdg.agentserver.service.basic;

import cn.com.yusong.yhdg.common.domain.basic.BatchMobileMessage;
import cn.com.yusong.yhdg.common.domain.basic.BatchMobileMessageDetail;
import cn.com.yusong.yhdg.common.domain.basic.MobileMessage;
import cn.com.yusong.yhdg.common.entity.json.ExtResult;
import cn.com.yusong.yhdg.common.entity.pagination.Page;
import cn.com.yusong.yhdg.agentserver.persistence.basic.BatchMobileMessageDetailMapper;
import cn.com.yusong.yhdg.agentserver.persistence.basic.BatchMobileMessageMapper;
import cn.com.yusong.yhdg.agentserver.persistence.basic.MobileMessageMapper;
import cn.com.yusong.yhdg.agentserver.persistence.basic.MobileMessageTemplateMapper;
import cn.com.yusong.yhdg.agentserver.utils.AppUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * Created by chen on 2017/10/30.
 */
@Service
public class BatchMobileMessageService {
    @Autowired
    BatchMobileMessageMapper batchMobileMessageMapper;
    @Autowired
    BatchMobileMessageDetailMapper batchMobileMessageDetailMapper;
    @Autowired
    MobileMessageMapper mobileMessageMapper;
    @Autowired
    MobileMessageTemplateMapper mobileMessageTemplateMapper;

    public BatchMobileMessage find(int id) {
        return batchMobileMessageMapper.find(id);
    }

    public Page findPage(BatchMobileMessage search) {
        Page page = search.buildPage();
        page.setTotalItems(batchMobileMessageMapper.findPageCount(search));
        search.setBeginIndex(page.getOffset());
        page.setResult(batchMobileMessageMapper.findPageResult(search));
        return page;
    }

    @Transactional(rollbackFor=Throwable.class)
    public ExtResult insert(BatchMobileMessage entity, String [] variables, String [] contents) {
        Map<String, String> variableMap = new HashMap<String, String>();
        String content = entity.getContent();

        for(int i = 0; i < variables.length; i++) {
            content = StringUtils.replace(content, "${" + variables[i] + "}", contents[i]);
            variableMap.put(variables[i], contents[i]);
        }

        entity.setContent(content);
        String mobiles[] = entity.getMobile().split(",");
        Set<String> mobileSet = new HashSet<String>(200);
        for(int i = 0; i < mobiles.length; i++) {
            if(mobiles[i].trim().length() == 11 && StringUtils.isNumeric(mobiles[i].trim())) {
                mobileSet.add(mobiles[i].trim());
            }
        }
        entity.setMobileCount(mobileSet.size());

        entity.setVariable(StringUtils.join(variables, ","));
        batchMobileMessageMapper.insert(entity);

        String variableJson = AppUtils.encodeJson2(variableMap);
        String templateCode = mobileMessageTemplateMapper.find(0, entity.getTemplateId()).getCode();

        Date now = new Date();
        for (String mobile : mobileSet) {
            BatchMobileMessageDetail messageDetail = new BatchMobileMessageDetail();
            messageDetail.setBatchId(entity.getId());
            messageDetail.setMobile(mobile);
            batchMobileMessageDetailMapper.insert(messageDetail);

            MobileMessage mobileMessage = new MobileMessage();
            mobileMessage.setMobile(mobile);
            mobileMessage.setVariable(variableJson);
            mobileMessage.setContent(content);
            mobileMessage.setCreateTime(now);
            mobileMessage.setStatus(MobileMessage.MessageStatus.NOT.getValue());
            mobileMessage.setType(entity.getId());
            mobileMessage.setDelay(0);
            mobileMessage.setTemplateCode(templateCode);
            mobileMessage.setSourceType(0);
            mobileMessageMapper.insert(mobileMessage);
        }

        return ExtResult.successResult();
    }
}
