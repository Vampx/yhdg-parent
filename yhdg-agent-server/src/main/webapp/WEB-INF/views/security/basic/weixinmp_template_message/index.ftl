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
                url: "${contextPath}/security/basic/weixinmp_template_message/page.htm",
                fitColumns: true,
                pageSize: 50,
                pageList: [50, 100, 150],
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
                            title: '源类型',
                            align: 'center',
                            field: 'sourceType',
                            width: 30,
                            formatter: function(val){
                                <#list sourceTypeEnum as e>
                                    if(${e.getValue()} == val)
                                    return '${e.getName()}';
                                </#list>
                            }
                        },
                        {
                            title: '手机号',
                            align: 'center',
                            field: 'mobile',
                            width: 60
                        },

                        {
                            title: '模板id',
                            align: 'center',
                            field: 'type',
                            width: 30
                        },
                        {
                            title: '重发次数',
                            align: 'center',
                            field: 'resendNum',
                            width: 30
                        },
                        {
                            title: '变量',
                            align: 'center',
                            field: 'variable',
                            width: 70
                        },
                        {
                            title: '消息状态',
                            align: 'center',
                            field: 'status',
                            width: 35,
                              formatter: function(val){
                                  <#list messageStatusEnum as e>
                                      if(${e.getValue()} == val)
                                      return '${e.getName()}';
                                  </#list>
                              }
                        },
                        {
                            title: '处理时间',
                            align: 'center',
                            field: 'handleTime',
                            width: 90
                        },
                        {
                            title: '昵称',
                            align: 'center',
                            field: 'nickname',
                            width: 70
                        },
                        {
                            title: '阅读次数',
                            align: 'center',
                            field: 'readCount',
                            width: 30
                        },
                        {
                            title: '创建时间',
                            align: 'center',
                            field: 'createTime',
                            width: 90
                        },
                        {
                            title: '操作',
                            align: 'center',
                            field: 'id',
                            width: 30,
                            formatter: function(val, row) {
                                var html = '';
                                <@app.has_oper perm_code='7_3_2_2'>
                                    html += '<a href="javascript:view(ID)">查看</a>';
                                </@app.has_oper>
                                return html.replace(/ID/g, row.id);
                            }
                        }
                    ]
                ],
                onLoadSuccess:function() {
                    $('#page_table').datagrid('clearChecked');
                    $('#page_table').datagrid('clearSelections');
                },
                queryParams: {
                    appId: 0
                }
            });
        });



        function reload() {
            var datagrid = $('#page_table');
            datagrid.datagrid('reload');
        }

        function query() {
            var datagrid = $('#page_table');
            var messageStatus = $('#messageStatus').combobox("getValue");
            var sourceType = $('#sourceType').combobox("getValue");
            var templateType = $('#templateType').combobox("getValue");
            var mobile = $('#mobile').val();
            var appId = $('#app_id').combotree('getValue');
            console.log('ssss',templateType);
            datagrid.datagrid('options').queryParams = {
                status: messageStatus,
                sourceType: sourceType,
                type: templateType,
                mobile: mobile,
                appId: appId
            };
            datagrid.datagrid('load');
        }

        function edit(id) {
            App.dialog.show({
                css: 'width:550px;height:336px;overflow:visible;',
                title: '修改',
                href: "${contextPath}/security/basic/weixinmp_template_message/edit.htm?id=" + id,
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
                css: 'width:600px;height:540px;overflow:visible;',
                title: '查看',
                href: "${contextPath}/security/basic/weixinmp_template_message/view.htm?id=" + id,
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
                            <td>平台类型：</td>
                            <td>
                                <input name="appId" id="app_id" style="height: 28px;width: 130px;" class="easyui-combobox"  editable="false"
                                       data-options="url:'${contextPath}/security/basic/agent/self_platform_agent_list.htm',
                                    method:'get',
                                    valueField:'id',
                                    textField:'agentName',
                                    editable:false,
                                    multiple:false,
                                    panelHeight:'200',
                                    onSelect: function(node) {
                                        query();
                                    }"
                                />
                            </td>
                            <td align="right">源类型：</td>
                            <td>
                                <select class="easyui-combobox" id="sourceType" style="height: 28px; width: 120px;">
                                    <option value="">所有</option>
                                    <#list sourceTypeEnum as e>
                                        <option value='${(e.getValue())!}'>${e.getName()}</option>
                                    </#list>
                                </select>
                            </td>
                            <td align="right">模板类型：</td>
                            <td>
                                <select class="easyui-combobox" id="templateType" style="height: 28px; width: 120px;">
                                    <option value="">所有</option>
                                    <#list typeEnum as e>
                                        <option value='${(e.getValue())!}'>${e.getName()}</option>
                                    </#list>
                                </select>
                            </td>
                            <td align="right">消息状态：</td>
                            <td>
                                <select class="easyui-combobox" id="messageStatus" style="height: 28px; width: 120px;">
                                    <option value="">所有</option>
                                    <#list messageStatusEnum as e>
                                        <option value='${(e.getValue())!}'>${e.getName()}</option>
                                    </#list>
                                </select>
                            </td>
                            <td align="right">&nbsp;&nbsp;手机号：</td>
                            <td><input type="text" class="text" id="mobile"/></td>
                        </tr>
                    </table>
                </div>
                <div class="panel grid_wrap">
                    <div class="toolbar clearfix">
                        <h3>公众号消息记录</h3>
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


