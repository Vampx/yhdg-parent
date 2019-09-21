package cn.com.yusong.yhdg.appserver.persistence.hdg;

import cn.com.yusong.yhdg.common.domain.hdg.ExchangeInstallmentCountDetail;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ExchangeInstallmentCountDetailMapper extends MasterMapper {



	int findPageCount(ExchangeInstallmentCountDetail countDetail);

	List<ExchangeInstallmentCountDetail> findCountId(long countId);

	ExchangeInstallmentCountDetail find(@Param("id") long id);

}
