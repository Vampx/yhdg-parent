package cn.com.yusong.yhdg.staticserver.web.controller.security;

import cn.com.yusong.yhdg.common.constant.CacheKey;
import cn.com.yusong.yhdg.common.constant.Constant;
import cn.com.yusong.yhdg.common.domain.basic.*;
import cn.com.yusong.yhdg.common.domain.hdg.*;
import cn.com.yusong.yhdg.common.entity.NewBoxNum;
import cn.com.yusong.yhdg.common.protocol.msg22.Msg222000004;
import cn.com.yusong.yhdg.common.tool.memcached.MemCachedConfig;
import cn.com.yusong.yhdg.staticserver.biz.client.ClientBizUtils;
import cn.com.yusong.yhdg.staticserver.config.AppConfig;
import cn.com.yusong.yhdg.staticserver.constant.RespCode;
import cn.com.yusong.yhdg.staticserver.service.basic.*;
import cn.com.yusong.yhdg.staticserver.service.hdg.*;
import cn.com.yusong.yhdg.staticserver.tools.weixin.WxMpPayCallback;
import com.alipay.api.AlipayApiException;
import com.alipay.api.internal.util.AlipaySignature;
import org.apache.commons.io.IOUtils;
import org.dom4j.DocumentException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

@Controller
@RequestMapping(value = "/security/pay_callback")
public class PayCallbackController extends SecurityController {
    static Logger log = LoggerFactory.getLogger(PayCallbackController.class);

    private void ok(HttpServletResponse response) throws IOException {
        String resp = "<xml><return_code><![CDATA[SUCCESS]]></return_code><return_msg><![CDATA[OK]]></return_msg></xml>";
        response.getOutputStream().write(resp.getBytes(Charset.forName("UTF-8")));
        response.getOutputStream().flush();
    }

    private void fail(HttpServletResponse response) throws IOException {
        String resp = "<xml><return_code><![CDATA[FAIL]]></return_code><return_msg><![CDATA[FAIL]]></return_msg></xml>";
        response.getOutputStream().write(resp.getBytes(Charset.forName("UTF-8")));
        response.getOutputStream().flush();
    }

    @Autowired
    AppConfig appConfig;
    @Autowired
    AlipayPayOrderService alipayPayOrderService;
    @Autowired
    WeixinPayOrderService weixinPayOrderService;
    @Autowired
    WeixinmpPayOrderService weixinmpPayOrderService;
    @Autowired
    AlipayfwPayOrderService alipayfwPayOrderService;
    @Autowired
    SystemConfigService systemConfigService;
    @Autowired
    AgentSystemConfigService agentSystemConfigService;
    @Autowired
    BatteryOrderService batteryOrderService;
    @Autowired
    CustomerService customerService;
    @Autowired
    CabinetBoxService cabinetBoxService;
    @Autowired
    CabinetService cabinetService;
    @Autowired
    BatteryService batteryService;
    @Autowired
    CustomerExchangeInfoService customerExchangeInfoService;
    @Autowired
    ExchangeWhiteListService exchangeWhiteListService;
    @Autowired
    BespeakOrderService bespeakOrderService;
    @Autowired
    PartnerService partnerService;
    @Autowired
    MultiPayOrderService multiPayOrderService;
    @Autowired
    WeixinmaPayOrderService weixinmaPayOrderService;

    @ResponseBody
    @RequestMapping(value = "pay_callback.htm")
    public String payCallback() {
        String[] list = new String[] {
                "MP2019071000000000000000005995",
                "MP2019071000000000000000005996",
                "MP2019071000000000000000006007",
                "MP2019071000000000000000006014",
                "MP2019071000000000000000006015",
                "MP2019071000000000000000006016",
                "MP2019071000000000000000006017",
                "MP2019071000000000000000006018",
                "MP2019071000000000000000006019",
                "MP2019071000000000000000006020",
                "MP2019071000000000000000006021",
                "MP2019071000000000000000006022",
                "MP2019071000000000000000006023",
                "MP2019071000000000000000006024",
                "MP2019071000000000000000006026",
                "MP2019071000000000000000006027",
                "MP2019071000000000000000006028",
                "MP2019071000000000000000006029",
                "MP2019071000000000000000006030",
                "MP2019071000000000000000006031",
                "MP2019071000000000000000006032",
                "MP2019071000000000000000006033",
                "MP2019071000000000000000006034",
                "MP2019071000000000000000006035",
                "MP2019071000000000000000006037",
                "MP2019071000000000000000006038",
                "MP2019071000000000000000006041",
                "MP2019071000000000000000006042",
                "MP2019071000000000000000006043",
                "MP2019071000000000000000006044",
                "MP2019071000000000000000006045",
                "MP2019071000000000000000006046"
        };

        for (String id : list) {
            WeixinmpPayOrder weixinmpPayOrder = weixinmpPayOrderService.find(id);
            try {
                if (weixinmpPayOrder.getSourceType() == WeixinmpPayOrder.SourceType.DEPOSIT_ORDER_CUSTOMER_PAY.getValue()) {
                    weixinmpPayOrderService.depositOrderPayOk(weixinmpPayOrder);

                } else if (weixinmpPayOrder.getSourceType() == WeixinmpPayOrder.SourceType.FOREGIFT_DEPOSIT_ORDER_AGENT_PAY.getValue()) {
                    weixinmpPayOrderService.agentForegiftDepositOrderPayOk(weixinmpPayOrder);

                } else if (weixinmpPayOrder.getSourceType() == WeixinmpPayOrder.SourceType.FOREGIFT_ORDER_CUSTOMER_PAY.getValue()) {
                    weixinmpPayOrderService.foregiftOrderPayOk(weixinmpPayOrder);

                } else if (weixinmpPayOrder.getSourceType() == WeixinmpPayOrder.SourceType.CUSTOMER_RENT_GROUP_MONEY_PAY.getValue()) {
                    weixinmpPayOrderService.vehicleGroupOrderPayOk(weixinmpPayOrder);

                } else if (weixinmpPayOrder.getSourceType() == WeixinmpPayOrder.SourceType.CUSTOMER_RENT_GROUP_RENT_MONEY_PAY.getValue()) {
                    weixinmpPayOrderService.vehicleGroupOrderRentPayOk(weixinmpPayOrder);

                } else if (weixinmpPayOrder.getSourceType() == WeixinmpPayOrder.SourceType.PACKET_PERIOD_ORDER_PAY.getValue()) {
                    weixinmpPayOrderService.packetPeriodOrderPayOk(weixinmpPayOrder);

                } else if (weixinmpPayOrder.getSourceType() == WeixinmpPayOrder.SourceType.BATTERY_ORDER_CUSTOMER_PAY.getValue()) {
                    if (weixinmpPayOrderService.batteryOrderPayOk(weixinmpPayOrder)) {
                        openFullBox(weixinmpPayOrder.getSourceId());
                    }
                } else if (weixinmpPayOrder.getSourceType() == WeixinmpPayOrder.SourceType.LAXIN_PAY_ORDER_PAY.getValue()) {
                    weixinmpPayOrderService.laxinOrderPayOk(weixinmpPayOrder);

                } else if (weixinmpPayOrder.getSourceType() == WeixinmpPayOrder.SourceType.DEPOSIT_ORDER_AGENT_PAY.getValue()) {
                    weixinmpPayOrderService.agentDepositOrderPayOk(weixinmpPayOrder);

                } else if (weixinmpPayOrder.getSourceType() == AlipayPayOrder.SourceType.RENT_FOREGIFT_ORDER_PAY.getValue()) {
                    weixinmpPayOrderService.rentForegiftOrderPayOk(weixinmpPayOrder);

                } else if (weixinmpPayOrder.getSourceType() == AlipayPayOrder.SourceType.RENT_PERIOD_ORDER_PAY.getValue()) {
                    weixinmpPayOrderService.rentPeriodOrderPayOk(weixinmpPayOrder);

                } else if (weixinmpPayOrder.getSourceType() == AlipayPayOrder.SourceType.MULTI_ORDER_CUSTOMER_PAY.getValue()) {
                    multiPayOrderService.multiPayDetailPayOk(weixinmpPayOrder);

                } else if (weixinmpPayOrder.getSourceType() == AlipayPayOrder.SourceType.CUSTOMER_EXCHANGE_FIRST_MONEY_PAY.getValue()) {
                    weixinmpPayOrderService.exchangeForegiftOrderFirstMoneyPayOk(weixinmpPayOrder);

                } else if (weixinmpPayOrder.getSourceType() == AlipayPayOrder.SourceType.CUSTOMER_INSTALLMENT_MONEY_PAY.getValue()) {
                    weixinmpPayOrderService.foregiftInstallmentMoneyPayOk(weixinmpPayOrder);

                } else if (weixinmpPayOrder.getSourceType() == AlipayPayOrder.SourceType.CUSTOMER_RENT_FIRST_MONEY_PAY.getValue()) {
                    weixinmpPayOrderService.rentForegiftOrderFirstMoneyPayOk(weixinmpPayOrder);

                } else if (weixinmpPayOrder.getSourceType() == AlipayPayOrder.SourceType.MULTI_ORDER_VEHICLE_PAY.getValue()) {
                    weixinmpPayOrderService.multiPayDetailPayOk(weixinmpPayOrder);

                }

            } catch (Exception e) {
                log.error("Order: {} update status fail", WeixinmpPayOrder.SourceType.getName(weixinmpPayOrder.getSourceType()));
                log.error("handle pay result error", e);
                log.error("Order: {} update status fail", PayOrder.SourceType.getName(weixinmpPayOrder.getSourceType().intValue()));

            }
        }

        return "ok";
    }

    @ResponseBody
    @RequestMapping(value = "alipay_pay_ok.htm")
    public String alipayPayOk(HttpServletRequest request) throws AlipayApiException {
        Map<String, String> paramMap = new HashMap<String, String>();
        Map requestParams = request.getParameterMap();
        for (Iterator iter = requestParams.keySet().iterator(); iter.hasNext(); ) {
            String name = (String) iter.next();
            String[] values = (String[]) requestParams.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i] : valueStr + values[i] + ",";
            }
            paramMap.put(name, valueStr);
            log.warn(name+"==="+valueStr);
        }

        String tradeStatus = request.getParameter("trade_status");
        String outTradeNo = request.getParameter("out_trade_no");
        String tradeNo = request.getParameter("trade_no");

        AlipayPayOrder alipayPayOrder = alipayPayOrderService.find(outTradeNo);
        if (alipayPayOrder == null) {
            log.warn("AlipayPayOrder.id not exist, id:{}", outTradeNo);
        }

        Partner partner = partnerService.find(alipayPayOrder.getPartnerId());
        String alipayPublic = partner.getAlipayAliKey();

        boolean verifyPass = AlipaySignature.rsaCheckV1(paramMap, alipayPublic, Constant.ENCODING_UTF_8, "RSA2");

        if (!verifyPass) {
            log.debug("sign verify fail");
            return "verify fail";
        }

        if(!"TRADE_SUCCESS".equals(tradeStatus)) {
            log.error("支付状态失败 {}", tradeStatus);
            return "支付状态失败";
        }

        alipayPayOrder.setPaymentId(tradeNo);
        int effect = alipayPayOrderService.payOk(alipayPayOrder);
        if (effect > 0) {
            log.debug("Order: {} pay success", outTradeNo);

            try {
                if (alipayPayOrder.getSourceType() == AlipayPayOrder.SourceType.DEPOSIT_ORDER_CUSTOMER_PAY.getValue()) {
                    alipayPayOrderService.depositOrderPayOk(alipayPayOrder);

                }  else if (alipayPayOrder.getSourceType() == AlipayPayOrder.SourceType.FOREGIFT_DEPOSIT_ORDER_AGENT_PAY.getValue()) {
                    alipayPayOrderService.agentForegiftDepositOrderPayOk(alipayPayOrder);

                } else if (alipayPayOrder.getSourceType() == AlipayPayOrder.SourceType.FOREGIFT_ORDER_CUSTOMER_PAY.getValue()) {
                    alipayPayOrderService.foregiftOrderPayOk(alipayPayOrder);

                } else if (alipayPayOrder.getSourceType() == AlipayPayOrder.SourceType.CUSTOMER_RENT_GROUP_MONEY_PAY.getValue()) {
                    alipayPayOrderService.vehicleGroupOrderPayOk(alipayPayOrder);

                }  else if (alipayPayOrder.getSourceType() == AlipayPayOrder.SourceType.CUSTOMER_RENT_GROUP_RENT_MONEY_PAY.getValue()) {
                    alipayPayOrderService.vehicleGroupOrderRentPayOk(alipayPayOrder);

                } else if (alipayPayOrder.getSourceType() == AlipayPayOrder.SourceType.PACKET_PERIOD_ORDER_PAY.getValue()) {
                    alipayPayOrderService.packetPeriodOrderPayOk(alipayPayOrder);

                } else if (alipayPayOrder.getSourceType() == AlipayPayOrder.SourceType.BATTERY_ORDER_CUSTOMER_PAY.getValue()) {
                    if (alipayPayOrderService.batteryOrderPayOk(alipayPayOrder)) {
                        openFullBox(alipayPayOrder.getSourceId());
                    }
                } else if (alipayPayOrder.getSourceType() == AlipayPayOrder.SourceType.LAXIN_PAY_ORDER_PAY.getValue()) {
                    alipayPayOrderService.laxinOrderPayOk(alipayPayOrder);

                }  else if (alipayPayOrder.getSourceType() == AlipayPayOrder.SourceType.DEPOSIT_ORDER_AGENT_PAY.getValue()) {
                    alipayPayOrderService.agentDepositOrderPayOk(alipayPayOrder);

                } else if (alipayPayOrder.getSourceType() == AlipayPayOrder.SourceType.CUSTOMER_EXCHANGE_FIRST_MONEY_PAY.getValue()) {
                    alipayPayOrderService.exchangeForegiftOrderFirstMoneyPayOk(alipayPayOrder);

                } else if (alipayPayOrder.getSourceType() == AlipayPayOrder.SourceType.CUSTOMER_INSTALLMENT_MONEY_PAY.getValue()) {
                    alipayPayOrderService.foregiftInstallmentMoneyPayOk(alipayPayOrder);

                } else if (alipayPayOrder.getSourceType() == AlipayPayOrder.SourceType.CUSTOMER_RENT_FIRST_MONEY_PAY.getValue()) {
                    alipayPayOrderService.rentForegiftOrderFirstMoneyPayOk(alipayPayOrder);

                } else if (alipayPayOrder.getSourceType() == AlipayPayOrder.SourceType.RENT_FOREGIFT_ORDER_PAY.getValue()) {
                    alipayPayOrderService.rentForegiftOrderPayOk(alipayPayOrder);

                } else if (alipayPayOrder.getSourceType() == AlipayPayOrder.SourceType.RENT_PERIOD_ORDER_PAY.getValue()) {
                    alipayPayOrderService.rentPeriodOrderPayOk(alipayPayOrder);

                } else if (alipayPayOrder.getSourceType() == AlipayPayOrder.SourceType.MULTI_ORDER_CUSTOMER_PAY.getValue()) {
                    multiPayOrderService.multiPayDetailPayOk(alipayPayOrder);

                } else if (alipayPayOrder.getSourceType() == AlipayPayOrder.SourceType.MULTI_ORDER_VEHICLE_PAY.getValue()) {
                    alipayPayOrderService.multiPayDetailPayOk(alipayPayOrder);

                }
                
            } catch (Exception e) {
                log.error("Order: {} update status fail", AlipayPayOrder.SourceType.getName(alipayPayOrder.getSourceType()));
                log.error("handle pay result error", e);

            }

        } else {
            log.warn("Order.id update fail, id: {}, stats: {}", outTradeNo, alipayPayOrder.getOrderStatus());
        }

        return "success";
    }

    @ResponseBody
    @RequestMapping(value = "weixin_pay_ok")
    public void weixinPayOk(HttpServletRequest request, HttpServletResponse response) throws IOException, DocumentException, InterruptedException {
        String xml = IOUtils.toString(request.getInputStream(), "UTF-8");
        if (log.isDebugEnabled()) {
            log.debug("wx pay result: {}", xml);
        }

        WxMpPayCallback result = new WxMpPayCallback(xml);
        WeixinPayOrder weixinPayOrder = weixinPayOrderService.find(result.out_trade_no);
        if(weixinPayOrder == null) {
            log.warn("WeixinPayOrder.id not exist, id:{}", result.out_trade_no);
        }

        Partner partner = partnerService.find(weixinPayOrder.getPartnerId());
        String weixinPartnerKey = partner.getWeixinPartnerKey();

        boolean ok = result.signOk(weixinPartnerKey);
        weixinPayOrder.setPaymentId(result.transaction_id);
        if (ok) {
            log.debug("sign verify ok");
            if (weixinPayOrder != null) {
                if (weixinPayOrder.getOrderStatus() == WeixinPayOrder.Status.INIT.getValue()) {
                    int effect = weixinPayOrderService.payOk(weixinPayOrder);
                    if (effect > 0) {
                        log.debug("Order: {} pay success", result.out_trade_no);
                        try {
                            if (weixinPayOrder.getSourceType() == WeixinPayOrder.SourceType.DEPOSIT_ORDER_CUSTOMER_PAY.getValue()) {
                                weixinPayOrderService.depositOrderPayOk(weixinPayOrder);

                            } else if (weixinPayOrder.getSourceType() == WeixinPayOrder.SourceType.FOREGIFT_ORDER_CUSTOMER_PAY.getValue()) {
                                weixinPayOrderService.foregiftOrderPayOk(weixinPayOrder);

                            } else if (weixinPayOrder.getSourceType() == WeixinPayOrder.SourceType.PACKET_PERIOD_ORDER_PAY.getValue()) {
                                weixinPayOrderService.packetPeriodOrderPayOk(weixinPayOrder);

                            } else if (weixinPayOrder.getSourceType() == WeixinPayOrder.SourceType.BATTERY_ORDER_CUSTOMER_PAY.getValue()) {
                                if (weixinPayOrderService.batteryOrderPayOk(weixinPayOrder)) {
                                    openFullBox(weixinPayOrder.getSourceId());
                                }

                            } else if (weixinPayOrder.getSourceType() == WeixinPayOrder.SourceType.LAXIN_PAY_ORDER_PAY.getValue()) {
                                weixinPayOrderService.laxinOrderPayOk(weixinPayOrder);

                            } else if (weixinPayOrder.getSourceType() == WeixinPayOrder.SourceType.DEPOSIT_ORDER_AGENT_PAY.getValue()) {
                                weixinPayOrderService.agentDepositOrderPayOk(weixinPayOrder);

                            } else if (weixinPayOrder.getSourceType() == WeixinPayOrder.SourceType.CUSTOMER_INSTALLMENT_MONEY_PAY.getValue()) {
                                weixinPayOrderService.foregiftInstallmentMoneyPayOk(weixinPayOrder);

                            } else if (weixinPayOrder.getSourceType() == WeixinPayOrder.SourceType.RENT_FOREGIFT_ORDER_PAY.getValue()) {
                                weixinPayOrderService.rentForegiftOrderPayOk(weixinPayOrder);

                            } else if (weixinPayOrder.getSourceType() == WeixinPayOrder.SourceType.RENT_PERIOD_ORDER_PAY.getValue()) {
                                weixinPayOrderService.rentPeriodOrderPayOk(weixinPayOrder);

                            } else if (weixinPayOrder.getSourceType() == WeixinPayOrder.SourceType.MULTI_ORDER_CUSTOMER_PAY.getValue()) {
                                multiPayOrderService.multiPayDetailPayOk(weixinPayOrder);

                            } else if (weixinPayOrder.getSourceType() == WeixinPayOrder.SourceType.CUSTOMER_EXCHANGE_FIRST_MONEY_PAY.getValue()) {
                                weixinPayOrderService.exchangeForegiftOrderFirstMoneyPayOk(weixinPayOrder);

                            } else if (weixinPayOrder.getSourceType() == WeixinPayOrder.SourceType.FOREGIFT_DEPOSIT_ORDER_AGENT_PAY.getValue()) {
                                weixinPayOrderService.agentForegiftDepositOrderPayOk(weixinPayOrder);

                            }  else if (weixinPayOrder.getSourceType() == WeixinPayOrder.SourceType.CUSTOMER_RENT_GROUP_MONEY_PAY.getValue()) {
                                weixinPayOrderService.vehicleGroupOrderPayOk(weixinPayOrder);

                            }  else if (weixinPayOrder.getSourceType() == WeixinPayOrder.SourceType.CUSTOMER_RENT_GROUP_RENT_MONEY_PAY.getValue()) {
                                weixinPayOrderService.vehicleGroupOrderRentPayOk(weixinPayOrder);

                            } else if (weixinPayOrder.getSourceType() == WeixinPayOrder.SourceType.CUSTOMER_RENT_FIRST_MONEY_PAY.getValue()) {
                                weixinPayOrderService.rentForegiftOrderFirstMoneyPayOk(weixinPayOrder);

                            } else if (weixinPayOrder.getSourceType() == AlipayPayOrder.SourceType.MULTI_ORDER_VEHICLE_PAY.getValue()) {
                                weixinmpPayOrderService.multiPayDetailPayOk(weixinPayOrder);

                            }
                        } catch (Exception e) {
                            log.error("Order: {} update status fail", WeixinPayOrder.SourceType.getName(weixinPayOrder.getSourceType()));
                            log.error("handle pay result error", e);
                            log.error("Order: {} update status fail", PayOrder.SourceType.getName(weixinPayOrder.getSourceType().intValue()));

                        }

                    } else {
                        log.warn("effect = 0, id: {}", result.out_trade_no);
                    }

                } else {
                    log.error("weixin pay order status error: {}", weixinPayOrder.getOrderStatus());
                }


                ok(response);
            } else {
                log.error("weixin pay order not exist: {}", result.out_trade_no);
                fail(response);
            }
        } else {
            log.error("sign verify fail");
            fail(response);
        }
    }

    @ResponseBody
    @RequestMapping(value = "weixinmp_pay_ok.htm")
    public void weixinmpPayOk(HttpServletRequest request, HttpServletResponse response) throws IOException,
            DocumentException, InterruptedException {
        String xml = IOUtils.toString(request.getInputStream(), "UTF-8");
        if (log.isDebugEnabled()) {
            log.debug("wxmp pay result: {}", xml);
        }

        WxMpPayCallback result = new WxMpPayCallback(xml);
        WeixinmpPayOrder weixinmpPayOrder = weixinmpPayOrderService.find(result.out_trade_no);
        if(weixinmpPayOrder == null) {
            log.error("weixinmp pay order not exist: {}", result.out_trade_no);
            fail(response);
            return;
        }

        Partner partner = partnerService.find(weixinmpPayOrder.getPartnerId());
        String wxmp_partner_key = partner.getMpPartnerKey();

        boolean ok = result.signOk(wxmp_partner_key);
        if (ok) {
            log.debug("sign verify ok");

            if (weixinmpPayOrder.getOrderStatus() == WeixinmpPayOrder.Status.INIT.getValue()) {
                int effect = weixinmpPayOrderService.payOk(weixinmpPayOrder);
                if (effect > 0) {
                    log.debug("Order: {} pay success", result.out_trade_no);
                    try {
                        if (weixinmpPayOrder.getSourceType() == WeixinmpPayOrder.SourceType.DEPOSIT_ORDER_CUSTOMER_PAY.getValue()) {
                            weixinmpPayOrderService.depositOrderPayOk(weixinmpPayOrder);

                        } else if (weixinmpPayOrder.getSourceType() == WeixinmpPayOrder.SourceType.FOREGIFT_DEPOSIT_ORDER_AGENT_PAY.getValue()) {
                            weixinmpPayOrderService.agentForegiftDepositOrderPayOk(weixinmpPayOrder);

                        } else if (weixinmpPayOrder.getSourceType() == WeixinmpPayOrder.SourceType.FOREGIFT_ORDER_CUSTOMER_PAY.getValue()) {
                            weixinmpPayOrderService.foregiftOrderPayOk(weixinmpPayOrder);

                        }  else if (weixinmpPayOrder.getSourceType() == WeixinmpPayOrder.SourceType.CUSTOMER_RENT_GROUP_MONEY_PAY.getValue()) {
                            weixinmpPayOrderService.vehicleGroupOrderPayOk(weixinmpPayOrder);

                        }  else if (weixinmpPayOrder.getSourceType() == WeixinmpPayOrder.SourceType.CUSTOMER_RENT_GROUP_RENT_MONEY_PAY.getValue()) {
                            weixinmpPayOrderService.vehicleGroupOrderRentPayOk(weixinmpPayOrder);

                        } else if (weixinmpPayOrder.getSourceType() == WeixinmpPayOrder.SourceType.PACKET_PERIOD_ORDER_PAY.getValue()) {
                            weixinmpPayOrderService.packetPeriodOrderPayOk(weixinmpPayOrder);

                        } else if (weixinmpPayOrder.getSourceType() == WeixinmpPayOrder.SourceType.BATTERY_ORDER_CUSTOMER_PAY.getValue()) {
                            if (weixinmpPayOrderService.batteryOrderPayOk(weixinmpPayOrder)) {
                                openFullBox(weixinmpPayOrder.getSourceId());
                            }
                        } else if (weixinmpPayOrder.getSourceType() == WeixinmpPayOrder.SourceType.LAXIN_PAY_ORDER_PAY.getValue()) {
                            weixinmpPayOrderService.laxinOrderPayOk(weixinmpPayOrder);

                        } else if (weixinmpPayOrder.getSourceType() == WeixinmpPayOrder.SourceType.DEPOSIT_ORDER_AGENT_PAY.getValue()) {
                            weixinmpPayOrderService.agentDepositOrderPayOk(weixinmpPayOrder);

                        } else if (weixinmpPayOrder.getSourceType() == WeixinmpPayOrder.SourceType.RENT_FOREGIFT_ORDER_PAY.getValue()) {
                            weixinmpPayOrderService.rentForegiftOrderPayOk(weixinmpPayOrder);

                        } else if (weixinmpPayOrder.getSourceType() == WeixinmpPayOrder.SourceType.RENT_PERIOD_ORDER_PAY.getValue()) {
                            weixinmpPayOrderService.rentPeriodOrderPayOk(weixinmpPayOrder);

                        } else if (weixinmpPayOrder.getSourceType() == WeixinmpPayOrder.SourceType.MULTI_ORDER_CUSTOMER_PAY.getValue()) {
                            multiPayOrderService.multiPayDetailPayOk(weixinmpPayOrder);

                        } else if (weixinmpPayOrder.getSourceType() == WeixinmpPayOrder.SourceType.CUSTOMER_EXCHANGE_FIRST_MONEY_PAY.getValue()) {
                            weixinmpPayOrderService.exchangeForegiftOrderFirstMoneyPayOk(weixinmpPayOrder);

                        } else if (weixinmpPayOrder.getSourceType() == WeixinmpPayOrder.SourceType.CUSTOMER_INSTALLMENT_MONEY_PAY.getValue()) {
                            weixinmpPayOrderService.foregiftInstallmentMoneyPayOk(weixinmpPayOrder);

                        } else if (weixinmpPayOrder.getSourceType() == WeixinmpPayOrder.SourceType.CUSTOMER_RENT_FIRST_MONEY_PAY.getValue()) {
                            weixinmpPayOrderService.rentForegiftOrderFirstMoneyPayOk(weixinmpPayOrder);

                        } else if (weixinmpPayOrder.getSourceType() == AlipayPayOrder.SourceType.MULTI_ORDER_VEHICLE_PAY.getValue()) {
                            weixinmpPayOrderService.multiPayDetailPayOk(weixinmpPayOrder);

                        }

                    } catch (Exception e) {
                        log.error("Order: {} update status fail", WeixinmpPayOrder.SourceType.getName(weixinmpPayOrder.getSourceType()));
                        log.error("handle pay result error", e);
                        log.error("Order: {} update status fail", PayOrder.SourceType.getName(weixinmpPayOrder.getSourceType().intValue()));

                    }

                } else {
                    log.warn("effect = 0, id: {}", result.out_trade_no);
                }

            } else {
                log.error("weixinmp pay order status error: {}", weixinmpPayOrder.getOrderStatus());
            }

            ok(response);

        } else {
            log.error("sign verify fail");
            fail(response);
        }

    }

    @ResponseBody
    @RequestMapping(value = "weixinma_pay_ok.htm")
    public void weixinmaPayOk(HttpServletRequest request, HttpServletResponse response) throws IOException,
            DocumentException, InterruptedException {
        String xml = IOUtils.toString(request.getInputStream(), "UTF-8");
        if (log.isDebugEnabled()) {
            log.debug("wxmp pay result: {}", xml);
        }

        WxMpPayCallback result = new WxMpPayCallback(xml);
        WeixinmaPayOrder weixinmaPayOrder = weixinmaPayOrderService.find(result.out_trade_no);
        if(weixinmaPayOrder == null) {
            log.error("weixinma pay order not exist: {}", result.out_trade_no);
            fail(response);
            return;
        }

        Partner partner = partnerService.find(weixinmaPayOrder.getPartnerId());
        String wxma_partner_key = partner.getMaPartnerKey();

        boolean ok = result.signOk(wxma_partner_key);
        if (ok) {
            log.debug("sign verify ok");

            if (weixinmaPayOrder.getOrderStatus() == WeixinmaPayOrder.Status.INIT.getValue()) {
                int effect = weixinmaPayOrderService.payOk(weixinmaPayOrder);
                if (effect > 0) {
                    log.debug("Order: {} pay success", result.out_trade_no);
                    try {
                        if (weixinmaPayOrder.getSourceType() == WeixinmaPayOrder.SourceType.DEPOSIT_ORDER_CUSTOMER_PAY.getValue()) {
                            weixinmaPayOrderService.depositOrderPayOk(weixinmaPayOrder);

                        } else if (weixinmaPayOrder.getSourceType() == WeixinmaPayOrder.SourceType.FOREGIFT_DEPOSIT_ORDER_AGENT_PAY.getValue()) {
                            weixinmaPayOrderService.agentForegiftDepositOrderPayOk(weixinmaPayOrder);

                        } else if (weixinmaPayOrder.getSourceType() == WeixinmaPayOrder.SourceType.FOREGIFT_ORDER_CUSTOMER_PAY.getValue()) {
                            weixinmaPayOrderService.foregiftOrderPayOk(weixinmaPayOrder);

                        }  else if (weixinmaPayOrder.getSourceType() == WeixinmaPayOrder.SourceType.CUSTOMER_RENT_GROUP_MONEY_PAY.getValue()) {
                            weixinmaPayOrderService.vehicleGroupOrderPayOk(weixinmaPayOrder);

                        }  else if (weixinmaPayOrder.getSourceType() == WeixinmaPayOrder.SourceType.CUSTOMER_RENT_GROUP_RENT_MONEY_PAY.getValue()) {
                            weixinmaPayOrderService.vehicleGroupOrderRentPayOk(weixinmaPayOrder);

                        } else if (weixinmaPayOrder.getSourceType() == WeixinmaPayOrder.SourceType.PACKET_PERIOD_ORDER_PAY.getValue()) {
                            weixinmaPayOrderService.packetPeriodOrderPayOk(weixinmaPayOrder);

                        } else if (weixinmaPayOrder.getSourceType() == WeixinmaPayOrder.SourceType.BATTERY_ORDER_CUSTOMER_PAY.getValue()) {
                            if (weixinmaPayOrderService.batteryOrderPayOk(weixinmaPayOrder)) {
                                openFullBox(weixinmaPayOrder.getSourceId());
                            }
                        } else if (weixinmaPayOrder.getSourceType() == WeixinmaPayOrder.SourceType.LAXIN_PAY_ORDER_PAY.getValue()) {
                            weixinmaPayOrderService.laxinOrderPayOk(weixinmaPayOrder);

                        } else if (weixinmaPayOrder.getSourceType() == WeixinmaPayOrder.SourceType.DEPOSIT_ORDER_AGENT_PAY.getValue()) {
                            weixinmaPayOrderService.agentDepositOrderPayOk(weixinmaPayOrder);

                        } else if (weixinmaPayOrder.getSourceType() == WeixinmaPayOrder.SourceType.RENT_FOREGIFT_ORDER_PAY.getValue()) {
                            weixinmaPayOrderService.rentForegiftOrderPayOk(weixinmaPayOrder);

                        } else if (weixinmaPayOrder.getSourceType() == WeixinmaPayOrder.SourceType.RENT_PERIOD_ORDER_PAY.getValue()) {
                            weixinmaPayOrderService.rentPeriodOrderPayOk(weixinmaPayOrder);

                        } else if (weixinmaPayOrder.getSourceType() == WeixinmaPayOrder.SourceType.MULTI_ORDER_CUSTOMER_PAY.getValue()) {
                            multiPayOrderService.multiPayDetailPayOk(weixinmaPayOrder);

                        } else if (weixinmaPayOrder.getSourceType() == WeixinmaPayOrder.SourceType.CUSTOMER_EXCHANGE_FIRST_MONEY_PAY.getValue()) {
                            weixinmaPayOrderService.exchangeForegiftOrderFirstMoneyPayOk(weixinmaPayOrder);

                        } else if (weixinmaPayOrder.getSourceType() == WeixinmaPayOrder.SourceType.CUSTOMER_INSTALLMENT_MONEY_PAY.getValue()) {
                            weixinmaPayOrderService.foregiftInstallmentMoneyPayOk(weixinmaPayOrder);

                        } else if (weixinmaPayOrder.getSourceType() == WeixinmaPayOrder.SourceType.CUSTOMER_RENT_FIRST_MONEY_PAY.getValue()) {
                            weixinmaPayOrderService.rentForegiftOrderFirstMoneyPayOk(weixinmaPayOrder);

                        } else if (weixinmaPayOrder.getSourceType() == AlipayPayOrder.SourceType.MULTI_ORDER_VEHICLE_PAY.getValue()) {
                            weixinmaPayOrderService.multiPayDetailPayOk(weixinmaPayOrder);

                        }

                    } catch (Exception e) {
                        log.error("Order: {} update status fail", WeixinmaPayOrder.SourceType.getName(weixinmaPayOrder.getSourceType()));
                        log.error("handle pay result error", e);
                        log.error("Order: {} update status fail", PayOrder.SourceType.getName(weixinmaPayOrder.getSourceType().intValue()));

                    }

                } else {
                    log.warn("effect = 0, id: {}", result.out_trade_no);
                }

            } else {
                log.error("weixinma pay order status error: {}", weixinmaPayOrder.getOrderStatus());
            }

            ok(response);

        } else {
            log.error("sign verify fail");
            fail(response);
        }

    }

    @ResponseBody
    @RequestMapping(value = "alipayfw_pay_ok.htm")
    public String alipayfwPayOk(HttpServletRequest request) throws AlipayApiException {
        Map<String, String> paramMap = new HashMap<String, String>();
        Map requestParams = request.getParameterMap();
        for (Iterator iter = requestParams.keySet().iterator(); iter.hasNext(); ) {
            String name = (String) iter.next();
            String[] values = (String[]) requestParams.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i] : valueStr + values[i] + ",";
            }
            //乱码解决，这段代码在出现乱码时使用。
            //valueStr = new String(valueStr.getBytes("ISO-8859-1"), "utf-8");
            paramMap.put(name, valueStr);
            log.warn(name+"==="+valueStr);
        }

        String tradeStatus = request.getParameter("trade_status");
        String outTradeNo = request.getParameter("out_trade_no");
        String tradeNo = request.getParameter("trade_no");

        AlipayfwPayOrder alipayfwPayOrder = alipayfwPayOrderService.find(outTradeNo);
        if(alipayfwPayOrder == null) {
            log.warn("alipayfwPayOrder.id not exist, id:{}", outTradeNo);
            return "success";
        }

        Partner partner = partnerService.find(alipayfwPayOrder.getPartnerId());
        String alipayPublic = partner.getFwAliKey();

        //切记alipaypublickey是支付宝的公钥，请去open.alipay.com对应应用下查看。
        //boolean AlipaySignature.rsaCheckV1(Map<String, String> params, String publicKey, String charset, String sign_type)
        boolean verifyPass = AlipaySignature.rsaCheckV1(paramMap, alipayPublic, Constant.ENCODING_UTF_8, "RSA2");

        if (!verifyPass) {
            log.debug("sign verify fail");
            return "verify fail";
        }

        if(!"TRADE_SUCCESS".equals(tradeStatus)) {
            log.error("支付状态失败 {}", tradeStatus);
            return "支付状态失败";
        }

        alipayfwPayOrder.setPaymentId(tradeNo);
        int effect = alipayfwPayOrderService.payOk(alipayfwPayOrder);
        if (effect > 0) {
            log.debug("Order: {} pay success", outTradeNo);

            try {
                if (alipayfwPayOrder.getSourceType() == AlipayfwPayOrder.SourceType.DEPOSIT_ORDER_CUSTOMER_PAY.getValue()) {
                    alipayfwPayOrderService.depositOrderPayOk(alipayfwPayOrder);

                } else if (alipayfwPayOrder.getSourceType() == AlipayfwPayOrder.SourceType.FOREGIFT_DEPOSIT_ORDER_AGENT_PAY.getValue()) {
                    alipayfwPayOrderService.agentForegiftDepositOrderPayOk(alipayfwPayOrder);

                }  else if (alipayfwPayOrder.getSourceType() == AlipayfwPayOrder.SourceType.FOREGIFT_ORDER_CUSTOMER_PAY.getValue()) {
                    alipayfwPayOrderService.foregiftOrderPayOk(alipayfwPayOrder);

                }  else if (alipayfwPayOrder.getSourceType() == AlipayfwPayOrder.SourceType.CUSTOMER_RENT_GROUP_MONEY_PAY.getValue()) {
                    alipayfwPayOrderService.vehicleGroupOrderPayOk(alipayfwPayOrder);

                }  else if (alipayfwPayOrder.getSourceType() == AlipayfwPayOrder.SourceType.CUSTOMER_RENT_GROUP_RENT_MONEY_PAY.getValue()) {
                    alipayfwPayOrderService.vehicleGroupOrderRentPayOk(alipayfwPayOrder);

                } else if (alipayfwPayOrder.getSourceType() == AlipayfwPayOrder.SourceType.PACKET_PERIOD_ORDER_PAY.getValue()) {
                    alipayfwPayOrderService.packetPeriodOrderPayOk(alipayfwPayOrder);

                } else if (alipayfwPayOrder.getSourceType() == AlipayfwPayOrder.SourceType.BATTERY_ORDER_CUSTOMER_PAY.getValue()) {
                    if (alipayfwPayOrderService.batteryOrderPayOk(alipayfwPayOrder)) {
                        openFullBox(alipayfwPayOrder.getSourceId());
                    }
                } else if (alipayfwPayOrder.getSourceType() == AlipayfwPayOrder.SourceType.LAXIN_PAY_ORDER_PAY.getValue()) {
                    alipayfwPayOrderService.laxinOrderPayOk(alipayfwPayOrder);

                } else if (alipayfwPayOrder.getSourceType() == AlipayfwPayOrder.SourceType.DEPOSIT_ORDER_AGENT_PAY.getValue()) {
                    alipayfwPayOrderService.agentDepositOrderPayOk(alipayfwPayOrder);

                } else if (alipayfwPayOrder.getSourceType() == AlipayfwPayOrder.SourceType.RENT_FOREGIFT_ORDER_PAY.getValue()) {
                    alipayfwPayOrderService.rentForegiftOrderPayOk(alipayfwPayOrder);

                } else if (alipayfwPayOrder.getSourceType() == AlipayfwPayOrder.SourceType.RENT_PERIOD_ORDER_PAY.getValue()) {
                    alipayfwPayOrderService.rentPeriodOrderPayOk(alipayfwPayOrder);

                } else if (alipayfwPayOrder.getSourceType() == AlipayfwPayOrder.SourceType.MULTI_ORDER_CUSTOMER_PAY.getValue()) {
                    multiPayOrderService.multiPayDetailPayOk(alipayfwPayOrder);

                } else if (alipayfwPayOrder.getSourceType() == AlipayfwPayOrder.SourceType.CUSTOMER_EXCHANGE_FIRST_MONEY_PAY.getValue()) {
                    alipayfwPayOrderService.exchangeForegiftOrderFirstMoneyPayOk(alipayfwPayOrder);

                } else if (alipayfwPayOrder.getSourceType() == AlipayfwPayOrder.SourceType.CUSTOMER_INSTALLMENT_MONEY_PAY.getValue()) {
                    alipayfwPayOrderService.foregiftInstallmentMoneyPayOk(alipayfwPayOrder);

                } else if (alipayfwPayOrder.getSourceType() == AlipayfwPayOrder.SourceType.CUSTOMER_RENT_FIRST_MONEY_PAY.getValue()) {
                    alipayfwPayOrderService.rentForegiftOrderFirstMoneyPayOk(alipayfwPayOrder);

                } else if (alipayfwPayOrder.getSourceType() == AlipayPayOrder.SourceType.MULTI_ORDER_VEHICLE_PAY.getValue()) {
                    weixinmpPayOrderService.multiPayDetailPayOk(alipayfwPayOrder);

                }

            } catch (Exception e) {
                log.error("Order: {} update status fail", AlipayfwPayOrder.SourceType.getName(alipayfwPayOrder.getSourceType()));
                log.error("handle pay result error", e);
            }

        } else {
            log.warn("Order.id update fail, id: {}, stats: {}", outTradeNo, alipayfwPayOrder.getOrderStatus());
        }

        return "success";
    }

    public void openFullBox(String orderId) throws InterruptedException {
        BatteryOrder batteryOrder = batteryOrderService.find(orderId);
        String cabinetId = batteryOrder.getPutCabinetId();

        Cabinet cabinet = cabinetService.find(cabinetId);
        Customer customer = customerService.find(batteryOrder.getCustomerId());

        Integer batteryType = null;
        CustomerExchangeInfo customerExchangeInfo = customerExchangeInfoService.find(customer.getId());
        if(customerExchangeInfo != null){
            batteryType = customerExchangeInfo.getBatteryType();
        }else{
            ExchangeWhiteList exchangeWhiteList = exchangeWhiteListService.findByCustomer(cabinet.getAgentId(), customer.getId());
            batteryType = exchangeWhiteList.getBatteryType();
        }
        if(batteryType == null) {
            log.error("客户没有设置电池类型");
            return;
        }

        //查询是否有预约订单
        String bespeakBoxNum = null;
        BespeakOrder bespeakOrder = bespeakOrderService.findSuccessByCustomer(customer.getId());
        if(bespeakOrder != null && bespeakOrder.getBespeakCabinetId().equals(cabinet.getId())){
            bespeakBoxNum = bespeakOrder.getBespeakBoxNum();
        }

        CabinetBox cabinetBox = cabinetBoxService.findOneFull(cabinet.getId(), null,  batteryType, bespeakBoxNum);
        if (cabinetBox == null) {
            int fullCount = cabinetBoxService.findFullCount(cabinetId);
            String errorMessage = "没有符合类型的已充满电池";
            if (fullCount != 0) {
                errorMessage = "扫码者与当前柜子不匹配";
            }
            log.error(errorMessage);
            return;
        }
        //查询分柜子情况
        Battery battery = batteryService.find(cabinetBox.getBatteryId());
        if (battery == null) {
            String errorMessage = "电池不存在: " + cabinetBox.getBatteryId();
            log.error(errorMessage);
            return;
        }

        if(bespeakBoxNum == null || !bespeakBoxNum.equals(cabinetBox.getBoxNum())){
            int effect = cabinetBoxService.lockBox(cabinetBox.getCabinetId(), cabinetBox.getBoxNum(), CabinetBox.BoxStatus.FULL.getValue(), CabinetBox.BoxStatus.FULL_LOCK.getValue());
            if (effect == 0) {
                log.error("锁定箱门失败");
                return;
            }
        }

        //发送开箱指令
        ClientBizUtils.SerialResult result = ClientBizUtils.openStandardBox(appConfig, cabinetBox.getCabinetId(), cabinetBox.getBoxNum(), cabinet.getSubtype());
        //如果协议发送成功
        if (result.getCode() == RespCode.CODE_0.getValue()) {
            Msg222000004 msg222000004 = (Msg222000004) result.getData();
            BatteryOrder order = batteryOrderService.createNewOrder(customer, battery, cabinet, cabinetBox, bespeakOrder);

            NewBoxNum newBoxNum = new NewBoxNum("static-server", batteryOrder.getPutCabinetId(), null, batteryOrder.getPutBoxNum(), cabinetBox.getBoxNum(), new Date());
            String cacheKey = CacheKey.key(CacheKey.K_OLD_BOXNUM_V_NEW_BOXNUM, batteryOrder.getPutCabinetId(), batteryOrder.getPutBoxNum());
            appConfig.memCachedClient.set(cacheKey, newBoxNum, MemCachedConfig.CACHE_FIVE_MINUTE);
            //appConfig.memCachedClient.set(CacheKey.key(CacheKey.K_OLD_BOXNUM_V_NEW_BOXNUM, batteryOrder.getPutCabinetId(), batteryOrder.getPutBoxNum()), cabinetBox.getBoxNum(), MemCachedConfig.CACHE_FIVE_MINUTE);

            if (log.isDebugEnabled()) {
                log.debug("K_OLD_BOXNUM_V_NEW_BOXNUM, cabinetId: {}, oldBox: {}, newBox: {}", batteryOrder.getPutCabinetId(), batteryOrder.getPutBoxNum(), cabinetBox.getBoxNum());
            }

            if (msg222000004.boxStatus != 0) {
                log.debug("{} 柜子 {} 箱号 开门状态错误 箱门状态：{}",cabinet.getId(), cabinetBox.getBoxNum(), msg222000004.boxStatus);
            }
        } else {
            if (result.getSerial() == -1) {
                cabinetBoxService.unlockBox(cabinetBox.getCabinetId(), cabinetBox.getBoxNum(), CabinetBox.BoxStatus.FULL_LOCK.getValue(), CabinetBox.BoxStatus.FULL.getValue());

            } else {
                CabinetBox box = cabinetBoxService.find(cabinetBox.getCabinetId(), cabinetBox.getBoxNum());
                BatteryOrder order = batteryOrderService.createNewOrder(customer, battery, cabinet, cabinetBox, bespeakOrder);

                NewBoxNum newBoxNum = new NewBoxNum("static-server", batteryOrder.getPutCabinetId(), null, batteryOrder.getPutBoxNum(), cabinetBox.getBoxNum(), new Date());
                String cacheKey = CacheKey.key(CacheKey.K_OLD_BOXNUM_V_NEW_BOXNUM, batteryOrder.getPutCabinetId(), batteryOrder.getPutBoxNum());
                appConfig.memCachedClient.set(cacheKey, newBoxNum, MemCachedConfig.CACHE_FIVE_MINUTE);

                //appConfig.memCachedClient.set(CacheKey.key(CacheKey.K_OLD_BOXNUM_V_NEW_BOXNUM, batteryOrder.getPutCabinetId(), batteryOrder.getPutBoxNum()), cabinetBox.getBoxNum(), MemCachedConfig.CACHE_FIVE_MINUTE);
                if (log.isDebugEnabled()) {
                    log.debug("K_OLD_BOXNUM_V_NEW_BOXNUM, cabinetId: {}, oldBox: {}, newBox: {}", batteryOrder.getPutCabinetId(), batteryOrder.getPutBoxNum(), cabinetBox.getBoxNum());
                }

                insertCabinetOperateLog(cabinet.getAgentId(),
                        cabinet.getId(),
                        cabinet.getCabinetName(),
                        cabinetBox.getBoxNum(),
                        CabinetOperateLog.OperateType.OPEN_DOOR,
                        CabinetOperateLog.OperatorType.DISPATCH,
                        String.format("客户%s %s, 换电订单打开满箱失败", customer.getFullname(), customer.getMobile()));
            }
        }
    }
}
