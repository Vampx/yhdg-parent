<div class="popup_body">
    <div class="search">
        <div class="float_right">
            <button class="btn btn_green" id="release_${pid}">发布</button>&nbsp;&nbsp;
            <button class="btn btn_yellow" id="add_${pid}">新增</button>
        </div>
        <h4>绑定素材</h4>
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
            url: "${contextPath}/security/yms/playlist_detail/page.htm?&playlistId="+playlistId,
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
                        title: '素材id',
                        align: 'center',
                        field: 'materialId',
                        width: 40
                    },
                    {
                        title: '素材名称',
                        align: 'center',
                        field: 'materialName',
                        width: 40
                    },
                    {
                        title: '排序号',
                        align: 'center',
                        field: 'orderNum',
                        width: 40
                    },
                    {
                        title: '创建时间',
                        align: 'center',
                        field: 'createTime',
                        width: 70
                    },
                    {
                        title: '操作',
                        align: 'center',
                        field: 'action',
                        width: 50,
                        formatter: function(val, row) {
                            var html = '';
                            html += ' <a href="javascript:edit_order_num_${pid}(ID)">排序</a>';
                            html += ' <a href="javascript:remove_${pid}(ID)">解绑</a>';
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
        $('#release_${pid}').click(function() {
            release();
        });

        function release() {
            $.post('${contextPath}/security/yms/playlist/release.htm', {
                playlistId: playlistId
            }, function (json) {
                if (json.success) {
                    $.messager.alert('提示消息', '发布成功', 'info');
                    reload();
                } else {
                    $.messager.alert('提示消息', json.message, 'info');
                }
            }, 'json');

        }

        function add() {
            App.dialog.show({
                css: 'width:750px;height:470px;',
                title: '增加素材',
                href: "${contextPath}/security/yms/playlist_detail/add.htm?agentId=${agentId!0}",
                windowData: {
                    ok: function (checked) {
                        console.log(checked);
                        var materialId = [];
                        for(var i=0; i<checked.length; i++){
                            materialId.push(checked[i].id)
                        }
                        $.post('${contextPath}/security/yms/playlist_detail/create.htm',
                                {
                                    materialId:materialId,
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


    function edit_order_num_${pid}(id) {
        App.dialog.show({
            css: 'width:400px;height:160px;overflow:visible;',
            title: '排序',
            href: "${contextPath}/security/yms/playlist_detail/edit_order_num.htm?id=" + id,
            event: {
                onClose: function() {
                    reload();
                }
            }
        });
    }

    function remove_${pid}(id) {
        var win = $('#${pid}'), windowData = win.data('windowData');var playlistId = windowData.playlistId;
        $.messager.confirm('提示信息', '确认删除?', function (ok) {
            if (ok) {
                $.post('${contextPath}/security/yms/playlist_detail/delete.htm',
                        {
                            id: id,
                            playlistId:playlistId
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

