/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2014 All Rights Reserved.
 */
package cn.com.yusong.yhdg.weixinserver.alipay.executor;

import cn.com.yusong.yhdg.common.domain.basic.Alipayfw;
import cn.com.yusong.yhdg.weixinserver.alipay.AlipayMsgBuildUtil;
import cn.com.yusong.yhdg.weixinserver.config.AppConfig;
import com.alipay.api.AlipayClient;
import net.sf.json.JSONObject;

/**
 * 默认执行器(该执行器仅发送ack响应)
 * 
 * @author baoxing.gbx
 * @version $Id: InAlipayDefaultExecutor.java, v 0.1 Jul 30, 2014 10:22:11 AM baoxing.gbx Exp $
 */
public class InAlipayDefaultExecutor extends BaseExecutor implements ActionExecutor {

    public InAlipayDefaultExecutor(Alipayfw alipayfw, AlipayClient alipayClient, JSONObject bizContent) {
        super(alipayfw, alipayClient, bizContent);
    }

    @Override
    public String execute() throws Exception {

        //取得发起请求的支付宝账号id
        final String fromUserId = bizContent.getString("FromUserId");

        return AlipayMsgBuildUtil.buildBaseAckMsg(alipayfw.getAppId(), fromUserId);
    }
}
