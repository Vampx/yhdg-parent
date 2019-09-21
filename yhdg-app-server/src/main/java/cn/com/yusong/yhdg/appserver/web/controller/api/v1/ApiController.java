package cn.com.yusong.yhdg.appserver.web.controller.api.v1;

import cn.com.yusong.yhdg.appserver.config.AppConfig;
import cn.com.yusong.yhdg.appserver.config.TokenCache;
import cn.com.yusong.yhdg.appserver.constant.RespCode;
import cn.com.yusong.yhdg.appserver.entity.result.RestResult;
import cn.com.yusong.yhdg.appserver.service.basic.*;
import cn.com.yusong.yhdg.appserver.service.hdg.*;
import cn.com.yusong.yhdg.common.constant.CacheKey;
import cn.com.yusong.yhdg.common.constant.ConstEnum;
import cn.com.yusong.yhdg.common.constant.Constant;
import cn.com.yusong.yhdg.common.domain.basic.SystemBatteryType;
import cn.com.yusong.yhdg.common.domain.hdg.*;
import cn.com.yusong.yhdg.common.entity.AreaCache;
import cn.com.yusong.yhdg.common.tool.memcached.MemCachedConfig;
import cn.com.yusong.yhdg.common.utils.AppUtils;
import cn.com.yusong.yhdg.common.utils.CodecUtils;
import cn.com.yusong.yhdg.common.utils.YhdgUtils;
import cn.com.yusong.yhdg.common.web.controller.AbstractController;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;


public abstract class ApiController extends AbstractController {

    private static final Logger log = LogManager.getLogger(ApiController.class);

    @Autowired
    protected AppConfig config;
    @Autowired
    protected TokenCache tokenCache;
    @Autowired
    protected AreaCache areaCache;
    @Autowired
    protected AgentService agentService;
    @Autowired
    BatteryOrderService batteryOrderService;
    @Autowired
    CabinetBoxService cabinetBoxService;
    @Autowired
    BatteryService batteryService;
    @Autowired
    CabinetService cabinetService;
    @Autowired
    CustomerService customerService;
    @Autowired
    CabinetOperateLogService cabinetOperateLogService;


    protected String staticImagePath(String imagePath) {
        if (StringUtils.isEmpty(imagePath)) {
            return imagePath;
        } else {
            return config.staticUrl + imagePath;
        }
    }

    protected List<String> asImagePathList(String... imagePaths) {
        List<String> list = new ArrayList<String>(imagePaths.length);
        for (String e : imagePaths) {
            if (StringUtils.isNotEmpty(e)) {
                list.add(config.staticUrl + e);
            }
        }
        return list;
    }

    protected TokenCache.Data getTokenData() {
        return TokenCache.tokenCacheHolder.get();
    }

    protected List<String> getAllErrors(BindingResult result) {
        List<String> list = Collections.emptyList();
        if (result.hasErrors()) {
            list = new ArrayList<String>();
            List<ObjectError> errorList = result.getAllErrors();
            for (ObjectError error : errorList) {
                list.add(error.getDefaultMessage());
            }
        }
        return list;
    }

    protected String getClientIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarder-For");
        if (StringUtils.isEmpty(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public void handleMethodArgumentNotValidException(MethodArgumentNotValidException exception, HttpServletResponse response) throws IOException {
        String message = null;

        BindingResult bindingResult = exception.getBindingResult();
        List<String> errors = getAllErrors(bindingResult);
        response.setStatus(200);
        response.setContentType(ConstEnum.ContentType.JSON.getValue());
        response.getWriter().println(AppUtils.encodeJson(RestResult.result(RespCode.CODE_2.getValue(), message = StringUtils.join(errors, ", "))));
        response.flushBuffer();

        log.error("valid fail message: {}", message);
    }

    protected void insertCabinetOperateLog(Integer agentId,
                                           String cabinetId,
                                           String cabinetName,
                                           String boxNum,
                                           CabinetOperateLog.OperateType operateType,
                                           CabinetOperateLog.OperatorType operatorType,
                                           String content,
                                           String operator) {
        CabinetOperateLog operateLog = new CabinetOperateLog();
        operateLog.setAgentId(agentId);
        operateLog.setCabinetId(cabinetId);
        operateLog.setCabinetName(cabinetName);
        operateLog.setBoxNum(boxNum);
        operateLog.setOperateType(operateType.getValue());
        operateLog.setOperatorType(operatorType.getValue());
        operateLog.setContent(content);
        operateLog.setOperator(operator);
        operateLog.setCreateTime(new Date());
        cabinetOperateLogService.insert(operateLog);
    }

    protected boolean checkSign(Long time, String sign) {
        String key = "YSKJ-ERTYUIOFGHJKLRTYUJHERGNGUYUIOYUIOFGHJK";
        String sign2 = CodecUtils.md5(key + "YSKJ" + DateFormatUtils.format(new Date(), Constant.DATE_FORMAT) + time);

        if (sign2.equalsIgnoreCase(sign)) {
            return true;
        } else {
            log.debug("签名不一致 sign: {}, sign2: {}", sign, sign2);
            return false;
        }
    }

    public static void main(String[] args) {
        String key = "YSKJ-ERTYUIOFGHJKLRTYUJHERGNGUYUIOYUIOFGHJK";
        String sign2 = CodecUtils.md5(key + "YSKJ" + DateFormatUtils.format(new Date(), Constant.DATE_FORMAT) + 1560318168);
        System.out.print(sign2);
    }
}
