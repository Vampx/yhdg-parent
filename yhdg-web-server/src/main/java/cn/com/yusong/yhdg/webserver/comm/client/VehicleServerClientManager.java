package cn.com.yusong.yhdg.webserver.comm.client;

import cn.com.yusong.yhdg.common.constant.ConstEnum;
import cn.com.yusong.yhdg.common.constant.Constant;
import cn.com.yusong.yhdg.common.protocol.msg_tbit.TextMessage;
import cn.com.yusong.yhdg.common.tool.netty.Message;
import cn.com.yusong.yhdg.common.tool.zk.Listener;
import cn.com.yusong.yhdg.common.utils.YhdgUtils;
import cn.com.yusong.yhdg.webserver.config.AppConfig;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class VehicleServerClientManager implements Listener {
    static Logger log = LogManager.getLogger(VehicleServerClientManager.class);

    final Map<String, VehicleServerClient> clientMap = new ConcurrentHashMap<String, VehicleServerClient>();
    AppConfig config;

    public VehicleServerClientManager(AppConfig config) {
        this.config = config;
    }

    public ChannelFuture writeAndFlush(String address, TextMessage message) {
        VehicleServerClient VehicleServerClient = clientMap.get(address);
        if(VehicleServerClient == null || VehicleServerClient.getChannel() == null || !VehicleServerClient.getChannel().isOpen()) {
            return null;
        }
        log.debug("begin sent to{}, {}", VehicleServerClient.getChannel().remoteAddress(), message.encode());
        return VehicleServerClient.getChannel().writeAndFlush(message);
    }

    public void writeAndFlush(Message message) {
        for(VehicleServerClient client : clientMap.values()) {
            if(client.getChannel() != null && client.getChannel().isOpen()) {
                client.getChannel().writeAndFlush(message);
            }
        }
    }

    public ChannelFuture write(String address, Message message) {
        VehicleServerClient VehicleServerClient = clientMap.get(address);
        if(VehicleServerClient == null || VehicleServerClient.getChannel() == null || !VehicleServerClient.getChannel().isOpen()) {
            return null;
        }

        return VehicleServerClient.getChannel().write(message);
    }

    @Override
    public void listen(Param param) throws Exception {
        List<String> children = (List<String>) param.result;
        Set<String> servers = new HashSet<String>(10);

        for(String child : children) {
            byte[] data = config.zkClient.get(ConstEnum.Node.NODE_VEHICLE_SERVER.getValue() + "/" + child);
            String source = new String(data, Constant.CHARSET_UTF_8);
            Map<String, String> map = YhdgUtils.stringToMap(source, ",");

            if(log.isDebugEnabled()) {
                log.debug("child: {}", source);
            }

            String host = getInnerIp(map.get("server.ip"));
            int port = Integer.parseInt(map.get("server.port"));
            String key = host + ":" + port;
            String threadName = "VehicleServerClient/" + key;
            servers.add(key);

            if(!clientMap.containsKey(key)) {
                VehicleServerClient client = new VehicleServerClient(host, port, threadName, config.vehicleBizFactory, config.executorService);
                client.startup();

                clientMap.put(key, client);
            }
        }

        for(String key : clientMap.keySet()) {
            if(!servers.contains(key)) {
                VehicleServerClient client = clientMap.remove(key);
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
