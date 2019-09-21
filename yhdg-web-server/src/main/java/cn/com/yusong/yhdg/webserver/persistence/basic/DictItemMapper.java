package cn.com.yusong.yhdg.webserver.persistence.basic;

import cn.com.yusong.yhdg.common.domain.basic.DictItem;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface DictItemMapper extends MasterMapper {
    int findPageCount(DictItem dictItem);

    DictItem findByCategoryAndItemValue(@Param("categoryId") Long categoryId, @Param("itemValue") String itemValue);

    List findPageResult(DictItem dictItem);

    int insert(DictItem dictItem);

    int update(DictItem dictItem);

    DictItem find(long id);

    int delete(long id);

    List<DictItem> findByCategory(long categoryId);

    DictItem findByValue(String itemValue);

    int updateOrderNum(@Param("id") long id, @Param("orderNum") int orderNum);
}
