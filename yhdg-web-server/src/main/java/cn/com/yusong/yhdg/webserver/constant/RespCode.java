package cn.com.yusong.yhdg.webserver.constant;

public enum RespCode {
    CODE_0(0, "成功"),
    CODE_1(1, "服务器内部错误"),
    CODE_2(2, "参数错误"),
    CODE_3(3, "会话超时"),
    CODE_4(4, "登录二维码已过期"),
    CODE_5(5, "账号不存在"),
    CODE_6(6, "密码未设置")
    ;

    private final int value;
    private final String name;

    RespCode(int value, String name) {
        this.value = value;
        this.name = name;
    }

    public int getValue() {
        return value;
    }

    public String getName() {
        return name;
    }
}