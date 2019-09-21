package cn.com.yusong.yhdg.webserver.persistence.basic;

import cn.com.yusong.yhdg.common.domain.basic.UserMpPushMessageTemplate;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

public interface UserMpPushMessageTemplateMapper extends MasterMapper {
    public int insert(UserMpPushMessageTemplate search);
    int deleteByUser(@Param("userId")long userId);
}
