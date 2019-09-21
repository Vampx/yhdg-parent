package cn.com.yusong.yhdg.common.constant;

public enum YmsRespCode {


    CODE_0(0, "成功"),

    //terminal return code
    CODE_1(1, "服务器内部错误"),
    CODE_2(2, "参数错误"),
    CODE_3(3, "终端编号不存在"),
    CODE_4(4, "验证字符串错误"),
    CODE_5(5, "服务器离线"),
    CODE_6(6, "终端被禁用"),
    CODE_7(7, "会话超时"),

    SERVER_CODE(1000, "SERVER_CODE"),

    //front server return code
    CODE_1001(1001, "服务器内部错误"),
    CODE_1002(1002, "参数错误"),
    CODE_1003(1003, "会话超时"),
    CODE_1004(1004, "注册码不存在"),
    CODE_1005(1005, "分发服务器不存在"),
    ;

    private final int value;
    private final String name;

    private YmsRespCode(int value, String name) {
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
