package cn.com.yusong.yhdg.webserver.persistence.basic;

import cn.com.yusong.yhdg.common.domain.basic.StationInOutMoney;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;

import java.util.List;

public interface StationInOutMoneyMapper extends MasterMapper {
    StationInOutMoney find(Long id);
    int findPageCount(StationInOutMoney search);
    List<StationInOutMoney> findPageResult(StationInOutMoney search);
    int insert(StationInOutMoney entity);

}
