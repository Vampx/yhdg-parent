package cn.com.yusong.yhdg.agentserver.web.controller.security.yms;


import cn.com.yusong.yhdg.agentserver.service.yms.MaterialGroupService;
import cn.com.yusong.yhdg.agentserver.web.controller.security.basic.SecurityController;
import cn.com.yusong.yhdg.common.annotation.ViewModel;
import cn.com.yusong.yhdg.common.constant.ConstEnum;
import cn.com.yusong.yhdg.common.domain.yms.MaterialGroup;
import cn.com.yusong.yhdg.common.entity.json.ExtResult;
import cn.com.yusong.yhdg.common.entity.json.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.util.Collections;
import java.util.Set;

@Controller
@RequestMapping(value = "/security/yms/material_group")
public class MaterialGroupController extends SecurityController {
    @Autowired
    MaterialGroupService materialGroupService;

    @ResponseBody
    @ViewModel(ViewModel.JSON_ARRAY)
    @RequestMapping(value = "tree.htm")
    public void tree(String dummy, Integer agentId, HttpServletResponse response) throws Exception {
        response.setContentType(ConstEnum.ContentType.JSON.getValue());

        Set<Integer> checkedSet = Collections.emptySet();
        materialGroupService.tree(checkedSet, dummy, agentId, response.getOutputStream());
    }

    @RequestMapping(value = "index.htm")
    public void index(Model model) {
    }

    @RequestMapping("page.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public PageResult page(MaterialGroup search) {
        return PageResult.successResult(materialGroupService.findPage(search));
    }

    @RequestMapping(value = "add.htm")
    public void add(Model model) {
    }

    @RequestMapping(value = "edit.htm")
      public String edit(Model model, Long id) {
        MaterialGroup entity = materialGroupService.find(id);
        if(entity == null) {
            return SEGMENT_RECORD_NOT_FOUND;
        } else {
            model.addAttribute("entity", entity);
        }
        return "/security/yms/material_group/edit";
    }

    @RequestMapping("create.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public ExtResult create(MaterialGroup entity) {
        materialGroupService.insert(entity);
        return ExtResult.successResult();
    }

    @RequestMapping("update.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public ExtResult update(MaterialGroup entity) {
        materialGroupService.update(entity);
        return ExtResult.successResult();
    }

    @RequestMapping("delete.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public ExtResult delete(Long id) {
        return materialGroupService.delete(id);
    }

    @RequestMapping(value = "view.htm")
    public String view(Model model, Long id) {
        MaterialGroup entity = materialGroupService.find(id);
        if(entity == null) {
            return SEGMENT_RECORD_NOT_FOUND;
        } else {
            model.addAttribute("entity", entity);
        }
        return "/security/yms/material_group/view";
    }
}
