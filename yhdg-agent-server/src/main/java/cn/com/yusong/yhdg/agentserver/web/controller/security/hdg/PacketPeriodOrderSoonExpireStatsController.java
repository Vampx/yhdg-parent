package cn.com.yusong.yhdg.agentserver.web.controller.security.hdg;

import cn.com.yusong.yhdg.agentserver.entity.SessionUser;
import cn.com.yusong.yhdg.agentserver.service.hdg.PacketPeriodOrderService;
import cn.com.yusong.yhdg.agentserver.service.hdg.PacketPeriodOrderSoonExpireStatsService;
import cn.com.yusong.yhdg.agentserver.utils.StatsXlsUtils;
import cn.com.yusong.yhdg.agentserver.web.controller.security.basic.SecurityController;
import cn.com.yusong.yhdg.common.annotation.SecurityControl;
import cn.com.yusong.yhdg.common.annotation.ViewModel;
import cn.com.yusong.yhdg.common.constant.ConstEnum;
import cn.com.yusong.yhdg.common.domain.hdg.PacketPeriodOrder;
import cn.com.yusong.yhdg.common.entity.json.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

@Controller
@RequestMapping("/security/hdg/packet_period_order_soon_expire_stats")
public class PacketPeriodOrderSoonExpireStatsController extends SecurityController {
    @Autowired
    PacketPeriodOrderService packetPeriodOrderService;
    @Autowired
    PacketPeriodOrderSoonExpireStatsService packetPeriodOrderSoonExpireStatsService;

    @SecurityControl(limits = "hdg.PacketPeriodOrderSoonExpireStats:list")
    @RequestMapping("index.htm")
    public void index(Model model){
        model.addAttribute(MENU_CODE_NAME, "hdg.PacketPeriodOrderSoonExpireStats:list");
        model.addAttribute("expireDate",PacketPeriodOrder.ExpireDate.values());
        model.addAttribute("isBattery",PacketPeriodOrder.IsBattery.values());
        model.addAttribute("isDeposit", ConstEnum.Flag.values());
    }
    @RequestMapping("page.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public PageResult page(PacketPeriodOrder search,int expireDate, HttpSession session) {
        SessionUser sessionUser = getSessionUser(session);
        if(search.getAgentId()==null){
            search.setAgentId(sessionUser.getAgentId());
        }
        return PageResult.successResult(packetPeriodOrderService.findBySoonExpiresPage(search, expireDate));
    }

    @RequestMapping("export_excel.htm")
    public void exportExcel(Integer agentId, String cabinetId,int expireDate,String isBattery, String isDeposit, HttpServletRequest request, HttpServletResponse response) throws IOException {
        File writeFile = new File(String.format("%s/%s", getAppConfig().appDir.getPath(),"要到期包时总统计.xls"));
        OutputStream os = new FileOutputStream(writeFile);
        PacketPeriodOrder box = new PacketPeriodOrder();
        box.setAgentId(agentId);
        box.setCabinetId(cabinetId);
        box.setIsBattery(isBattery);
        box.setIsDeposit(isDeposit);
        StatsXlsUtils.writeSoonExpierPacket(packetPeriodOrderSoonExpireStatsService.findBySoonExpiresAll(box,expireDate), os);
        downloadSupport(writeFile, request, response, "要到期包时总统计.xls");
    }

}
