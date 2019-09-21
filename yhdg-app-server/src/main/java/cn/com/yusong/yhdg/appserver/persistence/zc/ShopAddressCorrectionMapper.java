package cn.com.yusong.yhdg.appserver.persistence.zc;

import cn.com.yusong.yhdg.common.domain.hdg.CabinetAddressCorrection;
import cn.com.yusong.yhdg.common.domain.hdg.ShopAddressCorrection;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;

/**
 * Created by ruanjian5 on 2017/12/15.
 */
public interface ShopAddressCorrectionMapper extends MasterMapper {

    public void insert(ShopAddressCorrection entiy);
}
