package cn.com.yusong.yhdg.webserver.service.basic;

import cn.com.yusong.yhdg.common.domain.basic.Phoneapp;
import cn.com.yusong.yhdg.common.domain.basic.RotateImage;
import cn.com.yusong.yhdg.common.entity.json.ExtResult;
import cn.com.yusong.yhdg.common.entity.pagination.Page;
import cn.com.yusong.yhdg.webserver.persistence.basic.PhoneappMapper;
import cn.com.yusong.yhdg.webserver.persistence.basic.RotateImageMapper;
import cn.com.yusong.yhdg.webserver.service.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class PhoneappService extends AbstractService {
    @Autowired
    private PhoneappMapper phoneappMapper;

    @Autowired
    private RotateImageMapper rotateImageMapper;

    public Phoneapp find(Integer id){
        Phoneapp phoneapp = phoneappMapper.find(id);
        return phoneapp;
    }

    public Page findPage(Phoneapp search) {
        Page page = search.buildPage();
        page.setTotalItems(phoneappMapper.findPageCount(search));
        search.setBeginIndex(page.getOffset());
        List<Phoneapp> phoneappList = phoneappMapper.findPageResult(search);
        page.setResult(phoneappList);

        return page;
    }

    public void setReferenced(Phoneapp phoneapp){
    }

    public ExtResult insert(Phoneapp phoneapp){
        phoneapp.setCreateTime(new Date());
        phoneappMapper.insert(phoneapp);

        return ExtResult.successResult(phoneapp.getId().toString());
    }

    public ExtResult update(Phoneapp phoneapp) {
        phoneappMapper.update(phoneapp);

        return ExtResult.successResult(phoneapp.getId().toString());
    }

    @Transactional(rollbackFor=Throwable.class)
    public ExtResult delete(int id) {

        Phoneapp phoneapp = phoneappMapper.find(id);
        List<Integer> list =new ArrayList<Integer>();
        if(phoneapp!=null){
            RotateImage rotateImage =new RotateImage();
            rotateImage.setType(RotateImage.Type.APP.getValue());
            rotateImage.setSourceId(phoneapp.getId());
            List<RotateImage> allSourceId = rotateImageMapper.findAllSourceId(rotateImage);
            if(allSourceId!=null && allSourceId.size()>0){
                for (RotateImage rot: allSourceId ) {
                    list.add(rot.getId());
                }
            }
            if(list!=null&&list.size()>0){
                rotateImageMapper.deleteByAllSourceId(list);
            }
            phoneappMapper.delete(id);
        }
        return ExtResult.successResult();
    }

    public List<Phoneapp> findList(Phoneapp search) {
        return phoneappMapper.findList(search);
    }

}
