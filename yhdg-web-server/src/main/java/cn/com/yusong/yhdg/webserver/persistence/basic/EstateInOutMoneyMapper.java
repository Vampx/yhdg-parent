package cn.com.yusong.yhdg.webserver.persistence.basic;

import cn.com.yusong.yhdg.common.domain.basic.EstateInOutMoney;
import cn.com.yusong.yhdg.common.domain.basic.ShopInOutMoney;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;

import java.util.List;

public interface EstateInOutMoneyMapper extends MasterMapper {
    EstateInOutMoney find(Long id);
    int findPageCount(EstateInOutMoney search);
    List<EstateInOutMoney> findPageResult(EstateInOutMoney search);
    int insert(EstateInOutMoney entity);

}
