package cn.com.yusong.yhdg.frontserver.service.yms;


import cn.com.yusong.yhdg.common.domain.yms.BigContent;
import cn.com.yusong.yhdg.common.domain.yms.TerminalStrategy;
import cn.com.yusong.yhdg.common.utils.AppUtils;
import cn.com.yusong.yhdg.frontserver.entity.Strategy;
import cn.com.yusong.yhdg.frontserver.entity.StrategyXml;
import cn.com.yusong.yhdg.frontserver.persistence.yms.BigContentMapper;
import cn.com.yusong.yhdg.frontserver.persistence.yms.TerminalMapper;
import cn.com.yusong.yhdg.frontserver.persistence.yms.TerminalStrategyMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;


@Service("TerminalStrategyService")
public class TerminalStrategyService {

    @Autowired
    TerminalStrategyMapper terminalStrategyMapper;
    @Autowired
    BigContentMapper bigContentMapper;
    @Autowired
    TerminalMapper terminalMapper;

    public TerminalStrategy find(long id) {
        return terminalStrategyMapper.find(id);
    }

    public StrategyXml findStrategyXml(long strategyId) throws IOException {
        TerminalStrategy terminalStrategy = terminalStrategyMapper.find(strategyId);
        if(terminalStrategy == null) {
            return new StrategyXml(null, null);
        }

        String content = bigContentMapper.find(BigContent.Type.TERMINAL_STRATEGY.getValue(), strategyId);
        String json = "";
        if(content != null) {
            Strategy strategy = (Strategy) AppUtils.decodeJson(content, Strategy.class);
            strategy.setUid(String.format("strategy-%d-%d", terminalStrategy.getId(), terminalStrategy.getVersion()));
            json = AppUtils.encodeJson(strategy);
        }
        return new StrategyXml(AppUtils.getUid(strategyId, terminalStrategy.getVersion()), json);
    }

}
