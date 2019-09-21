package cn.com.yusong.yhdg.webserver.persistence.hdg;

import cn.com.yusong.yhdg.common.domain.hdg.FaultLog;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface FaultLogMapper extends MasterMapper {
    int findPageCount(FaultLog faultLog);

    List findPageResult(FaultLog faultLog);

    FaultLog find(long id);

    int findCount(Integer status);

    int findCountByAgent(@Param("agentId") int agentId);

    int handle(@Param("id") long id,
               @Param("handleType") int handleType,
               @Param("handleMemo") String handleMemo,
               @Param("handleTime") Date handleTime,
               @Param("handlerName") String handlerName,
               @Param("status") int status);
}
