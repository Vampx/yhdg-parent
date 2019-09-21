package cn.com.yusong.yhdg.common.protocol.msg08;

import cn.com.yusong.yhdg.common.protocol.MsgCode;
import cn.com.yusong.yhdg.common.tool.netty.TypeOperator;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class Msg081000007 extends Msg081 {
    private static final Logger log = LogManager.getLogger(Msg081000007.class);


    public static final byte[] EMPTY_IMEI_CODE = new byte[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};

    public String code;
    public String version;
    public int temp1;
    public int temp2;
    public int degree;
    public byte network;
    public byte signal;
    public byte fireState;
    public List<Box> boxList = new ArrayList<Box>(20);

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public int getMsgCode() {
        return MsgCode.MSG_081000007.getCode();
    }

    @Override
    public void readData(ByteBuf buffer) {
        //setRequestBodyHex(buffer);

        byte[] bytes = new byte[12];
        buffer.readBytes(bytes);
        code = new String(Hex.encodeHex(bytes)).toUpperCase();

        version = new String(new char[]{(char) buffer.readByte(), (char) buffer.readByte()});

        temp1 = (buffer.readByte() == 0 ? 1 : -1) * buffer.readUnsignedByte();
        temp2 = (buffer.readByte() == 0 ? 1 : -1) * buffer.readUnsignedByte();
        degree = Integer.valueOf(Integer.toHexString(buffer.readInt()));
        network = buffer.readByte();
        signal = buffer.readByte();
        fireState = buffer.readByte();
        int boxListSize = buffer.readUnsignedByte();
        for (int i = 0; i < boxListSize; i++) {
            Box box = new Box();
            box.read(buffer);
            boxList.add(box);
        }
    }

    public HeartParam toParam() {
        HeartParam heartParam = new HeartParam();
        heartParam.code = code;
        heartParam.version = version;

        if (temp1 != -1 * 0XFF) {
            heartParam.temp1 = temp1;
        }
        if (temp2 != -1 * 0XFF) {
            heartParam.temp2 = temp2;
        }

        heartParam.degree = degree;
        heartParam.network = network;
        heartParam.signal = signal==0?100:signal;
        heartParam.fireState = fireState;
        heartParam.boxList = new ArrayList<HeartParam.Box>(boxList.size());

        /**
         * 设备会在门状态发生变化时候才会立刻发心跳
         * 当把电池从01号拿出来，但是不关门，立刻放入02号 放入02号时候电池还是柜子中。
         * 为了解决这个问题，先处理空箱(电池取出的情况) 再处理满箱(电池放入的情况)
         */
        for (Box e : boxList) {
            if (e.batteryCode.equals(HeartParam.EMPTY_IMEI_CODE)) {
                HeartParam.Box box = new HeartParam.Box();
                box.boxNum = e.boxNum;
                int[] boxState = parseBoxState(e.boxState);
                box.isClosed = (byte) boxState[0];
                box.isSmokeAlarm = (byte) boxState[1];
                box.batteryCode = null;
                box.voltage = e.voltage * 10;
                box.electricity = e.electricity * 10;
                box.volume = e.volume;
                box.protectState = e.protectState;
                box.fet = e.fet;
                box.chargeStatus = e.chargeStatus;
                box.power = e.power;
                box.restCapacity = e.restCapacity * 10;
                box.ratedCapacity = e.ratedCapacity * 10;
                box.circle = e.circle;
                box.serials = e.serials;
                box.singleVoltage = e.singleVoltage;
                box.temp = e.temp;
                heartParam.boxList.add(box);
            }
        }

        for (Box e : boxList) {
            if (!e.batteryCode.equals(HeartParam.EMPTY_IMEI_CODE)) {
                HeartParam.Box box = new HeartParam.Box();
                box.boxNum = e.boxNum;
                int[] boxState = parseBoxState(e.boxState);
                box.isClosed = (byte) boxState[0];
                box.isSmokeAlarm = (byte) boxState[1];
                box.batteryCode = e.batteryCode;
                box.voltage = e.voltage * 10;
                box.electricity = e.electricity * 10;
                box.volume = e.volume;
                box.protectState = e.protectState;
                box.fet = e.fet;
                box.chargeStatus = e.chargeStatus;
                box.power = e.power;
                box.restCapacity = e.restCapacity * 10;
                box.ratedCapacity = e.ratedCapacity * 10;
                box.circle = e.circle;
                box.serials = e.serials;
                box.singleVoltage = e.singleVoltage;
                box.temp = e.temp;
                heartParam.boxList.add(box);
            }
        }

        return heartParam;
    }

    public static int[] parseBoxState(int switchMode) {
        int[] array = new int[8];
        for (int i = 0; i < 8; i++) {
            array[i] = switchMode >> i & 0x01;
        }
        return array;
    }

    public static int toBoxState(int[] array) {
        int v = 0;
        for (int i = 0; i < 2; i++) {
            if (array[i] == 1) {
                v = v | (0x01 << i);
            }
        }
        return v;
    }

    public static class Box extends TypeOperator {
        public byte boxNum;
        public byte boxState;
        public int serial;
        public String batteryCode;
        public int voltage;
        public int electricity;
        public byte volume;
        public int protectState;
        public byte fet;
        public byte chargeStatus;
        public int power;
        public int restCapacity;
        public int ratedCapacity;
        public int circle;
        public int serials;
        public String singleVoltage;
        public String temp;
        public int reserved;

        public void read(ByteBuf buffer) {
            this.boxNum = buffer.readByte();
            this.boxState = buffer.readByte();
            this.serial = buffer.readInt();

            byte[] bytes = new byte[15];
            buffer.readBytes(bytes);
            for (int i = 0; i < bytes.length; i ++) {
                if (bytes[i] < 0x30) {
                    bytes[i] = (byte) (bytes[i] + 0x30);
                }
            }
            try {
                batteryCode = new String(bytes, "UTF-8");
                //智租电池特殊处理
                if(batteryCode.startsWith("B") && batteryCode.contains("YS") && batteryCode.length() == 15){
                    batteryCode = batteryCode.substring(0,batteryCode.length() -1);
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            this.voltage = buffer.readUnsignedShort();
            this.electricity = buffer.readShort();
            this.volume = buffer.readByte();
            this.protectState = buffer.readUnsignedShort();
            this.fet = buffer.readByte();
            this.chargeStatus = buffer.readByte();
            this.power = buffer.readShort();
            this.restCapacity = buffer.readShort();
            this.ratedCapacity = buffer.readShort();
            this.circle = buffer.readShort();

            singleVoltage = "";
            int volSize = buffer.readByte();
            this.serials = volSize;
            for(int i =0;i< volSize;i++){
                if(i == volSize -1){
                    singleVoltage +=  buffer.readShort();
                }else{
                    singleVoltage +=  buffer.readShort() +",";
                }
            }

            temp = "";
            int tempSize = buffer.readByte();
            for(int i =0;i< tempSize;i++){
                if(i == tempSize -1){
                    temp +=  (buffer.readShort()  - 2731) / 10 ;
                }else{
                    temp +=  (buffer.readShort()  - 2731) / 10 +",";
                }
            }


            this.reserved = buffer.readShort();
        }

    }
}
