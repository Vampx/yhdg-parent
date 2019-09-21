package cn.com.yusong.yhdg.agentappserver.web.controller.api.v1.shop.hdg;

import cn.com.yusong.yhdg.agentappserver.service.basic.AgentService;
import cn.com.yusong.yhdg.agentappserver.service.basic.OrderIdService;
import cn.com.yusong.yhdg.agentappserver.service.basic.SystemConfigService;
import cn.com.yusong.yhdg.agentappserver.service.basic.UserService;
import cn.com.yusong.yhdg.agentappserver.service.hdg.ShopDepositOrderService;
import cn.com.yusong.yhdg.agentappserver.config.TokenCache;
import cn.com.yusong.yhdg.agentappserver.constant.RespCode;
import cn.com.yusong.yhdg.agentappserver.entity.result.RestResult;
import cn.com.yusong.yhdg.agentappserver.service.hdg.ShopService;
import cn.com.yusong.yhdg.agentappserver.web.controller.api.v1.ApiController;
import cn.com.yusong.yhdg.common.constant.ConstEnum;
import cn.com.yusong.yhdg.common.domain.basic.Agent;
import cn.com.yusong.yhdg.common.domain.basic.CustomerDepositOrder;
import cn.com.yusong.yhdg.common.domain.basic.OrderId;
import cn.com.yusong.yhdg.common.domain.basic.User;
import cn.com.yusong.yhdg.common.domain.hdg.Shop;
import cn.com.yusong.yhdg.common.domain.hdg.ShopDepositOrder;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;
import java.io.IOException;
import java.util.Date;

@Controller("api_v1_shop_hdg_shop_deposit_order")
@RequestMapping(value = "/api/v1/shop/hdg/shop_deposit_order")
public class ShopDepositOrderController extends ApiController {


    @Autowired
    AgentService agentService;
    @Autowired
    ShopService shopService;
    @Autowired
    OrderIdService orderIdService;
    @Autowired
    ShopDepositOrderService shopDepositOrderService;
    @Autowired
    SystemConfigService systemConfigService;
    @Autowired
    UserService userService;

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class CreateByWeixinParam {
        public int money;
    }

    /**
     * 1-充值(微信公众号)
     * 门店充值(公众号)
     * @param param
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/create_by_weixin_mp")
    public RestResult createByWeixinMp(@Valid @RequestBody CreateByWeixinParam param) {
        TokenCache.Data tokenData = getTokenData();
        String shopId = tokenData.shopId;
        int agentId = tokenData.agentId;
        if (agentId == 0) {
            return RestResult.result(RespCode.CODE_2.getValue(), "用户信息错误");
        }
        User user = userService.find(tokenData.userId);
        if (user == null) {
            return RestResult.result(RespCode.CODE_2.getValue(), "账号不存在");
        }
        Agent agent = agentService.find(agentId);
        if (agent == null) {
            return RestResult.result(RespCode.CODE_2.getValue(), "运营商不存在");
        }
        Shop shop = shopService.find(shopId);
        if (shop == null) {
            return RestResult.result(RespCode.CODE_2.getValue(), "门店不存在");
        }
        if (param.money <= 0) {
            return RestResult.result(RespCode.CODE_2.getValue(), "充值金额有误");
        }

        ShopDepositOrder shopDepositOrder = new ShopDepositOrder();
        shopDepositOrder.setId(orderIdService.newOrderId(OrderId.OrderIdType.CUSTOMER_DEPOSIT_ORDER));
        shopDepositOrder.setPartnerId(agent.getPartnerId());
        shopDepositOrder.setMoney(param.money);
        shopDepositOrder.setGift(0);
        shopDepositOrder.setShopId(shopId);
        shopDepositOrder.setShopName(shop.getShopName());
        shopDepositOrder.setStatus(CustomerDepositOrder.Status.NOT.getValue());
        shopDepositOrder.setCreateTime(new Date());
        shopDepositOrder.setPayType(ConstEnum.PayType.WEIXIN_MP.getValue());
        shopDepositOrder.setClientType(ConstEnum.ClientType.MP.getValue());
        shopDepositOrderService.insert(shopDepositOrder);

        return shopDepositOrderService.payByWeixinMp(false, user, agent, shopDepositOrder);
    }

    public static class CreateByAlipayfwParam {
        public int money;
    }

    /**
     * 1-充值(支付宝当面付)
     * 门店充值(支付宝 生活号)
     * @param param
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/create_by_alipay_fw")
    public RestResult createAlipayFw(@Valid @RequestBody CreateByAlipayfwParam param) throws IOException {
        TokenCache.Data tokenData = getTokenData();
        int agentId = tokenData.agentId;
        if (agentId == 0) {
            return RestResult.result(RespCode.CODE_2.getValue(), "用户信息错误");
        }
        User user = userService.find(tokenData.userId);
        if (user == null) {
            return RestResult.result(RespCode.CODE_2.getValue(), "账号不存在");
        }
        Agent agent = agentService.find(agentId);
        if (agent == null) {
            return RestResult.result(RespCode.CODE_2.getValue(), "运营商不存在");
        }
        Shop shop = shopService.find(tokenData.shopId);
        if (shop == null) {
            return RestResult.result(RespCode.CODE_2.getValue(), "门店不存在");
        }
        if (param.money <= 0) {
            return RestResult.result(RespCode.CODE_2.getValue(), "充值金额有误");
        }

        ShopDepositOrder shopDepositOrder = new ShopDepositOrder();
        shopDepositOrder.setId(orderIdService.newOrderId(OrderId.OrderIdType.CUSTOMER_DEPOSIT_ORDER));
        shopDepositOrder.setPartnerId(agent.getPartnerId());
        shopDepositOrder.setMoney(param.money);
        shopDepositOrder.setGift(0);
        shopDepositOrder.setShopId(shop.getId());
        shopDepositOrder.setShopName(shop.getShopName());
        shopDepositOrder.setStatus(CustomerDepositOrder.Status.NOT.getValue());
        shopDepositOrder.setCreateTime(new Date());
        shopDepositOrder.setPayType(ConstEnum.PayType.ALIPAY_FW.getValue());
        shopDepositOrder.setClientType(ConstEnum.ClientType.FW.getValue());
        shopDepositOrderService.insert(shopDepositOrder);

        return shopDepositOrderService.payByAlipayfw(false, user, agent, shopDepositOrder);
    }
}
