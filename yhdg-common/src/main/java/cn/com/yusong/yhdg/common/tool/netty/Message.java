package cn.com.yusong.yhdg.common.tool.netty;

import io.netty.buffer.ByteBuf;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

public abstract class Message extends TypeOperator {
    private int serial;

    public abstract int getMsgCode();
    public void readData(ByteBuf buffer) {
    }
    public void writeData(ByteBuf buffer) {
    }
    public void readTime(ByteBuf buffer) {
    }
    public void writeTime(ByteBuf buffer) {
    }
    public boolean checkCRC() {
        return false;
    }

    public final void decode(ByteBuf buffer) {
        int msgCode = buffer.readInt();
        assert msgCode == getMsgCode();
        readTime(buffer);
        serial = buffer.readInt();

        buffer.readInt(); //包体长度
        readData(buffer);
    }
    public final void encode(ByteBuf buffer) {

        if(getMsgCode() == 0) {
            throw new IllegalArgumentException("msgCode is zero");
        }


        buffer.writeInt(getMsgCode());
        writeTime(buffer);
        buffer.writeInt(serial);

        int position = buffer.writerIndex();
        buffer.writeInt(0);

        //是否校验异或
        if(checkCRC()) buffer.writeShort(0);

        writeData(buffer);

        if(checkCRC()){
            writeCRC(buffer, position + 4);

            buffer.setInt(position, buffer.writerIndex() - position - 4 - 2);
        }else{
            buffer.setInt(position, buffer.writerIndex() - position - 4);
        }

    }

    public void writeCRC(ByteBuf buffer, int position){
        int bodyLength = buffer.writerIndex() - position - 2;
        ByteBuf buf = buffer.slice(position + 2, bodyLength);//复制包体
        byte[] crcBytes = new byte[bodyLength];//定义包体字节
        buf.readBytes(crcBytes);//写入
        String checkSumHex = getCRC(crcBytes) ;//获取到异或
        byte[] bytes = null;
        try {
            bytes = Hex.decodeHex(checkSumHex.toCharArray());
        } catch (DecoderException e) {
            e.printStackTrace();
        }
        buffer.setBytes(position, bytes);//写入异或
    }

    public int getSerial() {
        return serial;
    }

    public void setSerial(int serial) {
        this.serial = serial;
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.MULTI_LINE_STYLE);
    }
}
