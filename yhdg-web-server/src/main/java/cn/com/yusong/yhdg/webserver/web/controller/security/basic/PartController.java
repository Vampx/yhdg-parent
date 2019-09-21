package cn.com.yusong.yhdg.webserver.web.controller.security.basic;

import cn.com.yusong.yhdg.common.annotation.SecurityControl;
import cn.com.yusong.yhdg.common.annotation.ViewModel;
import cn.com.yusong.yhdg.common.constant.ConstEnum;
import cn.com.yusong.yhdg.common.domain.basic.Part;
import cn.com.yusong.yhdg.common.entity.json.ExtResult;
import cn.com.yusong.yhdg.common.entity.json.PageResult;
import cn.com.yusong.yhdg.webserver.service.basic.PartService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping(value = "/security/basic/part")
public class PartController extends SecurityController {

    @Autowired
    PartService partService;

    @SecurityControl(limits = "basic.Part:list")
    @RequestMapping(value = "index.htm")
    public void index(Model model) {
        model.addAttribute(MENU_CODE_NAME, "basic.Part:list");
    }

    @RequestMapping("page.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public PageResult page(Part search) {
        return PageResult.successResult(partService.findPage(search));
    }

    @ResponseBody
    @ViewModel(ViewModel.JSON_ARRAY)
    @RequestMapping(value = "tree.htm")
    public void tree(Integer id, Integer agentId, HttpServletResponse response) throws Exception {
        response.setContentType(ConstEnum.ContentType.JSON.getValue());
        partService.tree(id, response.getOutputStream());
    }

    @RequestMapping(value = "add.htm")
    public void add() {
    }

    @RequestMapping(value = "edit.htm")
    public String edit(Model model, Integer id) {
        Part entity = partService.find(id);
        if(entity == null) {
            return SEGMENT_RECORD_NOT_FOUND;
        } else {
            model.addAttribute("entity", entity);
        }
        return "/security/basic/part/edit";
    }

    @RequestMapping("create.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public ExtResult create(Part entity) {
        List<String> permIdList = new ArrayList<String>();
        if (entity.getPermIds() != null) {
            String[] str = StringUtils.split(entity.getPermIds(), ",");
            for (String permId : str) {
                permIdList.add(permId);
            }
        }
        entity.setPermIdList(permIdList);
        entity.setCreateTime(new Date());
        return partService.create(entity);
    }

    @RequestMapping(value = "part_platform.htm")
    public void partPlatform(Model model, String mobile) {
        List<Part> platformList = partService.findList(mobile, Part.PartType.PLATFORM.getValue());
        model.addAttribute("platformList", platformList);
    }

    @ViewModel(ViewModel.INNER_PAGE)
    @RequestMapping(value = "add_part_platform.htm")
    public void addPartPlatform(Model model) {
    }

    @RequestMapping("create_part_platform.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public ExtResult createPartPlatform(Part entity){
        return partService.create(entity);
    }

    @RequestMapping(value = "part_agent.htm")
    public void partAgent(Model model, String mobile) {
        List<Part> agentPartList = partService.findList(mobile, Part.PartType.AGENT.getValue());
        model.addAttribute("agentPartList", agentPartList);
    }

    @ViewModel(ViewModel.INNER_PAGE)
    @RequestMapping(value = "add_part_agent.htm")
    public void addPartAgent(Model model) {
    }

    @RequestMapping("create_part_agent.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public ExtResult createPartAgent(Part entity){
        return partService.create(entity);
    }

    @RequestMapping(value = "part_export.htm")
    public void partExport(Model model, String mobile) {
        List<Part> exportList = partService.findList(mobile, Part.PartType.EXPORT.getValue());
        model.addAttribute("exportList", exportList);
    }

    @ViewModel(ViewModel.INNER_PAGE)
    @RequestMapping(value = "add_part_export.htm")
    public void addPartExport(Model model) {
    }

    @RequestMapping("create_part_export.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public ExtResult createPartExport(Part entity){
        return partService.create(entity);
    }

    @RequestMapping("update.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public ExtResult update(Part entity) {
        return partService.update(entity);
    }

    @RequestMapping("delete.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public ExtResult delete(Integer id) {
        partService.delete(id);
        return ExtResult.successResult();
    }

    @RequestMapping(value = "view.htm")
    public String view(Model model, Integer id) {
        Part entity = partService.find(id);
        if(entity == null) {
            return SEGMENT_RECORD_NOT_FOUND;
        } else {
            model.addAttribute("entity", entity);
        }
        return "/security/basic/part/view";
    }
}
