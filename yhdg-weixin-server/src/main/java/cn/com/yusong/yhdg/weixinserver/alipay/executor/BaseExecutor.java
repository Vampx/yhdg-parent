package cn.com.yusong.yhdg.weixinserver.alipay.executor;

import cn.com.yusong.yhdg.common.domain.basic.Alipayfw;
import cn.com.yusong.yhdg.weixinserver.config.AppConfig;
import com.alipay.api.AlipayClient;
import net.sf.json.JSONObject;

public abstract class BaseExecutor {

    protected Alipayfw alipayfw;
    protected AlipayClient alipayClient;
    protected JSONObject bizContent;

    public BaseExecutor(Alipayfw alipayfw, AlipayClient alipayClient, JSONObject bizContent) {
        this.alipayfw = alipayfw;
        this.alipayClient = alipayClient;
        this.bizContent = bizContent;
    }
}
