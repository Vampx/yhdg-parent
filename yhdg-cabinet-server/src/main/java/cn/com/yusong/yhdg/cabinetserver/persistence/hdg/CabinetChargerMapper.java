package cn.com.yusong.yhdg.cabinetserver.persistence.hdg;

import cn.com.yusong.yhdg.common.domain.hdg.CabinetBox;
import cn.com.yusong.yhdg.common.domain.hdg.CabinetCharger;
import cn.com.yusong.yhdg.common.entity.Kv;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CabinetChargerMapper extends MasterMapper {

    CabinetCharger find(@Param("cabinetId") String cabinetId, @Param("boxNum") String boxNum);

    List<CabinetCharger> findListByCabinet(String cabinetId);

    List<CabinetCharger> findByOldVersion(String chargerVersion);

    int insert(CabinetCharger cabinetCharger);

    int update(CabinetCharger cabinetCharger);
}
