package cn.com.yusong.yhdg.common.protocol.msg92;

import cn.com.yusong.yhdg.common.protocol.MsgCode;
import io.netty.buffer.ByteBuf;

import java.util.ArrayList;
import java.util.List;

/**
 * 上报下载进度（播放列表）（响应）
 */
public class Msg922000012 extends Msg922 implements Interface922000012 {

    public int id;
    public int version;
    public List<Detail> detailList = new ArrayList<Detail>();
    @Override
    public int getMsgCode() {
        return MsgCode.MSG_922000012.getCode();
    }

    @Override
    public void readData(ByteBuf buffer) {
        readRtn(buffer);
        id = buffer.readInt();
        version = buffer.readInt();
        int size = buffer.readInt();
        for(int i = 0; i < size; i++) {
            Detail detail = new Detail();
            detail.read(buffer);
            detailList.add(detail);
        }
    }

    @Override
    public void writeData(ByteBuf buffer) {
        writeRtn(buffer);
        buffer.writeInt(id);
        buffer.writeInt(version);
        buffer.writeInt(detailList.size());
        for(Detail detail : detailList) {
            detail.write(buffer);
        }
    }
}
