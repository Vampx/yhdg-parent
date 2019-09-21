package cn.com.yusong.yhdg.weixinserver.web.controller.security;

import cn.com.yusong.yhdg.common.annotation.NotLogin;
import cn.com.yusong.yhdg.common.constant.Constant;
import cn.com.yusong.yhdg.common.domain.basic.Alipayfw;
import cn.com.yusong.yhdg.common.tool.alipay.AlipayfwClientHolder;
import cn.com.yusong.yhdg.weixinserver.alipay.Dispatcher;
import cn.com.yusong.yhdg.weixinserver.alipay.executor.ActionExecutor;
import cn.com.yusong.yhdg.weixinserver.utils.RequestUtil;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.internal.util.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

@Controller
@RequestMapping(value = "/alipay")
public class AlipayController extends BaseController {

    private final static Logger log = LogManager.getLogger(AlipayController.class);

    public static final String CHARSET_UTF_8 = "UTF-8";
    public static final String SIGN_TYPE = "RSA2";

    @Autowired
    AlipayfwClientHolder alipayClientHolder;

    @NotLogin
    @RequestMapping(value = "service.htm")
    public void service(HttpServletRequest request, HttpServletResponse response) throws IOException {
        service(1, request, response);
    }

    @NotLogin
    @RequestMapping(value = "service_{alipayfwId}.htm")
    public void service(@PathVariable("alipayfwId")int alipayfwId, HttpServletRequest request, HttpServletResponse response) throws IOException {
        //支付宝响应消息
        String responseMsg = "";
        //1. 解析请求参数
        Map<String, String> params = RequestUtil.getRequestParams(request);

        if(log.isDebugEnabled()) {
            //打印本次请求日志，开发者自行决定是否需要
            for(Map.Entry<String, String> entry : params.entrySet()) {
                log.debug("支付宝请求串, {}={}", entry.getKey(), entry.getValue());
            }
        }

        Alipayfw alipayfw = alipayfwService.find(alipayfwId);
        AlipayClient alipayClient = alipayClientHolder.getAlipayfw(alipayfw.getId());

        try {
            //2. 验证签名
            this.verifySign(alipayfw, params);

            //3. 获取业务执行器   根据请求中的 service, msgType, eventType, actionParam 确定执行器
            ActionExecutor executor = Dispatcher.getExecutor(alipayfw, alipayClient, params);

            //4. 执行业务逻辑
            responseMsg = executor.execute();

        }
        catch (AlipayApiException alipayApiException) {
            log.error("处理生活号推送错误", alipayApiException);
            //开发者可以根据异常自行进行处理
            alipayApiException.printStackTrace();

        }
        catch (Exception exception) {
            log.error("处理生活号推送错误", exception);
            //开发者可以根据异常自行进行处理
            exception.printStackTrace();

        } finally {
            //5. 响应结果加签及返回
            try {
                //对响应内容加签
                responseMsg = encryptAndSign(responseMsg,
                        alipayfw.getAliKey(),
                        alipayfw.getPriKey(), CHARSET_UTF_8,
                        false, true, SIGN_TYPE);

                //http 内容应答
                response.reset();
                response.setContentType("text/xml;charset=UTF-8");
                PrintWriter printWriter = response.getWriter();
                printWriter.print(responseMsg);
                response.flushBuffer();

                if(log.isDebugEnabled()) {
                    //开发者自行决定是否要记录，视自己需求
                    log.debug("开发者响应串, {}", responseMsg);
                }

            } catch (AlipayApiException alipayApiException) {
                log.error("处理生活号推送错误", alipayApiException);
                //开发者可以根据异常自行进行处理
                alipayApiException.printStackTrace();
            }
        }
    }

    /**
     * 验签
     * @param params‘
     * @return
     */
    private void verifySign(Alipayfw alipayfw, Map<String, String> params) throws AlipayApiException {
        if (!AlipaySignature.rsaCheckV2(params, alipayfw.getAliKey(),
                params.get("charset"), SIGN_TYPE)) {
            throw new AlipayApiException("verify sign fail.");
        }
    }


    public static String encryptAndSign(String bizContent, String alipayPublicKey, String cusPrivateKey, String charset,
                                        boolean isEncrypt, boolean isSign, String signType) throws AlipayApiException {
        StringBuilder sb = new StringBuilder();
        if (StringUtils.isEmpty(charset)) {
            charset = "GBK";
        }
        sb.append("<?xml version=\"1.0\" encoding=\"" + charset + "\"?>");
        if (isEncrypt) {// 加密
            sb.append("<alipay>");
            String encrypted = AlipaySignature.rsaEncrypt(bizContent, alipayPublicKey, charset);
            sb.append("<response>" + encrypted + "</response>");
            sb.append("<encryption_type>AES</encryption_type>");
            if (isSign) {
                String sign = AlipaySignature.rsaSign(encrypted, cusPrivateKey, charset, signType);
                sb.append("<sign>" + sign + "</sign>");
                sb.append("<sign_type>");
                sb.append(signType);
                sb.append("</sign_type>");
            }
            sb.append("</alipay>");
        } else if (isSign) {// 不加密，但需要签名
            sb.append("<alipay>");
            sb.append("<response>" + bizContent + "</response>");
            String sign = AlipaySignature.rsaSign(bizContent, cusPrivateKey, charset, signType);
            sb.append("<sign>" + sign + "</sign>");
            sb.append("<sign_type>");
            sb.append(signType);
            sb.append("</sign_type>");
            sb.append("</alipay>");
        } else {// 不加密，不加签
            sb.append(bizContent);
        }
        return sb.toString();
    }
}
