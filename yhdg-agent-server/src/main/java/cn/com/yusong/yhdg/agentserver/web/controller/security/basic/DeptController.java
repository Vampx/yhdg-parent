package cn.com.yusong.yhdg.agentserver.web.controller.security.basic;

import cn.com.yusong.yhdg.common.annotation.ViewModel;
import cn.com.yusong.yhdg.common.constant.ConstEnum;
import cn.com.yusong.yhdg.common.domain.basic.Dept;
import cn.com.yusong.yhdg.common.entity.json.ExtResult;
import cn.com.yusong.yhdg.common.entity.json.PageResult;
import cn.com.yusong.yhdg.agentserver.service.basic.AgentService;
import cn.com.yusong.yhdg.agentserver.service.basic.DeptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.util.Collections;
import java.util.Set;

@Controller
@RequestMapping(value = "/security/basic/dept")
public class DeptController extends SecurityController {

    @Autowired
    DeptService deptService;
    @Autowired
    AgentService agentService;

    @ResponseBody
    @ViewModel(ViewModel.JSON_ARRAY)
    @RequestMapping(value = "tree.htm")
    public void tree(String dummy, Integer agentId, HttpServletResponse response) throws Exception {
        response.setContentType(ConstEnum.ContentType.JSON.getValue());

        Set<Integer> checkedSet = Collections.emptySet();
        deptService.tree(checkedSet, dummy, agentId, response.getOutputStream());
    }

    @RequestMapping(value = "index.htm")
    public void index(Model model) {

    }

    @RequestMapping("page.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public PageResult page(Dept search) {
        return PageResult.successResult(deptService.findPage(search));
    }

    @RequestMapping(value = "add.htm")
    public void add(Model model) {
    }

    @RequestMapping(value = "edit.htm")
    public String edit(Model model, int id) {
        Dept entity = deptService.find(id);
        if(entity == null) {
            return SEGMENT_RECORD_NOT_FOUND;
        } else {
            model.addAttribute("entity", entity);
        }
        return "/security/basic/dept/edit";
    }

    @RequestMapping("create.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public ExtResult create(Dept entity) {
        deptService.insert(entity);
        return ExtResult.successResult();
    }

    @RequestMapping("update.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public ExtResult update(Dept entity) {
        deptService.update(entity);
        return ExtResult.successResult();
    }

    @RequestMapping("delete.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public ExtResult delete(int id) {
        return deptService.delete(id);
    }

    @RequestMapping(value = "view.htm")
    public String view(Model model, int id) {
        Dept entity = deptService.find(id);
        if(entity == null) {
            return SEGMENT_RECORD_NOT_FOUND;
        } else {
            Integer agentId = entity.getAgentId();
            if (agentId != null && agentId != 0) {
                entity.setAgentName(agentService.find(agentId).getAgentName());
            }
            model.addAttribute("entity", entity);
        }
        return "/security/basic/dept/view";
    }
}
