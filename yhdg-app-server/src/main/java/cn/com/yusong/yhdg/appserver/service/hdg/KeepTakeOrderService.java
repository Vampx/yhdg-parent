package cn.com.yusong.yhdg.appserver.service.hdg;

import cn.com.yusong.yhdg.appserver.constant.RespCode;
import cn.com.yusong.yhdg.appserver.entity.result.RestResult;
import cn.com.yusong.yhdg.appserver.persistence.hdg.KeepTakeOrderMapper;
import cn.com.yusong.yhdg.common.constant.Constant;
import cn.com.yusong.yhdg.common.domain.hdg.KeepTakeOrder;
import cn.com.yusong.yhdg.common.service.AbstractService;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ruanjian5 on 2017/11/11.
 */
@Service
public class KeepTakeOrderService   {
        @Autowired
        KeepTakeOrderMapper keepTakeOrderMapper;
        public List<KeepTakeOrder> findList(long dispatcherId,int offset ,int limit){
            return  keepTakeOrderMapper.findList(dispatcherId,offset,limit);
        }

        public KeepTakeOrder find(String orderId){
            return  keepTakeOrderMapper.find(  orderId);
        }


        public RestResult getList(long userId,int offset,int limit){

            List<Map> list = new ArrayList<Map>();
            List<KeepTakeOrder> orderList = keepTakeOrderMapper.findList(userId,offset,limit);
            if(!orderList.isEmpty()){
                for (KeepTakeOrder keepTakeOrder:orderList){
                    Map mapOrder = new HashMap();
                    mapOrder.put("id",keepTakeOrder.getId());
                    mapOrder.put("orderCount",keepTakeOrder.getOrderCount());
                    mapOrder.put("cabinetId",keepTakeOrder.getCabinetId());
                    mapOrder.put("cabinetName",keepTakeOrder.getCabinetName());
                    mapOrder.put("takeTime",keepTakeOrder.getCreateTime() != null?
                            DateFormatUtils.format(keepTakeOrder.getCreateTime(), Constant.DATE_TIME_FORMAT):null);
                    list.add(mapOrder);
                }
            }

            return  RestResult.dataResult(RespCode.CODE_0.getValue(),"",list);
        }
}
