package cn.com.yusong.yhdg.weixinserver.persistence.hdg;

import cn.com.yusong.yhdg.common.domain.hdg.Cabinet;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

public interface CabinetMapper extends MasterMapper {

    Cabinet find(@Param("id") String id);

}
