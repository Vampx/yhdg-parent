package cn.com.yusong.yhdg.serviceserver.tool.sms;

import cn.com.yusong.yhdg.common.domain.basic.SmsConfig;
import cn.com.yusong.yhdg.common.domain.basic.SmsConfigInfo;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Node;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SwlhHttpClient extends SmsHttpClient {

    final static Logger log = LogManager.getLogger(DxwHttpClient.class);

    public static final SwlhHttpClient INSTANCE = new SwlhHttpClient();

    static final String SERVICE_URL = "http://access.xx95.net:8886/Connect_Service.asmx/SendSmsEx";

    static final String CHARSET = "UTF-8";

    public SwlhHttpClient() {
        this.smsType = SmsConfig.Type.SWLH.getValue();
    }

    @Override
    public Result send(SmsConfigInfo account, Param param) {

        List<NameValuePair> formParam = new ArrayList<NameValuePair>();
        String[] accounts = StringUtils.split(account.getAccount(), "|");
        formParam.add(new NameValuePair("epid", accounts[0]));
        formParam.add(new NameValuePair("User_Name", accounts[1]));
        formParam.add(new NameValuePair("password", account.getPassword()));
        formParam.add(new NameValuePair("phone", param.mobile));
        formParam.add(new NameValuePair("content", handleSign(account, param.content)));
        formParam.add(new NameValuePair("ExtendCode", ""));

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
        //<?xml version="1.0" encoding="utf-8"?>
        //<string xmlns="http://access.xx95.net:8886/">00</string>
        if(result.httpCode == 200 && StringUtils.isNotEmpty(result.httpContent)) {
            try {
                Document document = DocumentHelper.parseText(result.httpContent);
                List<Node> list = document.selectNodes("/string");
                if(list != null) {
                    String code = list.get(0).getText();
                    if("00".equals(code)) {
                        result.success = true;

                    } else if("01".equals(code)) {
                        result.success = false;
                        result.message = "号码（超过上限50个）、内容等为空或内容长度超过210";

                    } else if("02".equals(code)) {
                        result.success = false;
                        result.message = "用户鉴权失败";

                    } else if("03".equals(code)) {
                        result.success = false;
                        result.message = "登录IP黑名单";

                    } else if("10".equals(code)) {
                        result.success = false;
                        result.message = "余额不足";

                    } else if("99".equals(code)) {
                        result.success = false;
                        result.message = "服务器接受失败";
                    } else {
                        result.success = false;
                        result.message = "未知代码: " + code;
                    }
                }
            } catch (Exception e) {
                log.error("解析XML出现错误, XML: {}", result.httpContent);
                log.error("解析XML出现错误: ", e);

                result.success = false;
                result.message = "解析XML失败";
            }
        } else {
            result.success = false;
            result.message = "httpCode " + result.httpCode;
        }
    }

    public static void main(String[] args) {
        SmsConfigInfo account = new SmsConfig();
        account.setId(2);
        account.setAccount("AHHS1155798|admin");
        account.setPassword("3464ca62305559ca");
        Param param = new Param();
        param.mobile = "13777351251";
        param.content = "可以测试通过吗";
        Result result = SwlhHttpClient.INSTANCE.send(account, param);
        System.out.println(result.success);
        System.out.println(result.message);

    }
}
