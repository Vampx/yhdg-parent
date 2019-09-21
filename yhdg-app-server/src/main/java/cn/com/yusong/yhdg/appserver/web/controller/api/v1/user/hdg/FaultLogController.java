package cn.com.yusong.yhdg.appserver.web.controller.api.v1.user.hdg;

import cn.com.yusong.yhdg.appserver.config.TokenCache;
import cn.com.yusong.yhdg.appserver.constant.RespCode;
import cn.com.yusong.yhdg.appserver.entity.result.RestResult;
import cn.com.yusong.yhdg.appserver.service.hdg.FaultLogService;
import cn.com.yusong.yhdg.appserver.web.controller.api.v1.ApiController;
import cn.com.yusong.yhdg.common.constant.Constant;
import cn.com.yusong.yhdg.common.domain.hdg.FaultLog;
import cn.com.yusong.yhdg.common.entity.json.DateDeserialize;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.map.annotate.JsonDeserialize;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.text.ParseException;
import java.util.*;

/**
 * Created by ruanjian5 on 2017/11/10.
 */

@Controller("api_v1_user_hdg_fault_log ")
@RequestMapping(value = "/api/v1/user/hdg/fault_log ")
public class FaultLogController extends ApiController {

    @Autowired
    FaultLogService faultLogService;
    @JsonIgnoreProperties(ignoreUnknown = true)
    public  static class StatusParam{
        public int status;
        public int offset;
        public int limit;
    }

    @ResponseBody
    @RequestMapping(value = "/list.htm")
    public RestResult list(@RequestBody StatusParam param){
        TokenCache.Data tokenData = getTokenData();
        long userId = tokenData.userId;

        Map map = new HashMap();
        List<Map> list = new ArrayList<Map>();
        List<FaultLog> faultLogList = faultLogService.findList(userId,param.status,param.offset, param.limit);

        if(faultLogList != null){
            for (FaultLog faultLog : faultLogList){
                Map map1 = new HashMap();
                map1.put("id", faultLog.getId());
                map1.put("typeName", FaultLog.FaultType.getName(faultLog.getFaultType()));
                map1.put("batteryId", faultLog.getBatteryId());
                map1.put("cabinetId", faultLog.getCabinetId());
                map1.put("cabinetName", faultLog.getCabinetName());
                map1.put("createTime", faultLog.getCreateTime() != null ? DateFormatUtils.format(faultLog.getCreateTime(), Constant.DATE_TIME_FORMAT) : null);
                map1.put("faultContent", faultLog.getFaultContent());
                map1.put("faultLevel", faultLog.getFaultLevel());
                map1.put("handlerName", faultLog.getHandlerName());
                map1.put("handleTime", faultLog.getHandleTime() != null ? DateFormatUtils.format(faultLog.getHandleTime(), Constant.DATE_TIME_FORMAT) : null);
                map1.put("handleResult", FaultLog.Status.getName(faultLog.getStatus()));
                list.add(map1);
            }
        }
        int faultCount = faultLogService.findCountByDispatcher(userId, param.status);
        map.put("faultCount",faultCount);
        map.put("faultList",list);
        return  RestResult.dataResult(RespCode.CODE_0.getValue(),"",map);
    }

    /**
     * 处理故障
     */
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class HandleParam{

        public long id;

        @NotBlank(message = "处理时间不能为空")
        @JsonDeserialize(using = DateDeserialize.class)
        public Date handleTime;

        public String handleMemo;

        @NotBlank(message = "处理人不能为空")
        public String handlerName;
    }

    @ResponseBody
    @RequestMapping(value = "/handle.htm")
    public RestResult handle(@RequestBody HandleParam param) throws ParseException {
        if (faultLogService.handle(param.id, FaultLog.HandleType.MANUAL.getValue(), param.handleMemo, param.handlerName, param.handleTime) == 0) {
            return RestResult.result(RespCode.CODE_1.getValue(), "处理故障信息失败");
        }
        return  RestResult.SUCCESS;
    }
}
