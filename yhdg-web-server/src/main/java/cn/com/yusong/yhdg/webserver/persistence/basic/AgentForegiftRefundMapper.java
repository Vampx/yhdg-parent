package cn.com.yusong.yhdg.webserver.persistence.basic;

import cn.com.yusong.yhdg.common.domain.basic.AgentForegiftRefund;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;

import java.util.List;

public interface AgentForegiftRefundMapper extends MasterMapper {

    int insert(AgentForegiftRefund agentForegiftRefund);

    int findPageCount(AgentForegiftRefund agentForegiftRefund);

    List<AgentForegiftRefund> findPageResult(AgentForegiftRefund agentForegiftRefund);
}