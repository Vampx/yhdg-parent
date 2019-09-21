package cn.com.yusong.yhdg.webserver.service.basic;

import cn.com.yusong.yhdg.common.constant.Constant;
import cn.com.yusong.yhdg.common.domain.basic.*;
import cn.com.yusong.yhdg.common.entity.json.ExtResult;
import cn.com.yusong.yhdg.common.entity.pagination.Page;
import cn.com.yusong.yhdg.webserver.persistence.basic.*;
import cn.com.yusong.yhdg.webserver.utils.AppUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@Service
public class BatchWeixinmpTemplateMessageService {
    @Autowired
    BatchWeixinmpTemplateMessageMapper batchWeixinmpTemplateMessageMapper;
    @Autowired
    BatchWeixinmpTemplateMessageDetailMapper batchWeixinmpTemplateMessageDetailMapper;
    @Autowired
    CustomerMapper customerMapper;
    @Autowired
    MpPushMessageTemplateDetailMapper pushMessageTemplateDetailMapper;
    @Autowired
    WeixinmpTemplateMessageMapper weixinmpTemplateMessageMapper;


    public BatchWeixinmpTemplateMessage find(long id) {
        return batchWeixinmpTemplateMessageMapper.find(id);
    }

    public Page findPage(BatchWeixinmpTemplateMessage search) {
        Page page = search.buildPage();
        page.setTotalItems(batchWeixinmpTemplateMessageMapper.findPageCount(search));
        search.setBeginIndex(page.getOffset());
        page.setResult(batchWeixinmpTemplateMessageMapper.findPageResult(search));
        return page;
    }

    @Transactional(rollbackFor = Throwable.class)
    public ExtResult insert(BatchWeixinmpTemplateMessage template, String[] variables, String[] contents, Integer checked) throws IOException {
        Map<String, String> variableMap = new HashMap<String, String>();

        List<MpPushMessageTemplateDetail> templateDetailList = pushMessageTemplateDetailMapper.findByTemplateId(Constant.SYSTEM_PARTNER_ID, template.getTemplateId());

        int j = 0;
            for (MpPushMessageTemplateDetail p : templateDetailList) {
                String content = p.getKeywordValue();

                Pattern pattern = Pattern.compile("(\\$\\{\\w+\\})");
                Matcher matcher = pattern.matcher(content);
                while (matcher.find()) {
                    content = content.replace(matcher.group(1), contents[j++]);
                }
                variableMap.put(p.getId(), content);
            }
        String variableJson = AppUtils.encodeJson(variableMap);
        template.setContent(variableJson);

        String mobiles[] = template.getMobile().split(",");
        Set<String> mobileSet = new HashSet<String>(200);
        for (int i = 0; i < mobiles.length; i++) {
            if (mobiles[i].trim().length() == 11 && StringUtils.isNumeric(mobiles[i].trim())) {
                mobileSet.add(mobiles[i].trim());
            }
        }
        template.setCustomerCount(mobileSet.size());

        template.setVariable(StringUtils.join(variables, ","));
        batchWeixinmpTemplateMessageMapper.insert(template);

        Date now = new Date();
        for (String mobile : mobileSet) {
            BatchWeixinmpTemplateMessageDetail messageDetail = new BatchWeixinmpTemplateMessageDetail();
            messageDetail.setBatchId(template.getId());
            messageDetail.setMobile(mobile);
            Customer customer = customerMapper.findOpenId(mobile);
            if (customer.getMpOpenId() != null) {
                messageDetail.setOpenId(customer.getMpOpenId());
                batchWeixinmpTemplateMessageDetailMapper.insert(messageDetail);
            }

            WeixinmpTemplateMessage message = new WeixinmpTemplateMessage();
//            message.setSourceType(WeixinmpTemplateMessage.SourceType.CHARGER.getValue());
            message.setType(template.getTemplateId());
            message.setVariable(variableJson);
            message.setStatus(WeixinmpTemplateMessage.MessageStatus.NOT.getValue());
            message.setDelay(0);
            message.setUrl(template.getUrl());
            message.setCreateTime(new Date());
            message.setSourceId(String.valueOf(template.getTemplateId()));
            //message.setAppId(Constant.SYSTEM_PARTNER_ID);
            message.setOpenId(customer.getMpOpenId());
            message.setMobile(mobile);
            message.setNickname(customer.getNickname());
            message.setReadCount(0);
            weixinmpTemplateMessageMapper.insert(message);

        }

        return ExtResult.successResult();
    }

}
