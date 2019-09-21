package cn.com.yusong.yhdg.webserver.service.hdg;

import cn.com.yusong.yhdg.common.constant.ConstEnum;
import cn.com.yusong.yhdg.common.constant.Constant;
import cn.com.yusong.yhdg.common.domain.basic.DictCategory;
import cn.com.yusong.yhdg.common.domain.hdg.*;
import cn.com.yusong.yhdg.common.entity.json.DataResult;
import cn.com.yusong.yhdg.common.entity.json.ExtResult;
import cn.com.yusong.yhdg.common.entity.pagination.Page;
import cn.com.yusong.yhdg.common.utils.ExportXlsUtils;
import cn.com.yusong.yhdg.common.utils.IdUtils;
import cn.com.yusong.yhdg.common.utils.YhdgUtils;
import cn.com.yusong.yhdg.webserver.persistence.basic.DictItemMapper;
import cn.com.yusong.yhdg.webserver.persistence.basic.SystemConfigMapper;
import cn.com.yusong.yhdg.webserver.persistence.hdg.*;
import cn.com.yusong.yhdg.webserver.service.AbstractService;
import jxl.Sheet;
import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class CabinetChargerService extends AbstractService {

	static Logger log = LoggerFactory.getLogger(CabinetChargerService.class);

	@Autowired
	CabinetChargerMapper cabinetChargerMapper;

	public CabinetCharger findByCabinetBox(String cabinetId, String boxNum) {
		return cabinetChargerMapper.findByCabinetBox(cabinetId, boxNum);
	}

	public ExtResult update(CabinetCharger cabinetCharger) {
		StringBuilder builder = new StringBuilder();
		builder.append("000000");
		String abnormalStr = String.valueOf(cabinetCharger.getEnableNfc());
		String enableNfcStr = String.valueOf(cabinetCharger.getAbnormal());
		String binaryStr = builder.append(enableNfcStr).append(abnormalStr).toString();
		Integer other = Integer.valueOf(binaryStr, 2);
		cabinetCharger.setOther(other);

		if (cabinetChargerMapper.update(cabinetCharger) >= 1) {
			return ExtResult.successResult();
		} else {
			return ExtResult.failResult("修改失败");
		}
	}

}
