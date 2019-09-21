package cn.com.yusong.yhdg.common.protocol.msg08;

import cn.com.yusong.yhdg.common.protocol.MsgCode;
import cn.com.yusong.yhdg.common.tool.netty.TypeOperator;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class Msg081000005 extends Msg081 {
    private static final Logger log = LogManager.getLogger(Msg081000005.class);

    public static void main(String[] args) throws DecoderException {
        String hex = "04d3f644000000a1000001aa0668ff3633374b4d43122423313500060004000002310100000a01010000154a00000000000000000000000000000000000000000000000000000000000000000000020100000000000000000000000000000000000000000000000000000000000000000000000000000301000000000000000000000000000000000000000000000000000000000000000000000000000004010000000000000000000000000000000000000000000000000000000000000000000000000000050100000000000000000000000000000000000000000000000000000000000000000000000000000600000020cb5953444c3438323630303030303031149a00004e2000031000000b380b2e07ef00000701000000000000000000000000000000000000000000000000000000000000000000000000000008010000000000000000000000000000000000000000000000000000000000000000000000000000090100000000000000000000000000000000000000000000000000000000000000000000000000000a010000000000000000000000000000000000000000000000000000000000000000000000000000";
        ByteBuf buf = Unpooled.buffer(1024);
        buf.writeBytes(Hex.decodeHex(hex.toCharArray()));

        Msg081000005 msg = new Msg081000005();
        msg.decode(buf);
        msg.toParam();
    }


    public static final byte[] EMPTY_IMEI_CODE = new byte[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
    //
    public byte checksum;

    public String code;
    public String version;
    public int temp1;
    public int temp2;
    public int degree;
    public byte network;
    public byte signal;
    public byte fireState;
    //
    public byte fanSpeed;
    public byte waterLevel;
    public byte smokeState;

    public List<Box> boxList = new ArrayList<Box>(20);

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public int getMsgCode() {
        return MsgCode.MSG_081000005.getCode();
    }

    @Override
    public void readData(ByteBuf buffer) {
        //Content所有字节异或
        checksum = buffer.readByte();

        byte[] bytes = new byte[12];
        buffer.readBytes(bytes);
        code = new String(Hex.encodeHex(bytes)).toUpperCase();

        version = new String(new char[]{(char) buffer.readByte(), (char) buffer.readByte()});

        temp1 = (buffer.readByte() == 0 ? 1 : -1) * buffer.readUnsignedByte();
        temp2 = (buffer.readByte() == 0 ? 1 : -1) * buffer.readUnsignedByte();
        //
        fanSpeed = buffer.readByte();

        degree = Integer.valueOf(Integer.toHexString(buffer.readInt()));
        network = buffer.readByte();
        signal = buffer.readByte();
        fireState = buffer.readByte();
        //
        waterLevel = buffer.readByte();
        smokeState = buffer.readByte();

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
        heartParam.signal = signal == 0 ? 100 : signal;
        heartParam.fireState = fireState;
        //
        heartParam.fanSpeed = fanSpeed;
        heartParam.waterLevel = waterLevel;
        heartParam.smokeState = smokeState;

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
                box.voltage = e.voltage;
                box.electricity = e.electricity;
                box.volume = e.volume;
                box.protectState = e.protectState;
                box.fet = e.fet;
                box.chargeStatus = e.chargeStatus;
                box.power = e.power;
                box.batteryTemp1 = e.batteryTemp1;
                box.batteryTemp2 = e.batteryTemp2;
                box.restCapacity = e.restCapacity;
                //
                box.boxVersion = e.boxVersion;
                box.boxTemp = e.boxTemp;
                box.fanSpeed = e.fanSpeed;

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
                box.voltage = e.voltage;
                box.electricity = e.electricity;
                box.volume = e.volume;
                box.protectState = e.protectState;
                box.fet = e.fet;
                box.chargeStatus = e.chargeStatus;
                box.power = e.power;
                box.batteryTemp1 = e.batteryTemp1;
                box.batteryTemp2 = e.batteryTemp2;
                box.restCapacity = e.restCapacity;
                //
                box.boxVersion = e.boxVersion;
                box.boxTemp = e.boxTemp;
                box.fanSpeed = e.fanSpeed;

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
        public int batteryTemp1;
        public int batteryTemp2;
        public int restCapacity;
        public int reserved;
        //
        public byte boxVersion;
        public int boxTemp;
        public byte fanSpeed;
        public int cycleTimes;

        public void read(ByteBuf buffer) {
            this.boxNum = buffer.readByte();
            //
            this.boxVersion = buffer.readByte();

            this.boxState = buffer.readByte();
            this.serial = buffer.readInt();

            //
            this.boxTemp = buffer.readShort();
            this.fanSpeed = buffer.readByte();

            this.chargeStatus = buffer.readByte();
            this.power = buffer.readShort();

            byte[] bytes = new byte[16];
            buffer.readBytes(bytes);

            buffer.readBytes(bytes);
            for (int i = 0; i < bytes.length; i ++) {
                if (bytes[i] < 0x30) {
                    bytes[i] = (byte) (bytes[i] + 0x30);
                }
            }
            try {
                this.batteryCode = new String(bytes, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

          /*String bc = "";
            char[] chars = new String(Hex.encodeHex(bytes)).toUpperCase().toCharArray();
            for (int i = 1; i < chars.length; i = i + 2) {
                bc += chars[i];
            }
            batteryCode = bc;*/


            this.voltage = buffer.readUnsignedShort();
            this.electricity = buffer.readUnsignedShort();
            this.volume = buffer.readByte();
            this.restCapacity = buffer.readShort();
            this.cycleTimes = buffer.readShort();
            this.batteryTemp1 = buffer.readShort();
            this.batteryTemp2 = buffer.readShort();
            this.protectState = buffer.readUnsignedShort();
            this.fet = buffer.readByte();
            this.reserved = buffer.readShort();
        }

    }
}
