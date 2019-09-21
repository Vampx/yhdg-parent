<div class="state-list">
    <#if priceStationList?? && (priceStationList?size>0) >
        <#list priceStationList as priceStation>
        <div class="price_station">
            <input type="text" readonly value="<#if priceStation.stationId??>${(priceStation.stationName)!''}<#else></#if>"/>
            <i class="station-close" price_station_id="${(priceStation.stationId)!0}"  battery_type="${(priceStation.batteryType)!0}"><img src="${app.imagePath}/delete.png"/></i>
        </div>
        </#list>
    </#if>
</div>
<div class="addstate price_station_add">
    <input type="hidden" name="agentId" value="${(agentId)!0}">
    <input type="hidden" name="batteryType" value="${(batteryType)!0}">
    添加站点
</div>

<script>

    $(function() {

        $(".price_station_add").click(function () {
            var batteryTypeList = [], agentIdList = [];
            <#if priceStationList??>
                <#list priceStationList as priceStation>
                    batteryTypeList.push(${priceStation.batteryType});
                    agentIdList.push(${priceStation.agentId});
                </#list>
            </#if>
            var batteryType = 0;
            var agentId = 0;
            if (batteryTypeList.length > 0) {
                batteryType = batteryTypeList[0];
                agentId = agentIdList[0];
            } else {
                batteryType = $(this).find('input[name=batteryType]').val();
                agentId = $(this).find('input[name=agentId]').val();
            }

            if(batteryType == '') {
                $.messager.alert('提示信息', '请选择电池类型', 'info');
                return;
            }
            App.dialog.show({
                css: 'width:680px;height:530px;',
                title: '新建',
                href: "${contextPath}/security/hdg/station/price_station_page.htm?agentId=" + agentId,
                windowData: {
                    ok: function (rows) {
                        var ids = '';
                        if (rows.length > 0) {
                            for (var i = 0; i < rows.length; i++) {
                                ids += rows[i].stationId + ',';
                            }
                            ids = ids.substring(0, ids.lastIndexOf(','));
                            var values = {
                                batteryType:batteryType,
                                ids: ids
                            };
                            $.ajax({
                                cache: false,
                                async: false,
                                type: 'POST',
                                url: '${contextPath}/security/basic/agent_battery_type/price_station_create.htm',
                                dataType: 'json',
                                data: values,
                                success: function (json) {
                                <@app.json_jump/>
                                    if (json.success) {
                                        $.messager.alert('提示信息', '操作成功', 'info');
                                    } else {
                                        $.messager.alert('提示信息', json.message, 'info');
                                    }
                                }
                            });
                            return true;
                        } else {
                            $.messager.alert('提示信息', '请选择站点', 'info');
                            return false;
                        }
                    }
                },
                event: {
                    onClose: function() {
                        $.post('${contextPath}/security/basic/agent_battery_type/price_station.htm', {
                            batteryType:batteryType,
                            agentId: agentId
                        }, function (html) {
                            $("#price_station").html(html);
                        }, 'html');
                    }
                }
            });
        });

        $(".price_station .station-close").click(function () {
            var id = $(this).attr("price_station_id");
            var batteryTypeList = [], agentIdList = [];
            <#if priceStationList??>
                <#list priceStationList as priceStation>
                    batteryTypeList.push(${priceStation.batteryType});
                    agentIdList.push(${priceStation.agentId});
                </#list>
            </#if>
            var batteryType = 0;
            var agentId = 0;
            if (batteryTypeList.length > 0) {
                batteryType = batteryTypeList[0];
                agentId = agentIdList[0];
            } else {
                batteryType = $(this).find('input[name=batteryType]').val();
                agentId = $(this).find('input[name=agentId]').val();
            }
            $.messager.confirm('提示信息', '确认删除该站点?', function (ok) {
                if (ok) {
                    $.post("${contextPath}/security/basic/agent_battery_type/price_station_delete.htm?stationId=" + id + "&batteryType=" + batteryType, function (json) {
                        if (json.success) {
                            $.messager.alert('提示消息', '操作成功', 'info');
                            $.post('${contextPath}/security/basic/agent_battery_type/price_station.htm', {
                                batteryType:batteryType,
                                agentId: agentId
                            }, function (html) {
                                $("#price_station").html(html);
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