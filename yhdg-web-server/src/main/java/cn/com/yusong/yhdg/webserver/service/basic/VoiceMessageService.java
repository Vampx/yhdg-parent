package cn.com.yusong.yhdg.webserver.service.basic;

import cn.com.yusong.yhdg.common.domain.basic.VoiceMessage;
import cn.com.yusong.yhdg.common.entity.pagination.Page;
import cn.com.yusong.yhdg.webserver.persistence.basic.VoiceMessageMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VoiceMessageService {
    @Autowired
    private VoiceMessageMapper voiceMessageMapper;

    public VoiceMessage find(long id) {
        VoiceMessage record = voiceMessageMapper.find(id);
        return record;
    }

    public Page findPage(VoiceMessage search) {
        Page page = search.buildPage();
        page.setTotalItems(voiceMessageMapper.findPageCount(search));
        search.setBeginIndex(page.getOffset());
        List<VoiceMessage> list = voiceMessageMapper.findPageResult(search);
        page.setResult(list);
        return page;
    }
}
