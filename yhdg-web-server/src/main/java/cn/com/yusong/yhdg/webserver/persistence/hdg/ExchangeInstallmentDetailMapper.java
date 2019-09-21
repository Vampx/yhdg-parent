package cn.com.yusong.yhdg.webserver.persistence.hdg;

import cn.com.yusong.yhdg.common.domain.basic.CustomerForegiftOrder;
import cn.com.yusong.yhdg.common.domain.hdg.ExchangeInstallmentDetail;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;
import org.omg.PortableInterceptor.INACTIVE;

import java.util.List;


public interface ExchangeInstallmentDetailMapper extends MasterMapper {

    List<ExchangeInstallmentDetail> findListBySettingId(Long settingId);

    int findPageCount(ExchangeInstallmentDetail exchangeInstallmentDetail);

    List<ExchangeInstallmentDetail> findPageResult(ExchangeInstallmentDetail exchangeInstallmentDetail);

    int insert(ExchangeInstallmentDetail exchangeInstallmentDetail);

    int update(ExchangeInstallmentDetail exchangeInstallmentDetail);

    int updateNum(@Param("settingId") Long settingId, @Param("fromNum") Integer fromNum, @Param("toNum") Integer toNum);

    int delete(@Param("settingId") Long settingId, @Param("num") Integer num);

    int deleteBySettingId(@Param("settingId") Long settingId);
}
