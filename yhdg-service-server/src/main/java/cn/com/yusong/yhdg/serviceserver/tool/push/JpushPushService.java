package cn.com.yusong.yhdg.serviceserver.tool.push;

import cn.com.yusong.yhdg.common.constant.ConstEnum;
import cn.jiguang.common.resp.APIRequestException;
import cn.jpush.api.JPushClient;
import cn.jpush.api.push.PushResult;
import cn.jpush.api.push.model.Message;
import cn.jpush.api.push.model.Platform;
import cn.jpush.api.push.model.PushPayload;
import cn.jpush.api.push.model.audience.Audience;
import cn.jpush.api.push.model.notification.AndroidNotification;
import cn.jpush.api.push.model.notification.IosAlert;
import cn.jpush.api.push.model.notification.IosNotification;
import cn.jpush.api.push.model.notification.Notification;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * https://docs.jiguang.cn/jpush/server/push/rest_api_v3_push/
 */
public class JpushPushService implements PushService {
    static Logger log = LogManager.getLogger(JpushPushService.class);
    String masterSecret;
    String appKey;

    JPushClient jpushClient;

    public static void main(String[] args) throws Exception {
        AndroidPushNotification param = new AndroidPushNotification();
        param.title = "标题1";
        param.content = "内容1";
        param.registrationIdList.add("1a0018970af43faf872");

        JpushPushService service = new JpushPushService("2c8cb42db680491600220bde", "2f7c912b95a469076800b02b");
        Result result = service.sendNotification(param);
        System.out.println(result.success);
    }

    public JpushPushService(String masterSecret, String appKey) {
        this.masterSecret = masterSecret;
        this.appKey = appKey;

        jpushClient = new JPushClient(masterSecret, appKey);
    }

    @Override
    public Result sendNotification(AndroidPushNotification param) throws Exception {
        Notification notification = Notification.newBuilder()
                .setAlert(param.content)
                .addPlatformNotification(AndroidNotification.newBuilder()
                        .setTitle(param.title)
                        .addExtras(param.extras)
                        .setAlertType(param.alterType)
                        .build())
                .build();

        Audience audience = null;
        log.debug("极光android通知 token:{}",param.registrationIdList);
        if(!param.registrationIdList.isEmpty()) {
            audience = Audience.registrationId(param.registrationIdList);
        } else if(!param.aliasList.isEmpty()) {
            audience = Audience.alias(param.aliasList);
        }

        if(audience == null) {
            throw new RuntimeException("audience is null");
        }

        PushPayload payload = PushPayload.newBuilder()
                .setPlatform(Platform.android())
                .setAudience(audience)
                .setNotification(notification)
                .build();


        Result result = new Result();

        if(log.isDebugEnabled()) {
            log.debug("push {}", param);
        }

        try {
            PushResult pushResult = jpushClient.sendPush(payload);
            result.success = pushResult.isResultOK();
            result.data = pushResult;
            if(pushResult.error != null ) {
                result.message = "[push:Android_JpushPush,errorCode:" + pushResult.error.getCode() + ",errorMsg:" + pushResult.error.getMessage() + "]";
            }

            if(result.success) {
                if(log.isDebugEnabled()) {
                    log.debug("push success");
                }
            } else {
                log.error("push fail, {}", result.message);
            }
        } catch (APIRequestException exception) {
            log.error("push fail", exception);

            result.success = false;
            result.message = exception.getErrorCode() + exception.getErrorMessage();
            log.error("jpush error code: {}, error_msg: {}", exception.getErrorCode(), exception.getErrorMessage());
        }

        return result;
    }

    @Override
    public Result sendNotification(IosPushNotification param) throws Exception {

        IosAlert.Builder iosBuilder = IosAlert.newBuilder()
                .setTitleAndBody(param.title, null, param.content);

        IosNotification.Builder builder = IosNotification.newBuilder()
                        .setAlert(iosBuilder.build())
                        .addExtras(param.extras).setCategory("1");

        if(!param.extras.isEmpty()){
            if(StringUtils.isNotEmpty(param.extras.get("isPlay"))){
                if(ConstEnum.Flag.TRUE.getValue() == Integer.parseInt(param.extras.get("isPlay"))){
                    builder.setContentAvailable(true);//；朗读
                    builder.setSound(null);
                }
            }
        }else{
            builder.setSound("default");
        }
        Notification notification = Notification.newBuilder()
                .addPlatformNotification(builder.build())
                .build();
        Audience audience = null;
        log.debug("极光IOS通知 token:{}",param.registrationIdList);
        if(!param.registrationIdList.isEmpty()) {
            audience = Audience.registrationId(param.registrationIdList);
        } else if(!param.aliasList.isEmpty()) {
            audience = Audience.alias(param.aliasList);
        }
        if(audience == null) {
            throw new RuntimeException("audience is null");
        }
        PushPayload payload = PushPayload.newBuilder()
                .setPlatform(Platform.ios())
                .setAudience(audience)
                .setNotification(notification)
                .build();
        Result result = new Result();

        if(log.isDebugEnabled()) {
            log.debug("push {}", param);
        }

        try {
            PushResult pushResult = jpushClient.sendPush(payload);
            result.success = pushResult.isResultOK();
            result.data = pushResult;
            if (pushResult.error != null) {
                result.message = "[push:IOS_JpushPush,errorCode:" + pushResult.error.getCode() + ",errorMsg:" + pushResult.error.getMessage() + "]";
            }

            if(result.success) {
                if(log.isDebugEnabled()) {
                    log.debug("push success");
                }
            } else {
                log.error("push fail, {}", result.message);
            }
        } catch (APIRequestException exception) {
            log.error("push fail", exception);

            result.success = false;
            result.message = exception.getErrorCode() + exception.getErrorMessage();
            log.error("jpush error code: {}, error_msg: {}", exception.getErrorCode(), exception.getErrorMessage());
        }
        return result;
    }

    @Override
    public Result sendMessage(AndroidPushMessage param) throws Exception {
        Message.Builder builder = Message.newBuilder();
        builder.setTitle(param.title);
        builder.setMsgContent(param.content);
        builder.setContentType(param.contentType);
        builder.addExtras(param.extras);

        Audience audience = null;
        if(!param.registrationIdList.isEmpty()) {
            audience = Audience.registrationId(param.registrationIdList);
        } else if(!param.aliasList.isEmpty()) {
            audience = Audience.alias(param.aliasList);
        }

        if(audience == null) {
            throw new RuntimeException("audience is null");
        }

        PushPayload payload = PushPayload.newBuilder()
                .setPlatform(Platform.android())
                .setAudience(audience)
                .setMessage(builder.build())
                .build();


        Result result = new Result();

        if(log.isDebugEnabled()) {
            log.debug("push {}", param);
        }

        try {
            PushResult pushResult = jpushClient.sendPush(payload);
            result.success = pushResult.isResultOK();
            result.data = pushResult;
            if(pushResult.error != null ) {
                result.message = "[push:Android_JpushPush,errorCode:" + pushResult.error.getCode() + ",errorMsg:" + pushResult.error.getMessage() + "]";
            }

            if(result.success) {
                if(log.isDebugEnabled()) {
                    log.debug("push success");
                }
            } else {
                log.error("push fail, {}", result.message);
            }

        } catch (APIRequestException exception) {
            log.error("push fail", exception);

            result.success = false;
            result.message = exception.getErrorCode() + exception.getErrorMessage();
            log.error("jpush error code: {}, error_msg: {}", exception.getErrorCode(), exception.getErrorMessage());
        }

        return result;
    }

    @Override
    public Result sendMessage(IosPushMessage param) throws Exception {
        Message.Builder builder = Message.newBuilder();
        builder.setTitle(param.title);
        builder.setMsgContent(param.content);
        builder.setContentType(param.contentType);
        builder.addExtras(param.extras);

        Audience audience = null;
        if(!param.registrationIdList.isEmpty()) {
            audience = Audience.registrationId(param.registrationIdList);
        } else if(!param.aliasList.isEmpty()) {
            audience = Audience.alias(param.aliasList);
        }

        if(audience == null) {
            throw new RuntimeException("audience is null");
        }

        PushPayload payload = PushPayload.newBuilder()
                .setPlatform(Platform.ios())
                .setAudience(audience)
                .setMessage(builder.build())
                .build();


        Result result = new Result();

        if(log.isDebugEnabled()) {
            log.debug("push {}", param);
        }

        try {
            PushResult pushResult = jpushClient.sendPush(payload);
            result.success = pushResult.isResultOK();
            result.data = pushResult;
            if(pushResult.error != null) {
                result.message = "[push:IOS_JpushPush,errorCode:" + pushResult.error.getCode() + ",errorMsg:" + pushResult.error.getMessage() + "]";
            }

            if(result.success) {
                if(log.isDebugEnabled()) {
                    log.debug("push success");
                }
            } else {
                log.error("push fail, {}", result.message);
            }

        } catch (APIRequestException exception) {
            log.error("push fail", exception);

            result.success = false;
            result.message = exception.getErrorCode() + exception.getErrorMessage();
            log.error("jpush error code: {}, error_msg: {}", exception.getErrorCode(), exception.getErrorMessage());
        }

        return result;
    }

//    public static  void  main(String[] agrs) throws IOException {
//        JPushClient jpushClient1 = new JPushClient("b55c206b90f28a0dbe941c41","1c797cf86bd49462dcd882c7");
//        Map<String, String> extras = new HashMap<String, String>();
//        extras.put("isPlay","1");
//
//        //透传
//        //
//       /* Message.Builder builder = Message.newBuilder();
//        builder.setTitle("极光标题title");
//        builder.setMsgContent("极光内容content");
//        builder.setContentType("text");
//        builder.addExtras(extras);
//        Audience audience = null;
//
//        audience = Audience.registrationId( "160a3797c8011a350c7");
//
//
//        if(audience == null) {
//            throw new RuntimeException("audience is null");
//        }
//
//        PushPayload payload = PushPayload.newBuilder()
//                .setPlatform(Platform.android())
//                .setAudience(audience)
//                .setMessage(builder.build())
//                .build();*/
//
//
//
//        //通知
//
//        Notification notification = Notification.newBuilder()
//                .setAlert("极光推送内容content")
//                .addPlatformNotification(AndroidNotification.newBuilder()
//                        .setTitle("极光推送标题 有声音")
//
//                        .addExtras(extras)
//                        .setAlertType(-1)
//                        .build())
//                .build();
//        Audience audience = null;
////
//        audience = Audience.registrationId( "140fe1da9eaed957747");
//
//
//        if(audience == null) {
//            throw new RuntimeException("audience is null");
//        }
//        PushPayload payload = PushPayload.newBuilder()
//                .setPlatform(Platform.android())
//                .setAudience(audience)
//                .setNotification(notification)
//                .build();
//
//        //1114a89792ac1f587a4
//
//
//
//        //苹果 IOS 通知
//       /* IosAlert.Builder iosBuilder = IosAlert.newBuilder()
//                .setTitleAndBody("标题", "subtitle", "body 内容");
//
//        IosNotification.Builder builder = IosNotification.newBuilder()
//                .setAlert(iosBuilder.build())
//                .addExtras(Collections.EMPTY_MAP).setCategory("1")
//                //.setContentAvailable(true) //语音参数
//                //.setSound(null);
//                .setSound("default");
//
//
//        Notification notification = Notification.newBuilder()
//                //.setAlert(IosAlert.newBuilder().setTitleAndBody("标题", "sub标题", "body 内容").build())
//                .addPlatformNotification(builder.build())
//                .build();
//
//        Audience audience = null;
//
//            audience = Audience.registrationId("1114a89792ac1f587a4");
//
//
//        if(audience == null) {
//            throw new RuntimeException("audience is null");
//        }
//
//        PushPayload payload = PushPayload.newBuilder()
//                .setPlatform(Platform.ios())
//                .setAudience(audience)
//                .setNotification(notification)
//                .build();
//        */
//        Result result = new Result();
//        try {
//
//            PushResult pushResult = jpushClient1.sendPush(payload);
//            result.success = pushResult.isResultOK();
//            result.data = pushResult;
//            if(pushResult.error != null ) {
//                  result.message = "[push:IOS_JpushPush,errorCode:"+pushResult.error.getCode()+",errorMsg:"+pushResult.error.getMessage()+"]";
//            }
//            } catch (APIConnectionException e) {
//            e.printStackTrace();
//        } catch (APIRequestException e) {
//            e.printStackTrace();
//        }
//    }
}
