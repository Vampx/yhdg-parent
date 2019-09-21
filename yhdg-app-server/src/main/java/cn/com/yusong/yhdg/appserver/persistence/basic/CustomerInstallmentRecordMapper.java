package cn.com.yusong.yhdg.appserver.persistence.basic;

import cn.com.yusong.yhdg.common.domain.basic.CustomerInstallmentRecord;
import cn.com.yusong.yhdg.common.domain.basic.CustomerInstallmentRecordPayDetail;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CustomerInstallmentRecordMapper extends MasterMapper {

    CustomerInstallmentRecord find(@Param("id") long id);

    List<CustomerInstallmentRecord> findList(@Param("customerId") long customerId, @Param("status") int status, @Param("category") int category);

    int insert(CustomerInstallmentRecord customerInstallmentRecord);

    int updateOrderStatus(@Param("id") Long id,
                          @Param("toStatus") int toStatus,
                          @Param("fromStatus") Integer fromStatus);

    int updatePaidMoney(@Param("id") long id, @Param("status") int status);
}
