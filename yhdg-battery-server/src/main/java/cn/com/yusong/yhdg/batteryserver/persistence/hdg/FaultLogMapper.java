package cn.com.yusong.yhdg.batteryserver.persistence.hdg;

import cn.com.yusong.yhdg.common.domain.hdg.FaultLog;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;

public interface FaultLogMapper extends MasterMapper {
    int handle(@Param("id") long id,
               @Param("handleType") int handleType,
               @Param("handleMemo") String handleMemo,
               @Param("handleTime") Date handleTime,
               @Param("handlerName") String handlerName,
               @Param("status") int status
    );
    int create(FaultLog faultLog);

    int updateFaultLevel(@Param("id") long id, @Param("faultLevel") int faultLevel);
}
