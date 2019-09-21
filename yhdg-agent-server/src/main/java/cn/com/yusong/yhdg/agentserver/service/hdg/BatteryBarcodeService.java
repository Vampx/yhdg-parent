package cn.com.yusong.yhdg.agentserver.service.hdg;

import cn.com.yusong.yhdg.common.domain.hdg.BatteryBarcode;
import cn.com.yusong.yhdg.common.domain.hdg.BatteryCellRegular;
import cn.com.yusong.yhdg.common.entity.json.DataResult;
import cn.com.yusong.yhdg.common.entity.json.ExtResult;
import cn.com.yusong.yhdg.common.entity.pagination.Page;
import cn.com.yusong.yhdg.agentserver.persistence.hdg.BatteryBarcodeMapper;
import cn.com.yusong.yhdg.agentserver.persistence.hdg.BatteryCellRegularMapper;
import cn.com.yusong.yhdg.agentserver.service.AbstractService;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

@Service
public class BatteryBarcodeService extends AbstractService {

    @Autowired
    BatteryBarcodeMapper batteryBarcodeMapper;
    @Autowired
    BatteryCellRegularMapper batteryCellRegularMapper;

    public String findMaxCode(Long batteryFormatId) {
        return batteryBarcodeMapper.findMaxCode(batteryFormatId);
    }

    public BatteryBarcode findMaxCodeBatteryBarcode(Long batteryFormatId) {
        return batteryBarcodeMapper.findMaxCodeBatteryBarcode(batteryFormatId);
    }

    public List<BatteryBarcode> findList(Long batteryFormatId) {
        return batteryBarcodeMapper.findList(batteryFormatId);
    }

    public ExtResult findByBarcode(String barcode, String username) {
        BatteryBarcode batteryBarcode = batteryBarcodeMapper.findByBarcode(barcode);
        if (batteryBarcode == null) {
            return ExtResult.failResult("电池条码不存在");
        }
        batteryBarcode.setCreateTime(new Date());
        batteryBarcode.setOperator(username);
        return DataResult.successResult(batteryBarcode);
    }

    public Page findPage(BatteryBarcode search) {
        Page page = search.buildPage();
        page.setTotalItems(batteryBarcodeMapper.findPageCount(search));
        search.setBeginIndex(page.getOffset());
        List<BatteryBarcode> list = batteryBarcodeMapper.findPageResult(search);
        page.setResult(list);
        return page;
    }

    public ExtResult checkCodeCount(String equipmentNo, int num, int codeCount) {
        String text = equipmentNo;
        Calendar calendar = new GregorianCalendar();
        text = text.replaceAll("YYYY", DateFormatUtils.format(calendar, "yyyy"))
                .replaceAll("YY", DateFormatUtils.format(calendar, "yy"))
                .replaceAll("MM", DateFormatUtils.format(calendar, "MM"))
                .replaceAll("DD", DateFormatUtils.format(calendar, "dd"))
                .replaceAll("WW", StringUtils.leftPad(DateFormatUtils.format(calendar, "w"), 2, '0'));

        int length = 0;
        for (char c : text.toCharArray()) {
            if (c == 'N') {
                length++;
            }
        }

        String numStr = String.format("%d", num + codeCount);
        if (numStr.length() > length) {
            return ExtResult.failResult("流水号长度不够，生成失败，建议修改条码规则");
        } else {
            return ExtResult.successResult();
        }
    }

    @Transactional(rollbackFor=Throwable.class)
    public ExtResult create(BatteryBarcode batteryBarcode) {
        String equipmentNo;
        BatteryCellRegular regular = batteryCellRegularMapper.findByBatteryFormatIdAndType(batteryBarcode.getBatteryFormatId(), BatteryCellRegular.RegularType.BATTERY_FORMAT.getValue());
        ExtResult extResult = checkCodeCount(regular.getRegular(), regular.getNum(), batteryBarcode.getCodeCount());
        if (extResult.isSuccess()) {
            for (int i = 1; i <= batteryBarcode.getCodeCount(); i++) {
                equipmentNo = getNewEquipmentNo(regular.getRegular(), regular.getNum(), i);
                batteryBarcode.setBarcode(equipmentNo);
                batteryBarcode.setCreateTime(new Date());
                batteryBarcodeMapper.insert(batteryBarcode);
            }
            regular.setNum(regular.getNum()+batteryBarcode.getCodeCount());
            batteryCellRegularMapper.updateNumByBatteryFormatId(regular.getBatteryFormatId(), regular.getNum());
            return ExtResult.successResult();
        } else {
            return extResult;
        }
    }

    public String getNewEquipmentNo(String equipmentNo, int num, int str) {

        String text = equipmentNo;
        Calendar calendar = new GregorianCalendar();
        text = text.replaceAll("YYYY", DateFormatUtils.format(calendar, "yyyy"))
                .replaceAll("YY", DateFormatUtils.format(calendar, "yy"))
                .replaceAll("MM", DateFormatUtils.format(calendar, "MM"))
                .replaceAll("DD", DateFormatUtils.format(calendar, "dd"))
                .replaceAll("WW", StringUtils.leftPad(DateFormatUtils.format(calendar, "w"), 2, '0'));

        int length = 0;
        for(char c : text.toCharArray()) {
            if(c == 'N') {
                length++;
            }
        }

        String numStr = String.format("%d", num+str);
        if(numStr.length() > length) {
            throw new IllegalArgumentException(String.format("流水号长度越界, %s, %s", equipmentNo, numStr));
        }
        numStr = StringUtils.leftPad(numStr, length, '0');
        char[] numChars = numStr.toCharArray();
        int index = 0;
        StringBuilder builder = new StringBuilder();
        for(char c : text.toCharArray()) {
            if(c == 'N') {
                builder.append(numChars[index++]);
            } else {
                builder.append(c);
            }
        }

        return builder.toString();
    }

    @Transactional(rollbackFor=Throwable.class)
    public ExtResult delete(long id) {
        batteryBarcodeMapper.delete(id);
        return ExtResult.successResult();
    }

    @Transactional(rollbackFor=Throwable.class)
    public ExtResult batchDelete(long[] idList) {
        int i = 0;
        for (long id : idList) {
            i += batteryBarcodeMapper.delete(id);
        }
        return DataResult.successResult("成功删除" + i + "条电池条码");
    }
}

