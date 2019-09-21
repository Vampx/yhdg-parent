package cn.com.yusong.yhdg.common.protocol.msg_tbit;

import java.util.Date;

public class Msg_S1 extends TextMessage {

    public Integer loginStatus;


    @Override
    public String getMsgCode() {
        return MsgCode.MSG_S1;
    }

    @Override
    protected void writeBody(StringBuilder body) {
        body.append(time);
        body.append(SPLIT);
        body.append(getMsgCode());
        body.append(SPLIT);
        body.append(loginStatus);
    }
}
