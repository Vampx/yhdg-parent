package cn.com.yusong.yhdg.webserver.service.basic;

import cn.com.yusong.yhdg.common.domain.basic.RotateImage;
import cn.com.yusong.yhdg.common.domain.basic.Weixinmp;
import cn.com.yusong.yhdg.common.entity.json.ExtResult;
import cn.com.yusong.yhdg.common.entity.pagination.Page;
import cn.com.yusong.yhdg.webserver.persistence.basic.RotateImageMapper;
import cn.com.yusong.yhdg.webserver.persistence.basic.WeixinmpMapper;
import cn.com.yusong.yhdg.webserver.service.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class WeixinmpService extends AbstractService {

    @Autowired
    private WeixinmpMapper weixinmpMapper;

    @Autowired
    private RotateImageMapper rotateImageMapper;
    public Weixinmp find(Integer id){
        Weixinmp weixinmp = weixinmpMapper.find(id);
        return weixinmp;
    }

    public Page findPage(Weixinmp search) {
        Page page = search.buildPage();
        page.setTotalItems(weixinmpMapper.findPageCount(search));
        search.setBeginIndex(page.getOffset());
        List<Weixinmp> weixinmpList = weixinmpMapper.findPageResult(search);
        page.setResult(weixinmpList);

        return page;
    }

    public List<Weixinmp> findList(Weixinmp search) {
        return weixinmpMapper.findList(search);
    }

    public void setReferenced(Weixinmp weixinmp){
    }

    @Transactional(rollbackFor = Throwable.class)
    public ExtResult insert(Weixinmp weixinmp, List<String> sqlList){
        weixinmp.setCreateTime(new Date());
        weixinmpMapper.insert(weixinmp);
        String line = String.format("%d", weixinmp.getId());
        for (String sql : sqlList) {
            weixinmpMapper.insertSql(sql.replace("0/*weixinmp_id*/", line));
        }

        return ExtResult.successResult(weixinmp.getId().toString());
    }

    public ExtResult update(Weixinmp weixinmp) {
        weixinmpMapper.update(weixinmp);

        return ExtResult.successResult(weixinmp.getId().toString());
    }

    @Transactional(rollbackFor=Throwable.class)
    public ExtResult delete(int id) {
        Weixinmp weixinmp = weixinmpMapper.find(id);
        List<Integer> list =new ArrayList<Integer>();
        if(weixinmp!=null){
            RotateImage rotateImage =new RotateImage();
            rotateImage.setSourceId(weixinmp.getId());
            rotateImage.setType(RotateImage.Type.GZH.getValue());
            List<RotateImage> allSourceId = rotateImageMapper.findAllSourceId(rotateImage);
            if(allSourceId!=null && allSourceId.size()>0){
                for (RotateImage rot: allSourceId ) {
                    list.add(rot.getId());
                }
            }

            if(list!=null&&list.size()>0){
                rotateImageMapper.deleteByAllSourceId(list);
            }
            weixinmpMapper.delete(id);
        }

        return ExtResult.successResult();
    }


}
