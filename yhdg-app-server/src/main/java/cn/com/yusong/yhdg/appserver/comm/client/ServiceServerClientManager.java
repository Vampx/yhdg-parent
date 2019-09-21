package cn.com.yusong.yhdg.appserver.comm.client;

import cn.com.yusong.yhdg.appserver.config.AppConfig;
import cn.com.yusong.yhdg.common.constant.ConstEnum;
import cn.com.yusong.yhdg.common.constant.Constant;
import cn.com.yusong.yhdg.common.tool.netty.Message;
import cn.com.yusong.yhdg.common.tool.zk.Listener;
import cn.com.yusong.yhdg.common.utils.AppUtils;
import io.netty.channel.ChannelFuture;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class ServiceServerClientManager implements Listener {
    static Logger log = LogManager.getLogger(ServiceServerClientManager.class);

    final Map<String, ServiceServerClient> clientMap = new ConcurrentHashMap<String, ServiceServerClient>();
    AppConfig config;

    public ServiceServerClientManager(AppConfig config) {
        this.config = config;
    }

    public ChannelFuture writeAndFlush(String address, Message message) {
        ServiceServerClient serviceServerClient = clientMap.get(address);
        if(serviceServerClient == null || serviceServerClient.getChannel() == null || !serviceServerClient.getChannel().isOpen()) {
            return null;
        }

        return serviceServerClient.getChannel().writeAndFlush(message);
    }

    public ServiceServerClient getOneClient() {
        int size = clientMap.size();
        if(size > 1) { //如果大于一个service-server 则随机选择一个
            int random = (int) (Math.random() * size);
            int index = 0;
            for(String key : clientMap.keySet()) {
                if(random == index) {
                    ServiceServerClient client = clientMap.get(key);
                    if(client.getChannel() != null && client.getChannel().isOpen()) {
                        return client;
                    } else {
                        break;
                    }
                }
                index++;
            }
        }

        for(ServiceServerClient client : clientMap.values()) {
            if(client.getChannel() != null && client.getChannel().isOpen()) {
                return client;
            }
        }

        return null;
    }

    public ChannelFuture write(String address, Message message) {
        ServiceServerClient terminalServerClient = clientMap.get(address);
        if(terminalServerClient == null || terminalServerClient.getChannel() == null || !terminalServerClient.getChannel().isOpen()) {
            return null;
        }

        return terminalServerClient.getChannel().write(message);
    }

    @Override
    public void listen(Param param) throws Exception {
        List<String> children = (List<String>) param.result;
        Set<String> servers = new HashSet<String>(10);

        for(String child : children) {
            byte[] data = config.zkClient.get(ConstEnum.Node.NODE_SERVICE_SERVER.getValue() + "/" + child);
            String source = new String(data, Constant.CHARSET_UTF_8);
            Map<String, String> map = AppUtils.stringToMap(source, ",");

            if(log.isDebugEnabled()) {
                log.debug("child: {}", source);
            }

            String host = map.get("server.ip");
            int port = Integer.parseInt(map.get("server.port"));
            String key = host + ":" + port;
            String threadName = "ServiceServerClient/" + key;
            servers.add(key);

            if(!clientMap.containsKey(key)) {
                ServiceServerClient client = new ServiceServerClient(host, port, threadName, config);
                client.startup();

                clientMap.put(key, client);
            }
        }

        for(String key : clientMap.keySet()) {
            if(!servers.contains(key)) {
                ServiceServerClient client = clientMap.remove(key);
                if(client != null) {
                    client.close();
                }
            }
        }
    }
}
