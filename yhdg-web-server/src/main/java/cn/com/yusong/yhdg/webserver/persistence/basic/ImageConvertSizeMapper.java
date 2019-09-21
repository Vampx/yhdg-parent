package cn.com.yusong.yhdg.webserver.persistence.basic;

import cn.com.yusong.yhdg.common.domain.basic.ImageConvertSize;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ImageConvertSizeMapper extends MasterMapper {

    public ImageConvertSize find(@Param("id") int id);
    public List<ImageConvertSize> findPageResult(ImageConvertSize search);
    public int findPageCount(ImageConvertSize search);
    public int insert(@Param("sql") String sql);
    public int update(ImageConvertSize entity);
    public int delete(int id);
}
