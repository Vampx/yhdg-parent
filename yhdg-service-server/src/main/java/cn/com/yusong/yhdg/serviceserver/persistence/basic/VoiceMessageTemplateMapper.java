package cn.com.yusong.yhdg.serviceserver.persistence.basic;

import cn.com.yusong.yhdg.common.domain.basic.VoiceMessageTemplate;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

public interface VoiceMessageTemplateMapper extends MasterMapper {
    VoiceMessageTemplate find(@Param("id") int id, @Param("partnerId") int partnerId);
}
