package cn.com.yusong.yhdg.common.domain.basic;

import lombok.Getter;
import lombok.Setter;

/**
 * 微信公众号支付订单
 */
@Getter
@Setter
public class WeixinmpPayOrder extends PayOrder {
    String statsDate;
}