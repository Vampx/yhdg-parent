package cn.com.yusong.yhdg.agentappserver.web.controller.api.v1.agent.basic;

import cn.com.yusong.yhdg.agentappserver.constant.RespCode;
import cn.com.yusong.yhdg.agentappserver.entity.result.RestResult;
import cn.com.yusong.yhdg.agentappserver.service.basic.DictItemService;
import cn.com.yusong.yhdg.agentappserver.web.controller.api.v1.ApiController;
import cn.com.yusong.yhdg.common.domain.basic.DictItem;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Controller("agent_api_v1_agent_basic_dict_item")
@RequestMapping(value = "/agent_api/v1/agent/basic/dict_item")
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
