package cn.com.yusong.yhdg.agentserver.persistence.hdg;

import cn.com.yusong.yhdg.common.domain.hdg.BatteryCellModel;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface BatteryCellModelMapper extends MasterMapper {

	BatteryCellModel find(@Param("id") long id);

	int findPageCount(BatteryCellModel batteryCellModel);

	List<BatteryCellModel> findPageResult(BatteryCellModel batteryCellModel);

	int insert(BatteryCellModel batteryCellModel);

	int update(BatteryCellModel batteryCellModel);

	int delete(@Param("id") long id);

}
