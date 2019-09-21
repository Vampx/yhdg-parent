<div class="popup_body" style="min-height: 82%;">
    <div class="ui_table">
        <form method="post">
            <input type="hidden" id="check_total_money">
            <input type="hidden" name="refundRecordId" <#if entity.refundRecordId ?? >value="${entity.refundRecordId}" </#if>>
            <input type="hidden" name="sourceType" <#if entity.sourceType ?? >value="${sourceType}" </#if>>
            <input type="hidden" name="sourceId" <#if entity.id ?? >value="${entity.id}" </#if>>
            <input type="hidden" name="totalRefundableMoney" value="${entity.refundableMoney/100}">
            <input type="hidden" name="totalRealRefundMoney" value="${entity.refundableMoney/100}">
            <table cellpadding="0" cellspacing="0">
                <tr>
                    <td width="45px">注意：</td>
                    <td width="400px" style="color: #f73f38;">
                        实际退款金额以平台退款金额为准。赠送余额，将退还到余额账户
                    </td>
                    <td></td>
                </tr>
            </table>


            <div style="border-bottom: 10px solid #f0f0f0;"></div>
            <table cellpadding="0" cellspacing="0">
                <tr>
                    <td width="70px">多通道</td>
                    <td width="300px" align="right" style="color: #f73f38;">注意：已结清的多通道订单不在此显示</td>
                    <td></td>
                    <td></td>
                </tr>
            </table>

            <div class="customerMulti">
                <hr>
                <table cellpadding="0" cellspacing="0">
                    <tr>
                        <td width="400px">
                            <div>
                                可退租金：
                                <span><#if entity.refundableMoney ??>${(entity.refundableMoney/100)!0}<#else >0</#if></span> 元&nbsp;&nbsp;&nbsp;&nbsp;
                                实退租金：
                                <span class="edit_money"><#if entity.refundableMoney ??>${(entity.refundableMoney/100)!0}<#else >0</#if></span> 元&nbsp;&nbsp;&nbsp;&nbsp;
                                <input type="hidden" name="realRefundMoney" value="<#if entity.refundableMoney ??>${(entity.refundableMoney/100)!0}<#else>0</#if>">
                                <input type="hidden" name="refundableMoney" value="<#if entity.refundableMoney ??>${(entity.refundableMoney/100)!0}<#else>0</#if>">
                                <a class="changeMoney" href="javascript:void(0)">修改金额</a>&nbsp;&nbsp;&nbsp;&nbsp;
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
                        <span class="radio_box">
                            <input type="radio" class="radio" name="refundType" id="refund_type_1" checked  value="1"/><label for="refund_type_1">退款到客户余额</label>
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
        var totalMoney = parseInt(Math.round(0));
        var refundMoneys = $('input[name=realRefundMoney');
        for(var i = 0; i < refundMoneys.length; i++) {
            addMoney = parseInt(Math.round(refundMoneys[i].value * 100));
            totalMoney = totalMoney + addMoney;
        }
        $('#total_money').html("本次退款金额："+(totalMoney/100).toFixed(2) +" 元");
        $('#check_total_money').val((totalMoney/100).toFixed(2));


        win.find('button.ok').click(function() {
            $.messager.confirm('提示信息', $('#total_money').html()+'<br/><br/>是否确认?', function (ok) {
                if (ok) {
                    var values = {};
                    var refundType = $('input[name=refundType]:checked').val();
                    var refundMoney = parseInt(Math.round(win.find('input[name=totalRealRefundMoney]').val() * 100));
                    var refundableMoney = parseInt(Math.round(win.find('input[name=totalRefundableMoney]').val() * 100));
                    if(refundMoney == 0) {
                        $.messager.alert('提示信息', '金额为0，不可退款', 'info');
                        return;
                    }else {
                        values["refundRecordId"] = win.find('input[name=refundRecordId]').val();
                        values["sourceType"] = win.find('input[name=sourceType]').val();
                        values["sourceId"] = win.find('input[name=sourceId]').val();
                        values["refundMoney"] = refundMoney;
                        values["refundableMoney"] = refundableMoney;
                        values["customerId"] = ${(entity.customerId)!''};
                        values["refundType"] = refundType;

                        console.log(values);

                        $.post('${contextPath}/security/zc/zc_customer_multi_order/audit_money.htm', {
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
                }
            });
        });

        win.find('button.close').click(function() {
            win.window('close');
        });
    })()

    $('.changeMoney').click(function () {
        var refundableMoney = parseInt(Math.round($(this).parent().parent().parent().find('input[name=refundableMoney]').val() * 100));
        var realRefundMoney = parseInt(Math.round($(this).parent().parent().parent().find('input[name=realRefundMoney]').val() * 100));
        var input = $(this).parent().parent().parent().find('input[name=realRefundMoney]');
        var span =  $(this).parent().parent().parent().find('span[class=edit_money]');
        App.dialog.show({
            css: 'width:242px;height:149px;overflow:visible;',
            title: '修改金额',
            href: "${contextPath}/security/zc/zc_customer_multi_order/edit_money.htm?refundableMoney=" + refundableMoney +"&realRefundMoney=" + realRefundMoney ,
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