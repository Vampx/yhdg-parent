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
                url: "${contextPath}/security/hdg/shop_vehicle/page.htm",
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
                        { field: 'checkbox', checkbox: true },
                        {
                            title: '所属运营商',
                            align: 'center',
                            field: 'agentName',
                            width: 40
                        },
                        {
                            title: '所属门店',
                            align: 'center',
                            field: 'shopName',
                            width: 50
                        },
//                        {title: '客户名称', align: 'center', field: 'customerMobile', width: 40},
//                        {title: '客户电话', align: 'center', field: 'customerFullname', width: 40},
                        {title: '车辆编号', align: 'center', field: 'id', width: 40},
//                        {title: '车辆型号', align: 'center', field: 'modelCode', width: 40},
                        {title: '车型名称', align: 'center', field: 'modelName', width: 40},
                        {title: '电池类型', align: 'center', field: 'batteryTypeName', width: 40},
                        {title: '电池编号', align: 'center', field: 'batteryCode', width: 40},
//                        {
//                            title: '地理位置',
//                            align: 'center',
//                            field: 'address',
//                            width: 70,
//                            formatter: function (value) {
//                                if (value) {
//                                    return "<span title='" + value + "'>" + value + "</span>";
//                                } else {
//                                    return "";
//                                }
//                            }
//                        },
//                        {
//                            title: '在线',
//                            align: 'center',
//                            field: 'isOnline',
//                            width: 30,
//                            formatter: function (val, row) {
//                                return val == 1 ? '是' : '否';
//                            }
//                        },
                        {
                            title: '操作',
                            align: 'center',
                            field: 'action',
                            width: 30,
                            formatter: function(val, row) {
                                    var html = '';
                                    <@app.has_oper perm_code='4_1_2_3'>
                                        html += ' <a href="javascript:view(\'ID\')">查看</a>';
                                    </@app.has_oper>
                                    if (row.shopId != null) {
                                        <@app.has_oper perm_code='4_1_2_4'>
                                            html += ' <a href="javascript:remove(\'ID\')">解绑</a>';
                                        </@app.has_oper>
                                    }

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


        function swich_agent() {
            var agentId = $('#agent_id').combotree('getValue');
            var shopComboTree = $('#shop_id');

            shopComboTree.combotree({
                url: "${contextPath}/security/hdg/shop/tree.htm?agentId=" + agentId + ""
            });
            shopComboTree.combotree('reload');
        }

        function reload() {
            var datagrid = $('#page_table');
            datagrid.datagrid('reload');
        }

        function query() {
            var datagrid = $('#page_table');
            var agentId = $('#agent_id').combotree('getValue');
            var shopId = $('#shop_id').combotree('getValue');
            var batteryId = $('#battery_id').val();
            var modelName = $('#model_name').val();
            datagrid.datagrid('options').queryParams = {
                modelName: modelName,
                agentId: agentId,
                shopId: shopId,
                batteryId: batteryId
            };
            datagrid.datagrid('load');
        }

        function add() {
            App.dialog.show({
                css: 'width:795px;height:602px;overflow:visible;',
                title: '绑定车辆',
                href: "${contextPath}/security/hdg/shop_vehicle/add.htm"
            });
        }

        function view(id) {
            App.dialog.show({
                css: 'width:786px;height:515px;overflow:visible;',
                title: '查看',
                href: "${contextPath}/security/hdg/shop_vehicle/view.htm?id=" + id
            });
        }

        function remove(id) {
            $.messager.confirm('提示信息', '确认解绑?', function(ok) {
                if(ok) {
                    $.post('${contextPath}/security/hdg/shop_vehicle/cancel_binding.htm', {
                        id: id
                    }, function (json) {
                        if (json.success) {
                            $.messager.alert('提示消息', '解绑成功', 'info');
                            reload();
                        } else {
                            $.messager.alert('提示消息', json.message, 'info');
                        }
                    }, 'json');
                }
            });
        }

        function reloadTree() {
            var agentId = $('#agent_id').combotree('getValue');
            var datagrid = $('#page_table');
            datagrid.datagrid('options').queryParams = {
                agentId: agentId
            };
            datagrid.datagrid('reload');
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
                                <input name="agentId" id="agent_id" class="easyui-combotree" editable="false"
                                       style="width: 184px;height: 28px;"
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
                            <td width="70"  align="right">所属门店：</td>
                            <td>
                                <input name="shopId" id="shop_id" class="easyui-combotree" editable="false"
                                       style="width: 184px;height: 28px;"
                                       data-options="url:'${contextPath}/security/hdg/shop/tree.htm?dummy=${'所有'?url}',
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
                            <td align="right" width="70">车型名称：</td>
                            <td><input type="text" class="text" id="model_name"  /></td>
                            <#--<td align="right" width="70">电池类型：</td>-->
                            <#--<td>-->
                                <#--<select style="width:60px;" id="type">-->
                                    <#--<option value="">所有</option>-->
                                    <#--<#list batteryTypeList as c>-->
                                        <#--<option value="${(c.itemValue)!''}">${(c.itemName)!''}</option>-->
                                    <#--</#list>-->
                                <#--</select>-->
                            <#--</td>-->
                        </tr>
                    </table>
                </div>
                <div class="panel grid_wrap">
                    <div class="toolbar clearfix">
                        <div class="float_right">
                            <@app.has_oper perm_code='4_1_2_2'>
                                <button class="btn btn_green" onclick="add()">绑定车辆</button>
                            </@app.has_oper>
                        </div>
                        <h3>车辆信息</h3>
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





