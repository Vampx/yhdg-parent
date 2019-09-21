package cn.com.yusong.yhdg.webserver.service.basic;

import cn.com.yusong.yhdg.common.domain.basic.PlatformAccountInOutMoney;
import cn.com.yusong.yhdg.common.entity.pagination.Page;
import cn.com.yusong.yhdg.webserver.persistence.basic.PlatformAccountInOutMoneyMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PlatformAccountInOutMoneyService {

    @Autowired
    PlatformAccountInOutMoneyMapper platformAccountInOutMoneyMapper;

    public PlatformAccountInOutMoney find(Long id) {
        return platformAccountInOutMoneyMapper.find(id);
    }

    public Page findPage(PlatformAccountInOutMoney search) {
        Page page = search.buildPage();
        page.setTotalItems(platformAccountInOutMoneyMapper.findPageCount(search));
        search.setBeginIndex(page.getOffset());
        page.setResult(platformAccountInOutMoneyMapper.findPageResult(search));
        return page;
    }

}
