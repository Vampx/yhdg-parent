package cn.com.yusong.yhdg.appserver.web.controller.api.v1.customer.hdg;

import cn.com.yusong.yhdg.appserver.config.TokenCache;
import cn.com.yusong.yhdg.appserver.constant.RespCode;
import cn.com.yusong.yhdg.appserver.entity.result.RestResult;
import cn.com.yusong.yhdg.appserver.service.basic.CustomerService;
import cn.com.yusong.yhdg.appserver.service.hdg.BatteryService;
import cn.com.yusong.yhdg.appserver.service.hdg.CabinetService;
import cn.com.yusong.yhdg.appserver.service.hdg.FaultFeedBackService;

import cn.com.yusong.yhdg.appserver.web.controller.api.v1.ApiController;
import cn.com.yusong.yhdg.common.constant.ConstEnum;
import cn.com.yusong.yhdg.common.constant.Constant;
import cn.com.yusong.yhdg.common.domain.basic.Customer;
import cn.com.yusong.yhdg.common.domain.hdg.Battery;
import cn.com.yusong.yhdg.common.domain.hdg.Cabinet;
import cn.com.yusong.yhdg.common.domain.hdg.FaultFeedback;
import cn.com.yusong.yhdg.common.entity.QrcodeResult;
import cn.com.yusong.yhdg.common.utils.AppUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller("api_v1_customer_hdg_fault_feedback")
@RequestMapping(value = "/api/v1/customer/hdg/fault_feedback")
public class FaultFeekbackController extends ApiController {
    final static Logger log = LogManager.getLogger(FaultFeekbackController.class);

    @Autowired
    CustomerService customerService;
    @Autowired
    FaultFeedBackService faultFeedBackService;
    @Autowired
    CabinetService cabinetService;
    @Autowired
    BatteryService batteryService;

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class FaultNamesParam {
        public Integer faultType;
    }

    @ResponseBody
    @RequestMapping(value = "fault_names")
    public RestResult faultNames(@RequestBody FaultNamesParam param) {
        List<String> faultNames = FaultFeedback.getFaultNames(param.faultType);
        return RestResult.dataResult(RespCode.CODE_0.getValue(), null, faultNames);
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ViewParam {
        public Long id;
    }

    @ResponseBody
    @RequestMapping(value = "view")
    public RestResult view(@RequestBody ViewParam param) {
        return faultFeedBackService.view(param.id);
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ListParam {
        public int offset;
        public int limit;
    }

    @ResponseBody
    @RequestMapping(value = "list")
    public RestResult list(@RequestBody ListParam param) {
        TokenCache.Data tokenCache = getTokenData();
        return faultFeedBackService.getListByCustomer(tokenCache.customerId, param.offset, param.limit);
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class FaultFeedbackParam {
        public String qrcode;//柜子id  or 电池id
        public String faultName;
        public Integer faultType;
        public String memo;
        public String photoPath1;
        public String photoPath2;
        public String photoPath3;
        public String photoPath4;
        public String photoPath5;
        public String photoPath6;

    }

    @ResponseBody
    @RequestMapping(value = "/create")
    public RestResult create(@RequestBody FaultFeedbackParam param) {
        TokenCache.Data tokenData = getTokenData();
        Integer agentId = null;
        Cabinet cabinet = null;
        Battery battery = null;
        Customer customer = customerService.find(tokenData.customerId);
        if (customer == null) {
            return RestResult.result(RespCode.CODE_2.getValue(), "用户不存在");
        }
        if (StringUtils.isEmpty(param.qrcode)) {
            return RestResult.result(RespCode.CODE_2.getValue(), "编号或二维码不能为空");
        }

        param.qrcode = AppUtils.decodeUrl(param.qrcode, Constant.ENCODING_UTF_8);

        QrcodeResult result = ConstEnum.Qrcode.parse(param.qrcode);
        if (result != null) {
            if (result.type == ConstEnum.Qrcode.QRCODE_CABINET.getType()) {
                cabinet = cabinetService.find(result.value);
                if (cabinet == null) {
                    return RestResult.result(RespCode.CODE_2.getValue(), "柜子不存在");
                }
                agentId = cabinet.getAgentId();
            } else if (result.type == ConstEnum.Qrcode.QRCODE_BATTERY.getType()) {
                battery = batteryService.find(param.qrcode);
                if (battery == null) {
                    return RestResult.result(RespCode.CODE_2.getValue(), " 电池不存在");
                }
                agentId = battery.getAgentId();
            } else {
                return RestResult.result(RespCode.CODE_2.getValue(), " 不支持的二维码类型");
            }
        } else {
            cabinet = cabinetService.find(param.qrcode);
            if (cabinet == null) {
                battery = batteryService.find(param.qrcode);
                if (battery == null) {
                    return RestResult.result(RespCode.CODE_2.getValue(), "柜子 或者 电池 不存在");
                } else {
                    agentId = battery.getAgentId();
                }
            } else {
                agentId = cabinet.getAgentId();
            }
        }


        FaultFeedback faultFeedback = new FaultFeedback();
        faultFeedback.setCustomerId(customer.getId());
        faultFeedback.setCustomerName(customer.getFullname());
        faultFeedback.setCustomerMobile(customer.getMobile());
        faultFeedback.setFaultName(param.faultName);
        faultFeedback.setFaultType(param.faultType);
        faultFeedback.setMemo(param.memo);
        faultFeedback.setPhotoPath1(param.photoPath1);
        faultFeedback.setPhotoPath2(param.photoPath2);
        faultFeedback.setPhotoPath3(param.photoPath3);
        faultFeedback.setPhotoPath4(param.photoPath4);
        faultFeedback.setPhotoPath5(param.photoPath5);
        faultFeedback.setPhotoPath6(param.photoPath6);
        faultFeedback.setAgentId(agentId);
        faultFeedback.setHandleStatus(FaultFeedback.HandleStatus.UNHANDLED.getValue());

        if (cabinet != null) {
            faultFeedback.setCabinetId(cabinet.getId());
            faultFeedback.setCabinetName(cabinet.getCabinetName());
            faultFeedback.setCabinetAddress(cabinet.getAddress());
        }
        if (battery != null) {
            faultFeedback.setBatteryId(battery.getId());
        }
        return faultFeedBackService.insert(faultFeedback);
    }
}
