package cn.com.yusong.yhdg.common.exception;

public class CouponTicketNotAvailableException extends RuntimeException {

    public CouponTicketNotAvailableException() {
    }

    public CouponTicketNotAvailableException(String message) {
        super(message);
    }
}
