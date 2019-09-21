package cn.com.yusong.yhdg.weixinserver.persistence.basic;

import cn.com.yusong.yhdg.common.domain.basic.WeixinmpSubscribe;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

public interface WeixinmpSubscribeMapper extends MasterMapper {

    public WeixinmpSubscribe findByOpenId(@Param("weixinmpId") int weixinmpId, @Param("openId") String openId);
    public int insert(WeixinmpSubscribe mpSubscribe);
    public int delete(@Param("weixinmpId") int weixinmpId, @Param("openId") String openId);
}
