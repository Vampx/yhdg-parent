package cn.com.yusong.yhdg.batteryserver.web.controller.security.hdg;

import cn.com.yusong.yhdg.batteryserver.entity.Param;
import cn.com.yusong.yhdg.batteryserver.entity.Result;
import cn.com.yusong.yhdg.batteryserver.service.hdg.BatteryService;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;

@Controller
@RequestMapping(value = "/security/hdg/battery")
public class BatteryController extends SecurityController {

    static final Logger log = LogManager.getLogger(BatteryController.class);

    @Autowired
    BatteryService batteryService;


    @ResponseBody
    @RequestMapping(value = "report.htm")
    public String report(@RequestParam(value = "a") String code,
                         @RequestParam(value = "b", required = false) String version,
                         @RequestParam(value = "c", required = false) String voltage,
                         @RequestParam(value = "d", required = false) String electricity,
                         @RequestParam(value = "e", required = false) String surplusVolume,
                         @RequestParam(value = "f", required = false) String volume,
                         @RequestParam(value = "g", required = false) String second,
                         @RequestParam(value = "h", required = false) String produceDate,
                         @RequestParam(value = "i", required = false) String protectState,
                         @RequestParam(value = "j", required = false) String fet,
                         @RequestParam(value = "k", required = false) String batteryStrand,
                         @RequestParam(value = "l", required = false) String ntcSize,
                         @RequestParam(value = "m", required = false) String ntc,
                         @RequestParam(value = "n", required = false) String lng_,
                         @RequestParam(value = "o", required = false) String lat_,
                         @RequestParam(value = "p", required = false) String fetStatus,
                         @RequestParam(value = "q", required = false) String positionState,
                         @RequestParam(value = "r", required = false) String signal,
                         @RequestParam(value = "s") String chargeStatus,
                         @RequestParam(value = "t", required = false) String simCode,
                         @RequestParam(value = "u", required = false) String singleVoltage,
                         @RequestParam(value = "v", required = false) String percent,
                         @RequestParam(value = "w", required = false) Integer hit,
                         @RequestParam(value = "x", required = false) Integer dismantle) {

        if (log.isDebugEnabled()) {
            log.debug("code(a): {}, version(b): {}, voltage(c): {}, electricity(d): {}, surplusVolume(e): {}, volume(f): {}, second(g): {}, produceDate(h): {}, protectState(i): {}, fet(j): {}, batteryStrand(k):{},ntcSize(l):{},ntc(m):{},n(n):{},o(o):{},fetStatus(p):{},positionState(q):{},signal(r):{},chargeStatus(s):{}, simCode(t):{},singleVoltage(u):{}, percent(v):{}, hit(w):{}, dismantle(x):{}", code, version, voltage, electricity, surplusVolume, volume, second, produceDate, protectState, fet, batteryStrand, ntcSize, ntc, lng_, lat_, fetStatus, positionState, signal, chargeStatus, simCode, singleVoltage, percent, hit, dismantle);
        }
        Param param = new Param(code, version, voltage, electricity, surplusVolume, volume, second, produceDate, protectState, fet, batteryStrand, ntcSize, ntc, lng_, lat_, fetStatus, positionState, signal, chargeStatus, simCode, singleVoltage, percent, hit, dismantle);

        Result result = batteryService.report(param);

        String msg = String.format("a=%d,b=%d,c=%d,d=%d,e=%d,h=%d,i=%d,j=%d,k=%d,l=%d,", result.rtnCode, result.chargeType, result.duration, result.nextHeartbeat, result.reportSingleVoltage, result.gpsSwitch, result.lockSwitch, result.gprsShutdown, result.shutdownVoltage, result.acceleretedSpeed);
        if (log.isDebugEnabled()) {
            log.debug("id: {} code: {}, response {}", result.id, result.code, msg);
        }

        return msg;
    }

    @ResponseBody
    @RequestMapping(value = "update.htm")
    public int update(@RequestParam(value = "a", required = false) String code) {
        return batteryService.updateWaitCharge(code);
    }

}
