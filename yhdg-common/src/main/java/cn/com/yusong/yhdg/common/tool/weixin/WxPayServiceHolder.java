package cn.com.yusong.yhdg.common.tool.weixin;


import com.github.binarywang.wxpay.service.WxPayService;
import me.chanjar.weixin.mp.api.WxMpService;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 * https://github.com/Wechat-Group/WxJava/wiki/%E5%BE%AE%E4%BF%A1%E6%94%AF%E4%BB%98
 */

@Component
public class WxPayServiceHolder {
    Store store;
    Map<Integer, WxPayService> payServiceMap = new ConcurrentHashMap<Integer, WxPayService>();
    Map<Integer, WxPayService> payMpServiceMap = new ConcurrentHashMap<Integer, WxPayService>();
    Map<Integer, WxPayService> payMaServiceMap = new ConcurrentHashMap<Integer, WxPayService>();

    public WxPayServiceHolder(Store store) {
        this.store = store;
    }

    public WxPayService get(int id) {
        WxPayService service = payServiceMap.get(id);
        if (service == null) {
            WxPayService wxPayService = store.obtainPartner(id);
            if (wxPayService == null) {
                return null;
            }
            payServiceMap.put(id, wxPayService);
            return wxPayService;
        }
        return service;
    }

    public WxPayService getMp(int id) {
        WxPayService service = payMpServiceMap.get(id);
        if (service == null) {
            WxPayService wxPayService = store.obtainPartnerMp(id);
            if (wxPayService == null) {
                return null;
            }
            payMpServiceMap.put(id, wxPayService);
            return wxPayService;
        }
        return service;
    }

    public WxPayService getMa(int id) {
        WxPayService service = payMaServiceMap.get(id);
        if (service == null) {
            WxPayService wxPayService = store.obtainPartnerMa(id);
            if (wxPayService == null) {
                return null;
            }
            payMaServiceMap.put(id, wxPayService);
            return wxPayService;
        }
        return service;
    }

    public void putMp(int key, WxPayService wxPayService) {
        payMpServiceMap.put(key, wxPayService);
    }

    public void putMa(int key, WxPayService wxPayService) {
        payMaServiceMap.put(key, wxPayService);
    }

    public interface Store {
        public WxPayService obtainPartner(int id);

        public WxPayService obtainPartnerMp(int id);

        public WxPayService obtainPartnerMa(int id);
    }
}
