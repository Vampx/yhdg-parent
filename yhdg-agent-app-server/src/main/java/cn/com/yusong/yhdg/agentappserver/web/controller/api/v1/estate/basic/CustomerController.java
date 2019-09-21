package cn.com.yusong.yhdg.agentappserver.web.controller.api.v1.estate.basic;

import cn.com.yusong.yhdg.agentappserver.config.TokenCache;
import cn.com.yusong.yhdg.agentappserver.constant.RespCode;
import cn.com.yusong.yhdg.agentappserver.entity.result.RestResult;
import cn.com.yusong.yhdg.agentappserver.service.basic.AgentService;
import cn.com.yusong.yhdg.agentappserver.service.basic.CustomerService;
import cn.com.yusong.yhdg.agentappserver.service.basic.SystemConfigService;
import cn.com.yusong.yhdg.agentappserver.service.basic.UserService;
import cn.com.yusong.yhdg.agentappserver.service.hdg.EstateService;
import cn.com.yusong.yhdg.agentappserver.web.controller.api.v1.ApiController;
import cn.com.yusong.yhdg.common.constant.CacheKey;
import cn.com.yusong.yhdg.common.domain.hdg.Estate;
import cn.com.yusong.yhdg.common.tool.memcached.MemCachedClient;
import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;


@Controller("agent_api_v1_estate_basic_customer")
@RequestMapping(value = "/agent_api/v1/estate/basic/customer")
public class CustomerController extends ApiController {
    @Autowired
    MemCachedClient memCachedClient;
    @Autowired
    CustomerService customerService;
    @Autowired
    UserService userService;
    @Autowired
    EstateService estateService;
    @Autowired
    SystemConfigService systemConfigService;
    @Autowired
    AgentService agentService;

    /**
     * 11-物业支付忘记密码
     */

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class UpdatePayPasswordParam {
        @NotBlank(message = "Key不能为空")
        public String key;
        @NotBlank(message = "密码不能为空")
        public String payPassword;
    }

    @ResponseBody
    @RequestMapping(value = "/update_pay_password.htm")
    public RestResult UpdatePayPassword(@Valid @RequestBody UpdatePayPasswordParam param) {
        TokenCache.Data tokenData = getTokenData();
        Estate estate = estateService.find(tokenData.estateId);
        if (estate == null){
            return RestResult.dataResult(RespCode.CODE_2.getValue(),"物业不存在",null);
        }

        String uuid = (String) memCachedClient.get(CacheKey.key(CacheKey.K_ESTATE_ID_V_UUID, estate.getId()));
        if (!param.key.equals(uuid)) {
            return RestResult.result(RespCode.CODE_2.getValue(), "密钥验证失败");
        }

        estateService.setPayPassword(estate.getId(), param.payPassword);

        return RestResult.result(RespCode.CODE_0.getValue(), null);
    }

    /**
     * 12-修改物业支付密码
     */

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class UpdatePayPassword2Param {
        public String oldPassword;
        public String payPassword;
    }

    @ResponseBody
    @RequestMapping(value = "/update_pay_password2.htm")
    public RestResult UpdatePayPassword2(@Valid @RequestBody UpdatePayPassword2Param param) {
        TokenCache.Data tokenData = getTokenData();
        int agentId = tokenData.agentId;
        if (agentId == 0) {
            return RestResult.result(RespCode.CODE_2.getValue(), "用户信息错误");
        }
        Estate estate = estateService.find(tokenData.estateId);
        if (estate == null){
            return RestResult.dataResult(RespCode.CODE_2.getValue(),"物业不存在",null);
        }
        if (StringUtils.isEmpty(estate.getPayPassword())) {
            return RestResult.result(RespCode.CODE_2.getValue(), "原密码没有设置");
        }
        if (!estate.getPayPassword().equals(param.oldPassword)) {
            return RestResult.result(RespCode.CODE_2.getValue(), "原密码错误");
        }

        estateService.setPayPassword(estate.getId(), param.payPassword);

        return RestResult.result(RespCode.CODE_0.getValue(), null);
    }

}
