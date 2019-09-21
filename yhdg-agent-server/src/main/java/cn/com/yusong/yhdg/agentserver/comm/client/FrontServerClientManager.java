package cn.com.yusong.yhdg.agentserver.comm.client;

import cn.com.yusong.yhdg.common.constant.ConstEnum;
import cn.com.yusong.yhdg.common.constant.Constant;
import cn.com.yusong.yhdg.common.tool.netty.Message;
import cn.com.yusong.yhdg.common.tool.zk.Listener;
import cn.com.yusong.yhdg.common.utils.YhdgUtils;
import cn.com.yusong.yhdg.agentserver.config.AppConfig;
import io.netty.channel.ChannelFuture;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class FrontServerClientManager implements Listener {
    static Logger log = LogManager.getLogger(FrontServerClientManager.class);

    final Map<String, FrontServerClient> clientMap = new ConcurrentHashMap<String, FrontServerClient>();
    AppConfig config;

    public FrontServerClientManager(AppConfig config) {
        this.config = config;
    }

    @Override
    public void listen(Param param) throws Exception {
        List<String> children = (List<String>) param.result;
        Set<String> servers = new HashSet<String>(10);

        for(String child : children) {
            byte[] data = config.zkClient.get(ConstEnum.Node.NODE_FRONT_SERVER.getValue() + "/" + child);
            String source = new String(data, Constant.CHARSET_UTF_8);
            Map<String, String> map = YhdgUtils.stringToMap(source, ",");

            if(log.isDebugEnabled()) {
                log.debug("child: {}", source);
            }

            String host = getInnerIp(map.get("server.ip"));
            int port = Integer.parseInt(map.get("server.port"));
            String key = host + ":" + port;
            String threadName = "FrontServerClient/" + key;
            servers.add(key);

            if(!clientMap.containsKey(key)) {
                FrontServerClient client = new FrontServerClient(host, port, threadName, config.bizFactory, config.executorService);
                client.startup();

                clientMap.put(key, client);
            }
        }

        for(String key : clientMap.keySet()) {
            if(!servers.contains(key)) {
                FrontServerClient client = clientMap.remove(key);
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

    public void writeAndFlush(Message message) {
        for(FrontServerClient client : clientMap.values()) {
            if(client.getChannel() != null && client.getChannel().isOpen()) {
                client.getChannel().writeAndFlush(message);
            }
        }
    }

    public ChannelFuture writeAndFlush(String address, Message message) {
        if(log.isDebugEnabled()) {
            log.debug("send:{}, message:{}, ", address, message);
        }
        FrontServerClient frontServerClient = clientMap.get(address);
        if(frontServerClient == null || frontServerClient.getChannel() == null || !frontServerClient.getChannel().isOpen()) {
            return null;
        }

        return frontServerClient.getChannel().writeAndFlush(message);
    }
}
