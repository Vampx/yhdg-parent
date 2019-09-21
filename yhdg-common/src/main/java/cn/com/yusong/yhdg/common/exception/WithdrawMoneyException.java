package cn.com.yusong.yhdg.common.exception;

/**
 * 提现金额异常
 */
public class WithdrawMoneyException extends RuntimeException {
    public WithdrawMoneyException(String message) {
        super(message);
    }
}
