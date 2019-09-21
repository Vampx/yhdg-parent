<@app.html>
    <@app.head>
    <style type="text/css">
        .zj_list .zj_item {
            position: relative;
            width: 120px;
            height: 8px;
            padding: 10px 0;
            float: left;
            text-align: center;
            margin: 0 10px 10px 0;
            background: #f5f5f5;
            border-radius: 4px;
        }
        .zj_list .zj_item.selected {
            color: #fff;
            background: #4263ff;
        }
    </style>
    <script>
        $.extend($.fn.validatebox.defaults.rules, {
            unique: {
                validator: function (value, param) {
                    var success;
                    $.ajax({
                        cache: false,
                        async: false,
                        type: 'POST',
                        url: '${contextPath}/security/zd/rent_installment_setting/unique.htm',
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
                },
                message: '该用户已配置过分期'
            }
        });

        $(function() {
            $('#page_table').datagrid({
                fit: true,
                width: '100%',
                height: '100%',
                striped: true,
                pagination: true,
                url: "${contextPath}/security/zd/rent_installment_setting/page.htm",
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
                            title: '所属运营商',
                            align: 'center',
                            field: 'agentName',
                            width: 60
                        },
                        {title: '手机号', align: 'center', field: 'mobile', width: 60},
                        {title: '姓名', align: 'center', field: 'fullname', width: 60},
                        {title: '最后分期时间', align: 'center', field: 'finalInstallmentTime', width: 60},
                        {title: '分期数', align: 'center', field: 'installmentNum', width: 60},
                        {title: '电池类型', align: 'center', field: 'batteryTypeName', width: 60},
                        {
                            title: '押金金额',
                            align: 'center',
                            field: 'foregiftMoney',
                            width: 50,
                            formatter: function(val) {
                                return Number(val / 100).toFixed(2);
                            }
                        },
                        {
                            title: '租金金额',
                            align: 'center',
                            field: 'packetMoney',
                            width: 50,
                            formatter: function(val) {
                                return Number(val / 100).toFixed(2);
                            }
                        },
                        {
                            title: '保险金额',
                            align: 'center',
                            field: 'insuranceMoney',
                            width: 50,
                            formatter: function(val) {
                                return Number(val / 100).toFixed(2);
                            }
                        },
                        {
                            title: '付款总金额',
                            align: 'center',
                            field: 'totalMoney',
                            width: 60,
                            formatter: function(val) {
                                return Number(val / 100).toFixed(2);
                            }
                        },
                        {
                            title: '已付期数',
                            align: 'center',
                            field: 'paidInstallmentNum',
                            width: 60
                        },
                        {
                            title: '已付金额',
                            align: 'center',
                            field: 'paidInstallmentMoney',
                            width: 60,
                            formatter: function(val) {
                                return Number(val / 100).toFixed(2);
                            }
                        },
                        {
                            title: '剩余金额',
                            align: 'center',
                            field: 'installmentRestMoney',
                            width: 60,
                            formatter: function(val) {
                                return Number(val / 100).toFixed(2);
                            }
                        },
                        {title: '创建时间', align: 'center', field: 'createTime', width: 60},
                        {
                            title: '操作',
                            align: 'center',
                            field: 'action',
                            width: 60,
                            formatter: function(val, row) {
                                var html = '';
                                <@app.has_oper perm_code='zd.RentInstallmentSetting:edit'>
                                    html += ' <a href="javascript:edit(ID)">修改</a>';
                                </@app.has_oper>
                                <#--<@app.has_oper perm_code='zd.RentInstallmentSetting:detail'>-->
                                    <#--html += ' <a href="javascript:detail_list(ID)">明细</a>';-->
                                <#--</@app.has_oper>-->
                                <@app.has_oper perm_code='zd.RentInstallmentSetting:remove'>
                                    html += ' <a href="javascript:remove(ID)">删除</a>';
                                </@app.has_oper>
                                return html.replace(/ID/g, row.id);
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
            var agentId = $('#agent_id').combotree('getValue');
            var fullname = $('#fullname').val();
            var mobile = $('#mobile').val();
            datagrid.datagrid('options').queryParams = {
                agentId: agentId,
                fullname: fullname,
                mobile: mobile
            };

            datagrid.datagrid('load');
        }

        function add() {
            App.dialog.show({
                css: 'width:839px;height:676px;',
                title: '新建',
                href: "${contextPath}/security/zd/rent_installment_setting/add.htm",
                event: {
                    onClose: function() {
                        query();
                    }
                }
            });
        }

        function edit(id) {
            App.dialog.show({
                css: 'width:839px;height:676px;',
                title: '修改',
                href: "${contextPath}/security/zd/rent_installment_setting/edit.htm?id=" + id ,
                event: {
                    onClose: function() {
                        query();
                    },
                    onLoad: function() {
                    }
                }
            });
        }

        function detail_list(id) {
            App.dialog.show({
                css: 'width:800px;height:480px;overflow:visible;',
                title: '分期明细',
                href: "${contextPath}/security/zd/rent_installment_detail/index.htm?settingId=" + id
            });
        }

        function remove(id) {
            $.messager.confirm('提示信息', '删除分期设置将会删除关联的分期明细，确认删除?', function(ok) {
                if(ok) {
                    $.post('${contextPath}/security/zd/rent_installment_setting/delete.htm', {
                        id: id
                    }, function (json) {
                        if (json.success) {
                            $.messager.alert('info', '操作成功', 'info');
                            query();
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

                <div class="panel search" >
                    <div class="float_right">
                        <button class="btn btn_yellow"onclick="query()">搜索</button>
                    </div>
                    <table cellpadding="0" cellspacing="0" border="0">
                        <tr>
                            <td align="right">运营商：</td>
                            <td>
                                <input id="agent_id" class="easyui-combotree" editable="true"
                                       style="width: 200px;height: 28px;"
                                       data-options="url:'${contextPath}/security/basic/agent/tree.htm?dummy=${'所有'?url}',
                                method:'get',
                                valueField:'id',
                                textField:'text',
                                editable:true,
                                multiple:false,
                                panelHeight:'200'
                            "
                                >&nbsp;&nbsp;
                            </td>
                            <td align="right">姓名：</td>
                            <td><input type="text" class="text" id="fullname"/>&nbsp;&nbsp;</td>
                            <td align="right">手机号：</td>
                            <td><input type="text" class="text" id="mobile"/>&nbsp;&nbsp;</td>
                        </tr>
                    </table>
                </div>
                <div class="panel grid_wrap">
                    <div class="toolbar clearfix">
                        <div class="float_right">
                            <@app.has_oper perm_code='zd.RentInstallmentSetting:add'>
                                <button class="btn btn_green" onclick="add()">新建</button>
                            </@app.has_oper>
                        </div>
                        <h3>分期设置</h3>
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





