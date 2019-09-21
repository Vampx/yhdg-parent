package cn.com.yusong.yhdg.staticserver.persistence.basic;

import cn.com.yusong.yhdg.common.domain.basic.CustomerInstallmentRecord;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

public interface CustomerInstallmentRecordMapper extends MasterMapper {

    CustomerInstallmentRecord find(@Param("id") long id);

    int updateOrderStatus(@Param("id") Long id,
                          @Param("toStatus") int toStatus,
                          @Param("fromStatus") Integer fromStatus);

    int updatePaidMoney(@Param("id") long id, @Param("status") int status);
}
