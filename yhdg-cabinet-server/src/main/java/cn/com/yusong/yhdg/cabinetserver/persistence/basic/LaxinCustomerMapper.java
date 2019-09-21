package cn.com.yusong.yhdg.cabinetserver.persistence.basic;

import cn.com.yusong.yhdg.common.domain.basic.LaxinCustomer;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;

public interface LaxinCustomerMapper extends MasterMapper {
    public LaxinCustomer findByTargetMobile(@Param("targetMobile") String targetMobile);
}
