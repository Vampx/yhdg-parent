package cn.com.yusong.yhdg.agentserver.service.basic;

import cn.com.yusong.yhdg.common.domain.basic.MobileMessage;
import cn.com.yusong.yhdg.common.domain.basic.SmsConfigInfo;
import cn.com.yusong.yhdg.common.entity.pagination.Page;
import cn.com.yusong.yhdg.agentserver.persistence.basic.MobileMessageMapper;
import cn.com.yusong.yhdg.agentserver.service.AbstractService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MobileMessageService extends AbstractService {
    @Autowired
    MobileMessageMapper mobileMessageMapper;

    public MobileMessage find(long id) {
        MobileMessage record = mobileMessageMapper.find(id);
        if(record != null && record.getSenderId() != null) {
            SmsConfigInfo smsConfigInfo = findSmsConfigInfo(record.getSenderId());
            if(smsConfigInfo != null) {
                record.setSenderName(smsConfigInfo.getConfigName());
            }
        }
        return record;
    }

    public Page findPage(MobileMessage search) {
        List<MobileMessage> list = null;
        Page page = search.buildPage();
        page.setTotalItems(mobileMessageMapper.findPageCount(search));
        search.setBeginIndex(page.getOffset());
        page.setResult(list = mobileMessageMapper.findPageResult(search));

        for(MobileMessage e : list) {
            if(e != null && e.getSenderId() != null) {
                SmsConfigInfo smsConfigInfo = findSmsConfigInfo(e.getSenderId());
                if(smsConfigInfo != null) {
                    e.setSenderName(smsConfigInfo.getConfigName());
                }
            }
        }
        return page;
    }

    public int insert(MobileMessage mobileMessage) {
        if(StringUtils.isEmpty(mobileMessage.getMobile())) {
            throw new IllegalArgumentException();
        }

        return mobileMessageMapper.insert(mobileMessage);
    }

    public int updateStatus(long id, int status) {
        return mobileMessageMapper.updateStatus(id, status);
    }
}
