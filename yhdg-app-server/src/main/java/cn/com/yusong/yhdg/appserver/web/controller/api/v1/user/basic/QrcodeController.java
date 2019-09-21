package cn.com.yusong.yhdg.appserver.web.controller.api.v1.user.basic;

import cn.com.yusong.yhdg.appserver.config.TokenCache;
import cn.com.yusong.yhdg.appserver.constant.RespCode;
import cn.com.yusong.yhdg.appserver.entity.result.RestResult;
import cn.com.yusong.yhdg.appserver.service.basic.CustomerService;
import cn.com.yusong.yhdg.appserver.service.hdg.CabinetService;
import cn.com.yusong.yhdg.appserver.web.controller.api.v1.ApiController;
import cn.com.yusong.yhdg.common.constant.ConstEnum;
import cn.com.yusong.yhdg.common.domain.basic.Customer;
import cn.com.yusong.yhdg.common.domain.hdg.Cabinet;
import cn.com.yusong.yhdg.common.entity.QrcodeResult;
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
import java.util.HashMap;
import java.util.Map;

@Controller("api_v1_user_basic_qrcode")
@RequestMapping(value = "/api/v1/user/basic/qrcode")
public class QrcodeController extends ApiController {
    final static Logger log = LogManager.getLogger(QrcodeController.class);

    @Autowired
    CustomerService customerService;

    @Autowired
    CabinetService cabinetService;


    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ScanParam {
        @NotBlank(message = "二维码不能为空")
        public String qrcode;

    }

    @ResponseBody
    @RequestMapping(value = "/scan")
    public RestResult scan(@Valid @RequestBody ScanParam param) {
        TokenCache.Data tokenData = getTokenData();
        long customerid = tokenData.customerId;

        QrcodeResult result = ConstEnum.Qrcode.parse(param.qrcode);
        if(result == null) {
            return RestResult.result(RespCode.CODE_2.getValue(), "不可识别的二维码");
        }

        if(result.type == ConstEnum.Qrcode.QRCODE_CABINET.getType()) {
            return handleCabinetQrcode(customerid, result.value);

        }

        return RestResult.result(RespCode.CODE_2.getValue(), "不支持的二维码类型");
    }

    private RestResult handleCabinetQrcode (long customerId, String cabinetId) {
        Cabinet cabinet = cabinetService.find(cabinetId);
        if(cabinet == null) {
            return RestResult.result(RespCode.CODE_2.getValue(), "柜子不存在");
        }

        Map root = new HashMap();
        Map data = new HashMap();

        root.put("type", ConstEnum.Qrcode.QRCODE_CABINET.getType());
        root.put("cabinetQrcode", data);

        data.put("cabinetId", cabinet.getId());

        return RestResult.dataResult(RespCode.CODE_0.getValue(), null, root);
    }

}
