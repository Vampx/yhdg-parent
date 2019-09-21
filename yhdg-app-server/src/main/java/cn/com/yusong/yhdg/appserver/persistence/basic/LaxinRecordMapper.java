package cn.com.yusong.yhdg.appserver.persistence.basic;

import cn.com.yusong.yhdg.common.domain.basic.LaxinRecord;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface LaxinRecordMapper extends MasterMapper {

    public List<LaxinRecord> findList (@Param("laxinId") long laxinId, @Param("status")int status, @Param("offset")int offset, @Param("limit")int limit);

    public LaxinRecord findByTargetMobile (@Param("laxinId") long laxinId, @Param("targetMobile")String targetMobile);

    public int totalMoneyByTransferTime(@Param("laxinId") long laxinId, @Param("status")int status, @Param("beginTime") Date beginTime, @Param("endTime")Date endTime);

    public int totalCountByTransferTime(@Param("laxinId") long laxinId, @Param("status")int status, @Param("beginTime") Date beginTime, @Param("endTime")Date endTime);

    public int totalMoneyByStatus(@Param("laxinId") long laxinId, @Param("status")int[] status);

    public int insert(LaxinRecord laxinRecord);
}
