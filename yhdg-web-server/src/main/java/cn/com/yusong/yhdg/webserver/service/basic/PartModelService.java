package cn.com.yusong.yhdg.webserver.service.basic;

import cn.com.yusong.yhdg.common.domain.basic.Part;
import cn.com.yusong.yhdg.common.domain.basic.PartModel;
import cn.com.yusong.yhdg.common.domain.basic.PartPerm;
import cn.com.yusong.yhdg.common.entity.json.ExtResult;
import cn.com.yusong.yhdg.common.entity.pagination.Page;
import cn.com.yusong.yhdg.webserver.persistence.basic.PartMapper;
import cn.com.yusong.yhdg.webserver.persistence.basic.PartModelMapper;
import cn.com.yusong.yhdg.webserver.persistence.basic.PartPermMapper;
import cn.com.yusong.yhdg.webserver.service.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.Date;
import java.util.List;

@Service
public class PartModelService extends AbstractService {

    @Autowired
    PartModelMapper partModelMapper;
    @Autowired
    PartPermMapper partPermMapper;

    public PartModel find(Integer id) {
        PartModel partModel = partModelMapper.find(id);
        return partModel;
    }

    public List<PartModel> findList(Integer partModelType) {
        return partModelMapper.findList(partModelType);
    }

    public Page findPage(PartModel partModel) {
        Page page = partModel.buildPage();
        page.setTotalItems(partModelMapper.findPageCount(partModel));
        partModel.setBeginIndex(page.getOffset());
        page.setResult(partModelMapper.findPageResult(partModel));
        return page;
    }

    @Transactional(rollbackFor = Throwable.class)
    public ExtResult create(PartModel partModel) {
        int partNameUnique = partModelMapper.findUnique(partModel.getId(), partModel.getPartModelName());
        if (partNameUnique > 0) {
            return ExtResult.failResult("该角色模板名称已存在");
        }
        partModelMapper.insert(partModel);
        for (String permId : partModel.getPermIdList()) {
            PartPerm partPerm = new PartPerm();
            partPerm.setPartId(partModel.getId());
            partPerm.setPermId(permId);
            partPermMapper.insert(partPerm);
        }
        return ExtResult.successResult();
    }

    @Transactional(rollbackFor = Throwable.class)
    public ExtResult update(PartModel partModel) {
        int partNameUnique = partModelMapper.findUnique(partModel.getId(), partModel.getPartModelName());
        if (partNameUnique > 0) {
            return ExtResult.failResult("该角色模板名称已存在");
        }
        partModelMapper.update(partModel);
        partPermMapper.delete(partModel.getId());
        for (String permId : partModel.getPermIdList()) {
            PartPerm partPerm = new PartPerm();
            partPerm.setPartId(partModel.getId());
            partPerm.setPermId(permId);
            partPermMapper.insert(partPerm);
        }
        return ExtResult.successResult();
    }

    public int delete(Integer id) {
        partPermMapper.delete(id);
        return partModelMapper.delete(id);
    }

}
