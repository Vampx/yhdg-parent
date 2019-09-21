package cn.com.yusong.yhdg.webserver.service.zc;

import cn.com.yusong.yhdg.common.constant.ConstEnum;
import cn.com.yusong.yhdg.common.constant.Constant;
import cn.com.yusong.yhdg.common.domain.basic.Agent;
import cn.com.yusong.yhdg.common.domain.basic.AgentInfo;
import cn.com.yusong.yhdg.common.domain.zc.Vehicle;
import cn.com.yusong.yhdg.common.domain.zc.VehicleModel;
import cn.com.yusong.yhdg.common.entity.json.DataResult;
import cn.com.yusong.yhdg.common.entity.json.ExtResult;
import cn.com.yusong.yhdg.common.entity.pagination.Page;
import cn.com.yusong.yhdg.common.entity.tree.Node;
import cn.com.yusong.yhdg.common.entity.tree.NodeModel;
import cn.com.yusong.yhdg.common.utils.*;
import cn.com.yusong.yhdg.common.utils.AppUtils;
import cn.com.yusong.yhdg.webserver.config.AppConfig;
import cn.com.yusong.yhdg.webserver.persistence.basic.AgentMapper;
import cn.com.yusong.yhdg.webserver.persistence.zc.*;
import cn.com.yusong.yhdg.webserver.service.AbstractService;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.codehaus.jackson.JsonEncoding;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.map.ObjectMapper;
import cn.com.yusong.yhdg.webserver.utils.MediaUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.io.File;
import java.io.IOException;
import java.util.*;

@Service
public class VehicleModelService extends AbstractService {
    static Logger log = LogManager.getLogger(VehicleModelService.class);

    @Autowired
    VehicleModelMapper vehicleModelMapper;
    @Autowired
    AgentMapper agentMapper;
    @Autowired
    VehicleOrderMapper vehicleOrderMapper;
    @Autowired
    VehicleMapper vehicleMapper;
    @Autowired
    PriceSettingMapper priceSettingMapper;
    @Autowired
    VehicleVipPriceMapper vehicleVipPriceMapper;
    @Autowired
    VehiclePeriodOrderMapper vehiclePeriodOrderMapper;
    @Autowired
    AppConfig config;

    public VehicleModel find(Integer id) {
        return vehicleModelMapper.find(id);
    }

    public Page findPage(VehicleModel search) {
        Page page = search.buildPage();
        page.setTotalItems(vehicleModelMapper.findPageCount(search));
        search.setBeginIndex(page.getOffset());
        List<VehicleModel> list = vehicleModelMapper.findPageResult(search);
        page.setResult(list);
        return page;
    }

    public HttpClientUtils.HttpResp uploadImage(MultipartFile file) throws IOException {
        String uuid = IdUtils.uuid();
        String fileSuffix = AppUtils.getFileSuffix(file.getOriginalFilename());
        File sourceFile = new File(config.tempDir, uuid + "." + fileSuffix);
        AppUtils.makeParentDir(sourceFile);
        file.transferTo(sourceFile);

        File targetFile = cutDown(sourceFile, IdUtils.uuid(), fileSuffix, Constant.MAX_IMAGE_WIDTH);
        Map<String, File> fileMap = new HashMap<String, File>();
        fileMap.put(targetFile.getName(), targetFile);
        HttpClientUtils.HttpResp httpResp = HttpClientUtils.uploadFile(
                config.staticUrl + "/security/hdg_upload/vehicle_model_image.htm",
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

    public List<VehicleModel> findList(VehicleModel vehicleModel) {
        return vehicleModelMapper.findList(vehicleModel);
    }

    @Transactional(rollbackFor=Throwable.class)
    public ExtResult create(VehicleModel vehicleModel) {
        vehicleModel.setCreateTime(new Date());
        if (vehicleModel.getAgentId() != null) {
            Agent agent = agentMapper.find(vehicleModel.getAgentId());
            if (agent != null) {
                vehicleModel.setAgentName(agent.getAgentName());
                vehicleModel.setAgentCode(agent.getAgentCode());
            }
        }
        vehicleModelMapper.insert(vehicleModel);
        return DataResult.successResult();
    }

    @Transactional(rollbackFor=Throwable.class)
    public ExtResult cancelCreate(VehicleModel vehicleModel) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Field[] fields = vehicleModel.getClass().getDeclaredFields();
        List<String> imagePathList = new ArrayList<String>();
        for (int i = 0; i < fields.length; i++) {
            String fieldName = fields[i].getName();
            String firstLetter = fieldName.substring(0, 1).toUpperCase();
            String getter = "get" + firstLetter + fieldName.substring(1);
            String setter = "set" + firstLetter + fieldName.substring(1);
            Method getMethod = vehicleModel.getClass().getMethod(getter, new Class[]{});
            Method setMethod = vehicleModel.getClass().getMethod(setter, fields[i].getType());
            Object newValue = getMethod.invoke(vehicleModel, new Object[]{});
            if (fieldName.equals("agentId") || fieldName.equals("agentName") || fieldName.equals("agentCode") || fieldName.equals("modelName") || fieldName.equals("isActive") || fieldName.equals("memo") || fieldName.equals("createTime")) {
                continue;
            }
            if (newValue != null && StringUtils.isNotEmpty((String) newValue)) {
                imagePathList.add((String) newValue);
            }
        }
        String[] imagePaths = new String[imagePathList.size()];
        int i = 0;
        for (String imagePath : imagePathList) {
            imagePaths[i] = imagePath;
            i++;
        }
        removeImages(imagePaths);
        return DataResult.successResult();
    }

    @Transactional(rollbackFor=Throwable.class)
    public ExtResult cancelUpdate(VehicleModel vehicleModel) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        VehicleModel dbVehicleModel = vehicleModelMapper.find(vehicleModel.getId());
        VehicleModel newVehicleModel = new VehicleModel();
        BeanUtils.copyProperties(vehicleModel, newVehicleModel);
        Field[] fields = newVehicleModel.getClass().getDeclaredFields();
        //进行数据对比，图片如果修改了，需要把旧图删掉
        List<String> imagePathList = new ArrayList<String>();
        for (int i = 0; i < fields.length; i++) {
            String fieldName = fields[i].getName();
            String firstLetter = fieldName.substring(0, 1).toUpperCase();
            String getter = "get" + firstLetter + fieldName.substring(1);
            String setter = "set" + firstLetter + fieldName.substring(1);
            Method getMethod = newVehicleModel.getClass().getMethod(getter, new Class[]{});
            Method setMethod = newVehicleModel.getClass().getMethod(setter, fields[i].getType());
            Object newValue = getMethod.invoke(newVehicleModel, new Object[]{});
            Object oldValue = getMethod.invoke(dbVehicleModel, new Object[]{});
            if (fieldName.equals("agentId") || fieldName.equals("agentName") || fieldName.equals("agentCode") || fieldName.equals("modelName") || fieldName.equals("isActive") || fieldName.equals("memo") || fieldName.equals("createTime")) {
                continue;
            }
            if (newValue != null && StringUtils.isNotEmpty((String) newValue) && !newValue.equals(oldValue)) {
                imagePathList.add((String) newValue);
            }
        }
        String[] imagePaths = new String[imagePathList.size()];
        int i = 0;
        for (String imagePath : imagePathList) {
            imagePaths[i] = imagePath;
            i++;
        }
        return ExtResult.successResult();
    }

    public ExtResult update(VehicleModel vehicleModel) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        VehicleModel dbVehicleModel = vehicleModelMapper.find(vehicleModel.getId());
        VehicleModel newVehicleModel = new VehicleModel();
        BeanUtils.copyProperties(vehicleModel, newVehicleModel);
        Field[] fields = newVehicleModel.getClass().getDeclaredFields();
        //进行数据对比，图片如果修改了，需要把旧图删掉
        List<String> imagePathList = new ArrayList<String>();
        for (int i = 0; i < fields.length; i++) {
            String fieldName = fields[i].getName();
            String firstLetter = fieldName.substring(0, 1).toUpperCase();
            String getter = "get" + firstLetter + fieldName.substring(1);
            String setter = "set" + firstLetter + fieldName.substring(1);
            Method getMethod = newVehicleModel.getClass().getMethod(getter, new Class[]{});
            Method setMethod = newVehicleModel.getClass().getMethod(setter, fields[i].getType());
            Object newValue = getMethod.invoke(newVehicleModel, new Object[]{});
            Object oldValue = getMethod.invoke(dbVehicleModel, new Object[]{});
            if (fieldName.equals("agentId") || fieldName.equals("agentName") || fieldName.equals("agentCode") || fieldName.equals("modelName") || fieldName.equals("isActive") || fieldName.equals("memo") || fieldName.equals("createTime")) {
                continue;
            }
            //新图如果为空，也可以删掉旧图
            if (newValue != null && !newValue.equals(oldValue)) {
                imagePathList.add((String) oldValue);
            }
        }
        String[] imagePaths = new String[imagePathList.size()];
        int i = 0;
        for (String imagePath : imagePathList) {
            imagePaths[i] = imagePath;
            i++;
        }
        if (dbVehicleModel != null && !vehicleModel.getAgentId().equals(dbVehicleModel.getAgentId())) {
            Agent agent = agentMapper.find(vehicleModel.getAgentId());
            if (agent != null) {
                vehicleModel.setAgentName(agent.getAgentName());
                vehicleModel.setAgentCode(agent.getAgentCode());
            }
        }
        if (vehicleModelMapper.update(vehicleModel) == 1) {
            removeImages(imagePaths);
        }
        return ExtResult.successResult();
    }

    public ExtResult removeImage(String imagePath) {
        Map params = new HashMap();
        params.put("imagePath", imagePath);
        if (sendData(params)) {
            return ExtResult.successResult();
        } else {
            return ExtResult.failResult("删除图片失败");
        }
    }

    private boolean sendData(Map params){
        boolean flag = false;
        Map<String, String> header = new HashMap<String, String>();
        header.put("Content-Type", "application/json");
        OkHttpClientUtils.HttpResp httpResp = null;
        try {
            String url = config.staticUrl + "/security/hdg_upload/remove_image.htm";
            httpResp = OkHttpClientUtils.post(url, YhdgUtils.encodeJson(params),header);
            if(httpResp != null && httpResp.status == 200) {
                log.debug("invoke success ","图片删除成功");
                flag = true;
            } else {
                log.debug("invoke fail {}", httpResp);
            }
        } catch (Exception e) {
            log.error("删除失败：", e);
        }
        return  flag;
    }

    public ExtResult removeImages(String[] imagePaths) {
        Map params = new HashMap();
        params.put("imagePaths", imagePaths);
        if (sendDatas(params)) {
            return ExtResult.successResult();
        } else {
            return ExtResult.failResult("删除图片失败");
        }
    }

    private boolean sendDatas(Map params){
        boolean flag = false;
        Map<String, String> header = new HashMap<String, String>();
        header.put("Content-Type", "application/json");
        OkHttpClientUtils.HttpResp httpResp = null;
        try {
            String url = config.staticUrl + "/security/hdg_upload/remove_images.htm";
            httpResp = OkHttpClientUtils.post(url, YhdgUtils.encodeJson(params),header);
            if(httpResp != null && httpResp.status == 200) {
                log.debug("invoke success ","图片删除成功");
                flag = true;
            } else {
                log.debug("invoke fail {}", httpResp);
            }
        } catch (Exception e) {
            log.error("删除失败：", e);
        }
        return  flag;
    }

    @Transactional(rollbackFor=Throwable.class)
    public ExtResult delete(Integer id) {
        if (vehicleMapper.findCountByModelId(id) > 0) {
            return ExtResult.failResult("存在该车型对应的车辆无法删除");
        }
        if (vehicleOrderMapper.findCountByModelId(id) > 0) {
            return ExtResult.failResult("存在该车型对应的租车订单无法删除");
        }
        if (priceSettingMapper.findCountByModelId(id) > 0) {
            return ExtResult.failResult("存在该车型对应的套餐设置无法删除");
        }
        if (vehicleVipPriceMapper.findCountByModelId(id) > 0) {
            return ExtResult.failResult("存在该车型对应的vip套餐无法删除");
        }
        if (vehiclePeriodOrderMapper.findCountByModelId(id) > 0) {
            return ExtResult.failResult("存在该车型对应的租车租金订单无法删除");
        }
        vehicleModelMapper.delete(id);
        return ExtResult.successResult();
    }

    public void tree(Set<Integer> checked, String dummy, Integer agentId, ServletOutputStream stream) throws IOException {

        List<VehicleModel> list = vehicleModelMapper.findByAgent(agentId);
        if (agentId == null && list.size() == ConstEnum.Flag.FALSE.getValue()) {
            list = vehicleModelMapper.findAll();
        }
        List<Node<NodeModel>> roots = new ArrayList<Node<NodeModel>>();
        for (VehicleModel vehicleModel : list) {
            NodeModel nodeModel = new NodeModel();
            Node<NodeModel> root = new Node<NodeModel>(nodeModel);
            nodeModel.setId(vehicleModel.getId());
            nodeModel.setName(vehicleModel.getModelName());
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
        List<VehicleModel> modelList = vehicleModelMapper.findByAgent(agentId);
        if (modelList.size() == ConstEnum.Flag.FALSE.getValue()) {
            modelList = vehicleModelMapper.findAll();
        }
        List<Node<NodeModel>> roots = new ArrayList<Node<NodeModel>>();
        if (StringUtils.isNotEmpty(dummy)) {
            NodeModel data = new NodeModel();
            Node<NodeModel> root = new Node<NodeModel>(data);
            data.setId("");
            data.setName(dummy);
            roots.add(root);

            for (VehicleModel topVehicleModel : modelList) {
                NodeModel nodeModel = new NodeModel();
                Node<NodeModel> node = new Node<NodeModel>(nodeModel, root);

                nodeModel.setId(topVehicleModel.getId());
                nodeModel.setName(topVehicleModel.getModelName());
                nodeModel.setCheckStatus(checked.contains(topVehicleModel.getId()) ? NodeModel.CheckedStatus.checked : NodeModel.CheckedStatus.unchecked);
            }

        } else {
            for (VehicleModel topVehicleModel : modelList) {
                NodeModel nodeModel = new NodeModel();
                Node<NodeModel> node = new Node<NodeModel>(nodeModel);

                nodeModel.setId(topVehicleModel.getId());
                nodeModel.setCheckStatus(checked.contains(topVehicleModel.getId()) ? NodeModel.CheckedStatus.checked : NodeModel.CheckedStatus.unchecked);
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
}
