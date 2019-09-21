package cn.com.yusong.yhdg.serviceserver.persistence.basic;

import cn.com.yusong.yhdg.common.domain.basic.Customer;
import cn.com.yusong.yhdg.common.domain.basic.CustomerInfo;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface CustomerMapper extends MasterMapper {
    public List<Customer> findAll(@Param("offset") int offset, @Param("limit") int limit);

    public List<Customer> findAllByAgent(@Param("agentId") int agentId, @Param("offset") int offset, @Param("limit") int limit);

    public List<Customer> findByWagesDay(@Param("wagesDay") String wagesDay, @Param("offset") int offset, @Param("limit") int limit);

    public List<Customer> findByNotAgent(@Param("agentIdList") List<Integer> agentIdList, @Param("offset") int offset, @Param("limit") int limit);

    public Customer find(@Param("id") Long id);

    public Customer findByMobile(@Param("partnerId") int partnerId, @Param("mobile") String mobile);

    public Customer findPush(@Param("id") Long id);

    public CustomerInfo findInfo(int id);

    public Customer findByBatteryId(@Param("batteryId") String batteryId);

    public int findBalance(int id);

    public int findTotal();

    public int findIncrement(@Param("beginTime") Date beginTime, @Param("endTime") Date endTime);

    public int findCountByCreateTime(@Param("type") String type, @Param("beginTime") Date beginTime, @Param("endTime") Date endTime);

    public int findCountByToday();

    public int findCountByTodayNotAgent(@Param("agentIdList")List<Integer> agentIdList);

    public int findCountByTodayAndAgent(@Param("agentId")int agentId);

    public int findNotChargeCount(@Param("statsDate") String statsDate);

    public int updateBalance(@Param("id") long id, @Param("balance") int balance);

    public int updateStatsInfo(@Param("id") int id, @Param("orderCount") int orderCount, @Param("lastChargeTime") Date lastChargeTime);

    public int updateBackBatteryOrderId(@Param("id") long id, @Param("backBatteryOrderId") String backBatteryOrderId);

    public int updateBindingOvertimeFaultLogId(@Param("id") Long id, @Param("bindingOvertimeFaultLogId") Long bindingOvertimeFaultLogId);

    public int updateLowVolumeCount(@Param("id") Long id, @Param("lowVolumeCount") int lowVolumeCount);

    public int updateGiveTime(@Param("id") Long id, @Param("giveTime") Date giveTime);
}
