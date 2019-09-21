package cn.com.yusong.yhdg.common.protocol.msg_tbit;

public class Msg_S3 extends TextMessage {

    @Override
    public String getMsgCode() {
        return MsgCode.MSG_S3;
    }

    @Override
    protected void writeBody(StringBuilder body) {
        body.append(SPLIT);
        body.append(getMsgCode());
    }
}
