package cn.com.yusong.yhdg.staticserver.persistence.zc;


import cn.com.yusong.yhdg.common.domain.zc.RentPrice;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;

import java.util.List;

public interface RentPriceMapper extends MasterMapper {
    RentPrice find(long id);

    List<RentPrice> findListBySettingId(long settingId);

}
