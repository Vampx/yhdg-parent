package cn.com.yusong.yhdg.webserver.persistence.hdg;

import cn.com.yusong.yhdg.common.domain.hdg.CabinetProperty;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by zhoub on 2017/10/25.
 */
public interface CabinetPropertyMapper extends MasterMapper {
    List<CabinetProperty> findByCabinet(@Param("cabinetId") String cabinetId);
    int deleteByCabinet(@Param("cabinetId") String cabinetId);
    int insert(CabinetProperty cabinetProperty);
}
