package cn.com.yusong.yhdg.agentserver.persistence.basic;

import cn.com.yusong.yhdg.common.domain.basic.CustomerDepositGift;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CustomerDepositGiftMapper extends MasterMapper {
    public int insert(CustomerDepositGift entity);
    public int delete(@Param("partnerId") int partnerId);
    List<CustomerDepositGift> findAll();
    List<CustomerDepositGift> findPartnerId(@Param("partnerId") Integer partnerId);
}
