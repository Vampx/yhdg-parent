package cn.com.yusong.yhdg.webserver.service.hdg;

import cn.com.yusong.yhdg.common.domain.hdg.CabinetCode;
import cn.com.yusong.yhdg.common.entity.json.ExtResult;
import cn.com.yusong.yhdg.common.entity.pagination.Page;
import cn.com.yusong.yhdg.webserver.persistence.hdg.CabinetCodeMapper;
import cn.com.yusong.yhdg.webserver.persistence.hdg.CabinetMapper;
import org.springframework.aop.framework.AopContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CabinetCodeService {

    @Autowired
    CabinetCodeMapper cabinetCodeMapper;
    @Autowired
    CabinetMapper cabinetMapper;

    public Page findPage(CabinetCode search) {
        Page page = search.buildPage();
        page.setTotalItems(cabinetCodeMapper.findPageCount(search));
        search.setBeginIndex(page.getOffset());
        page.setResult(cabinetCodeMapper.findPageResult(search));
        return page;
    }

    public CabinetCode find(String id) {
        return cabinetCodeMapper.find(id);
    }

    public ExtResult swap(String from, String to) {
        CabinetCode a = cabinetCodeMapper.findByCode(from);
        if(a == null) {
            return ExtResult.failResult("换电柜编号[" + from + "]不存在");
        }

        CabinetCode b = cabinetCodeMapper.findByCode(to);
        if(b == null) {
            return ExtResult.failResult("换电柜编号[" + to + "]不存在");
        }
        self().swapCode(a, b);
        return ExtResult.successResult();
    }

    @Transactional(rollbackFor=Throwable.class)
    public void swapCode(CabinetCode a, CabinetCode b) {
        String temp = "@@@@@@";
        synchronized (this) {
            cabinetCodeMapper.update(a.getId(), temp);
            cabinetCodeMapper.update(b.getId(), a.getCode());
            cabinetCodeMapper.update(a.getId(), b.getCode());
            cabinetMapper.updateMac(a.getCode(), b.getId());
            cabinetMapper.updateMac(b.getCode(), a.getId());
        }
    }

    private CabinetCodeService self() {
        return (CabinetCodeService) AopContext.currentProxy();
    }
}
