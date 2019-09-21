package cn.com.yusong.yhdg.appserver.persistence.basic;

import cn.com.yusong.yhdg.common.domain.basic.MpPushMessageTemplateDetail;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface AppPushMessageTemplateDetailMapper extends MasterMapper {
    public List<MpPushMessageTemplateDetail> findByTemplateId(@Param("agentId") int agentId, @Param("templateId") int templateId);
}
