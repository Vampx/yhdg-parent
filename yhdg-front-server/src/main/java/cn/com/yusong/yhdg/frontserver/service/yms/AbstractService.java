package cn.com.yusong.yhdg.frontserver.service.yms;


import cn.com.yusong.yhdg.common.constant.CacheKey;
import cn.com.yusong.yhdg.common.domain.yms.TerminalStrategy;
import cn.com.yusong.yhdg.common.tool.memcached.MemCachedConfig;
import cn.com.yusong.yhdg.frontserver.config.AppConfig;
import cn.com.yusong.yhdg.frontserver.persistence.yms.TerminalMapper;
import cn.com.yusong.yhdg.frontserver.persistence.yms.TerminalStrategyMapper;
import cn.com.yusong.yhdg.common.tool.memcached.MemCachedClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AbstractService {

    @Autowired
    AppConfig config;
    @Autowired
    MemCachedClient memCachedClient;
    @Autowired
    TerminalStrategyMapper terminalStrategyMapper;
    @Autowired
    TerminalMapper terminalMapper;

    public TerminalStrategy findTerminalStrategy(long id) {
        String key = CacheKey.key(CacheKey.K_ID_V_TERMINAL_STRATEGY, id);

        TerminalStrategy strategy = (TerminalStrategy) memCachedClient.get(key);
        if (strategy != null) {
            return strategy;
        }

        strategy = terminalStrategyMapper.find(id);
        if (strategy != null) {
            memCachedClient.set(key, strategy, MemCachedConfig.CACHE_ONE_WEEK);
        }

        return strategy;
    }

    private static class Key {
        int sourceType;
        int sourceId;

        public int getSourceType() {
            return sourceType;
        }

        public void setSourceType(int sourceType) {
            this.sourceType = sourceType;
        }

        public int getSourceId() {
            return sourceId;
        }

        public void setSourceId(int sourceId) {
            this.sourceId = sourceId;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Key key = (Key) o;

            if (sourceType != key.sourceType) return false;
            return sourceId == key.sourceId;

        }

        @Override
        public int hashCode() {
            int result = sourceType;
            result = 31 * result + sourceId;
            return result;
        }
    }

}
