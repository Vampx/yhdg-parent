package cn.com.yusong.yhdg.routeserver.biz;

import cn.com.yusong.yhdg.common.protocol.msg01.Msg011000004;
import cn.com.yusong.yhdg.common.protocol.msg01.Msg012000004;
import cn.com.yusong.yhdg.common.utils.CodecUtils;
import cn.com.yusong.yhdg.routeserver.config.ServerInfo;
import cn.com.yusong.yhdg.routeserver.constant.RespCode;
import cn.com.yusong.yhdg.routeserver.protocol.Msg011000001;
import cn.com.yusong.yhdg.routeserver.protocol.Msg012000001;
import io.netty.channel.ChannelHandlerContext;
import org.springframework.stereotype.Component;

import java.util.Map;

/*
 * 查询业务服务器
 */
@Component
public class Biz011000004 extends AbstractBiz {

    @Override
    public void doBiz(ChannelHandlerContext context, Map<String, Object> attributes, Object message) throws Exception {
        Msg011000004 request = (Msg011000004) message;
        Msg012000004 response = new Msg012000004();
        response.setSerial(request.getSerial());

        //校验
        String sign = CodecUtils.signMd5(request.json);
        if(!sign.equals(request.sign)){
            response.rtnCode = RespCode.CODE_5.getValue();
            writeAndClose(context, response);
            return;
        }

        ServerInfo server = config.selectOneBatteryServer();
        //组装包体json格式
        if(server != null){
            response.json.ip = server.ip;
            response.json.port = server.port;
        }

        writeAndFlush(context, response);
    }
}
