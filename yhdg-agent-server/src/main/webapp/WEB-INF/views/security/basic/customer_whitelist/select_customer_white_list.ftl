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
    <button class="btn btn_blue" id="ok_${pid}" onclick="update_white()">确定</button>
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
            url: "${contextPath}/security/basic/customer/page.htm",
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
                    {field: 'checkbox', checkbox: true},
                    {title: '平台',
                        align: 'center',
                        field: 'platformName',
                        width: 40
                    },
                    {
                        title: '客户名称',
                        align: 'center',
                        field: 'fullname',
                        width: 60
                    },
                    {title: '手机号',
                        align: 'center',
                        field: 'mobile',
                        width: 60
                    }
                ]
            ],
            queryParams: {
                isWhiteList: 0
            }
        });
    })();
    $('#close_${pid}').click(function() {
        $('#${pid}').window('close');
    });
    $('#query_${pid}').click(function() {
        queryCustomer();
    });
    function queryCustomer() {
        var datagrid = $('#page_table_${pid}');
        datagrid.datagrid('options').queryParams = {
            fullname: $('#fullname_${pid}').val(),
            mobile: $('#mobile_${pid}').val()
        };
        datagrid.datagrid('load');
    }
    function update_white() {
        var customers = $('#page_table_${pid}').datagrid('getChecked');
        if (customers.length > 0) {
            var ids = [];
            for (var j = 0; j < customers.length; j++) {
                ids.push(customers[j].id);
            }
            $.post('${contextPath}/security/basic/customer_white_list/update_white_list.htm', {
                ids: ids
            }, function (json) {
                $('#${pid}').window('close');
            }, 'json');
            return true;
        } else {
            $.messager.alert('提示信息', '请选择用户', 'info');
            return false;
        }
    }

</script>






