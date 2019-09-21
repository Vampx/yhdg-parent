package cn.com.yusong.yhdg.agentserver.persistence.basic;


import cn.com.yusong.yhdg.common.domain.basic.CustomerGuide;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CustomerGuideMapper extends MasterMapper {

    CustomerGuide find(int id);

    List<CustomerGuide> findAll();

    int findPageCount(CustomerGuide search);

    List findPageResult(CustomerGuide search);

    int findByParentId(int parentId);

    int insert(CustomerGuide entity);

    int update(CustomerGuide entity);

    int updateByParentId(@Param("fromParentId") int fromParentId, @Param("toParentId") int toParentId);

    int delete(int id);

    int deleteByParentId(int parentId);
}
