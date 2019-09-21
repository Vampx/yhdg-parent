package cn.com.yusong.yhdg.serviceserver.tool.push;

import cn.com.yusong.yhdg.common.utils.AppUtils;
import com.alibaba.fastjson.JSON;
import nsp.NSPClient;
import nsp.OAuth2Client;
import nsp.support.common.AccessToken;
import nsp.support.common.NSPException;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * http://developer.huawei.com/consumer/cn/service/hms/catalog/huaweipush.html?page=hmssdk_huaweipush_devguide_s
 *
 * http://developer.huawei.com/consumer/cn/service/hms/catalog/huaweipush.html?page=hmssdk_huaweipush_devguide
 */
public class HuaweiPushService implements PushService {

    private String service = "openpush.openapi.notification_send";
    private static NSPClient client;

    String appId;
    String appSecret;
    long tokenTime; //获取token的时间
    long expireTime; //过期时间

    public HuaweiPushService(String appId, String appSecret) {
        this.appId = appId;
        this.appSecret = appSecret;

    }
    @Override
    public Result sendNotification(AndroidPushNotification notification) throws Exception {
        //type 1 透传异步消息 3 系统通知栏异步消息

        Result result = new Result();
        HashMap<String, Object> pushParams = new HashMap<String,Object>();
        pushParams.put("device_token_list", notification.registrationIdList);
        pushParams.put("payload", buildPayload(notification, 3));

        client.setTimeout(10000, 15000);
        String rsp = client.call("openpush.openapi.notification_send", pushParams, String.class);

        HuaweiPushResponse response = JSON.parseObject(rsp, HuaweiPushResponse.class);
        result.success = response.getResult_code() == 0;
        result.data = response;
        result.message = "[push:HuaweiPush,errorCode:"+response.getResult_code()+",errorMsg:"+response.getResult_desc()+"]";
        return result;
    }

    @Override
    public Result sendNotification(IosPushNotification notification) {
        return null;
    }

    @Override
    public Result sendMessage(AndroidPushMessage message) throws Exception {
        Result result = new Result();
        HashMap<String, Object> pushParams = new HashMap<String,Object>();
        pushParams.put("device_token_list", message.registrationIdList);
        pushParams.put("payload", buildPayload(message, 1));

        client.setTimeout(10000, 15000);
        String rsp = client.call("openpush.openapi.notification_send", pushParams, String.class);

        HuaweiPushResponse response = JSON.parseObject(rsp, HuaweiPushResponse.class);
        result.success = response.getResult_code() == 0;
        result.data = response;
        result.message = "[push:HuaweiPush,errorCode:"+response.getResult_code()+",errorMsg:"+response.getResult_desc()+"]";
        return result;
    }

    @Override
    public Result sendMessage(IosPushMessage message) {
        return null;
    }

    private synchronized NSPClient getNSPClient() throws NSPException, IOException {
        if(client == null || expireTime - tokenTime <= 1000L * 3600) {
            OAuth2Client oauth2Client = new OAuth2Client();
            oauth2Client.initKeyStoreStream(new FileInputStream("mykeystorebj.jks"), "123456");
            AccessToken access_token = oauth2Client.getAccessToken("client_credentials", appId, appSecret);
            expireTime = System.currentTimeMillis() + access_token.getExpires_in() * 1000L;

            client = new NSPClient(access_token.getAccess_token());
            client.initHttpConnections(30, 50);//设置每个路由的连接数和最大连接数
            client.initKeyStoreStream(new FileInputStream("classpath:mykeystorebj.jks"), "123456");//如果访问https必须导入证书流和密码
        }

        return client;
    }

    private String buildPayload(AndroidPushNotification param, int type) {
        Hps hps = new Hps();
        hps.msg.type = type;
        hps.msg.body.put("title", param.title);
        hps.msg.body.put("conent", param.content);
        hps.ext.putAll(param.extras);


        Map root = new HashMap();
        root.put("hps", hps);

        return AppUtils.encodeJson2(root);
    }

    private String buildPayload(AndroidPushMessage param, int type) {
        Hps hps = new Hps();
        hps.msg.type = type;
        hps.msg.body.put("title", param.title);
        hps.msg.body.put("conent", param.content);
        hps.ext.putAll(param.extras);


        Map root = new HashMap();
        root.put("hps", hps);

        return AppUtils.encodeJson2(root);
    }

    static class Hps {
        public Msg msg = new Msg();
        public Map<String, String> ext = new HashMap<String, String>();
    }

    static class Msg {
        public int type;
        public Map<String, String> body = new HashMap<String, String>();
        public Map<String, String> action = new HashMap<String, String>();
    }

    public static class HuaweiPushResponse {
        private String result_desc;
        private int result_code;
        private String request_id;

        public String getResult_desc() {
            return result_desc;
        }

        public void setResult_desc(String result_desc) {
            this.result_desc = result_desc;
        }

        public int getResult_code() {
            return result_code;
        }

        public void setResult_code(int result_code) {
            this.result_code = result_code;
        }

        public String getRequest_id() {
            return request_id;
        }

        public void setRequest_id(String request_id) {
            this.request_id = request_id;
        }
    }
}
