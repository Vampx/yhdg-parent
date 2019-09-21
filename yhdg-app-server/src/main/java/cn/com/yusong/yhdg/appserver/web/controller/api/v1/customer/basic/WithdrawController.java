package cn.com.yusong.yhdg.appserver.web.controller.api.v1.customer.basic;

import cn.com.yusong.yhdg.appserver.config.TokenCache;
import cn.com.yusong.yhdg.appserver.constant.RespCode;
import cn.com.yusong.yhdg.appserver.entity.result.RestResult;
import cn.com.yusong.yhdg.appserver.service.basic.CustomerService;
import cn.com.yusong.yhdg.appserver.service.basic.SystemConfigService;
import cn.com.yusong.yhdg.appserver.service.basic.WithdrawService;
import cn.com.yusong.yhdg.appserver.web.controller.api.v1.ApiController;
import cn.com.yusong.yhdg.common.constant.ConstEnum;
import cn.com.yusong.yhdg.common.domain.basic.Customer;
import cn.com.yusong.yhdg.common.domain.basic.Withdraw;
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

@Controller("api_v1_customer_basic_withdraw")
@RequestMapping(value = "/api/v1/customer/basic/withdraw")
public class WithdrawController extends ApiController {
    @Autowired
    WithdrawService withdrawService;
    @Autowired
    CustomerService customerService;
    @Autowired
    SystemConfigService systemConfigService;

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class CreateParam {
        public Integer accountType;
        public String payPassword;
        public Integer money;
        public String alipayAccount;
        public String wxOpenId;
    }

    /*
    * 50-提现提交
    * */

    @ResponseBody
    @RequestMapping(value = "/create.htm")
    public RestResult create(@Valid @RequestBody CreateParam param) {
        long customerId = getTokenData().customerId;
        Customer customer = customerService.find(customerId);
        if (customer == null) {
            return RestResult.result(RespCode.CODE_2.getValue(), "客户不存在");
        } else {
            if (param.accountType == 2 && StringUtils.isEmpty(param.alipayAccount)) {
                return RestResult.result(RespCode.CODE_2.getValue(), "请输入支付宝账户");
            }
            if (param.accountType == 3 && StringUtils.isEmpty(param.wxOpenId)) {
                return RestResult.result(RespCode.CODE_2.getValue(), "请输入微信OpenId");
            }

            if (!param.payPassword.equals(customer.getPayPassword())) {
                return RestResult.result(RespCode.CODE_9.getValue(), "密码错误");
            }

            if (customer.getBalance() < param.money) {
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
                String id = withdrawService.insert(param.accountType, param.alipayAccount, param.wxOpenId, param.money, serviceMoney, customer);
                Map data = new HashMap();
                data.put("id", id);
                data.put("nickname", customer.getNickname());
                return RestResult.dataResult(RespCode.CODE_0.getValue(), null, data);
            } catch (BalanceNotEnoughException exception) {
                return RestResult.result(RespCode.CODE_2.getValue(), "余额不足");
            }


        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class InfoParam {
        public String id;
    }

    /*
    * 51-查询账单详情
    * */

    @ResponseBody
    @RequestMapping(value = "/info.htm")
    public RestResult info(@Valid @RequestBody InfoParam param) {
        Withdraw withdraw = withdrawService.find(param.id);
        if (withdraw == null) {
            return RestResult.result(RespCode.CODE_2.getValue(), "记录不存在");
        }

        NotNullMap map = new NotNullMap();
        map.put("id", withdraw.getId());
        map.put("money", withdraw.getMoney());
        map.put("accountType", withdraw.getAccountType());
        map.put("serviceMoney", withdraw.getServiceMoney());
        map.put("status", withdraw.getStatus());
        map.putDateTime("handleTime", withdraw.getHandleTime());
        map.putDateTime("createTime", withdraw.getCreateTime());
        map.put("auditUser", withdraw.getAuditUser());
        map.putDateTime("auditTime", withdraw.getAuditTime());
        map.put("auditMemo", withdraw.getAuditMemo());
        return RestResult.dataResult(RespCode.CODE_0.getValue(), null, map);
    }


    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ListParam {
        public int offset;
        public int limit;
    }

    /*
     * 53-查询提现记录
     * */

    @ResponseBody
    @RequestMapping(value = "/list.htm")
    public RestResult list(@Valid @RequestBody ListParam param) {
        TokenCache.Data tokenData = getTokenData();

        List<Map> data = new ArrayList<Map>();
        List<Withdraw> withdrawList = withdrawService.findByCustomer(Withdraw.Type.CUSTOMER.getValue(), tokenData.customerId, param.offset, param.limit);
        for (Withdraw withdraw : withdrawList) {
            NotNullMap map = new NotNullMap();
            map.put("id", withdraw.getId());
            map.put("money", withdraw.getMoney());
            map.put("accountType", withdraw.getAccountType());
            map.put("serviceMoney", withdraw.getServiceMoney());
            map.put("status", withdraw.getStatus());
            map.putDateTime("handleTime", withdraw.getHandleTime());
            map.putDateTime("createTime", withdraw.getCreateTime());
            map.put("auditUser", withdraw.getAuditUser());
            map.putDateTime("auditTime", withdraw.getAuditTime());
            map.put("auditMemo", withdraw.getAuditMemo());
            data.add(map);
        }

        return RestResult.dataResult(RespCode.CODE_0.getValue(), null, data);
    }
}
