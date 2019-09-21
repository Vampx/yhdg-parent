package cn.com.yusong.yhdg.agentserver.service.basic;

import cn.com.yusong.yhdg.agentserver.persistence.basic.PhoneappMapper;
import cn.com.yusong.yhdg.agentserver.service.AbstractService;
import cn.com.yusong.yhdg.common.domain.basic.Phoneapp;
import cn.com.yusong.yhdg.common.entity.json.ExtResult;
import cn.com.yusong.yhdg.common.entity.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class PhoneappService extends AbstractService {
    @Autowired
    private PhoneappMapper phoneappMapper;

    public Phoneapp find(Integer id){
        Phoneapp phoneapp = phoneappMapper.find(id);
        return phoneapp;
    }

    public Page findPage(Phoneapp search) {
        Page page = search.buildPage();
        page.setTotalItems(phoneappMapper.findPageCount(search));
        search.setBeginIndex(page.getOffset());
        List<Phoneapp> phoneappList = phoneappMapper.findPageResult(search);
        page.setResult(phoneappList);

        return page;
    }

    public ExtResult insert(Phoneapp phoneapp){
        phoneapp.setCreateTime(new Date());
        phoneappMapper.insert(phoneapp);

        return ExtResult.successResult();
    }

    public ExtResult update(Phoneapp phoneapp) {
        phoneappMapper.update(phoneapp);

        return ExtResult.successResult();
    }

    @Transactional(rollbackFor=Throwable.class)
    public ExtResult delete(int id) {
        phoneappMapper.delete(id);

        return ExtResult.successResult();
    }

}
