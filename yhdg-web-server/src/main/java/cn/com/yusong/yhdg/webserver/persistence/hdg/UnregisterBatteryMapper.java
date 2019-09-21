package cn.com.yusong.yhdg.webserver.persistence.hdg;

import cn.com.yusong.yhdg.common.domain.hdg.UnregisterBattery;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UnregisterBatteryMapper extends MasterMapper {
    UnregisterBattery find(@Param("id") String id);

    int findCountByQrcode(@Param("qrcode") String qrcode);

    int findCountByCode(@Param("code") String code);

    int findPageCount(UnregisterBattery unregisterBattery);

    List<UnregisterBattery> findPageResult(UnregisterBattery unregisterBattery);

    int findCountByShellCode(@Param("shellCode") String shellCode);

    UnregisterBattery findByCodeAndShellCode(@Param("code") String code, @Param("shellCode") String shellCode);

    UnregisterBattery findByShellCode(@Param("shellCode") String shellCode);

    UnregisterBattery findByCode(String code);

    int insert(UnregisterBattery unregisterBattery);

    int delete(@Param("id") String id);

    int checkBattery(@Param("id") String id, @Param("cellMfr") String cellMfr, @Param("cellModel") String cellModel);

    int updateQrcode(@Param("id") String id, @Param("qrcode") String qrcode);

    int updateCode(@Param("id") String id, @Param("code") String code);

    int updateIdAndCode(@Param("id") String id, @Param("toId") String toId, @Param("code") String code);

    int updateCellCount(@Param("id") String id, @Param("cellCount") int cellCount);

    int clearCode(@Param("id") String id, @Param("code") String code, @Param("newCode") String newCode);
}
