package cn.com.yusong.yhdg.agentserver.persistence.zd;

import cn.com.yusong.yhdg.common.domain.zd.RentForegiftRefund;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;

public interface RentForegiftRefundMapper extends MasterMapper {

    int insert(RentForegiftRefund rentForegiftRefund);
}