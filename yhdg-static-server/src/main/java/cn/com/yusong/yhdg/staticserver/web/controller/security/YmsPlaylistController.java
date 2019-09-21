package cn.com.yusong.yhdg.staticserver.web.controller.security;

import cn.com.yusong.yhdg.common.annotation.ViewModel;
import cn.com.yusong.yhdg.common.entity.json.ExtResult;
import cn.com.yusong.yhdg.staticserver.service.yms.PackPlaylistTool;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/security/playlist")
public class YmsPlaylistController extends SecurityController {
    static Logger log = LogManager.getLogger(YmsPlaylistController.class);

    @Autowired
    PackPlaylistTool packPlaylistTool;

    @ViewModel(ViewModel.JSON)
    @ResponseBody
    @RequestMapping(value = "download.htm", method = RequestMethod.POST)
    public ExtResult download(Long playlistId, Integer version) {
        try {
            return packPlaylistTool.doPack(getAppConfig().appDir, playlistId, version);
        } catch (Exception e) {
            log.error("doPack error", e);
            return ExtResult.failResult("打包出现错误");
        }
    }
}
