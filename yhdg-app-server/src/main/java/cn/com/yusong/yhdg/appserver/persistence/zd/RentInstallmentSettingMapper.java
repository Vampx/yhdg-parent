package cn.com.yusong.yhdg.appserver.persistence.zd;

import cn.com.yusong.yhdg.common.domain.zd.RentInstallmentSetting;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;


public interface RentInstallmentSettingMapper extends MasterMapper {

    RentInstallmentSetting find(long id);

    RentInstallmentSetting findByMobile(@Param("mobile") String mobile);

}
