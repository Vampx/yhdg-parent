package cn.com.yusong.yhdg.webserver.service.basic;

import cn.com.yusong.yhdg.common.domain.basic.RotateImage;
import cn.com.yusong.yhdg.common.entity.json.ExtResult;
import cn.com.yusong.yhdg.common.entity.json.PageResult;
import cn.com.yusong.yhdg.common.entity.pagination.Page;
import cn.com.yusong.yhdg.webserver.persistence.basic.RotateImageMapper;
import cn.com.yusong.yhdg.webserver.service.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * Created by chen on 2017/10/28.
 */
@Service
public class RotateImageService extends AbstractService {
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
    public ExtResult create(RotateImage entity,String a) {
        entity.setCreateTime(new Date());
        rotateImageMapper.insert(entity);
        return  ExtResult.successResult(entity.getId().toString());
    }

    public int update(RotateImage entity) {
        return rotateImageMapper.update(entity);
    }
    public ExtResult update(RotateImage entity,String a) {
        entity.setCreateTime(new Date());
        rotateImageMapper.update(entity);
        return  ExtResult.successResult(entity.getId().toString());
    }
    public int updateOrderNum(int id, int orderNum) {
        return rotateImageMapper.updateOrderNum(id, orderNum);
    }

    public int delete(int id) {
        return rotateImageMapper.delete(id);
    }
    public ExtResult delete(int id ,String a) {


        rotateImageMapper.delete(id);
        return  ExtResult.successResult();
    }

}
