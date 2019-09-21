package cn.com.yusong.yhdg.cabinetserver.constant;

public enum RespCode {

    CODE_0(0, "成功"),
    CODE_1(1, "服务器内部错误"),
    CODE_2(2, "参数错误"),
    CODE_3(3, "终端编号不存在"),
    CODE_4(4, "CRC无效"),
//    CODE_5(5, "桩点不可用"),
//    CODE_6(6, "余额不足"),
    CODE_7(7, "终端不存在"),
//    CODE_8(8, "用户不存在"),
//    CODE_9(9, "终端被禁用"),
//    CODE_10(10, "用户被禁用"),
//    CODE_11(11, "订单不存在"),
    CODE_12(12, "无更新包"),
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
