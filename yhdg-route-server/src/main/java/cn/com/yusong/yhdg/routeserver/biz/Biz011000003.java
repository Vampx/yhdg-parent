package cn.com.yusong.yhdg.routeserver.biz;

import cn.com.yusong.yhdg.common.domain.hdg.Cabinet;
import cn.com.yusong.yhdg.common.domain.hdg.CabinetCode;
import cn.com.yusong.yhdg.routeserver.config.AppConfig;
import cn.com.yusong.yhdg.routeserver.config.ServerInfo;
import cn.com.yusong.yhdg.routeserver.constant.RespCode;
import cn.com.yusong.yhdg.routeserver.protocol.Msg011000003;
import cn.com.yusong.yhdg.routeserver.protocol.Msg012000003;
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
public class Biz011000003 extends BizHandler {

    @Autowired
    CabinetCodeService cabinetCodeService;
    @Autowired
    CabinetService cabinetService;
    @Autowired
    AppConfig config;

    @Override
    protected void biz(HttpServletRequest request, HttpServletResponse response, RequestMessage message) throws Exception {
        Msg011000003 msg = (Msg011000003) message;
        Msg012000003 resp = new Msg012000003();
//        if (config.getExpiryTime() != null && config.getExpiryTime() < new Date().getTime()) {
//            resp.setRtnCode(RespCode.CODE_2.getValue());
//            resp.setRtnMsg("服务授权已过期");
//            write(request, response, resp);
//            return;
//        }
        if(StringUtils.isEmpty(msg.getCode())) {
            resp.setRtnCode(RespCode.CODE_2.getValue());
            resp.setRtnMsg("编号是空");
            write(request, response, resp);
            return;
        }

        CabinetCode cabinetCode = cabinetCodeService.find(msg.getCode());
        if(cabinetCode != null) {
            Cabinet cabinet = cabinetService.find(cabinetCode.getCode());
            if(cabinet != null && validSim(msg.getCcid()) && !msg.getCcid().equals(StringUtils.trimToEmpty(cabinet.getSimMemo()))) {
                cabinetService.updateSimMemo(cabinet.getId(), StringUtils.trimToEmpty(cabinet.getSimMemo()),  StringUtils.trimToEmpty(msg.getCcid()));
            }
        }

        ServerInfo server = config.selectOneCabinetServer();

        if(server == null) {
            resp.setRtnCode(RespCode.CODE_4.getValue());
        } else {
            resp.setIp(server.ip);
            resp.setPort(server.port);
        }
        write(request, response, resp);
    }

    private boolean validSim(String text) {
        if(StringUtils.isEmpty(text)) {
            return false;
        }

        char[] chars = text.toCharArray();
        for(char c : chars) {
            if(c != '0') {
                return true;
            }
        }

        return false;
    }
}
