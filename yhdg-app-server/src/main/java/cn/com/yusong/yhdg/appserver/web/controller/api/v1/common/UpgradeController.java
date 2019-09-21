package cn.com.yusong.yhdg.appserver.web.controller.api.v1.common;


import cn.com.yusong.yhdg.appserver.config.AppConfig;
import cn.com.yusong.yhdg.appserver.constant.RespCode;
import cn.com.yusong.yhdg.appserver.entity.result.RestResult;
import cn.com.yusong.yhdg.appserver.service.basic.UpgradePackService;
import cn.com.yusong.yhdg.appserver.web.controller.api.v1.ApiController;
import cn.com.yusong.yhdg.common.annotation.NotLogin;
import cn.com.yusong.yhdg.common.domain.basic.UpgradePack;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

@Controller("api_v1_common_basic_upgrade")
@RequestMapping(value = "/api/v1/common/basic/upgrade")
public class UpgradeController extends ApiController {

    @Autowired
    UpgradePackService upgradePackService;

    @Autowired
    AppConfig appConfig;

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ListParam {
        public int type;
    }

    @NotLogin
    @ResponseBody
    @RequestMapping(value = "/latest_version")
    public RestResult getVersion(@RequestBody ListParam param) {
        UpgradePack pack = upgradePackService.find(param.type);
        Map map = new HashMap();
        if (pack != null) {
            map.put("version", pack.getVersion());
            map.put("memo", pack.getMemo());
            map.put("url", appConfig.getStaticUrl() + pack.getFilePath());
            map.put("isForce", pack.getIsForce());
        }
        return RestResult.dataResult(RespCode.CODE_0.getValue(), "", map);
    }
}
