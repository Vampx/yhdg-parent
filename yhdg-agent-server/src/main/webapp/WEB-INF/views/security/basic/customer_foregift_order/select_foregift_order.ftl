<div class="popup_body">
    <div class="search">
        <div class="float_right">
            <button class="btn btn_yellow" id="query_${pid}">搜索</button>
        </div>
        <table cellpadding="0" cellspacing="0" border="0">
            <tr>
                <td align="right">订单编号：</td>
                <td><input type="text" class="text"  id="id_${pid}"/>&nbsp;&nbsp;</td>
                <td align="right">客户名称：</td>
                <td><input type="text" class="text" id="fullname_${pid}"/>&nbsp;&nbsp;</td>
                <td align="right">手机号：</td>
                <td><input type="text" class="text"  id="mobile_${pid}"/>&nbsp;&nbsp;</td>
                <td align="right">业务类型：</td>
                <td>
                    <select class="easyui-combobox" id="foregift_type_${pid}" name="foregiftType" style="width:180px;height: 30px ">
                        <#list foregiftType as e>
                            <option value="${e.getValue()}">${e.getName()}</option>
                        </#list>
                    </select>
                </td>
            </tr>
        </table>
    </div>
    <div style="width:950px; height:360px; padding-top: 6px;">
        <table id="page_table_${pid}">
        </table>
    </div>
</div>
<div class="popup_btn">
    <button class="btn btn_yellow" id="delete_${pid}">清空</button>
    <button class="btn btn_border" id="close_${pid}">关闭</button>
</div>

<script>
    (function () {
        var win = $('#${pid}'), windowData = win.data('windowData');
        var datagrid = $('#page_table_${pid}');
        datagrid.datagrid({
            fit: true,
            width: '100%',
            height: '100%',
            striped: true,
            pagination: true,
            url: "${contextPath}/security/basic/customer_foregift_order/page.htm?status=${(payOk)!''}",
            pageSize: 10,
            pageList: [10, 50, 100],
            fitColumns: true,
            idField: 'id',
            singleSelect: true,
            selectOnCheck: false,
            checkOnSelect: false,
            autoRowHeight: false,
            rowStyler: gridRowStyler,
            columns: [
                [
                    {
                        title: 'checkbox', checkbox: true
                    },
                    {title: '订单编号', align: 'center', field: 'id', width: 80},
                    {
                        title: '客户名称',
                        align: 'center',
                        field: 'customerFullname',
                        width: 80
                    },
                    {title: '手机号', align: 'center', field: 'customerMobile', width: 80},
                    {title: '业务类型', align: 'center', field: 'foregiftName', width: 80},
                    {title: '支付状态', align: 'center', field: 'statusName', width: 80}
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
        var customerForegiftOrder = datagrid.datagrid('getSelected');
        if(customerForegiftOrder) {
            windowData.ok({
                customerForegiftOrder: customerForegiftOrder
            });
            win.window('close');
        } else {
            $.messager.alert('提示信息', '请选择订单');
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
            customerFullname: $('#fullname_${pid}').val(),
            customerMobile: $('#mobile_${pid}').val(),
            id: $('#id_${pid}').val(),
            foregiftType: $('#foregift_type_${pid}').combobox('getValue')
        };
        datagrid.datagrid('load');
    }

    $('#delete_${pid}').click(function() {
        var customerForegiftOrder = datagrid.datagrid('getSelected');
        customerForegiftOrder = {
            id: "",
            customerFullname: ""
        };
        windowData.ok({
            customerForegiftOrder:customerForegiftOrder
        });
        win.window('close');
    });
</script>