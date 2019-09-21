package cn.com.yusong.yhdg.webserver.persistence.basic;


import cn.com.yusong.yhdg.common.domain.basic.PartnerInOutCash;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;

import java.util.List;

public interface PartnerInOutCashMapper extends MasterMapper {
    int findPageCount(PartnerInOutCash search);
    List<PartnerInOutCash> findPageResult(PartnerInOutCash search);
}
