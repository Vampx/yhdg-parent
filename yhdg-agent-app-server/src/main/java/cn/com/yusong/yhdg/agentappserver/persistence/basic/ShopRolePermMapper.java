package cn.com.yusong.yhdg.agentappserver.persistence.basic;

import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ShopRolePermMapper extends MasterMapper {
    List<String> findShopRoleAll(@Param("shopRoleId") Integer shopRoleId);
}
