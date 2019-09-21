<@app.html>
    <@app.head>
    <script>
        $.extend($.fn.validatebox.defaults.rules, {
            equals: {
                validator: function (value, param) {
                    return value == $(param[0]).val();
                },
                message: '密码不匹配'
            },
            mobile: {
                validator: function (value, param) {
                    if (value != "") {
                         var result = /^1[3|4|5|7|8][0-9]\d{4,8}$/.test(value);
                         if(result) {
                             var success;
                             $.ajax({
                                 cache: false,
                                 async: false,
                                 type: 'POST',
                                 url: '${contextPath}/security/basic/person/unique.htm',
                                 data: {
                                     mobile: value,
                                     id: param.length ? param[0] : ''
                                 },
                                 dataType: 'json',
                                 success: function (json) {
                                     <@app.json_jump/>
                                     if (json.success) {
                                         success = true;
                                     } else {
                                         success = false;
                                     }
                                 }
                             });
                             return success;
                         }
                    }
                },
                message: '手机号码格式错误/手机号码重复'
            }
        });

        $(function () {
            $('#page_table').datagrid({
                fit: true,
                width: '100%',
                height: '100%',
                striped: true,
                pagination: true,
                url: "${contextPath}/security/basic/person/page.htm",
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
                        {field: 'checkbox', checkbox: true},
                        {
                            title: '手机号',
                            align: 'center',
                            field: 'mobile',
                            width: 60
                        },
                        {title: '类型', align: 'center', field: 'isProtected', width: 60,
                            formatter: function(val, row) {
                                if(val == 1){
                                    return "管理员";
                                }else{
                                    return "普通账户";
                                }
                            }
                        },
                        {
                            title: '操作',
                            align: 'center',
                            field: 'id',
                            width: 100,
                            formatter: function (val, row) {
                                var html = '';
                                <@app.has_oper perm_code='basic.Person:view'>
                                    html += '<a href="javascript:view(ID)">查看</a>';
                                </@app.has_oper>
                                <@app.has_oper perm_code='basic.Person:edit'>
                                    html += ' <a href="javascript:edit(ID)">修改</a>';
                                </@app.has_oper>
                                return html.replace(/ID/g, row.id);
                            }
                        }
                    ]
                ],
                onLoadSuccess: function () {
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
            var mobile = $('#mobile').val();
            datagrid.datagrid('options').queryParams = {
                mobile: mobile,
                agentId: $('#agent_id').combotree('getValue')
            };
            datagrid.datagrid('load');
        }

        function add() {
            App.dialog.show({
                css: 'width:380px;height:430px;overflow:visible;',
                title: '新建',
                href: "${contextPath}/security/basic/person/add.htm",
                event: {
                    onClose: function () {
                        reload();
                    }
                }
            });
        }

        function edit(id) {
            App.dialog.show({
                css: 'width:780px;height:680px;overflow:visible;',
                title: '修改',
                href: "${contextPath}/security/basic/person/edit.htm?id=" + id,
                event: {
                    onClose: function () {
                        var datagrid = $('#page_table');
                        datagrid.datagrid('reload');
                    },
                    onLoad: function () {
                    }
                }
            });
        }

        function view(id) {
            App.dialog.show({
                css: 'width:590px;height:380px;overflow:visible;',
                title: '查看',
                href: "${contextPath}/security/basic/person/view.htm?id=" + id
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
                <div class="panel search">
                    <div class="float_right">
                        <button class="btn btn_yellow" onclick="query()">搜索</button>
                    </div>
                    <table cellpadding="0" cellspacing="0" border="0">
                        <tr>
                            <td width="60" align="right">运营商：</td>
                            <td>
                                <input name="agentId" id="agent_id" class="easyui-combotree" editable="true"
                                       style="width: 200px;height: 28px;"
                                       data-options="url:'${contextPath}/security/basic/agent/tree.htm?dummy=${'系统'?url}',
                                method:'get',
                                valueField:'id',
                                textField:'text',
                                editable:true,
                                multiple:false,
                                panelHeight:'200',
                                onClick: function(node) {
                                }
                            "
                                        >
                            </td>
                            <td align="right" width="60">手机：</td>
                            <td><input type="text" class="text" id="mobile"/></td>
                        </tr>
                    </table>
                </div>
                <div class="panel grid_wrap">
                    <div class="toolbar clearfix">
                        <div class="float_right">
                            <@app.has_oper perm_code='basic.Person:add'>
                                <button class="btn btn_green" onclick="add()">新建</button>
                            </@app.has_oper>
                        </div>
                        <h3>用户信息</h3>
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