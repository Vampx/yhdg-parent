<div class="popup_body">
    <div class="search">
        <div class="float_right">
            <button class="btn btn_yellow" id="query_${pid}">搜索</button>
        </div>
        <table cellpadding="0" cellspacing="0" border="0">
            <tr>
                <td align="right">运营商：</td>
                <td>
                    <input type="text" name="agentId" id="agent_id" class="easyui-combotree" editable="false"  style=" height: 28px;"
                           data-options="url:'${contextPath}/security/agent/tree.htm?dummy=${'无'?url}',
                                            method:'get',
                                            valueField:'id',
                                            textField:'text',
                                            editable:false,
                                            multiple:false,
                                            panelHeight:'auto',
                                            onClick: function(node) {
                                            }
                                        "
                            >&nbsp;
                </td>
                <td align="right" width="90">类型：</td>
                <td>
                    <select style="width:70px;" id="biz_type">
                        <option value="">所有</option>
                    <#list BizTypeEnum as e>
                        <option value="${e.getValue()}">${e.getName()}</option>
                    </#list>
                    </select>
                </td>
                <td align="right" width="60">状态：</td>
                <td>
                    <select style="width:70px;" id="status">
                        <option value="">所有</option>
                    <#list StatusEnum as e>
                        <option value="${e.getValue()}">${e.getName()}</option>
                    </#list>
                    </select>
                </td>

            </tr>
        </table>
    </div>
    <div style="width:800px; height:360px; padding-top: 6px;">
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
            url: "${contextPath}/security/basic/day_balance_record/page.htm",
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
                        title: 'checkbox', checkbox: true
                    },
                    {
                        title: 'id',
                        align: 'center',
                        field: 'id',
                        width: 40
                    },
                    {
                        title: '类型',
                        align: 'center',
                        field: 'bizType',
                        width: 40,
                        formatter: function(val){
                        <#list BizTypeEnum as e>
                            if(${e.getValue()} == val)
                            return '${e.getName()}';
                        </#list>
                        }
                    },
                    {
                        title: '运营商名称',
                        align: 'center',
                        field: 'agentName',
                        width: 40
                    },
                    {
                        title: '运营账号',
                        align: 'center',
                        field: 'agentMobile',
                        width: 40
                    },
                    {
                        title: '运营账户',
                        align: 'center',
                        field: 'agentAccountName',
                        width: 40
                    },
                    {title: '总收入', align: 'center', field: 'money', width: 40,
                        formatter: function(val) {
                            return Number(val / 100).toFixed(2);
                        }
                    },
                    {
                        title: '状态',
                        align: 'center',
                        field: 'status',
                        width: 30,
                        formatter: function(val){
                        <#list StatusEnum as e>
                            if(${e.getValue()} == val)
                            return '${e.getName()}';
                        </#list>
                        }
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
    var win = $('#${pid}'), windowData = win.data('windowData');
    var datagrid = $('#page_table_${pid}');
    function select_${pid}() {
        var balance = datagrid.datagrid('getSelected');
        if(balance) {
            balance = {

            };
            windowData.ok({
                balance: balance
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
        queryBalance();
    });
    function queryBalance() {
        datagrid.datagrid('options').queryParams = {
                agentId:$('#agent_id').val(),
                bizType: $('#biz_type').val(),
                status: $('#status').val()
        };
        datagrid.datagrid('load');
    }
</script>






