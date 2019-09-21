<#assign imagePath='${contextPath}/static/images' >
<div class="popup_body" style="min-height: 82%;">
    <div class="ui_table">
        <form method="post">
            <input type="hidden" id="check_total_money">
            <input type="hidden" id="check_total_num">
            <table cellpadding="0" cellspacing="0"  style="margin-bottom: 20px;">
                <tr>
                    <td width="45px">注意：</td>
                    <td width="400px" style="color: #f73f38;">
                        实际退款金额以平台退款金额为准。赠送余额，将退还到余额账户
                    </td>
                    <td width="300px" align="right">
                        <a href="javascript:viewRecord('${customerId!}')">
                            <span>
                                <img src="${imagePath}/img/inside/record.png"/>
                            </span>
                            退款记录
                        </a>
                    </td>
                </tr>
            </table>
            <table cellpadding="0" cellspacing="0">
                <tr>
                    <td width="60px" style="font-weight: 600"><input type="checkbox" class="foregiftCheckbox" >&nbsp;&nbsp;押金</td>
                    <td width="300px" style="color: #f73f38;">可退押金=支付押金+电池抵扣券</td>
                    <td></td>
                    <td></td>
                </tr>
            </table>
            <hr>

            <div class="foregift">
            <#if customerForegiftOrderList ?? && customerForegiftOrderList?size &gt; 0>
                <#list customerForegiftOrderList as customerForegiftOrder>
                    <table cellpadding="0" cellspacing="0">
                        <tr>
                            <input type="hidden">
                            <td width="45px">
                                <input type="checkbox" class="checkbox" payType="${(customerForegiftOrder.payType)!''}">
                            </td>
                            <td width="700px">
                                <div style="padding-top: 10px;">
                                    可退押金：
                                    <span class="edit_money"><#if customerForegiftOrder.refundableMoney ??>${(customerForegiftOrder.refundableMoney/100)!0}<#else>0</#if> </span>
                                    <input type="hidden" name="refundableMoney" value="<#if customerForegiftOrder.refundableMoney ??>${(customerForegiftOrder.refundableMoney/100)!0}<#else>0</#if>">
                                    <input type="hidden" name="realRefundMoney" value="<#if customerForegiftOrder.refundableMoney ??>${(customerForegiftOrder.refundableMoney/100)!0}<#else>0</#if>">
                                    <input type="hidden" name="refundRecordId" value="${(customerForegiftOrder.refundRecordId)!''}">
                                    <input type="hidden" name="sourceType" value="${(customerForegiftOrder.sourceType)!''}">
                                    <input type="hidden" name="sourceId" value="${(customerForegiftOrder.id)!''}">
                                    元（${(customerForegiftOrder.statusName)!''}）&nbsp;&nbsp;&nbsp;&nbsp;
                                    <a class="changeMoney" href="javascript:void(0)">修改金额</a>&nbsp;&nbsp;&nbsp;&nbsp;
                                </div>
                                <div style="padding-top: 10px;color: #999999;">
                                    套餐押金：<#if customerForegiftOrder.price ??>${(customerForegiftOrder.price/100)!0}<#else>0</#if> 元&nbsp;&nbsp;&nbsp;&nbsp;
                                    押金劵：<#if customerForegiftOrder.ticketMoney ??>${(customerForegiftOrder.ticketMoney/100)!0}<#else>0</#if> 元&nbsp;&nbsp;&nbsp;&nbsp;
                                    电池抵扣券：<#if customerForegiftOrder.deductionTicketMoney ??>${(customerForegiftOrder.deductionTicketMoney/100)!0}<#else>0</#if>元&nbsp;
                                </div>
                                <div style="padding-top: 10px;">
                                    <#if customerForegiftOrder.payType == 1>
                                        余额支付&nbsp;&nbsp;&nbsp;&nbsp;
                                    <#else>
                                        支付订单号：${(customerForegiftOrder.payOrderId)!''}&nbsp;&nbsp;&nbsp;&nbsp;
                                    </#if>
                                </div>
                                <div style="padding-top: 10px;">
                                    支付金额：<#if customerForegiftOrder.payOrderMoney ??>${(customerForegiftOrder.payOrderMoney/100)!0}<#else >0</#if> 元&nbsp;&nbsp;&nbsp;&nbsp;
                                    <span style="color: #999999;">支付时间：${(customerForegiftOrder.handleTime?string('yyyy-MM-dd HH:mm:ss'))!}&nbsp;&nbsp;&nbsp;&nbsp;</span>
                                    备注：${(customerForegiftOrder.memo)!''}
                                </div>
                            </td>
                        </tr>
                    </table>
                <hr>
                </#list>
            <#else>
                <div style="text-align-last:center;color:  #999999;">
                    暂无可退押金
                </div>
            </#if>
                <div style="border-bottom: 20px solid #fff;"></div>
            </div>

            <table cellpadding="0" cellspacing="0">
                <tr>
                    <td width="60px" style="font-weight: 600"><input type="checkbox" class="packetCheckbox" >&nbsp;&nbsp;租金</td>
                    <td width="300px" style="color: #f73f38;">注意：使用过的租金建议不退</td>
                    <td></td>
                    <td></td>
                </tr>
            </table>
            <hr>

            <div class="packetPeriodPrice">
            <#if packetPeriodOrderList ?? && packetPeriodOrderList?size &gt; 0>
                <#list packetPeriodOrderList as packetPeriodOrder>
                    <table cellpadding="0" cellspacing="0">
                        <tr>
                            <td width="45px">
                                <input type="checkbox" class="checkbox" payType="${(packetPeriodOrder.payType)!''}">
                            </td>
                            <td width="700px">
                                <div style="padding-top: 10px;">
                                    可退租金：
                                    <span class="edit_money"><#if packetPeriodOrder.refundableMoney ??>${(packetPeriodOrder.refundableMoney/100)!0}<#else >0</#if> </span>
                                    <input type="hidden" name="refundableMoney" value="<#if packetPeriodOrder.refundableMoney ??>${(packetPeriodOrder.refundableMoney/100)!0}<#else >0</#if>">
                                    <input type="hidden" name="realRefundMoney" value="<#if packetPeriodOrder.refundableMoney ??>${(packetPeriodOrder.refundableMoney/100)!0}<#else >0</#if> ">
                                    <input type="hidden" name="refundRecordId" value="${(packetPeriodOrder.refundRecordId)!''}">
                                    <input type="hidden" name="sourceType" value="${(packetPeriodOrder.sourceType)!''}">
                                    <input type="hidden" name="sourceId" value="${(packetPeriodOrder.id)!''}">
                                    元（${(packetPeriodOrder.statusName)!''}）&nbsp;&nbsp;&nbsp;&nbsp;
                                    <a class="changeMoney" href="javascript:void(0)">修改金额</a>&nbsp;&nbsp;&nbsp;&nbsp;
                                </div>
                                <div style="padding-top: 10px;color: #999999;">
                                    套餐价格：<#if packetPeriodOrder.price ??>${(packetPeriodOrder.price/100)!0}<#else >0</#if> 元&nbsp;&nbsp;&nbsp;&nbsp;
                                    租金劵：<#if packetPeriodOrder.ticketMoney ??>${(packetPeriodOrder.ticketMoney/100)!0}<#else >0</#if> 元&nbsp;&nbsp;&nbsp;&nbsp;
                                </div>
                                <div style="padding-top: 10px;">
                                    <#if packetPeriodOrder.payType == 1>
                                        余额支付&nbsp;&nbsp;&nbsp;&nbsp;
                                    <#else>
                                        支付订单号：${(packetPeriodOrder.payOrderId)!''}&nbsp;&nbsp;&nbsp;&nbsp;
                                    </#if>
                                </div>
                                <div style="padding-top: 10px;">
                                    支付金额：<#if packetPeriodOrder.payOrderMoney ??>${(packetPeriodOrder.payOrderMoney/100)!''}<#else >0</#if> 元&nbsp;&nbsp;&nbsp;&nbsp;
                                    <span style="color: #999999;">支付时间：${(packetPeriodOrder.handleTime?string('yyyy-MM-dd HH:mm:ss'))!''}&nbsp;&nbsp;&nbsp;&nbsp;</span>
                                    备注：${(packetPeriodOrder.memo)!''}
                                </div>
                            </td>
                        </tr>
                    </table>
                <hr>
                </#list>
            <#else>
                <div style="text-align-last:center;color:  #999999;">
                    暂无可退租金
                </div>
            </#if>
                <div style="border-bottom: 20px solid #fff;"></div>
            </div>

            <table cellpadding="0" cellspacing="0">
                <tr>
                    <td width="60px" style="font-weight: 600"><input type="checkbox" class="insuranceCheckbox" >&nbsp;&nbsp;保险</td>
                    <td width="300px" style="color: #f73f38;">注意：使用过的保险建议不退</td>
                    <td></td>
                    <td></td>
                </tr>
            </table>
            <hr>

            <div class="insurance">
            <#if insuranceOrderList ?? && insuranceOrderList?size &gt; 0>
                <#list insuranceOrderList as insuranceOrder>
                    <table cellpadding="0" cellspacing="0">
                        <tr>
                            <td width="45px">
                                <input type="checkbox" class="checkbox" payType="${(insuranceOrder.payType)!''}">
                            </td>
                            <td width="700px">
                                <div style="padding-top: 10px;">
                                    可退保险：
                                    <span class="edit_money">${(insuranceOrder.refundableMoney/100)!0}</span>
                                    <input type="hidden" name="refundableMoney" value="${(insuranceOrder.refundableMoney/100)!0}">
                                    <input type="hidden" name="realRefundMoney" value="${(insuranceOrder.refundableMoney/100)!0}">
                                    <input type="hidden" name="refundRecordId" value="${(insuranceOrder.refundRecordId)!''}">
                                    <input type="hidden" name="sourceType" value="${(insuranceOrder.sourceType)!''}">
                                    <input type="hidden" name="sourceId" value="${(insuranceOrder.id)!''}">
                                    元（${(insuranceOrder.statusName)!''}）
                                    &nbsp;&nbsp;&nbsp;&nbsp;<a class="changeMoney" href="javascript:void(0)">修改金额</a>&nbsp;&nbsp;&nbsp;&nbsp;
                                </div>
                                <div style="padding-top: 10px;color: #999999;">
                                    保额：<#if insuranceOrder.paid ??> ${(insuranceOrder.paid/100)!0}<#else >0</#if>元&nbsp;&nbsp;&nbsp;&nbsp;
                                    保险有效期：${(insuranceOrder.beginTime?string('yyyy-MM-dd HH:mm:ss'))!''}~${(insuranceOrder.endTime?string('yyyy-MM-dd HH:mm:ss'))!''}
                                </div>
                                <div style="padding-top: 10px;">
                                    <#if insuranceOrder.payType == 1>
                                        余额支付&nbsp;&nbsp;&nbsp;&nbsp;
                                    <#else>
                                        支付订单号：${(insuranceOrder.payOrderId)!''}&nbsp;&nbsp;&nbsp;&nbsp;
                                    </#if>
                                </div>
                                <div style="padding-top: 10px;">
                                    支付金额：<#if insuranceOrder.payOrderMoney ??>${(insuranceOrder.payOrderMoney/100)!''}<#else >0</#if> 元&nbsp;&nbsp;&nbsp;&nbsp;
                                    <span style="color: #999999;">支付时间： ${(insuranceOrder.handleTime?string('yyyy-MM-dd HH:mm:ss'))!''}&nbsp;&nbsp;&nbsp;&nbsp;</span>
                                    备注：${(insuranceOrder.memo)!''}
                                </div>
                            </td>
                        </tr>
                    </table>
                <hr>
                </#list>
            <#else>
            <div style="text-align-last:center;color:  #999999;">
                    暂无可退保险
            </div>
            </#if>
                <div style="border-bottom: 20px solid #fff;"></div>
            </div>

            <table cellpadding="0" cellspacing="0">
                <tr>
                    <td width="60px" style="font-weight: 600"><input type="checkbox" class="customerMultiCheckbox" >&nbsp;&nbsp;多通道</td>
                    <td width="300px" style="color: #f73f38;">注意：已结清的多通道订单不在此显示</td>
                    <td></td>
                    <td></td>
                </tr>
            </table>
            <hr>

            <div class="customerMulti">
                <#if customerMultiOrderList ?? && customerMultiOrderList?size &gt; 0>
                    <#list customerMultiOrderList as customerMultiOrder>
                        <table cellpadding="0" cellspacing="0">
                            <tr>
                                <td width="45px">
                                    <input type="checkbox" class="checkbox" payType="15">
                                </td>
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
                        <hr>
                    </#list>
                <#else>
                    <div style="text-align-last:center;color:  #999999;">
                        暂无可退多通道订单
                    </div>
                </#if>
                <div style="border-bottom: 20px solid #fff;"></div>
            </div>

            <table cellspacing="0" cellpadding="0">
                <tr>
                    <td style="padding-left: 20px;">退款方式：</td>
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
            <td align="left" id="total_money" width="200px;" style="font-size: 12px;padding-left: 12px;">
                本次退款金额：<span id="money_detail" style="color: #FF4012;">0.00</span>元
            </td>
            <td width="270px;"></td>
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
                win = $('#' + pid);


        win.find('button.ok').click(function() {
            var checkTotalNum = $('#check_total_num').val();
            if(checkTotalNum == 0) {
                $.messager.alert('提示信息', '请选择订单', 'info');
                return;
            }
            $.messager.confirm('提示信息', $('#total_money').html()+'<br/><br/>是否确认?', function (ok) {
                if (ok) {
                    var orderList = [];
                    $('input[class=checkbox]:checked').each(function () {
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
                    });
                    var values = {};
                    var refundType = $('input[name=refundType]:checked').val();
                    values["customerId"] = ${customerId};
                    values["refundType"] = refundType;
                    values["orderList"] = orderList;

                    $.post('${contextPath}/security/zd/rent_refund/refund_money.htm', {
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

    //单个复选框
    $('.checkbox').change(function () {
        if($(this).attr('checked')=="checked") {
            $(this).parent().parent().parent().css("background", "#4263ff");
        }else{
            $(this).parent().parent().parent().css("background", "#ffffff");
        }
        checkPayType();
    });

    //全选所有押金
    $('.foregiftCheckbox').change(function () {
        if ($(this).attr('checked') == "checked") {
            $('.foregift').find('input[type=checkbox]').attr('checked', true);
        }else{
            $('.foregift').find('input[type=checkbox]').attr('checked', false);
        }
        checkPayType();
    });

    //全选所有租金
    $('.packetCheckbox').change(function () {
        if ($(this).attr('checked') == "checked") {
            $('.packetPeriodPrice').find('input[type=checkbox]').attr('checked', true);
        }else{
            $('.packetPeriodPrice').find('input[type=checkbox]').attr('checked', false);
        }
        checkPayType();
    });

    //全选所有保险
    $('.insuranceCheckbox').change(function () {
        if($(this).attr('checked')=="checked") {
            $('.insurance').find('input[type=checkbox]').attr('checked', true);
        }else {
            $('.insurance').find('input[type=checkbox]').attr('checked', false);
        }
        checkPayType();
    });

    //全选所有多通道
    $('.customerMultiCheckbox').change(function () {
        if($(this).attr('checked')=="checked") {
            $('.customerMulti').find('input[type=checkbox]').attr('checked', true);
        }else {
            $('.customerMulti').find('input[type=checkbox]').attr('checked', false);
        }
        checkPayType();
    });

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
            href: "${contextPath}/security/zd/rent_refund/edit_money.htm?refundableMoney=" + refundableMoney +"&realRefundMoney=" + realRefundMoney ,
            windowData: {
                ok: function (realRefundMoney) {
                    input.val(realRefundMoney);
                    span.html(realRefundMoney);
                    //更新退款金额
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

                    //未选中部分
                    $('input[class=checkbox]:not(:checked)').each(function () {
                        //更新样式
                        $(this).parent().parent().parent().css("background", "#ffffff");
                    });
                }
            },
            event: {
                onClose: function () {
                }
            }
        });
    })

</script>