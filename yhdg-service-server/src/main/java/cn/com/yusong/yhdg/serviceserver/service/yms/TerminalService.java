package cn.com.yusong.yhdg.serviceserver.service.yms;

import cn.com.yusong.yhdg.common.constant.ConstEnum;
import cn.com.yusong.yhdg.common.domain.basic.AgentInfo;
import cn.com.yusong.yhdg.common.domain.basic.PushMessage;
import cn.com.yusong.yhdg.common.domain.hdg.Cabinet;
import cn.com.yusong.yhdg.common.domain.hdg.CabinetOnlineStats;
import cn.com.yusong.yhdg.common.domain.hdg.FaultLog;
import cn.com.yusong.yhdg.common.domain.yms.Terminal;
import cn.com.yusong.yhdg.common.entity.AreaCache;
import cn.com.yusong.yhdg.serviceserver.persistence.basic.AgentMapper;
import cn.com.yusong.yhdg.serviceserver.persistence.hdg.*;
import cn.com.yusong.yhdg.serviceserver.persistence.yms.TerminalMapper;
import cn.com.yusong.yhdg.serviceserver.service.AbstractService;
import cn.com.yusong.yhdg.serviceserver.service.basic.SystemConfigService;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
public class TerminalService extends AbstractService {
    static final Logger log = LogManager.getLogger(TerminalService.class);
    @Autowired
    TerminalMapper terminalMapper;

    /**
     * 屏幕心跳10分钟不上报 设置离线
     */
    public void refreshOnline() {
        while (true) {
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.MINUTE, -10);
            List<Terminal> terminalList = terminalMapper.findByHeartTime(calendar.getTime(), 500);
            if (terminalList.isEmpty()) {
                break;
            }
            for (Terminal terminal : terminalList) {
                terminalMapper.updateOnline(terminal.getId(), ConstEnum.Flag.FALSE.getValue());
            }
        }
    }


}
