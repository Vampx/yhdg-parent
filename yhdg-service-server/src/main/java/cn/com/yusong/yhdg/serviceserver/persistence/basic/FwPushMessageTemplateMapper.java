package cn.com.yusong.yhdg.serviceserver.persistence.basic;

import cn.com.yusong.yhdg.common.domain.basic.FwPushMessageTemplate;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

public interface FwPushMessageTemplateMapper extends MasterMapper {
    public FwPushMessageTemplate find(@Param("alipayfwId") int alipayfwId, @Param("id") int id);
}
