package cn.com.yusong.yhdg.serviceserver.tool.sms;


import cn.com.yusong.yhdg.common.domain.basic.SmsConfig;
import cn.com.yusong.yhdg.common.domain.basic.SmsConfigInfo;
import com.taobao.api.DefaultTaobaoClient;
import com.taobao.api.TaobaoClient;
import com.taobao.api.request.AlibabaAliqinFcSmsNumSendRequest;
import com.taobao.api.response.AlibabaAliqinFcSmsNumSendResponse;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * 阿里大于短信接口
 */
public class AldyClient extends SmsHttpClient {

    final static Logger log = LogManager.getLogger(AldyClient.class);

    public static final AldyClient INSTANCE = new AldyClient();

    public AldyClient() {
        this.smsType = SmsConfig.Type.ALDY.getValue();
    }

    @Override
    public Result send(SmsConfigInfo account, Param param) {

        Result result = new Result();
        result.id = account.getId();

        if(log.isDebugEnabled()) {
            log.debug("param : {}", ReflectionToStringBuilder.toString(param, ToStringStyle.MULTI_LINE_STYLE));
        }

        try {
            String sign = StringUtils.substringBetween(param.content, "【", "】");
            if(StringUtils.isEmpty(sign)) {
                sign = account.getSign();
            }

            if(StringUtils.isEmpty(sign)) {
                throw new IllegalArgumentException("SmsConfig or content must be contain sign");
            }

            TaobaoClient client = new DefaultTaobaoClient("https://eco.taobao.com/router/rest", account.getAccount(), account.getPassword());
            AlibabaAliqinFcSmsNumSendRequest req = new AlibabaAliqinFcSmsNumSendRequest();
            req.setExtend("");
            req.setSmsType("normal");
            req.setSmsFreeSignName(sign);
            req.setSmsParamString(param.vairable);
            req.setRecNum(param.mobile);
            req.setSmsTemplateCode(param.templateCode);

            AlibabaAliqinFcSmsNumSendResponse rsp = client.execute(req);
            if(StringUtils.isEmpty(rsp.getErrorCode()) && StringUtils.isNotEmpty(rsp.getBody()) && rsp.getBody().contains("\"alibaba_aliqin_fc_sms_num_send_response\"")) {
                result.success = true;
            } else {
                //msg, subCode, subMsg, boy
                result.success = false;
                result.message = rsp.getMsg();
                log.error("alidayu message fail, {}", ReflectionToStringBuilder.toString(rsp, ToStringStyle.MULTI_LINE_STYLE));
            }
        } catch (Exception e) {
            log.error("send message fail", e);
            return Result.FAIL;
        }
        return result;
    }

    //{"alibaba_aliqin_fc_sms_num_send_response":{"result":{"err_code":"0","model":"104762665299^1106596107525","success":true},"request_id":"z27jfmbg726n"}}

    //{"error_response":{"code":15,"msg":"Remote service error","sub_code":"isv.BUSINESS_LIMIT_CONTROL","sub_msg":"触发业务流控","request_id":"12l8ssqlvchxt"}}

    public static void main(String[] args) {
        SmsConfigInfo config = new SmsConfigInfo();
        config.setId(1);
        config.setAccount("23445426");
        config.setPassword("965c54f49317eb85adeca7481bef6833");

        Param param = new Param();
        param.mobile = "13675608767";
        param.content = "【大鱼测试】测试ttt";
        param.vairable = "{\"code\":\"1234\",\"product\":\"测测看看\"}";
        param.templateCode = "SMS_13251520";


        AldyClient client = new AldyClient();
        Result result = client.send(config, param);
        System.out.println(result.success);
        System.out.println(result.message);
    }
}
