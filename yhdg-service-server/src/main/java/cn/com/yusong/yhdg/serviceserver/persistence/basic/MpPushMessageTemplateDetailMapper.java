package cn.com.yusong.yhdg.serviceserver.persistence.basic;

import cn.com.yusong.yhdg.common.domain.basic.MpPushMessageTemplateDetail;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface MpPushMessageTemplateDetailMapper extends MasterMapper {
    public List<MpPushMessageTemplateDetail> findByTemplateId(@Param("weixinmpId") int weixinmpId, @Param("templateId") int templateId);
}
