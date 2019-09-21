package cn.com.yusong.yhdg.agentserver.web.controller.security.basic;

import cn.com.yusong.yhdg.common.annotation.SecurityControl;
import cn.com.yusong.yhdg.common.domain.basic.DictCategory;
import cn.com.yusong.yhdg.agentserver.constant.OperCodeConst;
import cn.com.yusong.yhdg.agentserver.service.basic.DictCategoryService;
import cn.com.yusong.yhdg.agentserver.service.basic.DictItemService;
import cn.com.yusong.yhdg.common.annotation.ViewModel;
import cn.com.yusong.yhdg.common.constant.ConstEnum;
import cn.com.yusong.yhdg.common.domain.basic.DictItem;
import cn.com.yusong.yhdg.common.entity.json.ExtResult;
import cn.com.yusong.yhdg.common.entity.json.PageResult;
import cn.com.yusong.yhdg.agentserver.constant.AppConstEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 字典项目
 */
@Controller
@RequestMapping(value = "/security/basic/dict_item")
public class DictItemController extends SecurityController {

    @Autowired
    DictItemService dictItemService;
    @Autowired
    DictCategoryService dictCategoryService;

//    @SecurityControl(limits = OperCodeConst.CODE_7_5_2_1)
    @RequestMapping("index.htm")
    public String index(Model model) {

//        model.addAttribute(MENU_CODE_NAME, AppConstEnum.Menu.MENU_07_05_02.getValue());
        return "/security/basic/dict_item/index";
    }

    @RequestMapping("page.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public PageResult page(DictItem search) {
        return PageResult.successResult(dictItemService.findPage(search));
    }

    @RequestMapping("add.htm")
    @ViewModel(ViewModel.INNER_PAGE)
    public void add(Model model){
        model.addAttribute("NUMBER", DictCategory.ValueType.NUMBER.getValue());
    }

    @RequestMapping("create.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public ExtResult create(DictItem entity) {
        return dictItemService.insert(entity);
    }

    @RequestMapping("edit.htm")
    @ViewModel(ViewModel.INNER_PAGE)
    public String edit(Long id, Model model) {
        DictItem entity = dictItemService.find(id);
        if(entity == null) {
            return SEGMENT_RECORD_NOT_FOUND;
        }
        model.addAttribute("entity", entity);
        DictCategory dictCategory = dictCategoryService.find(entity.getCategoryId());
        if (dictCategory != null) {
            if (dictCategory.getValueType() == DictCategory.ValueType.NUMBER.getValue()){
                model.addAttribute("valueType", "数字");
            } else {
                model.addAttribute("valueType", "文字");
            }
        }
        return "/security/basic/dict_item/edit";
    }

    @RequestMapping("update.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public ExtResult update(DictItem dictItem) {
        return dictItemService.update(dictItem);
    }

    @RequestMapping("view.htm")
    @ViewModel(ViewModel.INNER_PAGE)
    public String view(Long id, Model model) {
        DictItem entity = dictItemService.find(id);
        if(entity == null) {
            return SEGMENT_RECORD_NOT_FOUND;
        }
        model.addAttribute("entity", entity);
        return "/security/basic/dict_item/view";
    }

    @RequestMapping("delete.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public ExtResult delete(Long id) {
        return dictItemService.delete(id);
    }

    @RequestMapping("find_category.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public void find_category(HttpServletResponse response) throws IOException {
        response.setContentType(ConstEnum.ContentType.JSON.getValue());
        dictItemService.dictCategoryJson(response.getOutputStream());
    }

    @ResponseBody
    @RequestMapping("update_order_num.htm")
    @ViewModel(ViewModel.JSON)
    public ExtResult updateOrderNum(Long id, int orderNum) {
        dictItemService.updateOrderNum(id, orderNum);
        return ExtResult.successResult();
    }

}
