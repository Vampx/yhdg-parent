package cn.com.yusong.yhdg.webserver.persistence.basic;

import cn.com.yusong.yhdg.common.domain.basic.WeixinmpOpenId;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

public interface WeixinmpOpenIdMapper extends MasterMapper {
	public int deleteByOpenId(@Param("openId") String openId);
}
