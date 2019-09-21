package cn.com.yusong.yhdg.webserver.web.controller.security.basic;

import cn.com.yusong.yhdg.common.annotation.ViewModel;
import cn.com.yusong.yhdg.common.domain.basic.DictCategory;
import cn.com.yusong.yhdg.common.entity.json.DataResult;
import cn.com.yusong.yhdg.webserver.service.basic.DictCategoryService;
import cn.com.yusong.yhdg.webserver.web.controller.security.basic.SecurityController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;

@Controller
@RequestMapping(value = "/security/basic/dict_category")
public class DictCategoryController extends SecurityController {

    @Autowired
    DictCategoryService dictCategoryService;

    @RequestMapping(value = "/find")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public DataResult find(Long id) throws IOException {
        DictCategory dictCategory = dictCategoryService.find(id);
        return DataResult.successResult(dictCategory);
    }

}
