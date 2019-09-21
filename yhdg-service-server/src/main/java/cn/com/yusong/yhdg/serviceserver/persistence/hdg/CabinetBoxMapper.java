package cn.com.yusong.yhdg.serviceserver.persistence.hdg;

import cn.com.yusong.yhdg.common.domain.hdg.CabinetBox;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface CabinetBoxMapper extends MasterMapper {
    List<CabinetBox> findLockList(@Param("lockTime") Date lockTime, @Param("boxStatusList") List<Integer> boxStatusList);

    List<String> findEmptyBox(@Param("boxStatus") Integer boxStatus);

    public CabinetBox findOneEmptyBoxNum(@Param("cabinetId") String cabinetId, @Param("boxStatus") int boxStatus);

    public List<CabinetBox> findAll(@Param("offset") int offset, @Param("limit") int limit);

    public List<CabinetBox> findAllByAgent(@Param("agentId") int agentId, @Param("offset") int offset, @Param("limit") int limit);

    public List<CabinetBox> findByNotAgent(@Param("agentIdList") List<Integer> agentId, @Param("offset") int offset, @Param("limit") int limit);

    int updateOnline(@Param("cabinetId") String cabinetId, @Param("isOnline") int isOnline);

    int updateStatus(@Param("cabinetId") String cabinetId, @Param("boxNum") String boxNum, @Param("fromStatus") int fromStatus, @Param("toStatus") int toStatus);

    int findEmptyBoxCount(@Param("cabinetId") String cabinetId,@Param("boxStatus") Integer boxStatus);
}
