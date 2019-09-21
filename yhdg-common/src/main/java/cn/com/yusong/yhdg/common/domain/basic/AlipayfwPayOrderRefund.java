package cn.com.yusong.yhdg.common.domain.basic;

/**
 * 支付宝生活号
 */
public class AlipayfwPayOrderRefund extends PayOrderRefund {
    String statsDate;

    public String getStatsDate() {
        return statsDate;
    }

    public void setStatsDate(String statsDate) {
        this.statsDate = statsDate;
    }
}
