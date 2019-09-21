package cn.com.yusong.yhdg.serviceserver.persistence.basic;

import cn.com.yusong.yhdg.common.domain.basic.UserNoticeMessage;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;

/**
 * Created by ruanjian5 on 2017/12/23.
 */
public interface UserNoticeMessageMapper extends MasterMapper {
    public int insert(UserNoticeMessage noticeMessage);
}
