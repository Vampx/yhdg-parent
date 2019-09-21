package cn.com.yusong.yhdg.serviceserver.comm.server;

import cn.com.yusong.yhdg.common.tool.netty.BizFactory;
import cn.com.yusong.yhdg.common.tool.netty.NettyHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.ExecutorService;

public class MainServerHandler extends NettyHandler {

    static final Logger log = LogManager.getLogger(MainServerHandler.class);


    public MainServerHandler(BizFactory bizFactory, ExecutorService executorService) {
        super(bizFactory, executorService);
    }
}
