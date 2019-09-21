package cn.com.yusong.yhdg.serviceserver.persistence.basic;

import cn.com.yusong.yhdg.common.domain.basic.CustomerInstallmentRecordPayDetail;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;


public interface CustomerInstallmentRecordPayDetailMapper extends MasterMapper {
    CustomerInstallmentRecordPayDetail find(long id);
    List<CustomerInstallmentRecordPayDetail> findWillExpire(@Param("status") int status, @Param("expireTime") Date expireTime, @Param("offset") int offset, @Param("limit") int limit);
}
