package cn.com.yusong.yhdg.agentappserver.persistence.hdg;

import cn.com.yusong.yhdg.common.domain.hdg.CabinetDegreeInput;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CabinetDegreeInputMapper extends MasterMapper {

    CabinetDegreeInput find(Long id);

    int insert(CabinetDegreeInput record);

    List<CabinetDegreeInput> findList(@Param("cabinetId") String cabinetId, @Param("offset") int offset, @Param("limit") int limit);

    CabinetDegreeInput findEnd(String cabinetId);

    CabinetDegreeInput findSum(String cabinetId);

    List<CabinetDegreeInput> findListByEstate(@Param("agentId") Integer agentId, @Param("estateId") long estateId, @Param("offset") int offset, @Param("limit") int limit);

    int updateStatus(@Param("id") long id, @Param("status") int status);

}