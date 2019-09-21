package cn.com.yusong.yhdg.appserver.service.basic;

import cn.com.yusong.yhdg.appserver.persistence.basic.UserNoticeMessageMapper;
import cn.com.yusong.yhdg.appserver.service.AbstractService;
import cn.com.yusong.yhdg.common.domain.basic.UserNoticeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserNoticeMessageService extends AbstractService {
    @Autowired
    UserNoticeMessageMapper userNoticeMessageMapper;

    public List<UserNoticeMessage> findListByUserId(Long userId, Integer type, Integer offset, Integer limit) {
        return userNoticeMessageMapper.findListByUserId(userId, type, offset, limit);
    }
}
