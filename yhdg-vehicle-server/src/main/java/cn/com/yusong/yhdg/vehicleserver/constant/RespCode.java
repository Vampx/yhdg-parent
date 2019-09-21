package cn.com.yusong.yhdg.vehicleserver.constant;

public enum RespCode {

    CODE_0(0, "失败"),
    CODE_1(1, "成功"),
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
