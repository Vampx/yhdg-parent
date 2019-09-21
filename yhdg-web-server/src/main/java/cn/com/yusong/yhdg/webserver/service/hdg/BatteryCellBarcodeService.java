package cn.com.yusong.yhdg.webserver.service.hdg;

import cn.com.yusong.yhdg.common.domain.hdg.BatteryCellBarcode;
import cn.com.yusong.yhdg.common.domain.hdg.BatteryCellRegular;
import cn.com.yusong.yhdg.common.entity.json.DataResult;
import cn.com.yusong.yhdg.common.entity.json.ExtResult;
import cn.com.yusong.yhdg.common.entity.pagination.Page;
import cn.com.yusong.yhdg.webserver.persistence.hdg.BatteryCellBarcodeMapper;
import cn.com.yusong.yhdg.webserver.persistence.hdg.BatteryCellRegularMapper;
import cn.com.yusong.yhdg.webserver.service.AbstractService;
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
public class BatteryCellBarcodeService extends AbstractService {

    @Autowired
    BatteryCellBarcodeMapper batteryCellBarcodeMapper;
    @Autowired
    BatteryCellRegularMapper batteryCellRegularMapper;

    public String findMaxCode(Long cellFormatId) {
        return batteryCellBarcodeMapper.findMaxCode(cellFormatId);
    }

    public BatteryCellBarcode findMaxCodeCellBarcode(Long cellFormatId) {
        return batteryCellBarcodeMapper.findMaxCodeCellBarcode(cellFormatId);
    }

    public List<BatteryCellBarcode> findList(Long cellFormatId) {
        return batteryCellBarcodeMapper.findList(cellFormatId);
    }

    public ExtResult findByBarcode(String barcode, String username) {
        BatteryCellBarcode batteryCellBarcode = batteryCellBarcodeMapper.findByBarcode(barcode);
        if (batteryCellBarcode == null) {
            return ExtResult.failResult("电芯条码不存在");
        }
        batteryCellBarcode.setCreateTime(new Date());
        batteryCellBarcode.setOperator(username);
        return DataResult.successResult(batteryCellBarcode);
    }

    public Page findPage(BatteryCellBarcode search) {
        Page page = search.buildPage();
        page.setTotalItems(batteryCellBarcodeMapper.findPageCount(search));
        search.setBeginIndex(page.getOffset());
        List<BatteryCellBarcode> list = batteryCellBarcodeMapper.findPageResult(search);
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
    public ExtResult create(BatteryCellBarcode batteryCellBarcode) {
        String equipmentNo;
        BatteryCellRegular regular = batteryCellRegularMapper.findByCellFormatIdAndType(batteryCellBarcode.getCellFormatId(),BatteryCellRegular.RegularType.CELL_FORMAT.getValue());
        ExtResult extResult = checkCodeCount(regular.getRegular(), regular.getNum(), batteryCellBarcode.getCodeCount());
        if (extResult.isSuccess()) {
            for (int i = 1; i <= batteryCellBarcode.getCodeCount(); i++) {
                equipmentNo = getNewEquipmentNo(regular.getRegular(), regular.getNum(), i);
                batteryCellBarcode.setBarcode(equipmentNo);
                batteryCellBarcode.setCreateTime(new Date());
                batteryCellBarcodeMapper.insert(batteryCellBarcode);
            }
            regular.setNum(regular.getNum() + batteryCellBarcode.getCodeCount());
            batteryCellRegularMapper.updateNumByCellFormatId(regular.getCellFormatId(), regular.getNum());
            return ExtResult.successResult();
        } else {
            return extResult;
        }
    }

    public ExtResult update(BatteryCellBarcode batteryCellBarcode) {
        batteryCellBarcodeMapper.update(batteryCellBarcode);
        return ExtResult.successResult();
    }

    @Transactional(rollbackFor=Throwable.class)
    public ExtResult delete(long id) {
        batteryCellBarcodeMapper.delete(id);
        return ExtResult.successResult();
    }

    @Transactional(rollbackFor=Throwable.class)
    public ExtResult batchDelete(long[] idList) {
        int i = 0;
        for (long id : idList) {
            i += batteryCellBarcodeMapper.delete(id);
        }
        return DataResult.successResult("成功删除" + i + "条电芯条码");
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


}

