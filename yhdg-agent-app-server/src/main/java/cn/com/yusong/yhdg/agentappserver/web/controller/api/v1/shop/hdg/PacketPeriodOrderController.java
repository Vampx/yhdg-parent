package cn.com.yusong.yhdg.agentappserver.web.controller.api.v1.shop.hdg;

import cn.com.yusong.yhdg.agentappserver.config.TokenCache;
import cn.com.yusong.yhdg.agentappserver.constant.RespCode;
import cn.com.yusong.yhdg.agentappserver.entity.result.RestResult;
import cn.com.yusong.yhdg.agentappserver.service.hdg.PacketPeriodOrderService;
import cn.com.yusong.yhdg.agentappserver.service.hdg.ShopDayStatsService;
import cn.com.yusong.yhdg.agentappserver.web.controller.api.v1.ApiController;
import cn.com.yusong.yhdg.common.domain.hdg.PacketPeriodOrder;
import cn.com.yusong.yhdg.common.domain.hdg.ShopDayStats;
import cn.com.yusong.yhdg.common.entity.NotNullMap;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;
import java.util.*;

@Controller("agent_api_v1_shop_hdg_packet_period_order")
@RequestMapping("/agent_api/v1/shop/hdg/packet_period_order")
public class PacketPeriodOrderController extends ApiController {
    @Autowired
    private PacketPeriodOrderService packetPeriodOrderService;
    @Autowired
    private ShopDayStatsService shopDayStatsService;

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ListParam {
        public String keyword;
        public int offset;
        public int limit;
        public int stats;
    }

    @ResponseBody
    @RequestMapping(value = "/list.htm")
    public RestResult list(@Valid @RequestBody ListParam param) {
        TokenCache.Data tokenData = getTokenData();
        String shopId = tokenData.shopId;
        List<PacketPeriodOrder> list = packetPeriodOrderService.findListByShop(shopId, param.keyword, param.offset, param.limit);

        List<Map> result = new ArrayList<Map>();
        for (PacketPeriodOrder order : list) {
            NotNullMap line = new NotNullMap();
            line.putString("id", order.getId());
            line.putString("customerFullname", order.getCustomerFullname());
            line.putString("customerMobile", order.getCustomerMobile());
            line.putString("batteryTypeName", order.getBatteryTypeName());
            line.putString("payTypeName", order.getPayTypeName());
            line.putInteger("money", order.getMoney());
            line.putDateTime("beginTime", order.getBeginTime());
            line.putDateTime("endTime", order.getEndTime());
            line.putInteger("status", order.getStatus());
            result.add(line);
        }

        Map data = new HashMap();
        data.put("list", result);

        if (param.stats == 1) {
            NotNullMap stats = new NotNullMap();
            ShopDayStats shopDayStats = shopDayStatsService.statsOrderAndRefundMoney(shopId);
            List<Integer> statusList = Arrays.asList(PacketPeriodOrder.Status.NOT_USE.getValue(),
                    PacketPeriodOrder.Status.USED.getValue(),
                    PacketPeriodOrder.Status.EXPIRED.getValue(),
                    PacketPeriodOrder.Status.APPLY_REFUND.getValue(),
                    PacketPeriodOrder.Status.REFUND.getValue());
            int orderCount = packetPeriodOrderService.findCountByShopAndStatus(shopId, statusList);
            int refundCount = packetPeriodOrderService.findCountByShopAndStatus(shopId, Arrays.asList(PacketPeriodOrder.Status.REFUND.getValue()));
            stats.putInteger("packetOrderIncome", shopDayStats.getPacketPeriodMoney());
            stats.putInteger("packetRefundMoney", shopDayStats.getRefundPacketPeriodMoney());
            stats.putInteger("packetOrderCount", orderCount);
            stats.putInteger("packetRefundCount", refundCount);
            data.put("stats", stats);
        }

        return RestResult.dataResult(RespCode.CODE_0.getValue(), null, data);
    }

}
