package cn.com.yusong.yhdg.serviceserver.persistence.hdg;

import cn.com.yusong.yhdg.common.domain.hdg.AgentDayStats;
import cn.com.yusong.yhdg.common.domain.hdg.Cabinet;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface CabinetMapper extends MasterMapper {
    public Cabinet find(@Param("id") String id);
    Cabinet findByMac(String mac);
    public List<AgentDayStats> findAgentIncrement(@Param("beginTime") Date beginTime, @Param("endTime") Date endTime);
    public int findIncrement(@Param("beginTime") Date beginTime, @Param("endTime") Date endTime);
    public int findTotal();
    public List<String> findAllId();
    public List<Cabinet> findNeedForegiftList(@Param("statsTime") Date statsTime);
    public List<Cabinet> findNeedRentList(@Param("statsTime") Date statsTime);
    public List<Cabinet> findByActiveStatus(@Param("activeStatus") int activeStatus);
    public int updateAllFullCount(@Param("id") String id, @Param("allFullCount") int allFullCount, @Param("allFullFaultLogId") Long allFullFaultLogId);
    public int updateUseVolume(@Param("id") String id, @Param("useVolume") int useVolume);

    public List<Cabinet> findByHeartTime(@Param("heartTime") Date heartTime, @Param("limit") int limit);

    public List<Cabinet> findByOfflineMessageTime(@Param("heartTime") Date heartTime, @Param("limit") int limit);

    public List<Cabinet> findBySubtype(@Param("subtype") Integer subtype, @Param("batteryStatus") Integer batteryStatus, @Param("offset") int offset, @Param("limit") int limit);

    public List<Cabinet> findAll(@Param("offset") int offset, @Param("limit") int limit);

    public List<Cabinet> findAllByAgent(@Param("agentId") int agentId, @Param("offset") int offset, @Param("limit") int limit);

    public List<Cabinet> findByNotAgent(@Param("agentIdList") List<Integer> agentIdList, @Param("offset") int offset, @Param("limit") int limit);

    public int updateOnline(@Param("id") String id, @Param("isOnline") Integer isOnline);

    public int updateRentRecordTime(@Param("id") String id, @Param("rentRecordTime") Date rentRecordTime);

    public int updateOfflineMessageTime(@Param("id") String id, @Param("offlineFaultLogId") long offlineFaultLogId);

}
