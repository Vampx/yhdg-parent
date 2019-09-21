package cn.com.yusong.yhdg.serviceserver.persistence.basic;

import cn.com.yusong.yhdg.common.domain.basic.AlipayfwOpenId;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

public interface AlipayfwOpenIdMapper extends MasterMapper {
    public AlipayfwOpenId findByOpenId(@Param("alipayfwId") int alipayfwId, @Param("openId") String openId);
}
