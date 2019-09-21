package cn.com.yusong.yhdg.common.protocol.msg08;

import cn.com.yusong.yhdg.common.constant.ConstEnum;
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

public class Msg081000006 extends Msg081 {
    private static final Logger log = LogManager.getLogger(Msg081000006.class);

    public static final byte[] EMPTY_IMEI_CODE = new byte[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
    public String checkSum;
    public Boolean checkCRC;
    public String code;
    public String version;
    public int temp1;
    public int temp2;
    public byte fanSpeed;
    public int degree;
    public byte network;
    public byte signal;
    public byte peripheral;
    public int acVoltage;
    public int boxSize;
    public List<Box> boxList = new ArrayList<Box>(20);

    public static String getCRC(byte[] bytes) {
        int CRC = 0x0000ffff;
        int POLYNOMIAL = 0x0000a001;

        int i, j;
        for (i = 0; i < bytes.length; i++) {
            CRC ^= ((int) bytes[i] & 0x000000ff);
            for (j = 0; j < 8; j++) {
                if ((CRC & 0x00000001) != 0) {
                    CRC >>= 1;
                    CRC ^= POLYNOMIAL;
                } else {
                    CRC >>= 1;
                }
            }
        }
        return new String(Integer.toHexString(CRC)).toUpperCase();
    }

    public static void main(String[] args) throws DecoderException {
        String hex = "000028005f000a0101000000000000000000000000000000000000000000000000020100000000000000000000000000000000000000000000000003010000000000000000000000000000000000000000000000000401000000000000000000000000000000000000000000000000050100000000000000000000000000000000000000000000000006010000000000000000000000000000000000000000000000000701000000000000000000000000000000000000000000000000080100000000000000000000000000000000000000000000000009010000000000000000000000000000000000000000000000000a0100000000000000000000000000000000000000000000000003e8001e005a001e0a0000000000";
        byte[] bytes = Hex.decodeHex(hex.toCharArray());
        System.out.println(getCRC(bytes));

    }

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public int getMsgCode() {
        return MsgCode.MSG_081000006.getCode();
    }

    @Override
    public void readData(ByteBuf buffer) {
        //setRequestBodyHex(buffer);
        //Content所有字节异或校验
        byte[] bytes = new byte[2];
        buffer.readBytes(bytes);
        checkSum = new String(Hex.encodeHex(bytes)).toUpperCase();
        if(checkCRC(checkSum,  buffer)){
            checkCRC = true;
        }else{
            checkCRC = false;
        }

        bytes = new byte[12];
        buffer.readBytes(bytes);
        code = new String(Hex.encodeHex(bytes)).toUpperCase();
        version = new String(new char[]{(char) buffer.readByte(), (char) buffer.readByte()});
        temp1 = (buffer.readShort() - 2731) * 10;
        temp2 = (buffer.readShort() - 2731) * 10;
        fanSpeed = buffer.readByte();
        degree = Integer.valueOf(Integer.toHexString(buffer.readInt()));
        network = buffer.readByte();
        signal = buffer.readByte();
        peripheral = buffer.readByte();
        acVoltage = buffer.readShort() * 100;
        int boxListSize = buffer.readUnsignedByte();
        boxSize = boxListSize;
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
        heartParam.temp1 = temp1;
        heartParam.temp2 = temp2;
        heartParam.fanSpeed = fanSpeed;
        heartParam.degree = degree;
        heartParam.network = network;
        heartParam.signal = signal;
        heartParam.peripheral = peripheral;
        heartParam.acVoltage = acVoltage;
        heartParam.boxList = new ArrayList<HeartParam.Box>(boxList.size());

        /**
         * 设备会在门状态发生变化时候才会立刻发心跳
         * 当把电池从01号拿出来，但是不关门，立刻放入02号 放入02号时候电池还是柜子中。
         * 为了解决这个问题，先处理空箱(电池取出的情况) 再处理满箱(电池放入的情况)
         */
        for (Box e : boxList) {
            if (e.batteryExist == 0) {
                HeartParam.Box box = new HeartParam.Box();
                //格口
                box.boxNum = e.boxNum;
                box.boxState = e.boxState;
                int[] boxState = parseBoxState(e.boxState);
                box.isClosed = (byte) boxState[0];
                box.power = e.power;

                //充电器
                box.chargerModule = e.chargerModule;
                box.chargerVersion = e.chargerVersion;
                box.chargeState = e.chargeState;
                box.chargeStage = e.chargeStage;
                box.chargeTime = e.chargeTime;
                box.chargeVoltage = e.chargeVoltage * 10;
                box.batteryVoltage = e.batteryVoltage * 10;
                box.chargeCurrent = e.chargeCurrent;
                box.transformerTemperature = e.transformerTemperature;
                box.heatsinkTemperature = e.heatsinkTemperature;
                box.ambientTemperature = e.ambientTemperature;
                box.chargerFault = e.chargerFault;

                //电池
                box.batteryCode = null;
                box.batteryVersion = e.batteryVersion;
                box.voltage = e.voltage * 10;
                box.electricity = e.electricity * 10;
                box.restCapacity = e.restCapacity * 10;
                box.volume = e.volume;
                box.circle = e.circle;
                box.serials = e.serials;
                box.ratedCapacity = e.ratedCapacity * 10;
                box.temp = e.temp;
                box.singleVoltage = e.singleVoltage;
                box.fet = e.fet;
                box.linkStatus = e.linkStatus;
                box.protectState = e.protectState;

                heartParam.boxList.add(box);
            }
        }

        for (Box e : boxList) {
            if (e.batteryExist != 0) {
                HeartParam.Box box = new HeartParam.Box();
                //格口
                box.boxNum = e.boxNum;
                box.boxState = e.boxState;
                int[] boxState = parseBoxState(e.boxState);
                box.isClosed = (byte) boxState[0];
                box.power = e.power;

                //充电器
                box.chargerModule = e.chargerModule;
                box.chargerVersion = e.chargerVersion;
                box.chargeState = e.chargeState;
                box.chargeStage = e.chargeStage;
                box.chargeTime = e.chargeTime;
                box.chargeVoltage = e.chargeVoltage * 10;
                box.batteryVoltage = e.batteryVoltage * 10;
                box.chargeCurrent = e.chargeCurrent;
                box.transformerTemperature = e.transformerTemperature;
                box.heatsinkTemperature = e.heatsinkTemperature;
                box.ambientTemperature = e.ambientTemperature;
                box.chargerFault = e.chargerFault;

                //电池
                box.batteryCode = e.batteryCode;
                box.batteryVersion = e.batteryVersion;
                box.voltage = e.voltage * 10;
                box.electricity = e.electricity * 10;
                box.restCapacity = e.restCapacity * 10;
                box.volume = e.volume;
                box.circle = e.circle;
                box.serials = e.serials;
                box.ratedCapacity = e.ratedCapacity * 10;
                box.temp = e.temp;
                box.singleVoltage = e.singleVoltage;
                box.fet = e.fet;
                box.linkStatus = e.linkStatus;
                box.protectState = e.protectState;

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

    public static String parseVersion(byte src) {
        StringBuilder stringBuilder = new StringBuilder();
        char[] buffer = new char[2];
        buffer[0] = Character.forDigit((src >>> 4) & 0x0F, 16);
        buffer[1] = Character.forDigit(src & 0x0F, 16);
        stringBuilder.append(buffer);
        String str = stringBuilder.toString();
        String returnVersion = "";
        if(str.length()> 0){
            returnVersion = str.substring(0,1) + "." + str.substring(1,str.length());
        }
        return returnVersion;
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
        public String chargerVersion;
        public String chargerModule;
        public int chargeState;
        public int chargeStage;
        public int chargeTime;
        public int chargeVoltage;
        public int batteryVoltage;
        public int chargeCurrent;
        public int transformerTemperature;
        public int heatsinkTemperature;
        public int ambientTemperature;
        public int chargerFault;
        public int batteryExist;
        public String batteryCode;
        public String batteryVersion;
        public int voltage;
        public int electricity;
        public int restCapacity;
        public byte volume;
        public int circle;
        public int serials;
        public int ratedCapacity;
        public String temp;
        public String singleVoltage;
        public byte fet;
        public int linkStatus;
        public int protectState;
        public byte chargeStatus;
        public int power;
        public int reserved;
        public byte boxState;
        public int serial;

        public void read(ByteBuf buffer) {
            this.boxNum = buffer.readByte();
            //充电器
            byte[] bytes = new byte[6];
            buffer.readBytes(bytes);
            for (int i = 0; i < bytes.length; i ++) {
                if (bytes[i] < 0x30) {
                    bytes[i] = (byte) (bytes[i] + 0x30);
                }
            }
            try {
                chargerModule = new String(bytes, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            chargerVersion = parseVersion(buffer.readByte());
            chargeState = buffer.readByte();
            chargeStage = buffer.readByte();
            chargeTime = buffer.readShort();
            chargeVoltage = buffer.readShort();
            batteryVoltage = buffer.readShort();
            chargeCurrent = buffer.readShort();
            transformerTemperature = (buffer.readShort()  - 2731) / 10;
            heatsinkTemperature = (buffer.readShort()  - 2731) / 10;;
            ambientTemperature = (buffer.readShort()  - 2731) / 10;;
            chargerFault = buffer.readByte();

            //电池
            batteryExist =  buffer.readByte();
            if(batteryExist == ConstEnum.Flag.TRUE.getValue()){
                bytes = new byte[15];
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
                batteryVersion = parseVersion(buffer.readByte());
                voltage = buffer.readShort();
                electricity = buffer.readShort();
                restCapacity = buffer.readShort();
                volume = buffer.readByte();
                circle = buffer.readShort();
                ratedCapacity = buffer.readShort();

                temp = "";
                int tempSize = buffer.readByte();
                for(int i =0;i< tempSize;i++){
                    if(i == tempSize -1){
                        temp +=  (buffer.readShort()  - 2731) / 10 ;
                    }else{
                        temp +=  (buffer.readShort()  - 2731) / 10 +",";
                    }
                }

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

                fet = buffer.readByte();
                linkStatus = buffer.readByte();
                protectState = buffer.readShort();
            }

            //箱门
            power = buffer.readShort();
            boxState = buffer.readByte();
            serial = buffer.readInt();
            reserved = buffer.readShort();
        }
    }
}
