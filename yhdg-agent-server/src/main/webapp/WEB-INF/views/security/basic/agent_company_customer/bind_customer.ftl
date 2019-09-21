<div class="tab_item" style="display: block;">
    <div class="toolbar clearfix">
        <div class="float_right">
            <div class="float_right">
            <#if editFlag ?? && editFlag == 1>
                <button class="btn btn_blue" onclick="addBatch()">批量绑定</button>
            </#if>
            </div>
        </div>
        <h3>已绑定骑手</h3>
    </div>
    <div class="grid" style="height:425px;">
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
            url: "${contextPath}/security/basic/agent_company_customer/page.htm?agentCompanyId=${(agentCompanyId)!''}&agentId=${(agentId)!''}&unbindCompanyFlag=0",
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
                        title: '所属运营商',
                        align: 'center',
                        field: 'agentName',
                        width: 60
                    },
                    {
                        title: '运营公司名称',
                        align: 'center',
                        field: 'agentCompanyName',
                        width: 60
                    },
                    {title: '骑手姓名', align: 'center', field: 'customerFullname', width: 60},
                    {title: '骑手手机号', align: 'center', field: 'customerMobile', width: 60},
                    <#if editFlag ?? && editFlag == 1>
                    {
                        title: '操作',
                        align: 'center',
                        field: 'action',
                        width: 80,
                        formatter: function (val, row) {
                            var html = '<a href="javascript:remove_${pid}(AGENT_COMPANY_ID, CUSTOMER_ID)">删除</a>';
                            return html.replace(/AGENT_COMPANY_ID/g, row.agentCompanyId).replace(/CUSTOMER_ID/g, row.customerId);
                        }
                    }
                    </#if>
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
            href: "${contextPath}/security/basic/agent_company_customer/unbind_company.htm?agentCompanyId=${(agentCompanyId)!''}&agentId=${(agentId)!''}",
            windowData: {
                ok: function(rows) {
                    if(rows.length > 0) {
                        var customerIdList = [];
                        for(var i = 0; i < rows.length; i++) {
                            customerIdList.push(rows[i].customerId);
                        }
                        $.post('${contextPath}/security/basic/agent_company_customer/batch_bind_company.htm?agentCompanyId=${(agentCompanyId)!''}',{
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
                        $.messager.alert('提示信息', '请选择骑手', 'info');
                        return false;
                    }
                }
            }
        });
    }

    function remove_${pid}(agentCompanyId, customerId) {
        $.messager.confirm('提示信息', '确认解绑?', function (ok) {
            if(ok) {
                $.post('${contextPath}/security/basic/agent_company_customer/delete.htm', {
                    customerId: customerId,
                    agentCompanyId: agentCompanyId
                }, function(json) {
                    if(json.success) {
                        $.messager.alert('提示信息', '解绑成功', 'info');
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