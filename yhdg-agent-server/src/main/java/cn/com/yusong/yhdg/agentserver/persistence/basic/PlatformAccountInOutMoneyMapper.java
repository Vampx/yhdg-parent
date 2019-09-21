package cn.com.yusong.yhdg.agentserver.persistence.basic;

import cn.com.yusong.yhdg.common.domain.basic.PlatformAccount;
import cn.com.yusong.yhdg.common.domain.basic.PlatformAccountInOutMoney;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface PlatformAccountInOutMoneyMapper extends MasterMapper {
    PlatformAccountInOutMoney find(@Param("id") Long id);
    int findPageCount(PlatformAccountInOutMoney PlatformAccount);
    List<PlatformAccountInOutMoney> findPageResult(PlatformAccountInOutMoney PlatformAccount);
    int insert(PlatformAccountInOutMoney inOutMoney);
}
