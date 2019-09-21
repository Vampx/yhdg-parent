package cn.com.yusong.yhdg.serviceserver.persistence.zd;

import cn.com.yusong.yhdg.common.domain.zd.CustomerRentInfo;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;

public interface CustomerRentInfoMapper extends MasterMapper {
    CustomerRentInfo find(long id);
}
