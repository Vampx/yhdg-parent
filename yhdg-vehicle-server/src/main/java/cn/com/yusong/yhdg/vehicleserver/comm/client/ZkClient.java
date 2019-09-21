package cn.com.yusong.yhdg.vehicleserver.comm.client;

import cn.com.yusong.yhdg.common.constant.ConstEnum;
import cn.com.yusong.yhdg.common.tool.zk.ZookeeperEndpoint;
import cn.com.yusong.yhdg.vehicleserver.config.AppConfig;
import org.apache.zookeeper.CreateMode;

public class ZkClient {
    AppConfig config;
    ZookeeperEndpoint endpoint;

    public ZkClient(AppConfig config) {
        this.config = config;
        endpoint = new ZookeeperEndpoint(this.config.getZkUrl(), ConstEnum.Node.NODE_YHDG.getValue());
        config.zkClient = this;
    }

    public void startup() throws Exception {
        String path = ConstEnum.Node.NODE_VEHICLE_SERVER.getValue() + "/" + config.getZkName() + "-" + config.getServerIp() + ":" + config.getServerPort();
        endpoint.create(ConstEnum.Node.NODE_VEHICLE_SERVER.getValue(), CreateMode.PERSISTENT);

        if(endpoint.exists(path) != null) {
            endpoint.delete(path, false);
        }
        endpoint.create(path, CreateMode.EPHEMERAL, config.getZkData());
        endpoint.addEphemeral(path, CreateMode.EPHEMERAL, config.getZkData());

        endpoint.addDataChangeListener(config.configListener, path);
    }

    public byte[] get(String path) throws Exception {
        return endpoint.get(path);
    }

    public void close() {
        endpoint.close();
    }
}
