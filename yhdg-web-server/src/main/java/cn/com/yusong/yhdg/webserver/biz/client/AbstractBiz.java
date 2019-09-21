package cn.com.yusong.yhdg.webserver.biz.client;

import cn.com.yusong.yhdg.common.tool.netty.Biz;
import cn.com.yusong.yhdg.webserver.config.AppConfig;
import cn.com.yusong.yhdg.common.tool.memcached.MemCachedClient;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class AbstractBiz extends Biz {

    @Autowired
    protected MemCachedClient memCachedClient;
    @Autowired
    protected AppConfig config;
}