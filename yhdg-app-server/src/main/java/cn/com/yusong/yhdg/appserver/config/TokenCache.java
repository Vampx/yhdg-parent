package cn.com.yusong.yhdg.appserver.config;

import cn.com.yusong.yhdg.common.utils.IdUtils;
import cn.com.yusong.yhdg.common.tool.memcached.MemCachedClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


/**
 * 一个简单的内存缓存
 */
@Component
public class TokenCache {
    public final static String CACHE_KEY_TOKEN = "token1:";
    public final static String CACHE_KEY_TOKEN_CUSTOMER_ID = "customerId:";
    public static ThreadLocal<Data> tokenCacheHolder = new ThreadLocal<Data>();

    @Autowired
    MemCachedClient memCachedClient;

    public TokenCache() {
    }

    public void touch(TokenCache.Data data, int expireTime) {
        data.touchTime = System.currentTimeMillis();
        memCachedClient.set(CACHE_KEY_TOKEN + data.token, data, expireTime);
    }

    public void put(TokenCache.Data data, int expireTime) {
        memCachedClient.set(CACHE_KEY_TOKEN + data.token, data, expireTime);
    }

    public Data putUser(long id, int agentId, int expireTime) {
        String token = IdUtils.uuid();
        Data data = new Data();
        data.token = token;
        data.isUser = true;
        data.userId = id;
        data.agentId = agentId;
        data.rootAgentId = agentId;
        data.createTime = System.currentTimeMillis();

        memCachedClient.set(CACHE_KEY_TOKEN + token, data, expireTime);
        return data;
    }

    public Data putOpenCustomer(long id, int partnerId, int clientType, String openid, String secondOpenid, int appId, int expireTime) {
        String token = IdUtils.uuid();
        Data data = new Data();
        data.token = token;
        data.isCustomer = true;
        data.customerId = id;
        data.partnerId = partnerId;
        data.clientType = clientType;
        data.openid = openid;
        data.secondOpenid = secondOpenid;
        data.appId = appId;

        data.createTime = System.currentTimeMillis();

        memCachedClient.set(CACHE_KEY_TOKEN + token, data, expireTime);
        memCachedClient.set(CACHE_KEY_TOKEN_CUSTOMER_ID + token, data.customerId, expireTime);

        return data;
    }

    public Data putCustomer(long id, int partnerId, String mobile, int clientType, int appId, Integer pushType, String pushToken, int expireTime) {
        String token = IdUtils.uuid();
        Data data = new Data();
        data.token = token;
        data.isCustomer = true;
        data.customerId = id;
        data.partnerId = partnerId;
        data.mobile = mobile;
        data.clientType = clientType;
        data.appId = appId;
        data.pushType = pushType;
        data.pushToken = pushToken;

        data.createTime = System.currentTimeMillis();

        memCachedClient.set(CACHE_KEY_TOKEN + token, data, expireTime);
        memCachedClient.set(CACHE_KEY_TOKEN_CUSTOMER_ID + token, data.customerId, expireTime);

        return data;
    }

    public Data putCabinetApp(String cabinetAppId, String cabineId, int expireTime) {
        String token = IdUtils.uuid();
        Data data = new Data();
        data.token = token;
        data.isCabinetApp = true;
        data.cabinetAppId = cabinetAppId;
        data.cabinetId = cabineId;

        data.createTime = System.currentTimeMillis();

        memCachedClient.set(CACHE_KEY_TOKEN + token, data, expireTime);
        return data;
    }

    public Data putCopy(TokenCache.Data tokenData, int expireTime) {
        TokenCache.Data copy = tokenData.copy();
        copy.touchTime = System.currentTimeMillis();
        memCachedClient.set(CACHE_KEY_TOKEN + copy.token, copy, expireTime);
        memCachedClient.set(CACHE_KEY_TOKEN_CUSTOMER_ID + copy.token, copy.customerId, expireTime);
        return copy;
    }

    public Data get(String token) {
        String key = CACHE_KEY_TOKEN + token;
        return (Data) memCachedClient.get(key);
    }

    public void remove(String token) {
        String key = CACHE_KEY_TOKEN + token;
        memCachedClient.delete(key);
        memCachedClient.delete(CACHE_KEY_TOKEN_CUSTOMER_ID + token);
    }

    public static class Data implements java.io.Serializable {
        public static int CLIENT_TYPE_MP = 1;
        public static int CLIENT_TYPE_FW = 2;
        public static int CLIENT_TYPE_APP = 3;
        public static int CLIENT_TYPE_CABINET_APP = 4;
        public static int CLIENT_TYPE_MA = 5;

        public String token;

        public boolean isCustomer;
        public boolean isUser;
        public boolean isCabinetApp;

        public long customerId;
        public String mobile;
        public int partnerId;
        public String cabinetAppId;
        public String cabinetId;
        public long userId;
        public int rootAgentId;
        public int agentId;
        public int appId;
        public int clientType;
        public String openid;
        public String secondOpenid;
        public long laxinId;

        public Integer pushType;
        public String pushToken;

        public long createTime;
        public long touchTime = System.currentTimeMillis();

        public Data copy() {
            Data data = new Data();
            data.token = IdUtils.uuid();

            data.isCustomer = isCustomer;
            data.isUser = isUser;
            data.isCabinetApp = isCabinetApp;

            data.customerId = customerId;
            data.cabinetAppId = cabinetAppId;
            data.cabinetId = cabinetId;
            data.userId = userId;
            data.rootAgentId = rootAgentId;
            data.agentId = agentId;
            data.appId = appId;
            data.clientType = clientType;
            data.openid = openid;
            data.secondOpenid = secondOpenid;
            data.laxinId = laxinId;

            data.pushType = pushType;
            data.pushToken = pushToken;

            data.createTime = createTime;
            data.touchTime = touchTime;
            return data;
        }

        public boolean pushTokenEquals(Data o) {
            if(pushType == null || o.pushType == null || pushToken == null || o.pushToken == null) {
                return true;
            }

            return pushType.equals(o.pushType) && pushToken.equals(o.pushToken);
        }
    }
}
