package cn.com.yusong.yhdg.weixinserver.service;

import cn.com.yusong.yhdg.common.constant.ConstEnum;
import cn.com.yusong.yhdg.common.spring.SpringContextHolder;
import cn.com.yusong.yhdg.common.tool.zk.ZookeeperEndpoint;
import cn.com.yusong.yhdg.weixinserver.config.AppConfig;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.zookeeper.CreateMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.ServletContext;

@Service
public class SystemListener {

    static Logger log = LogManager.getLogger(SystemListener.class);

    @Autowired
    AppConfig appConfig;


    public void start(ServletContext servletContext) {
        SpringContextHolder.assertContextInjected();

        try {
            appConfig.init(servletContext);
            registerNode();
        } catch(Exception e) {
            e.printStackTrace();
            log.error("系统初始化错误", e);
            throw new RuntimeException(e);
        }
    }

    public void stop(ServletContext ServletContext) {
        if(appConfig.zkClient != null) {
            appConfig.zkClient.close();
        }
    }

    void registerNode() throws Exception {
        String path = ConstEnum.Node.NODE_WEIXIN_SERVER.getValue() + ConstEnum.Node.NODE_WEIXIN_SERVER_CHILDREN.getValue();
        ZookeeperEndpoint endpoint = new ZookeeperEndpoint(appConfig.getZkUrl(), ConstEnum.Node.NODE_YHDG.getValue());
        appConfig.zkClient = endpoint;

        endpoint.create(ConstEnum.Node.NODE_WEIXIN_SERVER.getValue(), CreateMode.PERSISTENT);
        endpoint.create(path, CreateMode.PERSISTENT_SEQUENTIAL);

        endpoint.create(ConstEnum.Node.NODE_CABINET_SERVER.getValue(), CreateMode.PERSISTENT);
//        List<String> children = endpoint.addChildrenChangeListener(appConfig.serviceServerClientManager, ConstEnum.Node.NODE_SERVICE_SERVER.getValue());
//        appConfig.serviceServerClientManager.listen(new Listener.Param(children, ConstEnum.Node.NODE_SERVICE_SERVER.getValue()));
//
//        endpoint.create(ConstEnum.Node.NODE_TERMINAL_SERVER.getValue(), CreateMode.PERSISTENT);
//
//        children = endpoint.addChildrenChangeListener(appConfig.terminalServerClientManager, ConstEnum.Node.NODE_TERMINAL_SERVER.getValue());
//        appConfig.terminalServerClientManager.listen(new Listener.Param(children, ConstEnum.Node.NODE_TERMINAL_SERVER.getValue()));
    }
}