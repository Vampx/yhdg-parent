package cn.com.yusong.yhdg.appserver.persistence.zc;


import cn.com.yusong.yhdg.common.domain.zc.Vehicle;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface VehicleMapper extends MasterMapper {
    Vehicle findByVinNo(@Param("vinNo") String vinNo);
    int updateCustomerUse(@Param("id") Integer id,
                          @Param("status") int status,
                          @Param("customerId") long customerId,
                          @Param("customerMobile") String customerMobile,
                          @Param("customerFullname") String customerFullname
    );
    Vehicle find(@Param("id") Integer id);
}
