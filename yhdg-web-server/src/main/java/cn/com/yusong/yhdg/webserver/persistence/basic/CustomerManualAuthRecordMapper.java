package cn.com.yusong.yhdg.webserver.persistence.basic;

import cn.com.yusong.yhdg.common.domain.basic.CustomerManualAuthRecord;
import cn.com.yusong.yhdg.common.domain.zd.RentOrder;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface CustomerManualAuthRecordMapper extends MasterMapper {

    CustomerManualAuthRecord find(long id);

    int findPageCount(CustomerManualAuthRecord search);

    List<CustomerManualAuthRecord> findPageResult(CustomerManualAuthRecord search);

    int audit(@Param("id") long id, @Param("status")int status, @Param("auditMemo")String auditMemo, @Param("auditTime")Date auditTime, @Param("auditUser") String auditUser);
}

