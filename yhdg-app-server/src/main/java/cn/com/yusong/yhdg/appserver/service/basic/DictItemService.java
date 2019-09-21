package cn.com.yusong.yhdg.appserver.service.basic;

import cn.com.yusong.yhdg.appserver.persistence.basic.DictItemMapper;
import cn.com.yusong.yhdg.common.domain.basic.DictItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ruanjian5 on 2017/11/7.
 */
@Service
public class DictItemService {

    @Autowired
    DictItemMapper dictItemMapper;

    public List<DictItem> findByCategory(long categoryId) {
        return dictItemMapper.findByCategory(categoryId);
    }

    public Map<String, String> findMapByCategory(long categoryId) {
        Map<String, String> dictItemMap = new HashMap<String, String>();

        List<DictItem> itemList = dictItemMapper.findByCategory(categoryId);
        for(DictItem e : itemList) {
            dictItemMap.put(e.getItemValue(), e.getItemName());
        }
        return dictItemMap;
    }


}
