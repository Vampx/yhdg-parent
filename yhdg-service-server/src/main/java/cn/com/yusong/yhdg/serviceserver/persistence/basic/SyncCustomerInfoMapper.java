package cn.com.yusong.yhdg.serviceserver.persistence.basic;

import cn.com.yusong.yhdg.common.domain.basic.SyncCustomerInfo;
import cn.com.yusong.yhdg.common.domain.hdg.PushOrderMessage;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface SyncCustomerInfoMapper extends MasterMapper {
    public List<SyncCustomerInfo> findList(@Param("sendStatus") int sendStatus, @Param("offset") int offset, @Param("limit") int limit);
    public int complete(@Param("id") long id, @Param("handleTime") Date handleTime, @Param("sendStatus") int sendStatus, @Param("resendNum") Integer resendNum);
}
