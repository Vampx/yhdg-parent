<div class="tab_item" style="display: block;">
    <div class="toolbar clearfix">
        <div class="float_right">
            <div class="float_right">
                <button class="btn btn_blue" onclick="addBatch()">批量新建</button>
            </div>
        </div>
        <h3>白名单客户</h3>
    </div>
    <div class="grid" style="height:325px;">
        <table id="page_table_box_${pid}"></table>
    </div>
</div>

<script>

    (function () {
        $('#page_table_box_${pid}').datagrid({
            fit: true,
            width: '100%',
            height: '100%',
            striped: true,
            pagination: true,
            url: "${contextPath}/security/hdg/exchange_whitelist/page.htm?batteryType=${(batteryType)!''}&agentId=${(agentId)!''}",
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
                        title: '客户姓名',
                        align: 'center',
                        field: 'fullname',
                        width: 80
                    },
                    {
                        title: '手机号',
                        align: 'center',
                        field: 'mobile',
                        width: 80
                    },
                    {
                        title: '操作',
                        align: 'center',
                        field: 'action',
                        width: 80,
                        formatter: function (val, row) {
                            var html = '<a href="javascript:remove_${pid}(EXCHANGE_WHITELIST_ID)">删除</a>';
                            return html.replace(/EXCHANGE_WHITELIST_ID/g, row.id);
                       }
                    }
                ]
            ]
        });
    })();
    function reload_${pid}() {
        var datagrid = $('#page_table_box_${pid}');
        datagrid.datagrid('reload');
    }

    function addBatch() {
        App.dialog.show({
            css: 'width:680px;height:530px;',
            title: '新建',
            href: "${contextPath}/security/basic/customer/select_whitelist_customer.htm",
            windowData: {
                ok: function(rows) {
                    if(rows.length > 0) {
                        var customerIdList = [];
                        for(var i = 0; i < rows.length; i++) {
                            customerIdList.push(rows[i].customerId);
                        }
                        $.post('${contextPath}/security/hdg/exchange_whitelist/batch_create_whitelist_customer.htm?batteryType=${(batteryType)!''}&agentId=${(agentId)!''}',{
                            customerIdList: customerIdList
                        }, function(json) {
                            if(json.success) {
                                $.messager.alert('提示信息', json.message, 'info');
                                reload_${pid}();
                            }else{
                                $.messager.alert('提示消息', json.message, 'info');
                            }
                        }, 'json');
                        return true;
                    }else {
                        $.messager.alert('提示信息', '请选择客户', 'info');
                        return false;
                    }
                }
            }
        });
    }

    function remove_${pid}(exchangeWhiteListId) {
        $.messager.confirm('提示信息', '确认删除?', function (ok) {
            if(ok) {
                $.post('${contextPath}/security/hdg/exchange_whitelist/delete_whitelist_customer.htm', {
                    exchangeWhiteListId: exchangeWhiteListId
                }, function(json) {
                    if(json.success) {
                        $.messager.alert('提示信息', '删除成功', 'info');
                        reload_${pid}();
                    }else {
                        $.messager.alert('提示信息', json.message, 'info');
                    }
                }, 'json');
            }
        })
    }

    $('#${pid}').data('ok', function() {
        return true;
    });

</script>