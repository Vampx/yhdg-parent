package cn.com.yusong.yhdg.webserver.service.basic;

import cn.com.yusong.yhdg.common.constant.ConstEnum;
import cn.com.yusong.yhdg.common.constant.Constant;
import cn.com.yusong.yhdg.common.domain.basic.*;
import cn.com.yusong.yhdg.common.domain.hdg.BatteryOrder;
import cn.com.yusong.yhdg.common.domain.hdg.PacketPeriodOrder;
import cn.com.yusong.yhdg.common.entity.AreaCache;
import cn.com.yusong.yhdg.common.entity.json.DataResult;
import cn.com.yusong.yhdg.common.entity.json.ExtResult;
import cn.com.yusong.yhdg.common.entity.pagination.Page;
import cn.com.yusong.yhdg.common.tool.memcached.MemCachedClient;
import cn.com.yusong.yhdg.common.utils.CodecUtils;
import cn.com.yusong.yhdg.webserver.persistence.basic.*;
import cn.com.yusong.yhdg.webserver.persistence.hdg.*;
import cn.com.yusong.yhdg.webserver.service.AbstractService;
import jxl.Sheet;
import jxl.Workbook;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

@Service
public class CustomerWhitelistService extends AbstractService {
    @Autowired
    CustomerWhitelistMapper customerWhitelistMapper;
    @Autowired
    PartnerMapper partnerMapper;


    public CustomerWhitelist find(Integer id) {
        CustomerWhitelist customerWhitelist = customerWhitelistMapper.find(id);
        return customerWhitelist;
    }

    public Page findPage(CustomerWhitelist search) {
        Page page = search.buildPage();
        page.setTotalItems(customerWhitelistMapper.findPageCount(search));
        search.setBeginIndex(page.getOffset());
        List<CustomerWhitelist> customerWhitelistList = customerWhitelistMapper.findPageResult(search);
        for (CustomerWhitelist customerWhitelist : customerWhitelistList) {
            Partner partner = partnerMapper.find(customerWhitelist.getPartnerId());
            if (partner != null) {
                customerWhitelist.setPartnerName(partner.getPartnerName());
            }
        }
        page.setResult(customerWhitelistList);
        return page;
    }


    @Transactional(rollbackFor = Throwable.class)
    public ExtResult create(CustomerWhitelist customerWhitelist) {
        if (customerWhitelist.getAgentId() == null) {
            customerWhitelist.setAgentId(0);
            customerWhitelist.setAgentName("无");
        } else {
            AgentInfo agentInfo = findAgentInfo(customerWhitelist.getAgentId());
            if (agentInfo != null) {
                customerWhitelist.setAgentName(agentInfo.getAgentName());
            }
        }
        boolean mobile = customerWhitelistMapper.findUnique(customerWhitelist.getMobile()) == 0;
        if (!mobile) {
            return ExtResult.failResult("该手机号码已存在");
        }
        customerWhitelist.setCreateTime(new Date());
        customerWhitelistMapper.insert(customerWhitelist);
        return ExtResult.successResult();
    }


    @Transactional(rollbackFor = Throwable.class)
    public ExtResult update(CustomerWhitelist customerWhitelist) {
        if (customerWhitelist.getAgentId() == null) {
            customerWhitelist.setAgentId(0);
            customerWhitelist.setAgentName("无");
        } else {
            AgentInfo agentInfo = findAgentInfo(customerWhitelist.getAgentId());
            if (agentInfo != null) {
                customerWhitelist.setAgentName(agentInfo.getAgentName());
            }
        }
        if (customerWhitelist.getAgentId() == 0) {
            customerWhitelist.setAgentName("无");
        }
        int total = customerWhitelistMapper.update(customerWhitelist);
        if (total == 1) {
            return ExtResult.successResult();
        } else {
            return ExtResult.failResult("修改失败！");
        }
    }

    @Transactional(rollbackFor = Throwable.class)
    public ExtResult delete(Integer id) {
        customerWhitelistMapper.delete(id);
        return ExtResult.successResult();
    }
}
