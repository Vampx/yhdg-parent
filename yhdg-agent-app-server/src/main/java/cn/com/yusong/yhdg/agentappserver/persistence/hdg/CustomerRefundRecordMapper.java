package cn.com.yusong.yhdg.agentappserver.persistence.hdg;

import cn.com.yusong.yhdg.common.domain.basic.CustomerRefundRecord;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CustomerRefundRecordMapper extends MasterMapper {

    int insert(CustomerRefundRecord refundRecord);

}