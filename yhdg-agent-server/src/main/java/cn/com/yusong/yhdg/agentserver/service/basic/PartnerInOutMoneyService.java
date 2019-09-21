package cn.com.yusong.yhdg.agentserver.service.basic;

import cn.com.yusong.yhdg.common.domain.basic.*;
import cn.com.yusong.yhdg.common.entity.pagination.Page;
import cn.com.yusong.yhdg.agentserver.persistence.basic.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PartnerInOutMoneyService {

    @Autowired
    PartnerInOutMoneyMapper partnerInOutMoneyMapper;

    public PartnerInOutMoney find(Long id) {
        return partnerInOutMoneyMapper.find(id);
    }

    public Page findPage(PartnerInOutMoney search) {
        Page page = search.buildPage();
        page.setTotalItems(partnerInOutMoneyMapper.findPageCount(search));
        search.setBeginIndex(page.getOffset());
        page.setResult(partnerInOutMoneyMapper.findPageResult(search));
        return page;
    }

}
