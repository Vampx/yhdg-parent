package cn.com.yusong.yhdg.agentserver.service.basic;

import cn.com.yusong.yhdg.common.domain.basic.PartnerInOutCash;
import cn.com.yusong.yhdg.common.entity.pagination.Page;
import cn.com.yusong.yhdg.agentserver.persistence.basic.PartnerInOutCashMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PartnerInOutCashService {

    @Autowired
    PartnerInOutCashMapper partnerInOutCashMapper;

    public Page findPage(PartnerInOutCash search) {
        Page page = search.buildPage();
        page.setTotalItems(partnerInOutCashMapper.findPageCount(search));
        search.setBeginIndex(page.getOffset());
        page.setResult(partnerInOutCashMapper.findPageResult(search));
        return page;
    }

}
