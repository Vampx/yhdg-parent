package cn.com.yusong.yhdg.appserver.service.basic;

import cn.com.yusong.yhdg.appserver.persistence.basic.WeixinPayOrderMapper;
import cn.com.yusong.yhdg.common.domain.basic.WeixinPayOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WeixinPayOrderService {
    @Autowired
    WeixinPayOrderMapper weixinPayOrderMapper;

   public int insert(WeixinPayOrder weixinPayOrder) {
       return weixinPayOrderMapper.insert(weixinPayOrder);
   }
}
