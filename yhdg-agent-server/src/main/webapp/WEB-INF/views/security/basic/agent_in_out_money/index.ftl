<div class="tab_item" style="display: block">
    <div class="toolbar clearfix">
        <div class="float_right">

        </div>
        <h3>运营商流水信息</h3>
    </div>
    <div class="grid" style="height:345px;">
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
            url: "${contextPath}/security/basic/agent_in_out_money/page.htm?agentId=${agentId}",
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
                        title: '运营商',
                        align: 'center',
                        field: 'agentName',
                        width: 40
                    },
                    {
                        title: '业务类型',
                        align: 'center',
                        field: 'bizTypeName',
                        width: 70
                    },
                    {
                        title: '收入类型',
                        align: 'center',
                        field: 'typeName',
                        width: 40
                    },
                    {
                        title: '金额',
                        align: 'center',
                        field: 'money',
                        width: 40,
                        formatter:function(val) {
                            return val / 100;
                        }
                    },
                    {
                        title: '剩余金额',
                        align: 'center',
                        field: 'balance',
                        width: 40,
                        formatter:function(val) {
                            return val / 100;
                        }
                    },
                    {
                        title: '操作人',
                        align: 'center',
                        field: 'operator',
                        width: 40
                    },
                    {
                        title: '创建时间',
                        align: 'center',
                        field: 'createTime',
                        width: 75
                    },
                    {
                        title: '操作',
                        align: 'center',
                        field: 'action',
                        width: 40,
                        formatter: function(val, row) {
                            var html = '';
                            html += '<a href="javascript:view_record(ID)">查看</a>';
                            return html.replace(/ID/g, row.id);
                        }
                    }
                ]
            ]
        });
    })();

    function view_record(id) {
        App.dialog.show({
            css: 'width:350px;height:400px;',
            title: '查看',
            href: "${contextPath}/security/basic/agent_in_out_money/view.htm?id=" + id,
            event: {
                onClose: function() {
                }
            }
        });
    }

</script>