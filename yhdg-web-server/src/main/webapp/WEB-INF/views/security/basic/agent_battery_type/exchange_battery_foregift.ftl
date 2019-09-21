<div class="deposit">
    <div class="deposit-list battery_foregift_create">
    <#if exchangeBatteryForegiftList?? && (exchangeBatteryForegiftList?size>0) >
        <#list exchangeBatteryForegiftList as exchangeBatteryForegift>
            <div <#if exchangeBatteryForegift_index == 0 >class="active battery_foregift"<#else>class="battery_foregift"</#if>
                 battery_foregift_id="${(exchangeBatteryForegift.id)!0}">
                <p>金额:<#if exchangeBatteryForegift.money??>${(exchangeBatteryForegift.money)/100}<#else>0</#if> 元</p>
                <div >
                    <img class="edit_foregift" battery_foregift_id="${(exchangeBatteryForegift.id)!0}"
                         src="${app.imagePath}/edit2.png" />
                    <img class="close_foregift"  battery_foregift_id="${(exchangeBatteryForegift.id)!0}"
                         src="${app.imagePath}/delete2.png" />
                </div>
            </div>
        </#list>
    </#if>
    </div>
    <div class="add-deposit battery_foregift_add">添加押金</div>
</div>
<div class="deposit-tips" id="exchange_battery_foregift_memo">
<#include 'exchange_battery_foregift_memo.ftl'>
</div>
<script>

    var batteryType = $("#agent_battery_type_form").find('input[name=batteryType]').val();
    var agentId = $("#agent_battery_type_form").find('input[name=agentId]').val();

    $(function() {

        var foregiftId = $(".deposit-list").find(".active").attr("battery_foregift_id");
        if (foregiftId != '' && foregiftId != undefined) {
            //利用押金id去查找租金备注
            $.post('${contextPath}/security/basic/agent_battery_type/exchange_battery_foregift_memo.htm', {
                batteryForegiftId: foregiftId
            }, function (html) {
                $("#exchange_battery_foregift_memo").html(html);
            }, 'html');
        }

        if (foregiftId != '' && batteryType != '' && agentId != '') {
            //租金
            $.post('${contextPath}/security/basic/agent_battery_type/exchange_packet_period_price.htm', {
                foregiftId: foregiftId,
                batteryType: batteryType,
                agentId: agentId
            }, function (html) {
                $("#exchange_packet_period_price").html(html);
            }, 'html');
        }

        $(".battery_foregift_add").click(function () {
            if(batteryType == '') {
                $.messager.alert('提示信息', '请选择电池类型', 'info');
                return;
            }

            App.dialog.show({
                css: 'width:450px;height:295px;overflow:visible;',
                title: '添加押金',
                href: "${contextPath}/security/basic/agent_battery_type/exchange_battery_foregift_add.htm?batteryType="+ batteryType+ "&agentId="+ agentId,
                windowData:{
                },
                event: {
                    onClose: function () {
                        $.post('${contextPath}/security/basic/agent_battery_type/find_foregift_list.htm', {
                            batteryType: batteryType,
                            agentId: agentId
                        }, function (json) {
                            if (json.success) {
                                var firstForegift = json.data;
                                var foregiftId = firstForegift.id;
                                //利用第一个押金的id去查询租金信息
                                $.post('${contextPath}/security/basic/agent_battery_type/exchange_packet_period_price.htm', {
                                    foregiftId: foregiftId,
                                    batteryType: firstForegift.batteryType,
                                    agentId: firstForegift.agentId
                                }, function (html) {
                                    $("#exchange_packet_period_price").html(html);
                                }, 'html');

                                //利用押金id去查找租金备注
                                $.post('${contextPath}/security/basic/agent_battery_type/exchange_battery_foregift_memo.htm', {
                                    batteryForegiftId: foregiftId
                                }, function (html) {
                                    $("#exchange_battery_foregift_memo").html(html);
                                }, 'html');

                            } else {
                                //无押金，显示租金为空
                                $.post('${contextPath}/security/basic/agent_battery_type/exchange_packet_period_price.htm', {
                                    foregiftId: 0,
                                    batteryType: batteryType,
                                    agentId: agentId
                                }, function (html) {
                                    $("#exchange_packet_period_price").html(html);
                                }, 'html');
                            }
                        }, 'json');
                    }
                }
            });


        });

        $(".battery_foregift .edit_foregift").click(function () {
            var batteryForegiftId = $(this).attr("battery_foregift_id");
            App.dialog.show({
                css: 'width:450px;height:295px;overflow:visible;',
                title: '修改押金',
                href: "${contextPath}/security/basic/agent_battery_type/exchange_battery_foregift_edit.htm?batteryForegiftId=" + batteryForegiftId,
                windowData:{
                },
                event: {
                    onClose: function () {
                        $.post('${contextPath}/security/basic/agent_battery_type/find_foregift_list.htm', {
                            batteryType: batteryType,
                            agentId: agentId
                        }, function (json) {
                            if (json.success) {
                                var firstForegift = json.data;
                                var foregiftId = firstForegift.id;
                                //利用第一个押金的id去查询租金信息
                                $.post('${contextPath}/security/basic/agent_battery_type/exchange_packet_period_price.htm', {
                                    foregiftId: foregiftId,
                                    batteryType: batteryType,
                                    agentId: agentId
                                }, function (html) {
                                    $("#exchange_packet_period_price").html(html);
                                }, 'html');
                            } else {
                                //无押金，显示租金为空
                                $.post('${contextPath}/security/basic/agent_battery_type/exchange_packet_period_price.htm', {
                                    foregiftId: 0,
                                    batteryType: batteryType,
                                    agentId: agentId
                                }, function (html) {
                                    $("#exchange_packet_period_price").html(html);
                                }, 'html');
                            }
                        }, 'json');
                    }
                }
            });
        });

        $(".battery_foregift .close_foregift").click(function () {
            var batteryForegiftId = $(this).attr("battery_foregift_id");
            $.messager.confirm('提示信息', '确认删除?', function (ok) {
                if (ok) {
                    //重新加载押金信息
                    $.post("${contextPath}/security/basic/agent_battery_type/exchange_battery_foregift_delete.htm?batteryForegiftId=" + batteryForegiftId, function (json) {
                        if (json.success) {
                            $.messager.alert('提示消息', '操作成功', 'info');
                            $.post('${contextPath}/security/basic/agent_battery_type/exchange_battery_foregift.htm', {
                                batteryType: batteryType,
                                agentId: agentId
                            }, function (html) {
                                $("#exchange_battery_foregift").html(html);
                            }, 'html');

                            //重新加载租金信息,获取新的押金集合，得到第一个押金
                            $.post('${contextPath}/security/basic/agent_battery_type/find_foregift_list.htm', {
                                batteryType: ${(batteryType)!0},
                                agentId: ${(agentId)!0}
                            }, function (json) {
                                if(json.success) {
                                    var firstForegift = json.data;
                                    var foregiftId = firstForegift.id;
                                    //利用第一个押金的id去查询租金信息
                                    $.post('${contextPath}/security/basic/agent_battery_type/exchange_packet_period_price.htm', {
                                        foregiftId: foregiftId,
                                        batteryType: firstForegift.batteryType,
                                        agentId: firstForegift.agentId
                                    }, function (html) {
                                        $("#exchange_packet_period_price").html(html);
                                    }, 'html');
                                }else {
                                    //无押金，显示租金为空
                                    $.post('${contextPath}/security/basic/agent_battery_type/exchange_packet_period_price.htm', {
                                        foregiftId: 0,
                                        batteryType: batteryType,
                                        agentId: agentId
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
        $(this).addClass("active").siblings().removeClass("active");
        //获取押金id
        var batteryForegiftId = $(this).attr("battery_foregift_id");

        //利用押金id去查找租金备注
        $.post('${contextPath}/security/basic/agent_battery_type/exchange_battery_foregift_memo.htm', {
            batteryForegiftId: batteryForegiftId
        }, function (html) {
            $("#exchange_battery_foregift_memo").html(html);
        }, 'html');

        //利用押金id去查找租金
        $.post('${contextPath}/security/basic/agent_battery_type/exchange_packet_period_price.htm', {
            foregiftId: batteryForegiftId,
            batteryType: batteryType,
            agentId: agentId
        }, function (html) {
            $("#exchange_packet_period_price").html(html);
        }, 'html');

    })



</script>