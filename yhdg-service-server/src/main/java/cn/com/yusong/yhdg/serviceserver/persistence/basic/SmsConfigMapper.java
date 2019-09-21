package cn.com.yusong.yhdg.serviceserver.persistence.basic;

import cn.com.yusong.yhdg.common.domain.basic.SmsConfigInfo;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SmsConfigMapper extends MasterMapper {
    public List<SmsConfigInfo> findInfoByPartner(@Param("partnerId") int partnerId);
}
