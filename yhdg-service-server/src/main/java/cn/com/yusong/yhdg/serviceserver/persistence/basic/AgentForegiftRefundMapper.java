package cn.com.yusong.yhdg.serviceserver.persistence.basic;

import cn.com.yusong.yhdg.common.domain.basic.AgentForegiftRefund;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface AgentForegiftRefundMapper extends MasterMapper {

    public List<Map> findByAgent(@Param("beginTime") Date beginTime, @Param("endTime") Date endTime);

}