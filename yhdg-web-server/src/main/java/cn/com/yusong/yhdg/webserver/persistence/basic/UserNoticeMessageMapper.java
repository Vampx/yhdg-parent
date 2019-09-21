package cn.com.yusong.yhdg.webserver.persistence.basic;

import cn.com.yusong.yhdg.common.domain.basic.UserNoticeMessage;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;

import java.util.List;


public interface UserNoticeMessageMapper extends MasterMapper {
    UserNoticeMessage find(long id);
    int findPageCount(UserNoticeMessage search);
    List<UserNoticeMessage> findPageResult(UserNoticeMessage search);
}
