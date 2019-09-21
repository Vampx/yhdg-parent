package cn.com.yusong.yhdg.appserver.persistence.basic;


import cn.com.yusong.yhdg.common.domain.basic.MobileMessageTemplate;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

public interface MobileMessageTemplateMapper extends MasterMapper {
    public MobileMessageTemplate find(@Param("partnerId") int partnerId, @Param("id") int id);
}
