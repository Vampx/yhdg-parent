package cn.com.yusong.yhdg.appserver.persistence.basic;

import cn.com.yusong.yhdg.common.domain.basic.PartnerMpOpenId;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

public interface PartnerMpOpenIdMapper extends MasterMapper {
    public PartnerMpOpenId findByOpenId(@Param("partnerId")int partnerId, @Param("openId") String openId);
    public int insert(PartnerMpOpenId openId);
    public int update(@Param("partnerId")int partnerId, @Param("openId")String openId,  @Param("nickname")String nickname, @Param("photoPath")String photoPath);
    public int updateCustomerId(@Param("partnerId")int partnerId, @Param("openId") String openId, @Param("customerId") Long customerId);
}
