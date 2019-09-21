package cn.com.yusong.yhdg.serviceserver.persistence.basic;

import cn.com.yusong.yhdg.common.domain.basic.WeixinmpOpenId;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

public interface WeixinmpOpenIdMapper extends MasterMapper {
    public WeixinmpOpenId findByOpenId(@Param("weixinmpId") int weixinmpId, @Param("openId") String openId);
}
