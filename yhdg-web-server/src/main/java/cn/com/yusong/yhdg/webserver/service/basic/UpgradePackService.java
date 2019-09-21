package cn.com.yusong.yhdg.webserver.service.basic;

import cn.com.yusong.yhdg.common.constant.Constant;
import cn.com.yusong.yhdg.common.domain.basic.UpgradePack;
import cn.com.yusong.yhdg.common.entity.json.ExtResult;
import cn.com.yusong.yhdg.common.entity.pagination.Page;
import cn.com.yusong.yhdg.common.utils.YhdgUtils;
import cn.com.yusong.yhdg.webserver.config.AppConfig;
import cn.com.yusong.yhdg.webserver.persistence.basic.UpgradePackMapper;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.IOException;
import java.util.Date;

/**
 * Created by chen on 2017/10/31.
 */
@Service
public class UpgradePackService {
    @Autowired
    UpgradePackMapper upgradePackMapper;
    @Autowired
    AppConfig appConfig;

    public UpgradePack find(long id) {
        return upgradePackMapper.find(id);
    }

    public Page findPage(UpgradePack search) {
        Page page = search.buildPage();
        page.setTotalItems(upgradePackMapper.findPageCount(search));
        search.setBeginIndex(page.getOffset());
        page.setResult(upgradePackMapper.findPageResult(search));
        return page;
    }

    public Page findScreenPage(UpgradePack search) {
        Page page = search.buildPage();
        page.setTotalItems(upgradePackMapper.findScreenPageCount(search));
        search.setBeginIndex(page.getOffset());
        page.setResult(upgradePackMapper.findScreenPageResult(search));
        return page;
    }

    @Transactional(rollbackFor=Throwable.class)
    public ExtResult update(UpgradePack entity) throws IOException {
        UpgradePack upgradePack = upgradePackMapper.find(entity.getId());
        if(upgradePack == null) {
            return ExtResult.failResult("记录不存在");
        }

        File descFile = appConfig.getFile(upgradePack.getDescFile());
        YhdgUtils.makeParentDir(descFile);
        FileUtils.writeStringToFile(descFile, entity.getVersion(), Constant.ENCODING_UTF_8);
        entity.setUpdateTime(new Date());
        upgradePackMapper.update(entity);

        return ExtResult.successResult();
    }


}
