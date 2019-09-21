package cn.com.yusong.yhdg.webserver.service.hdg;

import cn.com.yusong.yhdg.common.domain.hdg.CabinetOperateLog;
import cn.com.yusong.yhdg.common.entity.pagination.Page;
import cn.com.yusong.yhdg.webserver.persistence.hdg.CabinetOperateLogMapper;
import cn.com.yusong.yhdg.webserver.service.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CabinetOperateLogService extends AbstractService {
    @Autowired
    CabinetOperateLogMapper cabinetOperateLogMapper;

    public CabinetOperateLog find(long id) {
        return cabinetOperateLogMapper.find(id);
    }

    public Page findPage(CabinetOperateLog search) {
        Page page = search.buildPage();
        page.setTotalItems(cabinetOperateLogMapper.findPageCount(search));
        search.setBeginIndex(page.getOffset());
        List<CabinetOperateLog> cabinetOperateLogList = cabinetOperateLogMapper.findPageResult(search);
        for (CabinetOperateLog cabinetOperateLog: cabinetOperateLogList) {
            cabinetOperateLog.setAgentName(findAgentInfo(cabinetOperateLog.getAgentId()).getAgentName());
        }
        page.setResult(cabinetOperateLogList);
        return page;
    }

    public void insert(CabinetOperateLog log) {
        cabinetOperateLogMapper.insert(log);
    }
}
