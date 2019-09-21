package cn.com.yusong.yhdg.common.protocol.msg_tbit;

public class Msg_S14 extends TextMessage {

    public String value;


    @Override
    public String getMsgCode() {
        return MsgCode.MSG_S14;
    }

    @Override
    protected void writeBody(StringBuilder body) {
        body.append(SPLIT);
        body.append(getMsgCode());
        body.append(SPLIT);
        body.append(value);
    }
}
