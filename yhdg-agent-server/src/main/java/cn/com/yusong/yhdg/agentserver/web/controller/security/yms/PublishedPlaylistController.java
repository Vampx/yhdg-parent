package cn.com.yusong.yhdg.agentserver.web.controller.security.yms;

import cn.com.yusong.yhdg.agentserver.constant.AppConstEnum;
import cn.com.yusong.yhdg.agentserver.constant.OperCodeConst;
import cn.com.yusong.yhdg.agentserver.entity.SessionUser;
import cn.com.yusong.yhdg.agentserver.service.yms.PublishedMaterialService;
import cn.com.yusong.yhdg.agentserver.service.yms.PublishedPlaylistDetailService;
import cn.com.yusong.yhdg.agentserver.service.yms.PublishedPlaylistService;
import cn.com.yusong.yhdg.agentserver.web.controller.security.basic.SecurityController;
import cn.com.yusong.yhdg.common.annotation.SecurityControl;
import cn.com.yusong.yhdg.common.annotation.ViewModel;
import cn.com.yusong.yhdg.common.domain.yms.PublishedPlaylist;
import cn.com.yusong.yhdg.common.domain.yms.PublishedPlaylistDetail;
import cn.com.yusong.yhdg.common.entity.json.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.List;


@Controller
@RequestMapping(value = "/security/yms/published_playlist")
public class PublishedPlaylistController extends SecurityController {

    @Autowired
    PublishedPlaylistService publishedPlaylistService;
    @Autowired
    PublishedPlaylistDetailService publishedPlaylistDetailService;
    @Autowired
    PublishedMaterialService publishedMaterialService;

    @SecurityControl(limits = "yms.PublishedPlaylist:list")
    @RequestMapping(value = "index.htm")
    public void index(Model model) {
        model.addAttribute(MENU_CODE_NAME, "yms.PublishedPlaylist:list");
    }

    @RequestMapping(value = "page.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public PageResult page(PublishedPlaylist search) {
        return PageResult.successResult(publishedPlaylistService.findPage(search));
    }

    @ViewModel(ViewModel.INNER_PAGE)
    @RequestMapping("view.htm")
    public String view(Model model, int id, HttpSession httpSession) {
        SessionUser sessionUser = getSessionUser(httpSession);

        List<PublishedPlaylistDetail> publishedPlaylistDetails = publishedPlaylistDetailService.findByPlaylist(id);
        model.addAttribute("id", id);
        model.addAttribute("publishedPlaylistDetails", publishedPlaylistDetails);

        if (!publishedPlaylistDetails.isEmpty()) {
            model.addAttribute("publishedPlaylistDetail", publishedPlaylistDetails.get(0));
            model.addAttribute("materials", publishedMaterialService.findByAreaAndDetail(publishedPlaylistDetails.get(0).getId()));
            model.addAttribute("detailId", publishedPlaylistDetails.get(0).getId());
        }
        return "/security/yms/published_playlist/view";
    }

    @ViewModel(ViewModel.INNER_PAGE)
    @RequestMapping("view_basic.htm")
    public String viewBasic(Model model, Long detailId) {
        PublishedPlaylistDetail publishedPlaylistDetail =  publishedPlaylistDetailService.find(detailId);
        if (publishedPlaylistDetail != null) {
            model.addAttribute("publishedPlaylistDetail", publishedPlaylistDetail);
            model.addAttribute("materials", publishedMaterialService.findByAreaAndDetail(detailId));
            model.addAttribute("detailId", detailId);
        }
        return "/security/yms/published_playlist/view_basic";
    }

}
