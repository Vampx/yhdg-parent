package cn.com.yusong.yhdg.agentserver.service;

import cn.com.yusong.yhdg.common.constant.ConstEnum;
import cn.com.yusong.yhdg.common.spring.SpringContextHolder;
import cn.com.yusong.yhdg.common.tool.zk.Listener;
import cn.com.yusong.yhdg.common.tool.zk.ZookeeperEndpoint;
import cn.com.yusong.yhdg.agentserver.config.AppConfig;
import cn.com.yusong.yhdg.agentserver.job.JobManager;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.zookeeper.CreateMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.ServletContext;
import java.io.IOException;
import java.util.List;

@Service
public class SystemListener {

    static Logger log = LogManager.getLogger(SystemListener.class);

    @Autowired
    AppConfig appConfig;


    public void start(ServletContext servletContext) {
        SpringContextHolder.assertContextInjected();
        log.debug("SystemListener.start 0");

        try {
            log.debug("SystemListener.start 1");
            appConfig.init(servletContext);

            log.debug("SystemListener.start 2");
            deleteTempFile();

            log.debug("SystemListener.start 3");
            registerNode();

            JobManager jobManager = new JobManager(appConfig);
            jobManager.start();
            log.debug("SystemListener.start end");
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

    void deleteTempFile() throws IOException {
        AppConfig appConfig = SpringContextHolder.getBean(AppConfig.class);
        if(appConfig.tempDir.exists()) {
            try {
                FileUtils.cleanDirectory(appConfig.tempDir);
            } catch (Exception e) {
            }
        } else {
            appConfig.tempDir.mkdirs();
        }
    }

    void registerNode() throws Exception {
        String path = ConstEnum.Node.NODE_WEB_SERVER.getValue() + ConstEnum.Node.NODE_WEB_SERVER_CHILDREN.getValue();
        ZookeeperEndpoint endpoint = new ZookeeperEndpoint(appConfig.zkUrl, ConstEnum.Node.NODE_YHDG.getValue());
        appConfig.zkClient = endpoint;

        endpoint.create(ConstEnum.Node.NODE_WEB_SERVER.getValue(), CreateMode.PERSISTENT);
        endpoint.create(path, CreateMode.PERSISTENT_SEQUENTIAL);

        endpoint.create(ConstEnum.Node.NODE_CABINET_SERVER.getValue(), CreateMode.PERSISTENT);
        List<String> children = endpoint.addChildrenChangeListener(appConfig.cabinetServerClientManager, ConstEnum.Node.NODE_CABINET_SERVER.getValue());
        appConfig.cabinetServerClientManager.listen(new Listener.Param(children, ConstEnum.Node.NODE_CABINET_SERVER.getValue()));

        endpoint.create(ConstEnum.Node.NODE_SERVICE_SERVER.getValue(), CreateMode.PERSISTENT);
        children = endpoint.addChildrenChangeListener(appConfig.serviceServerClientManager, ConstEnum.Node.NODE_SERVICE_SERVER.getValue());
        appConfig.serviceServerClientManager.listen(new Listener.Param(children, ConstEnum.Node.NODE_SERVICE_SERVER.getValue()));

        endpoint.create(ConstEnum.Node.NODE_FRONT_SERVER.getValue(), CreateMode.PERSISTENT);
        children = endpoint.addChildrenChangeListener(appConfig.frontServerClientManager, ConstEnum.Node.NODE_FRONT_SERVER.getValue());
        appConfig.frontServerClientManager.listen(new Listener.Param(children, ConstEnum.Node.NODE_FRONT_SERVER.getValue()));
    }
}