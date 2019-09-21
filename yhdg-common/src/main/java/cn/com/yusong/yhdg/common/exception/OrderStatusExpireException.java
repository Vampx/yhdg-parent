package cn.com.yusong.yhdg.common.exception;

public class OrderStatusExpireException extends RuntimeException {

    public OrderStatusExpireException() {
    }

    public OrderStatusExpireException(String message) {
        super(message);
    }
}
