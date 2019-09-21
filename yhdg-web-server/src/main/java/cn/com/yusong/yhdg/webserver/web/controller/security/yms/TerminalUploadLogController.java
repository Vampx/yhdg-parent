package cn.com.yusong.yhdg.webserver.web.controller.security.yms;

import cn.com.yusong.yhdg.common.annotation.ViewModel;
import cn.com.yusong.yhdg.common.domain.yms.TerminalUploadLog;
import cn.com.yusong.yhdg.common.entity.json.ExtResult;
import cn.com.yusong.yhdg.common.entity.json.PageResult;
import cn.com.yusong.yhdg.webserver.config.AppConfig;
import cn.com.yusong.yhdg.webserver.service.yms.TerminalService;
import cn.com.yusong.yhdg.webserver.service.yms.TerminalUploadLogService;
import cn.com.yusong.yhdg.webserver.web.controller.security.basic.SecurityController;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;

@Controller
@RequestMapping(value = "/security/yms/terminal_upload_log")
public class TerminalUploadLogController extends SecurityController {

    @Autowired
    TerminalUploadLogService terminalUploadLogService;
    @Autowired
    TerminalService terminalService;
    @Autowired
    private AppConfig appConfig;

//    @SecurityControl(limits = OperCodeConst.CODE_6_2_8_1)
    @RequestMapping(value = "index.htm")
    public void index(Model model) {
//        model.addAttribute(MENU_CODE_NAME, AppConstEnum.Menu.MENU_06_02_08.getValue());
        model.addAttribute("typeEnum", TerminalUploadLog.Type.values());
        model.addAttribute("terminalList", terminalService.findId());
    }

    @RequestMapping("page.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public PageResult page(TerminalUploadLog search) {
        return PageResult.successResult(terminalUploadLogService.findPage(search));
    }


    @ViewModel(ViewModel.INNER_PAGE)
    @RequestMapping(value = "/select_terminal_upload_log.htm")
    public void selectTerminalUploadLog(Model model) {
        model.addAttribute("typeEnum", TerminalUploadLog.Type.values());
        model.addAttribute("terminalList", terminalService.findId());
    }

    @RequestMapping("notice_upload.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public ExtResult noticeUpload(TerminalUploadLog search) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String logTime = sdf.format(search.getQueryLogTime());

        ExtResult result = terminalUploadLogService.noticeUpload(search, logTime);
        return result;
    }

    @RequestMapping("delete.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public ExtResult delete(long id) {
        terminalUploadLogService.delete(id);
        return ExtResult.successResult();
    }

    //导出日志
    @RequestMapping("download_log.htm")
    public void downLoadLog(long id, HttpServletRequest request, HttpServletResponse response) throws IOException {
        TerminalUploadLog terminalUploadLog = terminalUploadLogService.find(id);
        String filePath = appConfig.staticUrl + terminalUploadLog.getFilePath();
        downLoadFromUrl(filePath, request, response, terminalUploadLog.getTypeName() + terminalUploadLog.getLogTime() + ".log");
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
