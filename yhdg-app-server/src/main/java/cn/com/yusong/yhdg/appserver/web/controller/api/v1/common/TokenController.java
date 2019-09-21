package cn.com.yusong.yhdg.appserver.web.controller.api.v1.common;

import cn.com.yusong.yhdg.appserver.config.TokenCache;
import cn.com.yusong.yhdg.appserver.constant.RespCode;
import cn.com.yusong.yhdg.appserver.entity.result.DataResult;
import cn.com.yusong.yhdg.appserver.entity.result.RestResult;
import cn.com.yusong.yhdg.appserver.web.controller.api.v1.ApiController;
import cn.com.yusong.yhdg.common.constant.ConstEnum;
import cn.com.yusong.yhdg.common.tool.memcached.MemCachedConfig;
import cn.com.yusong.yhdg.common.tool.memcached.MemCachedClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Controller("api_v1_common_basic_token")
@RequestMapping(value = "/api/v1/common/basic/token")
public class TokenController extends ApiController {

    @ResponseBody
    @RequestMapping(value = "/refresh_token")
    public RestResult refreshToken() {
        int expireIn = MemCachedConfig.CACHE_ONE_DAY;
        TokenCache.Data copy = tokenCache.putCopy(getTokenData(), expireIn);

        Map map = new HashMap();

        if(copy.isUser) {
            map.put("id", copy.userId);
        } else if(copy.isCustomer) {
            map.put("id", copy.customerId);
        } else if(copy.isCabinetApp) {
            map.put("id", copy.cabinetId);
        }

        map.put("token", copy.token);
        map.put("expireIn", expireIn);

        return DataResult.dataResult(RespCode.CODE_0.getValue(), null, map);
    }

    @ResponseBody
    @RequestMapping(value = "/token_data")
    public RestResult tokenData() {
        TokenCache.Data tokenData = getTokenData();
        return DataResult.dataResult(RespCode.CODE_0.getValue(), null, tokenData);
    }

}
