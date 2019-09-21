package cn.com.yusong.yhdg.appserver.persistence.basic;

import cn.com.yusong.yhdg.common.domain.basic.CustomerInstallmentRecordPayDetail;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;


public interface CustomerInstallmentRecordPayDetailMapper extends MasterMapper {
    CustomerInstallmentRecordPayDetail find(long id);
    List<CustomerInstallmentRecordPayDetail> findList(@Param("recordId") long recordId, @Param("customerId") long customerId, @Param("category") int category);
    int findCountByCustomerId(@Param("customerId") long customerId, @Param("status")int status, @Param("category") int category, @Param("nowDate") Date nowDate);
    int insert(CustomerInstallmentRecordPayDetail customerInstallmentRecordPayDetail);
    int updateApplyDetail(@Param("id") long id,
                          @Param("toStatus")int toStatus,
                          @Param("fromStatus")int fromStatus,
                          @Param("payTime") Date payTime,
                          @Param("money")Integer money,
                          @Param("payType")Integer payType);
    int updatePayType(@Param("recordId") long recordId, @Param("payType")Integer payType);
}
