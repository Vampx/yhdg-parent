package cn.com.yusong.yhdg.webserver.service.zd;

import cn.com.yusong.yhdg.common.constant.Constant;
import cn.com.yusong.yhdg.common.domain.basic.AgentInfo;
import cn.com.yusong.yhdg.common.domain.basic.SystemBatteryType;
import cn.com.yusong.yhdg.common.domain.zd.RentPeriodOrder;
import cn.com.yusong.yhdg.common.entity.json.ExtResult;
import cn.com.yusong.yhdg.common.entity.pagination.Page;
import cn.com.yusong.yhdg.webserver.persistence.zd.RentPeriodOrderMapper;
import cn.com.yusong.yhdg.webserver.service.AbstractService;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class RentPeriodOrderService extends AbstractService {
    @Autowired
    private RentPeriodOrderMapper rentPeriodOrderMapper;

    public Page findPage(RentPeriodOrder search) {
        Page page = search.buildPage();
        page.setTotalItems(rentPeriodOrderMapper.findPageCount(search));
        search.setBeginIndex(page.getOffset());
        List<RentPeriodOrder> list = rentPeriodOrderMapper.findPageResult(search);
        for (RentPeriodOrder rentPeriodOrder: list) {
            Integer agentId = rentPeriodOrder.getAgentId();
            //设置运营商名称
            if (agentId != null) {
                rentPeriodOrder.setAgentName(findAgentInfo(rentPeriodOrder.getAgentId()).getAgentName());
            }
            //设置电池类型名称
            if (rentPeriodOrder.getBatteryType() != null) {
                rentPeriodOrder.setBatteryTypeName(findBatteryType(rentPeriodOrder.getBatteryType()).getTypeName());
            }
        }
        page.setResult(list);
        return page;
    }

    public Page findPageForbalance(RentPeriodOrder search) {
        Page page = search.buildPage();
        page.setTotalItems(rentPeriodOrderMapper.findPageForBalanceCount(search));
        search.setBeginIndex(page.getOffset());
        List<RentPeriodOrder> list = rentPeriodOrderMapper.findPageForBalanceResult(search);
        for (RentPeriodOrder rentPeriodOrder : list) {
            if (rentPeriodOrder.getAgentId() != null) {
                AgentInfo agentInfo = findAgentInfo(rentPeriodOrder.getAgentId());
                if (agentInfo != null) {
                    rentPeriodOrder.setAgentName(agentInfo.getAgentName());
                }
            }
            if (rentPeriodOrder.getBatteryType() != null) {
                SystemBatteryType batteryType = findBatteryType(rentPeriodOrder.getBatteryType());
                if (batteryType != null) {
                    rentPeriodOrder.setBatteryTypeName(batteryType.getTypeName());
                }
            }
        }
        page.setResult(list);
        return page;
    }

    public RentPeriodOrder find(String id) {
        return rentPeriodOrderMapper.find(id);
    }

    @Transactional(rollbackFor = Throwable.class)
    public ExtResult extendRent(RentPeriodOrder entity, String operator) {
        RentPeriodOrder dbRentPeriodOrder = rentPeriodOrderMapper.find(entity.getId());
        //增加天数
        dbRentPeriodOrder.setDayCount(dbRentPeriodOrder.getDayCount() + entity.getDayCount());
        //延长过期时间
        Date newEndTime = DateUtils.addDays(dbRentPeriodOrder.getEndTime(), entity.getDayCount());
        dbRentPeriodOrder.setEndTime(newEndTime);
        //更新状态
        if (newEndTime.compareTo(new Date()) > 0) {
            if (dbRentPeriodOrder.getStatus() == RentPeriodOrder.Status.EXPIRED.getValue()) {
                dbRentPeriodOrder.setStatus(RentPeriodOrder.Status.USED.getValue());
            }
        }

        Date now = new Date();
        String nowDate = DateFormatUtils.format(now, Constant.DATE_TIME_FORMAT);
        String operatorMemo = "";
        if (StringUtils.isEmpty(dbRentPeriodOrder.getOperatorMemo())) {
            operatorMemo = "" + operator + "在" + nowDate + "延长租期" + entity.getDayCount() + "天;" + "\n";
        } else {
            operatorMemo = dbRentPeriodOrder.getOperatorMemo() + operator + "在" + nowDate + "延长租期" + entity.getDayCount() + "天;" +"\n";
        }

        int result = rentPeriodOrderMapper.extendRent(dbRentPeriodOrder.getId(), dbRentPeriodOrder.getDayCount(), dbRentPeriodOrder.getEndTime(), dbRentPeriodOrder.getStatus(), operatorMemo);
        if (result == 0) {
            return ExtResult.failResult("操作失败");
        }
        return ExtResult.successResult();
    }

    public List<RentPeriodOrder> findCanRefundByCustomerId(Long customerId) {
        return rentPeriodOrderMapper.findCanRefundByCustomerId(customerId);
    }
}
