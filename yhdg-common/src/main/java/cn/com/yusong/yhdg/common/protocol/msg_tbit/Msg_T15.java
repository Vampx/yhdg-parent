package cn.com.yusong.yhdg.common.protocol.msg_tbit;

public class Msg_T15 extends TextMessage {

    public int code; // 1 成功 0 设备


    @Override
    public String getMsgCode() {
        return MsgCode.MSG_T15;
    }

    @Override
    protected void readBody(String[] array) {
        int index = 0;
        time = array[index++];
        terminalType = array[index++];
        version = array[index++];
        vinNo = array[index++];
        msgCode =  array[index++];

        code =  Integer.parseInt(array[index++]);
    }
}
