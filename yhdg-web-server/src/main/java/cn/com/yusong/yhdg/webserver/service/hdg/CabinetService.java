package cn.com.yusong.yhdg.webserver.service.hdg;

import cn.com.yusong.yhdg.common.constant.ConstEnum;
import cn.com.yusong.yhdg.common.constant.Constant;
import cn.com.yusong.yhdg.common.domain.AreaEntity;
import cn.com.yusong.yhdg.common.domain.basic.Area;
import cn.com.yusong.yhdg.common.domain.basic.DictItem;
import cn.com.yusong.yhdg.common.domain.basic.User;
import cn.com.yusong.yhdg.common.domain.hdg.*;
import cn.com.yusong.yhdg.common.entity.AreaCache;
import cn.com.yusong.yhdg.common.entity.CabinetBoxStats;
import cn.com.yusong.yhdg.common.entity.json.ExtResult;
import cn.com.yusong.yhdg.common.entity.pagination.Page;
import cn.com.yusong.yhdg.common.entity.tree.Node;
import cn.com.yusong.yhdg.common.entity.tree.NodeModel;
import cn.com.yusong.yhdg.common.tool.memcached.MemCachedClient;
import cn.com.yusong.yhdg.common.utils.AppUtils;
import cn.com.yusong.yhdg.common.utils.GeoHashUtils;
import cn.com.yusong.yhdg.common.utils.HttpClientUtils;
import cn.com.yusong.yhdg.common.utils.IdUtils;
import cn.com.yusong.yhdg.webserver.config.AppConfig;
import cn.com.yusong.yhdg.webserver.persistence.basic.UserMapper;
import cn.com.yusong.yhdg.webserver.persistence.hdg.*;
import cn.com.yusong.yhdg.webserver.persistence.yms.TerminalMapper;
import cn.com.yusong.yhdg.webserver.service.AbstractService;
import cn.com.yusong.yhdg.webserver.utils.MediaUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.codehaus.jackson.JsonEncoding;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.*;

@Service
public class CabinetService extends AbstractService {
    static Logger log = LoggerFactory.getLogger(CabinetService.class);

    @Autowired
    CabinetMapper cabinetMapper;
    @Autowired
    ShopMapper shopMapper;
    @Autowired
    CabinetBoxMapper cabinetBoxMapper;
    @Autowired
    TerminalMapper terminalMapper;
    @Autowired
    AppConfig config;
    @Autowired
    AreaCache areaCache;
    @Autowired
    MemCachedClient memCachedClient;
    @Autowired
    CabinetDynamicCodeCustomerMapper cabinetDynamicCodeCustomerMapper;
    @Autowired
    CabinetOperateLogMapper cabinetOperateLogMapper;
    @Autowired
    CabinetPropertyMapper cabinetPropertyMapper;
    @Autowired
    CabinetCurrentFaultMapper cabinetCurrentFaultMapper;
    @Autowired
    CabinetDayStatsMapper cabinetDayStatsMapper;
    @Autowired
    CabinetMonthStatsMapper cabinetMonthStatsMapper;
    @Autowired
    CabinetBatteryTypeMapper cabinetBatteryTypeMapper;
    @Autowired
    BatteryChargeRecordMapper batteryChargeRecordMapper;
    @Autowired
    BatteryOrderMapper batteryOrderMapper;
    @Autowired
    BatteryOrderRefundMapper batteryOrderRefundMapper;
    @Autowired
    KeepTakeOrderMapper keepTakeOrderMapper;
    @Autowired
    KeepPutOrderMapper keepPutOrderMapper;
    @Autowired
    KeepOrderMapper keepOrderMapper;
    @Autowired
    BackBatteryOrderMapper backBatteryOrderMapper;
    @Autowired
    BatteryMapper batteryMapper;
    @Autowired
    FaultFeedbackMapper faultFeedbackMapper;
    @Autowired
    CabinetInstallRecordMapper cabinetInstallRecordMapper;
    @Autowired
    CabinetIncomeTemplateMapper cabinetIncomeTemplateMapper;
    @Autowired
    CabinetAppMapper cabinetAppMapper;
    @Autowired
    VipPriceShopMapper vipPriceShopMapper;
    @Autowired
    UserMapper userMapper;


    public Page findPage(Cabinet search) {
        Page page = search.buildPage();
        page.setTotalItems(cabinetMapper.findPageCount(search));
        search.setBeginIndex(page.getOffset());
        List<Cabinet> list = cabinetMapper.findPageResult(search);
        for (Cabinet cabinet : list) {
            cabinet.setAgentName(findAgentInfo(cabinet.getAgentId()).getAgentName());
            //获取禁用格口数
            int unActiveBoxNum = cabinetBoxMapper.findUnActiveNum(cabinet.getId());
            cabinet.setUnActiveBoxNum(unActiveBoxNum);
        }
        page.setResult(setAreaProperties(areaCache, list));
        return page;
    }

    public List<Cabinet> findListByBatteryType(Integer batteryType) {
        return cabinetMapper.findListByBatteryType(batteryType);
    }

    public List<Cabinet> findListByShopId(String shopId) {
        return cabinetMapper.findListByShopId(shopId);
    }
    public List<Cabinet> findListByEstateId(long estateId) {
        return cabinetMapper.findListByEstateId(estateId);
    }

    public Cabinet find(String id) {
        Cabinet cabinet = (Cabinet) setAreaProperties(areaCache, cabinetMapper.find(id));
        if (cabinet != null) {
            if (cabinet.getAgentId() != null) {
                cabinet.setAgentName(findAgentInfo(cabinet.getAgentId()).getAgentName());
            }

        }
        return cabinet;
    }

    public Page findPageControl(Cabinet search) {
        List<CabinetBoxStats> cabinetBoxStatsList = new ArrayList<CabinetBoxStats>();
        Page page = search.buildPage();
        page.setTotalItems(cabinetMapper.findPageCount(search));
        search.setBeginIndex(page.getOffset());
        List<Cabinet> list = cabinetMapper.findPageResult(search);
        for (Cabinet c : list) {
            String id = c.getId();
            CabinetBoxStats cabinetBoxStats = new CabinetBoxStats();
            cabinetBoxStats.setAgentName(findAgentInfo(c.getAgentId()).getAgentName());
            cabinetBoxStats.setCabinetName(c.getCabinetName());
            cabinetBoxStats.setCabinetId(id);
            //换电柜格口数量
            cabinetBoxStats.setBoxCount(cabinetBoxMapper.statsBoxCount(c.getAgentId(), id));
            //空箱数量
            cabinetBoxStats.setEmptyCount(cabinetBoxMapper.statsBoxCountByStatus(id, CabinetBox.BoxStatus.EMPTY.getValue()));
            //正在充电的电池(充电中)
            cabinetBoxStats.setChargingCount(cabinetBoxMapper.statsCountByChargeStatus(id, Battery.ChargeStatus.CHARGING.getValue()));
            //待充数量(待充电)
            cabinetBoxStats.setWaitChargeCount(cabinetBoxMapper.statsCountByChargeStatus(id, Battery.ChargeStatus.WAIT_CHARGE.getValue()));
            //完成充电的数量(满电)
            cabinetBoxStats.setCompleteChargeCount(cabinetBoxMapper.statsCompleteChargeCount(id, Battery.Status.IN_BOX.getValue()));
            //未完成充电的数量(欠压)
            cabinetBoxStats.setNotCompleteChargeCount(cabinetBoxMapper.statsNotCompleteChargeCount(id, Battery.Status.IN_BOX.getValue()));
            cabinetBoxStatsList.add(cabinetBoxStats);
        }

        page.setResult(cabinetBoxStatsList);
        return page;
    }

    public Page statsPage(CabinetBox cabinetBox) {
        Page<CabinetBox> page = new Page<CabinetBox>();
        String cabinetId = cabinetBox.getCabinetId();
        if (cabinetBox.getViewFlag() == 1) { //换电柜格口数量
            List<CabinetBox> list = cabinetBoxMapper.statsBoxPage(cabinetId);
            page.setResult(list);
        } else if (cabinetBox.getViewFlag() == 2) { //空箱数量
            List<CabinetBox> list = cabinetBoxMapper.statsBoxPageByStatus(cabinetId, CabinetBox.BoxStatus.EMPTY.getValue());
            page.setResult(list);
        } else if (cabinetBox.getViewFlag() == 3) { //完成充电的数量(满电)
            List<CabinetBox> list = cabinetBoxMapper.statsCompleteChargePage(cabinetId, Battery.Status.IN_BOX.getValue());
            page.setResult(list);
        } else if (cabinetBox.getViewFlag() == 4) { //未完成充电的数量(欠压)
            List<CabinetBox> list = cabinetBoxMapper.statsNotCompleteChargePage(cabinetId, Battery.Status.IN_BOX.getValue());
            page.setResult(list);
        } else if (cabinetBox.getViewFlag() == 5) {  //待充数量(待充电)
            List<CabinetBox> list = cabinetBoxMapper.statsCountByChargeStatusPage(cabinetId, Battery.ChargeStatus.WAIT_CHARGE.getValue());
            page.setResult(list);
        } else if (cabinetBox.getViewFlag() == 6) { //正在充电的电池(充电中)
            List<CabinetBox> list = cabinetBoxMapper.statsCountByChargeStatusPage(cabinetId, Battery.ChargeStatus.CHARGING.getValue());
            page.setResult(list);
        }
        return page;
    }

    public int findCabinetCount() {
        return cabinetMapper.findCabinetCount();
    }

    public Map<String, List<CabinetState>> findBatteryStateList(String[] cabinetId) {
        List<DictItem> batteryTypeList = new ArrayList<DictItem>();
        Map<String, List<CabinetState>> map = new HashMap<String, List<CabinetState>>();

        for (String id : cabinetId) {
            List<CabinetState> batteryList = new ArrayList<CabinetState>();
            for (DictItem dictItem : batteryTypeList) {
                CabinetState cabinetState = cabinetBoxMapper.findCountByBatteryType(id, Battery.Status.IN_BOX.getValue(), Integer.valueOf(dictItem.getItemValue()));
                cabinetState.setType(Integer.valueOf(dictItem.getItemValue()));

                List<Integer> typeList = new ArrayList<Integer>();
                typeList.add(CabinetBox.TYPE_NOT_SUPPORT_CHARGE);
                typeList.add(Integer.valueOf(dictItem.getItemValue()));

                int emptyBoxCount = cabinetBoxMapper.findEmptyCountBatteryType(id, CabinetBox.BoxStatus.EMPTY.getValue(), typeList);
                cabinetState.setEmptyBoxCount(emptyBoxCount);
                batteryList.add(cabinetState);
            }
            for (CabinetState state : batteryList) {
                String type = findBatteryType(state.getType()).getTypeName();
                state.setBatteryTypeName(type);
            }
            map.put(id, batteryList);
        }
        return map;
    }

    public Cabinet findByTerminalId(String terminalId) {
        return cabinetMapper.findByTerminalId(terminalId);
    }

    @Transactional(rollbackFor = Throwable.class)
    public ExtResult delete(String id) {
        if (cabinetBoxMapper.statsBoxCount(null, id) > 0) {
            return ExtResult.failResult("存在分柜格口不能删除！");
        }
        if (cabinetPropertyMapper.findByCabinet(id).size() > 0) {
            return ExtResult.failResult("存在配置信息不能删除！");
        }
        if (batteryChargeRecordMapper.findCountByCabinet(id) > 0) {
            return ExtResult.failResult("存在充电记录不能删除！");
        }
        if (batteryOrderMapper.findCountByTakeCabinet(id) > 0) {
            return ExtResult.failResult("存在换电订单不能删除！");
        }
        if (batteryOrderMapper.findCountByPutCabinet(id) > 0) {
            return ExtResult.failResult("存在换电订单不能删除！");
        }
        if (batteryOrderRefundMapper.findCountByCabinet(id) > 0) {
            return ExtResult.failResult("存在换电退款订单不能删除！");
        }
        if (keepTakeOrderMapper.findCountByCabinet(id) > 0) {
            return ExtResult.failResult("存在收电订单不能删除！");
        }
        if (keepPutOrderMapper.findCountByCabinet(id) > 0) {
            return ExtResult.failResult("存在投电订单不能删除！");
        }
        if (keepOrderMapper.findCountByTakeCabinet(id) > 0) {
            return ExtResult.failResult("存在维护订单不能删除！");
        }
        if (keepOrderMapper.findCountByPutCabinet(id) > 0) {
            return ExtResult.failResult("存在维护订单不能删除！");
        }
        if (backBatteryOrderMapper.findCountByCabinet(id) > 0) {
            return ExtResult.failResult("存在退租电池订单不能删除！");
        }
        if (batteryMapper.findCountByCabinet(id) > 0) {
            return ExtResult.failResult("存在站点电池不能删除！");
        }
        if (cabinetOperateLogMapper.findCountByCabinet(id) > 0) {
            return ExtResult.failResult("存在柜子操作日志不能删除！");
        }
        if (faultFeedbackMapper.findCountByCabinet(id) > 0) {
            return ExtResult.failResult("存在故障反馈信息不能删除！");
        }
        if (cabinetDayStatsMapper.findCountByCabinet(id) > 0) {
            return ExtResult.failResult("存在日收入统计信息不能删除！");
        }
        if (cabinetMonthStatsMapper.findCountByCabinet(id) > 0) {
            return ExtResult.failResult("存在月收入统计信息不能删除！");
        }
        if (cabinetMapper.delete(id) == 0) {
            return ExtResult.failResult("删除失败！");
        }
        cabinetOperateLogMapper.delete(id);
        return ExtResult.successResult();
    }


    public ExtResult findUnique(String id) {
        Cabinet cabinet = cabinetMapper.findUnique(id);
        if (cabinet == null) {
            return ExtResult.successResult();
        }
        return ExtResult.failResult("终端编号重复");
    }

    @Transactional(rollbackFor = Throwable.class)
    public int bindBatteryType(String cabinetId, Integer batteryType) {
        CabinetBatteryType cabinetBatteryType = new CabinetBatteryType();
        cabinetBatteryType.setCabinetId(cabinetId);
        cabinetBatteryType.setBatteryType(batteryType);
        return cabinetBatteryTypeMapper.insert(cabinetBatteryType);
    }

    @Transactional(rollbackFor = Throwable.class)
    public int bindShop(String id, String shopId) {
        return cabinetMapper.bindShop(id, shopId);
    }
    @Transactional(rollbackFor = Throwable.class)
    public int bindEstate(String id, String estateId) {
        return cabinetMapper.bindEstate(id, estateId);
    }

    @Transactional(rollbackFor = Throwable.class)
    public ExtResult updateShop(Cabinet cabinet) {
        Shop shop;
        if (cabinet.getShopId() == null || (shop = shopMapper.find(cabinet.getShopId())) == null) {
            Shop newShop = new Shop();
            String maxId = findShopMaxId();
            newShop.setId(maxId);
            newShop.setAgentId(cabinet.getAgentId());
            newShop.setShopName(cabinet.getShopName());
            newShop.setLinkname(cabinet.getShopLinkname());
            newShop.setTel(cabinet.getShopTel());
            newShop.setIsAllowOpenBox(cabinet.getIsAllowOpenBox());
            newShop.setWorkTime(cabinet.getShopWorkTime());
            if (cabinet.getShopWorkTime().equals("-")) {
                newShop.setWorkTime(null);
            }
            newShop.setBalance(0);
            newShop.setActiveStatus(cabinet.getShopActiveStatus());
            newShop.setCreateTime(new Date());
            int result1 = shopMapper.insert(newShop);
            int result2 = cabinetMapper.bindShop(cabinet.getId(), newShop.getId());
            int result3 = cabinetMapper.bindIsAllowOpenBox(cabinet.getId(), newShop.getIsAllowOpenBox());

            if (result1 == 1 && result2 == 1 && result3 == 1) {
                return ExtResult.successResult();
            } else {
                return ExtResult.failResult("保存门店失败");
            }
        } else {
            shop.setAgentId(cabinet.getAgentId());
            shop.setShopName(cabinet.getShopName());
            shop.setLinkname(cabinet.getShopLinkname());
            shop.setTel(cabinet.getShopTel());
            shop.setWorkTime(cabinet.getShopWorkTime());
            shop.setIsAllowOpenBox(cabinet.getIsAllowOpenBox());
            if (cabinet.getShopWorkTime().equals("-")) {
                shop.setWorkTime(null);
            }
            shop.setBalance(cabinet.getShopBalance());
            shop.setActiveStatus(cabinet.getShopActiveStatus());
            int result1 = shopMapper.update(shop);
            int result2 = cabinetMapper.bindShop(cabinet.getId(), shop.getId());
            int result3 = cabinetMapper.bindIsAllowOpenBox(cabinet.getId(), shop.getIsAllowOpenBox());

            if (result1 == 1 && result2 == 1&& result3 == 1) {
                return ExtResult.successResult();
            } else {
                return ExtResult.failResult("保存门店失败");
            }
        }
    }

    public String findShopMaxId() {
        String date = DateFormatUtils.format(new Date(), "yyyyMMdd");
        String id = shopMapper.findMaxId(date);
        if (StringUtils.isEmpty(id)) {
            id = String.format("%s%0" + Constant.CABINET_ID_SEQUENCE_LENGTH + "d", date, 1);
        } else {
            String num = id.substring(id.length() - Constant.CABINET_ID_SEQUENCE_LENGTH);
            if (Long.parseLong(num) >= Math.pow(10, Constant.CABINET_ID_SEQUENCE_LENGTH) - 1) {
                throw new RuntimeException("今日门店数量已达到上限");
            }

            id = String.valueOf(Long.parseLong(id) + 1);
        }
        return id;
    }

    @Transactional(rollbackFor = Throwable.class)
    public ExtResult updateBasic(Cabinet cabinet, long userId) {
        Cabinet dbCabinet = cabinetMapper.find(cabinet.getId());
        if (dbCabinet == null) {
            return ExtResult.failResult("记录不存在");
        }

        boolean changed = false;
        if (dbCabinet.getTerminalId() == null && cabinet.getTerminalId() != null) {
            changed = true;
        } else if (dbCabinet.getTerminalId() != null && cabinet.getTerminalId() == null) {
            changed = true;
        } else if (!dbCabinet.getTerminalId().equals(cabinet.getTerminalId())) {
            changed = true;
        }

        cabinet.setKeyword(cabinet.getCabinetName() + StringUtils.trimToEmpty(dbCabinet.getAddress()));

        //如果修改了动态码,清空原先动态码和客户的绑定关系
        if (cabinet.getDynamicCode() != null) {
            if (!cabinet.getDynamicCode().equals(dbCabinet.getDynamicCode())) {
                //如果修改后的动态码不为空,必须为4位数字
                if (cabinet.getDynamicCode() != null && !cabinet.getDynamicCode().equals("")) {
                    if (cabinet.getDynamicCode().length() != 4) {
                        return ExtResult.failResult("动态码必须为4位数字!");
                    }
                }
                cabinetDynamicCodeCustomerMapper.deleteByCabinetId(cabinet.getId());
            }
        }
        if (cabinet.getUpLineStatus() == Cabinet.UpLineStatus.NOT_ONLINE.getValue()) {
            cabinet.setAgentId(1);
            cabinet.setUpLineTime(null);
        }

        if (dbCabinet.getUpLineStatus() == null && cabinet.getUpLineStatus() == Cabinet.UpLineStatus.ONLINE.getValue()) {
            cabinet.setUpLineTime(new Date());
        } else if (dbCabinet.getUpLineStatus() != Cabinet.UpLineStatus.ONLINE.getValue() && cabinet.getUpLineStatus() == Cabinet.UpLineStatus.ONLINE.getValue()) {
            cabinet.setUpLineTime(new Date());
        }

        if (cabinet.getWorkTime().equals("-")) {
            cabinet.setWorkTime(null);
        }

        if (cabinetMapper.update(cabinet) <= 0) {
            return ExtResult.failResult("修改失败！");
        }

        if (!dbCabinet.getAgentId().equals(cabinet.getAgentId())) {
            cabinetMapper.cleanDispatcherId(cabinet.getId());
            cabinetBoxMapper.updateAgentId(cabinet.getAgentId(), cabinet.getId());
            cabinetMapper.updateAgentId(cabinet.getAgentId(), cabinet.getId());
        }

        if (changed && StringUtils.isNotEmpty(dbCabinet.getLoginToken())) {
            String key = "token:" + dbCabinet.getLoginToken();
            memCachedClient.delete(key);
            if (log.isDebugEnabled()) {
                log.debug("柜子token删除了 {} token: {}", dbCabinet.getId(), key);
            }
        }

        cabinetBoxMapper.updateChargeFullVolume(cabinet.getId(), cabinet.getChargeFullVolume());

        if ((dbCabinet.getUpLineStatus() == null || dbCabinet.getUpLineStatus() != Cabinet.UpLineStatus.ONLINE.getValue()) && cabinet.getUpLineStatus() == Cabinet.UpLineStatus.ONLINE.getValue()) {
            User user = userMapper.find(userId);
            CabinetInstallRecord installRecord = new CabinetInstallRecord();
            installRecord.setAgentId(cabinet.getAgentId());
            installRecord.setAgentName(cabinet.getAgentName());
            installRecord.setCabinetId(cabinet.getId());
            installRecord.setAgentId(cabinet.getAgentId());
            installRecord.setAgentName(cabinet.getAgentName());
            installRecord.setCabinetName(cabinet.getCabinetName());
            installRecord.setTerminalId(cabinet.getTerminalId());
            installRecord.setLat(cabinet.getLat());
            installRecord.setLng(cabinet.getLng());
            installRecord.setPermitExchangeVolume(cabinet.getPermitExchangeVolume());
            installRecord.setChargeFullVolume(cabinet.getChargeFullVolume());
            installRecord.setCreateTime(new Date());
            installRecord.setImagePath1(cabinet.getImagePath1());
            installRecord.setImagePath2(cabinet.getImagePath2());
            installRecord.setMinExchangeVolume(cabinet.getMinExchangeVolume());
            installRecord.setBroker(user.getFullname());
            installRecord.setTel(user.getMobile());
            CabinetIncomeTemplate cabinetIncomeTemplate = cabinetIncomeTemplateMapper.findByAgentId(cabinet.getAgentId());
            installRecord.setStatus(CabinetInstallRecord.Status.APPROVE.getValue());
            if (cabinetIncomeTemplate != null) {
                installRecord.setForegiftMoney(cabinetIncomeTemplate.getForegiftMoney());
                installRecord.setRentMoney(cabinetIncomeTemplate.getRentMoney());
                installRecord.setRentPeriodType(cabinetIncomeTemplate.getPeriodType());
                installRecord.setRentExpireTime(cabinetIncomeTemplate.getRentExpireTime());
            } else {
                installRecord.setForegiftMoney(0);
                installRecord.setRentMoney(0);
                installRecord.setRentPeriodType(0);
            }
            cabinetInstallRecordMapper.insert(installRecord);
        }
        return ExtResult.successResult();
    }

    @Transactional(rollbackFor = Throwable.class)
    public ExtResult updateRatio(String id, Integer platformRatio, Integer agentRatio, Integer provinceAgentRatio, Integer cityAgentRatio, Integer shopRatio, Integer shopFixedMoney) {
        int total = cabinetMapper.updateRatio(id, platformRatio, agentRatio, provinceAgentRatio, cityAgentRatio, shopRatio, shopFixedMoney);
        if (total == 0) {
            return ExtResult.failResult("修改分成方式失败！");
        } else {
            return ExtResult.successResult();
        }
    }

    @Transactional(rollbackFor = Throwable.class)
    public ExtResult unbindBatteryType(String cabinetId, Integer batteryType) {
        cabinetBatteryTypeMapper.delete(cabinetId, batteryType);
        return ExtResult.successResult();
    }

    @Transactional(rollbackFor = Throwable.class)
    public ExtResult unbindShop(String cabinetId) {
        cabinetMapper.unbindShop(cabinetId);
        Cabinet cabinet = cabinetMapper.find(cabinetId);
        if (cabinet.getShopId() != null) {
            VipPriceShop vipPriceShop = vipPriceShopMapper.findByShopId(cabinet.getShopId());
            if (vipPriceShop != null) {
                updateShopPrice(vipPriceShop.getId(), cabinet.getShopId());
            }else {
                updateShopPriceByCabint(cabinet.getShopId());
            }
        }
        return ExtResult.successResult();
    }
    @Transactional(rollbackFor = Throwable.class)
    public ExtResult unbindEstate(String cabinetId) {
        cabinetMapper.unbindEstate(cabinetId);
        return ExtResult.successResult();
    }

    public ExtResult updateImage(Cabinet cabinet) {
        Cabinet dbCabinet = cabinetMapper.find(cabinet.getId());
        cabinet.setTerminalId(dbCabinet.getTerminalId());
        int total = cabinetMapper.update(cabinet);
        if (total == 0) {
            return ExtResult.failResult("修改失败！");
        }
        return ExtResult.successResult();
    }

    public ExtResult updateCustomerType(Cabinet cabinet) {
        Cabinet dbCabinet = cabinetMapper.find(cabinet.getId());
        cabinet.setTerminalId(dbCabinet.getTerminalId());
        int total = cabinetMapper.update(cabinet);
        if (total == 0) {
            return ExtResult.failResult("修改失败！");
        }
        return ExtResult.successResult();
    }

    private File[] imageTransfer(MultipartFile file) throws IOException {
        String uuid = IdUtils.uuid();
        String fileSuffix = AppUtils.getFileSuffix(file.getOriginalFilename());
        File sourceFile = new File(config.tempDir, uuid + "." + fileSuffix);
        File sourceFile2 = new File(config.tempDir, uuid + "." + fileSuffix);
        AppUtils.makeParentDir(sourceFile);
        AppUtils.makeParentDir(sourceFile2);
        FileUtils.copyInputStreamToFile(file.getInputStream(),sourceFile);
        FileUtils.copyInputStreamToFile(file.getInputStream(),sourceFile2);
        File[] files = {sourceFile, sourceFile2};
        return files;
    }

    public HttpClientUtils.HttpResp uploadImage(MultipartFile file, int num) throws IOException {

        String fileSuffix = AppUtils.getFileSuffix(file.getOriginalFilename());
        File[] files = imageTransfer(file);
        File targetFile1 = null;
        File targetFile2 = null;
        for (int i = 0; i < files.length; i++) {
            if (i == 0) {
                targetFile1 = cutDown(files[i], IdUtils.uuid(), fileSuffix, Constant.MAX_IMAGE_WIDTH);
            }
            if (i == 1) {
                targetFile2 = cutDown(files[i], IdUtils.uuid(), fileSuffix, Constant.SMALL_IMAGE_WIDTH);
            }
        }

        Map<String, File> fileMap = new HashMap<String, File>();
        fileMap.put(targetFile1.getName(), targetFile1);
        fileMap.put(targetFile2.getName(), targetFile2);

        HttpClientUtils.HttpResp httpResp = HttpClientUtils.uploadSomeImage(
                config.staticUrl + "/security/hdg_upload/cabinet_image.htm",
                fileMap,
                Collections.EMPTY_MAP,
                AppUtils.buildHttpHeader(System.currentTimeMillis(), config.uploadSalt));
        for (int i = 0; i < files.length; i++) {
            if (files[i].exists()) {
                files[i].delete();
            }
        }
        return httpResp;
    }

    protected File cutDown(File sourceFile, String uuid, String fileSuffix, int stand) throws IOException {
        if ("png".equals(fileSuffix)) {
            return sourceFile;
        }
        File targetFile = new File(sourceFile.getParentFile(), uuid + "." + fileSuffix);
        MediaUtils.MediaInfo mediaInfo = MediaUtils.getImageInfo(sourceFile);

        mediaInfo = MediaUtils.getImageSnapshotSize(mediaInfo, stand);
        MediaUtils.makeImageSnapshot(sourceFile, targetFile, fileSuffix, mediaInfo.width, mediaInfo.height);
        return targetFile;
    }

    @Transactional(rollbackFor = Throwable.class)
    public ExtResult updateLocation(Cabinet cabinet) {
        Cabinet dbCabinet = cabinetMapper.find(cabinet.getId());
        if (dbCabinet == null) {
            return ExtResult.failResult("记录不存在");
        }

        cabinet.setGeoHash(GeoHashUtils.getGeoHashString(cabinet.getLng(), cabinet.getLat()));
        AreaEntity areaEntity = setAreaProperties(areaCache, cabinet);
        String address = StringUtils.trimToEmpty(areaEntity.getProvinceName()) + StringUtils.trimToEmpty(areaEntity.getCityName()) + StringUtils.trimToEmpty(areaEntity.getDistrictName()) + StringUtils.trimToEmpty(areaEntity.getStreet());
        cabinet.setAddress(address);

        cabinet.setKeyword(dbCabinet.getCabinetName() + address);
        cabinet.setTerminalId(dbCabinet.getTerminalId());
        int effect = cabinetMapper.update(cabinet);
        if (effect == 0) {
            return ExtResult.failResult("修改失败！");
        }
        return ExtResult.successResult();
    }

    @Transactional(rollbackFor = Throwable.class)
    public ExtResult updateNewLocation(Cabinet cabinet) {
        Cabinet dbCabinet = cabinetMapper.find(cabinet.getId());
        if (dbCabinet == null) {
            return ExtResult.failResult("记录不存在");
        }
        cabinet.setGeoHash(GeoHashUtils.getGeoHashString(cabinet.getLng(), cabinet.getLat()));
        if (cabinet.getProvinceName() != null) {
            if (cabinet.getProvinceName().substring(cabinet.getProvinceName().length() - 1).equals("市")) {
                cabinet.setProvinceName(cabinet.getProvinceName().substring(0, cabinet.getProvinceName().length() - 1));
            }
            Area area = areaCache.getByName(cabinet.getProvinceName());
            if (area != null) {
                cabinet.setProvinceId(Integer.valueOf(area.getAreaCode()));
            }
        }
        if (cabinet.getCityName() != null) {
            Area area = areaCache.getByName(cabinet.getCityName());
            if (area != null) {
                cabinet.setCityId(Integer.valueOf(area.getAreaCode()));
            }
        }
        if (cabinet.getDistrictName() != null) {
            Area area = areaCache.getByName(cabinet.getDistrictName());
            if (area != null) {
                cabinet.setDistrictId(Integer.valueOf(area.getAreaCode()));
            }
        }
        String street = null;
        if (cabinet.getStreetName() != null) {
            street = cabinet.getStreetName();
        }
        if (cabinet.getStreetNumber() != null) {
            street = cabinet.getStreetNumber();
        }
        if (cabinet.getStreetName() != null && cabinet.getStreetNumber() != null) {
            street = cabinet.getStreetName() + cabinet.getStreetNumber();
        }
        cabinet.setStreet(street);
        if (dbCabinet.getCabinetName() != null && cabinet.getAddress() != null) {
            cabinet.setKeyword(dbCabinet.getCabinetName() + cabinet.getAddress());
        }
        cabinet.setWorkTime(dbCabinet.getWorkTime());
        cabinet.setUpLineTime(dbCabinet.getUpLineTime());
        cabinet.setTerminalId(dbCabinet.getTerminalId());
        int effect = cabinetMapper.update(cabinet);
        if (effect == 0) {
            return ExtResult.failResult("修改失败！");
        }
        return ExtResult.successResult("操作成功");
    }

    public List<Cabinet> findByAgent(long agentId, Long dispatcherId, Integer provinceId, Integer cityId, Integer districtId) {
        return cabinetMapper.findByAgent(agentId, dispatcherId, provinceId, cityId, districtId);
    }

    @Transactional(rollbackFor = Throwable.class)
    public ExtResult updateDispatcherById(String id) {
        int effect = cabinetMapper.updateDispatcherById(id, null);
        if (effect == 0) {
            return ExtResult.failResult("删除失败！");
        }
        return ExtResult.successResult();
    }

    @Transactional(rollbackFor = Throwable.class)
    public ExtResult updateOnline(String id, Integer foregiftMoney, Integer rentMoney, Integer rentPeriodType, Date rentExpireTime, Integer activePlatformDeduct, Integer platformDeductMoney, Date platformDeductExpireTime) {
        int effect = cabinetMapper.updateUpline(id, foregiftMoney, rentMoney, rentPeriodType, rentExpireTime, activePlatformDeduct, platformDeductMoney, platformDeductExpireTime);
        if (effect == 0) {
            return ExtResult.failResult("修改失败");
        }
        return ExtResult.successResult();
    }

    public void tree(Set<Integer> checked, String dummy, Integer agentId, ServletOutputStream stream) throws IOException {

        List<Cabinet> list = cabinetMapper.findByAgentIdList(agentId);

        List<Node<NodeModel>> roots = new ArrayList<Node<NodeModel>>();
        for (Cabinet cabinet : list) {
            NodeModel nodeModel = new NodeModel();
            Node<NodeModel> root = new Node<NodeModel>(nodeModel);
            nodeModel.setId(cabinet.getId());
            nodeModel.setName(cabinet.getCabinetName());
            roots.add(root);
        }

        ObjectMapper objectMapper = new ObjectMapper();
        JsonGenerator json = objectMapper.getJsonFactory().createJsonGenerator(stream, JsonEncoding.UTF8);
        json.writeStartArray();
        AppUtils.writeJson(roots, json);
        json.writeEndArray();
        json.flush();
        json.close();
    }

    public void tree(String dummy, Integer agentId, OutputStream stream) throws IOException {
        tree(buildTree(dummy, agentId), stream);
    }

    private List<Node<NodeModel>> buildTree(String dummy, Integer agentId) {
        Set<String> checked = Collections.emptySet();
        List<Cabinet> cabinetList = cabinetMapper.findByAgentIdList(agentId);
        if (cabinetList.size() == ConstEnum.Flag.FALSE.getValue()) {
            cabinetList = cabinetMapper.findAll();
        }
        List<Node<NodeModel>> roots = new ArrayList<Node<NodeModel>>();
        if (StringUtils.isNotEmpty(dummy)) {
            NodeModel data = new NodeModel();
            Node<NodeModel> root = new Node<NodeModel>(data);
            data.setId("");
            data.setName(dummy);
            roots.add(root);

            for (Cabinet cabinet : cabinetList) {
                NodeModel nodeModel = new NodeModel();
                Node<NodeModel> node = new Node<NodeModel>(nodeModel, root);

                nodeModel.setId(cabinet.getId());
                nodeModel.setName(cabinet.getCabinetName());
                nodeModel.setCheckStatus(checked.contains(cabinet.getId()) ? NodeModel.CheckedStatus.checked : NodeModel.CheckedStatus.unchecked);
            }

        } else {
            for (Cabinet top : cabinetList) {
                NodeModel nodeModel = new NodeModel();
                Node<NodeModel> node = new Node<NodeModel>(nodeModel);

                nodeModel.setId(top.getId());
                nodeModel.setCheckStatus(checked.contains(top.getId()) ? NodeModel.CheckedStatus.checked : NodeModel.CheckedStatus.unchecked);
                roots.add(node);
            }
        }
        return roots;
    }

    private void tree(List<Node<NodeModel>> roots, OutputStream stream) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonGenerator json = objectMapper.getJsonFactory().createJsonGenerator(stream, JsonEncoding.UTF8);
        json.writeStartArray();
        AppUtils.writeJson(roots, json);

        json.writeEndArray();
        json.flush();
        json.close();
    }


    public void cabinetListTree(Long cabinetCompanyId, ServletOutputStream stream) throws IOException {

        List<Cabinet> list = cabinetMapper.findCabinetList(cabinetCompanyId);

        List<Node<NodeModel>> roots = new ArrayList<Node<NodeModel>>();
        for (Cabinet cabinet : list) {
            NodeModel nodeModel = new NodeModel();
            Node<NodeModel> root = new Node<NodeModel>(nodeModel);
            nodeModel.setId(cabinet.getId());
            nodeModel.setName(cabinet.getCabinetName());
            roots.add(root);
        }

        ObjectMapper objectMapper = new ObjectMapper();
        JsonGenerator json = objectMapper.getJsonFactory().createJsonGenerator(stream, JsonEncoding.UTF8);
        json.writeStartArray();
        AppUtils.writeJson(roots, json);
        json.writeEndArray();
        json.flush();
        json.close();
    }

    public void cabinetTree(Long cabinetCompanyId, OutputStream stream) throws IOException {
        cabinetTree(buildCabinetTree(cabinetCompanyId), stream);
    }

    private List<Node<NodeModel>> buildCabinetTree(Long cabinetCompanyId) {
        Set<String> checked = Collections.emptySet();
        List<Cabinet> cabinetList = cabinetMapper.findCabinetList(cabinetCompanyId);
        List<Node<NodeModel>> roots = new ArrayList<Node<NodeModel>>();
        for (Cabinet top : cabinetList) {
            NodeModel nodeModel = new NodeModel();
            Node<NodeModel> node = new Node<NodeModel>(nodeModel);

            nodeModel.setId(top.getId());
            nodeModel.setCheckStatus(checked.contains(top.getId()) ? NodeModel.CheckedStatus.checked : NodeModel.CheckedStatus.unchecked);
            roots.add(node);
        }
        return roots;
    }

    private void cabinetTree(List<Node<NodeModel>> roots, OutputStream stream) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonGenerator json = objectMapper.getJsonFactory().createJsonGenerator(stream, JsonEncoding.UTF8);
        json.writeStartArray();
        AppUtils.writeJson(roots, json);

        json.writeEndArray();
        json.flush();
        json.close();
    }
}
