package cn.com.yusong.yhdg.staticserver.persistence.basic;


import cn.com.yusong.yhdg.common.domain.basic.MobileMessageTemplate;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

public interface MobileMessageTemplateMapper extends MasterMapper {
    public MobileMessageTemplate find(@Param("appId") int agentId, @Param("id") int id);
}
