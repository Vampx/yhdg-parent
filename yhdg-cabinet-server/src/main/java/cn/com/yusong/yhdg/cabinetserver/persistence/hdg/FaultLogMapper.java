package cn.com.yusong.yhdg.cabinetserver.persistence.hdg;

import cn.com.yusong.yhdg.common.domain.hdg.FaultLog;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

public interface FaultLogMapper extends MasterMapper {
    public int insert(FaultLog log);

    public int handle(@Param("id") long id,
               @Param("handleType") int handleType,
               @Param("handleMemo") String handleMemo,
               @Param("handleTime") Date handleTime,
               @Param("handlerName") String handlerName,
               @Param("status") int status
               );
}
