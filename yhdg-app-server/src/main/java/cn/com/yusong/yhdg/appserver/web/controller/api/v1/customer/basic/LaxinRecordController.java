package cn.com.yusong.yhdg.appserver.web.controller.api.v1.customer.basic;

import cn.com.yusong.yhdg.appserver.config.TokenCache;
import cn.com.yusong.yhdg.appserver.constant.RespCode;
import cn.com.yusong.yhdg.appserver.entity.result.RestResult;
import cn.com.yusong.yhdg.appserver.service.basic.LaxinRecordService;
import cn.com.yusong.yhdg.appserver.web.controller.api.v1.ApiController;
import cn.com.yusong.yhdg.common.domain.basic.LaxinRecord;
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

@Controller("api_v1_customer_basic_laxin_record")
@RequestMapping(value = "/api/v1/customer/basic/laxin_record")
public class LaxinRecordController extends ApiController {

    @Autowired
    LaxinRecordService laxinRecordService;

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ListParam {
        public int status;
        public int offset;
        public int limit;
    }

    @ResponseBody
    @RequestMapping(value = "/list")
    public RestResult findLaxinList(@Valid @RequestBody ListParam param) {
        TokenCache.Data tokenData = getTokenData();

        List<LaxinRecord> laxinList = laxinRecordService.findList(tokenData.laxinId, param.status, param.offset, param.limit);

        List<Map> data = new ArrayList<Map>();
        for (LaxinRecord record : laxinList) {
            NotNullMap line = new NotNullMap();
            line.put("id", record.getId());
            line.putMobileMask("mobile", record.getTargetMobile());
            line.putString("fullname", record.getTargetFullname());
            line.put("money", record.getLaxinMoney());
            line.put("status", record.getStatus());
            line.putDateTime("createTime", record.getCreateTime());
            data.add(line);
        }

        return RestResult.dataResult(RespCode.CODE_0.getValue(), null, data);
    }


}
