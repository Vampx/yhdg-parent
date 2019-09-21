package cn.com.yusong.yhdg.common.domain.basic;

import cn.com.yusong.yhdg.common.domain.LongIdEntity;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * 服务号OpenId表
 * 用于保证openId的唯一性
 */
@Setter
@Getter
public class PartnerFwOpenId extends LongIdEntity {
    Integer partnerId;
    String openId;
    Long customerId;
    String nickname;
    String photoPath;
    Date createTime;
}
