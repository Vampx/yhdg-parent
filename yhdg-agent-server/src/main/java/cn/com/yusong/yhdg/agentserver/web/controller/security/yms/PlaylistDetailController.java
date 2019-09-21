package cn.com.yusong.yhdg.agentserver.web.controller.security.yms;

import cn.com.yusong.yhdg.agentserver.service.yms.PlaylistDetailService;
import cn.com.yusong.yhdg.agentserver.service.yms.PlaylistService;
import cn.com.yusong.yhdg.agentserver.web.controller.security.basic.SecurityController;
import cn.com.yusong.yhdg.common.annotation.ViewModel;
import cn.com.yusong.yhdg.common.domain.yms.PlayListDetail;
import cn.com.yusong.yhdg.common.domain.yms.Playlist;
import cn.com.yusong.yhdg.common.entity.json.ExtResult;
import cn.com.yusong.yhdg.common.entity.json.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by zhoub on 2017/7/24.
 */
@Controller
@RequestMapping(value = "/security/yms/playlist_detail")
public class PlaylistDetailController extends SecurityController {
    @Autowired
    PlaylistDetailService playlistDetailService;
    @Autowired
    PlaylistService playlistService;

    @RequestMapping(value = "index.htm")
    public String index(Integer playlistId, Model model) {
        Playlist playlist = playlistService.find(playlistId);
        if (playlist == null) {
            return SEGMENT_RECORD_NOT_FOUND;
        } else {
            model.addAttribute("agentId", playlist.getAgentId());
        }
        model.addAttribute("playlistId", playlistId);
        return "/security/yms/playlist_detail/index";
    }

    @RequestMapping(value = "page.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public PageResult page(PlayListDetail search) {
        return PageResult.successResult(playlistDetailService.findPage(search));
    }

    @RequestMapping(value = "edit_order_num.htm")
    public String extendRent(Model model, int id) {
        PlayListDetail entity = playlistDetailService.find(id);
        if (entity == null) {
            return SEGMENT_RECORD_NOT_FOUND;
        } else {
            model.addAttribute("entity", entity);
        }
        return "/security/yms/playlist_detail/edit_order_num";
    }

    @RequestMapping("order_num.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public ExtResult orderNum(PlayListDetail entity) {
        return playlistDetailService.update(entity);
    }

    @RequestMapping("delete.htm")
    @ResponseBody
    @ViewModel(ViewModel.JSON)
    public ExtResult delete(int id) {
        return playlistDetailService.delete(id);
    }

    @RequestMapping(value = "add.htm")
    public void add(Integer agentId, Model model) {
        model.addAttribute("agentId", agentId);
    }

    @RequestMapping("create.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public ExtResult create(@RequestParam int playlistId, @RequestParam Long[] materialId) {
        playlistDetailService.create(playlistId, materialId);
        return ExtResult.successResult();
    }

    @RequestMapping("update.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public ExtResult update(PlayListDetail playListDetail) {
        return playlistDetailService.update(playListDetail);
    }




}
