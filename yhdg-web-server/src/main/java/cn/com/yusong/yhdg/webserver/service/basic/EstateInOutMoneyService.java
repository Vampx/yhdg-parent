package cn.com.yusong.yhdg.webserver.service.basic;

import cn.com.yusong.yhdg.common.domain.basic.EstateInOutMoney;
import cn.com.yusong.yhdg.common.domain.hdg.Estate;
import cn.com.yusong.yhdg.common.entity.pagination.Page;
import cn.com.yusong.yhdg.webserver.persistence.basic.EstateInOutMoneyMapper;
import cn.com.yusong.yhdg.webserver.persistence.hdg.EstateMapper;
import cn.com.yusong.yhdg.webserver.service.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EstateInOutMoneyService extends AbstractService{

    @Autowired
    EstateInOutMoneyMapper estateInOutMoneyMapper;
    @Autowired
    EstateMapper estateMapper;


    public Page findPage(EstateInOutMoney search) {
        Page page = search.buildPage();
        page.setTotalItems(estateInOutMoneyMapper.findPageCount(search));
        search.setBeginIndex(page.getOffset());
        List<EstateInOutMoney> pageResult = estateInOutMoneyMapper.findPageResult(search);
        for (EstateInOutMoney estateInOutMoney : pageResult) {
            if (estateInOutMoney.getEstateId() != null) {
                Estate estate = estateMapper.find(estateInOutMoney.getEstateId());
                if (estate != null) {
                    estateInOutMoney.setEstateName(estate.getEstateName());
                    estateInOutMoney.setTel(estate.getTel());
                    estateInOutMoney.setLinkname(estate.getLinkname());
                }
            }
        }
        page.setResult(pageResult);
        return page;
    }

    public EstateInOutMoney find(Long id) {
        return estateInOutMoneyMapper.find(id);
    }
}
