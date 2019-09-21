package cn.com.yusong.yhdg.common.domain.basic;

import cn.com.yusong.yhdg.common.domain.PageEntity;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Setter
@Getter
public class PartnerInOutCash extends PageEntity {
    Integer partnerId;
    String partnerName;
    String statsDate;
    Integer weixinmpIncome;
    Integer weixinmpRefund;
    Integer weixinmpWithdraw;
    Integer alipayfwIncome;
    Integer alipayfwRefund;
    Integer alipayfwWithdraw;
    Integer weixinIncome;
    Integer weixinRefund;
    Integer weixinWithdraw;
    Integer alipayIncome;
    Integer alipayRefund;
    Integer alipayWithdraw;
    Date updateTime;

    public void init() {
        weixinmpIncome = 0;
        weixinmpRefund = 0;
        weixinmpWithdraw = 0;
        alipayfwIncome = 0;
        alipayfwRefund = 0;
        alipayfwWithdraw = 0;
        weixinIncome = 0;
        weixinRefund = 0;
        weixinWithdraw = 0;
        alipayIncome = 0;
        alipayRefund = 0;
        alipayWithdraw = 0;
        updateTime = new Date();
    }
}
