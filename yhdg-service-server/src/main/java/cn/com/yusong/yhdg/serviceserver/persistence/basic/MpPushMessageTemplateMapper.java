package cn.com.yusong.yhdg.serviceserver.persistence.basic;

import cn.com.yusong.yhdg.common.domain.basic.MpPushMessageTemplate;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

public interface MpPushMessageTemplateMapper extends MasterMapper {
    public MpPushMessageTemplate find(@Param("weixinmpId") int weixinmpId, @Param("id") int id);
}
