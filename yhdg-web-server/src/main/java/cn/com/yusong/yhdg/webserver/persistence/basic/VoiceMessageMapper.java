package cn.com.yusong.yhdg.webserver.persistence.basic;

import cn.com.yusong.yhdg.common.domain.basic.VoiceMessage;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;

import java.util.List;

public interface VoiceMessageMapper extends MasterMapper {
    VoiceMessage find(long id);

    int findPageCount(VoiceMessage search);

    List<VoiceMessage> findPageResult(VoiceMessage search);
}
