package cn.com.yusong.yhdg.appserver.persistence.basic;

import cn.com.yusong.yhdg.common.domain.basic.CustomerDepositGift;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CustomerDepositGiftMapper extends MasterMapper {
    public List<CustomerDepositGift> findAll(@Param("partnerId") Integer partnerId);
}
