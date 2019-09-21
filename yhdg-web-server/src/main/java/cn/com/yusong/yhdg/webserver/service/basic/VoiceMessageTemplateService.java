package cn.com.yusong.yhdg.webserver.service.basic;

import cn.com.yusong.yhdg.common.domain.basic.Partner;
import cn.com.yusong.yhdg.common.domain.basic.VoiceMessageTemplate;
import cn.com.yusong.yhdg.common.entity.json.ExtResult;
import cn.com.yusong.yhdg.common.entity.pagination.Page;
import cn.com.yusong.yhdg.webserver.persistence.basic.PartnerMapper;
import cn.com.yusong.yhdg.webserver.persistence.basic.VoiceMessageTemplateMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VoiceMessageTemplateService {
    @Autowired
    private VoiceMessageTemplateMapper voiceMessageTemplateMapper;
    @Autowired
    private PartnerMapper partnerMapper;

    public VoiceMessageTemplate find(int partnerId, long id) {
        VoiceMessageTemplate voiceMessageTemplate = voiceMessageTemplateMapper.find(partnerId, id);
        if (voiceMessageTemplate != null) {
            Partner partner = partnerMapper.find(voiceMessageTemplate.getPartnerId());
            voiceMessageTemplate.setPartnerName(partner.getPartnerName());
        }

        return voiceMessageTemplate;
    }

    public Page findPage(VoiceMessageTemplate search) {
        Page page = search.buildPage();
        page.setTotalItems(voiceMessageTemplateMapper.findPageCount(search));
        search.setBeginIndex(page.getOffset());
        page.setResult(voiceMessageTemplateMapper.findPageResult(search));
        return page;
    }

    public ExtResult update(VoiceMessageTemplate entity) {
        if(voiceMessageTemplateMapper.update(entity) == 0) {
            return ExtResult.failResult("操作失败");
        }
        return ExtResult.successResult();
    }

}
