package cn.com.yusong.yhdg.agentserver.service.hdg;

import cn.com.yusong.yhdg.common.domain.hdg.CabinetAddressCorrectionExemptReview;
import cn.com.yusong.yhdg.common.entity.json.ExtResult;
import cn.com.yusong.yhdg.common.entity.pagination.Page;
import cn.com.yusong.yhdg.agentserver.persistence.hdg.CabinetAddressCorrectionExemptReviewMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
public class CabinetAddressCorrectionExemptReviewService {

    @Autowired
    CabinetAddressCorrectionExemptReviewMapper cabinetAddressCorrectionExemptReviewMapper;

    public Page findPage(CabinetAddressCorrectionExemptReview search) {
        Page page = search.buildPage();
        page.setTotalItems(cabinetAddressCorrectionExemptReviewMapper.findPageCount(search));
        search.setBeginIndex(page.getOffset());
        page.setResult(cabinetAddressCorrectionExemptReviewMapper.findPageResult(search));
        return page;
    }

    public CabinetAddressCorrectionExemptReview find(Long id) {
        return cabinetAddressCorrectionExemptReviewMapper.find(id);
    }

    @Transactional(rollbackFor = Throwable.class)
    public ExtResult create(CabinetAddressCorrectionExemptReview entity) {
        CabinetAddressCorrectionExemptReview exemptReview = cabinetAddressCorrectionExemptReviewMapper.find(entity.getId());
        if (exemptReview != null){
            return ExtResult.failResult("该人员已存在");
        }
        entity.setCreateTime(new Date());
        int total = cabinetAddressCorrectionExemptReviewMapper.insert(entity);
        if (total == 0) {
            return ExtResult.failResult("操作失败");
        }
        return ExtResult.successResult();
    }

    public ExtResult delete(long id) {
        int total = cabinetAddressCorrectionExemptReviewMapper.delete(id);
        if (total == 0) {
            return ExtResult.failResult("操作失败");
        }
        return ExtResult.successResult();
    }
}
