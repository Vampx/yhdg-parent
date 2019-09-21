package cn.com.yusong.yhdg.webserver.persistence.basic;


import cn.com.yusong.yhdg.common.domain.basic.PartnerInOutMoney;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface PartnerInOutMoneyMapper extends MasterMapper {
    public PartnerInOutMoney find(@Param("id") Long id);
    public int insert(PartnerInOutMoney record);
    public int findPageCount(PartnerInOutMoney search);
    List<PartnerInOutMoney> findPageResult(PartnerInOutMoney search);
}
