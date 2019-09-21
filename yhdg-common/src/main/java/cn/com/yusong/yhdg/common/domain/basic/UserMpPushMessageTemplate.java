package cn.com.yusong.yhdg.common.domain.basic;

import cn.com.yusong.yhdg.common.domain.IntIdEntity;
import lombok.Getter;
import lombok.Setter;

/**
 * 运营商账号推送消息配置
 */
@Setter
@Getter
public class UserMpPushMessageTemplate extends IntIdEntity {
    Integer weixinmpId;
    Long userId;
    Integer isActive;

    String weixinmpName;

}
