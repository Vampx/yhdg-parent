package cn.com.yusong.yhdg.agentserver.persistence.hdg;

import cn.com.yusong.yhdg.common.domain.hdg.KeepOrder;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface KeepOrderMapper extends MasterMapper {

    KeepOrder find(String id);

    int findCountByTakeCabinet(@Param("takeCabinetId") String takeCabinetId);

    int findCountByPutCabinet(@Param("putCabinetId") String putCabinetId);

    int findPageCount(KeepOrder keepOrder);

    List findPageResult(KeepOrder keepOrder);
}
