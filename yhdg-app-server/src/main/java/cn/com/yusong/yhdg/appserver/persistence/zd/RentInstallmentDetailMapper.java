package cn.com.yusong.yhdg.appserver.persistence.zd;

import cn.com.yusong.yhdg.common.domain.zd.RentInstallmentDetail;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;

import java.util.List;


public interface RentInstallmentDetailMapper extends MasterMapper {

    List<RentInstallmentDetail> findListBySettingId(Long settingId);

}
