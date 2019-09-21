package cn.com.yusong.yhdg.agentserver.service.basic;

import cn.com.yusong.yhdg.common.domain.basic.*;
import cn.com.yusong.yhdg.common.entity.pagination.Page;
import cn.com.yusong.yhdg.agentserver.persistence.basic.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PlatformAccountService {

    @Autowired
    PlatformAccountMapper platformAccountMapper;

    public PlatformAccount find(Integer id) {
        return platformAccountMapper.find(id);
    }

    public Page findPage(PlatformAccount search) {
        Page page = search.buildPage();
        page.setTotalItems(platformAccountMapper.findPageCount(search));
        search.setBeginIndex(page.getOffset());
        page.setResult(platformAccountMapper.findPageResult(search));
        return page;
    }

    public int updateAccount(int id, String mpAccountName, String mpOpenId, String alipayAccountName, String alipayAccount) {
        return platformAccountMapper.updateAccount(id, mpAccountName, mpOpenId, alipayAccountName, alipayAccount);
    }
}
