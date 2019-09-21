<div class="popup_body">
    <div class="search">
        <div class="float_right">
            <button class="btn btn_yellow" id="query_${pid}">搜索</button>
        </div>
        <table cellpadding="0" cellspacing="0" border="0">
            <tr>
                <td  width="60" align="right">运营商：</td>
                <td >
                    <input name="agentId" id="agent_id" class="easyui-combotree" editable="false" style="width: 170px;height: 28px;"
                           data-options="url:'${contextPath}/security/basic/agent/tree.htm?dummy=${'所有'?url}',
                                                            method:'get',
                                                            valueField:'id',
                                                            textField:'text',
                                                            editable:false,
                                                            multiple:false,
                                                            panelHeight:'200',
                                                            onClick: function(node) {
                                                               reloadTree();
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
                        <option value="customerMobile">手机号</option>
                        <option value="version">版本</option>
                    </select>
                </td>
                <td><input type="text" class="text" id="query_value"/></td>
                <td align="right">当前电量：</td>
                <td><input type="text"style="width:55px;" class="text"
                           id="min_volume"/>-
                    <input type="text"style="width:55px;" class="text"
                           id="max_volume"/></td>
                <td align="right" width="40">在线：</td>
                <td>
                    <select  id="is_online"  name="isOnline"
                             style="width:60px;">
                        <option value="">所有</option>
                        <option value="1">是</option>
                        <option value="0">否</option>
                    </select>
                </td>
                <td align="right">离柜时间：</td>
                <td>
                    <input id="begin_time_${pid}" class="easyui-datetimebox"
                           type="text"
                           style="width:150px;height:27px;">
                    -
                </td>
                <td>
                    <input id="end_time_${pid}" class="easyui-datetimebox" type="text"
                           style="width:150px;height:27px;">
                </td>
            </tr>
        </table>
    </div>
    <div style="width:1350px; height:360px; padding-top: 6px;">
        <table id="page_table_${pid}">
        </table>
    </div>
</div>
<div class="popup_btn">
    <button class="btn btn_border" id="close_${pid}">关闭</button>
</div>
<script>
    (function() {
        var win = $('#${pid}'), windowData = win.data('windowData');
        var datagrid = $('#page_table_${pid}');
        datagrid.datagrid({
            fit: true,
            width: '100%',
            height: '100%',
            striped: true,
            pagination: true,
            url: "${contextPath}/security/zd/battery/page.htm?agentId=${Session['SESSION_KEY_USER'].agentId}",
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
                        title: '电池编号',
                        align: 'center',
                        field: 'id',
                        width: 50
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
                            return row.statusName + '/' + row.chargeStatusName;
                        }

                    },
                    {
                        title: '上线状态',
                        align: 'center',
                        field: 'upLineStatusName',
                        width: 60
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
                        title: '电池类型',
                        align: 'center',
                        field: 'batteryType',
                        width: 60
                    },
                    {
                        title: '上报时间',
                        align: 'center',
                        field: 'reportTime',
                        width: 90
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
                    {title: '所在柜口', align: 'center', field: 'boxNum', width: 40}
                ]
            ],
            onLoadSuccess:function() {
                datagrid.datagrid('clearChecked');
                datagrid.datagrid('clearSelections');
            },onClickRow:  function(rowIndex, rowData) {
                select_${pid}(rowData.id);
            }
        });
    })();

    var win = $('#${pid}'), windowData = win.data('windowData');
    var datagrid = $('#page_table_${pid}');
    function select_${pid}() {
        var battery = datagrid.datagrid('getSelected');
        if(battery) {
            windowData.ok({
                battery: battery
            });
            win.window('close');
        } else {
            $.messager.alert('提示信息', '请选择一个电池');
        }
    }

    $('#close_${pid}').click(function() {
        $('#${pid}').window('close');
    });
    $('#query_${pid}').click(function() {
        query();
    });

    function query() {
        var datagrid = $('#page_table_${pid}');
        var queryName = $('#query_name').val();
        var queryValue = $('#query_value').val();
        var queryParams = {
            isOnline: $('#is_online').val(),
            minVolume: $('#min_volume').val(),
            maxVolume: $('#max_volume').val(),
            queryBeginTime: $('#begin_time_${pid}').datetimebox('getValue'),
            queryEndTime: $('#end_time_${pid}').datetimebox('getValue'),
            type: $('#type').val(),
            agentId: $('#agent_id').combotree('getValue'),
            id: $('#id').val()
        };
        queryParams[queryName] = queryValue;
        datagrid.datagrid('options').queryParams = queryParams;
        datagrid.datagrid('load');
    }
</script>

