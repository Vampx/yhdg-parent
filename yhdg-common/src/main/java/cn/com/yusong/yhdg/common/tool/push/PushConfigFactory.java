package cn.com.yusong.yhdg.common.tool.push;


import cn.com.yusong.yhdg.common.domain.basic.PushConfig;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ruanjian5 on 2017/10/31.
 */
public class PushConfigFactory {
    Map<Integer, PushConfig> map = new HashMap<Integer, PushConfig>();

    public PushConfig put(int key,PushConfig pushConfig){
        if(map.containsKey(key)) {
            PushConfig config = map.get(key);
            if(config.isSameEntity(pushConfig)){
                    return  config;
            }
        }
        map.put(key,pushConfig);
        return  pushConfig;
    }

    public synchronized PushConfig get(int key) {
        return map.get(key);
    }

}
