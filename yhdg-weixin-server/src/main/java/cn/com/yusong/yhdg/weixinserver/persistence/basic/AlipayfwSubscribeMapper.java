package cn.com.yusong.yhdg.weixinserver.persistence.basic;

import cn.com.yusong.yhdg.common.domain.basic.AlipayfwSubscribe;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

public interface AlipayfwSubscribeMapper extends MasterMapper {

    public AlipayfwSubscribe findByOpenId(@Param("alipayfwId") int alipayfwId, @Param("openId") String openId);
    public int insert(AlipayfwSubscribe fwSubscribe);
    public int delete(@Param("alipayfwId") int alipayfwId, @Param("openId") String openId);
}
