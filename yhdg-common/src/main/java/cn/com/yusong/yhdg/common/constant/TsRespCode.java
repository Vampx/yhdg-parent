package cn.com.yusong.yhdg.common.constant;

/**
 * terminal server response code
 */
public enum TsRespCode {

    CODE_0(0, "成功"),
    CODE_1(1, "服务器内部错误"),
    CODE_2(2, "会话超时"),
    CODE_3(3, "终端不在线"),
    CODE_4(4, "终端响应超时"),
    CODE_5(5, "服务不在线"),
    CODE_6(6, "开箱失败"),
    CODE_7(7, "截屏失败"),
    CODE_8(8, "开锁失败"),
    CODE_9(9, "参数修改失败"),
    ;

    private final int value;
    private final String name;

    private TsRespCode(int value, String name) {
        this.value = value;
        this.name = name;
    }

    public short getValue() {
        return (short) value;
    }

    public String getName() {
        return name;
    }
}
