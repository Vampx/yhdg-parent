package cn.com.yusong.yhdg.webserver.persistence.basic;

import cn.com.yusong.yhdg.common.domain.basic.VoiceMessageTemplate;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface VoiceMessageTemplateMapper extends MasterMapper {
    VoiceMessageTemplate find(@Param("partnerId") int partnerId, @Param("id") long id);

    int findPageCount(VoiceMessageTemplate search);

    List<VoiceMessageTemplate> findPageResult(VoiceMessageTemplate search);

    int update(VoiceMessageTemplate entity);
}
