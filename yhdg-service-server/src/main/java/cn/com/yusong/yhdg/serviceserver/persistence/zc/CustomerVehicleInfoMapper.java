package cn.com.yusong.yhdg.serviceserver.persistence.zc;

import cn.com.yusong.yhdg.common.domain.zc.CustomerVehicleInfo;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;

public interface CustomerVehicleInfoMapper extends MasterMapper {
    CustomerVehicleInfo find(long id);
}
