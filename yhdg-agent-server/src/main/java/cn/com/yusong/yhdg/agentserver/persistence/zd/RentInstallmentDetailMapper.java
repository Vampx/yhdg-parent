package cn.com.yusong.yhdg.agentserver.persistence.zd;

import cn.com.yusong.yhdg.common.domain.zd.RentInstallmentDetail;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;


public interface RentInstallmentDetailMapper extends MasterMapper {

    List<RentInstallmentDetail> findListBySettingId(Long settingId);

    int findPageCount(RentInstallmentDetail rentInstallmentDetail);

    List<RentInstallmentDetail> findPageResult(RentInstallmentDetail rentInstallmentDetail);

    int insert(RentInstallmentDetail rentInstallmentDetail);

    int update(RentInstallmentDetail rentInstallmentDetail);

    int updateNum(@Param("settingId") Long settingId, @Param("fromNum") Integer fromNum, @Param("toNum") Integer toNum);

    int delete(@Param("settingId") Long settingId, @Param("num") Integer num);

    int deleteBySettingId(@Param("settingId") Long settingId);
}
