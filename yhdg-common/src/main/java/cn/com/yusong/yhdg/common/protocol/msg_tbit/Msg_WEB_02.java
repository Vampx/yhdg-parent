package cn.com.yusong.yhdg.common.protocol.msg_tbit;

public class Msg_WEB_02 extends TextMessage {

    @Override
    public String getMsgCode() {
        return MsgCode.MSG_WEB_02;
    }

    @Override
    protected void writeBody(StringBuilder body) {
        body.append(time);
        body.append(SPLIT);
        body.append(getMsgCode());
    }
}
