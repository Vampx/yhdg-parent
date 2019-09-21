<div class="popup_body">
    <div class="search">
        <div class="float_right">
            <button class="btn btn_yellow" id="add_${pid}">新增</button>
        </div>
        <h4>关联终端</h4>
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
        var playlistId = windowData.playlistId;
        var datagrid = $('#parent_page_table_${pid}');
        datagrid.datagrid({
            fit: true,
            width: '100%',
            height: '100%',
            striped: true,
            pagination: true,
            url: "${contextPath}/security/yms/terminal/page.htm?playlistId="+playlistId,
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
                        field: 'id',
                        width: 181
                    },
                    {
                        title: '版本',
                        align: 'center',
                        field: 'version',
                        width: 143
                    },
                    {
                        title: '在线',
                        align: 'center',
                        field: 'terminalIsOnline',
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
                            var html = '<a href="javascript:del(\'ID\')">移除</a>';
                            return html.replace(/ID/g, row.id);
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
                title: '增加终端',
                href: "${contextPath}/security/yms/terminal/select_terminal.htm?playlistId=" + playlistId,
                windowData: {
                    ok: function (checked) {
                        console.log(checked);
                        var terminalId = [];
                        for(var i=0; i<checked.length; i++){
                            terminalId.push(checked[i].id)
                        }
                        $.post('${contextPath}/security/yms/terminal/update.htm',
                                {
                                    terminalIds:terminalId,
                                    playlistId:playlistId
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

    function del(id) {
        var win = $('#${pid}'), windowData = win.data('windowData');
        $.messager.confirm('提示信息', '确认删除?', function (ok) {
            if (ok) {
                $.post('${contextPath}/security/yms/terminal/delete_playlist.htm',
                        {
                            terminalId: id
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

