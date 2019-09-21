package cn.com.yusong.yhdg.agentserver.service.hdg;

import cn.com.yusong.yhdg.common.constant.ConstEnum;
import cn.com.yusong.yhdg.common.domain.hdg.BatteryTypeIncomeRatio;
import cn.com.yusong.yhdg.common.entity.json.DataResult;
import cn.com.yusong.yhdg.common.entity.json.ExtResult;
import cn.com.yusong.yhdg.common.entity.pagination.Page;
import cn.com.yusong.yhdg.agentserver.persistence.hdg.*;
import cn.com.yusong.yhdg.agentserver.service.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class BatteryTypeIncomeRatioService extends AbstractService {

    @Autowired
    BatteryTypeIncomeRatioMapper batteryTypeIncomeRatioMapper;

    public BatteryTypeIncomeRatio find(long id) {
        return batteryTypeIncomeRatioMapper.find(id);
    }

    public BatteryTypeIncomeRatio findByBatteryType(Integer batteryType, Integer agentId) {
        return batteryTypeIncomeRatioMapper.findByBatteryType(batteryType, agentId);
    }

    public Page findPage(BatteryTypeIncomeRatio search) {
        Page page = search.buildPage();
        page.setTotalItems(batteryTypeIncomeRatioMapper.findPageCount(search));
        search.setBeginIndex(page.getOffset());
        List<BatteryTypeIncomeRatio> list = batteryTypeIncomeRatioMapper.findPageResult(search);
        for (BatteryTypeIncomeRatio batteryTypeIncomeRatio : list) {
            batteryTypeIncomeRatio.setAgentName(findAgentInfo(batteryTypeIncomeRatio.getAgentId()).getAgentName());
            if (batteryTypeIncomeRatio.getBatteryType() != null) {
                String type = findBatteryType(batteryTypeIncomeRatio.getBatteryType()).getTypeName();
                batteryTypeIncomeRatio.setBatteryTypeName(type);
            }
        }
        page.setResult(list);
        return page;
    }

    @Transactional(rollbackFor=Throwable.class)
    public ExtResult create(BatteryTypeIncomeRatio batteryTypeIncomeRatio) {
        BatteryTypeIncomeRatio dbBatteryTypeIncomeRatio = batteryTypeIncomeRatioMapper.findByBatteryType(batteryTypeIncomeRatio.getBatteryType(), batteryTypeIncomeRatio.getAgentId());
        if (dbBatteryTypeIncomeRatio != null) {
            return ExtResult.failResult("该电池类型和运营商关联的收入分配信息已存在");
        }
        batteryTypeIncomeRatio.setIsReview(ConstEnum.Flag.FALSE.getValue());
        batteryTypeIncomeRatio.setCreateTime(new Date());
        int result = batteryTypeIncomeRatioMapper.insert(batteryTypeIncomeRatio);
        if (result > 0) {
            return DataResult.successResult();
        }
        return DataResult.failResult("对不起! 保存失败", null);
    }

    public ExtResult update(BatteryTypeIncomeRatio batteryTypeIncomeRatio) {
        batteryTypeIncomeRatio.setIsReview(ConstEnum.Flag.FALSE.getValue());
        batteryTypeIncomeRatioMapper.update(batteryTypeIncomeRatio);
        return ExtResult.successResult();
    }

    @Transactional(rollbackFor=Throwable.class)
    public ExtResult delete(long id) {
        batteryTypeIncomeRatioMapper.delete(id);
        return ExtResult.successResult();
    }
}
