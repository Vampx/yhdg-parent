package cn.com.yusong.yhdg.agentserver.service.basic;

import cn.com.yusong.yhdg.common.domain.basic.RotateImage;
import cn.com.yusong.yhdg.common.entity.pagination.Page;
import cn.com.yusong.yhdg.agentserver.persistence.basic.RotateImageMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * Created by chen on 2017/10/28.
 */
@Service
public class RotateImageService {
    @Autowired
    RotateImageMapper rotateImageMapper;

    public RotateImage find(int id) {
        return rotateImageMapper.find(id);
    }

    public Page findPage(RotateImage search) {
        Page page = search.buildPage();
        page.setTotalItems(rotateImageMapper.findPageCount(search));
        search.setBeginIndex(page.getOffset());
        page.setResult(rotateImageMapper.findPageResult(search));
        return page;
    }

    public int create(RotateImage entity) {
        entity.setCreateTime(new Date());
        return rotateImageMapper.insert(entity);
    }

    public int update(RotateImage entity) {
        return rotateImageMapper.update(entity);
    }

    public int updateOrderNum(int id, int orderNum) {
        return rotateImageMapper.updateOrderNum(id, orderNum);
    }

    public int delete(int id) {
        return rotateImageMapper.delete(id);
    }
}
