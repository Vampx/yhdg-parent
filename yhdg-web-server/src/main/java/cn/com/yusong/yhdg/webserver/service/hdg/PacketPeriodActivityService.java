package cn.com.yusong.yhdg.webserver.service.hdg;

import cn.com.yusong.yhdg.common.constant.ConstEnum;
import cn.com.yusong.yhdg.common.domain.hdg.PacketPeriodActivity;
import cn.com.yusong.yhdg.common.entity.json.DataResult;
import cn.com.yusong.yhdg.common.entity.json.ExtResult;
import cn.com.yusong.yhdg.common.entity.pagination.Page;
import cn.com.yusong.yhdg.webserver.persistence.hdg.ActivityCustomerMapper;
import cn.com.yusong.yhdg.webserver.persistence.hdg.PacketPeriodActivityMapper;
import cn.com.yusong.yhdg.webserver.service.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class PacketPeriodActivityService extends AbstractService {

    @Autowired
    PacketPeriodActivityMapper packetPeriodActivityMapper;
    @Autowired
    ActivityCustomerMapper activityCustomerMapper;

    public PacketPeriodActivity find(long id) {
        return packetPeriodActivityMapper.find(id);
    }

    public Page findPage(PacketPeriodActivity search) {
        Page page = search.buildPage();
        page.setTotalItems(packetPeriodActivityMapper.findPageCount(search));
        search.setBeginIndex(page.getOffset());
        List<PacketPeriodActivity> list = packetPeriodActivityMapper.findPageResult(search);
        Date now = new Date();
        for (PacketPeriodActivity activity: list) {
            activity.setAgentName(findAgentInfo(activity.getAgentId()).getAgentName());
            if(now.getTime() < activity.getBeginTime().getTime()) {
                activity.setStatus(PacketPeriodActivity.Status.NOT_START.getValue());
            } else if(activity.getBeginTime().getTime() < now.getTime() && now.getTime() < activity.getEndTime().getTime()) {
                activity.setStatus(PacketPeriodActivity.Status.EXECUTING.getValue());
            } else if(activity.getEndTime().getTime() < now.getTime()) {
                activity.setStatus(PacketPeriodActivity.Status.OVER.getValue());
            }
        }
        page.setResult(list);
        return page;
    }


    @Transactional(rollbackFor=Throwable.class)
    public ExtResult create(PacketPeriodActivity activity) {
        activity.setCreateTime(new Date());
        int result = packetPeriodActivityMapper.insert(activity);
        if (result > 0) {
            return DataResult.successResult();
        }
        return DataResult.failResult("对不起! 保存失败", null);
    }

    public ExtResult update(PacketPeriodActivity activity) {
        packetPeriodActivityMapper.update(activity);
        return ExtResult.successResult();
    }

    @Transactional(rollbackFor=Throwable.class)
    public ExtResult delete(long id) {
        activityCustomerMapper.deleteByActivityId(id);
        packetPeriodActivityMapper.delete(id);
        return ExtResult.successResult();
    }
}
