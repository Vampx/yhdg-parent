package cn.com.yusong.yhdg.webserver.service.hdg;

import cn.com.yusong.yhdg.common.domain.hdg.PacketPeriodOrder;
import cn.com.yusong.yhdg.common.entity.pagination.Page;
import cn.com.yusong.yhdg.webserver.persistence.hdg.PacketPeriodOrderMapper;
import cn.com.yusong.yhdg.webserver.service.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
public class PacketPeriodOrderSoonExpireStatsService extends AbstractService {

    @Autowired
    PacketPeriodOrderMapper packetPeriodOrderMapper;



    public List<PacketPeriodOrder> findBySoonExpiresAll(PacketPeriodOrder search,int expireDate) {
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
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
        List<PacketPeriodOrder> list = packetPeriodOrderMapper.findBySoonExpireAll(search);
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
        return list;
    }

}
