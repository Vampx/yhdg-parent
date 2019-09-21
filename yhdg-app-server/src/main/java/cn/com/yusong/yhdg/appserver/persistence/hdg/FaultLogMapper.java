package cn.com.yusong.yhdg.appserver.persistence.hdg;

import cn.com.yusong.yhdg.common.domain.hdg.FaultLog;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * Created by ruanjian5 on 2017/11/10.
 */
public interface FaultLogMapper extends MasterMapper {

    public int findCountByDispatcher(@Param("dispatcherId") long dispatcherId, @Param("status") Integer status);

    public List<FaultLog> findDispatcher(@Param("dispatcherId") long dispatcherId,
                               @Param("cabinetId") String cabinetId,
                               @Param("status") Integer status);


    public List<FaultLog> findByStatus(@Param("dispatcherId") long dispatcherId,
                                       @Param("status") Integer status,
                                       @Param("offset") int offset,
                                       @Param("limit") int limit );

    public int handle(@Param("id")long id,
                      @Param("handleType") int handleType,
                      @Param("handleMemo") String handleMemo,
                      @Param("handlerName") String handlerName,
                      @Param("handleTime") Date handleTime,
                      @Param("toStatus") int toStatus,
                      @Param("fromStatus") int fromStatus);

    int findFaultType(long id);
}
