package cn.com.yusong.yhdg.common.protocol.msg92;

import cn.com.yusong.yhdg.common.protocol.MsgCode;
import io.netty.buffer.ByteBuf;

import java.util.ArrayList;
import java.util.List;

/**
 * 查询未执行命令（响应）
 */
public class Msg922000025 extends Msg922 implements Interface922000025 {

    public List<Command> commandList = new ArrayList<Command>();

    @Override
    public int getMsgCode() {
        return MsgCode.MSG_922000025.getCode();
    }

    @Override
    public void readData(ByteBuf buffer) {
        readRtn(buffer);

        int size = buffer.readInt();
        for(int i = 0; i < size; i++) {
            Command command = new Command();
            command.read(buffer);
            commandList.add(command);
        }
    }

    @Override
    public void writeData(ByteBuf buffer) {
        writeRtn(buffer);

        buffer.writeInt(commandList.size());
        for(Command command : commandList) {
            command.write(buffer);
        }
    }
}
