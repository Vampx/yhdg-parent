package cn.com.yusong.yhdg.common.exception;

/**
 * 桩点不可用
 */
public class ChargerPointNotAvailableException extends RuntimeException {

    public ChargerPointNotAvailableException() {
    }

    public ChargerPointNotAvailableException(String message) {
        super(message);
    }
}
