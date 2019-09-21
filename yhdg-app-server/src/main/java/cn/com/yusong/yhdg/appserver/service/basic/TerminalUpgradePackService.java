package cn.com.yusong.yhdg.appserver.service.basic;

import cn.com.yusong.yhdg.appserver.persistence.basic.TerminalUpgradePackMapper;
import cn.com.yusong.yhdg.common.domain.basic.TerminalUpgradePack;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by chen on 2017/5/17.
 */
@Service
public class TerminalUpgradePackService {
    @Autowired
    TerminalUpgradePackMapper terminalUpgradePackMapper;

    public TerminalUpgradePack find(int id) {
        return terminalUpgradePackMapper.find(id);
    }

    public List<TerminalUpgradePack> findByOldVersion(int packType, String oldVersion) {
        return terminalUpgradePackMapper.findByOldVersion(packType, oldVersion);
    }
}
