<td colspan="2">
    <div class="zj_list">
    <#if exchangeBatteryForegiftList?? && (exchangeBatteryForegiftList?size>0) >
        <#list exchangeBatteryForegiftList as exchangeBatteryForegift>
            <div <#if exchangeBatteryForegift_index == 0>class="zj_item battery_foregift selected"<#else>class="zj_item battery_foregift"</#if> battery_foregift_id="${(exchangeBatteryForegift.id)!0}">
                <span class="icon">
                    <i class="fa fa-fw fa-edit" battery_foregift_id="${(exchangeBatteryForegift.id)!0}"></i>
                    <i class="fa fa-fw fa-close" battery_foregift_id="${(exchangeBatteryForegift.id)!0}"></i>
                </span>
                <p class="num">金额:<#if exchangeBatteryForegift.money??>${(exchangeBatteryForegift.money)/100}<#else>
                    0</#if> 元</p>
            </div>
        </#list>
    </#if>
        <div class="zj_add battery_foregift_add">
            <p class="icon"><i class="fa fa-fw fa-plus"></i></p>
            <p class="text">添加押金</p>
        </div>
    </div>
</td>

<script>
    $(function() {

        $(".battery_foregift_add").click(function () {
            App.dialog.show({
                css: 'width:450px;height:365px;overflow:visible;',
                title: '添加押金',
                href: "${contextPath}/security/hdg/exchange_battery_foregift/exchange_battery_foregift_add.htm?batteryType="+ ${(entity.batteryType)!''}+ "&agentId="+ ${(entity.agentId)!''},
                windowData:{
                },
                event: {
                    onClose: function () {
                        $.post('${contextPath}/security/hdg/exchange_battery_foregift/find_foregift_list.htm', {
                            batteryType: ${(entity.batteryType)!''},
                            agentId: ${(entity.agentId)!''}
                        }, function (json) {
                            if (json.success) {
                                var firstForegift = json.data;
                                var foregiftId = firstForegift.id;
                                //利用第一个押金的id去查询租金信息
                                $.post('${contextPath}/security/hdg/packet_period_price/exchange_packet_period_price.htm', {
                                    foregiftId: foregiftId,
                                    batteryType: ${(entity.batteryType)!''},
                                    agentId: ${(entity.agentId)!''}
                                }, function (html) {
                                    $("#exchange_packet_period_price").html(html);
                                }, 'html');
                            } else {
                                //无押金，显示租金为空
                                $.post('${contextPath}/security/hdg/packet_period_price/exchange_packet_period_price.htm', {
                                    foregiftId: 0,
                                    batteryType: ${(entity.batteryType)!''},
                                    agentId: ${(entity.agentId)!''}
                                }, function (html) {
                                    $("#exchange_packet_period_price").html(html);
                                }, 'html');
                            }
                        }, 'json');
                    }
                }
            });
        });

        $(".battery_foregift .fa-edit").click(function () {
            var batteryForegiftId = $(this).attr("battery_foregift_id");
            App.dialog.show({
                css: 'width:450px;height:365px;overflow:visible;',
                title: '修改押金',
                href: "${contextPath}/security/hdg/exchange_battery_foregift/exchange_battery_foregift_edit.htm?batteryForegiftId=" + batteryForegiftId,
                windowData:{
                },
                event: {
                    onClose: function () {
                        $.post('${contextPath}/security/hdg/exchange_battery_foregift/find_foregift_list.htm', {
                            batteryType: ${(entity.batteryType)!''},
                            agentId: ${(entity.agentId)!''}
                        }, function (json) {
                            if (json.success) {
                                var firstForegift = json.data;
                                var foregiftId = firstForegift.id;
                                //利用第一个押金的id去查询租金信息
                                $.post('${contextPath}/security/hdg/packet_period_price/exchange_packet_period_price.htm', {
                                    foregiftId: foregiftId,
                                    batteryType: ${(entity.batteryType)!''},
                                    agentId: ${(entity.agentId)!''}
                                }, function (html) {
                                    $("#exchange_packet_period_price").html(html);
                                }, 'html');
                            } else {
                                //无押金，显示租金为空
                                $.post('${contextPath}/security/hdg/packet_period_price/exchange_packet_period_price.htm', {
                                    foregiftId: 0,
                                    batteryType: ${(entity.batteryType)!''},
                                    agentId: ${(entity.agentId)!''}
                                }, function (html) {
                                    $("#exchange_packet_period_price").html(html);
                                }, 'html');
                            }
                        }, 'json');
                    }
                }
            });
        });

        $(".battery_foregift .fa-close").click(function () {
            var batteryForegiftId = $(this).attr("battery_foregift_id");
            $.messager.confirm('提示信息', '删除押金设置将删除对应的分期设置和租金设置，是否删除?', function (ok) {
                if (ok) {
                    //重新加载押金信息
                    $.post("${contextPath}/security/hdg/exchange_battery_foregift/exchange_battery_foregift_delete.htm?batteryForegiftId=" + batteryForegiftId, function (json) {
                        if (json.success) {
                            $.messager.alert('提示消息', '操作成功', 'info');
                            $.post('${contextPath}/security/hdg/exchange_battery_foregift/exchange_battery_foregift.htm', {
                                batteryType: ${(entity.batteryType)!''},
                                agentId: ${(entity.agentId)!''}
                            }, function (html) {
                                $("#exchange_battery_foregift").html(html);
                            }, 'html');

                            //重新加载租金信息,获取新的押金集合，得到第一个押金
                            $.post('${contextPath}/security/hdg/exchange_battery_foregift/find_foregift_list.htm', {
                                batteryType: ${(entity.batteryType)!''},
                                agentId: ${(entity.agentId)!''}
                            }, function (json) {
                                if(json.success) {
                                    var firstForegift = json.data;
                                    var foregiftId = firstForegift.id;
                                    //利用第一个押金的id去查询租金信息
                                    $.post('${contextPath}/security/hdg/packet_period_price/exchange_packet_period_price.htm', {
                                        foregiftId: foregiftId,
                                        batteryType: ${(entity.batteryType)!''},
                                        agentId: ${(entity.agentId)!''}
                                    }, function (html) {
                                        $("#exchange_packet_period_price").html(html);
                                    }, 'html');
                                }else {
                                    //无押金，显示租金为空
                                    $.post('${contextPath}/security/hdg/packet_period_price/exchange_packet_period_price.htm', {
                                        foregiftId: 0,
                                        batteryType: ${(entity.batteryType)!''},
                                        agentId: ${(entity.agentId)!''}
                                    }, function (html) {
                                        $("#exchange_packet_period_price").html(html);
                                    }, 'html');
                                }
                            }, 'json');
                        } else {
                            $.messager.alert('提示消息', json.message, 'info');
                        }
                    }, 'json');
                }
            });
        });
    });

    //随点击切换 包时段套餐
    $('.battery_foregift').click(function () {
        $(this).addClass("selected").siblings().removeClass("selected");
        //获取押金id
        var batteryForegiftId = $(this).attr("battery_foregift_id");
        //利用押金id去查找租金
        $.post('${contextPath}/security/hdg/packet_period_price/exchange_packet_period_price.htm', {
            foregiftId: batteryForegiftId,
            batteryType: ${(entity.batteryType)!''},
            agentId: ${(entity.agentId)!''}
        }, function (html) {
            $("#exchange_packet_period_price").html(html);
        }, 'html');
    })



</script>