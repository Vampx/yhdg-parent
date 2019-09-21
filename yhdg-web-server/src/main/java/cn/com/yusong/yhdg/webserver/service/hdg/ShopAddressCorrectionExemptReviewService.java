package cn.com.yusong.yhdg.webserver.service.hdg;

import cn.com.yusong.yhdg.common.domain.hdg.ShopAddressCorrectionExemptReview;
import cn.com.yusong.yhdg.common.entity.json.ExtResult;
import cn.com.yusong.yhdg.common.entity.pagination.Page;
import cn.com.yusong.yhdg.webserver.persistence.hdg.ShopAddressCorrectionExemptReviewMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
public class ShopAddressCorrectionExemptReviewService {

    @Autowired
    ShopAddressCorrectionExemptReviewMapper shopAddressCorrectionExemptReviewMapper;

    public Page findPage(ShopAddressCorrectionExemptReview search) {
        Page page = search.buildPage();
        page.setTotalItems(shopAddressCorrectionExemptReviewMapper.findPageCount(search));
        search.setBeginIndex(page.getOffset());
        page.setResult(shopAddressCorrectionExemptReviewMapper.findPageResult(search));
        return page;
    }

    public ShopAddressCorrectionExemptReview find(Long id) {
        return shopAddressCorrectionExemptReviewMapper.find(id);
    }

    @Transactional(rollbackFor = Throwable.class)
    public ExtResult create(ShopAddressCorrectionExemptReview entity) {
        ShopAddressCorrectionExemptReview exemptReview = shopAddressCorrectionExemptReviewMapper.find(entity.getId());
        if (exemptReview != null){
            return ExtResult.failResult("该人员已存在");
        }
        entity.setCreateTime(new Date());
        int total = shopAddressCorrectionExemptReviewMapper.insert(entity);
        if (total == 0) {
            return ExtResult.failResult("操作失败");
        }
        return ExtResult.successResult();
    }

    public ExtResult delete(long id) {
        int total = shopAddressCorrectionExemptReviewMapper.delete(id);
        if (total == 0) {
            return ExtResult.failResult("操作失败");
        }
        return ExtResult.successResult();
    }
}
