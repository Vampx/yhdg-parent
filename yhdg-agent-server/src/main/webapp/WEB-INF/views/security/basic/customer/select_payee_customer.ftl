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
            url: "${contextPath}/security/basic/customer/payee_page.htm",
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
                        title: '客户名称',
                        align: 'center',
                        field: 'fullname',
                        width: 60
                    },
                    {title: '手机号', align: 'center', field: 'mobile', width: 60},
                   /* {title: '卡号', align: 'center', field: 'icCard', hidden:'true', width: 60},
                    {
                        title: '余额(元)',
                        align: 'center',
                        field: 'balance',
                        width: 60,
                        formatter: function(val) {
                            return Number(val / 100).toFixed(2);
                        }
                    }*/
                ]
            ],
            onLoadSuccess:function() {
                datagrid.datagrid('clearChecked');
                datagrid.datagrid('clearSelections');
            },onClickRow:  function(rowIndex, rowData) {
                select_${pid}(rowData.id);
            },
            queryParams: {
                fullname: "null",
                mobile: "null"
            }
        });
    })();
    var win = $('#${pid}'), windowData = win.data('windowData');
    var datagrid = $('#page_table_${pid}');
    function select_${pid}() {
        var customer = datagrid.datagrid('getSelected');
        if(customer) {
            windowData.ok({
                customer: customer
            });
            win.window('close');
        } else {
            $.messager.alert('提示信息', '请选择用户');
        }
    }

    $('#close_${pid}').click(function() {
        $('#${pid}').window('close');
    });
    $('#query_${pid}').click(function() {
        queryCustomer();
    });
    function queryCustomer() {
        datagrid.datagrid('options').queryParams = {
            fullname: $('#fullname_${pid}').val(),
            mobile: $('#mobile_${pid}').val()
        };
        datagrid.datagrid('load');
    }

    $('#delete_${pid}').click(function() {
        var customer = datagrid.datagrid('getSelected');
        customer = {
            id: "",
            fullname: "",
            mpOpenId: ""
        };
        windowData.ok({
            customer:customer
        });
        win.window('close');
    });
</script>






