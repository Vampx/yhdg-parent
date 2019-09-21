package cn.com.yusong.yhdg.common.protocol.msg_tbit;

public class Msg_T3 extends TextMessage {

    public int positionType;
    public String lngType;
    public Double lng;
    public String latType;
    public Double lat;
    public Double speed;
    public Double azimuth;
    public int stats;
    public String cellid;
    public String eqStatus;
    public int mileage;
    public int voltage;

    @Override
    public String getMsgCode() {
        return MsgCode.MSG_T3;
    }

    @Override
    protected void readBody(String[] array) {
        int index = 0;
        time = array[index++];
        terminalType = array[index++];
        version = array[index++];
        vinNo = array[index++];
        msgCode =  array[index++];

        positionType = Integer.parseInt(array[index++]);
        lngType = array[index++];
        lng = Double.parseDouble(array[index++]);
        latType = array[index++];
        lat = Double.parseDouble(array[index++]);
        speed = Double.parseDouble(array[index++]);
        azimuth = Double.parseDouble(array[index++]);
        stats = Integer.parseInt(array[index++]);
        cellid = array[index++];
        eqStatus = array[index++];
        mileage = Integer.parseInt(array[index++]);
        if(array.length > 16){
            voltage = Integer.parseInt(array[index++]);
        }
    }
}
