package cn.com.yusong.yhdg.vehicleserver.biz.server;

import cn.com.yusong.yhdg.common.constant.ConstEnum;
import cn.com.yusong.yhdg.common.constant.Constant;
import cn.com.yusong.yhdg.common.domain.zc.Vehicle;
import cn.com.yusong.yhdg.common.protocol.msg_tbit.Msg_S0;
import cn.com.yusong.yhdg.common.protocol.msg_tbit.Msg_S1;
import cn.com.yusong.yhdg.common.protocol.msg_tbit.Msg_T0;
import cn.com.yusong.yhdg.common.protocol.msg_tbit.Msg_T1;
import cn.com.yusong.yhdg.vehicleserver.comm.session.Session;
import cn.com.yusong.yhdg.vehicleserver.comm.session.VehicleSession;
import cn.com.yusong.yhdg.vehicleserver.constant.RespCode;
import cn.com.yusong.yhdg.vehicleserver.service.zc.VehicleOnlineStatsService;
import cn.com.yusong.yhdg.vehicleserver.service.zc.VehicleService;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Map;

/*
* 心跳
*
 */
@Log4j2
@Component
public class BizT0 extends AbstractBiz {

    @Autowired
    VehicleService vehicleService;
    @Autowired
    VehicleOnlineStatsService vehicleOnlineStatsService;



    @Override
    public void doBiz(ChannelHandlerContext context, Map<String, Object> attributes, Object message) throws Exception {
        Msg_T0 request = (Msg_T0) message;
        Msg_S0 response = new Msg_S0();

        VehicleSession session = sessionManager.getVehicleSession(request.vinNo);
        if (session == null) {
            //有心跳，没登录。与设备关联的通道改为心跳通道
//            Vehicle vehicle = vehicleService.findByVinNo(request.vinNo);
//            if(vehicle != null){
//                vehicleOnlineStatsService.begin(vehicle.getId());
//
//                //加入缓存
//                session = sessionManager.addVehicleSession(context, attributes, request.vinNo);
//            }

            //没有登录，心跳不保存
            return;
        }

        //更新
        updateLoginServer(session);
        vehicleService.updateHeart( request.vinNo);
        writeAndFlush(context, response);

    }
}
