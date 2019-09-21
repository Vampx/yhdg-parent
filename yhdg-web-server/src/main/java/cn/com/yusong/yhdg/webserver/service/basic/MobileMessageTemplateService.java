package cn.com.yusong.yhdg.webserver.service.basic;

import cn.com.yusong.yhdg.common.domain.basic.MobileMessageTemplate;
import cn.com.yusong.yhdg.common.domain.basic.Partner;
import cn.com.yusong.yhdg.common.entity.json.ExtResult;
import cn.com.yusong.yhdg.common.entity.pagination.Page;
import cn.com.yusong.yhdg.webserver.persistence.basic.MobileMessageTemplateMapper;
import cn.com.yusong.yhdg.webserver.service.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MobileMessageTemplateService   extends AbstractService {

    @Autowired
    MobileMessageTemplateMapper mobileMessageTemplateMapper;

    public MobileMessageTemplate find(int partnerId, long id) {
        return mobileMessageTemplateMapper.find(partnerId, id);
    }

    public List<MobileMessageTemplate> findAll(){
        return mobileMessageTemplateMapper.findAll();
    }

    public Page findPage(MobileMessageTemplate search) {
        Page page = search.buildPage();
        page.setTotalItems(mobileMessageTemplateMapper.findPageCount(search));
        search.setBeginIndex(page.getOffset());
        List<MobileMessageTemplate> list = mobileMessageTemplateMapper.findPageResult(search);
        for (MobileMessageTemplate mobileMessageTemplate : list) {
            Partner partner = findPartner(mobileMessageTemplate.getPartnerId());
            if (partner != null) {
                mobileMessageTemplate.setPartnerName(partner.getPartnerName());
            }
        }
        page.setResult(list);
        return page;
    }

    public ExtResult update(MobileMessageTemplate entity) {
        if(mobileMessageTemplateMapper.update(entity) == 0) {
            return ExtResult.failResult("操作失败");
        }
        return ExtResult.successResult();
    }
}
