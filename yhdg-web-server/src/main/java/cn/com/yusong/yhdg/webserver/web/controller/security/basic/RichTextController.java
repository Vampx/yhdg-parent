package cn.com.yusong.yhdg.webserver.web.controller.security.basic;

import cn.com.yusong.yhdg.common.annotation.SecurityControl;
import cn.com.yusong.yhdg.common.annotation.ViewModel;
import cn.com.yusong.yhdg.common.constant.ConstEnum;
import cn.com.yusong.yhdg.common.entity.json.ExtResult;
import cn.com.yusong.yhdg.common.utils.HttpClientUtils;
import cn.com.yusong.yhdg.webserver.utils.AppUtils;
import cn.com.yusong.yhdg.webserver.utils.ReadUrlUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
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

/**
 * Created by chen on 2017/10/28.
 */
@Controller
@RequestMapping(value = "/security/basic/rich_text")
public class RichTextController extends SecurityController {

    @SecurityControl(limits = "basic.RichText:list")
    @RequestMapping(value = "index.htm")
    public void index(Model model, Integer weixinmpId, Integer alipayfwId, Integer phoneappId) throws Exception {
        if(weixinmpId != null){
            model.addAttribute("weixinmpId",weixinmpId);
        }
        if(alipayfwId != null){
            model.addAttribute("alipayfwId",alipayfwId);
        }
        if(phoneappId != null){
            model.addAttribute("phoneappId",phoneappId);
        }
        model.addAttribute("richTextEnum", ConstEnum.RichText.values());
        model.addAttribute(MENU_CODE_NAME, "basic.RichText:list");
    }

    @RequestMapping("save.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public ExtResult save(long id, Integer weixinmpId, Integer alipayfwId, Integer phoneappId, String content, Model model) throws IOException {
        if (weixinmpId == null && alipayfwId == null && phoneappId == null) {
            return ExtResult.failResult("请先选择公众号或生活号或app");
        }
        if (StringUtils.isEmpty(content)) {
            return ExtResult.failResult("文本内容不能为空");
        }

        String head = "<head><meta charset='utf-8'><meta charset='utf-8'name='viewport' content='width=device-width, initial-scale=1.0'><style>img{ width:100%; }</style><title></title></head>";

//        if (id == ConstEnum.RichText.ABOUT_US.getValue()) {
//            head = "<head><meta charset='utf-8'><meta charset='utf-8'name='viewport' content='width=device-width, initial-scale=1.0'><style>img{ width:100%; }</style><title></title></head>";
//        }

        File file = null;
        if (weixinmpId != null) {
            file = new File(appConfig.tempDir, String.format("mp_%d_%d.html", id, weixinmpId));
        }else if (alipayfwId != null) {
            file = new File(appConfig.tempDir, String.format("fw_%d_%d.html", id, alipayfwId));
        }else if (phoneappId != null) {
            file = new File(appConfig.tempDir, String.format("app_%d_%d.html", id, phoneappId));
        }

        AppUtils.makeParentDir(file);

        StringBuilder contents = new StringBuilder();
        contents.append("<!doctype html>" + head + "<html><body>");
        contents.append(content);
        contents.append("</body></html>");
        FileUtils.writeStringToFile(file, contents.toString(), "UTF-8");

        Map<String, File> fileMap = new HashMap<String, File>();
        fileMap.put(file.getName(), file);

        HttpClientUtils.HttpResp httpResp = HttpClientUtils.uploadFile(appConfig.staticUrl + "/security/upload/rich_text.htm", fileMap, Collections.<String, String>emptyMap(), Collections.<String, String>emptyMap());
        if (httpResp.status / 100 == 2) {
            model.addAttribute("success", true);
            return ExtResult.successResult("上传文件成功");
        } else {
            return ExtResult.failResult("上传文件失败");
        }
    }

    @RequestMapping(value = "read.htm")
    public void read(long id, Integer weixinmpId, Integer alipayfwId, Integer phoneappId, HttpServletResponse response) throws Exception {
        if (weixinmpId == null && alipayfwId == null && phoneappId == null) {
            weixinmpId = 0;
        }
        String resource = null;
        if(weixinmpId !=null){
            resource =  appConfig.getStaticUrl() + String.format("/static/rich_text/mp_%d_%d.html", id, weixinmpId);
        }else if (alipayfwId != null) {
            resource = appConfig.getStaticUrl() + String.format("/static/rich_text/fw_%d_%d.html", id, alipayfwId);
        }else if (phoneappId != null){
            resource = appConfig.getStaticUrl() + String.format("/static/rich_text/app_%d_%d.html", id, phoneappId);
        }

        ReadUrlUtils.Result result = ReadUrlUtils.readUrl(resource);
        if (result.httpCode / 100 == 2) {
            response.setContentType("text/html;charset=UTF-8");
            response.getWriter().write(result.httpContent);
        } else {
            response.getWriter().write("");
        }
    }
}
