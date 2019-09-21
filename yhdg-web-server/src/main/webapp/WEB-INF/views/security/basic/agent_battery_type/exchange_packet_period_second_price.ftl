<#if packetPeriodPriceSecondList?? && (packetPeriodPriceSecondList?size>0) >
    <#list packetPeriodPriceSecondList as exchangeSecond>
    <div style="display: flex">
        <fieldset style="flex:1">
            <div class="popup_body" style="position: relative;">
                <div class="tab_item" style="display:block; ">
                    <div class="ui_table">
                        <table cellpadding="0" cellspacing="0">
                            <tbody class="table_list">
                            <tr>
                                <td>
                                    <p style="height:18px;">
                                        资费：${(exchangeSecond.price/100)?string('0.00')} 元/${(exchangeSecond.dayCount)!0} 天  <#if exchangeSecond.isTicket?? && exchangeSecond.isTicket == 1>可以使用优惠券<#else>不可使用优惠券</#if>  限制总次数：${(exchangeSecond.limitCount)!''}   限制每日换电次数：${(exchangeSecond.dayLimitCount)!''}
                                    </p>
                                    <p style="height:18px;">
                                        备注：${(exchangeSecond.memo)!''}
                                    </p>
                                </td>
                            </tr>
                            </tbody>
                        </table>
                    </div>
                </div>
                <div class="zj_item exchange_second" style="position: absolute;right: 0px;top: 8px;background: none">
                    <span class="icon">
                        <i class="fa fa-fw fa-edit" exchange_second_id="${(exchangeSecond.id)!0}" exchange_second_price_id="${(exchangeSecond.priceId)!0}"></i>
                        <i class="fa fa-fw fa-close" exchange_second_id="${(exchangeSecond.id)!0}" exchange_second_price_id="${(exchangeSecond.priceId)!0}"></i>
                    </span>
                </div>
            </div>
        </fieldset>
    </div>
    </#list>
</#if>
<div class="zj_add exchange_rent_add_second">
    <p class="icon"><i class="fa fa-fw fa-plus"></i></p>
    <p class="text">添加租金</p>
</div>

<script>

    $(function() {

        $(".exchange_rent_add_second").click(function () {

            var price = $('#price').numberspinner('getValue');
            if (price == '') {
                $.messager.alert('提示信息', "首次支付价格不能为空", 'info');
                return false;
            }
            var dayCount = $('#day_count').numberspinner('getValue');
            if (dayCount == '') {
                $.messager.alert('提示信息', "首次支付租金天数不能为空", 'info');
                return false;
            }
            var limitCount = $('#form_first').find('input[name=limitCount]').val();
            var dayLimitCount = $('#form_first').find('input[name=dayLimitCount]').val();
            var isTicket = $('input[name="isTicket"]:checked').val();
            var memo = $('#form_first').find('textarea[name=memo]').val();

            var idList = [];
            <#if packetPeriodPriceSecondList??>
                <#list packetPeriodPriceSecondList as packetPeriodPriceSecond>
                    idList.push(${packetPeriodPriceSecond.priceId});
                </#list>
            </#if>
            var id = 0;
            if (idList.length > 0) {
                id = idList[0];
            } else {
                id = ${(priceId)!0};
            }

            $.post('${contextPath}/security/basic/agent_battery_type/exchange_packet_period_price_create.htm', {
                batteryType: ${(batteryType)!0},
                agentId: ${(agentId)!0},
                foregiftId: ${(foregiftId)!0},
                price: parseInt(Math.round(price * 100)),
                dayCount: dayCount,
                limitCount: limitCount,
                dayLimitCount: dayLimitCount,
                isTicket: isTicket,
                memo: memo,
                id: id
            }, function (json) {
                if(json.success) {
                    var packetPeriodPrice = json.data;
                    var priceId = packetPeriodPrice.id;
                    App.dialog.show({
                        css: 'width:500px;height:425px;overflow:visible;',
                        title: '添加租金',
                        href: "${contextPath}/security/basic/agent_battery_type/exchange_packet_period_price_add_second.htm?batteryType="+ ${(batteryType)!0}+ "&agentId="+ ${(agentId)!0} + "&foregiftId=" + ${(foregiftId)!0} + "&priceId=" + priceId,
                        windowData:{
                        },
                        event: {
                            onClose: function() {
                                $.post('${contextPath}/security/basic/agent_battery_type/exchange_packet_period_second_price.htm', {
                                    priceId: priceId,
                                    agentId: ${(agentId)!0},
                                    batteryType: ${(batteryType)!0},
                                    foregiftId: ${(foregiftId)!0}
                                }, function (html) {
                                    $("#exchange_packet_period_second_price").html(html);
                                }, 'html');
                                $('#form_first').find('input[name=id]').val(priceId);
                            }
                        }
                    });
                } else {
                    $.messager.alert('提示信息', '请先创建押金', 'info');
                }
            },'json');

        });

        $(".exchange_second .fa-edit").click(function () {
            var exchangeId = $(this).attr("exchange_second_id");
            var secondPriceId = $(this).attr("exchange_second_price_id");

            App.dialog.show({
                css: 'width:500px;height:425px;overflow:visible;',
                title: '修改租金',
                href: "${contextPath}/security/basic/agent_battery_type/exchange_packet_period_second_price_edit.htm?exchangeId=" + exchangeId,
                windowData:{
                },
                event: {
                    onClose: function() {
                        $.post('${contextPath}/security/basic/agent_battery_type/exchange_packet_period_second_price.htm', {
                            priceId: secondPriceId,
                            agentId: ${(agentId)!0},
                            batteryType: ${(batteryType)!0},
                            foregiftId: ${(foregiftId)!0}
                        }, function (html) {
                            $("#exchange_packet_period_second_price").html(html);
                        }, 'html');
                    }
                }
            });
        });

        $(".exchange_second .fa-close").click(function () {
            var exchangeId = $(this).attr("exchange_second_id");
            var secondPriceId = $(this).attr("exchange_second_price_id");

            $.messager.confirm('提示信息', '确认删除?', function (ok) {
                if (ok) {
                    $.post("${contextPath}/security/basic/agent_battery_type/exchange_packet_period_second_price_delete.htm?exchangeId=" + exchangeId, function (json) {
                        if (json.success) {
                            $.messager.alert('提示消息', '操作成功', 'info');
                            $.post('${contextPath}/security/basic/agent_battery_type/exchange_packet_period_second_price.htm', {
                                priceId: secondPriceId,
                                agentId: ${(agentId)!0},
                                batteryType: ${(batteryType)!0},
                                foregiftId: ${(foregiftId)!0}
                            }, function (html) {
                                $("#exchange_packet_period_second_price").html(html);
                            }, 'html');
                        } else {
                            $.messager.alert('提示消息', json.message, 'info');
                        }
                    }, 'json');
                }
            });
        });
    });
</script>