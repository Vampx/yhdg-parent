package cn.com.yusong.yhdg.agentappserver.web.controller.api.v1.shop.basic;

import cn.com.yusong.yhdg.agentappserver.config.TokenCache;
import cn.com.yusong.yhdg.agentappserver.constant.RespCode;
import cn.com.yusong.yhdg.agentappserver.entity.result.RestResult;
import cn.com.yusong.yhdg.agentappserver.service.basic.*;
import cn.com.yusong.yhdg.agentappserver.service.hdg.*;
import cn.com.yusong.yhdg.agentappserver.web.controller.api.v1.ApiController;
import cn.com.yusong.yhdg.common.constant.CacheKey;
import cn.com.yusong.yhdg.common.constant.ConstEnum;
import cn.com.yusong.yhdg.common.domain.basic.*;
import cn.com.yusong.yhdg.common.domain.hdg.*;
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
import java.util.HashMap;
import java.util.Map;


@Controller("agent_api_v1_shop_basic_customer")
@RequestMapping(value = "/agent_api/v1/shop/basic/customer")
public class CustomerController extends ApiController {
    @Autowired
    MemCachedClient memCachedClient;
    @Autowired
    CustomerService customerService;
    @Autowired
    UserService userService;
    @Autowired
    ShopService shopService;
    @Autowired
    SystemConfigService systemConfigService;
    @Autowired
    AgentService agentService;

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ShopPayMobileListParam {
        public String mobile;
        public int offset;
        public int limit;
    }

    /**
     * 103-查询门店收款人手机号列表
     * <p>
     */
    @ResponseBody
    @RequestMapping("/shop_pay_mobile_list.htm")
    public RestResult shopPayMobileList(@Valid @RequestBody ShopPayMobileListParam param) {
        TokenCache.Data tokenData = getTokenData();
        int agentId = tokenData.agentId;
        if (agentId == 0) {
            return RestResult.result(RespCode.CODE_2.getValue(), "用户信息错误");
        }
        Agent agent = agentService.find(agentId);
        if (agent == null) {
            return RestResult.result(RespCode.CODE_2.getValue(), "运营商不存在");
        }
        return customerService.findMobileList(tokenData.agentId, agent.getPartnerId(), param.mobile, param.offset, param.limit);
    }


    /**
     * 105-查询门店收款人设置自己信息
     * <p>
     */
    @ResponseBody
    @RequestMapping("/shop_pay_mobile_info.htm")
    public RestResult shopPayMobileInfo() {
        TokenCache.Data tokenData = getTokenData();
        User loginUser = userService.find(tokenData.userId);
        if (loginUser == null) {
            return RestResult.result(RespCode.CODE_2.getValue(), "该用户不存在");
        }
        Agent agent = agentService.find(loginUser.getAgentId());
        if (agent == null) {
            return RestResult.result(RespCode.CODE_2.getValue(), "运营商不存在");
        }
        return customerService.findMobileInfo(agent.getPartnerId(), loginUser.getMobile());
    }


    /**
     * 106-查询门店余额支付密码
     */
    @ResponseBody
    @RequestMapping(value = "/info_balance.htm")
    public RestResult infoBalance() {
        TokenCache.Data tokenData = getTokenData();
        User loginUser = userService.find(tokenData.userId);
        if (loginUser == null) {
            return RestResult.result(RespCode.CODE_2.getValue(), "该用户不存在");
        }
        if (loginUser.getIsAdmin() != ConstEnum.Flag.TRUE.getValue()) {
            return RestResult.result(RespCode.CODE_2.getValue(), "该用户非门店核心管理员");
        }
        Shop shop = shopService.find(tokenData.shopId);
        if (shop == null){
            return RestResult.dataResult(RespCode.CODE_2.getValue(),"门店不存在",null);
        }
        if (StringUtils.isEmpty(shop.getPayPeopleMobile())){
            return RestResult.dataResult(RespCode.CODE_2.getValue(),"门店收款人不存在，请先设置收款人",null);
        }
        Agent agent = agentService.find(loginUser.getAgentId());
        if (agent == null) {
            return RestResult.result(RespCode.CODE_2.getValue(), "运营商不存在");
        }

        Customer customer = customerService.findByMobile(agent.getPartnerId(), shop.getPayPeopleMobile());

        int hasPayPassword = 1;
        if (StringUtils.isEmpty(shop.getPayPassword())) {
            hasPayPassword = 0;
        }
        Map map = new HashMap();
        map.put("balance", shop.getBalance());
        map.put("hasPayPassword", hasPayPassword);
        map.put("alipayAccount", shop.getPayPeopleFwOpenId());
        map.put("nickname", customer == null ? "" : customer.getNickname());
        map.put("fullname", customer == null ? "" : customer.getFullname());
        map.put("withdrawRatio", systemConfigService.findConfigValue(ConstEnum.SystemConfigKey.WITHDRAW_RATIO.getValue()));

        return RestResult.dataResult(RespCode.CODE_0.getValue(), null, map);
    }


    /**
     * 107-设置门店提现支付密码
     */

    public static class SetPayPassword {
        public String payPassword;
    }

    @ResponseBody
    @RequestMapping(value = "/set_pay_password.htm")
    public RestResult setPayPassword(@Valid @RequestBody SetPayPassword param) {
        TokenCache.Data tokenData = getTokenData();
        User loginUser = userService.find(tokenData.userId);
        if (loginUser == null) {
            return RestResult.result(RespCode.CODE_2.getValue(), "该用户不存在");
        }
        Shop shop = shopService.find(tokenData.shopId);
        if (shop == null){
            return RestResult.dataResult(RespCode.CODE_2.getValue(),"门店不存在",null);
        }
        shopService.setPayPassword(shop.getId(), param.payPassword);
        return RestResult.result(RespCode.CODE_0.getValue(), null);
    }


    /**
     * 130-修改门店体现支付密码
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
        Shop shop = shopService.find(tokenData.shopId);
        if (shop == null){
            return RestResult.dataResult(RespCode.CODE_2.getValue(),"门店不存在",null);
        }

        String uuid = (String) memCachedClient.get(CacheKey.key(CacheKey.K_SHOP_ID_V_UUID, shop.getId()));
        if (!param.key.equals(uuid)) {
            return RestResult.result(RespCode.CODE_2.getValue(), "密钥验证失败");
        }

        shopService.setPayPassword(shop.getId(), param.payPassword);

        return RestResult.result(RespCode.CODE_0.getValue(), null);
    }

    /**
     * 131-修改支付密码
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
        Shop shop = shopService.find(tokenData.shopId);
        if (shop == null){
            return RestResult.dataResult(RespCode.CODE_2.getValue(),"门店不存在",null);
        }
        if (StringUtils.isEmpty(shop.getPayPassword())) {
            return RestResult.result(RespCode.CODE_2.getValue(), "原密码没有设置");
        }
        if (!shop.getPayPassword().equals(param.oldPassword)) {
            return RestResult.result(RespCode.CODE_2.getValue(), "原密码错误");
        }

        shopService.setPayPassword(shop.getId(), param.payPassword);

        return RestResult.result(RespCode.CODE_0.getValue(), null);
    }

}
