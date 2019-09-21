package cn.com.yusong.yhdg.webserver.persistence.hdg;

import cn.com.yusong.yhdg.common.domain.hdg.CabinetAddressCorrection;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CabinetAddressCorrectionMapper extends MasterMapper {
    int findPageCount(CabinetAddressCorrection cabinetAddressCorrection);

    List<CabinetAddressCorrection> findPageResult(CabinetAddressCorrection cabinetAddressCorrection);

    CabinetAddressCorrection find(long id);

    int updateStatus(@Param("id") long id, @Param("status") int status);

    int delete(long id);

    int deleteByCustomerId(@Param("customerId") long customerId);

}
