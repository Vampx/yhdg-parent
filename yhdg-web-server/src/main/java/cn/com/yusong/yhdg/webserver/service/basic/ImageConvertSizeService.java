package cn.com.yusong.yhdg.webserver.service.basic;


import cn.com.yusong.yhdg.common.domain.basic.ImageConvertSize;
import cn.com.yusong.yhdg.common.entity.json.ExtResult;
import cn.com.yusong.yhdg.common.entity.pagination.Page;
import cn.com.yusong.yhdg.webserver.persistence.basic.ImageConvertSizeMapper;
import cn.com.yusong.yhdg.webserver.service.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ImageConvertSizeService extends AbstractService {

    @Autowired
    ImageConvertSizeMapper imageConvertSizeMapper;

    public ImageConvertSize find(int id) {
        return imageConvertSizeMapper.find(id);
    }

    public Page findPage(ImageConvertSize search) {
        Page page = search.buildPage();
        page.setTotalItems(imageConvertSizeMapper.findPageCount(search));
        search.setBeginIndex(page.getOffset());
        page.setResult(imageConvertSizeMapper.findPageResult(search));
        return page;
    }

    public ExtResult update(ImageConvertSize entity) {
        if(imageConvertSizeMapper.update(entity) == 0) {
            return ExtResult.failResult("操作失败");
        }
        return ExtResult.successResult();
    }
}
