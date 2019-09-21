package cn.com.yusong.yhdg.routeserver.comm.client;

import cn.com.yusong.yhdg.common.constant.ConstEnum;
import cn.com.yusong.yhdg.common.constant.Constant;
import cn.com.yusong.yhdg.common.tool.zk.Listener;
import cn.com.yusong.yhdg.common.tool.zk.ZookeeperEndpoint;
import cn.com.yusong.yhdg.common.utils.AppUtils;
import cn.com.yusong.yhdg.routeserver.config.AppConfig;
import cn.com.yusong.yhdg.routeserver.config.ServerInfo;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.zookeeper.CreateMode;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ZkClient {
    final static Logger log = LogManager.getLogger(ZkClient.class);

    AppConfig config;
    ZookeeperEndpoint endpoint;

    public ZkClient(AppConfig config) {
        this.config = config;
        endpoint = new ZookeeperEndpoint(this.config.getZkUrl(), ConstEnum.Node.NODE_YHDG.getValue());
        config.zkClient = this;
    }

    public void startup() throws Exception {
        String path = ConstEnum.Node.NODE_ROUTE_SERVER.getValue();
        endpoint.create(path, CreateMode.PERSISTENT);

        path = ConstEnum.Node.NODE_CABINET_SERVER.getValue();
        endpoint.create(path, CreateMode.PERSISTENT);

        List<String> children = endpoint.addChildrenChangeListener(childrenChangeCabinetListener, path);
        changeCabinetServer(children);
		
		path = ConstEnum.Node.NODE_BATTERY_SERVER.getValue();
        endpoint.create(path, CreateMode.PERSISTENT);

        children = endpoint.addChildrenChangeListener(childrenBatteryListener, path);
        changeBatteryServer(children);

        path = ConstEnum.Node.NODE_FRONT_SERVER.getValue();
        endpoint.create(path, CreateMode.PERSISTENT);

        children = endpoint.addChildrenChangeListener(childrenChangeFrontListener, path);
        changeFrontServer(children);
    }

    public void close() {
        endpoint.close();
    }

    Listener childrenBatteryListener = new Listener() {
        @Override
        public void listen(Param param) throws Exception {
            List<String> children = (List<String>) param.result;
            changeBatteryServer(children);

        }
    };

    private void changeBatteryServer(List<String> children) throws Exception {
        List<ServerInfo> serverInfoList = new ArrayList<ServerInfo>(10);
        for(String child : children) {
            byte[] data = endpoint.get(ConstEnum.Node.NODE_BATTERY_SERVER.getValue() + "/" + child);
            String source = new String(data, Constant.CHARSET_UTF_8);
            Map<String, String> map = AppUtils.stringToMap(source, ",");

            if(log.isDebugEnabled()) {
                log.debug("data: {}", source);
            }
            serverInfoList.add(new ServerInfo(
                    map.get("server.version"),
                    getOuterIp(map.get("server.ip")),
                    Integer.parseInt(map.get("server.port")),
                    Integer.parseInt(map.get("server.weight"))
            ));
        }

        config.batteryServerSelector.update(serverInfoList);
    }
	
    private void changeCabinetServer(List<String> children) throws Exception {
        List<ServerInfo> serverInfoList = new ArrayList<ServerInfo>(10);
        for(String child : children) {
            byte[] data = endpoint.get(ConstEnum.Node.NODE_CABINET_SERVER.getValue() + "/" + child);
            String source = new String(data, Constant.CHARSET_UTF_8);
            Map<String, String> map = AppUtils.stringToMap(source, ",");

            if(log.isDebugEnabled()) {
                log.debug("data: {}", source);
            }
            serverInfoList.add(new ServerInfo(
                    map.get("server.version"),
                    getOuterIp(map.get("server.ip")),
                    Integer.parseInt(map.get("server.port")),
                    Integer.parseInt(map.get("server.weight"))
            ));
        }

        config.cabinetServerSelector.update(serverInfoList);
    }


    Listener childrenChangeCabinetListener = new Listener() {
        @Override
        public void listen(Param param) throws Exception {
            List<String> children = (List<String>) param.result;
            changeCabinetServer(children);

        }
    };

    private void changeFrontServer(List<String> children) throws Exception {
        List<ServerInfo> serverInfoList = new ArrayList<ServerInfo>(10);
        for(String child : children) {
            byte[] data = endpoint.get(ConstEnum.Node.NODE_FRONT_SERVER.getValue() + "/" + child);
            String source = new String(data, Constant.CHARSET_UTF_8);
            Map<String, String> map = AppUtils.stringToMap(source, ",");

            if(log.isDebugEnabled()) {
                log.debug("data: {}", source);
            }
            serverInfoList.add(new ServerInfo(
                    map.get("server.version"),
                    getOuterIp(map.get("server.ip")),
                    Integer.parseInt(map.get("server.port")),
                    Integer.parseInt(map.get("server.weight"))
            ));
        }

        config.frontServerSelector.update(serverInfoList);
    }


    Listener childrenChangeFrontListener = new Listener() {
        @Override
        public void listen(Param param) throws Exception {
            List<String> children = (List<String>) param.result;
            changeFrontServer(children);

        }
    };

    private String getOuterIp(String ip) {
        if(StringUtils.isEmpty(ip)) {
            throw new IllegalArgumentException("ip is empty");
        }
        String[] list = StringUtils.split(ip, ", ");
        return list[list.length - 1];
    }

}
