package cn.com.yusong.yhdg.serviceserver.persistence.hdg;


import cn.com.yusong.yhdg.common.domain.hdg.FaultFeedback;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;

public interface FaultFeedbackMapper extends MasterMapper {
    FaultFeedback find(long id);

}
