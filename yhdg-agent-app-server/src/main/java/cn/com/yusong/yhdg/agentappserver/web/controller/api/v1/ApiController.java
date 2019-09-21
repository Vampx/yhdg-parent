package cn.com.yusong.yhdg.agentappserver.web.controller.api.v1;

import cn.com.yusong.yhdg.agentappserver.config.AppConfig;
import cn.com.yusong.yhdg.agentappserver.config.TokenCache;
import cn.com.yusong.yhdg.agentappserver.constant.RespCode;
import cn.com.yusong.yhdg.agentappserver.entity.result.RestResult;
import cn.com.yusong.yhdg.common.constant.ConstEnum;
import cn.com.yusong.yhdg.common.entity.AreaCache;
import cn.com.yusong.yhdg.common.utils.AppUtils;
import cn.com.yusong.yhdg.common.web.controller.AbstractController;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public abstract class ApiController extends AbstractController {

    private static final Logger log = LogManager.getLogger(ApiController.class);

    @Autowired
    protected AppConfig config;
    @Autowired
    protected TokenCache tokenCache;
    @Autowired
    protected AreaCache areaCache;

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

}
