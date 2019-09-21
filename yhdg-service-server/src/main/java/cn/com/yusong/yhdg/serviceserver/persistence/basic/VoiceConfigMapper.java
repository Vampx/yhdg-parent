package cn.com.yusong.yhdg.serviceserver.persistence.basic;

import cn.com.yusong.yhdg.common.domain.basic.VoiceConfig;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;

import java.util.List;

public interface VoiceConfigMapper extends MasterMapper {
    List<VoiceConfig> findByAgent(int agentId);
}
