package cn.com.yusong.yhdg.serviceserver.persistence.basic;

import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;

public interface FeedbackMapper extends MasterMapper {
    public int findTotal();

    public int findIncrement(@Param("beginTime") Date beginTime, @Param("endTime") Date endTime);
}
