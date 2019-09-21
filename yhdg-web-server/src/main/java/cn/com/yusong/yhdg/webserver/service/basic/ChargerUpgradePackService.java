package cn.com.yusong.yhdg.webserver.service.basic;

import cn.com.yusong.yhdg.common.domain.basic.ChargerUpgradePack;
import cn.com.yusong.yhdg.common.entity.json.ExtResult;
import cn.com.yusong.yhdg.common.entity.pagination.Page;
import cn.com.yusong.yhdg.webserver.config.AppConfig;
import cn.com.yusong.yhdg.webserver.persistence.basic.ChargerUpgradePackMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.Date;

@Service
public class ChargerUpgradePackService {

    @Autowired
    ChargerUpgradePackMapper chargerUpgradePackMapper;

    @Autowired
    AppConfig appConfig;

    public ChargerUpgradePack find(long id){return chargerUpgradePackMapper.find(id);}
    public Page findPage(ChargerUpgradePack search){
        Page page = search.buildPage();
        page.setTotalItems(chargerUpgradePackMapper.findPageCount(search));
        search.setBeginIndex(page.getOffset());
        page.setResult(chargerUpgradePackMapper.findPageResult(search));
        return page;
    }

    public ExtResult insert(ChargerUpgradePack entity) {
        entity.setUpdateTime(new Date());
        chargerUpgradePackMapper.insert(entity);
        return ExtResult.successResult();
    }

    @Transactional(rollbackFor=Throwable.class)
    public ExtResult update(ChargerUpgradePack entity) throws IOException {
        ChargerUpgradePack upgradePack = chargerUpgradePackMapper.find(entity.getId());
        if(upgradePack == null) {
            return ExtResult.failResult("记录不存在");
        }

        entity.setUpdateTime(new Date());
        chargerUpgradePackMapper.update(entity);

        return ExtResult.successResult();
    }

    public ExtResult delete(Long id) {
        if (chargerUpgradePackMapper.delete(id) == 0) {
            return ExtResult.failResult("操作失败");
        }
        return ExtResult.successResult();
    }
}
