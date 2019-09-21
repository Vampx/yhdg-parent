package cn.com.yusong.yhdg.webserver.service.basic;

import cn.com.yusong.yhdg.common.domain.basic.IdCardAuthRecord;
import cn.com.yusong.yhdg.common.entity.pagination.Page;
import cn.com.yusong.yhdg.webserver.persistence.basic.IdCardAuthRecordMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class IdCardAuthRecordService {
    @Autowired
    IdCardAuthRecordMapper idCardAuthRecordMapper;

    public Page findPage(IdCardAuthRecord search) {
        Page page = search.buildPage();
        page.setTotalItems(idCardAuthRecordMapper.findPageCount(search));
        search.setBeginIndex(page.getOffset());
        page.setResult(idCardAuthRecordMapper.findPageResult(search));
        return page;
    }
}
