package cn.com.yusong.yhdg.cabinetserver.biz.server;

import cn.com.yusong.yhdg.cabinetserver.comm.session.CabinetSession;
import cn.com.yusong.yhdg.common.constant.TsRespCode;
import cn.com.yusong.yhdg.common.domain.hdg.Cabinet;
import cn.com.yusong.yhdg.common.protocol.msg07.Msg071000001;
import cn.com.yusong.yhdg.common.protocol.msg07.Msg071000003;
import cn.com.yusong.yhdg.common.protocol.msg22.Msg221000004;
import cn.com.yusong.yhdg.common.protocol.msg22.Msg222000004;
import io.netty.channel.ChannelHandlerContext;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 开门通知 中转包
 */
@Component
public class Biz221000004 extends AbstractBiz implements HighPriorityBiz {

    @Override
    public void doBiz(ChannelHandlerContext context, Map<String, Object> attributes, Object message) throws Exception {
        Msg221000004 request = (Msg221000004) message;
        Msg222000004 response = new Msg222000004();
        response.setSerial(request.getSerial());

        CabinetSession session = sessionManager.getCabinetSession(request.cabinetId);
        if (session != null) {
            if(session.heartBizType == CabinetSession.NEW_CABINET_HERT){
                Cabinet cabinet = cabinetService.find(request.cabinetId);
                Msg071000003 msg = new Msg071000003();
                msg.code = cabinet.getMac();
                msg.lockNum = request.lockNum;
                msg.boxType = request.boxType;
                msg.openType = 2; //先写死为2
                msg.setSerial(request.getSerial());

                respPool.put(msg.getSerial(), new Resp(context, attributes, request));
                session.writeAndFlush(msg);
            }else{
                Msg071000001 msg = new Msg071000001();
                msg.lockNum = request.lockNum;
                msg.boxType = request.boxType;
                msg.setSerial(request.getSerial());

                respPool.put(msg.getSerial(), new Resp(context, attributes, request));
                session.writeAndFlush(msg);

            }

        } else {
            response.rtnCode = TsRespCode.CODE_3.getValue();
            context.writeAndFlush(response);
        }
    }
}
