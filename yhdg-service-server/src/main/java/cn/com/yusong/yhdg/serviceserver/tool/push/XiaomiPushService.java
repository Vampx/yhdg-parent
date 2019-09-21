package cn.com.yusong.yhdg.serviceserver.tool.push;

import cn.com.yusong.yhdg.common.utils.AppUtils;
import com.xiaomi.push.sdk.ErrorCode;
import com.xiaomi.xmpush.server.Constants;
import com.xiaomi.xmpush.server.Message;
import com.xiaomi.xmpush.server.Sender;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.util.*;

/**
 * https://dev.mi.com/mipush/docs/server-sdk/message/builder/android.html
 */
public class XiaomiPushService implements PushService {
    static Logger log = LogManager.getLogger(XiaomiPushService.class);
    String packageName;
    Sender sender;

    public XiaomiPushService(int formalPlatform, String appSecret, String packageName) {
        /*选择环境。
        在正式环境下使用push服务，启动时需要调用如下代码：
        Constants.useOfficial();
        在测试环境下使用push服务，启动时需要调用如下代码：
        Constants.useSandbox();
        在测试环境中使用push服务不会影响线上用户。
        注：测试环境只提供对IOS支持，不支持Android。*/
        log.debug("formalPlatform:{}", formalPlatform);
        log.debug("appSecret", appSecret);
        log.debug("packageName",packageName);

        if(formalPlatform == 1) {
            Constants.useOfficial();
        } else {
            Constants.useSandbox();
        }

        this.packageName = packageName;
        sender = new Sender(appSecret);
    }

    @Override
    public Result sendNotification(AndroidPushNotification param) throws Exception {
        Message.Builder message = new Message.Builder()
                .payload(AppUtils.encodeJson2(param.extras))
                .title(param.title)
                .description(param.content)
                .restrictedPackageName(packageName)
                .passThrough(0)  //  设置消息是否通过透传的方式至App, 1表示透传消息, 0表示通知栏消息(默认是通知栏消息)
                .notifyType(0)   // 使用默认提示音提示
                ;
        setKeyValue(message, param.extras);
        com.xiaomi.xmpush.server.Result pushResult = null;

        if(log.isDebugEnabled()) {
            log.debug("push {}", param);
        }


        Result result = new Result();

        if(log.isDebugEnabled()) {
            log.debug("push {}", param);
        }

        try {
            if (!param.registrationIdList.isEmpty()) {
                log.debug("param.registrationIdList:{}", param.registrationIdList);
                pushResult = sender.send(message.build(), param.registrationIdList, 10);
            } else if(!param.aliasList.isEmpty()) {
                pushResult = sender.sendToAlias(message.build(), param.aliasList, 10);
            } else {
                throw new RuntimeException("audience is null");
            }

            result.success = pushResult.getErrorCode().getValue() == ErrorCode.Success.getValue();
            result.data = pushResult;
            result.message = "[push:XiaomiPush,errorCode:"+pushResult.getErrorCode().getValue()+",errorMsg:"+pushResult.getErrorCode().getDescription()+"]";

            if(result.success) {
                if(log.isDebugEnabled()) {
                    log.debug("push success");
                }
            } else {
                log.error("push fail, {}", result.message);
            }

        } catch (Exception e) {
            result.success = false;
            log.error("push fail", e);
        }

        return result;
    }

    @Override
    public Result sendNotification(IosPushNotification notification) throws Exception {
        throw new UnsupportedOperationException();
    }

    @Override
    public Result sendMessage(AndroidPushMessage param) throws Exception {

        Message.Builder message = new Message.Builder()
                .payload(AppUtils.encodeJson2(param.extras))
                .title(param.title)
                .description(param.content)
                .restrictedPackageName(packageName)
                .passThrough(0)  //  设置消息是否通过透传的方式至App, 1表示透传消息, 0表示通知栏消息(默认是通知栏消息)
                .notifyType(0);   // 使用默认提示音提示
        setKeyValue(message, param.extras);
        com.xiaomi.xmpush.server.Result pushResult = null;

        if(log.isDebugEnabled()) {
            log.debug("push {}", param);
        }

        Result result = new Result();

        try {
            if(!param.registrationIdList.isEmpty()) {
                pushResult = sender.send(message.build(), param.registrationIdList, 10);
            } else if(!param.aliasList.isEmpty()) {
                pushResult = sender.sendToAlias(message.build(), param.aliasList, 10);
            } else {
                throw new RuntimeException("audience is null");
            }

            result.success = pushResult.getErrorCode().getValue() == ErrorCode.Success.getValue();
            result.data = pushResult;
            result.message = "[push:XiaomiPush,errorCode:"+pushResult.getErrorCode().getValue()+",errorMsg:"+pushResult.getErrorCode().getDescription()+"]";

            if(result.success) {
                if(log.isDebugEnabled()) {
                    log.debug("push success");
                }
            } else {
                log.error("push fail, {}", result.message);
            }

        } catch (Exception e) {
            result.success = false;
            log.error("push fail", e);
        }


        return result;
    }

    @Override
    public Result sendMessage(IosPushMessage message) {
        throw new UnsupportedOperationException();
    }

    /**
     * 设置通知或者消息的键值对
     * @param builder
     * @param keyValue
     *
     * 控制app前台通知弹出  key - 'notify_foreground', value - 开启/关闭(1/0)
     * 打开当前app对应的Launcher Activity key-'Constants.EXTRA_PARAM_NOTIFY_EFFECT',value - 'Constants.NOTIFY_LAUNCHER_ACTIVITY'
     *
     *
     */
    public static void setKeyValue(Message.Builder builder, Map<String, String> keyValue) {
        if (keyValue != null && !keyValue.isEmpty()) {
            Iterator<Map.Entry<String, String>> iterator = keyValue.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<String, String> next = iterator.next();
                builder.extra(next.getKey(), next.getValue());


            }
        }
    }
    private static List<String> fillList(List<String> list){
        list.add("asdf");
        return list;
    }
    public static void main(String[] agrs){
        //Constants.useOfficial();

        //Sender sender = new Sender("rR/BLqH3wXe8K1Hb70LfNA=="); //骑手端
        Sender sender = new Sender("yhKQu5WGxs1/REXu270LFA=="); //调度端
        Map<String, String> extras = new HashMap<String, String>();
        extras.put("isPlay","1");
        Message.Builder message = new Message.Builder()
                .payload(null)
                .title("Test小米推送title111")
                .description("Test小米推送Content111")
                //.restrictedPackageName("com.yusong.user") //骑手端
                .restrictedPackageName("com.yusong.dispatchplugin") //调度端
                .passThrough(0)  //  设置消息是否通过透传的方式至App, 1表示透传消息, 0表示通知栏消息(默认是通知栏消息)
                .notifyType(0);   // 使用默认提示音提示
        setKeyValue(message, extras);

        com.xiaomi.xmpush.server.Result pushResult = null;
        List list = new ArrayList();
        list.add("d9HzTo3XTIBVcxdSz0nf3p1IquYHsrpz612KJ8sb3Po=");
     //           1QK9EK8xUkS9hbqMCbUTimGHUuTSRc0ZldTlopaBw04=
        log.error("param.registrationIdList:{}",  list);
        try {
            pushResult = sender.send(message.build(), list, 10);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }


        Result result = new Result();
        result.success = pushResult.getErrorCode().getValue() == ErrorCode.Success.getValue();
        result.data = pushResult;
        result.message = "[push:XiaomiPush,errorCode:"+pushResult.getErrorCode().getValue()+",errorMsg:"+pushResult.getErrorCode().getDescription()+"]";
        System.out.println(result.message);
    }

}