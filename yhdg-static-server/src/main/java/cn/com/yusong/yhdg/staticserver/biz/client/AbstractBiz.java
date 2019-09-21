package cn.com.yusong.yhdg.staticserver.biz.client;


import cn.com.yusong.yhdg.common.tool.netty.Biz;
import cn.com.yusong.yhdg.staticserver.config.AppConfig;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class AbstractBiz extends Biz {
    @Autowired
    protected AppConfig config;
}
