package cn.com.yusong.yhdg.agentserver.web.controller.security.yms;

import cn.com.yusong.yhdg.agentserver.entity.SessionUser;
import cn.com.yusong.yhdg.agentserver.service.basic.AgentService;
import cn.com.yusong.yhdg.agentserver.service.yms.MaterialService;
import cn.com.yusong.yhdg.agentserver.service.yms.PlaylistDetailMaterialService;
import cn.com.yusong.yhdg.agentserver.service.yms.PlaylistDetailService;
import cn.com.yusong.yhdg.agentserver.service.yms.PlaylistService;
import cn.com.yusong.yhdg.agentserver.web.controller.security.basic.SecurityController;
import cn.com.yusong.yhdg.common.annotation.SecurityControl;
import cn.com.yusong.yhdg.common.annotation.ViewModel;
import cn.com.yusong.yhdg.common.constant.ConstEnum;
import cn.com.yusong.yhdg.common.domain.yms.Playlist;
import cn.com.yusong.yhdg.common.entity.json.DataResult;
import cn.com.yusong.yhdg.common.entity.json.ExtResult;
import cn.com.yusong.yhdg.common.entity.json.PageResult;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping(value = "/security/yms/playlist")
public class PlaylistController extends SecurityController {

    static Logger log = LogManager.getLogger(PlaylistController.class);

    @Autowired
    PlaylistService playlistService;
    @Autowired
    AgentService agentService;
    @Autowired
    PlaylistDetailService playlistDetailService;
    @Autowired
    PlaylistDetailMaterialService playlistAreaMaterialService;
    @Autowired
    MaterialService materialService;

    @SecurityControl(limits = "yms.playlist:list")
    @RequestMapping(value = "index.htm")
    public void index(Model model) {
        model.addAttribute(MENU_CODE_NAME, "yms.playlist:list");
    }

    @RequestMapping(value = "page.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public PageResult page(Playlist search) {
//        if (search.getAgentId() == null) {
//            Page page = search.buildPage();
//            page.setResult(new ArrayList<Playlist>());
//            return PageResult.successResult(page);
//        }

        return PageResult.successResult(playlistService.findPage(search));
    }

    @RequestMapping("findByAgent.htm")
    public void findByAgent(int agentId, HttpServletResponse response) throws IOException {
        response.setContentType(ConstEnum.ContentType.JSON.getValue());
        List<Playlist> playlists = playlistService.findByAgent(agentId);
        ObjectMapper objectMapper = new ObjectMapper();
        String postBody = objectMapper.writeValueAsString(playlists);
        response.getWriter().write(postBody);
    }

    @RequestMapping("add.htm")
    public void add() {
    }

    /**
     * 新建播放列表
     * @param entity
     * @return
     */
    @RequestMapping("create.htm")
    @ResponseBody
    @ViewModel(ViewModel.JSON)
    public DataResult create(Playlist entity, HttpSession httpSession) {
        SessionUser sessionUser = getSessionUser(httpSession);
        String username = sessionUser.getUsername();
        playlistService.insert(entity, username);
        return DataResult.successResult(entity.getId());
    }


    @ViewModel(ViewModel.INNER_PAGE)
    @RequestMapping("playlist_terminal.htm")
    public String playlistTerminal(){
        return "/security/yms/playlist/playlist_terminal";
    }


    @RequestMapping("delete.htm")
    @ResponseBody
    @ViewModel(ViewModel.JSON)
    public ExtResult delete(int id) {
        return playlistService.delete(id);
    }


}
