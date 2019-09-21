package cn.com.yusong.yhdg.appserver.persistence.basic;


import cn.com.yusong.yhdg.common.domain.basic.Feedback;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;

public interface FeedbackMapper extends MasterMapper {
    public int insert(Feedback feedback);

}
