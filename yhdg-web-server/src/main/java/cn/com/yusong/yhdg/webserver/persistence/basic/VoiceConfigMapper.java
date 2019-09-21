package cn.com.yusong.yhdg.webserver.persistence.basic;

import cn.com.yusong.yhdg.common.domain.basic.VoiceConfig;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;

import java.util.List;

public interface VoiceConfigMapper extends MasterMapper {
    VoiceConfig find(int id);

    int findPageCount(VoiceConfig search);

    List<VoiceConfig> findPageResult(VoiceConfig search);

    int insert(VoiceConfig entity);

    int update(VoiceConfig entity);
}
