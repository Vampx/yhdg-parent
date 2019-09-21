package cn.com.yusong.yhdg.common.service;

import cn.com.yusong.yhdg.common.constant.Constant;
import cn.com.yusong.yhdg.common.domain.AreaEntity;
import cn.com.yusong.yhdg.common.domain.basic.Area;
import cn.com.yusong.yhdg.common.domain.basic.OrderId;
import cn.com.yusong.yhdg.common.entity.AreaCache;
import cn.com.yusong.yhdg.common.utils.AppUtils;
import cn.com.yusong.yhdg.common.utils.CodecUtils;
import cn.com.yusong.yhdg.common.utils.YhdgUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public abstract class AbstractService {
    static Logger log = LoggerFactory.getLogger(AbstractService.class);

    protected Long getAccreditTime(String key, String module) throws Exception {
        HttpClient client = HttpClientBuilder.create().build();
        String url = "https://ql.qiling.work/staticserver/security/license/get.htm";
        HttpPost httpPost = new HttpPost(url);
        httpPost.addHeader("Content-Type", "application/json;charset=utf-8");
        Map requestMap = new HashMap();
        requestMap.put("key", key);
        requestMap.put("module", module);

        String requestBody = AppUtils.encodeJson(requestMap);

        StringEntity entity = new StringEntity(requestBody, "utf-8");
        entity.setContentEncoding("UTF-8");
        entity.setContentType("application/json");
        httpPost.setEntity(entity);

        HttpResponse response = client.execute(httpPost);
        String responseBody = EntityUtils.toString(response.getEntity(), "utf-8");
        if (log.isDebugEnabled()) {
            log.debug("request: {}", requestBody);
            log.debug("status: {}, response: {}", response.getStatusLine().getStatusCode(), responseBody);
        }
        Map map = (Map) YhdgUtils.decodeJson(responseBody, Map.class);
        if ((Boolean) map.get("success") == false) {
            log.debug("授权key错误: {}", key);
            return 0L;
        }

        Map data = (Map) map.get("data");
        String sign = data.get("sign").toString();
        Long time = (Long) data.get("time");
        String sign2 = CodecUtils.md5(key + "YSKJ" + DateFormatUtils.format(new Date(), Constant.DATE_FORMAT) + time);

        if (sign2.equalsIgnoreCase(sign)) {
            return time;
        } else {
            log.debug("签名不一致 sign: {}, sign2: {}", sign, sign2);
        }
        return 0L;
    }

    protected String newOrderId(long id, Calendar calendar, OrderId.OrderIdType type) {
        String result = null;
        if (OrderId.OrderIdType.BATTERY_ORDER == type) {
            String orderNum = String.format(OrderId.NUMBER_FORMAT, id);
            if (orderNum.length() > OrderId.NUMBER_LENGTH) {
                throw new IllegalArgumentException("orderId溢出(" + orderNum + ")");
            }

            result = OrderId.PREFIX_BATTERY + DateFormatUtils.format(calendar, OrderId.DATE_FORMAT) + orderNum;

        }  else if (OrderId.OrderIdType.CUSTOMER_DEPOSIT_ORDER == type) {
            String orderNum = String.format(OrderId.NUMBER_FORMAT, id);
            if (orderNum.length() > OrderId.NUMBER_LENGTH) {
                throw new IllegalArgumentException("orderId溢出(" + orderNum + ")");
            }

            result = OrderId.PREFIX_CUSTOMER_DEPOSIT + DateFormatUtils.format(calendar, OrderId.DATE_FORMAT) + orderNum;

        } else if (OrderId.OrderIdType.INSURANCE_ORDER == type) {
            String orderNum = String.format(OrderId.NUMBER_FORMAT, id);
            if (orderNum.length() > OrderId.NUMBER_LENGTH) {
                throw new IllegalArgumentException("orderId溢出(" + orderNum + ")");
            }

            result = OrderId.PREFIX_INSURANCE + DateFormatUtils.format(calendar, OrderId.DATE_FORMAT) + orderNum;

        } else if (OrderId.OrderIdType.KEEP_ORDER == type) {
            String orderNum = String.format(OrderId.NUMBER_FORMAT, id);
            if (orderNum.length() > OrderId.NUMBER_LENGTH) {
                throw new IllegalArgumentException("orderId溢出(" + orderNum + ")");
            }

            result = OrderId.PREFIX_KEEP + DateFormatUtils.format(calendar, OrderId.DATE_FORMAT) + orderNum;

        } else if (OrderId.OrderIdType.WEIXIN_PAY_ORDER == type) {
            String orderNum = String.format(OrderId.PAY_ORDER_NUMBER_FORMAT, id);
            if (orderNum.length() > OrderId.PAY_ORDER_NUMBER_LENGTH) {
                throw new IllegalArgumentException("orderId溢出(" + orderNum + ")");
            }

            result = OrderId.PREFIX_WEIXIN_PAY + DateFormatUtils.format(calendar, OrderId.PAY_ORDER_DATE_FORMAT) +
                    orderNum;

        } else if (OrderId.OrderIdType.BALANCE_TRANSFER_ORDER == type) {
            String orderNum = String.format(OrderId.NUMBER_FORMAT, id);
            if (orderNum.length() > OrderId.NUMBER_LENGTH) {
                throw new IllegalArgumentException("orderId溢出(" + orderNum + ")");
            }

            result = OrderId.PREFIX_BALANCE_TRANSFER + DateFormatUtils.format(calendar, OrderId.DATE_FORMAT) +
                    orderNum;

        } else if (OrderId.OrderIdType.ALIPAY_PAY_ORDER == type) {
            String orderNum = String.format(OrderId.PAY_ORDER_NUMBER_FORMAT, id);
            if (orderNum.length() > OrderId.PAY_ORDER_NUMBER_LENGTH) {
                throw new IllegalArgumentException("orderId溢出(" + orderNum + ")");
            }

            result = OrderId.PREFIX_ALIPAY_PAY + DateFormatUtils.format(calendar, OrderId.PAY_ORDER_DATE_FORMAT) +
                    orderNum;

        } else if (OrderId.OrderIdType.CUSTOMER_FORGIFT_ORDER == type) {
            String orderNum = String.format(OrderId.NUMBER_FORMAT, id);
            if (orderNum.length() > OrderId.NUMBER_LENGTH) {
                throw new IllegalArgumentException(OrderId.PREFIX_CUSTOMER_FOREGIFT + " orderId溢出(" + orderNum + ")");
            }

            result = OrderId.PREFIX_CUSTOMER_FOREGIFT + DateFormatUtils.format(calendar, OrderId.DATE_FORMAT) + orderNum;

        } else if (OrderId.OrderIdType.PACKET_PERIOD_ORDER == type) {
            String orderNum = String.format(OrderId.NUMBER_FORMAT, id);
            if (orderNum.length() > OrderId.NUMBER_LENGTH) {
                throw new IllegalArgumentException(OrderId.PREFIX_PACKET_PERIOD + " orderId溢出(" + orderNum + ")");
            }

            result = OrderId.PREFIX_PACKET_PERIOD + DateFormatUtils.format(calendar, OrderId.DATE_FORMAT) + orderNum;

        } else if (OrderId.OrderIdType.BACK_BATTERY_ORDER == type) {
            String orderNum = String.format(OrderId.NUMBER_FORMAT, id);
            if (orderNum.length() > OrderId.NUMBER_LENGTH) {
                throw new IllegalArgumentException(OrderId.PREFIX_BACK_BATTERY + " orderId溢出(" + orderNum + ")");
            }

            result = OrderId.PREFIX_BACK_BATTERY + DateFormatUtils.format(calendar, OrderId.DATE_FORMAT) + orderNum;

        } else if (OrderId.OrderIdType.WEIXINMP_PAY_ORDER == type) {
            String orderNum = String.format(OrderId.PAY_ORDER_NUMBER_FORMAT, id);
            if (orderNum.length() > OrderId.PAY_ORDER_NUMBER_LENGTH) {
                throw new IllegalArgumentException("orderId溢出(" + orderNum + ")");
            }

            result = OrderId.PREFIX_WEIXINMP_PAY + DateFormatUtils.format(calendar, OrderId.PAY_ORDER_DATE_FORMAT) + orderNum;

        } else if (OrderId.OrderIdType.WEIXINMA_PAY_ORDER == type) {
            String orderNum = String.format(OrderId.PAY_ORDER_NUMBER_FORMAT, id);
            if (orderNum.length() > OrderId.PAY_ORDER_NUMBER_LENGTH) {
                throw new IllegalArgumentException("orderId溢出(" + orderNum + ")");
            }

            result = OrderId.PREFIX_WEIXINMA_PAY + DateFormatUtils.format(calendar, OrderId.PAY_ORDER_DATE_FORMAT) + orderNum;

        }  else if (OrderId.OrderIdType.ALIPAYFW_PAY_ORDER == type) {
            String orderNum = String.format(OrderId.PAY_ORDER_NUMBER_FORMAT, id);
            if (orderNum.length() > OrderId.PAY_ORDER_NUMBER_LENGTH) {
                throw new IllegalArgumentException("orderId溢出(" + orderNum + ")");
            }

            result = OrderId.PREFIX_ALIPAYFW_PAY + DateFormatUtils.format(calendar, OrderId.PAY_ORDER_DATE_FORMAT) + orderNum;

        } else if (OrderId.OrderIdType.CUSTOMER_REFUND_RECORD == type) {
            String orderNum = String.format(OrderId.NUMBER_FORMAT, id);
            if (orderNum.length() > OrderId.NUMBER_LENGTH) {
                throw new IllegalArgumentException(OrderId.PREFIX_CUSTOMER_REFUND + " orderId溢出(" + orderNum + ")");
            }

            result = OrderId.PREFIX_CUSTOMER_REFUND + DateFormatUtils.format(calendar, OrderId.DATE_FORMAT) + orderNum;

        } else if (OrderId.OrderIdType.LAXIN_PAY_ORDER == type) {
            String orderNum = String.format(OrderId.NUMBER_FORMAT, id);
            if (orderNum.length() > OrderId.NUMBER_LENGTH) {
                throw new IllegalArgumentException(OrderId.PREFIX_LAXIN_PAY + " orderId溢出(" + orderNum + ")");
            }

            result = OrderId.PREFIX_LAXIN_PAY + DateFormatUtils.format(calendar, OrderId.DATE_FORMAT) + orderNum;

        } else if (OrderId.OrderIdType.LAXIN_RECORD == type) {
            String orderNum = String.format(OrderId.NUMBER_FORMAT, id);
            if (orderNum.length() > OrderId.NUMBER_LENGTH) {
                throw new IllegalArgumentException(OrderId.PREFIX_LAXIN_RECORD + " orderId溢出(" + orderNum + ")");
            }

            result = OrderId.PREFIX_LAXIN_RECORD + DateFormatUtils.format(calendar, OrderId.DATE_FORMAT) + orderNum;

        }else if (OrderId.OrderIdType.WITHDRAW_ORDER == type) {
            String orderNum = String.format(OrderId.NUMBER_FORMAT, id);
            if (orderNum.length() > OrderId.NUMBER_LENGTH) {
                throw new IllegalArgumentException(OrderId.PREFIX_WITHDRAW + " orderId溢出(" + orderNum + ")");
            }

            result = OrderId.PREFIX_WITHDRAW + DateFormatUtils.format(calendar, OrderId.DATE_FORMAT) + orderNum;

        }else if (OrderId.OrderIdType.AGENT_DEPOSIT_ORDER == type) {
            String orderNum = String.format(OrderId.NUMBER_FORMAT, id);
            if (orderNum.length() > OrderId.NUMBER_LENGTH) {
                throw new IllegalArgumentException(OrderId.PREFIX_AGENT_DEPOSIT + " orderId溢出(" + orderNum + ")");
            }

            result = OrderId.PREFIX_AGENT_DEPOSIT + DateFormatUtils.format(calendar, OrderId.DATE_FORMAT) + orderNum;

        }else if (OrderId.OrderIdType.BESPEAK_ORDER == type) {
            String orderNum = String.format(OrderId.NUMBER_FORMAT, id);
            if (orderNum.length() > OrderId.NUMBER_LENGTH) {
                throw new IllegalArgumentException(OrderId.PREFIX_BESPEAK + " orderId溢出(" + orderNum + ")");
            }

            result = OrderId.PREFIX_BESPEAK + DateFormatUtils.format(calendar, OrderId.DATE_FORMAT) + orderNum;

        }else if (OrderId.OrderIdType.RENT_FORGIFT_ORDER == type) {
            String orderNum = String.format(OrderId.NUMBER_FORMAT, id);
            if (orderNum.length() > OrderId.NUMBER_LENGTH) {
                throw new IllegalArgumentException(OrderId.PREFIX_RENT_FORGIFT + " orderId溢出(" + orderNum + ")");
            }

            result = OrderId.PREFIX_RENT_FORGIFT + DateFormatUtils.format(calendar, OrderId.DATE_FORMAT) + orderNum;

        }else if (OrderId.OrderIdType.RENT_PERIOD_ORDER == type) {
            String orderNum = String.format(OrderId.NUMBER_FORMAT, id);
            if (orderNum.length() > OrderId.NUMBER_LENGTH) {
                throw new IllegalArgumentException(OrderId.PREFIX_RENT_PERIOD + " orderId溢出(" + orderNum + ")");
            }

            result = OrderId.PREFIX_RENT_PERIOD + DateFormatUtils.format(calendar, OrderId.DATE_FORMAT) + orderNum;

        }else if (OrderId.OrderIdType.RENT_INSURANCE_ORDER == type) {
            String orderNum = String.format(OrderId.NUMBER_FORMAT, id);
            if (orderNum.length() > OrderId.NUMBER_LENGTH) {
                throw new IllegalArgumentException(OrderId.PREFIX_RENT_INSURANCE + " orderId溢出(" + orderNum + ")");
            }

            result = OrderId.PREFIX_RENT_INSURANCE + DateFormatUtils.format(calendar, OrderId.DATE_FORMAT) + orderNum;

        }else if (OrderId.OrderIdType.RENT_ORDER == type) {
            String orderNum = String.format(OrderId.NUMBER_FORMAT, id);
            if (orderNum.length() > OrderId.NUMBER_LENGTH) {
                throw new IllegalArgumentException(OrderId.PREFIX_RENT_ORDER + " orderId溢出(" + orderNum + ")");
            }

            result = OrderId.PREFIX_RENT_ORDER + DateFormatUtils.format(calendar, OrderId.DATE_FORMAT) + orderNum;

        }else if (OrderId.OrderIdType.AGENT_FORGIFT_DEPOSIT_ORDER == type) {
            String orderNum = String.format(OrderId.NUMBER_FORMAT, id);
            if (orderNum.length() > OrderId.NUMBER_LENGTH) {
                throw new IllegalArgumentException(OrderId.PREFIX_AGENT_FORGIFT_DEPOSIT_ORDER + " orderId溢出(" + orderNum + ")");
            }

            result = OrderId.PREFIX_AGENT_FORGIFT_DEPOSIT_ORDER + DateFormatUtils.format(calendar, OrderId.DATE_FORMAT) + orderNum;

        }else if (OrderId.OrderIdType.AGENT_FORGIFT_WITHDRAW_ORDER == type) {
            String orderNum = String.format(OrderId.NUMBER_FORMAT, id);
            if (orderNum.length() > OrderId.NUMBER_LENGTH) {
                throw new IllegalArgumentException(OrderId.PREFIX_AGENT_FORGIFT_WITHDRAW_ORDER + " orderId溢出(" + orderNum + ")");
            }

            result = OrderId.PREFIX_AGENT_FORGIFT_WITHDRAW_ORDER + DateFormatUtils.format(calendar, OrderId.DATE_FORMAT) + orderNum;

        }else if (OrderId.OrderIdType.VEHICLE_PACKET_PERIOD_ORDER == type) {
            String orderNum = String.format(OrderId.NUMBER_FORMAT, id);
            if (orderNum.length() > OrderId.NUMBER_LENGTH) {
                throw new IllegalArgumentException(OrderId.PREFIX_VEHICLE_PACKET_PERIOD_ORDER + " orderId溢出(" + orderNum + ")");
            }

            result = OrderId.PREFIX_VEHICLE_PACKET_PERIOD_ORDER + DateFormatUtils.format(calendar, OrderId.DATE_FORMAT) + orderNum;

        } else if (OrderId.OrderIdType.VEHICLE_CUSTOMER_FORGIFT_ORDER == type) {
            String orderNum = String.format(OrderId.NUMBER_FORMAT, id);
            if (orderNum.length() > OrderId.NUMBER_LENGTH) {
                throw new IllegalArgumentException(OrderId.PREFIX_VEHICLE_CUSTOMER_FORGIFT_ORDER + " orderId溢出(" + orderNum + ")");
            }

            result = OrderId.PREFIX_VEHICLE_CUSTOMER_FORGIFT_ORDER + DateFormatUtils.format(calendar, OrderId.DATE_FORMAT) + orderNum;

        }  else if (OrderId.OrderIdType.VEHICLE_GROUP_ORDER == type) {
            String orderNum = String.format(OrderId.NUMBER_FORMAT, id);
            if (orderNum.length() > OrderId.NUMBER_LENGTH) {
                throw new IllegalArgumentException(OrderId.PREFIX_VEHICLE_GROUP_ORDER + " orderId溢出(" + orderNum + ")");
            }

            result = OrderId.PREFIX_VEHICLE_GROUP_ORDER + DateFormatUtils.format(calendar, OrderId.DATE_FORMAT) + orderNum;

        }   else if (OrderId.OrderIdType.VEHICLE_ORDER == type) {
            String orderNum = String.format(OrderId.NUMBER_FORMAT, id);
            if (orderNum.length() > OrderId.NUMBER_LENGTH) {
                throw new IllegalArgumentException(OrderId.PREFIX_VEHICLE_ORDER + " orderId溢出(" + orderNum + ")");
            }

            result = OrderId.PREFIX_VEHICLE_ORDER + DateFormatUtils.format(calendar, OrderId.DATE_FORMAT) + orderNum;

        }  else {
            throw new IllegalArgumentException();
        }

        return result;
    }

    public List<? extends AreaEntity> setAreaProperties(AreaCache areaCache, List<? extends AreaEntity> list) {
        for (AreaEntity site : list) {
            setAreaProperties(areaCache, site);
        }

        return list;
    }

    public AreaEntity setAreaProperties(AreaCache areaCache, AreaEntity areaEntity) {
        if (areaEntity == null) {
            return null;
        }

        if (areaEntity.getProvinceId() != null) {
            Area area = areaCache.get(areaEntity.getProvinceId());
            if (area != null) {
                areaEntity.setProvinceName(area.getAreaName());
            }
        }
        if (areaEntity.getCityId() != null) {
            Area area = areaCache.get(areaEntity.getCityId());
            if (area != null) {
                areaEntity.setCityName(area.getAreaName());
            }
        }
        if (areaEntity.getDistrictId() != null) {
            Area area = areaCache.get(areaEntity.getDistrictId());
            if (area != null) {
                areaEntity.setDistrictName(area.getAreaName());
            }
        }

        return areaEntity;
    }


}
