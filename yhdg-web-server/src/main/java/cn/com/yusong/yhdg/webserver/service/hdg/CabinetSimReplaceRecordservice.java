package cn.com.yusong.yhdg.webserver.service.hdg;

import cn.com.yusong.yhdg.common.domain.hdg.CabinetSimReplaceRecord;
import cn.com.yusong.yhdg.common.entity.pagination.Page;
import cn.com.yusong.yhdg.webserver.persistence.hdg.CabinetSimReplaceRecordMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CabinetSimReplaceRecordservice {
    @Autowired
    CabinetSimReplaceRecordMapper subcabinetSimReplaceRecordMapper;

    public Page findPage(CabinetSimReplaceRecord search) {
        Page page = search.buildPage();
        page.setTotalItems(subcabinetSimReplaceRecordMapper.findPageCount(search));
        search.setBeginIndex(page.getOffset());
        page.setResult(subcabinetSimReplaceRecordMapper.findPageResult(search));
        return page;
    }
}
