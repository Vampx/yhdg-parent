package cn.com.yusong.yhdg.agentappserver.persistence.basic;

import cn.com.yusong.yhdg.common.domain.basic.Weixinmp;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface WeixinmpMapper extends MasterMapper {

    public Weixinmp find(@Param("id") int id);
    public List<Weixinmp> findAll();
}
