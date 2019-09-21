package cn.com.yusong.yhdg.webserver.web.controller.security.yms;

import cn.com.yusong.yhdg.common.annotation.SecurityControl;
import cn.com.yusong.yhdg.common.annotation.ViewModel;
import cn.com.yusong.yhdg.common.domain.yms.TerminalCrashLog;
import cn.com.yusong.yhdg.common.domain.yms.TerminalUploadLog;
import cn.com.yusong.yhdg.common.entity.json.PageResult;
import cn.com.yusong.yhdg.webserver.constant.AppConstEnum;
import cn.com.yusong.yhdg.webserver.constant.OperCodeConst;
import cn.com.yusong.yhdg.webserver.service.yms.TerminalCrashLogService;
import cn.com.yusong.yhdg.webserver.web.controller.security.basic.SecurityController;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

@Controller
@RequestMapping(value = "/security/yms/terminal_crash_log")
public class TerminalCrashLogController extends SecurityController {
    @Autowired
    TerminalCrashLogService terminalCrashLogService;

//    @SecurityControl(limits = OperCodeConst.CODE_6_2_7_1)
    @RequestMapping(value = "index.htm")
    public void index(Model model) throws Exception {
//        model.addAttribute(MENU_CODE_NAME, AppConstEnum.Menu.MENU_06_02_07.getValue());
    }

    @RequestMapping("page.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public PageResult page(TerminalCrashLog search) {
        return PageResult.successResult(terminalCrashLogService.findPage(search));
    }

    @ViewModel(ViewModel.INNER_PAGE)
    @RequestMapping("view.htm")
    public String view(Model model, long id) {
        TerminalCrashLog entity = terminalCrashLogService.find(id);
        if(entity == null) {
            return SEGMENT_RECORD_NOT_FOUND;
        }
        model.addAttribute("entity", entity);
        model.addAttribute("id", id);
        return "/security/yms/terminal_crash_log/view";
    }

    //导出日志
    @RequestMapping("download_log.htm")
    public void downLoadLog(int id, HttpServletRequest request, HttpServletResponse response) throws IOException {
        TerminalCrashLog entity = terminalCrashLogService.find(id);
        String filePath = appConfig.staticUrl + entity.getFilePath();
        downLoadFromUrl(filePath, request, response, new File(entity.getFilePath()).getName() + ".txt");
    }

    @RequestMapping(value = "load.htm")
    public void load(int id, HttpServletResponse response) throws IOException {
        TerminalCrashLog entity = terminalCrashLogService.find(id);
        if (entity == null){
        } else {
            File file = new File(getAppConfig().appDir, entity.getFilePath());
            if(file.exists()) {
                response.getOutputStream().write(FileUtils.readFileToByteArray(file));
                response.flushBuffer();
            }
        }
    }

    public static void  downLoadFromUrl(String urlStr,HttpServletRequest request, HttpServletResponse response, String fileName) throws IOException{
        String agent = request.getHeader("User-Agent");
        if (agent != null) {
            agent = agent.toLowerCase();
            if (agent.indexOf("firefox") != -1) {
                fileName = new String(fileName.getBytes(),"ISO-8859-1");
            } else if (agent.indexOf("msie") != -1) {
                fileName = java.net.URLEncoder.encode(fileName,"UTF-8");
            } else {
                fileName = java.net.URLEncoder.encode(fileName,"UTF-8");
            }
        }

        response.setHeader("Content-disposition", String.format("attachment;filename=%s", fileName));
        response.setContentType("application/octet-stream");
        response.setBufferSize(1024 * 10);

        URL url = new URL(urlStr);
        HttpURLConnection conn = (HttpURLConnection)url.openConnection();
        //设置超时间为3秒
        conn.setConnectTimeout(3*1000);
        //防止屏蔽程序抓取而返回403错误
        conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");

        InputStream inputStream = null;
        try {
            //得到输入流
            inputStream = conn.getInputStream();
            byte[] buf = new byte[1024 * 10];
            int count = -1;
            while( (count = inputStream.read(buf)) != -1) {
                response.getOutputStream().write(buf, 0, count);
            }
        } catch (Exception e) {
        } finally {
            IOUtils.closeQuietly(inputStream);
        }
        response.getOutputStream().flush();
        response.getOutputStream().close();
    }

}
