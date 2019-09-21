package cn.com.yusong.yhdg.agentserver.service.basic;

import cn.com.yusong.yhdg.common.domain.basic.DictCategory;
import cn.com.yusong.yhdg.common.domain.basic.DictItem;
import cn.com.yusong.yhdg.common.entity.json.ExtResult;
import cn.com.yusong.yhdg.common.entity.pagination.Page;
import cn.com.yusong.yhdg.common.entity.tree.Node;
import cn.com.yusong.yhdg.common.entity.tree.NodeModel;
import cn.com.yusong.yhdg.common.utils.AppUtils;
import cn.com.yusong.yhdg.agentserver.persistence.basic.DictCategoryMapper;
import cn.com.yusong.yhdg.agentserver.persistence.basic.DictItemMapper;
import org.codehaus.jackson.JsonEncoding;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.ServletOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class DictItemService {

    @Autowired
    DictItemMapper dictItemMapper;
    @Autowired
    DictCategoryMapper dictCategoryMapper;

    public Page findPage(DictItem search) {
        Page page = search.buildPage();
        page.setTotalItems(dictItemMapper.findPageCount(search));
        search.setBeginIndex(page.getOffset());
        List<DictItem> list = dictItemMapper.findPageResult(search);
        for (DictItem dictItem: list) {
            if (dictItem.getCategoryId() != null) {
                dictItem.setCategoryName(dictCategoryMapper.find(dictItem.getCategoryId()).getCategoryName());
            }
        }
        page.setResult(list);
        return page;
    }

    public ExtResult insert(DictItem entity) {
        int type;   //字段类型 1、数字 2、文字
        Pattern pattern = Pattern.compile("[0-9]*");        //判断是否为数字
        Matcher isNum = pattern.matcher(entity.getItemValue());
        if (isNum.matches()) {
            type = 1;   //1、数字
        } else {
            type = 2;   //2、文字
        }

        if (entity.getCategoryId() != null) {
            DictCategory dictCategory = dictCategoryMapper.find(entity.getCategoryId());
            if (type != dictCategory.getValueType()) {
                return ExtResult.failResult("字典类型不匹配！");
            }
        }

        DictItem dictItem = dictItemMapper.findByCategoryAndItemValue(entity.getCategoryId(), entity.getItemValue());

        if (dictItem != null) {
            return ExtResult.failResult("条目值重复");
        }

        if (dictItemMapper.insert(entity) == 0) {
            return ExtResult.failResult("操作失败");
        }

        return ExtResult.successResult();
    }

    public ExtResult update(DictItem entity) {
        int type;   //字段类型 1、数字 2、文字
        Pattern pattern = Pattern.compile("[0-9]*");        //判断是否为数字
        Matcher isNum = pattern.matcher(entity.getItemValue());
        if (isNum.matches()) {
            type = 1;   //1、数字
        } else {
            type = 2;   //2、文字
        }

        if (entity.getCategoryId() != null) {
            DictCategory dictCategory = dictCategoryMapper.find(entity.getCategoryId());
            if (type != dictCategory.getValueType()) {
                return ExtResult.failResult("字典类型不匹配！");
            }
        }

        DictItem  dictItem = dictItemMapper.findByCategoryAndItemValue(entity.getCategoryId(), entity.getItemValue());

        if (dictItem != null) {
            return ExtResult.failResult("条目值重复");
        }

        if (dictItemMapper.update(entity) == 0) {
            return ExtResult.failResult("操作失败");
        }
        return ExtResult.successResult();
    }

    public DictItem find(Long id) {
        return dictItemMapper.find(id);
    }

    public ExtResult delete(Long id) {
        if (dictItemMapper.delete(id) == 0) {
            return ExtResult.failResult("操作失败");
        }
        return ExtResult.successResult();
    }

    public void dictCategoryJson(ServletOutputStream stream) throws IOException {
        List<DictCategory> list = dictCategoryMapper.findAll();
        List<Node<NodeModel>> roots = new ArrayList<Node<NodeModel>>();
        for (DictCategory dictCategory : list) {
            NodeModel nodeModel = new NodeModel();
            Node<NodeModel> root = new Node<NodeModel>(nodeModel);
            nodeModel.setId(dictCategory.getId());
            nodeModel.setName(dictCategory.getCategoryName());
            roots.add(root);
        }
        ObjectMapper objectMapper = new ObjectMapper();
        JsonGenerator json = objectMapper.getJsonFactory().createJsonGenerator(stream, JsonEncoding.UTF8);
        json.writeStartArray();
        AppUtils.writeJson(roots, json);
        json.writeEndArray();
        json.flush();
        json.close();
    }


    public List<DictItem> findByCategory(long value) {
        return dictItemMapper.findByCategory(value);
    }

    public int updateOrderNum(long id, int orderNum) {
        return dictItemMapper.updateOrderNum(id, orderNum);
    }
}
