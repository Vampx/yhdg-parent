package cn.com.yusong.yhdg.agentserver.web.controller.security.zc;


import cn.com.yusong.yhdg.common.annotation.ViewModel;
import cn.com.yusong.yhdg.common.constant.ConstEnum;
import cn.com.yusong.yhdg.common.domain.zc.PriceSetting;
import cn.com.yusong.yhdg.common.domain.zc.RentPrice;
import cn.com.yusong.yhdg.common.entity.json.DataResult;
import cn.com.yusong.yhdg.common.entity.json.ExtResult;
import cn.com.yusong.yhdg.agentserver.service.zc.PriceSettingService;
import cn.com.yusong.yhdg.agentserver.service.zc.RentPriceService;
import cn.com.yusong.yhdg.agentserver.web.controller.security.basic.SecurityController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.util.Collections;
import java.util.Set;

@Controller
@RequestMapping(value = "/security/zc/rent_price")
public class RentPriceController extends SecurityController {
    @Autowired
    RentPriceService rentPriceService;
    @Autowired
    PriceSettingService priceSettingService;

    @ResponseBody
    @ViewModel(ViewModel.JSON_ARRAY)
    @RequestMapping(value = "tree.htm")
    public void tree(String dummy, Long priceSettingId, HttpServletResponse response) throws Exception {
        response.setContentType(ConstEnum.ContentType.JSON.getValue());
        Set<Integer> checkedSet = Collections.emptySet();
        rentPriceService.tree(checkedSet, dummy, priceSettingId, response.getOutputStream());
    }

    @ResponseBody
    @RequestMapping("find_by_id.htm")
    @ViewModel(ViewModel.JSON)
    public ExtResult findHdBatteryCount(Long rentPriceId) {
        RentPrice rentPrice = rentPriceService.find(rentPriceId);
        if (rentPrice != null) {
            PriceSetting priceSetting = priceSettingService.find(rentPrice.getPriceSettingId());
            if (priceSetting != null) {
                rentPrice.setModelId(priceSetting.getModelId());
                rentPrice.setCategory(priceSetting.getCategory());
            }
            return DataResult.successResult(rentPrice);
        } else {
            return ExtResult.failResult("租车套餐不存在");
        }
    }

    @ResponseBody
    @ViewModel(ViewModel.JSON)
    @RequestMapping(value = "find_group_by_price.htm")
    public ExtResult findGroupByPrice(long id) {
        return rentPriceService.findGroupByPrice(id);
    }

    @RequestMapping("delete.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public ExtResult delete(long id) {
        return rentPriceService.delete(id);
    }
}
