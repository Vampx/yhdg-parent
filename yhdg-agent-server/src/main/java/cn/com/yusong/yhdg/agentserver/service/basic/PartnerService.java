package cn.com.yusong.yhdg.agentserver.service.basic;

import cn.com.yusong.yhdg.common.domain.basic.*;
import cn.com.yusong.yhdg.common.entity.json.ExtResult;
import cn.com.yusong.yhdg.common.entity.pagination.Page;
import cn.com.yusong.yhdg.common.tool.memcached.MemCachedClient;
import cn.com.yusong.yhdg.agentserver.persistence.basic.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class PartnerService {
    @Autowired
    MemCachedClient memCachedClient;
    @Autowired
    PartnerMapper partnerMapper;
    @Autowired
    PlatformAccountMapper platformAccountMapper;
    @Autowired
    AgentMapper agentMapper;
    @Autowired
    WeixinmpMapper weixinmpMapper;
    @Autowired
    AlipayfwMapper alipayfwMapper;

    public Partner find(int id) {
        return partnerMapper.find(id);
    }

    public Page findPage(Partner search) {
        Page page = search.buildPage();
        page.setTotalItems(partnerMapper.findPageCount(search));
        search.setBeginIndex(page.getOffset());
        page.setResult(partnerMapper.findPageResult(search));
        return page;
    }

    public List<Partner> findAll() {
        return partnerMapper.findAll();
    }

    public List<Partner> findList(Partner search) {
        return partnerMapper.findList(search);
    }

    public ExtResult create(Partner partner, List<String> sqlList) {
        partner.setCreateTime(new Date());
        partnerMapper.insert(partner);

        PlatformAccount platformAccount = new PlatformAccount();
        platformAccount.setId(partner.getId());
        platformAccount.setPartnerName(partner.getPartnerName());
        platformAccount.setBalance(0);
        platformAccount.setCreateTime(new Date());
        platformAccountMapper.insert(platformAccount);

        String line = String.format("%d", partner.getId());
        for (String sql : sqlList) {
            partnerMapper.insertSql(sql.replace("0/*partner_id*/", line));
        }

        return ExtResult.successResult(platformAccount.getId()+"");
    }

    public ExtResult update(Partner partner) {
        partnerMapper.update(partner);

        return ExtResult.successResult();
    }

    public ExtResult delete(int id) {
        //已绑定运营商无法删除
        List<Agent> agentList = agentMapper.findByPartnerId(id);
        if (agentList.size() > 0) {
            return ExtResult.failResult("该商户已绑定运营商，无法删除");
        }

        //已经绑定生活号或公众号不能删除
        List<Weixinmp> weixinmpList = weixinmpMapper.findByPartnerId(id);
        if (weixinmpList.size() > 0) {
            return ExtResult.failResult("该商户已绑定公众号，无法删除");
        }
        List<Alipayfw> alipayfwList = alipayfwMapper.findByPartnerId(id);
        if (alipayfwList.size() > 0) {
            return ExtResult.failResult("该商户已绑定生活号，无法删除");
        }
        partnerMapper.delete(id);
        //同时删除平台账户信息
        platformAccountMapper.delete(id);

        return ExtResult.successResult();
    }

}
