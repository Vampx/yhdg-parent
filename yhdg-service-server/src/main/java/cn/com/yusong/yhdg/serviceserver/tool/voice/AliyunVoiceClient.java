package cn.com.yusong.yhdg.serviceserver.tool.voice;

import cn.com.yusong.yhdg.common.domain.basic.VoiceConfig;
import cn.com.yusong.yhdg.common.utils.AppUtils;
import cn.com.yusong.yhdg.serviceserver.config.AppConfig;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dyvmsapi.model.v20170525.SingleCallByTtsRequest;
import com.aliyuncs.dyvmsapi.model.v20170525.SingleCallByTtsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

/**
 * 阿里云文本转语音接口
 *
 * @author zhoub
 */
public class AliyunVoiceClient {
    final static Logger log = LogManager.getLogger(AliyunVoiceClient.class);

    public AliyunVoiceClient(AppConfig config) {
        config.aliyunVoiceClient = this;
    }

    public Result send(VoiceConfig account, Param param) {
        Result result = new Result();

        try {
            //设置访问超时时间
            System.setProperty("sun.net.client.defaultConnectTimeout", "10000");
            System.setProperty("sun.net.client.defaultReadTimeout", "10000");
            //云通信产品-语音API服务产品名称（产品名固定，无需修改）
            final String product = "Dyvmsapi";
            //产品域名（接口地址固定，无需修改）
            final String domain = "dyvmsapi.aliyuncs.com";
            //AK信息
            final String accessKeyId = account.getAccount();
            final String accessKeySecret = account.getPassword();
            //初始化acsClient 暂时不支持多region
            IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", accessKeyId, accessKeySecret);
            DefaultProfile.addEndpoint("cn-hangzhou", "cn-hangzhou", product, domain);
            IAcsClient acsClient = new DefaultAcsClient(profile);
            SingleCallByTtsRequest request = new SingleCallByTtsRequest();
            //必填-被叫显号,可在语音控制台中找到所购买的显号
            request.setCalledShowNumber(param.calledShowNumber);
            //必填-被叫号码
            request.setCalledNumber(param.calledNumber);
            //必填-Tts模板ID
            request.setTtsCode(param.templateCode);
            //可选-当模板中存在变量时需要设置此值
            request.setTtsParam(param.variable);
            //可选-音量 取值范围 0--200
            request.setVolume(param.volume);
            //可选-播放次数
            request.setPlayTimes(param.playTimes);
            //可选-外部扩展字段,此ID将在回执消息中带回给调用方
            request.setOutId(String.format("%d", param.id));
            //hint 此处可能会抛出异常，注意catch
            SingleCallByTtsResponse response = acsClient.getAcsResponse(request);
            if(response != null && response.getCode() != null && response.getCode().equals("OK")) {
                //请求成功
                result.success = true;
                if(log.isDebugEnabled()) {
                    log.debug("aliyun voice send {} success", param.id);
                }
            } else {
                //请求失败
                result.success = false;
                log.error("aliyun voice send {} failure, {}", param.id, AppUtils.encodeJson(response));
            }

        } catch (ClientException e) {
            log.error("aliyun voice send message fail", e);
            return Result.FAIL;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }

    public static void main(String[] args) {
        VoiceConfig config = new VoiceConfig();
        config.setId(1);
        config.setAccount("LTAIKVjonjAmq60b");
        config.setPassword("zHfMILqrDGqiaLKa2rbnADetVGEsaK");

        Param param = new Param();
        param.calledShowNumber = "057428840616";
        param.calledNumber = "13675608767";
        param.variable = "{\"name\":\"周兵\", \"charge\":\"10\"}";
        param.templateCode = "TTS_168346439";
        param.volume = 150;
        param.playTimes = 1;

        AliyunVoiceClient client = new AliyunVoiceClient(new AppConfig());
        Result result = client.send(config, param);
        System.out.println(result.success);
        System.out.println(result.message);
    }


}
