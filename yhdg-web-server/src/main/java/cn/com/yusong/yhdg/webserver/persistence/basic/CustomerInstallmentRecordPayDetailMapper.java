package cn.com.yusong.yhdg.webserver.persistence.basic;

import cn.com.yusong.yhdg.common.domain.basic.CustomerInstallmentRecordPayDetail;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CustomerInstallmentRecordPayDetailMapper extends MasterMapper {
    CustomerInstallmentRecordPayDetail find(@Param("id") Long id);
    int findCountByRecordId(@Param("recordId") long recordId);
    int findPageCount(CustomerInstallmentRecordPayDetail search);
    List<CustomerInstallmentRecordPayDetail> findPaidListByRecordId(@Param("recordId") Long recordId, @Param("category") Integer category, @Param("status") Integer status);
    List<CustomerInstallmentRecordPayDetail> findListByRecordId(@Param("recordId") Long recordId);
    List<CustomerInstallmentRecordPayDetail> findPageResult(CustomerInstallmentRecordPayDetail search);
    CustomerInstallmentRecordPayDetail findByRecordIdAndNum(@Param("recordId") Long recordId, @Param("num") Integer num, @Param("category") Integer category);
    int updateStatus(@Param("id") Long id,  @Param("status") Integer status);
}
