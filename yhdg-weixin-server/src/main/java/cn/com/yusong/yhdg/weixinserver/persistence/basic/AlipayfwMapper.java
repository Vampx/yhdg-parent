package cn.com.yusong.yhdg.weixinserver.persistence.basic;

import cn.com.yusong.yhdg.common.domain.basic.Alipayfw;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface AlipayfwMapper extends MasterMapper {

    public Alipayfw find(@Param("id") int id);
    public List<Alipayfw> findAll();
}
