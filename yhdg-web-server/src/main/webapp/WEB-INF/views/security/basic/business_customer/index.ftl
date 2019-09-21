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
                validator: function (value) {
                    if (value != "") {
                        return /^1\d{10}$/.test(value);
                    }

                },
                message: '请正确输入手机号码'
            },
            unique: {
                validator: function (value) {
                    var success;
                    $.ajax({
                        cache: false,
                        async: false,
                        type: 'POST',
                        url: '${contextPath}/security/basic/business_customer/unique.htm',
                        data: {
                            mobile: value
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
                },
                message: '手机号重复'
            }
        });

        $(function () {
            $('#page_table').datagrid({

                fit: true,
                width: '100%',
                height: '100%',
                striped: true,
                pagination: true,
                url: "${contextPath}/security/basic/business_customer/page.htm?type=1",
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
                        {title: '运营商', align: 'center', field: 'agentName', width: 50},
                        {
                            title: '站点公司',
                            align: 'center',
                            field: 'cabinetCompanyName',
                            width: 50
                        },
                        {
                            title: '手机',
                            align: 'center',
                            field: 'mobile',
                            width: 50,
                            formatter: function (val) {
                                return val;
                            }
                        },
                        {title: '姓名', align: 'center', field: 'fullname', width: 30},
                        {
                            title: '身份证',
                            align: 'center',
                            field: 'idCard',
                            width: 60
                        },
                        {title: '单位', align: 'center', field: 'companyName', width: 40},
                        {
                            title: '余额(元)',
                            align: 'center',
                            field: 'agentBalance',
                            width: 40,
                            formatter: function(val) {
                                return Number(val / 100).toFixed(2);
                            }
                        },
                        {title: '绑定电池', align: 'center', field: 'batteryId', width: 40},
                        {title: '进入站点时间', align: 'center', field: 'cabinetBindingTime', width: 70},
                        {
                            title: '操作',
                            align: 'center',
                            field: 'id',
                            width: 80,
                            formatter: function (val, row) {
                                var html = '';
                                <@app.has_oper perm_code='3_2_3_2'>
                                    html += ' <a href="javascript:resignation(ID)">提交离职</a>';
                                </@app.has_oper>
                                return html.replace(/ID/g, row.id);
                            }
                        }
                    ]
                ],
                onLoadSuccess: function () {
                    $('#page_table').datagrid('clearChecked');
                    $('#page_table').datagrid('clearSelections');
                }, onDblClickRow: function (rowIndex, rowData) {
                    view(rowData.id);
                }
            });
        });


        function reload() {
            var datagrid = $('#page_table');
            datagrid.datagrid('reload');

        }

        function query() {
            var datagrid = $('#page_table');
            var agentId = $('#agent_id').combotree('getValue');
            var cabinetCompanyId = $('#cabinetCompanyId').combobox('getValue');
            var fullname = $('#full_name').val();
            var mobile = $('#mobile').val();
            var queryParams = {
                agentId: agentId,
                cabinetCompanyId:cabinetCompanyId,
                fullname: fullname,
                mobile: mobile
            };
            datagrid.datagrid('options').queryParams = queryParams;
            datagrid.datagrid('load');
        }

        function resignation(id) {
            $.messager.confirm('提示信息', '确认离职?', function (ok) {
                if (ok) {
                    $.post("${contextPath}/security/basic/business_customer/resignation.htm?id=" + id, function (json) {
                        if (json.success) {
                            $.messager.alert('提示消息', '操作成功', 'info');
                            reload();
                        } else {
                            $.messager.alert('提示消息', json.message, 'info');
                        }
                    }, 'json');
                }
            });
        }

//        function reloadTree() {
//            var companyId = $('#company_id').combotree('getValue');
//            var datagrid = $('#page_table');
//            datagrid.datagrid('options').queryParams = {
//                cabinetCompanyId: companyId
//            };
//            datagrid.datagrid('reload');
//        }
        function swich_agent() {
            var agentId = $('#agent_id').combotree('getValue');
            var cabinetCompanyIdBox = $('#cabinetCompanyId');
            cabinetCompanyIdBox.combobox({
                url: "${contextPath}/security/hdg/cabinet_company/cabinet_company_list.htm?agentId=" + agentId + ""
            });
            cabinetCompanyIdBox.combotree('reload');
        };

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
                                <input id="agent_id" class="easyui-combotree" editable="false"
                                       style="width: 200px;height: 28px;"
                                       data-options="url:'${contextPath}/security/basic/agent/tree.htm?dummy=${'所有'?url}',
                                method:'get',
                                valueField:'id',
                                textField:'text',
                                editable:false,
                                multiple:false,
                                panelHeight:'200',
                                onClick: function(node) {
                                   swich_agent();
                                }
                            "
                                >
                            </td>
                            <td width="70"  align="right">站点公司：</td>
                            <td>
                                <select name="cabinetCompanyId" id="cabinetCompanyId" style="height: 28px;width: 185px;" class="easyui-combobox"  editable="false"
                                        data-options="url:'${contextPath}/security/hdg/cabinet_company/cabinet_company_list.htm',
                                                    method:'get',
                                                    valueField:'id',
                                                    textField:'companyName',
                                                    editable:false,
                                                    multiple:false,
                                                    panelHeight:'200',
                                                     onLoadSuccess:function() {
                                                        <#--$('#cabinetCompanyId').combobox('setValue', '${(cabinetCompanyId)!''}');-->
                                                     },
                                                    onSelect: function(node) {

                                                    }"
                                >
                                </select>
                            </td>
                            <td align="right" width="60">手机号：</td>
                            <td><input type="text" class="text"  id="mobile"/></td>
                            <td align="right" width="60">姓名：</td>
                            <td><input type="text" class="text" id="full_name"/>&nbsp;&nbsp;</td>
                        </tr>
                    </table>
                </div>
                <div class="panel grid_wrap">
                    <div class="toolbar clearfix">
                        <div class="float_right">
                        </div>
                        <h3>骑手列表</h3>
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