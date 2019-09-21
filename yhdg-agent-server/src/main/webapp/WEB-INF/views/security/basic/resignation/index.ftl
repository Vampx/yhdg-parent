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
                url: "${contextPath}/security/basic/resignation/page.htm",
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
                        {title: '客户姓名', align: 'center', field: 'customerFullname', width: 50},
                        {title: '手机号码', align: 'center', field: 'customerMobile', width: 70},
                        {title: '离职原因', align: 'center', field: 'content', width: 200},
                        {title: '状态', align: 'center', field: 'stateName', width: 70},
                        {title: '创建时间', align: 'center', field: 'createTime', width: 80},
                        {
                            title: '操作',
                            align: 'center',
                            field: 'id',
                            width: 60,
                            formatter: function (val, row) {
                                var html = '';
                                <@app.has_oper perm_code='1_1_7_2'>
                                    html += ' <a href="javascript:view(ID)">查看</a>';
                                </@app.has_oper>
                                <@app.has_oper perm_code='1_1_7_3'>
                                    if (row.state ==${AUDIT}) {
                                        html += ' <a href="javascript:edit(ID)">审核</a>';
                                    }
                                </@app.has_oper>
                                return html.replace(/ID/g, row.id);
                                return '';
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
            var belongCabinetId = $('#belong_cabinet_id').val();

            datagrid.datagrid('options').queryParams = {
                customerMobile: mobile,
                cabinetId: belongCabinetId
            };
            datagrid.datagrid('load');
        }


        function edit(id) {
            App.dialog.show({
                css: 'width:630px;height:300px;',
                title: '审核',
                href: "${contextPath}/security/basic/resignation/edit.htm?id=" + id,
                event: {
                    onClose: function () {
                        reload();
                    }
                }
            });
        }


        function view(id) {
            App.dialog.show({
                css: 'width:670px;height:370px;',
                title: '详情',
                href: "${contextPath}/security/basic/resignation/view.htm?id=" + id
            });
        }

        function selectCabinet() {
            App.dialog.show({
                css: 'width:826px;height:522px;overflow:visible;',
                title: '选择换电柜',
                href: "${contextPath}/security/hdg/cabinet/select_cabinets.htm",
                windowData: {
                    ok: function (config) {
                        $("#belong_cabinet_id").val(config.cabinet.id);
                        $("#belong_cabinet_name").val(config.cabinet.cabinetName);
                    }
                },
                event: {
                    onClose: function () {
                    }
                }
            });
        }

    </script>
    </@app.head>

    <@app.body>

        <@app.container>
            <@app.banner/>

        <div class="main">
            <@app.menu/>w

            <div class="content">
                <div class="panel search">
                    <div class="float_right">
                        <button class="btn btn_yellow" onclick="query()">搜索</button>
                    </div>
                    <table cellpadding="0" cellspacing="0" border="0">
                        <tr>
                            <td align="right">手机号码：</td>
                            <td><input type="text" class="text" id="mobile"/></td>
                            <td align="right">所属站点：</td>
                            <td><input onclick="selectCabinet()" readonly type="text" class="text"
                                       id="belong_cabinet_name"/><input
                                    type="hidden" id="belong_cabinet_id"/></td>
                        </tr>
                    </table>
                </div>
                <div class="panel grid_wrap">
                    <div class="toolbar clearfix">
                        <div class="float_right">
                        </div>
                        <h3>会员离职管理</h3>
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
