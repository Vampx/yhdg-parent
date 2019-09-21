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
                url: "${contextPath}/security/hdg/shop_battery/page.htm",
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
                            title: 'checkbox', filed: 'id', checkbox: true
                        },
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
                        {
                            title: '电池编号',
                            align: 'center',
                            field: 'id',
                            width: 40
                        },
                        {
                            title: 'IMEI',
                            align: 'center',
                            field: 'code',
                            width: 80
                        },
                        {
                            title: '外壳编号',
                            align: 'center',
                            field: 'shellCode',
                            width: 60
                        },
                        {
                            title: '状态',
                            align: 'center',
                            field: 'statusName',
                            width: 60,
                            formatter: function (val, row) {
//                                return row.statusName + '/' + row.chargeStatusName;
                                return  row.chargeStatusName;
                            }

                        },
                        {
                            title: '当前电量',
                            align: 'center',
                            field: 'volume',
                            width: 50
                        },
                        {
                            title: '版本',
                            align: 'center',
                            field: 'version',
                            width: 30
                        },
                        {
                            title: '在线',
                            align: 'center',
                            field: 'isOnline',
                            width: 30,
                            formatter: function (val, row) {
                                return val == 1 ? '<a style="color: #00FF00;">是</a>' : '<a style="color: #ff0000;">否</a>';
                            }
                        },
                        {
                            title: '电池',
                            align: 'center',
                            field: 'batteryType',
                            width: 30
                        },
                        {
                            title: '换电次数',
                            align: 'center',
                            field: 'exchangeAmount',
                            width: 40
                        },
                        {
                            title: '上报时间',
                            align: 'center',
                            field: 'reportTime',
                            width: 60
                        },
                        {
                            title: '当前信号',
                            align: 'center',
                            field: 'currentSignal',
                            width: 40
                        },
                        {
                            title: '品牌',
                            align: 'center',
                            field: 'brandName',
                            width: 40
                        },
                        {title: '客户姓名', align: 'center', field: 'customerFullname', width: 40},
                        {title: '客户手机', align: 'center', field: 'customerMobile', width: 40},
                        {title: '所在站点', align: 'center', field: 'cabinetName', width: 40},
                        {title: '所在柜口', align: 'center', field: 'boxNum', width: 40},
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
            var shopId = $('#shop_id').combotree('getValue');
            var queryName = $('#query_name').val();
            var queryValue = $('#query_value').val();
            var chargeStatus = $('#charge_status').val();
            var queryParams = {
                agentId: agentId,
                shopId: shopId,
                chargeStatus: chargeStatus,
                type: $('#type').val()
            };
            queryParams[queryName] = queryValue;
            datagrid.datagrid('options').queryParams = queryParams;
            datagrid.datagrid('load');
        }
        function swich_agent() {
            var agentId = $('#agent_id').combotree('getValue');
            var shopComboTree = $('#shop_id');

            shopComboTree.combotree({
                url: "${contextPath}/security/hdg/shop/tree.htm?agentId=" + agentId + ""
            });
            shopComboTree.combotree('reload');
        }

        function view(id) {
            App.dialog.show({
                css: 'width:780px;height:520px;overflow:visible;',
                title: '查看',
                href: "${contextPath}/security/hdg/battery/view.htm?id=" + id
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
                    <table cellpadding="0" cellspacing="0" border="0" style="border-collapse:separate; border-spacing: 0px 3px;">
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
                            <td align="right" width="85">
                                <select style="width:80px;" id="query_name">
                                    <option value="id">电池编号</option>
                                    <option value="simMemo">SIM卡</option>
                                    <option value="code">IMEI</option>
                                    <option value="shellCode">外壳编号</option>
                                    <option value="qrcode">二维码</option>
                                </select>
                            </td>
                            <td><input type="text" class="text" id="query_value"/></td>
                            <td align="right" width="40">型号：</td>
                            <td>
                                <select style="width:60px;" id="type">
                                    <option value="">所有</option>
                                    <#list batteryTypeList as c>
                                        <option value="${(c.itemValue)!''}">${(c.itemName)!''}</option>
                                    </#list>
                                </select>
                            </td>
                            <td align="right" width="40">状态：</td>
                            <td>
                                <select style="width:60px;" id="charge_status">
                                    <option value="">所有</option>
                                    <#list batteryChargeStatusList as c>
                                        <option value="${(c.getValue())!''}">${(c.getName())!''}</option>
                                    </#list>
                                </select>
                            </td>
                        </tr>
                    </table>
                </div>
                <div class="panel grid_wrap" style="margin-top: 30px;">
                    <div class="toolbar clearfix">
                        <div class="float_right">
                        </div>
                        <h3>电池信息列表</h3>
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