package cn.com.yusong.yhdg.webserver.persistence.basic;

import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

public interface AlipayfwOpenIdMapper extends MasterMapper {
    public int deleteByOpenId(@Param("openId") String openId);
}
