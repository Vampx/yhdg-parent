package cn.com.yusong.yhdg.webserver.web.controller.security.hdg;

import cn.com.yusong.yhdg.common.annotation.SecurityControl;
import cn.com.yusong.yhdg.common.annotation.ViewModel;
import cn.com.yusong.yhdg.common.constant.Constant;
import cn.com.yusong.yhdg.common.domain.basic.User;
import cn.com.yusong.yhdg.common.domain.hdg.ExportRecord;
import cn.com.yusong.yhdg.common.entity.json.ExtResult;
import cn.com.yusong.yhdg.common.entity.json.PageResult;
import cn.com.yusong.yhdg.webserver.entity.SessionUser;
import cn.com.yusong.yhdg.webserver.service.hdg.ExportRecordService;
import cn.com.yusong.yhdg.webserver.web.controller.security.basic.SecurityController;
import jxl.read.biff.BiffException;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 *  发货记录
 */
@Controller
@RequestMapping(value = "/security/hdg/export_record")
public class ExportRecordController extends SecurityController {
	@Autowired
	ExportRecordService exportRecordService;

	@SecurityControl(limits = "hdg.ExportRecord:list")
	@RequestMapping(value = "index.htm")
	public void index(Model model) {
		model.addAttribute(MENU_CODE_NAME, "hdg.ExportRecord:list");
	}

	@RequestMapping("page.htm")
	@ViewModel(ViewModel.JSON)
	@ResponseBody
	public PageResult page(ExportRecord search) {
		return PageResult.successResult(exportRecordService.findPage(search));
	}

	@RequestMapping(value = "view.htm")
	public String view(Model model, Integer id) {
		ExportRecord entity = exportRecordService.find(id);
		if (entity == null) {
			return SEGMENT_RECORD_NOT_FOUND;
		} else {
			model.addAttribute("entity", entity);
		}
		return "/security/hdg/export_record/view";
	}

	@RequestMapping("delete.htm")
	@ViewModel(ViewModel.JSON)
	@ResponseBody
	public ExtResult delete(Integer id) {
		return exportRecordService.delete(id);
	}

	@RequestMapping("upload_file.htm")
	@ViewModel(ViewModel.INNER_PAGE)
	public void uploadFile(Model model) {
	}

	@RequestMapping(value = "batch_import_export.htm", method = RequestMethod.POST)
	@ViewModel(ViewModel.JSON)
	@ResponseBody
	public ExtResult batchImportExport(@RequestParam("file") MultipartFile file, ExportRecord exportRecord, HttpServletRequest request) throws IOException, BiffException {
		HttpSession httpSession = request.getSession();
		SessionUser sessionUser = null;
		if (httpSession != null) {
			sessionUser = (SessionUser) httpSession.getAttribute(Constant.SESSION_KEY_USER);
		}
		String userName = "";
		User user = userService.find(sessionUser.getId());
		if (user != null) {
			userName = user.getFullname();
		}
		if (StringUtils.isEmpty(userName)) {
			userName = sessionUser.getUsername();
		}
		String outPath = String.format("%s/%s", getAppConfig().appDir.getPath(), file.getOriginalFilename());
		File outFile = new File(outPath);
		if (!outFile.exists()) {
			outFile.getParentFile().mkdirs();
		}
		FileOutputStream outputStream = new FileOutputStream(outFile);
		int result = IOUtils.copy(file.getInputStream(), outputStream);
		if (result <= 0) {
			return ExtResult.failResult("操作失败！");
		}
		File mFile = new File(outPath);
		return exportRecordService.batchImportExport(mFile, exportRecord, userName);
	}

}
