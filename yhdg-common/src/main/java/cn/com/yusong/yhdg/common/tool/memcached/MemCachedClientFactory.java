package cn.com.yusong.yhdg.common.tool.memcached;


import org.springframework.beans.factory.FactoryBean;

import java.io.IOException;

public class MemCachedClientFactory implements FactoryBean<MemCachedClient> {

    MemCachedConfig config;

    @Override
    public MemCachedClient getObject() throws Exception {
        return build(config);
    }

    @Override
    public Class<?> getObjectType() {
        return cn.com.yusong.yhdg.common.tool.memcached.MemCachedClient.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

    public MemCachedConfig getConfig() {
        return config;
    }

    public void setConfig(MemCachedConfig config) {
        this.config = config;
    }

    public static MemCachedClient build(MemCachedConfig config) throws IOException {
        return new MemCachedClient(config);
    }



}
