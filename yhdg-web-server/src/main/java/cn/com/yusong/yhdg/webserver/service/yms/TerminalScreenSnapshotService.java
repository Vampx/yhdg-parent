package cn.com.yusong.yhdg.webserver.service.yms;

import cn.com.yusong.yhdg.common.domain.yms.TerminalScreenSnapshot;
import cn.com.yusong.yhdg.common.entity.pagination.Page;
import cn.com.yusong.yhdg.webserver.persistence.yms.TerminalScreenSnapshotMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TerminalScreenSnapshotService {
    @Autowired
    TerminalScreenSnapshotMapper terminalScreenSnapshotMapper;

    public Page findPage(TerminalScreenSnapshot search) {
        Page page = search.buildPage();
        page.setTotalItems(terminalScreenSnapshotMapper.findPageCount(search));
        search.setBeginIndex(page.getOffset());
        List<TerminalScreenSnapshot> list = terminalScreenSnapshotMapper.findPageResult(search);
        page.setResult(list);
        return page;
    }
}
