<td colspan="2">
    <div class="zj_list">
    <#if rentPeriodPriceList?? && (rentPeriodPriceList?size>0) >
        <#list rentPeriodPriceList as rentPeriodPrice>
            <div class="zj_item exchange_rent">
                <span class="icon">
                    <i class="fa fa-fw fa-edit"  exchange_rent_id="${(rentPeriodPrice.id)!''}"></i>
                    <i class="fa fa-fw fa-close" exchange_rent_id="${(rentPeriodPrice.id)!''}"></i>
                </span>
                <p class="num">价格:<#if rentPeriodPrice.price??>${(rentPeriodPrice.price)/100}<#else>0</#if> 元</p>
                <p class="text">时长:${(rentPeriodPrice.dayCount)!0} 天</p>
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
            $.post('${contextPath}/security/zd/rent_battery_foregift/find_foregift_list.htm', {
                batteryType: ${(entity.batteryType)!''},
                agentId: ${(entity.agentId)!''}
            }, function (json) {
                if(json.success) {
                    var foregift = json.data;
                    var foregiftId = ${(foregiftId)!0};
                    if(foregiftId == 0) {
                        foregiftId = foregift.id;
                    }
                    App.dialog.show({
                        css: 'width:500px;height:410px;overflow:visible;',
                        title: '添加租金',
                        href: "${contextPath}/security/zd/rent_period_price/add.htm?batteryType="+ ${(entity.batteryType)!''}+ "&agentId="+ ${(entity.agentId)!''} + "&foregiftId=" + foregiftId,
                        windowData:{
                        },
                        event: {
                            onClose: function() {
                                $.post('${contextPath}/security/zd/rent_period_price/rent_period_price.htm', {
                                    foregiftId: foregiftId,
                                    batteryType: ${(entity.batteryType)!''},
                                    agentId: ${(entity.agentId)!''}
                                }, function (html) {
                                    $("#exchange_packet_period_price").html(html);
                                }, 'html');
                            }
                        }
                    });
                }else {
                    $.messager.alert('提示信息', '请先创建押金', 'info');
                }
            },'json');

        });

        $(".exchange_rent .fa-edit").click(function () {
           var id = $(this).attr("exchange_rent_id");
            App.dialog.show({
                css: 'width:500px;height:410px;overflow:visible;',
                title: '修改租金',
                href: "${contextPath}/security/zd/rent_period_price/edit.htm?id=" + id,
                windowData:{
                },
                event: {
                    onClose: function() {
                        $.post('${contextPath}/security/zd/rent_period_price/rent_period_price.htm', {
                            foregiftId: ${(foregiftId)!0},
                            batteryType: ${(entity.batteryType)!''},
                            agentId: ${(entity.agentId)!''}
                        }, function (html) {
                            $("#exchange_packet_period_price").html(html);
                        }, 'html');
                    }
                }
            });
        });

        $(".exchange_rent .fa-close").click(function () {
            var id = $(this).attr("exchange_rent_id");
            $.messager.confirm('提示信息', '删除租金设置将删除对应的分期设置，确认删除?', function (ok) {
                if (ok) {
                    $.post("${contextPath}/security/zd/rent_period_price/delete.htm?id=" + id, function (json) {
                        if (json.success) {
                            $.messager.alert('提示消息', '操作成功', 'info');
                            $.post('${contextPath}/security/zd/rent_period_price/rent_period_price.htm', {
                                foregiftId: ${(foregiftId)!0},
                                batteryType: ${(entity.batteryType)!''},
                                agentId: ${(entity.agentId)!''}
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