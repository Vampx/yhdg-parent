package cn.com.yusong.yhdg.appserver.web.controller.api.v1.user.hdg;

import cn.com.yusong.yhdg.appserver.biz.client.ClientBizUtils;
import cn.com.yusong.yhdg.appserver.config.TokenCache;
import cn.com.yusong.yhdg.appserver.constant.RespCode;
import cn.com.yusong.yhdg.appserver.entity.result.RestResult;
import cn.com.yusong.yhdg.appserver.service.basic.UserService;
import cn.com.yusong.yhdg.appserver.service.hdg.*;
import cn.com.yusong.yhdg.appserver.web.controller.api.v1.ApiController;
import cn.com.yusong.yhdg.common.constant.ConstEnum;
import cn.com.yusong.yhdg.common.domain.basic.User;
import cn.com.yusong.yhdg.common.domain.hdg.*;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;
import java.util.*;

@Controller("api_v1_user_hdg_cabinet")
@RequestMapping(value = "/api/v1/user/hdg/cabinet")
public class CabinetController extends ApiController {

    @Autowired
    UserService userService;

    @Autowired
    BatteryService batteryService;

    @Autowired
    CabinetBoxService cabinetBoxService;

    @Autowired
    FaultLogService faultLogService;

    @Autowired
    CabinetService cabinetService;

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class CabinetParam {
        @NotBlank(message = "换电柜ID不能为空")
        public String cabinetId;
    }

    /**
     * 查询设备明细
     *
     * @param param
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/detail.htm")
    public RestResult detail(@Valid @RequestBody CabinetParam param) {
        TokenCache.Data tokenData = getTokenData();
        long userId = tokenData.userId;
        Map map = new HashMap();
        List<Map> listz = new ArrayList<Map>();
        Cabinet cabinet = cabinetService.find(param.cabinetId);
        if (cabinet != null) {

            map.put("id", cabinet.getId());
            map.put("cabinetName", cabinet.getCabinetName());
            map.put("address", cabinet.getAddress());
            map.put("lng", cabinet.getLng());
            map.put("lat", cabinet.getLat());
            map.put("batteryCount", cabinetService.findBatteryCountByCabinet(cabinet.getId()));
            map.put("waitTakeBatteryCount", cabinetService.findNotFullBatteryCountByCabinet(cabinet.getId()));
            map.put("onlineSubcabinetCount", cabinetService.findOnlineSubcabinetCountByCabinet(cabinet.getId()));
            map.put("offlineSubcabinetCount", cabinetService.findOfflineSubcabinetCountByCabinet(cabinet.getId()));

            Map<String, Integer> map1 = faultLogService.findCabenitCount(userId, cabinet.getId(), FaultLog.Status.WAIT_PROCESS.getValue());
            map.put("faultCount", map1.get("faultCount"));
            map.put("faultLevel", map1.get("faultLevel"));

            List<CabinetBox> boxList = cabinetBoxService.findList(cabinet.getId());
            List<Cabinet> subcabinetList = cabinetService.findByCabinetIdList(cabinet.getId());
            if (!boxList.isEmpty()) {
                for (Cabinet subcabinet : subcabinetList) {
                    Map submap = new HashMap();
                    submap.put("type", Cabinet.Subtype.getName(subcabinet.getSubtype()));
                    submap.put("isOnline", subcabinet.getIsOnline());
                    submap.put("activeStatus", subcabinet.getActiveStatus());
                    List<Map> list = new ArrayList<Map>();
                    for (CabinetBox box : boxList) {
                        if (subcabinet.getId().equals(box.getCabinetId())) {
                            Map map2 = new HashMap();
                            map2.put("boxNum", box.getBoxNum());
                            map2.put("volume", null);
                            //status 1 正常格口  2 故障格口 3 用户电池 4 空箱	5 预约
                            int status = 5;
                            if (box.getBoxStatus() == CabinetBox.BoxStatus.EMPTY.getValue()) {
                                status = 4;
                            } else if (box.getIsActive() == ConstEnum.Flag.FALSE.getValue()) {
                                status = 2;
                            } else if (box.getBoxStatus() == CabinetBox.BoxStatus.FULL.getValue()) {
                                Battery battery = batteryService.find(box.getBatteryId());
                                if (battery.getStatus() == Battery.Status.IN_BOX.getValue()) {
                                    status = 1;
                                } else if (battery.getStatus() == Battery.Status.IN_BOX_CUSTOMER_USE.getValue() || battery.getStatus() == Battery.Status.IN_BOX_NOT_PAY.getValue()) {
                                    status = 3;
                                }
                                map2.put("volume", battery.getVolume());
                            }
                            map2.put("status", status);
                            map2.put("chargeStatus", box.getChargeStatus());

                            list.add(map2);
                        }
                    }
                    submap.put("boxList", list);
                    listz.add(submap);
                }
            }
        }
        map.put("subcabinetList", listz);
        return RestResult.dataResult(RespCode.CODE_0.getValue(), "", map);
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class OpenALLEmptyBoxParam {
        @NotBlank(message = "换电柜ID不能为空")
        public String cabinetId;
    }

    /**
     * 打开全部柜子的空箱
     *
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/open_all_empty_box.htm")
    public RestResult openALLEmptyBox(@Valid @RequestBody OpenALLEmptyBoxParam param) throws InterruptedException {
        TokenCache.Data tokenData = getTokenData();
        long userId = tokenData.userId;

        User user = userService.find(userId);
        if(user == null) {
            return RestResult.result(RespCode.CODE_1.getValue(), "用户不存在");
        }

        Cabinet cabinet = cabinetService.find(param.cabinetId);
        if (cabinet == null) {
            return RestResult.result(RespCode.CODE_1.getValue(), "换电柜不存在");
        }

        //发送开箱指令
        Map<String, List<String>> successMap = new HashMap<String, List<String>>();
        Map<String, List<String>> failMap = new HashMap<String, List<String>>();

        RestResult result = null;
        List<CabinetBox> boxList = cabinetBoxService.findAllEmpty(param.cabinetId, CabinetBox.BoxStatus.EMPTY.getValue());
        for (CabinetBox subcabinetBox : boxList) {
            List<String> successList = successMap.get(subcabinetBox.getCabinetId());
            List<String> failList = failMap.get(subcabinetBox.getCabinetId());

            if (successList == null) {
                successMap.put(subcabinetBox.getCabinetId(), successList = new ArrayList<String>());
            }
            if (failList == null) {
                failMap.put(subcabinetBox.getCabinetId(), failList = new ArrayList<String>());
            }

            Cabinet subcabinet = cabinetService.find(subcabinetBox.getCabinetId());
            result = ClientBizUtils.openStandardBox(config, subcabinetBox.getCabinetId(), subcabinetBox.getBoxNum(), subcabinetBox.getSubtype());

            if (result != null && result.getCode() == 0) {
                successList.add(subcabinetBox.getBoxNum());

                insertCabinetOperateLog(cabinet.getAgentId(),
                        cabinet.getId(),
                        cabinet.getCabinetName(),
                        subcabinetBox.getBoxNum(),
                        CabinetOperateLog.OperateType.OPEN_DOOR,
                        CabinetOperateLog.OperatorType.DISPATCH,
                        String.format("维护员%s打开空箱子成功", user.getFullname()),
                        user.getFullname());

            } else {
                failList.add(subcabinetBox.getBoxNum());

                insertCabinetOperateLog(cabinet.getAgentId(),
                        cabinet.getId(),
                        cabinet.getCabinetName(),
                        subcabinetBox.getBoxNum(),
                        CabinetOperateLog.OperateType.OPEN_DOOR,
                        CabinetOperateLog.OperatorType.DISPATCH,
                        String.format("维护员%s打开空箱子失败", user.getFullname()),
                        user.getFullname());

            }
        }

        Map data = new HashMap();
        data.put("success", removeEmpty(successMap));
        data.put("fail", removeEmpty(failMap));

        return RestResult.dataResult(RespCode.CODE_0.getValue(), "", data);
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class OpenAllNotFullBox {
        @NotBlank(message = "换电柜ID不能为空")
        public String cabinetId;
    }

    /**
     * 打开全部电量不满的的空箱
     *
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/open_all_not_full_box.htm")
    public RestResult openALLNotFullBox(@Valid @RequestBody OpenAllNotFullBox param) throws InterruptedException {
        TokenCache.Data tokenData = getTokenData();
        long userId = tokenData.userId;
        Cabinet cabinet = cabinetService.find(param.cabinetId);
        if (cabinet == null) {
            return RestResult.result(RespCode.CODE_1.getValue(), "换电柜不存在");
        }

        User user = userService.find(userId);
        if(user == null) {
            return RestResult.result(RespCode.CODE_1.getValue(), "用户不存在");
        }

        //发送开箱指令
        Map<String, List<String>> successMap = new HashMap<String, List<String>>();
        Map<String, List<String>> failMap = new HashMap<String, List<String>>();
        RestResult result = null;

        List<CabinetBox> boxList = cabinetBoxService.findAllNotFull(param.cabinetId, CabinetBox.BoxStatus.FULL.getValue());
        for (CabinetBox subcabinetBox : boxList) {
            List<String> successList = successMap.get(subcabinetBox.getCabinetId());
            List<String> failList = failMap.get(subcabinetBox.getCabinetId());

            if (successList == null) {
                successMap.put(subcabinetBox.getCabinetId(), successList = new ArrayList<String>());
            }
            if (failList == null) {
                failMap.put(subcabinetBox.getCabinetId(), failList = new ArrayList<String>());
            }

            Cabinet subcabinet = cabinetService.find(subcabinetBox.getCabinetId());
            result = ClientBizUtils.openStandardBox(config, subcabinetBox.getCabinetId(), subcabinetBox.getBoxNum(), subcabinetBox.getSubtype());

            if (result != null && result.getCode() == 0) {
                cabinetBoxService.updateOpenType(subcabinetBox.getCabinetId(), subcabinetBox.getBoxNum(), CabinetBox.OpenType.KEEP_ORDER_OPEN_FULL_BOX.getValue(), userId);
                successList.add(subcabinetBox.getBoxNum());

                insertCabinetOperateLog(cabinet.getAgentId(),
                        cabinet.getId(),
                        cabinet.getCabinetName(),
                        subcabinetBox.getBoxNum(),
                        CabinetOperateLog.OperateType.OPEN_DOOR,
                        CabinetOperateLog.OperatorType.DISPATCH,
                        String.format("维护员%s打开欠电箱子成功", user.getFullname()),
                        user.getFullname());


            } else {
                failList.add(subcabinetBox.getBoxNum());

                insertCabinetOperateLog(cabinet.getAgentId(),
                        cabinet.getId(),
                        cabinet.getCabinetName(),
                        subcabinetBox.getBoxNum(),
                        CabinetOperateLog.OperateType.OPEN_DOOR,
                        CabinetOperateLog.OperatorType.DISPATCH,
                        String.format("维护员%s打开欠电箱子失败", user.getFullname()),
                        user.getFullname());

            }
        }


        Map data = new HashMap();
        data.put("success", removeEmpty(successMap));
        data.put("fail", removeEmpty(failMap));

        return RestResult.dataResult(RespCode.CODE_0.getValue(), "", data);
    }


    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class OpenAllCheckedBoxParam {
        public static class Subcabinet {
            public String subcabinetId;
            public List<String> boxNum = new ArrayList<String>();
        }

        @NotBlank(message = "换电柜ID不能为空")
        public String cabinetId;
        public List<Subcabinet> subcabinetList = new ArrayList<Subcabinet>();
    }

    @ResponseBody
    @RequestMapping(value = "/open_all_checked_box.htm")
    public RestResult openAllCheckedBox(@RequestBody OpenAllCheckedBoxParam param) throws InterruptedException {
        TokenCache.Data tokenData = getTokenData();
        long userId = tokenData.userId;

        User user = userService.find(userId);
        if(user == null) {
            return RestResult.result(RespCode.CODE_2.getValue(), "用户不存在");
        }

        Cabinet cabinet = cabinetService.find(param.cabinetId);
        if (cabinet == null) {
            return RestResult.result(RespCode.CODE_2.getValue(), "换电柜不存在");
        }

        //发送开箱指令
        Map<String, List<String>> successMap = new HashMap<String, List<String>>();
        Map<String, List<String>> failMap = new HashMap<String, List<String>>();
        RestResult result = null;

        for (OpenAllCheckedBoxParam.Subcabinet e : param.subcabinetList) {
            List<String> successList = successMap.get(e.subcabinetId);
            List<String> failList = failMap.get(e.subcabinetId);

            if (successList == null) {
                successMap.put(e.subcabinetId, successList = new ArrayList<String>());
            }
            if (failList == null) {
                failMap.put(e.subcabinetId, failList = new ArrayList<String>());
            }

            for (String boxNum : e.boxNum) {
                Cabinet subcabinet = cabinetService.find(e.subcabinetId);
                CabinetBox subcabinetBox = cabinetBoxService.find(e.subcabinetId, boxNum);
                if (subcabinetBox != null) {
                    result = ClientBizUtils.openStandardBox(config, subcabinetBox.getCabinetId(), subcabinetBox.getBoxNum(), subcabinetBox.getSubtype());
                    if (result != null && result.getCode() == 0) {
                        cabinetBoxService.updateOpenType(subcabinetBox.getCabinetId(), subcabinetBox.getBoxNum(), CabinetBox.OpenType.KEEP_ORDER_OPEN_FULL_BOX.getValue(), userId);
                        successList.add(subcabinetBox.getBoxNum());

                        insertCabinetOperateLog(cabinet.getAgentId(),
                                cabinet.getId(),
                                cabinet.getCabinetName(),
                                subcabinetBox.getBoxNum(),
                                CabinetOperateLog.OperateType.OPEN_DOOR,
                                CabinetOperateLog.OperatorType.DISPATCH,
                                String.format("维护员%s打开选中箱子成功", user.getFullname()),
                                user.getFullname());


                    } else {
                        failList.add(subcabinetBox.getBoxNum());

                        insertCabinetOperateLog(cabinet.getAgentId(),
                                cabinet.getId(),
                                cabinet.getCabinetName(),
                                subcabinetBox.getBoxNum(),
                                CabinetOperateLog.OperateType.OPEN_DOOR,
                                CabinetOperateLog.OperatorType.DISPATCH,
                                String.format("维护员%s打开选中箱子失败", user.getFullname()),
                                user.getFullname());

                    }

                }

            }
        }

        Map data = new HashMap();
        data.put("success", removeEmpty(successMap));
        data.put("fail", removeEmpty(failMap));

        return RestResult.dataResult(RespCode.CODE_0.getValue(), "", data);
    }

    private Map<String, List<String>> removeEmpty(Map<String, List<String>> map) {
        for (Iterator<Map.Entry<String, List<String>>> it = map.entrySet().iterator(); it.hasNext(); ) {
            Map.Entry<String, List<String>> item = it.next();
            if (item.getValue().isEmpty()) {
                it.remove();
            }
        }
        return map;
    }

}