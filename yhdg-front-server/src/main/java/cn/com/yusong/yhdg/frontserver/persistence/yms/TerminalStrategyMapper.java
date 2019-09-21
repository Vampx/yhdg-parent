package cn.com.yusong.yhdg.frontserver.persistence.yms;


import cn.com.yusong.yhdg.common.domain.yms.TerminalStrategy;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.springframework.stereotype.Component;

@Component("TerminalStrategyMapper")
public interface TerminalStrategyMapper extends MasterMapper {
    TerminalStrategy find(long id);

}
