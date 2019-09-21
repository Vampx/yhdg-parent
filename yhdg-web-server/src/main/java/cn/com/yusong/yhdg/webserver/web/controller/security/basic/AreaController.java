package cn.com.yusong.yhdg.webserver.web.controller.security.basic;

import cn.com.yusong.yhdg.common.annotation.ViewModel;
import cn.com.yusong.yhdg.common.constant.ConstEnum;
import cn.com.yusong.yhdg.common.entity.AreaCache;
import cn.com.yusong.yhdg.common.entity.json.DataResult;
import cn.com.yusong.yhdg.common.entity.json.ExtResult;
import cn.com.yusong.yhdg.webserver.service.basic.AreaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping(value = "/security/basic/area")
public class AreaController extends SecurityController {

    @Autowired
    AreaCache areaCache;
    @Autowired
    AreaService areaService;

    @RequestMapping(value = "show_area.htm")
    public void showArea() {
    }

    @ResponseBody
    @RequestMapping(value = "children.htm")
    public ExtResult children(int id) {
        return DataResult.successResult(areaCache.getChildren(id));
    }

    @ResponseBody
    @ViewModel(ViewModel.JSON_ARRAY)
    @RequestMapping(value = "async_tree.htm")
    public void asyncTree(String dummy, String id, HttpServletResponse response) throws Exception {
        response.setContentType(ConstEnum.ContentType.JSON.getValue());
        areaService.children(dummy, id, response.getOutputStream());
    }
}
