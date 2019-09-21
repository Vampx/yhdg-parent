package cn.com.yusong.yhdg.webserver.persistence.zd;

import cn.com.yusong.yhdg.common.domain.zd.RentPeriodActivity;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;

import java.util.List;

public interface RentPeriodActivityMapper extends MasterMapper {
    RentPeriodActivity find(long id);

    int findPageCount(RentPeriodActivity entity);

    List<RentPeriodActivity> findPageResult(RentPeriodActivity entity);

    int insert(RentPeriodActivity entity);

    int update(RentPeriodActivity entity);

    int delete(long id);
}
