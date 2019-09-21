<div class="popup_body">
    <div class="search">
        <div class="float_right">
            <button class="btn btn_yellow" id="query_${pid}">搜索</button>
        </div>
        <table cellpadding="0" cellspacing="0" border="0">
            <tr>
                <td align="right">客户名称：</td>
                <td><input type="text" class="text" id="fullname_${pid}"/>&nbsp;&nbsp;</td>
                <td align="right">手机号：</td>
                <td><input type="text" class="text" id="mobile_${pid}"/></td>
            </tr>
        </table>
    </div>
    <div style="width:800px; height:360px; padding-top: 6px;">
        <table id="page_table_${pid}">
        </table>
    </div>
</div>
<div class="popup_btn">
    <button class="btn btn_yellow" id="delete_${pid}">清空</button>
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
            url: "${contextPath}/security/hdg/battery_ride_order/page.htm?batteryId=${(batteryId)!''}",
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
                        title: '订单编号',
                        align: 'center',
                        field: 'id',
                        width: 30
                    },
                    {
                        title: '电池编号',
                        align: 'center',
                        field: 'batteryId',
                        width: 30
                    },
                    {
                        title: '骑行里程',
                        align: 'center',
                        field: 'currentDistance',
                        width: 25,
                        formatter: function (val, row) {
                            if(val>=1000){
                                return val/1000+"km"
                            }
                            return val+"m"
                        }
                    },
                    {
                        title: '客户姓名',
                        align: 'center',
                        field: 'customerFullname',
                        width: 20
                    },
                    {
                        title: '手机号',
                        align: 'center',
                        field: 'customerMobile',
                        width: 35
                    }
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

    function select_${pid}() {
        var win = $('#${pid}'), windowData = win.data('windowData');
        var datagrid = $('#page_table_${pid}');
        var order = datagrid.datagrid('getSelected');
        $.post("${contextPath}/security/hdg/battery_order_battery_report_log/find_all_map_count.htm", {orderId: order.id}, function (json) {
            if (!json.success) {
                $.messager.alert('提示信息', '该时段电池未上报信息', 'info');
                return;
            } else {
                if(order) {
                    windowData.ok({
                        order: order
                    });
                    win.window('close');
                } else {
                    $.messager.alert('提示信息', '请选择订单');
                }
            }
        }, 'json');
    }

    $('#close_${pid}').click(function() {
        $('#${pid}').window('close');
    });
    $('#query_${pid}').click(function() {
        queryOrder();
    });
    function queryOrder() {
        var datagrid = $('#page_table_${pid}');
        datagrid.datagrid('options').queryParams = {
            customerFullname: $('#fullname_${pid}').val(),
            customerMobile: $('#mobile_${pid}').val()
        };
        datagrid.datagrid('load');
    }

    $('#delete_${pid}').click(function() {
        var order = datagrid.datagrid('getSelected');
        order = {
            id: ""
        };
        windowData.ok({
            order:order
        });
        win.window('close');
    });
</script>

