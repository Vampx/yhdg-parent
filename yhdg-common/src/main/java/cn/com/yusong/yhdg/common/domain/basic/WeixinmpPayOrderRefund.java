package cn.com.yusong.yhdg.common.domain.basic;

/**
 * 微信公众号支付订单退款
 */

public class WeixinmpPayOrderRefund extends PayOrderRefund {
    String statsDate;

    public String getStatsDate() {
        return statsDate;
    }

    public void setStatsDate(String statsDate) {
        this.statsDate = statsDate;
    }
}