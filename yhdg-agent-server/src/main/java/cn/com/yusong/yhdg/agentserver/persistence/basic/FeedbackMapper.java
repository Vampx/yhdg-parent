package cn.com.yusong.yhdg.agentserver.persistence.basic;

import cn.com.yusong.yhdg.common.domain.basic.Feedback;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * Created by chen on 2017/10/31.
 */
public interface FeedbackMapper extends MasterMapper {
    Feedback find(long id);
    int findPageCount(Feedback search);
    List<Feedback> findPageResult(Feedback search);
    int delete(long id);
    int deleteByCustomerId(@Param("customerId") long customerId);
}
