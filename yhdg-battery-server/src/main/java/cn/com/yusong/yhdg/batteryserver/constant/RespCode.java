package cn.com.yusong.yhdg.batteryserver.constant;

public enum RespCode {

    CODE_0(0, "成功"),
    CODE_1(1, "服务器内部错误"),
    CODE_2(2, "参数错误"),
    CODE_3(3, "编号不存在"),
    CODE_4(4, "无更新包"),
    CODE_5(5, "签名错误");

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
