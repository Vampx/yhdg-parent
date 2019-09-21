package cn.com.yusong.yhdg.vehicleserver.biz.server;

import cn.com.yusong.yhdg.common.constant.ConstEnum;
import cn.com.yusong.yhdg.common.constant.Constant;
import cn.com.yusong.yhdg.common.domain.zc.Vehicle;
import cn.com.yusong.yhdg.common.protocol.msg_tbit.Msg_S1;
import cn.com.yusong.yhdg.common.protocol.msg_tbit.Msg_S3;
import cn.com.yusong.yhdg.common.protocol.msg_tbit.Msg_T1;
import cn.com.yusong.yhdg.common.protocol.msg_tbit.Msg_T3;
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
* 轨迹实时上传(暂不处理)
*
 */
@Log4j2
@Component
public class BizT3 extends AbstractBiz {



    @Override
    public void doBiz(ChannelHandlerContext context, Map<String, Object> attributes, Object message) throws Exception {
        Msg_T3 request = (Msg_T3) message;
        Msg_S3 response = new Msg_S3();

        writeAndFlush(context, response);

    }
}
