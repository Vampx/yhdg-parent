package cn.com.yusong.yhdg.agentappserver.persistence.basic;

import cn.com.yusong.yhdg.common.domain.basic.LaxinPayOrderDetail;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;

public interface LaxinPayOrderDetailMapper extends MasterMapper {

    public int insert(LaxinPayOrderDetail detail);
}
