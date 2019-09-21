package cn.com.yusong.yhdg.serviceserver.persistence.basic;

import cn.com.yusong.yhdg.common.domain.basic.FwPushMessageTemplateDetail;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface FwPushMessageTemplateDetailMapper extends MasterMapper {
    public List<FwPushMessageTemplateDetail> findByTemplateId(@Param("alipayfwId") int alipayfwId, @Param("templateId") int templateId);
}
