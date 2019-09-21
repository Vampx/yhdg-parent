package cn.com.yusong.yhdg.appserver.web.controller.api.v1.common;


import cn.com.yusong.yhdg.appserver.constant.RespCode;
import cn.com.yusong.yhdg.appserver.entity.result.RestResult;
import cn.com.yusong.yhdg.appserver.service.basic.RotateImageService;
import cn.com.yusong.yhdg.appserver.web.controller.api.v1.ApiController;
import cn.com.yusong.yhdg.common.annotation.NotLogin;
import cn.com.yusong.yhdg.common.domain.basic.RotateImage;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/api/v1/common/basic/rotate_image")
public class RotateImageController extends ApiController {

    @Autowired
    RotateImageService rotateImageService;

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class list {
        public int type;
        public int sourceId;
        public int category;
    }
    /**
     * 查询设置
     *
     * @return
     */
    @NotLogin
    @ResponseBody
    @RequestMapping(value = "/list")
    public RestResult list(@RequestBody RotateImageController.list param, HttpServletRequest request) {
        List list =new ArrayList();
        if(param.sourceId<=0){
            return RestResult.result(RespCode.CODE_2.getValue(), "来源ID错误");
        }else if(param.type<=0){
            return RestResult.result(RespCode.CODE_2.getValue(), "来源错误");
        }else if(param.category<=0){
            return RestResult.result(RespCode.CODE_2.getValue(), "类型错误");
        }
        List<RotateImage> typeAndSourceIdAll = rotateImageService.findTypeAndSourceIdAll(param.type, param.sourceId,param.category);
        for (RotateImage entity: typeAndSourceIdAll) {
            Map<String , Object>map =new HashMap<String, Object>();
            map.put("imagePath",entity.getImagePath());
            map.put("orderNum",entity.getOrderNum());
            map.put("isShow",entity.getIsShow());
            map.put("url",entity.getUrl());
            list.add(map);
        }
        return RestResult.dataResult(RespCode.CODE_0.getValue(), "", list);

    }


}
