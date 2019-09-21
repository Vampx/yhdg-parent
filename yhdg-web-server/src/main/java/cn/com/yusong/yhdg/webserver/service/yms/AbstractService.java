package cn.com.yusong.yhdg.webserver.service.yms;

import cn.com.yusong.yhdg.common.constant.CacheKey;
import cn.com.yusong.yhdg.common.domain.basic.AgentInfo;
import cn.com.yusong.yhdg.common.domain.yms.TerminalStrategy;
import cn.com.yusong.yhdg.common.protocol.msg93.Msg931000003;
import cn.com.yusong.yhdg.common.tool.memcached.MemCachedConfig;
import cn.com.yusong.yhdg.webserver.biz.server.ClientBizUtils;
import cn.com.yusong.yhdg.webserver.config.AppConfig;
import cn.com.yusong.yhdg.webserver.persistence.basic.AgentMapper;
import cn.com.yusong.yhdg.webserver.persistence.yms.TerminalMapper;
import cn.com.yusong.yhdg.webserver.persistence.yms.TerminalStrategyMapper;
import cn.com.yusong.yhdg.common.tool.memcached.MemCachedClient;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class AbstractService {
    @Autowired
    AppConfig config;
    @Autowired
    MemCachedClient memCachedClient;
    @Autowired
    TerminalMapper terminalMapper;
    @Autowired
    TerminalStrategyMapper terminalStrategyMapper;
    @Autowired
    AgentMapper agentMapper;


    public TerminalStrategy findTerminalStrategy(long id) {
        String key = CacheKey.key(CacheKey.K_ID_V_TERMINAL_STRATEGY, id);

        TerminalStrategy strategy = (TerminalStrategy) memCachedClient.get(key);
        if(strategy != null) {
            return strategy;
        }

        strategy = terminalStrategyMapper.find(id);
        if(strategy != null) {
            memCachedClient.set(key, strategy, MemCachedConfig.CACHE_ONE_WEEK);
        }

        return strategy;
    }

    public void noticeStrategyUpdate(AppConfig config, long strategyId) {
        List<String> terminalList = terminalMapper.findIdByStrategy(strategyId);
        if(!terminalList.isEmpty()) {
            TerminalStrategy strategy = findTerminalStrategy(strategyId);
            String uid = String.format("strategy-%d-%d", strategy.getId(), strategy.getVersion());
            ClientBizUtils.noticeStrategyChanged(config, uid, terminalList);
        }
    }

    public void noticePlaylistUpdate(AppConfig config, Long playlistId) {
        Msg931000003 msg = new Msg931000003();
        msg.playlistId = playlistId.intValue();
        ClientBizUtils.noticePlaylistUpdate(config, msg);
    }

    public AgentInfo findAgentInfo(int id) {
        String key = CacheKey.key(CacheKey.K_ID_V_AGENT_INFO, id);
        AgentInfo agentInfo = (AgentInfo) memCachedClient.get(key);
        if(agentInfo != null) {
            return agentInfo;
        }
        agentInfo =  agentMapper.findAgentInfo(id);
        if(agentInfo != null) {
            memCachedClient.set(key, agentInfo, MemCachedConfig.CACHE_ONE_WEEK);
        }
        return agentInfo;
    }
}
