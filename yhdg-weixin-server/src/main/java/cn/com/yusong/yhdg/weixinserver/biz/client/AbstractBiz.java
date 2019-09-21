package cn.com.yusong.yhdg.weixinserver.biz.client;

import cn.com.yusong.yhdg.common.tool.memcached.MemCachedClient;
import cn.com.yusong.yhdg.weixinserver.config.AppConfig;
import cn.com.yusong.yhdg.common.tool.netty.Biz;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class AbstractBiz extends Biz {

    @Autowired
    MemCachedClient memCachedClient;
    @Autowired
    AppConfig config;
}
