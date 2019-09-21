package cn.com.yusong.yhdg.agentserver.web.controller.security.basic;

import cn.com.yusong.yhdg.agentserver.entity.SessionUser;
import cn.com.yusong.yhdg.agentserver.service.basic.PlatformAccountService;
import cn.com.yusong.yhdg.agentserver.service.basic.WeixinmpService;
import cn.com.yusong.yhdg.agentserver.service.basic.WithdrawService;
import cn.com.yusong.yhdg.common.annotation.SecurityControl;
import cn.com.yusong.yhdg.common.domain.basic.PlatformAccount;
import cn.com.yusong.yhdg.common.domain.basic.User;
import cn.com.yusong.yhdg.common.domain.basic.Withdraw;
import cn.com.yusong.yhdg.common.entity.json.ExtResult;
import cn.com.yusong.yhdg.common.entity.json.PageResult;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/security/basic/withdraw")
public class WithdrawController extends SecurityController {

    @Autowired
    private WithdrawService withdrawService;
    @Autowired
    private PlatformAccountService platformAccountService;
    @Autowired
    private WeixinmpService weixinmpService;

    @SecurityControl(limits = "basic.Withdraw:list")
    @RequestMapping("index.htm")
    public void index(Model model) {
        model.addAttribute(MENU_CODE_NAME, "basic.Withdraw:list");
        model.addAttribute("StatusEnum", Withdraw.Status.values());
        model.addAttribute("AccountTypeEnum", Withdraw.AccountType.values());
    }

    @SecurityControl(limits = "basic.Withdraw:list")
    @RequestMapping("agent_index.htm")
    public void agentIndex(Model model) {
        model.addAttribute(MENU_CODE_NAME, "basic.Withdraw:list");
        model.addAttribute("StatusEnum", Withdraw.Status.values());
        model.addAttribute("AccountTypeEnum", Withdraw.AccountType.values());
    }

    @ResponseBody
    @RequestMapping("page.htm")
    public PageResult page(HttpSession session, Withdraw search) {
        SessionUser sessionUser = getSessionUser(session);
        search.setBelongAgentId(sessionUser.getAgentId());
        return PageResult.successResult(withdrawService.findPage(search));
    }

    @RequestMapping(value = "audit.htm", method = RequestMethod.GET)
    public void audit(Model model, String id) {
        Withdraw withdraw = withdrawService.find(id);
        model.addAttribute("entity", withdraw);
    }

    @RequestMapping("/reset.htm")
    public void reset(Model model, String id) {
        Withdraw withdraw = withdrawService.find(id);
        model.addAttribute("entity", withdraw);
    }

    @RequestMapping("/view.htm")
    public void view(Model model, String id) {
        Withdraw withdraw = withdrawService.find(id);
        model.addAttribute("entity", withdraw);
    }

    @RequestMapping("/flow.htm")
    public void flow(Model model, String id) {
        Withdraw withdraw = withdrawService.find(id);
        model.addAttribute("entity", withdraw);
    }

    @RequestMapping("/view_basic.htm")
    public void viewBasic(Model model, String id) {
        Withdraw withdraw = withdrawService.find(id);
        model.addAttribute("entity", withdraw);
        model.addAttribute("TypeEnum", Withdraw.Type.values());
    }

    @RequestMapping("/view_transfer_log.htm")
    public void viewTransferLog(Model model, String id) {
        model.addAttribute("id", id);
    }

    @ResponseBody
    @RequestMapping(value = "audit.htm", method = RequestMethod.POST)
    public ExtResult updateStatus(HttpSession httpSession, String id, int status, String auditMemo) {
        SessionUser sessionUser = getSessionUser(httpSession);
        return withdrawService.audit(id, Withdraw.Status.TO_AUDIT.getValue(), status, sessionUser.getUsername(), auditMemo);
    }

    @ResponseBody
    @RequestMapping(value = "cancel.htm", method = RequestMethod.POST)
    public ExtResult cancel(HttpSession httpSession, String id, int status, String auditMemo) {
        SessionUser sessionUser = getSessionUser(httpSession);
        return withdrawService.audit(id, Withdraw.Status.WITHDRAW_NO.getValue(), status, sessionUser.getUsername(), auditMemo);
    }

    @ResponseBody
    @RequestMapping("update_reset.htm")
    public ExtResult updateReset(HttpSession httpSession, String id, String accountName, String weixinAccount, String alipayAccount, String wxOpenId) {
        SessionUser sessionUser = getSessionUser(httpSession);
        return withdrawService.reset(id, accountName, weixinAccount, alipayAccount, wxOpenId, sessionUser.getUsername());
    }

//    /**
//     * 平台提现页面
//     */
//    @RequestMapping("add.htm")
//    public void add(Model model, int partnerId) {
//        PlatformAccount platformAccount = platformAccountService.find(partnerId);
//        model.addAttribute("platformAccount", platformAccount);
//        model.addAttribute("AccountTypeEnum", Withdraw.AccountType.values());
//    }
//
//    /**
//     * 平台提现
//     */
//    @ResponseBody
//    @RequestMapping("create.htm")
//    public ExtResult create(HttpSession session, int partnerId, int accountType, String mpAccountName, String mpOpenId, String alipayAccountName, String alipayAccount, double dMoney) {
//        int money = (int) (dMoney * 100);
//        if (money <= 0) {
//            return ExtResult.failResult("提现金额有误");
//        }
//        PlatformAccount platformAccount = platformAccountService.find(partnerId);
//        if (platformAccount.getBalance() < money) {
//            return ExtResult.failResult("提现金额大于余额");
//        }
//        if (accountType == Withdraw.AccountType.WEIXIN_MP.getValue()) {
//            if(StringUtils.isEmpty(mpAccountName) || StringUtils.isEmpty(mpOpenId)){
//                return ExtResult.failResult("公众号账户为空");
//            }
//
//        } else if (accountType == Withdraw.AccountType.ALIPAY.getValue()) {
//            if(StringUtils.isEmpty(alipayAccountName) || StringUtils.isEmpty(alipayAccount)){
//                return ExtResult.failResult("支付宝账户为空");
//            }
//        }
//
//        platformAccount.setMpAccountName(mpAccountName);
//        platformAccount.setMpOpenId(mpOpenId);
//        platformAccount.setAlipayAccountName(alipayAccountName);
//        platformAccount.setAlipayAccount(alipayAccount);
//        platformAccountService.updateAccount(partnerId, mpAccountName, mpOpenId, alipayAccountName, alipayAccount);
//
//        return withdrawService.insert(accountType, money, 0, platformAccount, getSessionUser(session).getUsername());
//    }
}