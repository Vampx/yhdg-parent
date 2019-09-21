package cn.com.yusong.yhdg.appserver.persistence.zc;

import cn.com.yusong.yhdg.common.domain.hdg.CabinetAddressCorrectionExemptReview;
import cn.com.yusong.yhdg.common.domain.hdg.ShopAddressCorrectionExemptReview;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

/**
 * Created by ruanjian5 on 2017/12/15.
 */
public interface ShopAddressCorrectionExemptReviewMapper extends MasterMapper {
    public ShopAddressCorrectionExemptReview find(@Param("id") long id);
}
