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
                url: "${contextPath}/security/basic/screen_upgrade_pack/page.htm?moduleId=${Session['SESSION_KEY_USER'].moduleId}",
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
                            title: '升级类型',
                            align: 'center',
                            field: 'upgradeName',
                            width: 60
                        },
                        {
                            title: '文件名称',
                            align: 'center',
                            field: 'fileName',
                            width: 60
                        },
                        {
                            title: '版本',
                            align: 'center',
                            field: 'version',
                            width: 60
                        },
                        {
                            title: '是否强制显示',
                            align: 'center',
                            field: 'isForce',
                            width: 60,
                            formatter: function(val) {
                                if(val==0){
                                    return '否'
                                }else{
                                    return '是'
                                }
                            }
                        },
                        {
                            title: '上传时间',
                            align: 'center',
                            field: 'updateTime',
                            width: 60
                        },
                        {title: '备注', align: 'center', field: 'memo', width: 100},
                        {
                            title: '操作',
                            align: 'center',
                            field: 'id',
                            width: 60,
                            formatter: function(val, row) {
                                var html = '';
                                <@app.has_oper perm_code='basic.ScreenUpgradePack:view'>
                                    html += '<a href="javascript:view(ID)">查看</a>';
                                </@app.has_oper>
                                <@app.has_oper perm_code='basic.ScreenUpgradePack:upload'>
                                    html+= ' <a href="javascript:edit(ID)">上传</a>';
                                </@app.has_oper>
                                <@app.has_oper perm_code='basic.ScreenUpgradePack:download'>
                                    html+= ' <a href="javascript:download(FILEPATH)">下载</a>';
                                </@app.has_oper>
                                return html.replace(/ID/g, row.id).replace(/FILEPATH/g, "'"+row.filePath+"'");
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
            var upgradeName = $('#upgrade_name').val();
            datagrid.datagrid('options').queryParams = {
                upgradeName: upgradeName
            };
            datagrid.datagrid('load');
        }

        function edit(id) {
            App.dialog.show({
                css: 'width:490px;height:363px;',
                title: '上传',
                href: "${contextPath}/security/basic/screen_upgrade_pack/edit.htm?id=" + id,
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
                css: 'width:490px;height:328px;',
                title: '查看',
                href: "${contextPath}/security/basic/screen_upgrade_pack/view.htm?id=" + id
            });
        }
        function download(filePath) {
            document.location.href = '${controller.appConfig.staticUrl}' + filePath;
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
                            <td align="right">名称：</td>
                            <td><input type="text" class="text" id="upgrade_name"/></td>
                        </tr>
                    </table>
                </div>
                <div class="panel grid_wrap">
                    <div class="toolbar clearfix">
                        <div class="float_right">

                        </div>
                        <h3>屏幕升级</h3>
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
