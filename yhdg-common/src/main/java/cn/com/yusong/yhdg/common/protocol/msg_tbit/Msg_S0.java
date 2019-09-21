package cn.com.yusong.yhdg.common.protocol.msg_tbit;

public class Msg_S0 extends TextMessage {

    @Override
    public String getMsgCode() {
        return MsgCode.MSG_S0;
    }

    @Override
    protected void writeBody(StringBuilder body) {
        body.append(SPLIT);
        body.append(getMsgCode());
    }
}
