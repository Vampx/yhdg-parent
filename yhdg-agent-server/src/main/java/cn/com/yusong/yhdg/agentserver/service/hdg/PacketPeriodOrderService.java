package cn.com.yusong.yhdg.agentserver.service.hdg;

import cn.com.yusong.yhdg.agentserver.persistence.basic.SystemBatteryTypeMapper;
import cn.com.yusong.yhdg.agentserver.persistence.hdg.PacketPeriodOrderMapper;
import cn.com.yusong.yhdg.agentserver.service.AbstractService;
import cn.com.yusong.yhdg.common.domain.basic.AgentInfo;
import cn.com.yusong.yhdg.common.domain.basic.SystemBatteryType;
import cn.com.yusong.yhdg.common.domain.hdg.PacketPeriodOrder;
import cn.com.yusong.yhdg.common.entity.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
public class PacketPeriodOrderService extends AbstractService{
    @Autowired
    PacketPeriodOrderMapper packetPeriodOrderMapper;
    @Autowired
    SystemBatteryTypeMapper systemBatteryTypeMapper;

    public Page findPage(PacketPeriodOrder search) {
        Page page = search.buildPage();
        page.setTotalItems(packetPeriodOrderMapper.findPageCount(search));
        search.setBeginIndex(page.getOffset());
        List<PacketPeriodOrder> list = packetPeriodOrderMapper.findPageResult(search);
        for (PacketPeriodOrder packetPeriodOrder: list) {
            Integer agentId = packetPeriodOrder.getAgentId();
            //设置运营商名称
            if (agentId != null) {
                packetPeriodOrder.setAgentName(findAgentInfo(packetPeriodOrder.getAgentId()).getAgentName());
            }
            //设置电池类型名称
            if (packetPeriodOrder.getBatteryType() != null) {
                packetPeriodOrder.setBatteryTypeName(findBatteryType(packetPeriodOrder.getBatteryType()).getTypeName());
            }
        }
        page.setResult(list);
        return page;
    }

    public Page findPageForbalance(PacketPeriodOrder search) {
        Page page = search.buildPage();
        page.setTotalItems(packetPeriodOrderMapper.findPageForBalanceCount(search));
        search.setBeginIndex(page.getOffset());
        List<PacketPeriodOrder> list = packetPeriodOrderMapper.findPageForBalanceResult(search);
        for (PacketPeriodOrder packetPeriodOrder : list) {
            if (packetPeriodOrder.getAgentId() != null) {
                AgentInfo agentInfo = findAgentInfo(packetPeriodOrder.getAgentId());
                if (agentInfo != null) {
                    packetPeriodOrder.setAgentName(agentInfo.getAgentName());
                }
            }
            if (packetPeriodOrder.getBatteryType() != null) {
                SystemBatteryType batteryType = findBatteryType(packetPeriodOrder.getBatteryType());
                if (batteryType != null) {
                    packetPeriodOrder.setBatteryTypeName(batteryType.getTypeName());
                }
            }
        }
        page.setResult(list);
        return page;
    }

    public PacketPeriodOrder find(String id) {
        return packetPeriodOrderMapper.find(id);
    }

    public List<PacketPeriodOrder> findCanRefundByCustomerId(Long customerId) {
        return packetPeriodOrderMapper.findCanRefundByCustomerId(customerId);
    }

    public Page findBySoonExpiresPage(PacketPeriodOrder search,int expireDate) {
        Page page = search.buildPage();
        Date date=new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        if(PacketPeriodOrder.ExpireDate.ONE_DAY.getValue()==expireDate){
            calendar.add(Calendar.DAY_OF_MONTH, +1);
        }else if(PacketPeriodOrder.ExpireDate.TWO_DAY.getValue()==expireDate){
            calendar.add(Calendar.DAY_OF_MONTH, +2);
        }else if(PacketPeriodOrder.ExpireDate.THREE_DAY.getValue()==expireDate){
            calendar.add(Calendar.DAY_OF_MONTH, +3);
        }
        if(expireDate!=PacketPeriodOrder.ExpireDate.All.getValue()){
            Date ThreeDaysdate = calendar.getTime();
            search.setCurrentTime(date);
            search.setCurrentThreeDaysTime(ThreeDaysdate);
            search.setStatus(PacketPeriodOrder.Status.USED.getValue());
        }else{
            search.setStatus(PacketPeriodOrder.Status.EXPIRED.getValue());
        }
        search.setBeginIndex(page.getOffset());
        page.setTotalItems(packetPeriodOrderMapper.findBySoonExpirePageCount(search));
        List<PacketPeriodOrder> list = packetPeriodOrderMapper.findBySoonExpirePageResult(search);
        for (PacketPeriodOrder packetPeriodOrder: list) {
            Integer agentId = packetPeriodOrder.getAgentId();
            //设置运营商名称
            if (agentId != null) {
                packetPeriodOrder.setAgentName(findAgentInfo(packetPeriodOrder.getAgentId()).getAgentName());
            }
            //设置电池类型名称
            if (packetPeriodOrder.getBatteryType() != null) {
                packetPeriodOrder.setBatteryTypeName(findBatteryType(packetPeriodOrder.getBatteryType()).getTypeName());
            }
        }
        page.setResult(list);
        return page;
    }
}
