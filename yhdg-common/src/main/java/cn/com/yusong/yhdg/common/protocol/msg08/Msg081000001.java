package cn.com.yusong.yhdg.common.protocol.msg08;

import cn.com.yusong.yhdg.common.protocol.MsgCode;
import cn.com.yusong.yhdg.common.tool.netty.TypeOperator;
import io.netty.buffer.ByteBuf;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class Msg081000001 extends Msg081 {

    public static final byte[] EMPTY_BATTERY_ID = new byte[]{0, 0, 0, 0};

    public String code;
    public String version;
    public int temp1;
    public int temp2;
    public List<Box> boxList = new ArrayList<Box>(20);

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public int getMsgCode() {
        return MsgCode.MSG_081000001.getCode();
    }

    @Override
    public void readData(ByteBuf buffer) {
        byte[] bytes = new byte[12];
        buffer.readBytes(bytes);
        code = new String(Hex.encodeHex(bytes)).toUpperCase();

        version = new String(new char[]{(char) buffer.readByte(), (char) buffer.readByte()});

        temp1 = (buffer.readByte() == 0 ? 1 : -1) * buffer.readUnsignedByte();
        temp2 = (buffer.readByte() == 0 ? 1 : -1) * buffer.readUnsignedByte();

        int boxListSize = buffer.readUnsignedByte();
        for(int i = 0; i < boxListSize; i++) {
            Box box = new Box();
            box.read(buffer);
            boxList.add(box);
        }
    }

    @Override
    public void writeData(ByteBuf buffer) {
        try {
            buffer.writeBytes(Hex.decodeHex(code.toCharArray()));
            buffer.writeBytes(version.getBytes());
        } catch (DecoderException e) {
            throw new RuntimeException(e);
        }

        buffer.writeByte(boxList.size());
        for(Box box : boxList) {
            box.write(buffer);
        }
    }

    public HeartParam toParam() {
        HeartParam heartParam = new HeartParam();
        heartParam.code = code;
        heartParam.version = version;
        heartParam.network = 1; //网络类型 0:GSM/GPRS 1:Wired 2:3G 3:4G

        if(temp1 != -1 * 0XFF) {
            heartParam.temp1 = temp1;
        }
        if(temp2 != -1 * 0XFF) {
            heartParam.temp2 = temp2;
        }

        heartParam.boxList = new ArrayList<HeartParam.Box>(boxList.size());

        /**
         * 设备会在门状态发生变化时候才会立刻发心跳
         * 当把电池从01号拿出来，但是不关门，立刻放入02号 放入02号时候电池还是柜子中。
         * 为了解决这个问题，先处理空箱(电池取出的情况) 再处理满箱(电池放入的情况)
         */
        for(Box e : boxList) {
            if(e.batteryId.equals(HeartParam.EMPTY_BATTERY_CARD)) {
                HeartParam.Box box = new HeartParam.Box();
                box.boxNum = e.boxNum;
                box.isClosed = e.isClosed;
                box.batteryId = null;
                heartParam.boxList.add(box);
            }
        }

        for(Box e : boxList) {
            if(!e.batteryId.equals(HeartParam.EMPTY_BATTERY_CARD)) {
                HeartParam.Box box = new HeartParam.Box();
                box.boxNum = e.boxNum;
                box.isClosed = e.isClosed;
                box.batteryId = e.batteryId;
                heartParam.boxList.add(box);
            }
        }

        return heartParam;
    }

    public static class Box extends TypeOperator {
        public byte boxNum;
        public byte isClosed;
        public int serial;
        public String batteryId;

        public void read(ByteBuf buffer) {
            this.boxNum= buffer.readByte();
            this.isClosed = buffer.readByte();
            this.serial = buffer.readInt();

            byte[] bytes = new byte[4];
            buffer.readBytes(bytes);
            batteryId = new String(Hex.encodeHex(bytes)).toUpperCase();
        }

        public void write(ByteBuf buffer) {
            buffer.writeByte(boxNum);
            buffer.writeByte(isClosed);
            buffer.writeInt(serial);

            if(StringUtils.isEmpty(batteryId)) {
                buffer.writeBytes(EMPTY_BATTERY_ID);
            } else {
                try {
                    buffer.writeBytes(Hex.decodeHex(batteryId.toCharArray()));
                } catch (DecoderException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}
