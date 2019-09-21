package cn.com.yusong.yhdg.common.domain.basic;

import lombok.Getter;
import lombok.Setter;

/**
 * 微信小程序支付订单
 */
@Getter
@Setter
public class WeixinmaPayOrder extends PayOrder {
    String statsDate;
}