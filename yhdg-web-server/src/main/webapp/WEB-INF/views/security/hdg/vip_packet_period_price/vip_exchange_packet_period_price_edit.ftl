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
            <span class="first-edit" exchange_first_price_id="${(exchange.id)!0}" vip_foregift_id = "${(exchange.vipForegiftId)!0}"><img src="${app.imagePath}/edit.png"/></span>
            <span class="first-close" exchange_first_price_id="${(exchange.id)!0}" vip_foregift_id = "${(exchange.vipForegiftId)!0}" style="margin-left: 11px;"><img src="${app.imagePath}/delete.png"></span>
        </div>
    </div>
    </#list>
</#if>
<div class="add-deposit exchange_rent_add_first">添加租金</div>

<script>


    $(function() {

        $(".exchange_rent_add_first").click(function () {

            var batteryType = ${(batteryType)!0};
            var agentId = ${(agentId)!0};
            var foregiftId = ${(foregiftId)!0};
            var vipForegiftId = ${(vipForegiftId)!0};
            var priceId = ${(priceId)!0};

            if(batteryType == undefined) {
                $.messager.alert('提示信息', '请选择电池类型', 'info');
                return;
            }

            if(foregiftId == undefined) {
                $.messager.alert('提示信息', '请添加押金', 'info');
                return;
            }
            if (vipForegiftId == 0) {
                $.messager.alert('提示信息', '请添加减免金额', 'info');
                return;
            } else {
                App.dialog.show({
                    css: 'width:800px;height:600px;',
                    title: '添加租金',
                    href: "${contextPath}/security/hdg/vip_packet_period_price/vip_exchange_packet_period_price_add_first.htm?batteryType="+ batteryType+ "&agentId="+ agentId + "&foregiftId=" + foregiftId+ "&vipForegiftId=" +vipForegiftId+ "&priceId="+ priceId,
                    windowData:{
                        ok: function (config) {
                            if (config) {
                                $(".deposit-list").find(".active").find(".edit_foregift").attr("vip_exchange_id",config.vipExchangeId);
                                if (config.vipPacketId) {
                                    // add_vip_packet_period_price_table(config.vipPacketId);
                                }
                                return true;
                            }
                        }
                    },
                    event: {
                        onClose: function() {
                            //利用押金id去查找租金
                            $.post('${contextPath}/security/hdg/vip_packet_period_price/vip_exchange_packet_period_price.htm', {
                                foregiftId: foregiftId,
                                batteryType: batteryType,
                                agentId: agentId,
                                vipForegiftId: vipForegiftId
                            }, function (html) {
                                $("#exchange_packet_period_price").html(html);
                            }, 'html');

                        }
                    }
                });
            }

        });

        $(".exchange_first .first-edit").click(function () {
            if (${(vipForegiftId)!0} == 0) {
                $.messager.alert('提示信息', '请添加减免金额', 'info');
                return;
            }
            var exchangeId = $(this).attr("exchange_first_price_id");

            var batteryType = $(this).parents().find("#agent_battery_type_form").find('input[name=batteryType]').val();
            var agentId = $(this).parents().find("#agent_battery_type_form").find('input[name=agentId]').val();
            var foregiftId = $(this).parents().find(".vip_battery_foregift_list").find(".active").find(".edit_foregift").attr("foregift_id");

            App.dialog.show({
                css: 'width:800px;height:600px;',
                title: '修改租金',
                href: "${contextPath}/security/hdg/vip_packet_period_price/vip_exchange_packet_period_first_price_edit.htm?exchangeId=" + exchangeId,
                windowData:{
                    ok: function (config) {
                        //利用押金id去查找租金
                        $.post('${contextPath}/security/hdg/vip_packet_period_price/vip_exchange_packet_period_price.htm', {
                            foregiftId: foregiftId,
                            batteryType: batteryType,
                            agentId: agentId,
                            vipForegiftId: config.vipForegiftId
                        }, function (html) {
                            $("#exchange_packet_period_price").html(html);
                        }, 'html');
                    }
                },
                event: {
                    onClose: function() {
                    }
                }
            });
        });

        $(".exchange_first .first-close").click(function () {
            var exchangeId = $(this).attr("exchange_first_price_id");

            var batteryType = $(this).parents().find("#agent_battery_type_form").find('input[name=batteryType]').val();
            var agentId = $(this).parents().find("#agent_battery_type_form").find('input[name=agentId]').val();
            var foregiftId = $(this).parents().find(".vip_battery_foregift_list").find(".active").find(".edit_foregift").attr("foregift_id");

            var vipForegiftId = $(this).attr("vip_foregift_id");

            $.messager.confirm('提示信息', '确认删除?', function (ok) {
                if (ok) {
                    $.post("${contextPath}/security/hdg/vip_packet_period_price/vip_exchange_packet_period_first_price_delete.htm?exchangeId=" + exchangeId, function (json) {
                        if (json.success) {
                            $.messager.alert('提示消息', '操作成功', 'info');
                            //利用押金id去查找租金
                            $.post('${contextPath}/security/hdg/vip_packet_period_price/vip_exchange_packet_period_price.htm', {
                                foregiftId: foregiftId,
                                batteryType: batteryType,
                                agentId: agentId,
                                vipForegiftId: vipForegiftId
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

    function add_vip_packet_period_price_table(vipPacketId){
        var vipPacketPeriodPriceId = $("#vip_packet_period_price_table");
        var length = vipPacketPeriodPriceId.find("tr").length;
        var html =
                '  <tr>\n'+
                '    <input type="hidden" name="vipPacketId" value="'+vipPacketId+'">\n'+
                '  </tr>';
        vipPacketPeriodPriceId.find('tr').eq(length-1).before(html);
    }
</script>