package cn.com.yusong.yhdg.webserver.web.controller.security.hdg;

import cn.com.yusong.yhdg.common.annotation.SecurityControl;
import cn.com.yusong.yhdg.common.annotation.ViewModel;
import cn.com.yusong.yhdg.common.constant.ConstEnum;
import cn.com.yusong.yhdg.common.constant.Constant;
import cn.com.yusong.yhdg.common.domain.basic.AgentBatteryType;
import cn.com.yusong.yhdg.common.domain.basic.DictCategory;
import cn.com.yusong.yhdg.common.domain.hdg.*;
import cn.com.yusong.yhdg.common.entity.json.DataResult;
import cn.com.yusong.yhdg.common.entity.json.ExtResult;
import cn.com.yusong.yhdg.common.entity.json.PageResult;
import cn.com.yusong.yhdg.common.utils.YhdgUtils;
import cn.com.yusong.yhdg.webserver.entity.SessionUser;
import cn.com.yusong.yhdg.webserver.service.basic.AgentService;
import cn.com.yusong.yhdg.webserver.service.basic.DictItemService;
import cn.com.yusong.yhdg.webserver.service.basic.SystemConfigService;
import cn.com.yusong.yhdg.webserver.service.hdg.*;
import cn.com.yusong.yhdg.webserver.web.controller.security.basic.SecurityController;
import jxl.read.biff.BiffException;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;


@Controller
@RequestMapping(value = "/security/hdg/cabinet_charger")
public class CabinetChargerController extends SecurityController {
	static Logger log = LoggerFactory.getLogger(CabinetChargerController.class);

	@Autowired
	CabinetChargerService cabinetChargerService;

	@RequestMapping(value = "info.htm")
	public void cabinetChargerDetail(Model model, String cabinetId, String boxNum) {
		CabinetCharger entity = cabinetChargerService.findByCabinetBox(cabinetId, boxNum);
		if (entity.getOther() != null) {
			int[] others = CabinetCharger.parseOther(entity.getOther());
			entity.setEnableNfc(others[0]);
			entity.setAbnormal(others[1]);
		}
		model.addAttribute("entity", entity);
	}

	@RequestMapping(value = "edit.htm")
	public String edit(Model model, String cabinetId, String boxNum) {
		CabinetCharger entity = cabinetChargerService.findByCabinetBox(cabinetId, boxNum);
		if (entity == null) {
			return SEGMENT_RECORD_NOT_FOUND;
		} else {
			if (entity.getOther() != null) {
				int[] others = CabinetCharger.parseOther(entity.getOther());
				entity.setEnableNfc(others[0]);
				entity.setAbnormal(others[1]);
			}
			model.addAttribute("entity", entity);
		}
		return "/security/hdg/cabinet_charger/edit";
	}

	@RequestMapping("update.htm")
	@ViewModel(ViewModel.JSON)
	@ResponseBody
	public ExtResult update(CabinetCharger entity) {
		return cabinetChargerService.update(entity);
	}

}
