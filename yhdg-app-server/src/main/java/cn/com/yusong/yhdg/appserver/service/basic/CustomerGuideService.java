package cn.com.yusong.yhdg.appserver.service.basic;

import cn.com.yusong.yhdg.appserver.constant.RespCode;
import cn.com.yusong.yhdg.appserver.entity.result.RestResult;
import cn.com.yusong.yhdg.appserver.service.AbstractService;
import cn.com.yusong.yhdg.common.constant.Constant;
import cn.com.yusong.yhdg.common.domain.basic.CustomerGuide;
import cn.com.yusong.yhdg.appserver.persistence.basic.CustomerGuideMapper;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CustomerGuideService extends AbstractService {
    @Autowired
    CustomerGuideMapper customerGuideMapper;

    public RestResult list() {
        List<Map> list = new ArrayList<Map>();

        List<CustomerGuide> allList = customerGuideMapper.findAll();
        for (CustomerGuide customerGuide : allList) {
            if (customerGuide.getParentId() == null) {
                Map map = new HashMap();
                List<Map> problemList = new ArrayList<Map>();
                map.put("title", customerGuide.getName());
                for (CustomerGuide customerGuide1 : allList) {
                    if (customerGuide1.getParentId() != null && customerGuide.getId() == customerGuide1.getParentId()) {
                        Map listMap = new HashMap();
                        listMap.put("name", customerGuide1.getName());
                        listMap.put("url", String.format(staticPath(Constant.PATH_CUSTOMER_GUIDE), customerGuide1.getId()));
                        problemList.add(listMap);
                    }
                }
                map.put("problemList", problemList);


                list.add(map);
            }
        }
        return RestResult.dataResult(RespCode.CODE_0.getValue(), null, list);
    }
}
