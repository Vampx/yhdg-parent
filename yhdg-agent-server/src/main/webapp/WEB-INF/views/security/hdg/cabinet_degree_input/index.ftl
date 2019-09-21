<@app.html>
    <@app.head>
    <script>
        $(function () {
            $('#page_table').datagrid({
                fit: true,
                width: '100%',
                height: '100%',
                nowrap: false,
                striped: true,
                pagination: true,
                url: "${contextPath}/security/hdg/cabinet_degree_input/page.htm",
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
                            title: '编号',
                            align: 'center',
                            field: 'index',
                            width: 60,
                            formatter:function(val,row,index){
                                var options = $("#page_table").datagrid('getPager').data("pagination").options;
                                var currentPage = options.pageNumber;
                                var pageSize = options.pageSize;
                                return (pageSize * (currentPage -1))+(index+1);
                            }
                        },
                        {
                            title: '运营商名称',
                            align: 'center',
                            field: 'agentName',
                            width: 60
                        },
                        {
                            title: '设备名称',
                            align: 'center',
                            field: 'cabinetName',
                            width: 60
                        },
                        {
                            title: '设备编号',
                            align: 'center',
                            field: 'cabinetId',
                            width: 70
                        },
                        {
                            title: '上期录入表码(度)',
                            align: 'center',
                            field: 'beginNum',
                            width: 80,
                            formatter: function (val) {
                                return Number(val / 100).toFixed(2);
                            }
                        },
                        {
                            title: '录入表码(度)',
                            align: 'center',
                            field: 'endNum',
                            width: 80,
                            formatter: function (val) {
                                return Number(val / 100).toFixed(2);
                            }
                        },
                        {
                            title: '系统表码(度)',
                            align: 'center',
                            field: 'beginInputNum',
                            width: 60,
                            formatter: function (val,row) {
                                return Number(row.systemInputNum/ 100).toFixed(2);
                            }
                        },
                        {
                            title: '本期用电量(度)',
                            align: 'center',
                            field: 'degree',
                            width: 60,
                            formatter: function (val) {
                                return Number(val / 100).toFixed(2);
                            }
                        },
                        {
                            title: '本期电费(元)',
                            align: 'center',
                            field: 'degreeMoney',
                            width: 60,
                            formatter: function (val) {
                                return Number(val / 100).toFixed(2);
                            }
                        },
                        {
                            title: '时间段',
                            align: 'center',
                            field: 'beginTime',
                            width:120,
                            formatter:function(val,row){
                                return row.beginDate +"<br>"+row.endDate;
                            }
                        },
                        {
                            title: '状态',
                            align: 'center',
                            field: 'statusName',
                            width: 60
                        },
                        {
                            title: '操作',
                            align: 'center',
                            field: 'id',
                            width: 60,
                            formatter: function (val, row) {
                                var html = '';
                                <@app.has_oper perm_code='hdg.CabinetDegreeInput:view'>
                                    html += '<a href="javascript:view(cabinetId,agentId)">查看记录</a>';
                                </@app.has_oper>
                                html = html.replace(/cabinetId/g, row.cabinetId);
                                return html.replace(/agentId/g, row.agentId);
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
            datagrid.datagrid('options').queryParams = {
                agentId: $('#agent_id').combotree('getValue'),
                cabinetName: $('#cabinetName').val(),
                cabinetId: $('#cabinetId').val(),
                beginTime: $('#beginTime').datebox('getValue'),
                endTime: $('#endTime').datebox('getValue')
            };

            datagrid.datagrid('load');
        }

        function view(cabinetId,agentId) {
            App.dialog.show({
                css: 'width:900px;height:600px;overflow:visible;',
                title: '查看记录',
                href: "${contextPath}/security/hdg/cabinet_degree_input/view.htm?cabinetId=" + cabinetId+'&agentId='+agentId
            });
        }

        function swich_agent() {
            var agentId = $('#agent_id').combotree('getValue');
            var estateComboTree = $('#estate_id');

            estateComboTree.combotree({
                url: "${contextPath}/security/hdg/estate/tree.htm?agentId=" + agentId + ""
            });
            estateComboTree.combotree('reload');
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
                            <td align="right">运营商：</td>
                            <td>
                                <input name="agentId" id="agent_id" class="easyui-combotree" editable="false"
                                       style="width: 200px;height: 28px;"
                                       data-options="url:'${contextPath}/security/basic/agent/tree.htm?agentId=${Session['SESSION_KEY_USER'].agentId}',
                                method:'get',
                                valueField:'id',
                                textField:'text',
                                editable:false,
                                multiple:false,
                                panelHeight:'200',
                                onLoadSuccess:function() {
                                    $('#agent_id').combotree('setValue', '${Session['SESSION_KEY_USER'].agentId}');
                                },
                                onClick: function(node) {

                                }
                            "
                                >&nbsp;&nbsp;&nbsp;&nbsp;
                            </td>
                            <td width="60" align="right">设备名称：</td>
                            <td>
                                <input type="hidden" id="agentId" value="">
                                <input type="text" class="text" id="cabinetName" />
                            </td>
                            <td width="60" align="right">设备编号：</td>
                            <td>
                                <input type="text" class="text" id="cabinetId"  />
                            </td>
                            <td width="60" align="right">时间段：</td>
                            <td>
                                <input type="text" class="easyui-datebox" id="beginTime"  style="width: 150px;height: 28px;"/>——
                                <input type="text" class="easyui-datebox" id="endTime"  style="width: 150px;height: 28px;"/>
                            </td>
                        </tr>
                    </table>
                </div>
                <div class="panel grid_wrap">
                    <div class="toolbar clearfix">
                        <#--<div class="float_right">
                            <@app.has_oper perm_code='hdg.estateUser:add'>
                                <button class="btn btn_green" onclick="add()">新建</button>
                            </@app.has_oper>
                        </div>-->
                        <h3>物业电费信息</h3>
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