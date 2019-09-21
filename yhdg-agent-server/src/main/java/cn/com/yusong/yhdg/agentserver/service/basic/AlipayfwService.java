package cn.com.yusong.yhdg.agentserver.service.basic;

import cn.com.yusong.yhdg.common.domain.basic.Alipayfw;
import cn.com.yusong.yhdg.common.entity.json.ExtResult;
import cn.com.yusong.yhdg.common.entity.pagination.Page;
import cn.com.yusong.yhdg.agentserver.persistence.basic.AlipayfwMapper;
import cn.com.yusong.yhdg.agentserver.service.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class AlipayfwService extends AbstractService {

    @Autowired
    private AlipayfwMapper alipayfwMapper;

    public Alipayfw find(Integer id){
        Alipayfw alipayfw = alipayfwMapper.find(id);
        return alipayfw;
    }

    public Page findPage(Alipayfw search) {
        Page page = search.buildPage();
        page.setTotalItems(alipayfwMapper.findPageCount(search));
        search.setBeginIndex(page.getOffset());
        List<Alipayfw> alipayfwList = alipayfwMapper.findPageResult(search);
        page.setResult(alipayfwList);

        return page;
    }

    public List<Alipayfw> findList(Alipayfw search) {
        return alipayfwMapper.findList(search);
    }

    @Transactional(rollbackFor = Throwable.class)
    public ExtResult insert(Alipayfw alipayfw, List<String> sqlList) {
        alipayfw.setCreateTime(new Date());
        alipayfwMapper.insert(alipayfw);

        String line = String.format("%d", alipayfw.getId());
        for (String sql : sqlList) {
            alipayfwMapper.insertSql(sql.replace("0/*alipayfw_id*/", line));
        }

        return ExtResult.successResult();
    }

    public ExtResult update(Alipayfw alipayfw) {
        alipayfwMapper.update(alipayfw);

        return ExtResult.successResult();
    }

    @Transactional(rollbackFor=Throwable.class)
    public ExtResult delete(int id) {
        alipayfwMapper.delete(id);

        return ExtResult.successResult();
    }

}
