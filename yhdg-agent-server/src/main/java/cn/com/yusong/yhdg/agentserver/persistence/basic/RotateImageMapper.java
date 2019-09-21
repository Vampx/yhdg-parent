package cn.com.yusong.yhdg.agentserver.persistence.basic;

import cn.com.yusong.yhdg.common.domain.basic.RotateImage;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by chen on 2017/10/28.
 */
public interface RotateImageMapper extends MasterMapper {
    public RotateImage find(int id);
    public int findPageCount(RotateImage entity);
    public List<RotateImage> findPageResult(RotateImage entity);
    public int insert(RotateImage entity);
    public int update(RotateImage entity);
    public int updateOrderNum(@Param("id") int id, @Param("orderNum") int orderNum);
    public int delete(int id);

}
