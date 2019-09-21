package cn.com.yusong.yhdg.webserver.persistence.hdg;


import cn.com.yusong.yhdg.common.domain.hdg.Battery;
import cn.com.yusong.yhdg.common.domain.hdg.BatteryCellModel;
import cn.com.yusong.yhdg.common.domain.hdg.CabinetCharger;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface CabinetChargerMapper extends MasterMapper {
    CabinetCharger findByCabinetBox(@Param("cabinetId") String cabinetId, @Param("boxNum") String boxNum);
    List<CabinetCharger> findByCabinetId(@Param("cabinetId") String cabinetId);
    int update(CabinetCharger cabinetCharger);
}
