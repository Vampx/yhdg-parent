package cn.com.yusong.yhdg.agentappserver.comm.client;

import cn.com.yusong.yhdg.agentappserver.config.AppConfig;
import cn.com.yusong.yhdg.common.constant.ConstEnum;
import cn.com.yusong.yhdg.common.constant.Constant;
import cn.com.yusong.yhdg.common.tool.netty.BizFactory;
import cn.com.yusong.yhdg.common.tool.netty.DefaultBizFactory;
import cn.com.yusong.yhdg.common.tool.netty.Message;
import cn.com.yusong.yhdg.common.tool.zk.Listener;
import cn.com.yusong.yhdg.common.utils.AppUtils;
import io.netty.channel.ChannelFuture;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class CabinetServerClientManager implements Listener {
    static Logger log = LogManager.getLogger(CabinetServerClientManager.class);

    final BizFactory bizFactory = new DefaultBizFactory();
    final Map<String, CabinetServerClient> clientMap = new ConcurrentHashMap<String, CabinetServerClient>();
    AppConfig config;

    public CabinetServerClientManager(AppConfig config) {
        this.config = config;
    }

    public ChannelFuture writeAndFlush(String address, Message message) {
        CabinetServerClient cabinetServerClient = clientMap.get(address);
        if(cabinetServerClient == null || cabinetServerClient.getChannel() == null || !cabinetServerClient.getChannel().isOpen()) {
            return null;
        }

        if(log.isDebugEnabled()) {
            log.debug("send:{}, message:{}, ", address, message);
        }

        return cabinetServerClient.getChannel().writeAndFlush(message);
    }

    public void writeAndFlush(Message message) {
        for(CabinetServerClient client : clientMap.values()) {
            if(client.getChannel() != null && client.getChannel().isOpen()) {
                client.getChannel().writeAndFlush(message);
            }
        }
    }

    public ChannelFuture write(String address, Message message) {
        CabinetServerClient cabinetServerClient = clientMap.get(address);
        if(cabinetServerClient == null || cabinetServerClient.getChannel() == null || !cabinetServerClient.getChannel().isOpen()) {
            return null;
        }

        return cabinetServerClient.getChannel().write(message);
    }

    @Override
    public void listen(Param param) throws Exception {
        List<String> children = (List<String>) param.result;
        Set<String> servers = new HashSet<String>(10);

        for(String child : children) {
            byte[] data = config.zkClient.get(ConstEnum.Node.NODE_CABINET_SERVER.getValue() + "/" + child);
            String source = new String(data, Constant.CHARSET_UTF_8);
            Map<String, String> map = AppUtils.stringToMap(source, ",");

            if(log.isDebugEnabled()) {
                log.debug("child: {}", source);
            }

            String host = getInnerIp(map.get("server.ip"));
            int port = Integer.parseInt(map.get("server.port"));
            String key = host + ":" + port;
            String threadName = "CabinetServerClient/" + key;
            servers.add(key);

            if(!clientMap.containsKey(key)) {
                CabinetServerClient client = new CabinetServerClient(host, port, threadName, bizFactory, config.executorService);
                client.startup();

                clientMap.put(key, client);
            }
        }

        for(String key : clientMap.keySet()) {
            if(!servers.contains(key)) {
                CabinetServerClient client = clientMap.remove(key);
                if(client != null) {
                    client.close();
                }
            }
        }
    }

    private String getInnerIp(String ip) {
        if(StringUtils.isEmpty(ip)) {
            throw new IllegalArgumentException("ip is empty");
        }
        String[] list = StringUtils.split(ip, ", ");
        return list[0];
    }
}
