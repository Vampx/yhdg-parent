<div class="popup_body" style="min-height: 82%;">
    <div class="ui_table">
        <form method="post">
            <input type="hidden" id="check_total_money">
            <input type="hidden" id="check_total_num">
            <table cellpadding="0" cellspacing="0">
                <tr>
                    <td width="45px">注意：</td>
                    <td width="400px" style="color: #f73f38;">
                        实际退款金额以平台退款金额为准。赠送余额，将退还到余额账户
                    </td>
                    <td></td>
                    <td width="300px" align="right"><a href="javascript:viewRecord('${customerId!}')">退款记录</a></td>
                </tr>
            </table>

            <table cellpadding="0" cellspacing="0">
                <tr>
                    <td width="45px">押金</td>
                    <td width="300px" align="right" style="color: #f73f38;">可退押金=支付押金+电池抵扣券</td>
                    <td></td>
                    <td></td>
                </tr>
            </table>
            <div class="foregift">
            <#if customerForegiftOrderList ?? && customerForegiftOrderList?size &gt; 0>
                <#list customerForegiftOrderList as customerForegiftOrder>
                    <hr>
                    <table cellpadding="0" cellspacing="0">
                        <tr>
                            <input type="checkbox" class="checkbox" payType="${(customerForegiftOrder.payType)!''}" checked="checked" style="display: none">
                            <td width="400px">
                                <div>
                                    可退押金：
                                    <span><#if customerForegiftOrder.refundableMoney ??>${(customerForegiftOrder.refundableMoney/100)!0}<#else>0</#if></span> 元&nbsp;&nbsp;&nbsp;&nbsp;
                                    实退押金：
                                    <span class="edit_money">
                                        <#if customerForegiftOrder.refundMoney?? && customerForegiftOrder.refundMoney != 0>
                                            ${(customerForegiftOrder.refundMoney/100)!}
                                        <#elseif customerForegiftOrder.refundableMoney??>
                                            ${(customerForegiftOrder.refundableMoney/100)!0}
                                        <#else>
                                            0
                                        </#if>
                                    </span> 元&nbsp;&nbsp;&nbsp;&nbsp;
                                    <input type="hidden" name="refundableMoney" value="<#if customerForegiftOrder.refundableMoney ??>${(customerForegiftOrder.refundableMoney/100)!0}<#else>0</#if>">
                                    <input type="hidden" name="realRefundMoney" value="<#if customerForegiftOrder.refundableMoney ??>${(customerForegiftOrder.refundableMoney/100)!0}<#else>0</#if>">
                                    <input type="hidden" name="refundRecordId" value="${(customerForegiftOrder.refundRecordId)!''}">
                                    <input type="hidden" name="sourceType" value="${(customerForegiftOrder.sourceType)!''}">
                                    <input type="hidden" name="sourceId" value="${(customerForegiftOrder.id)!''}">
                                    <a class="changeMoney" href="javascript:void(0)">修改金额</a>
                                    &nbsp;&nbsp;&nbsp;&nbsp;${(customerForegiftOrder.statusName)!''}
                                </div>
                                <div style="padding-top: 5px;">
                                    套餐押金：<#if customerForegiftOrder.price ??>${(customerForegiftOrder.price/100)!0}<#else>0</#if> 元&nbsp;
                                    押金劵：<#if customerForegiftOrder.ticketMoney ??>${(customerForegiftOrder.ticketMoney/100)!0}<#else>0</#if> 元&nbsp;
                                    电池抵扣券：<#if customerForegiftOrder.deductionTicketMoney ??>${(customerForegiftOrder.deductionTicketMoney/100)!0}<#else>0</#if>元&nbsp;
                                </div>
                            </td>
                            <td width="330px">
                                <div>
                                    <#if customerForegiftOrder.payType == 1>
                                        余额支付
                                    <#else>
                                        支付订单号：${(customerForegiftOrder.payOrderId)!''}
                                    </#if>
                                </div>
                                <div style="padding-top: 5px;">
                                    <span style="">支付时间：${(customerForegiftOrder.handleTime?string('yyyy-MM-dd HH:mm:ss'))!}&nbsp;&nbsp;&nbsp;</span>
                                    支付金额：<#if customerForegiftOrder.payOrderMoney ??>${(customerForegiftOrder.payOrderMoney/100)!0}<#else >0</#if> 元
                                </div>
                                <div style="padding-top: 5px;">
                                    备注：${(customerForegiftOrder.memo)!''}
                                </div>
                            </td>
                        </tr>
                    </table>
                </#list>
            </#if>
                <div style="border-bottom: 10px solid #f0f0f0;"></div>
            </div>

            <div style="border-bottom: 10px solid #f0f0f0;"></div>
            <table cellpadding="0" cellspacing="0">
                <tr>
                    <td width="45px">租金</td>
                    <td width="300px" align="right" style="color: #f73f38;">注意：使用过的租金建议不退</td>
                    <td></td>
                    <td></td>
                </tr>
            </table>

            <div class="packetPeriodPrice">
            <#if packetPeriodOrderList ?? && packetPeriodOrderList?size &gt; 0>
                <#list packetPeriodOrderList as packetPeriodOrder>
                    <hr>
                    <table cellpadding="0" cellspacing="0">
                        <tr>
                            <input type="checkbox" class="checkbox" payType="${(packetPeriodOrder.payType)!''}" checked="checked" style="display: none">
                            <td width="400px">
                                <div>
                                    可退租金：
                                    <span><#if packetPeriodOrder.refundableMoney ??>${(packetPeriodOrder.refundableMoney/100)!0}<#else >0</#if></span> 元&nbsp;&nbsp;&nbsp;&nbsp;
                                    实退租金：
                                    <span class="edit_money"><#if packetPeriodOrder.refundableMoney ??>${(packetPeriodOrder.refundableMoney/100)!0}<#else >0</#if></span> 元&nbsp;&nbsp;&nbsp;&nbsp;
                                    <input type="hidden" name="refundableMoney" value="<#if packetPeriodOrder.refundableMoney ??>${(packetPeriodOrder.refundableMoney/100)!0}<#else >0</#if>">
                                    <input type="hidden" name="realRefundMoney" value="<#if packetPeriodOrder.refundableMoney ??>${(packetPeriodOrder.refundableMoney/100)!0}<#else >0</#if> ">
                                    <input type="hidden" name="refundRecordId" value="${(packetPeriodOrder.refundRecordId)!''}">
                                    <input type="hidden" name="sourceType" value="${(packetPeriodOrder.sourceType)!''}">
                                    <input type="hidden" name="sourceId" value="${(packetPeriodOrder.id)!''}">
                                    <a class="changeMoney" href="javascript:void(0)">修改金额</a>&nbsp;&nbsp;&nbsp;&nbsp;
                                ${(packetPeriodOrder.statusName)!''}
                                </div>
                                <div style="padding-top: 5px;">
                                    套餐价格：<#if packetPeriodOrder.price ??>${(packetPeriodOrder.price/100)!0}<#else >0</#if> 元&nbsp;
                                    租金劵：<#if packetPeriodOrder.ticketMoney ??>${(packetPeriodOrder.ticketMoney/100)!0}<#else >0</#if> 元&nbsp;
                                </div>
                            </td>
                            <td width="330px">
                                <div style="padding-top: 5px;">
                                    <#if packetPeriodOrder.payType == 1>
                                        余额支付
                                    <#else>
                                        支付订单号：${(packetPeriodOrder.payOrderId)!''}
                                    </#if>
                                </div>

                                <div style="padding-top: 5px;">
                                    <span style="">支付时间：${(packetPeriodOrder.handleTime?string('yyyy-MM-dd HH:mm:ss'))!}&nbsp;&nbsp;&nbsp;</span>
                                    支付金额：<#if packetPeriodOrder.payOrderMoney ??>${(packetPeriodOrder.payOrderMoney/100)!''}<#else >0</#if> 元
                                </div>
                                <div style="padding-top: 5px;">
                                    备注：${(packetPeriodOrder.memo)!''}
                                </div>
                            </td>
                        </tr>
                    </table>
                </#list>
            </#if>
                <div style="border-bottom: 10px solid #f0f0f0;"></div>
            </div>

            <div style="border-bottom: 10px solid #f0f0f0;"></div>
            <table cellpadding="0" cellspacing="0">
                <tr>
                    <td width="45px">多通道</td>
                    <td width="300px" align="right" style="color: #f73f38;">注意：已结清的多通道订单不在此显示</td>
                    <td></td>
                    <td></td>
                </tr>
            </table>

            <div class="customerMulti">
                <#if customerMultiOrderList ?? && customerMultiOrderList?size &gt; 0>
                    <#list customerMultiOrderList as customerMultiOrder>
                        <hr>
                        <table cellpadding="0" cellspacing="0">
                            <tr>
                                <input type="checkbox" class="checkbox" payType="15" checked="checked" style="display: none">
                                <td width="700px">
                                    <div style="padding-top: 10px;">
                                        可退金额：
                                        <span class="edit_money">${(customerMultiOrder.refundableMoney/100)!0}</span>
                                        <input type="hidden" name="refundableMoney" value="${(customerMultiOrder.refundableMoney/100)!0}">
                                        <input type="hidden" name="realRefundMoney" value="${(customerMultiOrder.refundableMoney/100)!0}">
                                        <input type="hidden" name="refundRecordId" value="${(customerMultiOrder.refundRecordId)!''}">
                                        <input type="hidden" name="sourceType" value="${(customerMultiOrder.sourceType)!''}">
                                        <input type="hidden" name="sourceId" value="${(customerMultiOrder.id)!''}">
                                        元（${(customerMultiOrder.statusName)!''}）
                                        &nbsp;&nbsp;&nbsp;&nbsp;<a class="changeMoney" href="javascript:void(0)">修改金额</a>&nbsp;&nbsp;&nbsp;&nbsp;
                                    </div>
                                </td>
                            </tr>
                        </table>
                    </#list>
                </#if>
                <div style="border-bottom: 10px solid #f0f0f0;"></div>
            </div>

            <div style="border-bottom: 10px solid #f0f0f0;"></div>
            <table cellspacing="0" cellpadding="0">
                <tr>
                    <td>退款方式：</td>
                    <td>
                        <span class="radio_box">
                            <input type="radio" class="radio" name="refundType" id="refund_type_2" checked value="2"/><label for="refund_type_2">原路退回</label>
                        </span>
                        &nbsp;&nbsp;&nbsp;&nbsp;
                        <span class="radio_box">
                            <input type="radio" class="radio" name="refundType" id="refund_type_1" value="1"/><label for="refund_type_1">退款到客户余额</label>
                        </span>
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

                </td>
                <td width="270px;"><#if applyRefundTime ?? >申请退款时间：${app.format_date_time(applyRefundTime)}</#if> </td>
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
    checkPayType();
    function checkPayType(){
        var payType=0;
        $('#${pid}').find('input[type=checkbox]:checked').each(function () {
            payType = $(this).attr("payType");
            if(payType==14||payType==15){
                return;
            }
        })
        if(payType==14||payType==15){
            $('#refund_type_2').parent().hide();
            $('#refund_type_1').attr("checked",true);
        }else {
            $('#refund_type_2').parent().show();
        }
    }

    (function() {
        var pid = '${pid}',
                win = $('#' + pid),
                windowData = win.data('windowData'),
                form = win.find('form');
        //获取退款金额
        var totalMoney = parseInt(Math.round(0));
        var refundMoneys = $('input[name=realRefundMoney');
        for(var i = 0; i < refundMoneys.length; i++) {
            addMoney = parseInt(Math.round(refundMoneys[i].value * 100));
            totalMoney = totalMoney + addMoney;
        }
        $('#total_money').html("本次退款金额："+(totalMoney/100).toFixed(2) +"元");
        $('#check_total_num').val(refundMoneys.length);
        $('#check_total_money').val((totalMoney/100).toFixed(2));


        win.find('button.ok').click(function() {
            var checkTotalNum = $('#check_total_num').val();
            if(checkTotalNum == 0) {
                $.messager.alert('提示信息', '无订单，不可退款', 'info');
                return;
            }
            // var checkTotalMoney = $('#check_total_money').val();
            // if(checkTotalMoney == parseInt(Math.round(0)).toFixed(2)) {
            //     $.messager.alert('提示信息', '金额为0不可退款', 'info');
            //     return;
            // }
            $.messager.confirm('提示信息', $('#total_money').html()+'<br/><br/>是否确认?', function (ok) {
                if (ok) {
                    var orderList = [];
                    refundMoneys.each(function () {
                        //获取退款记录表id
                        var refundRecordId = $(this).parent().parent().parent().find('input[name=refundRecordId]').val();
                        var sourceType = $(this).parent().parent().parent().find('input[name=sourceType]').val();
                        var sourceId = $(this).parent().parent().parent().find('input[name=sourceId]').val();
                        var refundMoney = parseInt(Math.round($(this).parent().parent().parent().find('input[name=realRefundMoney]').val() * 100));
                        var orderMap = {};
                        orderMap["refundRecordId"] = refundRecordId;
                        orderMap["sourceType"] = sourceType;
                        orderMap["sourceId"] = sourceId;
                        orderMap["refundMoney"] = refundMoney;
                        orderList.push(orderMap);
                    })

                    var values = {};
                    var refundType = $('input[name=refundType]:checked').val();
                    values["customerId"] = ${customerId};
                    values["refundType"] = refundType;
                    values["orderList"] = orderList;

                    $.post('${contextPath}/security/zd/rent_refund/audit_money.htm', {
                        data : $.toJSON(values)
                    }, function(json) {
                        <@app.json_jump/>
                        if(json.success) {
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

    function viewRecord(id) {
        App.dialog.show({
            css: 'width:800px;height:680px;',
            title: '退款记录',
            href: "${contextPath}/security/basic/customer_refund_record/index.htm?customerId=" + id
        });
    }


    $('.changeMoney').click(function () {
        var refundableMoney = parseInt(Math.round($(this).parent().parent().parent().find('input[name=refundableMoney]').val() * 100));
        var realRefundMoney = parseInt(Math.round($(this).parent().parent().parent().find('input[name=realRefundMoney]').val() * 100));
        var input = $(this).parent().parent().parent().find('input[name=realRefundMoney]');
        var span =  $(this).parent().parent().parent().find('span[class=edit_money]');
        App.dialog.show({
            css: 'width:242px;height:149px;overflow:visible;',
            title: '修改金额',
            href: "${contextPath}/security/zd/rent_refund/edit_money.htm?refundableMoney=" + refundableMoney +"&realRefundMoney=" + realRefundMoney ,
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
                    $('#total_money').html("本次退款金额："+(totalMoney/100).toFixed(2) +"元");
                    $('#check_total_money').val((totalMoney/100).toFixed(2));

                }
            },
            event: {
                onClose: function () {
                }
            }
        });
    })

</script>