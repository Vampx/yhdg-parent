package cn.com.yusong.yhdg.common.protocol.msg07;

import cn.com.yusong.yhdg.common.constant.Constant;
import cn.com.yusong.yhdg.common.protocol.MsgCode;
import cn.com.yusong.yhdg.common.tool.netty.TypeOperator;
import io.netty.buffer.ByteBuf;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 开关门查询通知
 */
public class Msg072000004 extends Msg072 {
    public String checkSum;
    public Boolean checkCRC;

    public String boxCode;
    public List<Box> boxList = new ArrayList<Box>();

    @Override
    public int getMsgCode() {
        return MsgCode.MSG_072000004.getCode();
    }

    @Override
    public void readData(ByteBuf buffer) {
        //Content所有字节异或校验
        byte[] bytes = new byte[2];
        buffer.readBytes(bytes);
        checkSum = new String(Hex.encodeHex(bytes)).toUpperCase();
        if(checkCRC(checkSum,  buffer)){
            checkCRC = true;
        }else{
            checkCRC = false;
        }
        readRtn(buffer);
        bytes = new byte[12];
        buffer.readBytes(bytes);
        boxCode  = new String(Hex.encodeHex(bytes)).toUpperCase();
        int boxSize = buffer.readInt();
        for(int i = 0; i < boxSize; i++) {
            Box box = new Box();
            box.read(buffer);
            boxList.add(box);
        }
    }

    @Override
    public void writeData(ByteBuf buffer) {

    }

    public static class Box extends TypeOperator {
        public byte boxNum;
        public byte boxStatus;
        public String batteryId;

        public void read(ByteBuf buffer) {
            this.boxNum = buffer.readByte();
            this.boxStatus = buffer.readByte();

            byte[] bytes = new byte[4];
            buffer.readBytes(bytes);
            this.batteryId = new String(Hex.encodeHex(bytes)).toUpperCase();
        }

        public void write(ByteBuf buffer) {
            buffer.writeByte(this.boxNum);
            buffer.writeByte(this.boxStatus);

            try {
                byte[] bytes = Constant.EMPTY_BATTERY_BYTES;
                if(StringUtils.isNotEmpty(batteryId)) {
                    bytes = Hex.decodeHex(batteryId.toCharArray());
                }
                buffer.writeBytes(bytes);
            } catch (DecoderException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
