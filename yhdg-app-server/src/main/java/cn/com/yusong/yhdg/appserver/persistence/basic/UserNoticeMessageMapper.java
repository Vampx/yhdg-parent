package cn.com.yusong.yhdg.appserver.persistence.basic;

import cn.com.yusong.yhdg.common.domain.basic.UserNoticeMessage;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserNoticeMessageMapper extends MasterMapper {
    List<UserNoticeMessage> findListByUserId(@Param("userId") Long userId, @Param("type") Integer type, @Param("offset") Integer offset, @Param("limit") Integer limit);
}
