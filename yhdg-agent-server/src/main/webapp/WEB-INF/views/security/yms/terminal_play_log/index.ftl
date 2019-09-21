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
                        {
                            title: '运营商',
                            align: 'center',
                            field: 'agentId',
                            width: 60
                        },
                        {
                            title: '终端编号',
                            align: 'center',
                            field: 'terminalId',
                            width: 60
                        },
                        {
                            title: '播放区域编号',
                            align: 'center',
                            field: 'areaNum',
                            width: 60
                        },
                        {
                            title: '素材名称',
                            align: 'center',
                            field: 'materialName',
                            width: 100
                        },
                        {
                            title: '创建时间',
                            align: 'center',
                            field: 'createTime',
                            width: 70
                        },
                        {
                            title: '开始时间',
                            align: 'center',
                            field: 'beginTime',
                            width: 70
                        },
                        {
                            title: '结束时间',
                            align: 'center',
                            field: 'endTime',
                            width: 70
                        },
                        {
                            title: '操作',
                            align: 'center',
                            field: 'id',
                            width: 60,
                            formatter: function(val, row) {
                                var html = '<a href="javascript:view(ID,SUFFIX)">查看</a>';
                                return html.replace(/ID/g, row.id).replace(/SUFFIX/g, row.suffix);
                            }
                        }
                    ]
                ],
                onLoadSuccess:function() {
                    $('#page_table').datagrid('clearChecked');
                    $('#page_table').datagrid('clearSelections');
                }
            });
        });

        function reload() {
            var datagrid = $('#page_table');
            datagrid.datagrid('reload');
        }


        function query() {
            var datagrid = $('#page_table');
            var tree = $('#tree').tree("getSelected");
            var queryBeginTime = $('#begin_time').datebox('getValue');
            var queryEndTime = $('#end_time').datebox('getValue');
            var materialName = $('#materialName').val();
            var suffix = tree.id.replace('month-',"");
            var agentId = $('#agent_id').combotree("getValue");
            datagrid.datagrid('options').url = "${contextPath}/security/yms/terminal_play_log/page.htm";
            if(queryEndTime != "" &&  queryEndTime != ""){
                if(queryBeginTime >= queryEndTime || queryEndTime <= queryBeginTime) {
                    $.messager.alert('提示信息', '结束日期必须大于开始日期', 'info');
                    return;
                }
            }else{
                queryEndTime = "";
                queryBeginTime = "";
            }
            datagrid.datagrid('options').queryParams = {
                queryBeginTime: queryBeginTime,
                queryEndTime: queryEndTime,
                <#--agentId : ${(Session['SESSION_KEY_USER'].agentId)!''},-->
                suffix: suffix,
                materialName: materialName,
                agentId: agentId
            };
            datagrid.datagrid('reload');
        }


        function view(id, suffix) {
            App.dialog.show({
                css: 'width:560px;height:290px;',
                title: '查看',
                href: "${contextPath}/security/yms/terminal_play_log/view.htm?id=" + id + "&suffix=" + suffix
            });
        }

        function queryByTree(node) {
            var year=$('#suffix').tree('getParent',node.target).text;
            var month = node.text;
            var suffix = year + (month < 10 ? "0"+month : month);
            var datagrid = $('#page_table');
            datagrid.datagrid('options').queryParams = {
                suffix: suffix
            };
            datagrid.datagrid('load');
        }
    </script>
    </@app.head>
    <@app.body>
        <@app.container>
            <@app.banner/>
        <div class="main">
            <@app.menu/>
            <div class="content">
                <div class="panel ztree_wrap">
                    <div class="ztree">
                        <div class="ztree_head">
                            <h3>日期分组</h3>
                        </div>
                        <div class="ztree_body">
                            <ul class="easyui-tree" id="tree"
                                url="${contextPath}/security/yms/terminal_play_log/tree.htm?dummy=${'所有'?url}"
                                lines="true"
                                data-options="
                                    onClick: function(node) {
                                         if(node.id.indexOf('month') >= 0) {
                                            query()
                                        }
                                    },
                                    onLoadSuccess: function(node, data) {
                                        if(data.length){
                                            var tree = $('#tree');
                                            var node = tree.tree('find', data[0].id);
                                            var years = $('#tree').tree('getChildren',node.target);
                                            if(years.length) {
                                                var months = $('#tree').tree('getChildren',years[0].target);
                                                $('#tree').tree('select', months[0].target);
                                                query();
                                            }
                                        }
                                    }
                                "
                            >
                            </ul>
                        </div>
                    </div>
                </div>
                <div class="panel search" style="margin-left:250px;">
                    <div class="float_right">
                        <button class="btn btn_yellow" onclick="query()">搜索</button>
                    </div>
                    <table cellpadding="0" cellspacing="0" border="0">
                        <tr>
                            <td align="right" width="50">运营商：</td>
                            <td>
                                <input name="agentId" id="agent_id" class="easyui-combotree" editable="false" style="height: 28px;"
                                       data-options="url:'${contextPath}/security/basic/agent/tree.htm?dummy=${'所有'?url}',
                                method:'get',
                                        valueField:'id',
                                        textField:'text',
                                        editable:false,
                                        multiple:false,
                                        panelHeight:'200',
                                        onClick: function(node) {
                                }
                                "
                                >
                            </td>
                            <td width="65" align="right">素材名称：</td>
                            <td><input type="text" class="text" id="materialName"/></td>
                            <td align="right">&nbsp;&nbsp;开始/结束：</td>
                            <td>
                                <input id="begin_time" class="easyui-datebox" name="queryBeginTime" type="text" style="width:150px;;height:27px;" >
                                -
                                <input id="end_time" class="easyui-datebox" name="queryEndTime" type="text" style="width:150px;;height:27px;" >
                            </td>
                        </tr>
                    </table>
                </div>
                <div class="panel grid_wrap" style="margin-left:250px;">
                    <div class="toolbar clearfix">
                        <h3>终端播放日志</h3>
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
