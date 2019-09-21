package cn.com.yusong.yhdg.common.protocol.msg_tbit;

public class Msg_T1 extends TextMessage {
    public String terminalType;
    public String version;
    public String vinNo;
    public String sim;
    public String mobile;
    public String password;
    public String restartReason;


    @Override
    public String getMsgCode() {
        return MsgCode.MSG_T1;
    }

    @Override
    protected void readBody(String[] array) {
        int index = 0;
        time = array[index++];
        terminalType = array[index++];
        version = array[index++];
        vinNo = array[index++];
        msgCode =  array[index++];

        sim = array[index++];
        mobile = array[index++];
        password = array[index++];
        restartReason = array[index++];
    }
}
