package cn.com.yusong.yhdg.common.domain.basic;

import cn.com.yusong.yhdg.common.domain.LongIdEntity;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * 运营商公众号的openid关注状态
 */
@Setter
@Getter
public class WeixinmpSubscribe extends LongIdEntity {

    Integer weixinmpId;
    String openId;
    Date createTime;
}
