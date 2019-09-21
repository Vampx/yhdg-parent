package cn.com.yusong.yhdg.cabinetserver.persistence.hdg;

import cn.com.yusong.yhdg.common.domain.hdg.CabinetDayDegreeStats;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;

public interface CabinetDayDegreeStatsMapper extends MasterMapper {
    public CabinetDayDegreeStats find(@Param("cabinetId") String cabinetId,
                                         @Param("statsDate") String statsDate);

    public CabinetDayDegreeStats findBefore(@Param("cabinetId") String cabinetId,
                                            @Param("statsDate") String statsDate);

    int insert(CabinetDayDegreeStats cabinetDayDegreeStats);

    public int updateEnd(@Param("cabinetId") String cabinetId,
                      @Param("statsDate") String statsDate,@Param("endTime") Date endTime, @Param("endNum") int endNum,
                      @Param("num") int num
    );
    public int updateBegin(@Param("cabinetId") String cabinetId,
                      @Param("statsDate") String statsDate, @Param("endTime") Date endTime, @Param("beginNum") int beginNum,@Param("endNum") int endNum,
                      @Param("num") int num
    );
}
