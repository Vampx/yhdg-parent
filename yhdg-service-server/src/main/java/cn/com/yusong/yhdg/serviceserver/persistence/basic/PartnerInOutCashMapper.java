package cn.com.yusong.yhdg.serviceserver.persistence.basic;

import cn.com.yusong.yhdg.common.domain.basic.PartnerInOutCash;
import cn.com.yusong.yhdg.common.domain.hdg.CabinetDayStats;
import cn.com.yusong.yhdg.common.domain.hdg.CabinetMonthStats;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

public interface PartnerInOutCashMapper extends MasterMapper {
    public PartnerInOutCash find(@Param("partnerId") int partnerId, @Param("statsDate") String statsDate);
    public int insert(PartnerInOutCash partnerInOutCash);
    public int update(PartnerInOutCash partnerInOutCash);
}
