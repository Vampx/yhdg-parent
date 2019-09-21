package cn.com.yusong.yhdg.common.domain.basic;

import cn.com.yusong.yhdg.common.domain.LongIdEntity;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * 运营商公众号的openid
 *
 */
@Setter
@Getter
public class WeixinmpOpenId extends LongIdEntity {
    Integer weixinmpId; //运营商id
    String openId; //系统公众号的Openid
    String secondOpenId; //运营商的公众号的Openid
    Date createTime; //创建时间
}
