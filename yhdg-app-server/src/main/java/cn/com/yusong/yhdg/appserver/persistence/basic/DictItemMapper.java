package cn.com.yusong.yhdg.appserver.persistence.basic;

import cn.com.yusong.yhdg.common.domain.basic.DictItem;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by ruanjian5 on 2017/11/7.
 */
public interface DictItemMapper extends MasterMapper {

    public List<DictItem> findByCategory(long categoryId);
}
