package cn.com.yusong.yhdg.agentserver.service.zc;

import cn.com.yusong.yhdg.agentserver.config.AppConfig;
import cn.com.yusong.yhdg.agentserver.persistence.basic.CustomerMapper;
import cn.com.yusong.yhdg.agentserver.persistence.hdg.BatteryMapper;
import cn.com.yusong.yhdg.agentserver.persistence.hdg.CabinetMapper;
import cn.com.yusong.yhdg.agentserver.persistence.hdg.ShopStoreBatteryMapper;
import cn.com.yusong.yhdg.agentserver.persistence.zc.ShopRentCarMapper;
import cn.com.yusong.yhdg.agentserver.service.AbstractService;
import cn.com.yusong.yhdg.agentserver.utils.MediaUtils;
import cn.com.yusong.yhdg.common.constant.ConstEnum;
import cn.com.yusong.yhdg.common.constant.Constant;
import cn.com.yusong.yhdg.common.domain.basic.AgentInfo;
import cn.com.yusong.yhdg.common.domain.basic.Area;
import cn.com.yusong.yhdg.common.domain.basic.Customer;
import cn.com.yusong.yhdg.common.domain.hdg.Cabinet;
import cn.com.yusong.yhdg.common.domain.hdg.Shop;
import cn.com.yusong.yhdg.common.domain.hdg.ShopStoreBattery;
import cn.com.yusong.yhdg.common.entity.AreaCache;
import cn.com.yusong.yhdg.common.entity.json.DataResult;
import cn.com.yusong.yhdg.common.entity.json.ExtResult;
import cn.com.yusong.yhdg.common.entity.pagination.Page;
import cn.com.yusong.yhdg.common.utils.AppUtils;
import cn.com.yusong.yhdg.common.utils.GeoHashUtils;
import cn.com.yusong.yhdg.common.utils.HttpClientUtils;
import cn.com.yusong.yhdg.common.utils.IdUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.*;

@Service
public class ShopRentCarService extends AbstractService {

    @Autowired
    ShopRentCarMapper shopMapper;
    @Autowired
    AreaCache areaCache;
    @Autowired
    BatteryMapper batteryMapper;
    @Autowired
    CabinetMapper cabinetMapper;
    @Autowired
    CustomerMapper customerMapper;
    @Autowired
    ShopStoreBatteryMapper shopStoreBatteryMapper;
    @Autowired
    AppConfig config;

    public Page Page(Shop search) {
        Page page = search.buildPage();
        page.setTotalItems(shopMapper.findPageCount(search));
        search.setBeginIndex(page.getOffset());
        List<Shop> list = shopMapper.findPageResult(search);
        for (Shop shop : list) {
            AgentInfo agentInfo = findAgentInfo(shop.getAgentId());
            if (agentInfo != null) {
                shop.setAgentName(agentInfo.getAgentName());
            }
        }
        page.setResult(setAreaProperties(areaCache, list));
        return page;
    }

    public String findMaxId() {
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

    public Shop find(String id) {
        Shop shop = (Shop) setAreaProperties(areaCache, shopMapper.find(id));
        if (shop != null) {
            if (shop.getAgentId() != null) {
                shop.setAgentName(findAgentInfo(shop.getAgentId()).getAgentName());
            }
        }
        return shop;
    }

    @Transactional(rollbackFor = Throwable.class)
    public ExtResult delete(String id) {
        List<Cabinet> cabinetList = cabinetMapper.findListByShopId(id);
        if (cabinetList.size() > 0) {
            return ExtResult.failResult("该门店已绑定换电站，无法删除");
        }
        List<ShopStoreBattery> shopStoreBatteryList = shopStoreBatteryMapper.findByShopId(id);
        if (shopStoreBatteryList.size() > 0) {
            return ExtResult.failResult("该门店存在库存电池，无法删除");
        }
        if (shopMapper.delete(id) == 0) {
            return ExtResult.failResult("删除失败！");
        }
        return ExtResult.successResult();
    }

    public ExtResult insert(Shop shop) {
        if (shop.getId() == null) {
            shop.setId(findMaxId());
        }
        if (shop.getProvinceId() == null) {
            shop.setProvinceId(Constant.PROVINCE_ID);
        }
        if (shop.getCityId() == null) {
            shop.setCityId(Constant.CITY_ID);
        }
        if (shop.getDistrictId() == null) {
            shop.setDistrictId(Constant.DISTRICT_ID);
        }
        if (shop.getLng() == null) {
            shop.setLng(Constant.LNG);
        }
        if (shop.getLat() == null) {
            shop.setLat(Constant.LAT);
        }
        shop.setBalance(0);
        if (shop.getWorkTime().equals("-")) {
            shop.setWorkTime(null);
        }
        shop.setIsAllowOpenBox(ConstEnum.Flag.FALSE.getValue());
        shop.setIsExchange(ConstEnum.Flag.FALSE.getValue());
        shop.setIsRent(ConstEnum.Flag.FALSE.getValue());
        shop.setIsVehicle(ConstEnum.Flag.TRUE.getValue());
        shop.setCreateTime(new Date());
        if (shopMapper.insert(shop) == 0) {
            return DataResult.failResult("保存失败", null);
        }else{
            updateNewLocation(shop);
        }
        return DataResult.successResult("操作成功",shop.getId());
    }

    @Transactional(rollbackFor = Throwable.class)
    public ExtResult updateBasic(Shop shop) {
        Shop entity = shopMapper.find(shop.getId());
        if (entity == null) {
            return ExtResult.failResult("记录不存在");
        }
        List<Cabinet> cabinetList = cabinetMapper.findListByShopId(shop.getId());
        if (cabinetList.size() > 0) {
            for (Cabinet cabinet : cabinetList) {
                cabinetMapper.bindIsAllowOpenBox(cabinet.getId(), shop.getIsAllowOpenBox());
            }
        }
        if (shop.getWorkTime().equals("-")) {
            shop.setWorkTime(null);
        }
        if (shopMapper.update(shop) == 0) {
            return ExtResult.failResult("修改失败！");
        }

        return ExtResult.successResult();
    }

    @Transactional(rollbackFor = Throwable.class)
    public ExtResult updateRatio(String id, Integer zcPlatformRatio, Integer zcAgentRatio, Integer zcProvinceAgentRatio, Integer zcCityAgentRatio, Integer zcShopRatio, Integer zcShopFixedMoney) {
        int total = shopMapper.updateZcRatio(id, zcPlatformRatio, zcAgentRatio, zcProvinceAgentRatio, zcCityAgentRatio, zcShopRatio, zcShopFixedMoney);
        if (total == 0) {
            return ExtResult.failResult("修改分成方式失败！");
        } else {
            return ExtResult.successResult();
        }
    }

    public ExtResult updateNewLocation(Shop shop) {
        Shop entity = shopMapper.find(shop.getId());
        if (entity == null) {
            return ExtResult.failResult("记录不存在");
        }
        if (shop.getProvinceName() != null) {
            if (shop.getProvinceName().substring(shop.getProvinceName().length() - 1).equals("市")) {
                shop.setProvinceName(shop.getProvinceName().substring(0, shop.getProvinceName().length() - 1));
            }
            Area area = areaCache.getByName(shop.getProvinceName());
            if (area != null) {
                shop.setProvinceId(Integer.valueOf(area.getAreaCode()));
            }
        }
        if (shop.getCityName() != null) {
            Area area = areaCache.getByName(shop.getCityName());
            if (area != null) {
                shop.setCityId(Integer.valueOf(area.getAreaCode()));
            }
        }
        if (shop.getDistrictName() != null) {
            Area area = areaCache.getByName(shop.getDistrictName());
            if (area != null) {
                shop.setDistrictId(Integer.valueOf(area.getAreaCode()));
            }
        }
        shop.setGeoHash(GeoHashUtils.getGeoHashString(shop.getLng(), shop.getLat()));
//        AreaEntity areaEntity = setAreaProperties(areaCache, shop);
//        String address = StringUtils.trimToEmpty(areaEntity.getProvinceName()) + StringUtils.trimToEmpty(areaEntity.getCityName()) + StringUtils.trimToEmpty(areaEntity.getDistrictName()) + StringUtils.trimToEmpty(areaEntity.getStreet());
//        shop.setAddress(address);
        String street = null;
        if (shop.getStreetName() != null) {
            street = shop.getStreetName();
        }
        if (shop.getStreetNumber() != null) {
            street = shop.getStreetNumber();
        }
        if (shop.getStreetName() != null && shop.getStreetNumber() != null) {
            street = shop.getStreetName() + shop.getStreetNumber();
        }
        shop.setStreet(street);
        shop.setKeyword(shop.getAddress() + entity.getShopName());
//        String address = StringUtils.trimToEmpty(StringUtils.trimToEmpty(shop.getProvinceName()) + StringUtils.trimToEmpty(shop.getCityName()) + StringUtils.trimToEmpty(shop.getDistrictName()) + StringUtils.trimToEmpty(shop.getStreet()));
//        shop.setAddress(address);
        int effect = shopMapper.update(shop);
        if (effect == 0) {
            return ExtResult.failResult("修改失败！");
        }
        return ExtResult.successResult("操作成功");
    }

    @Transactional(rollbackFor = Throwable.class)
    public ExtResult setPayPeopleUpdate(Shop entity) {
        Shop shopPayPeopleMobile = shopMapper.findByPayPeopleMobile(entity.getPayPeopleMobile());
        if (shopPayPeopleMobile != null){
            return ExtResult.failResult("收款人已存在");
        }
        Customer customer = customerMapper.find(entity.getCustomerId());
        Shop shop = new Shop();
        shop.setId(entity.getId());
        shop.setPayPeopleMobile(entity.getPayPeopleMobile());
        shop.setPayPeopleName(entity.getPayPeopleName());
        if (customer.getMpOpenId() != null) {
            shop.setPayPeopleMpOpenId(customer.getMpOpenId());
        }
        if (customer.getFwOpenId() != null) {
            shop.setPayPeopleFwOpenId(customer.getFwOpenId());
        }
        shopMapper.update(shop);
        return ExtResult.successResult();
    }

    public void updatePayPeople(String id, String payPeopleName, String payPeopleMpOpenId, String payPeopleFwOpenId, String payPeopleMobile, String payPassword) {

        shopMapper.updatePayPeople(id, payPeopleName, payPeopleMpOpenId, payPeopleFwOpenId, payPeopleMobile, payPassword);
    }

    public ExtResult updateImage(Shop shop) {
        int total = shopMapper.update(shop);
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
                config.staticUrl + "/security/hdg_upload/shop_image.htm",
                fileMap,
                Collections.EMPTY_MAP,
                AppUtils.buildHttpHeader(System.currentTimeMillis(), config.uploadSalt));

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
}
