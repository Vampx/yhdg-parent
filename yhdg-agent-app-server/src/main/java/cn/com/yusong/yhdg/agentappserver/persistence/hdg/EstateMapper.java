package cn.com.yusong.yhdg.agentappserver.persistence.hdg;

import cn.com.yusong.yhdg.common.domain.hdg.Estate;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface EstateMapper extends MasterMapper {
	Estate find(@Param("id") Long id);
	int updatePayPassword(@Param("id") Long id, @Param("payPassword") String payPassword);
	int updateBalance(@Param("id") Long id, @Param("balance") int balance);
}
