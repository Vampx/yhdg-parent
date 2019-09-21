package cn.com.yusong.yhdg.appserver.service.basic;

import cn.com.yusong.yhdg.appserver.persistence.basic.AlipayPayOrderMapper;
import cn.com.yusong.yhdg.common.domain.basic.AlipayPayOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AlipayPayOrderService {
    @Autowired
    AlipayPayOrderMapper alipayPayOrderMapper;

   public int insert(AlipayPayOrder alipayPayOrder) {
       return alipayPayOrderMapper.insert(alipayPayOrder);
   }
}
