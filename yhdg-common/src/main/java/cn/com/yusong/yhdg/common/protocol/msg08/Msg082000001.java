package cn.com.yusong.yhdg.common.protocol.msg08;

import cn.com.yusong.yhdg.common.protocol.MsgCode;
import cn.com.yusong.yhdg.common.tool.netty.TypeOperator;
import io.netty.buffer.ByteBuf;

import java.util.ArrayList;
import java.util.List;

public class Msg082000001 extends Msg082 {

    public byte cabinetType;
    public byte activeFanTemp;
    public List<Box> boxList = new ArrayList<Box>();


    public static byte BATTERY_STATUS_EMPTY = 0; //没有电池
    public static byte BATTERY_STATUS_NOT_CHARGE = 1; //有电池未充电
    public static byte BATTERY_STATUS_CHARGING = 2; //有电池充电中
    public static byte BATTERY_STATUS_FULL = 3; //有电池已充满

    @Override
    public int getMsgCode() {
        return MsgCode.MSG_082000001.getCode();
    }

    @Override
    public void writeData(ByteBuf buffer) {
        writeRtn(buffer);
        buffer.writeByte(cabinetType);
        buffer.writeByte(activeFanTemp);

        buffer.writeByte(boxList.size());
        for(Box e : boxList) {
            e.write(buffer);
        }
    }

    @Override
    public void readData(ByteBuf buffer) {
        readRtn(buffer);
        cabinetType = buffer.readByte();
        activeFanTemp = buffer.readByte();

        int size = buffer.readByte();
        for(int i = 0; i < size; i++) {
            Box box = new Box();
            box.read(buffer);

            boxList.add(box);
        }
    }

    public static class Box extends TypeOperator {
        public byte boxNum;
        public byte batteryStatus;

        public void read(ByteBuf buffer) {
            boxNum = buffer.readByte();
            batteryStatus = buffer.readByte();
        }

        public void write(ByteBuf buffer) {
            buffer.writeByte(boxNum);
            buffer.writeByte(batteryStatus);
        }
    }
}
