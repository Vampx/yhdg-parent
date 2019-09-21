package cn.com.yusong.yhdg.appserver.persistence.hdg;

import cn.com.yusong.yhdg.common.domain.hdg.CabinetAddressCorrection;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;

/**
 * Created by ruanjian5 on 2017/12/15.
 */
public interface CabinetAddressCorrectionMapper extends MasterMapper {

    public void insert(CabinetAddressCorrection entiy);
}
