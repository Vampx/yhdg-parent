<div class="tab_item" style="display: block;">
    <div class="toolbar clearfix">
        <div class="float_right">
            <div class="float_right">
                <button class="btn btn_blue" onclick="addBatch()">批量绑定</button>
            </div>
        </div>
        <h3>已绑定换电站</h3>
    </div>
    <div class="grid" style="height:325px;">
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
            url: "${contextPath}/security/hdg/cabinet/page.htm?batteryType=${(batteryType)!''}&agentId=${(agentId)!''}",
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
                        title: '所属运营商',
                        align: 'center',
                        field: 'agentName',
                        width: 60
                    },
                    {
                        title: '换电站编号',
                        align: 'center',
                        field: 'id',
                        width: 60
                    },
                    {title: '换电站名称', align: 'center', field: 'cabinetName', width: 60},
                    {
                        title: '地址', align: 'center', field: 'address', width: 60
                    },
                    {
                        title: '操作',
                        align: 'center',
                        field: 'action',
                        width: 80,
                        formatter: function (val, row) {
                            var html = '<a href="javascript:remove_${pid}(CABINET_ID)">删除</a>';
                            return html.replace(/CABINET_ID/g, row.id);
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
            href: "${contextPath}/security/hdg/cabinet/unbind_cabinet_page.htm?agentId=${(agentId)!''}&batteryType=${(batteryType)!''}",
            windowData: {
                ok: function(rows) {
                    if(rows.length > 0) {
                        var cabinetIdList = [];
                        for(var i = 0; i < rows.length; i++) {
                            cabinetIdList.push(rows[i].cabinetId);
                        }
                        $.post('${contextPath}/security/hdg/cabinet/batch_bind_battery_type.htm?batteryType=${(batteryType)!''}',{
                            cabinetIdList: cabinetIdList
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

    function remove_${pid}(cabinetId) {
        $.messager.confirm('提示信息', '确认解绑?', function (ok) {
            if(ok) {
                $.post('${contextPath}/security/hdg/cabinet/unbind_battery_type.htm', {
                    cabinetId: cabinetId,
                    batteryType: ${(batteryType)!''}
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