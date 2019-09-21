package cn.com.yusong.yhdg.webserver.service.basic;

import cn.com.yusong.yhdg.common.domain.basic.Feedback;
import cn.com.yusong.yhdg.common.domain.basic.Partner;
import cn.com.yusong.yhdg.common.entity.pagination.Page;
import cn.com.yusong.yhdg.webserver.persistence.basic.FeedbackMapper;
import cn.com.yusong.yhdg.webserver.persistence.basic.PartnerMapper;
import cn.com.yusong.yhdg.webserver.service.AbstractService;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
public class FeedbackService extends AbstractService {
    @Autowired
    FeedbackMapper feedbackMapper;
    @Autowired
    PartnerMapper partnerMapper;

    public Feedback find(long id) {
        return feedbackMapper.find(id);
    }

    public Page findPage(Feedback search) {
        Page page = search.buildPage();
        page.setTotalItems(feedbackMapper.findPageCount(search));
        search.setBeginIndex(page.getOffset());
        List<Feedback> feedbackList = feedbackMapper.findPageResult(search);
        for (Feedback feedback : feedbackList) {
            Partner partner = partnerMapper.find(feedback.getPartnerId());
            if (partner != null) {
                feedback.setPartnerName(partner.getPartnerName());
            }
        }
        page.setResult(feedbackList);
        return page;
    }

    public int delete(long id) {
        return feedbackMapper.delete(id);
    }

}
