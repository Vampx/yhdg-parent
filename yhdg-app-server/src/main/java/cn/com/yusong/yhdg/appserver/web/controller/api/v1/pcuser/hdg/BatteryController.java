package cn.com.yusong.yhdg.appserver.web.controller.api.v1.pcuser.hdg;

import cn.com.yusong.yhdg.appserver.constant.RespCode;
import cn.com.yusong.yhdg.appserver.entity.result.RestResult;
import cn.com.yusong.yhdg.appserver.service.hdg.BatteryService;
import cn.com.yusong.yhdg.appserver.web.controller.api.v1.ApiController;
import cn.com.yusong.yhdg.common.annotation.NotLogin;
import cn.com.yusong.yhdg.common.domain.basic.Agent;
import cn.com.yusong.yhdg.common.domain.hdg.Battery;
import cn.com.yusong.yhdg.common.entity.json.ExtResult;
import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.constraints.NotNull;

@Controller("api_v1_pc_user_hdg_battery")
@RequestMapping(value = "/api/v1/pc_user/hdg/battery")
public class BatteryController extends ApiController {

    @Autowired
    BatteryService batteryService;

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class CreateParam {
        @NotNull
        public String id;
        @NotNull
        public String code;
        public String qrcode;
        public String shellcode;
        @NotNull
        public int agentId;
    }

    @NotLogin
    @ResponseBody
    @RequestMapping(value = "/create.htm")
    public RestResult create(@RequestBody CreateParam param) {
        Agent agent = agentService.find(param.agentId);
        if (agent == null) {
            return RestResult.result(RespCode.CODE_2.getValue(), "运营商不存在");
        }
        if (StringUtils.isEmpty(param.code)) {
            return RestResult.result(RespCode.CODE_2.getValue(), "IMEI不可为空");
        }
        ExtResult extResult = batteryService.create(param.id, param.code, param.qrcode, param.shellcode, param.agentId, 2);
        if (!extResult.isSuccess()) {
            return RestResult.result(RespCode.CODE_2.getValue(), extResult.getMessage());
        }
        return RestResult.result(RespCode.CODE_0.getValue(), null);
    }
}
