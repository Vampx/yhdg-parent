package cn.com.yusong.yhdg.routeserver.biz;

import cn.com.yusong.yhdg.routeserver.config.AppConfig;
import cn.com.yusong.yhdg.routeserver.config.ServerInfo;
import cn.com.yusong.yhdg.routeserver.constant.RespCode;
import cn.com.yusong.yhdg.routeserver.protocol.Msg011000001;
import cn.com.yusong.yhdg.routeserver.protocol.Msg012000001;
import cn.com.yusong.yhdg.routeserver.protocol.RequestMessage;
import cn.com.yusong.yhdg.routeserver.service.hdg.CabinetCodeService;
import cn.com.yusong.yhdg.routeserver.service.hdg.CabinetService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

@Service
public class Biz011000001 extends BizHandler {

    @Autowired
    CabinetCodeService cabinetCodeService;
    @Autowired
    CabinetService cabinetService;
    @Autowired
    AppConfig config;

    @Override
    protected void biz(HttpServletRequest request, HttpServletResponse response, RequestMessage message) throws Exception {

        Msg011000001 msg = (Msg011000001) message;
        Msg012000001 resp = new Msg012000001();
//        if (config.getExpiryTime() != null && config.getExpiryTime() < new Date().getTime()) {
//            resp.setRtnCode(RespCode.CODE_2.getValue());
//            resp.setRtnMsg("服务授权已过期");
//            write(request, response, resp);
//            return;
//        }
        if (StringUtils.isEmpty(msg.getCode())) {
            resp.setRtnCode(RespCode.CODE_2.getValue());
            resp.setRtnMsg("编号是空");
            write(request, response, resp);
            return;
        }

        ServerInfo server = config.selectOneFrontServer();

        if (server == null) {
            resp.setRtnCode(RespCode.CODE_4.getValue());
        } else {
            resp.setIp(server.ip);
            resp.setPort(server.port);
        }
        write(request, response, resp);
    }
}
