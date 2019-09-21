<div class="popup_body" style="min-height: 82%;">
    <div class="ui_table">
        <form method="post">
            <input type="hidden" id="check_total_money">
            <input type="hidden" id="check_total_num">
            <input type="hidden" name="sourceId" <#if entity.id ?? >value="${entity.id}" </#if>>
            <input type="hidden" name="totalRefundableMoney" value="${refundableMoney/100}">
            <input type="hidden" name="totalRealRefundMoney" value="${refundableMoney/100}">
            <table cellpadding="0" cellspacing="0">
                <tr>
                    <td width="45px">注意：</td>
                    <td width="400px" style="color: #f73f38;">
                        实际退款金额以平台退款金额为准。赠送余额，将退还到余额账户
                    </td>
                    <td></td>
                </tr>
            </table>

            <table cellpadding="0" cellspacing="0">
                <tr>
                    <td width="70px"><input type="checkbox" class="foregiftCheckbox" >&nbsp;&nbsp;押金</td>
                    <td width="300px" align="right" style="color: #f73f38;">可退押金=支付押金+抵扣券</td>
                    <td></td>
                    <td></td>
                </tr>
            </table>
            <div class="foregift">
                <hr>
                <table cellpadding="0" cellspacing="0">
                    <tr>
                        <td width="45px">
                            <input type="checkbox" class="checkbox">
                        </td>
                        <td width="400px">
                            <div>
                                可退押金：
                                <span><#if foregiftRefundableMoney??>${(foregiftRefundableMoney/100)!0}<#else>0</#if></span> 元
                                <#if vehicleForegiftOrder?? || batteryForegiftOrder??>
                                    （<#if vehicleForegiftOrder ??>车辆订单：${(vehicleForegiftOrder.statusName)!''}/</#if><#if batteryForegiftOrder ??> 电池订单：${(batteryForegiftOrder.statusName)!''}</#if>）
                                </#if>
                            </div>
                            <div style="margin-top: 5px;">
                                实退押金：
                                <span class="edit_money">
                                <#if foregiftRefundableMoney??>${(foregiftRefundableMoney/100)!0}<#else>0</#if>
                                    </span> 元&nbsp;&nbsp;&nbsp;&nbsp;
                                <input type="hidden" name="realRefundMoney" value="<#if foregiftRefundableMoney ??>${(foregiftRefundableMoney/100)!0}<#else>0</#if>">
                                <input type="hidden" name="refundableMoney" value="<#if foregiftRefundableMoney ??>${(foregiftRefundableMoney/100)!0}<#else>0</#if>">
                                <a class="changeMoney" href="javascript:void(0)">修改金额</a>
                                &nbsp;&nbsp;&nbsp;&nbsp;
                            </div>
                            <div style="padding-top: 5px;">
                                套餐押金：&nbsp;<#if foregiftPrice ??>${(foregiftPrice/100)!0}<#else>0</#if> 元&nbsp;
                                押金劵：<#if entity.foregiftTicketMoney ??>${(entity.foregiftTicketMoney/100)!0}<#else>0</#if> 元&nbsp;
                                抵扣券：<#if entity.deductionTicketMoney ??>${(entity.deductionTicketMoney/100)!0}<#else>0</#if> 元&nbsp;
                            </div>
                        </td>
                        <td width="330px">
                            <div>
                                租车组合支付
                            </div>
                            <div style="padding-top: 5px;">
                                <span style="">支付时间：${(entity.payTime?string('yyyy-MM-dd HH:mm:ss'))!}&nbsp;&nbsp;&nbsp;</span>
                                支付金额：<#if foregiftPayMoney ??>${(foregiftPayMoney/100)!0}<#else >0</#if> 元
                            </div>
                        </td>
                    </tr>
                </table>
                <div style="border-bottom: 10px solid #f0f0f0;"></div>
            </div>

            <div style="border-bottom: 10px solid #f0f0f0;"></div>
            <table cellpadding="0" cellspacing="0">
                <tr>
                    <td width="70px"><input type="checkbox" class="packetCheckbox" >&nbsp;&nbsp;租金</td>
                    <td width="300px" align="right" style="color: #f73f38;">注意：使用过的租金建议不退</td>
                    <td></td>
                    <td></td>
                </tr>
            </table>

            <div class="packetPeriodPrice">
                <hr>
                <table cellpadding="0" cellspacing="0">
                    <tr>
                        <td width="45px">
                            <input type="checkbox" class="checkbox">
                        </td>
                        <td width="400px">
                            <div>
                                可退租金：
                                <span><#if packetRefundableMoney ??>${(packetRefundableMoney/100)!0}<#else >0</#if></span> 元
                                <#if vehiclePeriodOrder ?? || batteryPeriodOrder ??>
                                    （<#if vehiclePeriodOrder ??>车辆订单：${(vehiclePeriodOrder.statusName)!''}/</#if><#if batteryPeriodOrder ??> 电池订单：${(batteryPeriodOrder.statusName)!''}</#if>）
                                </#if>
                            </div>
                            <div style="margin-top: 5px;">
                                实退租金：
                                <span class="edit_money"><#if packetRefundableMoney ??>${(packetRefundableMoney/100)!0}<#else >0</#if></span> 元&nbsp;&nbsp;&nbsp;&nbsp;

                                <input type="hidden" name="realRefundMoney" value="<#if packetRefundableMoney ??>${(packetRefundableMoney/100)!0}<#else>0</#if>">
                                <input type="hidden" name="refundableMoney" value="<#if packetRefundableMoney ??>${(packetRefundableMoney/100)!0}<#else>0</#if>">
                                <a class="changeMoney" href="javascript:void(0)">修改金额</a>&nbsp;&nbsp;&nbsp;&nbsp;
                            </div>
                            <div style="padding-top: 5px;">
                                套餐价格：&nbsp;<#if periodPrice ??>${(periodPrice/100)!0}<#else >0</#if> 元&nbsp;
                                租金劵：<#if entity.rentTicketMoney ??>${(entity.rentTicketMoney/100)!0}<#else >0</#if> 元&nbsp;
                            </div>
                        </td>
                        <td width="330px">
                            <div style="padding-top: 5px;">
                                租车组合支付
                            </div>
                            <div style="padding-top: 5px;">
                                <span style="">支付时间：${(entity.payTime?string('yyyy-MM-dd HH:mm:ss'))!}&nbsp;&nbsp;&nbsp;</span>
                                支付金额：<#if packetPayMoney ??>${(packetPayMoney/100)!''}<#else >0</#if> 元
                            </div>
                        </td>
                    </tr>
                </table>
                <div style="border-bottom: 10px solid #f0f0f0;"></div>
            </div>

            <div style="border-bottom: 10px solid #f0f0f0;"></div>
            <table cellspacing="0" cellpadding="0">
                <tr>
                    <td>退款方式：</td>
                    <td>
                    <#if entity.payType == 14 || entity.payType == 15 || entity.payType == 16>
                        <span class="radio_box">
                                    <input type="radio" class="radio" name="refundType" id="refund_type_1" checked  value="1"/><label for="refund_type_1">退款到客户余额</label>
                                </span>
                    <#else >
                        <span class="radio_box">
                                <input type="radio" class="radio" name="refundType" id="refund_type_2" checked value="2"/><label for="refund_type_2">原路退回</label>
                            </span>
                        &nbsp;&nbsp;&nbsp;&nbsp;
                        <span class="radio_box">
                                <input type="radio" class="radio" name="refundType" id="refund_type_1"  value="1"/><label for="refund_type_1">退款到客户余额</label>
                            </span>
                    </#if>
                    </td>
                </tr>
            </table>
        </form>
    </div>
</div>
<div>
    <div class="popup_btn" style="height: 10%">
        <table >
            <tr>
                <td align="left" id="total_money" width="200px;" style="font-size: 14px;color: red">
                    本次退款金额：<span id="money_detail" style="color: #FF4012;">0.00</span>元
                </td>
                <td width="270px;"><#if entity.applyRefundTime ?? >申请退款时间：${app.format_date_time(entity.applyRefundTime)}</#if> </td>
                <td width="150px;">
                    <button  class="btn btn_red ok">退款</button>
                </td>
                <td>
                    <button class="btn btn_border close">取消</button>
                </td>
            </tr>
        </table>
    </div>
</div>

<script>

    (function() {
        var pid = '${pid}',
                win = $('#' + pid),
                windowData = win.data('windowData'),
                form = win.find('form');
        //获取退款金额
//        var totalMoney = parseInt(Math.round(0));
//        var refundMoneys = $('input[name=realRefundMoney');
//        for(var i = 0; i < refundMoneys.length; i++) {
//            addMoney = parseInt(Math.round(refundMoneys[i].value * 100));
//            totalMoney = totalMoney + addMoney;
//        }
//
//        $('#total_money').html("本次退款金额："+(totalMoney/100).toFixed(2) +" 元");
//        $('#check_total_money').val((totalMoney/100).toFixed(2));

        win.find('button.ok').click(function() {
            var checkTotalNum = $('#check_total_num').val();
            if(checkTotalNum == 0) {
                $.messager.alert('提示信息', '请选择订单', 'info');
                return;
            }

            $.messager.confirm('提示信息', $('#total_money').html()+'<br/><br/>是否确认?', function (ok) {
                if (ok) {
                    var values = {};
                    var refundType = $('input[name=refundType]:checked').val();
                    var refundMoney = parseInt(Math.round(win.find('input[name=totalRealRefundMoney]').val() * 100));
                    var refundableMoney = parseInt(Math.round(win.find('input[name=totalRefundableMoney]').val() * 100));
                    values["sourceId"] = '${(entity.id)!''}';
                    values["refundMoney"] = refundMoney;
                    values["refundableMoney"] = refundableMoney;
                    values["customerId"] = ${(entity.customerId)!''};
                    values["refundType"] = refundType;

                    console.log(values);

                    $.post('${contextPath}/security/zc/group_order/refund_money.htm', {
                        data: $.toJSON(values)
                    }, function (json) {
                    <@app.json_jump/>
                        if (json.success) {
                            $.messager.alert('提示信息', json.message, 'info');
                            win.window('close');
                        } else {
                            $.messager.alert('提示信息', json.message, 'info');
                        }
                    }, 'json');
                }
            });
        });

        win.find('button.close').click(function() {
            win.window('close');
        });
    })()

    //单个复选框
    $('.checkbox').change(function () {
        if($(this).attr('checked')=="checked") {
            $(this).parent().parent().parent().css("background", "#4263ff");
        }else{
            $(this).parent().parent().parent().css("background", "#ffffff");
        }
    });

    //全选所有押金
    $('.foregiftCheckbox').change(function () {
        if ($(this).attr('checked') == "checked") {
            $('.foregift').find('input[type=checkbox]').attr('checked', true);
        }else{
            $('.foregift').find('input[type=checkbox]').attr('checked', false);
        }
    });

    //全选所有租金
    $('.packetCheckbox').change(function () {
        if ($(this).attr('checked') == "checked") {
            $('.packetPeriodPrice').find('input[type=checkbox]').attr('checked', true);
        }else{
            $('.packetPeriodPrice').find('input[type=checkbox]').attr('checked', false);
        }
    });

    //复选框状态改变，更新样式，更新退款总金额
    $('input[type=checkbox]').change(function () {
        var totalMoney = parseInt(Math.round(0));
        //选中部分
        $('input[class=checkbox]:checked').each(function () {
            //更新样式
            $(this).parent().parent().parent().css("background", "#4263ff");
            //更新总金额
            var addMoney = parseInt(Math.round($(this).parent().parent().parent().find('input[name=realRefundMoney]').val()*100));
            totalMoney = totalMoney + addMoney;
        });
        $('#money_detail').html((totalMoney / 100).toFixed(2));
        $('#check_total_money').val((totalMoney/100).toFixed(2));
        $('#check_total_num').val($('input[class=checkbox]:checked').length);
        var win = $('#${pid}');
        win.find('input[name=totalRealRefundMoney]').val(totalMoney/100);

        //未选中部分
        $('input[class=checkbox]:not(:checked)').each(function () {
            //更新样式
            $(this).parent().parent().parent().css("background", "#ffffff");
        });
    });

    $('.changeMoney').click(function () {
        var refundableMoney = parseInt(Math.round($(this).parent().parent().parent().find('input[name=refundableMoney]').val() * 100));
        var realRefundMoney = parseInt(Math.round($(this).parent().parent().parent().find('input[name=realRefundMoney]').val() * 100));
        var input = $(this).parent().parent().parent().find('input[name=realRefundMoney]');
        var span =  $(this).parent().parent().parent().find('span[class=edit_money]');
        App.dialog.show({
            css: 'width:242px;height:149px;overflow:visible;',
            title: '修改金额',
            href: "${contextPath}/security/zc/group_order/edit_money.htm?refundableMoney=" + refundableMoney +"&realRefundMoney=" + realRefundMoney ,
            windowData: {
                ok: function (realRefundMoney) {
                    input.val(realRefundMoney);
                    span.html(realRefundMoney);
                    //更新退款金额
                    var totalMoney = parseInt(Math.round(0));
                    var refundMoneys = $('input[name=realRefundMoney');
                    for(var i = 0; i < refundMoneys.length; i++) {
                        addMoney = parseInt(Math.round(refundMoneys[i].value * 100));
                        totalMoney = totalMoney + addMoney;
                    }
                    $('#total_money').html("本次退款金额："+(totalMoney/100).toFixed(2) +" 元");
                    $('#check_total_money').val((totalMoney/100).toFixed(2));
                    var win = $('#${pid}');
                    win.find('input[name=totalRealRefundMoney]').val(totalMoney/100);
                }
            },
            event: {
                onClose: function () {
                }
            }
        });
    })

</script>