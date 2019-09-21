package cn.com.yusong.yhdg.vehicleserver.biz.server;

import cn.com.yusong.yhdg.common.constant.ConstEnum;
import cn.com.yusong.yhdg.common.constant.Constant;
import cn.com.yusong.yhdg.common.domain.zc.Vehicle;
import cn.com.yusong.yhdg.common.protocol.msg_tbit.Msg_S1;
import cn.com.yusong.yhdg.common.protocol.msg_tbit.Msg_T1;
import cn.com.yusong.yhdg.vehicleserver.comm.session.Session;
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
* 登录
*
 */
@Log4j2
@Component
public class BizT1 extends AbstractBiz {

    @Autowired
    VehicleService vehicleService;
    @Autowired
    VehicleOnlineStatsService vehicleOnlineStatsService;


    @Override
    public void doBiz(ChannelHandlerContext context, Map<String, Object> attributes, Object message) throws Exception {
        Msg_T1 request = (Msg_T1) message;
        Msg_S1 response = new Msg_S1();

        //加入缓存
        Session session = sessionManager.addVehicleSession(context, attributes, request.vinNo);
        updateLoginServer(session);

        //更新
        Vehicle vehicle = vehicleService.insertOrUpdate( request.vinNo, request.version);
        //在线统计
        if(vehicle != null){
            vehicleOnlineStatsService.begin(vehicle.getId());
        }

        response.time =  DateFormatUtils.format(new Date(), Constant.DATE_TIME_FORMAT);
        response.loginStatus = ConstEnum.Flag.TRUE.getValue();
        writeAndFlush(context, response);

    }
}
