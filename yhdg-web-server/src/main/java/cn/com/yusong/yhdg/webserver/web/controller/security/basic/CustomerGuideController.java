package cn.com.yusong.yhdg.webserver.web.controller.security.basic;

import cn.com.yusong.yhdg.common.annotation.SecurityControl;
import cn.com.yusong.yhdg.common.annotation.ViewModel;
import cn.com.yusong.yhdg.common.constant.ConstEnum;
import cn.com.yusong.yhdg.common.domain.basic.CustomerGuide;
import cn.com.yusong.yhdg.common.entity.json.ExtResult;
import cn.com.yusong.yhdg.common.entity.json.PageResult;
import cn.com.yusong.yhdg.common.utils.HttpClientUtils;
import cn.com.yusong.yhdg.webserver.constant.AppConstEnum;
import cn.com.yusong.yhdg.webserver.constant.OperCodeConst;
import cn.com.yusong.yhdg.webserver.service.basic.CustomerGuideService;
import cn.com.yusong.yhdg.webserver.utils.AppUtils;
import cn.com.yusong.yhdg.webserver.utils.ReadUrlUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Controller
@RequestMapping(value = "/security/basic/customer_guide")
public class CustomerGuideController extends SecurityController {
    @Autowired
    CustomerGuideService customerGuideService;

    @SecurityControl(limits = "basic.CustomerGuide:list")
    @RequestMapping(value = "index.htm")
    public void index(Model model) throws Exception {
        model.addAttribute("richTextEnum", ConstEnum.RichText.values());
        model.addAttribute(MENU_CODE_NAME, "basic.CustomerGuide:list");
    }

    @RequestMapping("page.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public PageResult page(CustomerGuide search) {
        return PageResult.successResult(customerGuideService.findPage(search));
    }
    @ResponseBody
    @ViewModel(ViewModel.JSON_ARRAY)
    @RequestMapping(value = "tree.htm")
    public void tree(String dummy, HttpServletResponse response) throws Exception {
        response.setContentType(ConstEnum.ContentType.JSON.getValue());

        Set<Integer> checkedSet = Collections.emptySet();
        customerGuideService.tree(checkedSet, dummy,  response.getOutputStream());
    }

    @ResponseBody
    @ViewModel(ViewModel.JSON_ARRAY)
    @RequestMapping(value = "parentTree.htm")
    public void parentTree(String dummy, HttpServletResponse response) throws Exception {
        response.setContentType(ConstEnum.ContentType.JSON.getValue());

        Set<Integer> checkedSet = Collections.emptySet();
        customerGuideService.parentTree(checkedSet, dummy,  response.getOutputStream());
    }

    @RequestMapping("save.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public ExtResult save(long id, String content, Model model) throws IOException {
        if (StringUtils.isEmpty(content)) {
            return ExtResult.failResult("文本内容不能为空");
        }

        String head = "<head><meta charset='utf-8'><meta charset='utf-8'name='viewport' content='width=device-width, initial-scale=1.0'><style>img{ width:100%; }</style><title></title></head>";


        File file = new File(appConfig.tempDir, String.format("%d.html", id));
        AppUtils.makeParentDir(file);

        StringBuilder contents = new StringBuilder();
        contents.append("<!doctype html>" + head + "<html><body>");
        contents.append(content);
        contents.append("</body></html>");
        FileUtils.writeStringToFile(file, contents.toString(), "UTF-8");

        Map<String, File> fileMap = new HashMap<String, File>();
        fileMap.put(file.getName(), file);

        HttpClientUtils.HttpResp httpResp = HttpClientUtils.uploadFile(appConfig.staticUrl + "/security/upload/customer_guide.htm", fileMap, Collections.<String, String>emptyMap(), Collections.<String, String>emptyMap());
        if (httpResp.status / 100 == 2) {
            model.addAttribute("success", true);
            return ExtResult.successResult("上传文件成功");
        } else {
            return ExtResult.failResult("上传文件失败");
        }
    }

    @RequestMapping(value = "read.htm")
    public void read(long id, HttpServletResponse response) throws Exception {
        String resource = appConfig.getStaticUrl() + String.format("/static/customer_guide/%d.html", id);
        ReadUrlUtils.Result result = ReadUrlUtils.readUrl(resource);
        if (result.httpCode / 100 == 2) {
            response.setContentType("text/html;charset=UTF-8");
            response.getWriter().write(result.httpContent);
        } else {
            response.getWriter().write("");
        }
    }

    @RequestMapping(value = "add.htm")
    public void add(Model model) {
    }

    @RequestMapping(value = "view.htm")
    public void view(Model model) {
    }

    @RequestMapping("create.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public ExtResult create(CustomerGuide entity) {
        return customerGuideService.insert(entity);
    }

    @RequestMapping(value = "edit.htm")
    public String edit(Model model, Integer id) {
        CustomerGuide entity = customerGuideService.find(id);
        if (entity == null) {
            return SEGMENT_RECORD_NOT_FOUND;
        } else {
            model.addAttribute("entity", entity);
        }
        return "/security/basic/customer_guide/edit";
    }

    @RequestMapping("update.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public ExtResult update(CustomerGuide entity) {
        return customerGuideService.update(entity);
    }

    @RequestMapping("delete.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public ExtResult delete(Integer id) {
        return customerGuideService.delete(id);
    }
}
