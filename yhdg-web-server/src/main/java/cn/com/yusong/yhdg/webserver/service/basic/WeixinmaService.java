package cn.com.yusong.yhdg.webserver.service.basic;

import cn.com.yusong.yhdg.common.domain.basic.Phoneapp;
import cn.com.yusong.yhdg.common.domain.basic.RotateImage;
import cn.com.yusong.yhdg.common.domain.basic.Weixinma;
import cn.com.yusong.yhdg.common.entity.json.ExtResult;
import cn.com.yusong.yhdg.common.entity.pagination.Page;
import cn.com.yusong.yhdg.webserver.persistence.basic.PhoneappMapper;
import cn.com.yusong.yhdg.webserver.persistence.basic.RotateImageMapper;
import cn.com.yusong.yhdg.webserver.persistence.basic.WeixinmaMapper;
import cn.com.yusong.yhdg.webserver.service.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class WeixinmaService extends AbstractService {
    @Autowired
    private WeixinmaMapper weixinmaMapper;

    @Autowired
    private RotateImageMapper rotateImageMapper;

    public Weixinma find(Integer id){
        Weixinma weixinma = weixinmaMapper.find(id);
        return weixinma;
    }

    public Page findPage(Weixinma search) {
        Page page = search.buildPage();
        page.setTotalItems(weixinmaMapper.findPageCount(search));
        search.setBeginIndex(page.getOffset());
        List<Weixinma> weixinmaList = weixinmaMapper.findPageResult(search);
        page.setResult(weixinmaList);
        return page;
    }

    public void setReferenced(Weixinma weixinma){
    }

    public ExtResult insert(Weixinma weixinma){
        weixinma.setCreateTime(new Date());
        weixinmaMapper.insert(weixinma);

        return ExtResult.successResult(weixinma.getId().toString());
    }

    public ExtResult update(Weixinma weixinma) {
        weixinmaMapper.update(weixinma);

        return ExtResult.successResult(weixinma.getId().toString());
    }

    @Transactional(rollbackFor=Throwable.class)
    public ExtResult delete(int id) {

        Weixinma weixinma = weixinmaMapper.find(id);
        List<Integer> list =new ArrayList<Integer>();
        if(weixinma!=null){
            RotateImage rotateImage =new RotateImage();
            rotateImage.setType(RotateImage.Type.APP.getValue());
            rotateImage.setSourceId(weixinma.getId());
            List<RotateImage> allSourceId = rotateImageMapper.findAllSourceId(rotateImage);
            if(allSourceId!=null && allSourceId.size()>0){
                for (RotateImage rot: allSourceId ) {
                    list.add(rot.getId());
                }
            }
            if(list!=null&&list.size()>0){
                rotateImageMapper.deleteByAllSourceId(list);
            }
            weixinmaMapper.delete(id);
        }
        return ExtResult.successResult();
    }

    public List<Weixinma> findList(Weixinma search) {
        return weixinmaMapper.findList(search);
    }

}
