package cn.com.yusong.yhdg.serviceserver.tool.sms;

import cn.com.yusong.yhdg.common.domain.basic.SmsConfig;
import cn.com.yusong.yhdg.common.domain.basic.SmsConfigInfo;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DxwHttpClient extends SmsHttpClient {

    final static Logger log = LogManager.getLogger(DxwHttpClient.class);

    public static final DxwHttpClient INSTANCE = new DxwHttpClient();

    static final String SERVICE_URL = "http://web.duanxinwang.cc/asmx/smsservice.aspx";

    static final String CHARSET = "UTF-8";

    public DxwHttpClient() {
        this.smsType = SmsConfig.Type.DXW.getValue();
    }

    @Override
    public Result send(SmsConfigInfo account, Param param) {

        List<NameValuePair> formParam = new ArrayList<NameValuePair>();
        formParam.add(new NameValuePair("name", account.getAccount()));
        formParam.add(new NameValuePair("pwd", account.getPassword()));
        formParam.add(new NameValuePair("mobile", param.mobile));
        formParam.add(new NameValuePair("content", handleSign(account, param.content)));
        formParam.add(new NameValuePair("type", "pt"));
        formParam.add(new NameValuePair("extno", ""));

        if(log.isDebugEnabled()) {
            log.debug("send message, mobile: {}, content: {}", param.mobile, param.content);
        }

        try {
            Result result = send(SERVICE_URL, formParam, CHARSET, CHARSET);
            if(result != null) {
                result.id = account.getId();
            }

            if(log.isDebugEnabled()) {
                if(result.httpCode / 100 == 2) {
                    log.debug("status: {}, response text: {}", result.httpCode, result.httpContent);
                } else {
                    log.error("status: {}", result.httpCode);
                }
            }

            parseResult(result);
            return result;
        } catch (IOException e) {
            log.error("发送短信错误", e);
        }
        return Result.FAIL;
    }

    private void parseResult(Result result) {
        if(result.httpCode == 200 && StringUtils.isNotEmpty(result.httpContent)) {
            if(result.httpContent.startsWith("0,")) {
                result.success = true;

            } else if(result.httpContent.startsWith("1,")) {
                result.success = false;
                result.message = "含有敏感词汇";

            } else if(result.httpContent.startsWith("2,")) {
                result.success = false;
                result.message = "余额不足";

            } else if(result.httpContent.startsWith("3,")) {
                result.success = false;
                result.message = "没有号码";

            } else if(result.httpContent.startsWith("4,")) {
                result.success = false;
                result.message = "包含sql语句";

            } else if(result.httpContent.startsWith("10,")) {
                result.success = false;
                result.message = "账号不存在";

            } else if(result.httpContent.startsWith("11,")) {
                result.success = false;
                result.message = "账号注销";

            } else if(result.httpContent.startsWith("12,")) {
                result.success = false;
                result.message = "账号停用";

            } else if(result.httpContent.startsWith("13,")) {
                result.success = false;
                result.message = "IP鉴权失败";

            } else if(result.httpContent.startsWith("14,")) {
                result.success = false;
                result.message = "格式错误";


            } else if(result.httpContent.startsWith("-1,")) {
                result.success = false;
                result.message = "系统异常";

            } else {
                result.success = false;
                result.message = result.httpContent;
            }
        }
    }
}
