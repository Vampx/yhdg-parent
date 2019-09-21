package cn.com.yusong.yhdg.agentappserver.web.controller.api.v1.agent.basic;

import cn.com.yusong.yhdg.agentappserver.constant.RespCode;
import cn.com.yusong.yhdg.agentappserver.entity.result.RestResult;
import cn.com.yusong.yhdg.agentappserver.service.basic.AgentService;
import cn.com.yusong.yhdg.agentappserver.service.basic.UserService;
import cn.com.yusong.yhdg.agentappserver.web.controller.api.v1.ApiController;
import cn.com.yusong.yhdg.common.constant.CacheKey;
import cn.com.yusong.yhdg.common.constant.ConstEnum;
import cn.com.yusong.yhdg.common.constant.Constant;
import cn.com.yusong.yhdg.common.domain.basic.Agent;
import cn.com.yusong.yhdg.common.domain.basic.User;
import cn.com.yusong.yhdg.common.tool.memcached.MemCachedClient;
import cn.com.yusong.yhdg.common.tool.memcached.MemCachedConfig;
import cn.com.yusong.yhdg.common.utils.IdUtils;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;
import javax.validation.constraints.Pattern;
import java.util.HashMap;
import java.util.Map;

@Controller("agent_api_v1_agent_basic_auth_code")
@RequestMapping(value = "/agent_api/v1/agent/basic/auth_code")
public class AuthCodeController extends ApiController {

    @Autowired
    MemCachedClient memCachedClient;
    @Autowired
    AgentService agentService;
    @Autowired
    UserService userService;

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ValidateParam {
        @Pattern(regexp = Constant.VALIDATOR_PATTERN_MOBILE, message = "手机号格式错误")
        public String mobile;
        @NotBlank(message = "验证码不能为空")
        public String authCode;
    }

    /**
     * 验证短信验证码(已登录)
     *
     * @param param
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/validate.htm")
    public RestResult validate(@Valid @RequestBody ValidateParam param) {
        int agentId = getTokenData().agentId;
        if (agentId == 0) {
            return RestResult.result(RespCode.CODE_2.getValue(), "用户信息错误");
        }
        Agent agent = agentService.find(agentId);
        if (agent == null) {
            return RestResult.result(RespCode.CODE_2.getValue(), "运营商不存在");
        }
        User loginUser = userService.find(getTokenData().userId);
        if (loginUser == null) {
            return RestResult.result(RespCode.CODE_2.getValue(), "该用户不存在");
        }
        if (loginUser.getIsAdmin() != ConstEnum.Flag.TRUE.getValue()) {
            return RestResult.result(RespCode.CODE_2.getValue(), "该用户非门店核心管理员");
        }

        if (!loginUser.getMobile().equals(param.mobile)) {
            return RestResult.result(RespCode.CODE_2.getValue(), "手机号错误");
        }

        String cached = (String) memCachedClient.get(CacheKey.key(CacheKey.K_MOBILE_V_AUTH_CODE, param.mobile));
        if (cached == null || !cached.contains(param.authCode)) {
            return RestResult.result(RespCode.CODE_2.getValue(), "验证码错误");
        }
        memCachedClient.delete(CacheKey.key(CacheKey.K_MOBILE_V_AUTH_CODE, param.mobile));

        String key = IdUtils.uuid();
        memCachedClient.set(CacheKey.key(CacheKey.K_AGENT_ID_V_UUID, agent.getId()), key, MemCachedConfig.CACHE_FIVE_MINUTE);
        Map data = new HashMap();
        data.put("key", key);

        return RestResult.dataResult(RespCode.CODE_0.getValue(), null, data);
    }
}
