package cn.com.yusong.yhdg.appserver.persistence.basic;

import cn.com.yusong.yhdg.common.domain.basic.SyncCustomerInfo;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

public interface SyncCustomerInfoMapper extends MasterMapper {
    public SyncCustomerInfo findByMobile(@Param("mobile") String mobile);
    public SyncCustomerInfo findMpOpenId(@Param("mpOpenId") String mpOpenId);
    public SyncCustomerInfo findFwOpenId(@Param("fwOpenId") String fwOpenId);
    public int insert(SyncCustomerInfo syncCustomerInfo);
    public int delete(long id);
}
