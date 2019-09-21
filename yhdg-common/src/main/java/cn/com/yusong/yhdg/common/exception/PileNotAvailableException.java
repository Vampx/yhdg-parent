package cn.com.yusong.yhdg.common.exception;

/**
 * 桩点不可用
 */
public class PileNotAvailableException extends RuntimeException {

    public PileNotAvailableException() {
    }

    public PileNotAvailableException(String message) {
        super(message);
    }
}
