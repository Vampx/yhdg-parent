package cn.com.yusong.yhdg.appserver.service.hdg;

import cn.com.yusong.yhdg.appserver.persistence.hdg.CabinetAddressCorrectionExemptReviewMapper;
import cn.com.yusong.yhdg.appserver.service.AbstractService;
import cn.com.yusong.yhdg.common.domain.hdg.CabinetAddressCorrectionExemptReview;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by ruanjian5 on 2017/12/15.
 */
@Service
public class CabinetAddressCorrectionExemptReviewService  extends AbstractService{

    @Autowired
    CabinetAddressCorrectionExemptReviewMapper reviewMapper;

    public CabinetAddressCorrectionExemptReview find(long id){
        return  reviewMapper.find(id);
    }

}
