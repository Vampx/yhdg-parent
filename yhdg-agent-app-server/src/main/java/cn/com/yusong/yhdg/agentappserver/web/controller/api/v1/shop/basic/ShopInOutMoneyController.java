package cn.com.yusong.yhdg.agentappserver.web.controller.api.v1.shop.basic;

import cn.com.yusong.yhdg.agentappserver.config.TokenCache;
import cn.com.yusong.yhdg.agentappserver.constant.RespCode;
import cn.com.yusong.yhdg.agentappserver.entity.result.RestResult;
import cn.com.yusong.yhdg.agentappserver.service.basic.CustomerService;
import cn.com.yusong.yhdg.agentappserver.service.basic.ShopInOutMoneyService;
import cn.com.yusong.yhdg.agentappserver.service.basic.WithdrawService;
import cn.com.yusong.yhdg.agentappserver.service.hdg.ShopService;
import cn.com.yusong.yhdg.agentappserver.web.controller.api.v1.ApiController;
import cn.com.yusong.yhdg.common.domain.basic.ShopInOutMoney;
import cn.com.yusong.yhdg.common.domain.hdg.Shop;
import cn.com.yusong.yhdg.common.entity.NotNullMap;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@Controller( "agent_api_v1_shop_basic_in_out_money")
@RequestMapping(value = "/agent_api/v1/shop/basic/shop_in_out_money")
public class ShopInOutMoneyController extends ApiController {

    @Autowired
    ShopInOutMoneyService shopInOutMoneyService;
    @Autowired
    WithdrawService withdrawService;
    @Autowired
    CustomerService customerService;
    @Autowired
    ShopService shopService;

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ListParam {
        public int offset;
        public int limit;
    }
    /**
     * 110-查询账单
     * */
    @ResponseBody
    @RequestMapping("list.htm")
    public RestResult list(@Valid @RequestBody ListParam param) {
        TokenCache.Data tokenData = getTokenData();
        Shop shop = shopService.find(tokenData.shopId);
        if (shop == null) {
            return RestResult.result(RespCode.CODE_2.getValue(), "门店不存在");
        }

        List<Map> list = new ArrayList<Map>(param.limit);
        List<ShopInOutMoney> shopList = shopInOutMoneyService.findList(shop.getId(), param.offset, param.limit);
        for (ShopInOutMoney e : shopList) {
            NotNullMap line = new NotNullMap();
            line.put("id", e.getId());
            line.put("bizTypeName", e.getBizTypeName());
            line.put("money", e.getMoney());
            line.putDateTime("createTime", e.getCreateTime());
            list.add(line);
        }

        return RestResult.dataResult(RespCode.CODE_0.getValue(), null, list);
    }


}
