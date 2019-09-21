package cn.com.yusong.yhdg.appserver.persistence.basic;

import cn.com.yusong.yhdg.common.domain.basic.PartnerMaOpenId;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

public interface PartnerMaOpenIdMapper extends MasterMapper {
    public PartnerMaOpenId findByOpenId(@Param("partnerId") int partnerId, @Param("openId") String openId);
    public int insert(PartnerMaOpenId openId);
    public int update(@Param("partnerId") int partnerId, @Param("openId") String openId, @Param("nickname") String nickname, @Param("photoPath") String photoPath);
    public int updateCustomerId(@Param("partnerId") int partnerId, @Param("openId") String openId, @Param("customerId") Long customerId);
    public int updateSessionKey(@Param("id")long id, @Param("sessionKey") String sessionKey);
}
