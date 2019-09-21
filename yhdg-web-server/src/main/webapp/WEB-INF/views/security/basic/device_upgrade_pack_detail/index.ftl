<div class="popup_body">
    <div class="search">
        <div class="float_right">
            <button class="btn btn_yellow" id="add_${pid}">新增</button>
        </div>
        <h4>升级版本设备</h4>
    </div>
    <div style="width:750px; height:360px; padding-top: 6px;">
        <table id="parent_page_table_${pid}">
        </table>
    </div>
</div>
<div class="popup_btn">
    <button class="btn btn_border" id="close_${pid}">关闭</button>
</div>
<script>
    (function() {
        var win = $('#${pid}'), windowData = win.data('windowData');
        var upgradePackId = windowData.packId;
        var datagrid = $('#parent_page_table_${pid}');
        datagrid.datagrid({
            fit: true,
            width: '100%',
            height: '100%',
            striped: true,
            pagination: true,
            url: "${contextPath}/security/basic/device_upgrade_pack_detail/page.htm?packId=" + upgradePackId,
            fitColumns: true,
            pageSize: 10,
            pageList: [10, 50, 100],
            idField: 'id',
            singleSelect: true,
            selectOnCheck: true,
            checkOnSelect: true,
            autoRowHeight: false,
            rowStyler: gridRowStyler,
            columns: [
                [
                    {
                        title: '终端ID',
                        align: 'center',
                        field: 'deviceId',
                        width: 250
                    },
                    {
                        title: '终端版本',
                        align: 'center',
                        field: 'deviceVersion',
                        width: 250
                    },
                    {
                        title: '心跳时间',
                        align: 'center',
                        field: 'heartTime',
                        width: 250
                    },
                    {
                        title: '操作',
                        align: 'center',
                        field: 'action',
                        width: 200,
                        formatter: function(val, row) {
                            var html = '<a href="javascript:del(\'deviceId\')">移除</a>';
                            return html.replace(/deviceId/g, row.deviceId);
                        }
                    }
                ]
            ],
            onLoadSuccess:function() {
                datagrid.datagrid('clearChecked');
                datagrid.datagrid('clearSelections');
            }
        });


        $('#close_${pid}').click(function() {
            $('#${pid}').window('close');
        });

        $('#add_${pid}').click(function() {
            add();
        });

        function add() {
            App.dialog.show({
                css: 'width:750px;height:470px;',
                title: '增加升级电池',
                href: "${contextPath}/security/basic/device_upgrade_pack_detail/add.htm",
                windowData: {
                    ok: function (checked) {
                        console.log(checked);
                        var deviceId = [];
                        for(var i=0; i<checked.length; i++){
                            deviceId.push(checked[i].deviceId)
                        }
                        $.post('${contextPath}/security/basic/device_upgrade_pack_detail/create.htm',
                                {
                                    deviceId:deviceId,
                                    packId:upgradePackId
                                },
                                function(resultJSONObject){
                                    if(resultJSONObject.success){
                                        $.messager.alert("系统提示","添加成功","info");
                                        $('#page_table_${pid}').datagrid('reload');
                                    }else{
                                        $.messager.alert("系统提示","不","error");
                                    }
                                },"json");
                    }

                },
                event: {
                    onClose: function() {
                        reload();
                    },
                    onLoad: function() {
                    }
                }
            });
        }

    })();

    function reload() {
        var datagrid = $('#parent_page_table_${pid}');
        datagrid.datagrid('reload');
    }

    function del(terminalId) {
        var win = $('#${pid}'), windowData = win.data('windowData');
        var upgradePackId = windowData.packId;
        $.messager.confirm('提示信息', '确认删除?', function (ok) {
            if (ok) {
                $.post('${contextPath}/security/basic/device_upgrade_pack_detail/delete.htm?packId='+upgradePackId,
                        {
                            deviceId: terminalId
                        }, function (json) {
                            if (json.success) {
                                $.messager.alert('提示信息', '操作成功', 'info');
                                $('#parent_page_table_${pid}').datagrid('reload');
                            } else {
                                $.messager.alert('提示消息', json.message, 'info');
                            }
                        }, 'json');
            }
        });
    }

</script>

