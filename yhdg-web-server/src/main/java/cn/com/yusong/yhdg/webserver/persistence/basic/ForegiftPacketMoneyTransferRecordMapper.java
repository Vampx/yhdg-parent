package cn.com.yusong.yhdg.webserver.persistence.basic;

import cn.com.yusong.yhdg.common.domain.basic.ForegiftPacketMoneyTransferRecord;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ForegiftPacketMoneyTransferRecordMapper extends MasterMapper {
    ForegiftPacketMoneyTransferRecord find(@Param("id") Long id);
    ForegiftPacketMoneyTransferRecord findByCustomerId(@Param("customerId") Long customerId, @Param("foregiftOrderId") String foregiftOrderId);
    int findPageCount(ForegiftPacketMoneyTransferRecord search);
    List<ForegiftPacketMoneyTransferRecord> findPageResult(ForegiftPacketMoneyTransferRecord search);
    int insert(ForegiftPacketMoneyTransferRecord foregiftPacketMoneyTransferRecord);
}
