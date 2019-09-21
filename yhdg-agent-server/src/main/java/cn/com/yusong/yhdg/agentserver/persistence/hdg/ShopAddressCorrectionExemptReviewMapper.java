package cn.com.yusong.yhdg.agentserver.persistence.hdg;

import cn.com.yusong.yhdg.common.domain.hdg.ShopAddressCorrectionExemptReview;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;

import java.util.List;

public interface ShopAddressCorrectionExemptReviewMapper extends MasterMapper {
    int findPageCount(ShopAddressCorrectionExemptReview shopAddressCorrectionExemptReview);
    List findPageResult(ShopAddressCorrectionExemptReview shopAddressCorrectionExemptReview);
    ShopAddressCorrectionExemptReview find(long id);
    int insert(ShopAddressCorrectionExemptReview shopAddressCorrectionExemptReview);
    int delete(long id);
}
