package cn.com.yusong.yhdg.agentserver.web.controller.security.yms;


import cn.com.yusong.yhdg.agentserver.service.yms.TerminalDownloadProgressService;
import cn.com.yusong.yhdg.agentserver.service.yms.TerminalOnlineService;
import cn.com.yusong.yhdg.agentserver.utils.AppUtils;
import cn.com.yusong.yhdg.agentserver.web.controller.security.basic.SecurityController;
import cn.com.yusong.yhdg.common.annotation.ViewModel;
import cn.com.yusong.yhdg.common.domain.yms.TerminalOnline;
import cn.com.yusong.yhdg.common.domain.yms.TerminalPlaylistProgressInfo;
import cn.com.yusong.yhdg.common.entity.json.PageResult;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping(value = "/security/yms/terminal_online")
public class TerminalOnlineController extends SecurityController {
    @Autowired
    TerminalOnlineService terminalOnlineService;
    @Autowired
    TerminalDownloadProgressService terminalDownloadProgressService;

//    @SecurityControl(limits = OperCodeConst.CODE_6_2_4_1)
    @RequestMapping(value = "index.htm")
    public void index(Model model){
//        model.addAttribute(MENU_CODE_NAME, AppConstEnum.Menu.MENU_06_02_04.getValue());
    }

    @RequestMapping("page.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public PageResult page(TerminalOnline terminalOnline){
        return PageResult.successResult(terminalOnlineService.findPage(terminalOnline));
    }

    @ViewModel(ViewModel.INNER_PAGE)
    @RequestMapping("view.htm")
    public void view(Model model, String id){
            model.addAttribute("id", id);
    }

    @ViewModel(ViewModel.INNER_PAGE)
    @RequestMapping("view_basic_info.htm")
    public String viewBasicInfo(Model model, String id){
        TerminalOnline entity = terminalOnlineService.find(id);
        if (entity == null){
            return  SEGMENT_RECORD_NOT_FOUND;
        } else {
            entity.setCpu(entity.getCpu() == null ? 0 : entity.getCpu());
            entity.setMemory(entity.getMemory() == null ? 0 : entity.getMemory());
            model.addAttribute("entity", entity);
        }
        return "/security/yms/terminal_online/view_basic_info";
    }

    @RequestMapping(value = "view_playlist_progress_info.htm")
    public void viewPlaylistProgressInfo(Model model, String id) throws IOException {
        String json = terminalDownloadProgressService.findPlaylistProgressInfo(id);
        if(StringUtils.isNotEmpty(json)) {
            TerminalPlaylistProgressInfo entity = (TerminalPlaylistProgressInfo) AppUtils.decodeJson(json, TerminalPlaylistProgressInfo.class);
            model.addAttribute("entity", entity);
        }
        model.addAttribute("id", id);
    }

    @RequestMapping(value = "page_playlist_progress_info.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public PageResult pagePlaylistProgressInfo(Model model, String id) throws IOException {
        String json = terminalDownloadProgressService.findPlaylistProgressInfo(id);
        if(StringUtils.isNotEmpty(json)) {
            TerminalPlaylistProgressInfo entity = (TerminalPlaylistProgressInfo) AppUtils.decodeJson(json, TerminalPlaylistProgressInfo.class);
            List<TerminalPlaylistProgressInfo.file> fileList = entity.getFileList();
            model.addAttribute("fileList", fileList);
            model.addAttribute("entity", entity);
            return PageResult.successResult(fileList);
        } else {
            return PageResult.emptyResult();
        }
    }

}
