package cn.com.yusong.yhdg.serviceserver.tool.push;

public interface PushService {

    public abstract Result sendNotification(AndroidPushNotification notification) throws Exception;

    public abstract Result sendNotification(IosPushNotification notification) throws Exception;

    public abstract Result sendMessage(AndroidPushMessage message) throws Exception;

    public abstract Result sendMessage(IosPushMessage message) throws Exception;
}