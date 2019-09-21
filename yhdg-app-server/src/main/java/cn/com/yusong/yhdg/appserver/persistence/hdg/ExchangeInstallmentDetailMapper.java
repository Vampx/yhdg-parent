package cn.com.yusong.yhdg.appserver.persistence.hdg;

import cn.com.yusong.yhdg.common.domain.hdg.ExchangeInstallmentDetail;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;

import java.util.List;


public interface ExchangeInstallmentDetailMapper extends MasterMapper {

    List<ExchangeInstallmentDetail> findListBySettingId(Long settingId);

}
