package cn.com.yusong.yhdg.staticserver.persistence.basic;

import cn.com.yusong.yhdg.common.domain.basic.LaxinRecord;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface LaxinRecordMapper extends MasterMapper {
    public List<LaxinRecord> findByOrderId(@Param("orderId") String orderId);
    public int insert(LaxinRecord laxinRecord);
    public int updateStatus(@Param("id") String id, @Param("orderId") String orderId, @Param("status") int status, @Param("payType") int payType, @Param("payTime") Date payTime);
}
