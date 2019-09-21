package cn.com.yusong.yhdg.serviceserver.tool.push;

import com.meizu.push.sdk.server.IFlymePush;
import com.meizu.push.sdk.server.constant.PushResponseCode;
import com.meizu.push.sdk.server.constant.ResultPack;
import com.meizu.push.sdk.server.model.push.PushResult;
import com.meizu.push.sdk.server.model.push.UnVarnishedMessage;
import com.meizu.push.sdk.server.model.push.VarnishedMessage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MeizuPushService implements PushService {
    Long appId;
    String appSercretKey;
    IFlymePush push = null;
    public MeizuPushService(String appId,String appSercretKey){
        this.appId = Long.parseLong(appId);
        this.appSercretKey = appSercretKey;
        push = new IFlymePush(appSercretKey);
    }

    @Override
    public Result sendNotification(AndroidPushNotification notification) throws IOException {


        //组装消息
        VarnishedMessage message = new VarnishedMessage.Builder().appId(appId)
                .title(notification.title).content(notification.content)
                .build();

        ResultPack<PushResult> result = null;
        if(!notification.registrationIdList.isEmpty()) {
            result = push.pushMessage(message, notification.registrationIdList);
        }else if(!notification.aliasList.isEmpty()) {
            result = push.pushMessageByAlias(message, notification.aliasList);
        }else{
            throw new RuntimeException("audience is null");
        }
        // 2 处理推送结果


        Result rs = new Result();
        rs.success = result.isSucceed();
        rs.data = result;
        rs.message =  "[push:MeizuPush,errorCode:"+result.getErrorCode().getValue()+",errorMsg:"+result.getErrorCode().getDescription()+"]";
        return rs;
    }

    @Override
    public Result sendNotification(IosPushNotification notification) {
        return null;
    }

    @Override
    public Result sendMessage(AndroidPushMessage param) throws IOException {
        //组装透传消息
        UnVarnishedMessage message = new UnVarnishedMessage.Builder()
                .appId(appId)
                .title(param.title)
                .content(param.content)
                .isOffLine(true)
                .validTime(10)
                .build();
        ResultPack<PushResult> result = null;
        if(!param.registrationIdList.isEmpty()) {
              result = push.pushMessage(message, param.registrationIdList);
        }else if(!param.aliasList.isEmpty()) {
              result = push.pushMessageByAlias(message, param.aliasList);
        }else{
            throw new RuntimeException("audience is null");
        }
            // 2 处理推送结果


            Result rs = new Result();
            rs.success = result.isSucceed();
            rs.data = result;
            rs.message =  "[push:MeizuPush,errorCode:"+result.getErrorCode().getValue()+",errorMsg:"+result.getErrorCode().getDescription()+"]";
            return rs;
        }

        @Override
        public Result sendMessage(IosPushMessage message) {
            return null;
        }

    private void handleResult(ResultPack<PushResult> result) {
        if (result.isSucceed()) {
            // 2 调用推送服务成功 （其中map为设备的具体推送结果，一般业务针对超速的code类型做处理）
            PushResult pushResult = result.value();
            String msgId = pushResult.getMsgId();//推送消息ID，用于推送流程明细排查
            Map<String, List<String>> targetResultMap = pushResult.getRespTarget();//推送结果，全部推送成功，则map为empty
            System.out.println("push msgId:" + msgId);
            System.out.println("push targetResultMap:" + targetResultMap);
            if (targetResultMap != null && !targetResultMap.isEmpty()) {
                // 3 判断是否有获取超速的target
                if (targetResultMap.containsKey(PushResponseCode.RSP_SPEED_LIMIT.getValue())) {
                    // 4 获取超速的target
                    List<String> rateLimitTarget = targetResultMap.get(PushResponseCode.RSP_SPEED_LIMIT.getValue());
                    System.out.println("rateLimitTarget is :" + rateLimitTarget);
                    //TODO 5 业务处理，重推......
                }
            }
        } else {
            // 调用推送接口服务异常 eg: appId、appKey非法、推送消息非法.....
            // result.code(); //服务异常码
            // result.comment();//服务异常描述
            System.out.println(String.format("pushMessage error code:%s comment:%s", result.code(), result.comment()));
        }
    }
}
