package cn.com.yusong.yhdg.agentappserver.web.controller.api.v1.agentcompany.basic;

import cn.com.yusong.yhdg.agentappserver.config.TokenCache;
import cn.com.yusong.yhdg.agentappserver.constant.RespCode;
import cn.com.yusong.yhdg.agentappserver.entity.result.RestResult;
import cn.com.yusong.yhdg.agentappserver.service.basic.*;
import cn.com.yusong.yhdg.agentappserver.service.hdg.ShopService;
import cn.com.yusong.yhdg.agentappserver.web.controller.api.v1.ApiController;
import cn.com.yusong.yhdg.common.constant.ConstEnum;
import cn.com.yusong.yhdg.common.domain.basic.AgentCompany;
import cn.com.yusong.yhdg.common.domain.basic.Withdraw;
import cn.com.yusong.yhdg.common.domain.hdg.Shop;
import cn.com.yusong.yhdg.common.entity.NotNullMap;
import cn.com.yusong.yhdg.common.exception.BalanceNotEnoughException;
import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller("agent_api_v1_agentcompany_basic_withdraw")
@RequestMapping(value = "/agent_api/v1/agentcompany/basic/withdraw")
public class WithdrawController extends ApiController {
    @Autowired
    WithdrawService withdrawService;
    @Autowired
    CustomerService customerService;
    @Autowired
    ShopService shopService;
    @Autowired
    AgentService agentService;
    @Autowired
    SystemConfigService systemConfigService;
    @Autowired
    AgentCompanyService agentCompanyService;

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class CreateParam {
        public Integer accountType;
        public String payPassword;
        public Integer money;
        public String alipayAccount;
    }

    /**
     * 16-运营公司提现提交
     * */
    @ResponseBody
    @RequestMapping(value = "/agent_company_withdraw.htm")
    public RestResult agentCompanyWithdraw(@Valid @RequestBody CreateParam param) {
        TokenCache.Data tokenData = getTokenData();
        AgentCompany agentCompany = agentCompanyService.find(tokenData.agentCompanyId);
        if (agentCompany == null) {
            return RestResult.result(RespCode.CODE_2.getValue(), "运营公司不存在");
        }

        if (!param.payPassword.equals(agentCompany.getPayPassword())) {
            return RestResult.result(RespCode.CODE_9.getValue(), "密码错误");
        }

        if (agentCompany.getBalance() < param.money) {
            return RestResult.result(RespCode.CODE_2.getValue(), "余额不足");
        }
        double ratio = 0;
        String withdrawRatio = systemConfigService.findConfigValue(ConstEnum.SystemConfigKey.WITHDRAW_RATIO.getValue());
        if (StringUtils.isNotEmpty(withdrawRatio)) {
            ratio = Double.parseDouble(withdrawRatio);
        }
        int serviceMoney = (int) Math.round(param.money * ratio);
        if (param.money - serviceMoney < 100) {
            return RestResult.result(RespCode.CODE_2.getValue(), "扣除手续费后金额不能小于1元");
        }

        try {
            String id = withdrawService.agentCompanyWithdraw(param.accountType, param.alipayAccount, param.money, serviceMoney, agentCompany);
            Map data = new HashMap();
            data.put("id", id);
            return RestResult.dataResult(RespCode.CODE_0.getValue(), null, data);
        } catch (BalanceNotEnoughException exception) {
            return RestResult.result(RespCode.CODE_2.getValue(), "余额不足");
        }

    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ListParam {
        public int offset;
        public int limit;
    }

    /**
     * 17-查询运营公司账单
     * */
    @ResponseBody
    @RequestMapping(value = "/agent_company_withdraw_list.htm")
    public RestResult agentCompanyWithdrawList(@Valid @RequestBody ListParam param) {
        TokenCache.Data tokenData = getTokenData();
        AgentCompany agentCompany = agentCompanyService.find(tokenData.agentCompanyId);
        if (agentCompany == null) {
            return RestResult.result(RespCode.CODE_2.getValue(), "运营公司不存在");
        }
        List<Map> data = new ArrayList<Map>();
        List<Withdraw> withdrawList = withdrawService.findByAgentCompany(Withdraw.Type.AGENT_COMPANY.getValue(), tokenData.agentCompanyId, param.offset, param.limit);
        for (Withdraw withdraw : withdrawList) {
            NotNullMap line = new NotNullMap();
            line.put("id", withdraw.getId());
            line.put("accountType", withdraw.getAccountType());
            line.put("money", withdraw.getMoney());
            line.put("realMoney", withdraw.getRealMoney());
            line.put("serviceMoney", withdraw.getServiceMoney());
            line.put("status", withdraw.getStatus());
            line.putDateTime("createTime", withdraw.getCreateTime());
            line.putDateTime("auditTime", withdraw.getAuditTime());
            line.putDateTime("handleTime", withdraw.getHandleTime());
            data.add(line);
        }

        return RestResult.dataResult(RespCode.CODE_0.getValue(), null, data);
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class InfoParam {
        public String id;
    }

    /**
    * 18-查询运营公司账单详情
    * */
    @ResponseBody
    @RequestMapping(value = "/agent_company_withdraw_info.htm")
    public RestResult agentCompanyWithdrawInfo(@Valid @RequestBody InfoParam param) {
        Withdraw withdraw = withdrawService.find(param.id);
        NotNullMap map = new NotNullMap();
        if (withdraw != null) {
            map.put("money", withdraw.getMoney());
            map.put("accountType", withdraw.getAccountType());
            map.put("serviceMoney", withdraw.getServiceMoney());
            map.put("id", withdraw.getId());
            map.put("operator", withdraw.getAuditUser());
            map.putDateTime("handleTime", withdraw.getHandleTime());
            map.put("operatorMemo", withdraw.getAuditMemo());
            map.put("status", withdraw.getStatus());
            map.putDateTime("createTime", withdraw.getCreateTime());
        }
        return RestResult.dataResult(RespCode.CODE_0.getValue(), null, map);
    }


}
