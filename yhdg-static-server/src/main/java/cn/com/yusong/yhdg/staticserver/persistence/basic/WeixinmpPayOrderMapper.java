package cn.com.yusong.yhdg.staticserver.persistence.basic;

import cn.com.yusong.yhdg.common.domain.basic.WeixinmpPayOrder;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;


public interface WeixinmpPayOrderMapper extends MasterMapper {
    public WeixinmpPayOrder find(String id);
    public int payOk(@Param("id") String id, @Param("paymentId") String paymentId, @Param("fromStatus") int fromStatus, @Param("toStatus") int toStatus, @Param("handleTime") Date handleTime);
}
