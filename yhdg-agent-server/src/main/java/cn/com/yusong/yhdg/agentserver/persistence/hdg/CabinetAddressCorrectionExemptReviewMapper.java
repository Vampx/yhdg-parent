package cn.com.yusong.yhdg.agentserver.persistence.hdg;

import cn.com.yusong.yhdg.common.domain.hdg.CabinetAddressCorrectionExemptReview;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;

import java.util.List;

public interface CabinetAddressCorrectionExemptReviewMapper extends MasterMapper {
    int findPageCount(CabinetAddressCorrectionExemptReview cabinetAddressCorrectionExemptReview);
    List findPageResult(CabinetAddressCorrectionExemptReview cabinetAddressCorrectionExemptReview);
    CabinetAddressCorrectionExemptReview find(long id);
    int insert(CabinetAddressCorrectionExemptReview cabinetAddressCorrectionExemptReview);
    int delete(long id);
}
