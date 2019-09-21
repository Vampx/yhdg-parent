<div class="tab_item" style="display: block">
    <div class="toolbar clearfix">
        <div class="float_right">

        </div>

    </div>
    <div class="grid" style="height:345px;">
        <table id="page_table_box_${pid}"></table>
    </div>
</div>
<div class="popup_btn popup_btn_full">
    <button class="btn btn_red ok">确定</button>
    <button class="btn btn_border close">关闭</button>
</div>
<script>

    (function () {
        $('#page_table_box_${pid}').datagrid({
            fit: true,
            width: '100%',
            height: '100%',
            striped: true,
            pagination: true,
            url: "${contextPath}/security/basic/day_balance_record/page.htm?status=${status}&agentId=${agent}&bizType=${bizType}",
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
                    {
                        title: '结算日期',
                        align: 'center',
                        field: 'balanceDate',
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
                        width: 50,
                        formatter: function(val){
                        <#list StatusEnum as e>
                            if(${e.getValue()} == val)
                            return '${e.getName()}';
                        </#list>
                        }
                    }
                ]
            ]
        });
    })();

    (function() {
        var pid = '${pid}',
                win = $('#' + pid),
                windowData = win.data('windowData');
        win.find('button.ok').click(function() {
            var checked =  $('#page_table_box_${pid}').datagrid('getChecked');
            if(checked.length > 0) {
                windowData.ok(checked);
                win.window('close');
            } else {
                $.messager.alert('提示信息', '请选择数据', 'info');
                return false;
            }
        });
        win.find('button.close').click(function() {
            win.window('close');
        });
    })()

</script>