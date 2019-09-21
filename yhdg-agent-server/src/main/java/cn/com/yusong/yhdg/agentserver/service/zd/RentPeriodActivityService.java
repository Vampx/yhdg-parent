package cn.com.yusong.yhdg.agentserver.service.zd;

import cn.com.yusong.yhdg.agentserver.persistence.zd.RentActivityCustomerMapper;
import cn.com.yusong.yhdg.agentserver.persistence.zd.RentPeriodActivityMapper;
import cn.com.yusong.yhdg.agentserver.service.AbstractService;
import cn.com.yusong.yhdg.common.domain.zd.RentPeriodActivity;
import cn.com.yusong.yhdg.common.entity.json.DataResult;
import cn.com.yusong.yhdg.common.entity.json.ExtResult;
import cn.com.yusong.yhdg.common.entity.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class RentPeriodActivityService extends AbstractService {
    @Autowired
    private RentPeriodActivityMapper rentPeriodActivityMapper;
    @Autowired
    private RentActivityCustomerMapper rentActivityCustomerMapper;

    public RentPeriodActivity find(long id) {
        return rentPeriodActivityMapper.find(id);
    }

    public Page findPage(RentPeriodActivity search) {
        Page page = search.buildPage();
        page.setTotalItems(rentPeriodActivityMapper.findPageCount(search));
        search.setBeginIndex(page.getOffset());
        List<RentPeriodActivity> list = rentPeriodActivityMapper.findPageResult(search);
        Date now = new Date();
        for (RentPeriodActivity activity: list) {
            activity.setAgentName(findAgentInfo(activity.getAgentId()).getAgentName());
            if(now.getTime() < activity.getBeginTime().getTime()) {
                activity.setStatus(RentPeriodActivity.Status.NOT_START.getValue());
            } else if(activity.getBeginTime().getTime() < now.getTime() && now.getTime() < activity.getEndTime().getTime()) {
                activity.setStatus(RentPeriodActivity.Status.EXECUTING.getValue());
            } else if(activity.getEndTime().getTime() < now.getTime()) {
                activity.setStatus(RentPeriodActivity.Status.OVER.getValue());
            }
        }
        page.setResult(list);
        return page;
    }


    @Transactional(rollbackFor=Throwable.class)
    public ExtResult create(RentPeriodActivity activity) {
        activity.setCreateTime(new Date());
        int result = rentPeriodActivityMapper.insert(activity);
        if (result > 0) {
            return DataResult.successResult();
        }
        return DataResult.failResult("对不起! 保存失败", null);
    }

    public ExtResult update(RentPeriodActivity activity) {
        rentPeriodActivityMapper.update(activity);
        return ExtResult.successResult();
    }

    @Transactional(rollbackFor=Throwable.class)
    public ExtResult delete(long id) {
        rentActivityCustomerMapper.deleteByActivity(id);
        rentPeriodActivityMapper.delete(id);
        return ExtResult.successResult();
    }
}
