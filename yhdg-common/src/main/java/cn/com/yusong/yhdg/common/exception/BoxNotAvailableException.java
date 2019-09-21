package cn.com.yusong.yhdg.common.exception;

/**
 * 箱体不可用
 */
public class BoxNotAvailableException extends RuntimeException {

    public BoxNotAvailableException() {
    }

    public BoxNotAvailableException(String message) {
        super(message);
    }
}
