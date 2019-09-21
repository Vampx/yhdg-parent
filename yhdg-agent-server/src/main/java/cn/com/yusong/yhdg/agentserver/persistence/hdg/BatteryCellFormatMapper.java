package cn.com.yusong.yhdg.agentserver.persistence.hdg;

import cn.com.yusong.yhdg.common.domain.hdg.BatteryCellFormat;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface BatteryCellFormatMapper extends MasterMapper {
    BatteryCellFormat find(long id);
    List<BatteryCellFormat> findByCellModelId(@Param("cellModelId") long cellModelId);
    int findPageCount(BatteryCellFormat batteryCellFormat);
    List<BatteryCellFormat> findPageResult(BatteryCellFormat batteryCellFormat);
    int insert(BatteryCellFormat batteryCellFormat);
    int update(BatteryCellFormat batteryCellFormat);
    int delete(long id);
}
