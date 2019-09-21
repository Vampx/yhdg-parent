<#if packetPeriodPriceList?? && (packetPeriodPriceList?size>0) >
    <#list packetPeriodPriceList as exchange>
    <div class="setbox">
        <div class="set-1">
            <span class="font1 weight">首次</span>
            <span class="font1 margin-t-9">
                <b>租金：<#if exchange.price??>${(exchange.price/100)?string('0.00')}<#else>0</#if>元/${(exchange.dayCount)!0}天</b>
                <b style="margin-left: 18px;">备注：${(exchange.memo)!''}</b>
            </span>
        </div>
        <#if exchange.renewList?? && (exchange.renewList?size>0) >
            <#list exchange.renewList as renew>
                <div class="set-2">
                    <span class="font1 weight">续租</span>
                    <span class="font1 margin-t-9">
                        资费：${(renew.price/100)?string('0.00')}元/${(renew.dayCount)!0} 天
                        <#if renew.isTicket?? && renew.isTicket == 1>可以使用优惠券<#else>不可使用优惠券</#if>
                    </span>
                    <span class="font1 margin-t-9">
                        限制总次数：${(renew.limitCount)!''}
                        限制每日换电次数：${(renew.dayLimitCount)!''}
                    </span>
                    <span class="font1 margin-t-9">
                        备注：${(renew.memo)!''}
                    </span>
                </div>
            </#list>
        </#if>
        <div class="set-3 exchange_first">
            <span class="first-edit" exchange_first_price_id="${(exchange.id)!0}"><img src="${app.imagePath}/edit.png"/></span>
            <span class="first-close" exchange_first_price_id="${(exchange.id)!0}" style="margin-left: 11px;"><img src="${app.imagePath}/delete.png"></span>
        </div>
    </div>
    </#list>
</#if>
<div class="add-deposit exchange_rent_add_first">添加租金</div>

<script>

    var batteryType = $("#agent_battery_type_form").find('input[name=batteryType]').val();
    var agentId = $("#agent_battery_type_form").find('input[name=agentId]').val();
    var foregiftId = $(".deposit-list").find(".active").attr("battery_foregift_id");

    $(function() {

        $(".exchange_rent_add_first").click(function () {

            if(batteryType == '') {
                $.messager.alert('提示信息', '请选择电池类型', 'info');
                return;
            }

            if(foregiftId == undefined) {
                $.messager.alert('提示信息', '请添加押金', 'info');
                return;
            }

            App.dialog.show({
                css: 'width:800px;height:600px;',
                title: '添加租金',
                href: "${contextPath}/security/basic/agent_battery_type/exchange_packet_period_price_add_first.htm?batteryType="+ batteryType+ "&agentId="+ agentId + "&foregiftId=" + foregiftId+ "&priceId=" + 0,
                windowData:{
                },
                event: {
                    onClose: function() {
                        $.post('${contextPath}/security/basic/agent_battery_type/exchange_packet_period_price.htm', {
                            foregiftId: foregiftId,
                            batteryType: batteryType,
                            agentId: agentId
                        }, function (html) {
                            $("#exchange_packet_period_price").html(html);
                        }, 'html');
                    }
                }
            });

        });

        $(".exchange_first .first-edit").click(function () {
           var exchangeId = $(this).attr("exchange_first_price_id");
            App.dialog.show({
                css: 'width:800px;height:600px;',
                title: '修改租金',
                href: "${contextPath}/security/basic/agent_battery_type/exchange_packet_period_first_price_edit.htm?exchangeId=" + exchangeId,
                windowData:{
                },
                event: {
                    onClose: function() {
                        $.post('${contextPath}/security/basic/agent_battery_type/exchange_packet_period_price.htm', {
                            foregiftId: foregiftId,
                            batteryType: batteryType,
                            agentId: agentId
                        }, function (html) {
                            $("#exchange_packet_period_price").html(html);
                        }, 'html');
                    }
                }
            });
        });

        $(".exchange_first .first-close").click(function () {
            var exchangeId = $(this).attr("exchange_first_price_id");
            $.messager.confirm('提示信息', '确认删除?', function (ok) {
                if (ok) {
                    $.post("${contextPath}/security/basic/agent_battery_type/exchange_packet_period_first_price_delete.htm?exchangeId=" + exchangeId, function (json) {
                        if (json.success) {
                            $.messager.alert('提示消息', '操作成功', 'info');
                            $.post('${contextPath}/security/basic/agent_battery_type/exchange_packet_period_price.htm', {
                                foregiftId: foregiftId,
                                batteryType: batteryType,
                                agentId: agentId
                            }, function (html) {
                                $("#exchange_packet_period_price").html(html);
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