package cn.com.yusong.yhdg.webserver.web.controller.security.basic;

import cn.com.yusong.yhdg.common.annotation.SecurityControl;
import cn.com.yusong.yhdg.common.annotation.ViewModel;
import cn.com.yusong.yhdg.common.domain.basic.Part;
import cn.com.yusong.yhdg.common.domain.basic.PartModel;
import cn.com.yusong.yhdg.common.entity.json.ExtResult;
import cn.com.yusong.yhdg.common.entity.json.PageResult;
import cn.com.yusong.yhdg.common.utils.YhdgUtils;
import cn.com.yusong.yhdg.webserver.service.basic.PartModelService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value = "/security/basic/part_model")
public class PartModelController extends SecurityController {

    @Autowired
    PartModelService partModelService;

    @SecurityControl(limits = "basic.PartModel:list")
    @RequestMapping(value = "index.htm")
    public void index(Model model) {
        model.addAttribute(MENU_CODE_NAME, "basic.PartModel:list");
    }

    @RequestMapping("page.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public PageResult page(PartModel search) {
        return PageResult.successResult(partModelService.findPage(search));
    }

    @RequestMapping(value = "part_model_list.htm")
    public void partModelList(Model model, Integer partModelType) {
        List<PartModel> partModelList = partModelService.findList(partModelType);
        model.addAttribute("partModelList", partModelList);
    }

    @RequestMapping(value = "add.htm")
    public void add() {
    }

    @ViewModel(ViewModel.INNER_PAGE)
    @RequestMapping(value = "add_part_model.htm")
    public void addPartModel(Model model, Integer partModelType) {
        model.addAttribute("partModelType", partModelType);
        model.addAttribute("partModelTypeName", PartModel.PartModelType.getName(partModelType));
    }

    @ViewModel(ViewModel.INNER_PAGE)
    @RequestMapping(value = "edit_part_model.htm")
    public void editPartModel(Model model, Integer id) {
        PartModel entity = partModelService.find(id);
        model.addAttribute("entity", entity);
        model.addAttribute("partModelType", entity.getPartModelType());
        model.addAttribute("partModelTypeName", PartModel.PartModelType.getName(entity.getPartModelType()));
    }


    @RequestMapping("create_part_model.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public ExtResult createPartModel(String data) throws IOException {
        Map partModelMap = (Map) YhdgUtils.decodeJson(data,Map.class);
        if(partModelMap == null){
            return ExtResult.failResult("传参为空！");
        }
        Integer partModelType = Integer.parseInt(partModelMap.get("partModelType").toString());
        String partModelName = partModelMap.get("partModelName").toString();
        String permIds = partModelMap.get("permIds").toString();
        PartModel entity = new PartModel();
        entity.setPartModelType(partModelType);
        entity.setPartModelName(partModelName);
        List<String> permIdList = new ArrayList<String>();
        if (permIds != null) {
            String[] str = StringUtils.split(permIds, ",");
            for (int i = 0; i < str.length; i++) {
                if (i != 0 && i != str.length - 1) {
                    permIdList.add(str[i].trim());
                }else{
                    permIdList.add(str[i].replace("[", "").replace("]", "").trim());
                }
            }
        }
        entity.setCreateTime(new Date());
        entity.setPermIdList(permIdList);
        return partModelService.create(entity);
    }

    @RequestMapping("update_part_model.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public ExtResult updatePartModel(String data) throws IOException {
        Map partModelMap = (Map) YhdgUtils.decodeJson(data,Map.class);
        if(partModelMap == null){
            return ExtResult.failResult("传参为空！");
        }
        Integer id = Integer.parseInt(partModelMap.get("id").toString());
        Integer partModelType = Integer.parseInt(partModelMap.get("partModelType").toString());
        String partModelName = partModelMap.get("partModelName").toString();
        String permIds = partModelMap.get("permIds").toString();
        PartModel entity = new PartModel();
        entity.setId(id);
        entity.setPartModelType(partModelType);
        entity.setPartModelName(partModelName);
        List<String> permIdList = new ArrayList<String>();
        if (permIds != null) {
            String[] str = StringUtils.split(permIds, ",");
            for (int i = 0; i < str.length; i++) {
                if (i != 0 && i != str.length - 1) {
                    permIdList.add(str[i].trim());
                }else{
                    permIdList.add(str[i].replace("[", "").replace("]", "").trim());
                }
            }
        }
        entity.setPermIdList(permIdList);
        return partModelService.update(entity);
    }

    @RequestMapping(value = "edit.htm")
    public String edit(Model model, Integer id) {
        PartModel entity = partModelService.find(id);
        if(entity == null) {
            return SEGMENT_RECORD_NOT_FOUND;
        } else {
            model.addAttribute("entity", entity);
        }
        return "/security/basic/part_model/edit";
    }

    @RequestMapping("create.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public ExtResult create(PartModel entity) {
        return partModelService.create(entity);
    }

    @RequestMapping("delete.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public ExtResult delete(Integer id) {
        partModelService.delete(id);
        return ExtResult.successResult();
    }

}
