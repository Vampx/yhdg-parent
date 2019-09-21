package cn.com.yusong.yhdg.common.protocol.msg_tbit;

public class Msg_T14 extends TextMessage {

    public String returnValue;


    @Override
    public String getMsgCode() {
        return MsgCode.MSG_T14;
    }

    @Override
    protected void readBody(String[] array) {
        int index = 0;
        time = array[index++];
        terminalType = array[index++];
        version = array[index++];
        vinNo = array[index++];
        msgCode =  array[index++];

        returnValue =  array[index++];
    }
}
