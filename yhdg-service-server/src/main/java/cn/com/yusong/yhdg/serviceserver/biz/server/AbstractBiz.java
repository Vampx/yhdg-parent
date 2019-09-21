package cn.com.yusong.yhdg.serviceserver.biz.server;

import cn.com.yusong.yhdg.common.tool.netty.Biz;
import cn.com.yusong.yhdg.serviceserver.config.AppConfig;
import cn.com.yusong.yhdg.common.tool.memcached.MemCachedClient;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class AbstractBiz extends Biz {

    @Autowired
    MemCachedClient memCachedClient;
    @Autowired
    AppConfig config;
}
