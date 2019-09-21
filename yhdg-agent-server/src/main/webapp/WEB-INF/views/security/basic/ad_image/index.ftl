<@app.html>
    <@app.head>
    <script>
        $(function() {
            $('#page_table').datagrid({
                fit: true,
                width: '100%',
                height: '100%',
                striped: true,
                pagination: true,
                url: "${contextPath}/security/basic/ad_image/page.htm",
                fitColumns: true,
                pageSize: 10,
                pageList: [10, 50, 100],
                idField: 'id',
                singleSelect: true,
                selectOnCheck: false,
                checkOnSelect: false,
                autoRowHeight: false,
                rowStyler: gridRowStyler,
                columns: [
                    [
                        { field: 'checkbox', checkbox: true },
                        {
                            title: '序号',
                            align: 'center',
                            field: 'orderNum',
                            width: 40,
                            formatter: function(val,row){
                                var html = "<input type='text' style='width: 40px; height:15px; text-align: center;' id='order_num_ID' onblur='updateOrderNum(ID)' onkeypress='if(event.keyCode==13) {updateOrderNum(ID);return false;}' value=" + val+ ">"
                                return html.replace(/ID/g, row.id);}
                        },
                        {
                            title: '图片',
                            align: 'center',
                            field: 'imagePath',
                            width: 40,
                            formatter: function(val,row){
                                return "<img style='width:60px;height:30px;' src='${controller.appConfig.staticUrl}"+ val +"'>"
                            }
                        },
                        {
                            title: '操作',
                            align: 'center',
                            field: 'id',
                            width: 50,
                            formatter: function(val, row) {
                                var html = ' <a href="javascript:view(ID, AGENT_ID, MODULE_ID)">查看</a>';
                                    html += ' <a href="javascript:edit(ID, AGENT_ID, MODULE_ID)">修改</a>';
                                    html += ' <a href="javascript:remove(ID, AGENT_ID, MODULE_ID)">删除</a>';
                                return html.replace(/AGENT_ID/g, row.agentId).replace(/MODULE_ID/g, row.moduleId).replace(/ID/g, row.id);
                            }
                        }
                    ]
                ],
                queryParams: {
                },
                onLoadSuccess:function() {
                    $('#page_table').datagrid('clearChecked');
                    $('#page_table').datagrid('clearSelections');
                }
            });
        })

        function reload() {
            var datagrid = $('#page_table');
            datagrid.datagrid('reload');
        }

        function query() {
            var datagrid = $('#page_table');
            var statusId = $('#statusId').val();
            var status = $('#status').val();
            datagrid.datagrid('options').queryParams = {
                id: statusId,
                status: status
            };
            datagrid.datagrid('load');
        }

        function add() {
            App.dialog.show({
                css: 'width:420px;height:300px;overflow:visible;',
                title: '上传图片',
                href: "${contextPath}/security/basic/ad_image/add.htm",
                event: {
                    onClose: function() {
                        reload();
                    }
                }
            });
        }
        function updateOrderNum(id) {
            $.ajax({
                cache: false,
                async:false,
                type:'POST',
                url:"${contextPath}/security/basic/ad_image/update_order_num.htm",
                data:{
                    id: id,
                    orderNum: $('#order_num_' + id).val()
                },
                dataType: 'json',
                success: function(json){
                    <@app.json_jump/>
                    if(json.success) {
                        reload();
                    } else {
                        $.messager.alert('提示信息', json.message, 'info');
                    }

                }

            });
        }
        function edit(id) {
            App.dialog.show({
                css: 'width:420px;height:300px;overflow:visible;',
                title: '修改图片',
                href: "${contextPath}/security/basic/ad_image/edit.htm?id=" + id,
                event: {
                    onClose: function() {
                        var datagrid = $('#page_table');
                        datagrid.datagrid('reload');
                    },
                    onLoad: function() {
                    }
                }
            });
        }
        function view(id) {
            App.dialog.show({
                css: 'width:420px;height:300px;overflow:visible;',
                title: '查看',
                href: "${contextPath}/security/basic/ad_image/view.htm?id=" + id
            });
        }
        function remove(id) {
            $.messager.confirm('提示信息', '确认删除?', function(ok) {
                if(ok) {
                    $.post('${contextPath}/security/basic/ad_image/delete.htm', {
                        id: id
                    }, function (json) {
                        if (json.success) {
                            $.messager.alert('info', '操作成功', 'info');
                            reload();
                        } else {
                            $.messager.alert('提示消息', json.message, 'info');
                        }
                    }, 'json');
                }
            });
        }
    </script>
    </@app.head>
    <@app.body>
        <@app.container>
            <@app.banner/>
            <div class="main">
                <@app.menu/>
                <div class="content">
                    <div class="panel search" style="height: 30px;">
                        <div class="float_right">
                            <button class="btn btn_green" onclick="add()">新建</button>
                        </div>
                        <table cellpadding="0" cellspacing="0" border="0">
                            <tr>
                                <td align="right" width="75"></td>
                                <td>

                                </td>
                            </tr>
                        </table>
                    </div>
                    <div class="panel grid_wrap">
                        <div class="toolbar clearfix">
                            <h3>广告横幅</h3>
                        </div>
                        <div class="grid">
                            <table id="page_table"></table>
                        </div>
                    </div>
                </div>
            </div>
        </@app.container>
    </@app.body>
</@app.html>