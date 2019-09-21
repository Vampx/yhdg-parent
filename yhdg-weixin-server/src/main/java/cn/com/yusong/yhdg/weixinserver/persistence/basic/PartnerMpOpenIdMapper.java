package cn.com.yusong.yhdg.weixinserver.persistence.basic;

import cn.com.yusong.yhdg.common.domain.basic.PartnerMpOpenId;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

public interface PartnerMpOpenIdMapper extends MasterMapper {
    public PartnerMpOpenId findByOpenId(@Param("partnerId") Integer partnerId, @Param("openId") String openId);
    public int insert(PartnerMpOpenId openId);
    public int update(@Param("partnerId") Integer partnerId, @Param("openId")String openId,  @Param("nickname")String nickname, @Param("photoPath")String photoPath);
}
