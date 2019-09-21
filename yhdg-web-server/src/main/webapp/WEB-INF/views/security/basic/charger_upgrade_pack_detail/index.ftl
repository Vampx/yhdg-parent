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
        var upgradePackId = windowData.chargerUpgradePackId;
        var datagrid = $('#parent_page_table_${pid}');
        datagrid.datagrid({
            fit: true,
            width: '100%',
            height: '100%',
            striped: true,
            pagination: true,
            url: "${contextPath}/security/basic/charger_upgrade_pack_detail/page.htm?upgradePackId="+upgradePackId,
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
                        title: '换电柜ID',
                        align: 'center',
                        field: 'cabinetId',
                        width: 180
                    },
                    {
                        title: '充电器版本',
                        align: 'center',
                        field: 'cabinetChargerVersions',
                        width: 180
                    },
                    {
                        title: '换电柜名称',
                        align: 'center',
                        field: 'cabinetName',
                        width: 181
                    },
                    {
                        title: '运营商',
                        align: 'center',
                        field: 'agentName',
                        width: 143
                    },
                    {
                        title: '地址',
                        align: 'center',
                        field: 'cabinetAddress',
                        width: 143
                    },
                    {
                        title: '在线',
                        align: 'center',
                        field: 'cabinetIsOnline',
                        width: 143,
                        formatter: function(val, row) {
                            if (val == 1) {
                                return '<a style="color: #00FF00;">是</a>';
                            } else {
                                return '<a style="color: #ff0000;">否</a>';
                            }
                        }
                    },
                    {
                        title: '操作',
                        align: 'center',
                        field: 'action',
                        width: 150,
                        formatter: function(val, row) {
                            var html = '<a href="javascript:del(\'upgradePackId\',\'terminalId\')">移除</a>';
                            return html.replace(/upgradePackId/g, row.upgradePackId).replace(/terminalId/g, row.terminalId);
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
                title: '增加升级换电柜',
                href: "${contextPath}/security/basic/charger_upgrade_pack_detail/add.htm",
                windowData: {
                    ok: function (checked) {
                        console.log(checked);
                        var chargerId = [];
                        for(var i=0; i<checked.length; i++){
                            chargerId.push(checked[i].id)
                        }
                        $.post('${contextPath}/security/basic/charger_upgrade_pack_detail/create.htm',
                                {
                                    chargerId:chargerId,
                                    upgradePackId:upgradePackId
                                },
                                function(resultJSONObject){
                            if(resultJSONObject.success){
                                $.messager.alert("系统提示","添加成功","info");
                                $('#parent_page_table_${pid}').datagrid('reload');
                            }else{
                                $.messager.alert("系统提示","不","error");
                            }
                        },"json");
                     }

                    },

                event: {
                    onClose: function() {
                        var datagrid = $('#parent_page_table_${pid}');
                        datagrid.datagrid('reload');
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

    function del(upgradePackId, terminalId) {
        var win = $('#${pid}'), windowData = win.data('windowData');
        var upgradePackId = windowData.chargerUpgradePackId;
        $.messager.confirm('提示信息', '确认删除?', function (ok) {
            if (ok) {
                $.post('${contextPath}/security/basic/charger_upgrade_pack_detail/delete.htm?upgradePackId='+upgradePackId,
                        {
                            terminalId: terminalId
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

