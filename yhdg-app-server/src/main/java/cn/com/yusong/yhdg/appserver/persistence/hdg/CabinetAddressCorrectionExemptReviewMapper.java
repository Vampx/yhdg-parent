package cn.com.yusong.yhdg.appserver.persistence.hdg;

import cn.com.yusong.yhdg.common.domain.hdg.CabinetAddressCorrectionExemptReview;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

/**
 * Created by ruanjian5 on 2017/12/15.
 */
public interface CabinetAddressCorrectionExemptReviewMapper extends MasterMapper {
    public CabinetAddressCorrectionExemptReview find(@Param("id") long id);
}
