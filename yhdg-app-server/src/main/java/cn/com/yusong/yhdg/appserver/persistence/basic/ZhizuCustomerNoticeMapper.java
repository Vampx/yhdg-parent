package cn.com.yusong.yhdg.appserver.persistence.basic;

import cn.com.yusong.yhdg.common.domain.basic.Device;
import cn.com.yusong.yhdg.common.domain.basic.ZhizuCustomerNotice;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

public interface ZhizuCustomerNoticeMapper extends MasterMapper {
    ZhizuCustomerNotice find(@Param("cabinetId") String cabinetId);
    int insert(ZhizuCustomerNotice entity);
    int insertAll(@Param("agentId") Integer agentId);
    int delete(@Param("cabinetId") String cabinetId);
}
