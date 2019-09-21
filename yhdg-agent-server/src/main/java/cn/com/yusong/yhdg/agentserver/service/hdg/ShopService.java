package cn.com.yusong.yhdg.agentserver.service.hdg;

import cn.com.yusong.yhdg.agentserver.config.AppConfig;
import cn.com.yusong.yhdg.agentserver.persistence.hdg.*;
import cn.com.yusong.yhdg.agentserver.service.AbstractService;
import cn.com.yusong.yhdg.agentserver.utils.MediaUtils;
import cn.com.yusong.yhdg.common.constant.ConstEnum;
import cn.com.yusong.yhdg.common.constant.Constant;
import cn.com.yusong.yhdg.common.domain.AreaEntity;
import cn.com.yusong.yhdg.common.domain.basic.AgentInfo;
import cn.com.yusong.yhdg.common.domain.basic.Area;
import cn.com.yusong.yhdg.common.domain.hdg.Cabinet;
import cn.com.yusong.yhdg.common.domain.hdg.Shop;
import cn.com.yusong.yhdg.common.domain.hdg.ShopStoreBattery;
import cn.com.yusong.yhdg.common.entity.AreaCache;
import cn.com.yusong.yhdg.common.entity.json.DataResult;
import cn.com.yusong.yhdg.common.entity.json.ExtResult;
import cn.com.yusong.yhdg.common.entity.pagination.Page;
import cn.com.yusong.yhdg.common.entity.tree.Node;
import cn.com.yusong.yhdg.common.entity.tree.NodeModel;
import cn.com.yusong.yhdg.common.tool.memcached.MemCachedClient;
import cn.com.yusong.yhdg.common.utils.AppUtils;
import cn.com.yusong.yhdg.common.utils.GeoHashUtils;
import cn.com.yusong.yhdg.common.utils.HttpClientUtils;
import cn.com.yusong.yhdg.common.utils.IdUtils;
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
public class ShopService extends AbstractService {
    static Logger log = LoggerFactory.getLogger(ShopService.class);

    @Autowired
    ShopMapper shopMapper;
    @Autowired
    AppConfig config;
    @Autowired
    AreaCache areaCache;
    @Autowired
    MemCachedClient memCachedClient;
    @Autowired
    private AppConfig appConfig;
    @Autowired
    ShopUserMapper shopUserMapper;
    @Autowired
    BatteryMapper batteryMapper;
    @Autowired
    PacketPeriodOrderMapper packetPeriodOrderMapper;
    @Autowired
    CabinetMapper cabinetMapper;
    @Autowired
    ShopStoreBatteryMapper shopStoreBatteryMapper;
    /**
     * 查询分页
     *
     * @param search
     * @return
     */
    public Page findPage(Shop search) {
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

    public Page findUnboundPage(Shop search) {
        Page page = search.buildPage();
        page.setTotalItems(shopMapper.findUnboundPageCount(search));
        search.setBeginIndex(page.getOffset());
        List<Shop> list = shopMapper.findUnboundPageResult(search);
        page.setResult(list);
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

    @Transactional(rollbackFor = Throwable.class)
    public ExtResult clearImage(String id, Integer num) {
        if (num == 1) {
            if (shopMapper.clearImage1(id) == 1) {
                return ExtResult.successResult();
            }
        } else if (num == 2) {
            if (shopMapper.clearImage2(id) == 1) {
                return ExtResult.successResult();
            }
        }
        return ExtResult.failResult("删除失败！");
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
        shop.setCreateTime(new Date());
        if (shopMapper.insert(shop) == 0) {
            return DataResult.failResult("对不起! 保存失败", null);
        }else{
            updateNewLocation(shop);
        }
        return DataResult.successResult("操作成功",shop.getId());
    }

    public ExtResult findUnique(String id) {
        Shop shop = shopMapper.findUnique(id);
        if (shop == null) {
            return ExtResult.successResult();
        }
        return ExtResult.failResult("编号重复");
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

    public ExtResult updateImage(Shop shop) {
        int total = shopMapper.update(shop);
        if (total == 0) {
            return ExtResult.failResult("修改失败！");
        }
        return ExtResult.successResult();
    }


    public HttpClientUtils.HttpResp uploadImage(MultipartFile file, int num) throws IOException {

        String uuid = IdUtils.uuid();
        String fileSuffix = AppUtils.getFileSuffix(file.getOriginalFilename());
        File sourceFile = new File(config.tempDir, uuid + "." + fileSuffix);
        AppUtils.makeParentDir(sourceFile);
        file.transferTo(sourceFile);

        File targetFile = cutDown(sourceFile, IdUtils.uuid(), fileSuffix, Constant.MAX_IMAGE_WIDTH);
        Map<String, File> fileMap = new HashMap<String, File>();
        fileMap.put(targetFile.getName(), targetFile);
        HttpClientUtils.HttpResp httpResp = HttpClientUtils.uploadFile(
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
        sourceFile.delete();
        return targetFile;
    }

    public ExtResult updateLocation(Shop shop) {
        Shop entity = shopMapper.find(shop.getId());
        if (entity == null) {
            return ExtResult.failResult("记录不存在");
        }

        shop.setGeoHash(GeoHashUtils.getGeoHashString(shop.getLng(), shop.getLat()));
        AreaEntity areaEntity = setAreaProperties(areaCache, shop);
        String address = StringUtils.trimToEmpty(areaEntity.getProvinceName()) + StringUtils.trimToEmpty(areaEntity.getCityName()) + StringUtils.trimToEmpty(areaEntity.getDistrictName()) + StringUtils.trimToEmpty(areaEntity.getStreet());
        shop.setAddress(address);
        int effect = shopMapper.update(shop);
        if (effect == 0) {
            return ExtResult.failResult("修改失败！");
        }
        return ExtResult.successResult();
    }

    @Transactional(rollbackFor = Throwable.class)
    public ExtResult updateRatio(String id, Integer platformRatio, Integer agentRatio, Integer provinceAgentRatio, Integer cityAgentRatio, Integer shopRatio, Integer shopFixedMoney) {
        int total = shopMapper.updateRatio(id, platformRatio, agentRatio, provinceAgentRatio, cityAgentRatio, shopRatio, shopFixedMoney);
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

    public void vehicleTree(Set<Integer> checked, String dummy, Integer agentId, Integer isVehicle, ServletOutputStream stream) throws IOException {

        List<Shop> list = shopMapper.findByIsVehicle(agentId,isVehicle);
        if (agentId == null) {
            list = shopMapper.findAll();
        }
        List<Node<NodeModel>> roots = new ArrayList<Node<NodeModel>>();
        for (Shop shop : list) {
            NodeModel nodeModel = new NodeModel();
            Node<NodeModel> root = new Node<NodeModel>(nodeModel);
            nodeModel.setId(shop.getId());
            nodeModel.setName(shop.getShopName());
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


    public void tree(Set<Integer> checked, String dummy, Integer agentId, ServletOutputStream stream) throws IOException {

        List<Shop> list = shopMapper.findByAgent(agentId);
        if (agentId == null && list.size() == ConstEnum.Flag.FALSE.getValue()) {
            list = shopMapper.findAll();
        }
        List<Node<NodeModel>> roots = new ArrayList<Node<NodeModel>>();
        for (Shop shop : list) {
            NodeModel nodeModel = new NodeModel();
            Node<NodeModel> root = new Node<NodeModel>(nodeModel);
            nodeModel.setId(shop.getId());
            nodeModel.setName(shop.getShopName());
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
        List<Shop> shopList = shopMapper.findByAgent(agentId);
        if (shopList.size() == ConstEnum.Flag.FALSE.getValue()) {
            shopList = shopMapper.findAll();
        }
        List<Node<NodeModel>> roots = new ArrayList<Node<NodeModel>>();
        if (StringUtils.isNotEmpty(dummy)) {
            NodeModel data = new NodeModel();
            Node<NodeModel> root = new Node<NodeModel>(data);
            data.setId("");
            data.setName(dummy);
            roots.add(root);

            for (Shop topShop : shopList) {
                NodeModel nodeModel = new NodeModel();
                Node<NodeModel> node = new Node<NodeModel>(nodeModel, root);

                nodeModel.setId(topShop.getId());
                nodeModel.setName(topShop.getShopName());
                nodeModel.setCheckStatus(checked.contains(topShop.getId()) ? NodeModel.CheckedStatus.checked : NodeModel.CheckedStatus.unchecked);
            }

        } else {
            for (Shop topShop : shopList) {
                NodeModel nodeModel = new NodeModel();
                Node<NodeModel> node = new Node<NodeModel>(nodeModel);

                nodeModel.setId(topShop.getId());
                nodeModel.setCheckStatus(checked.contains(topShop.getId()) ? NodeModel.CheckedStatus.checked : NodeModel.CheckedStatus.unchecked);
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

    public void updatePayPeople(String id, String payPeopleName, String payPeopleMpOpenId, String payPeopleFwOpenId, String payPeopleMobile, String payPassword) {
        shopMapper.updatePayPeople(id, payPeopleName, payPeopleMpOpenId, payPeopleFwOpenId, payPeopleMobile, payPassword);
    }
}
