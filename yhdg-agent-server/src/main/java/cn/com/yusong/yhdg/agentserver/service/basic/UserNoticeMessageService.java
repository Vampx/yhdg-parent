package cn.com.yusong.yhdg.agentserver.service.basic;

import cn.com.yusong.yhdg.common.domain.basic.UserNoticeMessage;
import cn.com.yusong.yhdg.common.entity.pagination.Page;
import cn.com.yusong.yhdg.agentserver.persistence.basic.UserNoticeMessageMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserNoticeMessageService {
    @Autowired
    UserNoticeMessageMapper userNoticeMessageMapper;

    public UserNoticeMessage find(long id) {
        return userNoticeMessageMapper.find(id);
    }

    public Page findPage(UserNoticeMessage search) {
        Page page = search.buildPage();
        page.setTotalItems(userNoticeMessageMapper.findPageCount(search));
        search.setBeginIndex(page.getOffset());
        page.setResult(userNoticeMessageMapper.findPageResult(search));
        return page;
    }

}
