package cn.com.yusong.yhdg.agentserver.persistence.hdg;

import cn.com.yusong.yhdg.common.domain.hdg.BespeakOrder;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface BespeakOrderMapper extends MasterMapper {
    int findPageCount(BespeakOrder bespeakOrder);

    List<BespeakOrder> findPageResult(BespeakOrder bespeakOrder);

    BespeakOrder find(String id);

    int updateStatus(@Param("id") String id, @Param("status") Integer status, @Param("cancelTime") Date cancelTime);
}
