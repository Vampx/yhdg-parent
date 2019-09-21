package cn.com.yusong.yhdg.serviceserver.comm.client;

import cn.com.yusong.yhdg.common.constant.ConstEnum;
import cn.com.yusong.yhdg.common.tool.zk.Listener;
import cn.com.yusong.yhdg.common.tool.zk.ZookeeperEndpoint;
import cn.com.yusong.yhdg.serviceserver.config.AppConfig;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;

import java.io.IOException;
import java.util.List;

public class ZkClient {
    AppConfig config;
    ZookeeperEndpoint endpoint;

    public ZkClient(AppConfig config) {
        this.config = config;
        endpoint = new ZookeeperEndpoint(this.config.getZkUrl(), ConstEnum.Node.NODE_YHDG.getValue());
        config.zkClient = this;
    }

    public void startup() throws Exception {
        String path = ConstEnum.Node.NODE_SERVICE_SERVER.getValue() + "/" + config.getZkName() + "-" + config.getServerIp() + ":" + config.getServerPort();
        if(endpoint.exists(path) != null) {
            endpoint.delete(path, false);
        }

        endpoint.create(ConstEnum.Node.NODE_SERVICE_SERVER.getValue(), CreateMode.PERSISTENT);
        endpoint.create(path, CreateMode.EPHEMERAL, config.getZkData());
        endpoint.addEphemeral(path, CreateMode.EPHEMERAL, config.getZkData());

        endpoint.addDataChangeListener(listener, path);

        endpoint.create(ConstEnum.Node.NODE_TASK.getValue(), CreateMode.PERSISTENT);
    }

    public String create(String path, CreateMode createMode) throws Exception {
        return endpoint.create(path, createMode);
    }

    public String create(String path, CreateMode createMode, byte[] bytes) throws Exception {
        return endpoint.create(path, createMode, bytes);
    }

    public List<String> getChildren(String path) throws Exception {
        return endpoint.getChildren(path);
    }

    public byte[] get(String path) throws Exception {
        return endpoint.get(path);
    }

    public Stat exists(String path) throws Exception {
        return endpoint.exists(path);
    }

    public void delete(String path, boolean background) throws Exception {
        endpoint.delete(path, background);
    }


    public void close() {
        endpoint.close();
    }

    Listener listener = new Listener() {
        @Override
        public void listen(Param param) throws IOException {
            byte[] bytes = (byte[]) param.result;
            config.writeFile(bytes);
        }
    };



}
