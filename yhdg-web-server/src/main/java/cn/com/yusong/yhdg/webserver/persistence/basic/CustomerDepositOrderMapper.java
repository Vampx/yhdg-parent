package cn.com.yusong.yhdg.webserver.persistence.basic;

import cn.com.yusong.yhdg.common.domain.basic.CustomerDepositOrder;
import cn.com.yusong.yhdg.common.domain.hdg.BatteryOrder;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * Created by chen on 2017/5/20.
 */
public interface CustomerDepositOrderMapper extends MasterMapper {
    CustomerDepositOrder find(String id);
    int findPageCount(CustomerDepositOrder customerDepositOrder);
    List<CustomerDepositOrder> findPageResult(CustomerDepositOrder customerDepositOrder);
    List<CustomerDepositOrder> findList(long customerId);
    int insert(CustomerDepositOrder customerDepositOrder);
    int update(CustomerDepositOrder customerDepositOrder);
    int sumMoney(@Param("status") int status);
    int sumMonthMoney(@Param("status") int status, @Param("queryBeginDate") Date queryBeginDate, @Param("queryEndDate") Date queryEndDate);
    int deleteByCustomerId(@Param("customerId") long customerId);
    int refund(CustomerDepositOrder customerDepositOrder);
}
