package cn.com.yusong.yhdg.common.protocol.msg_tbit;

public class Msg_S15 extends TextMessage {

    public Integer lock;//1 开电 2 断电


    @Override
    public String getMsgCode() {
        return MsgCode.MSG_S15;
    }

    @Override
    protected void writeBody(StringBuilder body) {
        body.append(SPLIT);
        body.append(getMsgCode());
        body.append(SPLIT);
        body.append(lock);
    }
}
