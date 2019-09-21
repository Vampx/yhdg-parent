<td colspan="2">
    <div class="zj_list">
    <#if packetPeriodPriceList?? && (packetPeriodPriceList?size>0) >
        <#list packetPeriodPriceList as exchange>
            <div class="zj_item exchange_rent">
                <span class="icon">
                    <i class="fa fa-fw fa-edit"  exchange_rent_id="${(exchange.id)!''}"></i>
                    <i class="fa fa-fw fa-close" exchange_rent_id="${(exchange.id)!''}"></i>
                </span>
                <p class="num">价格:<#if exchange.price??>${(exchange.price)/100}<#else>0</#if> 元</p>
                <p class="text">时长:${(exchange.dayCount)!0} 天</p>
            </div>
        </#list>
    </#if>
        <div class="zj_add exchange_rent_add">
            <p class="icon"><i class="fa fa-fw fa-plus"></i></p>
            <p class="text">添加租金</p>
        </div>
    </div>
</td>


<script>
    $(function() {
        $(".exchange_rent_add").click(function () {
            var foregiftId = $(".zj_list").find(".selected").attr("battery_foregift_id");
            if (foregiftId == null) {
                $.messager.alert('提示信息', '请选择押金', 'info');
                return;
            }
            App.dialog.show({
                css: 'width:500px;height:470px;overflow:visible;',
                title: '添加租金',
                href: "${contextPath}/security/hdg/vip_packet_period_price/vip_exchange_packet_period_price_add.htm?batteryType="+ ${(batteryType)!''}+ "&agentId="+ ${(agentId)!''} + "&foregiftId=" + foregiftId + "&priceId=" + ${(priceId)!0} ,
                windowData:{
                },
                event: {
                    onClose: function() {
                        $.post('${contextPath}/security/hdg/vip_packet_period_price/vip_exchange_packet_period_price.htm', {
                            foregiftId: foregiftId,
                            batteryType: ${(batteryType)!0},
                            agentId: ${(agentId)!0},
                            priceId: ${(priceId)!0}
                        }, function (html) {
                            $("#exchange_packet_period_price").html(html);
                        }, 'html');
                    }
                }
            });

        });

        $(".exchange_rent .fa-edit").click(function () {
            var exchangeId = $(this).attr("exchange_rent_id");
            App.dialog.show({
                css: 'width:500px;height:470px;overflow:visible;',
                title: '修改租金',
                href: "${contextPath}/security/hdg/vip_packet_period_price/vip_exchange_packet_period_price_edit.htm?exchangeId=" + exchangeId,
                windowData:{
                },
                event: {
                    onClose: function() {
                        $.post('${contextPath}/security/hdg/vip_packet_period_price/vip_exchange_packet_period_price.htm', {
                            foregiftId: ${(foregiftId)!0},
                            batteryType: ${(batteryType)!0},
                            agentId: ${(agentId)!0},
                            priceId: ${(priceId)!0}
                        }, function (html) {
                            $("#exchange_packet_period_price").html(html);
                        }, 'html');
                    }
                }
            });
        });

        $(".exchange_rent .fa-close").click(function () {
            var exchangeId = $(this).attr("exchange_rent_id");
            $.messager.confirm('提示信息', '确认删除电池租金?', function (ok) {
                if (ok) {
                    $.post("${contextPath}/security/hdg/vip_packet_period_price/vip_exchange_packet_period_price_delete.htm?exchangeId=" + exchangeId, function (json) {
                        if (json.success) {
                            $.messager.alert('提示消息', '操作成功', 'info');
                            $.post('${contextPath}/security/hdg/vip_packet_period_price/vip_exchange_packet_period_price.htm', {
                                foregiftId: ${(foregiftId)!0},
                                batteryType: ${(batteryType)!0},
                                agentId: ${(agentId)!0},
                                priceId: ${(priceId)!0}
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