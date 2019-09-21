package cn.com.yusong.yhdg.appserver.service.hdg;

import cn.com.yusong.yhdg.appserver.persistence.hdg.KeepPutOrderMapper;


import cn.com.yusong.yhdg.common.domain.hdg.KeepPutOrder;

import cn.com.yusong.yhdg.common.service.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by ruanjian5 on 2017/11/11.
 */
@Service("HdgKeepPutOrderService")
public class KeepPutOrderService {


        @Autowired
        KeepPutOrderMapper keepPutOrderMapper;

        public List<KeepPutOrder> findList(long dispatcherId,int offset ,int limit){
            return  keepPutOrderMapper.findList(dispatcherId,offset,limit);
        }

        public KeepPutOrder find(String orderId){
            return  keepPutOrderMapper.find(orderId);
        }
}
