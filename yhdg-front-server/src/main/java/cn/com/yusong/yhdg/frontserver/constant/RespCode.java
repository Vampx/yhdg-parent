package cn.com.yusong.yhdg.frontserver.constant;

public enum RespCode {
    CODE_0(0, "成功"),
    ODE_1(1, "服务器内部错误"),
    CODE_2(2, "参数错误"),
    CODE_3(3, "终端编号不存在"),
    CODE_4(4, "验证字符串错误"),
    CODE_5(5, "服务器离线"),
    CODE_6(6, "终端被禁用"),
    CODE_7(7, "会话超时"),
    ;

    private final int value;
    private final String name;

    private RespCode(int value, String name) {
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
