package cn.com.yusong.yhdg.common.protocol.msg22;

import cn.com.yusong.yhdg.common.protocol.MsgCode;
import cn.com.yusong.yhdg.common.protocol.msg07.Msg072;
import cn.com.yusong.yhdg.common.protocol.msg07.Msg072000002;
import cn.com.yusong.yhdg.common.tool.netty.TypeOperator;
import io.netty.buffer.ByteBuf;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;

import java.util.ArrayList;
import java.util.List;

/**
 * 开关门查询通知
 */
public class Msg222000005 extends Msg222 {

    public List<Msg072000002.Box> boxList = new ArrayList<Msg072000002.Box>();

    @Override
    public int getMsgCode() {
        return MsgCode.MSG_222000005.getCode();
    }

    @Override
    public void readData(ByteBuf buffer) {
        readRtn(buffer);

        int boxSize = buffer.readInt();
        for(int i = 0; i < boxSize; i++) {
            Msg072000002.Box box = new Msg072000002.Box();
            box.read(buffer);
            boxList.add(box);
        }
    }

    @Override
    public void writeData(ByteBuf buffer) {
        writeRtn(buffer);

        buffer.writeInt(boxList.size());
        for(Msg072000002.Box box : boxList) {
            box.write(buffer);
        }
    }

}
