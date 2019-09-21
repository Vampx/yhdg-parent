package cn.com.yusong.yhdg.appserver.web.controller.api.v1.test;

import cn.com.yusong.yhdg.appserver.biz.client.ClientBizUtils;
import cn.com.yusong.yhdg.appserver.config.AppConfig;
import cn.com.yusong.yhdg.appserver.constant.RespCode;
import cn.com.yusong.yhdg.appserver.entity.result.RestResult;
import cn.com.yusong.yhdg.appserver.web.controller.api.v1.ApiController;
import cn.com.yusong.yhdg.common.annotation.NotLogin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller("api_v1_test_cabinet")
@RequestMapping(value = "/api/v1/test/cabinet")
public class CabinetController extends ApiController {

    @Autowired
    protected AppConfig config;

//    @NotLogin
//    @ResponseBody
//    @RequestMapping(value = "/open_box.htm")
//    public RestResult openBox(String cabinetId, String boxNum, Integer boxType) throws InterruptedException {
//        RestResult result = ClientBizUtils.openStandardBox(config, cabinetId, boxNum, boxType);
//
//        if (result.getCode() == RespCode.CODE_0.getValue()) {
//            return RestResult.dataResult(RespCode.CODE_0.getValue(), null, result.getData());
//        } else {
//            return result;
//        }
//    }
//
//    @NotLogin
//    @ResponseBody
//    @RequestMapping(value = "/query_box.htm")
//    public RestResult queryBox(String cabinetId) throws InterruptedException {
//        RestResult result = ClientBizUtils.queryBox(config, cabinetId);
//        if (result.getCode() == RespCode.CODE_0.getValue()) {
//            return RestResult.dataResult(RespCode.CODE_0.getValue(), null, result.getData());
//        } else {
//            return result;
//        }
//    }
}
