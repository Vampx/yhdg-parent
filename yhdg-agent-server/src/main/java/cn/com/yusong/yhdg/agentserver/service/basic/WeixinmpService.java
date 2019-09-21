package cn.com.yusong.yhdg.agentserver.service.basic;

import cn.com.yusong.yhdg.common.domain.basic.Weixinmp;
import cn.com.yusong.yhdg.common.entity.json.ExtResult;
import cn.com.yusong.yhdg.common.entity.pagination.Page;
import cn.com.yusong.yhdg.agentserver.persistence.basic.WeixinmpMapper;
import cn.com.yusong.yhdg.agentserver.service.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class WeixinmpService extends AbstractService {

    @Autowired
    private WeixinmpMapper weixinmpMapper;

    public Weixinmp find(Integer id){
        Weixinmp weixinmp = weixinmpMapper.find(id);
        return weixinmp;
    }

    public Page findPage(Weixinmp search) {
        Page page = search.buildPage();
        page.setTotalItems(weixinmpMapper.findPageCount(search));
        search.setBeginIndex(page.getOffset());
        List<Weixinmp> weixinmpList = weixinmpMapper.findPageResult(search);
        page.setResult(weixinmpList);

        return page;
    }

    public List<Weixinmp> findList(Weixinmp search) {
        return weixinmpMapper.findList(search);
    }

    @Transactional(rollbackFor = Throwable.class)
    public ExtResult insert(Weixinmp weixinmp, List<String> sqlList){
        weixinmp.setCreateTime(new Date());
        weixinmpMapper.insert(weixinmp);

        String line = String.format("%d", weixinmp.getId());
        for (String sql : sqlList) {
            weixinmpMapper.insertSql(sql.replace("0/*weixinmp_id*/", line));
        }

        return ExtResult.successResult();
    }

    public ExtResult update(Weixinmp weixinmp) {
        weixinmpMapper.update(weixinmp);

        return ExtResult.successResult();
    }

    @Transactional(rollbackFor=Throwable.class)
    public ExtResult delete(int id) {
        weixinmpMapper.delete(id);

        return ExtResult.successResult();
    }
}
