package cn.com.yusong.yhdg.appserver.web.controller.api.v1.cabinetapp.yms;

import cn.com.yusong.yhdg.appserver.config.TokenCache;
import cn.com.yusong.yhdg.appserver.constant.RespCode;
import cn.com.yusong.yhdg.appserver.entity.result.RestResult;
import cn.com.yusong.yhdg.appserver.persistence.yms.PlayListDetailMapper;
import cn.com.yusong.yhdg.appserver.service.basic.AgentSystemConfigService;
import cn.com.yusong.yhdg.appserver.service.basic.CustomerService;
import cn.com.yusong.yhdg.appserver.service.hdg.CabinetService;
import cn.com.yusong.yhdg.appserver.service.hdg.InsuranceOrderService;
import cn.com.yusong.yhdg.appserver.service.hdg.InsuranceService;
import cn.com.yusong.yhdg.appserver.service.yms.PlaylistDetailService;
import cn.com.yusong.yhdg.appserver.service.yms.TerminalService;
import cn.com.yusong.yhdg.appserver.web.controller.api.v1.ApiController;
import cn.com.yusong.yhdg.common.constant.ConstEnum;
import cn.com.yusong.yhdg.common.domain.hdg.Cabinet;
import cn.com.yusong.yhdg.common.domain.hdg.Insurance;
import cn.com.yusong.yhdg.common.domain.hdg.InsuranceOrder;
import cn.com.yusong.yhdg.common.domain.yms.PlayListDetail;
import cn.com.yusong.yhdg.common.domain.yms.Terminal;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.*;

@Controller("api_v1_cabinet_app_yms_playlist")
@RequestMapping(value = "/api/v1/cabinet_app/yms/playlist")
public class PlaylistController extends ApiController {

    static final Logger log = LogManager.getLogger(PlaylistController.class);

    @Autowired
    PlaylistDetailService playlistDetailService;
    @Autowired
    CabinetService cabinetService;
    @Autowired
    TerminalService terminalService;


    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class DetailParam {
        @NotNull
        public int playlistId;
    }

    @ResponseBody
    @RequestMapping("/detail.htm")
    public RestResult list(@Valid @RequestBody DetailParam param) {
        TokenCache.Data tokenData = getTokenData();
        long customerId = tokenData.customerId;

        List returnList = new ArrayList();

        List<PlayListDetail> list = playlistDetailService.findListByPlaylistId( param.playlistId);
        for(PlayListDetail playListDetail : list){
            Map map = new HashMap();
            map.put("orderNum", playListDetail.getOrderNum());
            map.put("materialType", playListDetail.getMaterialType());
            map.put("materialName",playListDetail.getMaterialName());
            map.put("duration", playListDetail.getDuration());
            String filePath = playListDetail.getFilePath().replace("/static/download", "");
            map.put("filePath", filePath);
            map.put("size", playListDetail.getSize());
            map.put("md5Sum", playListDetail.getMd5Sum());
            returnList.add(map);
        }

        return RestResult.dataResult(RespCode.CODE_0.getValue(), null, returnList);
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class UpgradeParam {
        @NotNull
        public int playlistId;
        @NotNull
        public String playlistVersion;
    }

    @ResponseBody
    @RequestMapping("/upgrade.htm")
    public RestResult upgrade(@Valid @RequestBody UpgradeParam param) {
        TokenCache.Data tokenData = getTokenData();
        Cabinet cabinet = cabinetService.find(tokenData.cabinetId);
        if (cabinet == null) {
            return RestResult.result(RespCode.CODE_2.getValue(), "对应终端不存在");
        }
        if(cabinet.getTerminalId() != null) {
            Terminal terminal = new Terminal();
            terminal.setId(cabinet.getTerminalId());
            terminal.setHeartPlaylistId(param.playlistId);
            terminal.setHeartPlaylistVersion(param.playlistVersion);
            terminalService.updateHeartPlaylistId(terminal);
        }
        return RestResult.dataResult(RespCode.CODE_0.getValue(), null, null);
    }


}

