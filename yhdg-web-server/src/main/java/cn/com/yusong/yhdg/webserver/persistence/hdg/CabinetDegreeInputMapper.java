package cn.com.yusong.yhdg.webserver.persistence.hdg;

import cn.com.yusong.yhdg.common.domain.hdg.CabinetDegreeInput;
import cn.com.yusong.yhdg.common.domain.hdg.Estate;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CabinetDegreeInputMapper extends MasterMapper {


    int findPageCount(CabinetDegreeInput estate);

    List<CabinetDegreeInput> findPageResult(CabinetDegreeInput estate);

    int findViewPageCount(CabinetDegreeInput estate);

    List<CabinetDegreeInput> findViewPageResult(CabinetDegreeInput estate);

    List<CabinetDegreeInput> findAll();

    List<CabinetDegreeInput> findByCabinetId(@Param("cabinetId")String cabinetId);

}
