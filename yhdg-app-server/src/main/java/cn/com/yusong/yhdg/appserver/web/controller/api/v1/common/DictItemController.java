package cn.com.yusong.yhdg.appserver.web.controller.api.v1.common;

import cn.com.yusong.yhdg.appserver.constant.RespCode;
import cn.com.yusong.yhdg.appserver.entity.result.RestResult;
import cn.com.yusong.yhdg.appserver.service.basic.DictItemService;
import cn.com.yusong.yhdg.appserver.web.controller.api.v1.ApiController;
import cn.com.yusong.yhdg.common.constant.ConstEnum;
import cn.com.yusong.yhdg.common.constant.Constant;
import cn.com.yusong.yhdg.common.domain.basic.DictItem;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;
import javax.validation.constraints.Pattern;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ruanjian5 on 2017/11/7.
 */
@Controller("api_v1_common_basic_dict_item")
@RequestMapping(value = "/api/v1/common/basic/dict_item")
public class DictItemController extends ApiController {
    @Autowired
    DictItemService dictItemService;

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ListParam {
        public int categoryId;
    }

    @ResponseBody
    @RequestMapping(value = "list.htm")
    public RestResult list(@Valid @RequestBody ListParam param) {
        Map map = new HashMap();
        List<Map> list = new ArrayList<Map>();
        List<DictItem> listDictItem = dictItemService.findByCategory(param.categoryId);
        if (listDictItem != null) {
            for (DictItem dictItem : listDictItem) {
                Map mapDictItem = new HashMap();
                mapDictItem.put("id", dictItem.getId());
                mapDictItem.put("itemName", dictItem.getItemName());
                mapDictItem.put("itemValue", dictItem.getItemValue());

                list.add(mapDictItem);
            }
        }
        return RestResult.dataResult(RespCode.CODE_0.getValue(), null, list);

    }
}
