<@app.html>
    <@app.head>
    <script>
        $(function () {
            $('#page_table').datagrid({
                fit: true,
                width: '100%',
                height: '100%',
                striped: true,
                pagination: true,
                url: "${contextPath}/security/basic/agent_settlement/page.htm",
                fitColumns: true,
                pageSize: 50,
                pageList: [50, 100],
                idField: 'id',
                singleSelect: true,
                selectOnCheck: false,
                checkOnSelect: false,
                autoRowHeight: false,
                rowStyler: gridRowStyler,
                columns: [
                    [
                        {
                            title: '状态',
                            align: 'center',
                            field: 'stateName',
                            width: 30
                        },
                        {
                            title: '总收入', align: 'center', field: 'money', width: 30,
                            formatter: function (val) {
                                return Number(val / 100).toFixed(2);
                            }
                        },
                        {title: '总换电单数', align: 'center', field: 'orderCount', width: 30},
                        {
                            title: '换电收入', align: 'center', field: 'exchangeMoney', width: 30,
                            formatter: function (val) {
                                return Number(val / 100).toFixed(2);
                            }
                        },
                        {
                            title: '套餐收入', align: 'center', field: 'packetPeriodMoney', width: 30,
                            formatter: function (val) {
                                return Number(val / 100).toFixed(2);
                            }
                        },
                        {title: '总套餐单数', align: 'center', field: 'packetPeriodCount', width: 30},
                        {
                            title: '省代收入', align: 'center', field: 'provinceIncome', width: 30,
                            formatter: function (val) {
                                return Number(val / 100).toFixed(2);
                            }
                        },
                        {
                            title: '市代收入', align: 'center', field: 'cityIncome', width: 30,
                            formatter: function (val) {
                                return Number(val / 100).toFixed(2);
                            }
                        },
                        {
                            title: '处理时间',
                            align: 'center',
                            field: 'handleTime',
                            width: 50
                        },
                        {
                            title: '申请时间',
                            align: 'center',
                            field: 'createTime',
                            width: 50
                        },
                        {
                            title: '操作',
                            align: 'center',
                            field: 'id',
                            width: 60,
                            formatter: function (val, row) {
                                var html = '';
                                <@app.has_oper perm_code='1_1_21_3'>
                                    html += ' <a href="javascript:edit(ID)">通过</a>';
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
            var queryParams = {
                state: $('#state').val()
            };
            datagrid.datagrid('options').queryParams = queryParams;
            datagrid.datagrid('load');
        }

        function view(id) {
            App.dialog.show({
                css: 'width:650px;height:440px;overflow:visible;',
                title: '查看',
                href: "${contextPath}/security/basic/agent_settlement/view.htm?id=" + id
            });
        }

        function edit() {
            $.messager.confirm('提示信息', '确认审核通过?', function (ok) {
                if (ok) {
                    $.ajax({
                        url: "${contextPath}/security/basic/agent_settlement/adopt.htm",
                        success: function (text) {
                            var json = $.evalJSON(text);
                            <@app.json_jump/>
                            if (json.success) {
                                $.messager.alert('提示信息', '操作成功', 'info');
                                $('#page_table').datagrid('load');
                            } else {
                                $.messager.alert('提示信息', json.message, 'info');
                            }
                        }
                    });
                }
            })
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
                            <td align="right" width="60">状态：</td>
                            <td>
                                <select style="width:90px;" id="state">
                                    <option value="">所有</option>
                                    <#list StateEnum as e>
                                        <option value="${e.getValue()}">${e.getName()}</option>
                                    </#list>
                                </select>
                            </td>
                        </tr>
                    </table>
                </div>
                <div class="panel grid_wrap">
                    <div class="toolbar clearfix">
                        <h3>结算记录</h3>
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
