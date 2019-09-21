package cn.com.yusong.yhdg.agentappserver.config;

import cn.com.yusong.yhdg.common.utils.IdUtils;
import cn.com.yusong.yhdg.common.tool.memcached.MemCachedClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 一个简单的内存缓存
 */
@Component
public class TokenCache {
    public final static String CACHE_KEY_TOKEN = "token2:";
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

    public Data putUser(long id, int agentId, String shopId, long estateId, String agentCompanyId, int expireTime) {
        String token = IdUtils.uuid();
        Data data = new Data();
        data.token = token;
        data.isUser = true;
        data.userId = id;
        data.agentId = agentId;
        data.shopId = shopId;
        data.estateId = estateId;
        data.agentCompanyId = agentCompanyId;
        data.rootAgentId = agentId;
        data.createTime = System.currentTimeMillis();

        memCachedClient.set(CACHE_KEY_TOKEN + token, data, expireTime);
        return data;
    }

    public Data putCopy(TokenCache.Data tokenData, int expireTime) {
        TokenCache.Data copy = tokenData.copy();
        copy.touchTime = System.currentTimeMillis();
        memCachedClient.set(CACHE_KEY_TOKEN + copy.token, copy, expireTime);
        return copy;
    }

    public Data get(String token) {
        String key = CACHE_KEY_TOKEN + token;
        return (Data) memCachedClient.get(key);
    }

    public void remove(String token) {
        String key = CACHE_KEY_TOKEN + token;
        memCachedClient.delete(key);
    }

    public static class Data implements java.io.Serializable {
        public String token;

        public boolean isUser;

        public long userId;
        public int rootAgentId;
        public int agentId;
        public String shopId;
        public long estateId;
        public String agentCompanyId;
        public int appId;

        public long createTime;
        public long touchTime = System.currentTimeMillis();

        public Data copy() {
            Data data = new Data();
            data.token = IdUtils.uuid();

            data.isUser = isUser;

            data.userId = userId;
            data.rootAgentId = rootAgentId;
            data.agentId = agentId;
            data.shopId = shopId;
            data.estateId = estateId;
            data.agentCompanyId = agentCompanyId;
            data.appId = appId;

            data.createTime = createTime;
            data.touchTime = touchTime;
            return data;
        }
    }
}
