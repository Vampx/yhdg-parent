package cn.com.yusong.yhdg.weixinserver.persistence.hdg;

import cn.com.yusong.yhdg.common.domain.hdg.Shop;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ShopMapper extends MasterMapper {
	Shop find(@Param("id") String id);
}
