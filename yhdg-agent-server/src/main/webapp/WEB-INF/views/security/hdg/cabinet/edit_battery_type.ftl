<div class="tab_item" style="display: block;">
    <div class="toolbar clearfix">
        <div class="float_right">
            <div class="float_right">
                <button class="btn btn_blue" onclick="addBatch()">批量绑定</button>
            </div>
        </div>
        <h3>已绑定电池类型</h3>
    </div>
    <div class="grid" style="height:425px;">
        <table id="page_table_box_${pid}"></table>
    </div>
</div>

<script>

    (function () {
        $('#page_table_box_${pid}').datagrid({
            fit: true,
            width: '100%',
            height: '100%',
            striped: true,
            pagination: true,
            url: "${contextPath}/security/basic/agent_battery_type/page.htm?agentId=${agentId}&cabinetId=${cabinetId}",
            pageSize: 10,
            pageList: [10, 50, 100],
            fitColumns: true,
            idField: 'id',
            singleSelect: true,
            selectOnCheck: false,
            checkOnSelect: false,
            autoRowHeight: false,
            rowStyler: gridRowStyler,
            columns: [
                [
                    {
                        title: '运营商',
                        align: 'center',
                        field: 'agentName',
                        width: 40
                    },
                    {
                        title: '类型名称',
                        align: 'center',
                        field: 'typeName',
                        width: 80
                    },
                    {
                        title: '额定电压(V)',
                        align: 'center',
                        field: 'ratedVoltage',
                        width: 80,
                        formatter: function (val) {
                            return Number(val / 1000);
                        }
                    },
                    {
                        title: '额定容量(Ah)',
                        align: 'center',
                        field: 'ratedCapacity',
                        width: 80,
                        formatter: function (val) {
                            return Number(val / 1000);
                        }
                    },
                    {
                        title: '操作',
                        align: 'center',
                        field: 'action',
                        width: 80,
                        formatter: function (val, row) {
                            var html = '<a href="javascript:remove_${pid}(BATTERY_TYPE)">解绑</a>';
                            return html.replace(/BATTERY_TYPE/g, row.batteryType);
                        }
                    }
                ]
            ]
        });
    })();
    function reload_${pid}() {
        var datagrid = $('#page_table_box_${pid}');
        datagrid.datagrid('reload');
    }

    function addBatch() {
        App.dialog.show({
            css: 'width:680px;height:530px;',
            title: '新建',
            href: "${contextPath}/security/basic/agent_battery_type/unbind_battery_type_page.htm?agentId=${(agentId)!''}&cabinetId=${(cabinetId)}",
            windowData: {
                ok: function(rows) {
                    if(rows.length > 0) {
                        var batteryTypeList = [];
                        for(var i = 0; i < rows.length; i++) {
                            batteryTypeList.push(rows[i].batteryType);
                        }
                        $.post('${contextPath}/security/basic/agent_battery_type/batch_bind_cabinet.htm?cabinetId=${(cabinetId)!''}',{
                            batteryTypeList: batteryTypeList
                        }, function(json) {
                            if(json.success) {
                                $.messager.alert('提示信息', json.message, 'info');
                                reload_${pid}();
                            }else{
                                $.messager.alert('提示消息', json.message, 'info');
                            }
                        }, 'json');
                        return true;
                    }else {
                        $.messager.alert('提示信息', '请选择换电站', 'info');
                        return false;
                    }
                }
            }
        });
    }

    function remove_${pid}(batteryType) {
        $.messager.confirm('提示信息', '确认解绑?', function (ok) {
            if(ok) {
                $.post('${contextPath}/security/hdg/cabinet/unbind_battery_type.htm', {
                    batteryType: batteryType,
                    cabinetId: ${(cabinetId)!''}
                }, function(json) {
                    if(json.success) {
                        $.messager.alert('提示信息', '解绑成功', 'info');
                        reload_${pid}();
                    }else {
                        $.messager.alert('提示信息', json.message, 'info');
                    }
                }, 'json');
            }
        })
    }

    $('#${pid}').data('ok', function() {
        return true;
    });

</script>