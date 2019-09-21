package cn.com.yusong.yhdg.common.protocol.msg_tbit;

public class Msg_T0 extends TextMessage {

    public String eqStatus;
    public int voltage;

    @Override
    public String getMsgCode() {
        return MsgCode.MSG_T0;
    }

    @Override
    protected void readBody(String[] array) {
        int index = 0;
        time = array[index++];
        terminalType = array[index++];
        version = array[index++];
        vinNo = array[index++];
        msgCode =  array[index++];

        eqStatus = array[index++];
        //voltage = Integer.parseInt(array[index++]);

    }
}
