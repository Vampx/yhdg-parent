<@app.html>
    <@app.head>
    <script>
        $(function () {
            $('#page_table').datagrid({
                autoRowHeight: false,
                rowStyler: gridRowStyler,
                fit: true,
                striped: true,
                singleSelect: true,
                selectOnCheck: false,
                checkOnSelect: false,
                collapsible: true,
                pagination: true,
                url: "${contextPath}/security/basic/balance_record/page.htm?agentId=${Session['SESSION_KEY_USER'].agentId}",
                pageSize: 50,
                pageList: [50, 100],
                idField: 'id',
                frozenColumns: [[
                    {
                        title: 'checkbox', checkbox: true
                    },
                    {
                        title: '结算日期',
                        align: 'center',
                        field: 'balanceDate',
                        width: 80
                    },
                    {
                        title: '类型',
                        align: 'center',
                        field: 'bizType',
                        width: 80,
                        formatter: function (val) {
                            <#list BizTypeEnum as e>
                                if (${e.getValue()}== val
                            )
                                return '${e.getName()}';
                            </#list>
                        }
                    }
                ]],
                columns: [
                    [
                        {
                            title: '商户名称',
                            align: 'center',
                            field: 'partnerName',
                            width: 80
                        },
                        {
                            title: '运营商名称',
                            align: 'center',
                            field: 'agentName',
                            width: 80
                        },
                        {
                            title: '门店名称',
                            align: 'center',
                            field: 'shopName',
                            width: 80
                        },
                        {
                            title: '总收入', align: 'center', field: 'money', width: 80,
                            formatter: function (val) {
                                return Number(val / 100).toFixed(2);
                            }
                        },
                        {
                            title: '包时段收入', align: 'center', field: 'packetPeriodMoney', width: 80,
                            formatter: function (val, row) {
                                return '<a href="javascript:view_packet_period_money(' + row.id + ')"><u>' + Number(val / 100).toFixed(2) + '</u></a>';
                            }
                        },
                        {
                            title: '按次收入', align: 'center', field: 'exchangeMoney', width: 80,
                            formatter: function (val, row) {
                                return Number(val / 100).toFixed(2);
                            }
                        },
                        {
                            title: '保险收入', align: 'center', field: 'insuranceMoney', width: 80,
                            formatter: function (val, row) {
                                return Number(val / 100).toFixed(2);
                            }
                        },
                        {
                            title: '押金剩余金额', align: 'center', field: 'foregiftRemainMoney', width: 100,
                            formatter: function (val, row) {
                                return Number(val / 100).toFixed(2);
                            }
                        },
                        {
                            title: '包时段退款', align: 'center', field: 'refundPacketPeriodMoney', width: 80,
                            formatter: function (val, row) {
                                return Number(val / 100).toFixed(2);
                            }
                        },
                        {
                            title: '保险退款', align: 'center', field: 'refundInsuranceMoney', width: 80,
                            formatter: function (val, row) {
                                return Number(val / 100).toFixed(2);
                            }
                        },
                        {
                            title: '抵扣券', align: 'center', field: 'deductionTicketMoney', width: 80,
                            formatter: function (val, row) {
                                return Number(val / 100).toFixed(2);
                            }
                        },
                        {
                            title: '状态',
                            align: 'center',
                            field: 'status',
                            width: 80,
                            formatter: function (val) {
                                <#list StatusEnum as e>
                                    if (${e.getValue()}== val
                                )
                                    return '${e.getName()}';
                                </#list>
                            }
                        },
                        {
                            title: '确认时间',
                            align: 'center',
                            field: 'confirmTime',
                            width: 150
                        },
                    <#--{-->
                    <#--title: '操作',-->
                    <#--align: 'center',-->
                    <#--field: 'action',-->
                    <#--width: 80,-->
                    <#--formatter: function(val, row) {-->
                    <#--var html = '';-->
                    <#--&lt;#&ndash;<@app.has_oper perm_code='5_1_1_4'>&ndash;&gt;-->
                    <#--html += '<a href="javascript:view(ID)">查看明细</a>';-->
                    <#--&lt;#&ndash;</@app.has_oper>&ndash;&gt;-->
                    <#--return html.replace(/ID/g, row.id);-->
                    <#--}-->
                    <#--}-->
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

            var status = $('#status').val();
            var bizType = $('#biz_type').val();
            var shopName = $('#shopName').val();
            var balanceDate = $('#balanceDate').datebox('getValue');
            var category = $('#category').val();
            var queryParams = {
                bizType: bizType,
                status: status,
                shopName: shopName,
                balanceDate: balanceDate,
                category: category
            };

            datagrid.datagrid('options').queryParams = queryParams;
            datagrid.datagrid('load');
        }


        function viewOrder(id) {
            App.dialog.show({
                css: 'width:1200px;height:570px;overflow:visible;',
                title: '查看',
                href: "${contextPath}/security/basic/balance_transfer_order/view.htm?id=" + id
            });
        }


        function view(id) {
            App.dialog.show({
                css: 'width:860px;height:580px;overflow:visible;',
                title: '查看',
                href: "${contextPath}/security/basic/balance_record/view.htm?id=" + id
            });
        }

        function confirm_status() {
            $.messager.confirm('提示信息', '确认提交?', function (ok) {
                if (ok) {
                    var ids = [];
                    var rows = $('#page_table').datagrid('getChecked');
                    for (var i = 0; i < rows.length; i++) {
                        ids.push(rows[i].id);
                    }
                    if (ids.length == 0) {
                        alert('你没有选择任何内容');
                    } else {
                        $.post('${contextPath}/security/basic/balance_record/confirm_status.htm', {
                            ids: ids
                        }, function (json) {
                            if (json.success) {
                                $.messager.alert('提示消息', json.message, 'info');
                                reload();
                            } else {
                                $.messager.alert('提示消息', json.message, 'info');
                            }
                        }, 'json');
                    }
                }
            });
        }

        function edit_status() {
            $.messager.confirm('提示信息', '确认提交?', function (ok) {
                if (ok) {
                    var ids = [];
                    var rows = $('#page_table').datagrid('getChecked');
                    for (var i = 0; i < rows.length; i++) {
                        ids.push(rows[i].id);
                    }
                    if (ids.length == 0) {
                        alert('你没有选择任何内容');
                    } else {
                        $.post('${contextPath}/security/basic/balance_record/confirm.htm', {
                            ids: ids
                        }, function (json) {
                            if (json.success) {
                                $.messager.alert('info', '操作成功', 'info');
                                reload();
                            } else {
                                $.messager.alert('提示消息', json.message, 'info');
                            }
                        }, 'json');
                    }
                }
            });
        }


        function edit_status_offline() {
            $.messager.confirm('提示信息', '确认提交?', function (ok) {
                if (ok) {
                    var ids = [];
                    var rows = $('#page_table').datagrid('getChecked');
                    for (var i = 0; i < rows.length; i++) {
                        ids.push(rows[i].id);
                    }
                    if (ids.length == 0) {
                        alert('你没有选择任何内容');
                    } else {
                        $.post('${contextPath}/security/basic/balance_record/confirmStatus.htm', {
                            ids: ids
                        }, function (json) {
                            if (json.success) {
                                $.messager.alert('提示消息', json.message, 'info');
                                reload();
                            } else {
                                $.messager.alert('提示消息', json.message, 'info');
                            }
                        }, 'json');
                    }
                }
            });
        }

        function view_packet_period_money(id) {
            App.dialog.show({
                css: 'width:1050px;height:510px;overflow:visible;',
                title: '包时段收入',
                href: "${contextPath}/security/basic/balance_record/view_packet_period_money.htm?id=" + id +"&serviceType=1"
            });
        }

        function view_exchange_money(id) {
            App.dialog.show({
                css: 'width:1050px;height:510px;overflow:visible;',
                title: '按次收入',
                href: "${contextPath}/security/basic/balance_record/view_exchange_money.htm?id=" + id
            });
        }

        function view_insurance_money(id) {
            App.dialog.show({
                css: 'width:1050px;height:510px;overflow:visible;',
                title: '保险收入',
                href: "${contextPath}/security/basic/balance_record/view_insurance_money.htm?id=" + id
            });
        }

        function view_province_income(id) {
            App.dialog.show({
                css: 'width:1050px;height:510px;overflow:visible;',
                title: '省代收入',
                href: "${contextPath}/security/basic/balance_record/view_province_income.htm?id=" + id
            });
        }

        function view_city_income(id) {
            App.dialog.show({
                css: 'width:1050px;height:510px;overflow:visible;',
                title: '市代收入',
                href: "${contextPath}/security/basic/balance_record/view_city_income.htm?id=" + id
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
                            <#if Session['SESSION_KEY_USER'].agentId == 0>
                                <td width="60" align="right">运营商：</td>
                                <td>
                                    <input type="text" name="agentId" id="agent_id" class="easyui-combotree"
                                           editable="false" style=" height: 28px;width: 200px"
                                           data-options="url:'${contextPath}/security/basic/agent/tree.htm?dummy=${'所有'?url}',
                                            method:'get',
                                            valueField:'id',
                                            textField:'text',
                                            editable:false,
                                            multiple:false,
                                            panelHeight:'500',
                                            onClick: function(node) {
                                                 $('#agent').val(node.id);
                                                $('#demo').val('');
                                            }
                                        "
                                    >&nbsp;
                                </td>
                            </#if>
                            <td align="right" width="60">类型：</td>
                            <td>
                                <select style="width:70px;" id="biz_type">
                                    <option value="">所有</option>
                                    <#list BizTypeEnum as e>
                                        <option value="${e.getValue()}">${e.getName()}</option>
                                    </#list>
                                </select>
                            </td>
                            <td align="right" width="70">业务类型：</td>
                            <td>
                                <select style="width:80px;" id="category">
                                    <#list Category as e>
                                        <option value="${e.getValue()}" <#if defaultCategory?? && defaultCategory == e.getValue()>selected</#if> >${e.getName()}</option>
                                    </#list>
                                </select>
                            </td>
                            <td align="right" width="40">状态：</td>
                            <td>
                                <select style="width:70px;" id="status">
                                    <option value="">所有</option>
                                    <#list StatusEnum as e>
                                        <option value="${e.getValue()}">${e.getName()}</option>
                                    </#list>
                                </select>&nbsp;&nbsp;&nbsp;&nbsp;
                            </td>
                            <td align="right" width="60">门店名称：</td>
                            <td>
                                <input type="text" class="text" id="shopName" style="width: 150px;height: 28px;"/>&nbsp;&nbsp;&nbsp;&nbsp;
                            </td>
                            <td align="right">日期：</td>
                            <td><input type="text" class="easyui-datebox" id="balanceDate" style="width: 150px;height: 28px;"/>&nbsp;&nbsp;
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
