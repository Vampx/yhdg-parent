package cn.com.yusong.yhdg.webserver.persistence.basic;

import cn.com.yusong.yhdg.common.domain.basic.CustomerMultiOrderDetail;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;


public interface CustomerMultiOrderDetailMapper extends MasterMapper {

    int findPageCount(CustomerMultiOrderDetail search);

    List<CustomerMultiOrderDetail> findPageResult(CustomerMultiOrderDetail search);

    CustomerMultiOrderDetail findBySource(@Param("sourceId") String sourceId, @Param("sourceType") Integer sourceType);
}
