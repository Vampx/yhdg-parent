package cn.com.yusong.yhdg.agentserver.web.controller.security.yms;


import cn.com.yusong.yhdg.agentserver.service.hdg.CabinetService;
import cn.com.yusong.yhdg.agentserver.service.yms.PlaylistService;
import cn.com.yusong.yhdg.agentserver.service.yms.TerminalPropertyService;
import cn.com.yusong.yhdg.agentserver.service.yms.TerminalService;
import cn.com.yusong.yhdg.agentserver.service.yms.TerminalStrategyService;
import cn.com.yusong.yhdg.agentserver.web.controller.security.basic.SecurityController;
import cn.com.yusong.yhdg.common.annotation.SecurityControl;
import cn.com.yusong.yhdg.common.annotation.ViewModel;
import cn.com.yusong.yhdg.common.domain.hdg.Cabinet;
import cn.com.yusong.yhdg.common.domain.yms.Playlist;
import cn.com.yusong.yhdg.common.domain.yms.Terminal;
import cn.com.yusong.yhdg.common.entity.json.ExtResult;
import cn.com.yusong.yhdg.common.entity.json.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;


@Controller("ymsTerminalController")
@RequestMapping(value = "/security/yms/terminal")
public class TerminalController extends SecurityController {

    @Autowired
    TerminalService terminalService;
    @Autowired
    TerminalPropertyService terminalPropertyService;
    @Autowired
    TerminalStrategyService terminalStrategyService;
    @Autowired
    PlaylistService playlistService;
    @Autowired
    CabinetService cabinetService;

    @SecurityControl(limits = "yms.Terminal:list")
    @RequestMapping("index.htm")
    public void index(Model model) {
        model.addAttribute(MENU_CODE_NAME, "yms.Terminal:list");
    }

    @RequestMapping("page.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public PageResult page(Terminal terminal){
        return PageResult.successResult(terminalService.findPage(terminal));
    }

    @RequestMapping(value = "add.htm")
    public void add(Model model, HttpSession httpSession) {
        int agentId = getSessionUser(httpSession).getAgentId();
        model.addAttribute("strategyList", terminalStrategyService.findByAgent(agentId));
    }

    @ViewModel(ViewModel.INNER_PAGE)
    @RequestMapping("edit.htm")
    public String edit(String id, Model model){
        Terminal terminal = terminalService.find(id);
        if (terminal == null){
            return SEGMENT_RECORD_NOT_FOUND;
        }
        model.addAttribute("id",id);
        return "/security/yms/terminal/edit";
    }

    @RequestMapping("edit_basic_info.htm")
    public String editBasicInfo(Model model, String id){
        Terminal entity = terminalService.find(id);
        if (entity == null){
            return SEGMENT_RECORD_NOT_FOUND;
        }else {
            model.addAttribute("entity", entity);
            Cabinet cabinet = cabinetService.findByTerminalId(entity.getId());
            if (cabinet != null) {
                Integer agentId = cabinet.getAgentId();
                model.addAttribute("strategyList", terminalStrategyService.findByAgent(agentId == null ? 0 : agentId));
            }
        }
        return "/security/yms/terminal/edit_basic_info";
    }

    @RequestMapping(value = "edit_terminal_route.htm")
    public String editTerminalRoute(Model model,String id){
        Terminal entity = terminalService.find(id);
        if (entity == null){
            return SEGMENT_RECORD_NOT_FOUND;
        }else {
            model.addAttribute("entity", entity);
        }
        return "/security/yms/terminal/edit_terminal_route";
    }

    @RequestMapping(value = "edit_terminal_property.htm")
    public String editTerminalProperty(Model model, String id) {
        Terminal entity = terminalService.find(id);
        if(entity == null) {
            return SEGMENT_RECORD_NOT_FOUND;
        } else {
            model.addAttribute("entity", entity);
            model.addAttribute("list", terminalPropertyService.findByTerminal(id));
        }
        return "/security/yms/terminal/edit_terminal_property";
    }

    @RequestMapping(value = "view.htm")
    public void view(Model model,String id,Integer item){
        model.addAttribute("id", id);
        model.addAttribute("item", item);
    }

    @ViewModel(ViewModel.INNER_PAGE)
    @RequestMapping("view_basic_info.htm")
    public String viewBasicInfo(Model model,String id,HttpSession httpSession ){
        Terminal entity = terminalService.find(id);
        if (entity == null){
            return SEGMENT_RECORD_NOT_FOUND;
        } else {
            model.addAttribute("entity", entity);
            Cabinet cabinet = cabinetService.findByTerminalId(entity.getId());
            if (cabinet != null) {
                Integer agentId = cabinet.getAgentId();
                model.addAttribute("strategyList", terminalStrategyService.findByAgent(agentId == null ? 0 : agentId));
            }
        }
        return "/security/yms/terminal/view_basic_info";
    }

    @ViewModel(ViewModel.INNER_PAGE)
    @RequestMapping("select_terminal.htm")
    public String selectTerminal(Integer playlistId, Model model){
        Playlist entity = playlistService.find(playlistId);
        if (entity == null){
            return SEGMENT_RECORD_NOT_FOUND;
        } else {
            model.addAttribute("agentId", entity.getAgentId());
        }
        return "/security/yms/terminal/select_terminal";
    }

    @RequestMapping(value = "view_terminal.htm")
    public void viewTerminal(Model model,Integer routeId) {
        model.addAttribute("routeId", routeId);
    }

    @RequestMapping("view_terminal_property.htm")
    public void viewTerminalProperty(Model model, int id){
        model.addAttribute("id",id);
    }

    @RequestMapping("update_basic_info.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public ExtResult updateBasicInfo(Terminal terminal){
        ExtResult result = terminalService.updateBasicInfo(terminal);
        return result;
    }

    @RequestMapping("delete.htm")
    @ResponseBody
    @ViewModel(ViewModel.JSON)
    public ExtResult delete(String id) {
        return terminalService.delete(id);
    }

    @RequestMapping("update.htm")
    @ResponseBody
    @ViewModel(ViewModel.JSON)
    public ExtResult update(String[] terminalIds, Integer playlistId) {
        if(terminalIds.length <= 0) {
            return ExtResult.failResult("请选择终端");
        }
        if(playlistId == null || playlistId <= 0) {
            return ExtResult.failResult("播放列表不存在");
        }
        terminalService.update(terminalIds, playlistId);

        return ExtResult.successResult();
    }

    @RequestMapping("update_property.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public ExtResult updateProperty(String id, int[] active, String[] property, String[] value) {
        terminalPropertyService.insert(id, active, property, value);
        return ExtResult.successResult();
    }

    @RequestMapping("create.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public ExtResult create(Terminal terminal) {
        return terminalService.insert(terminal);
    }


    @RequestMapping("select_playlists.htm")
    public String selectPlaylists() {
        return "/security/yms/terminal/select_playlists";
    }

    @RequestMapping("select_playlist.htm")
    public String selectPlaylist(Model model, String id) {
        Terminal entity = terminalService.find(id);
        if(entity == null) {
            return SEGMENT_RECORD_NOT_FOUND;
        }
        Playlist playlist = null;
        if (entity.getPlaylistId() != null) {
            playlist = playlistService.find(entity.getPlaylistId());
        }
        model.addAttribute("playlist",playlist);
        return "/security/yms/terminal/select_playlist";
    }

    @RequestMapping("relevance_cabinet.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public ExtResult relevanceCabinet(String cabinetId, String terminalId) {
        return terminalService.relevanceCabinet(cabinetId, terminalId);
    }

    @RequestMapping("delete_playlist.htm")
    @ResponseBody
    @ViewModel(ViewModel.JSON)
    public ExtResult deletePlaylist(String terminalId) {
        return terminalService.deletePlaylist(terminalId);
    }

}
