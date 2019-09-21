<div class="tab_item" style="display: block">

    <div class="toolbar clearfix">
        <div class="float_right">
            <button class="btn btn_yellow" onclick="query_${pid}()">搜索</button>
        </div>
        <table cellpadding="0" cellspacing="0" border="0">
            <tr>
                <td align="right" width="70">状态：</td>
                <td>
                    <select style="width:120px;" id="status_${pid}">
                        <option value="">所有</option>
                    <#list StatusEnum as e>
                        <option value="${e.getValue()}">${e.getName()}</option>
                    </#list>
                    </select>
                </td>
            </tr>
        </table>
    </div>
    <div class="grid" style="height:440px;">
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
            url: "${contextPath}/security/hdg/battery_parameter_log/page.htm?batteryId=${batteryId}",
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
                        title: '参数名称',
                        align: 'center',
                        field: 'paramName',
                        width: 60
                    },
                    {
                        title: '旧值',
                        align: 'center',
                        field: 'oldValue',
                        width: 40
                    },
                    {
                        title: '新值',
                        align: 'center',
                        field: 'newValue',
                        width: 40
                    },
                    {
                        title: '状态',
                        align: 'center',
                        field: 'statusName',
                        width: 40
                    },
                    {
                        title: '创建时间',
                        align: 'center',
                        field: 'createTime',
                        width: 80
                    },
                    {
                        title: '操作人',
                        align: 'center',
                        field: 'operator',
                        width: 30
                    },
                    {
                        title: '操作',
                        align: 'center',
                        field: 'action',
                        width: 30,
                        formatter: function(val, row) {
                            var html = '';
                            html += '<a href="javascript:view_${pid}(ID)">查看</a>';
                            return html.replace(/ID/g, row.id);
                        }
                    }
                ]
            ]
        });
    })();

    function query_${pid}() {
        var datagrid = $('#page_table_box_${pid}');
        var status = $('#status_${pid}').val();
        datagrid.datagrid('options').queryParams = {
            status: status
        };
        datagrid.datagrid('load');
    }

    function view_${pid}(id) {
        App.dialog.show({
            css: 'width:550px;height:420px;overflow:visible;',
            title: '查看',
            href: "${contextPath}/security/hdg/battery_parameter_log/view.htm?id=" + id
        });
    }

    var win = $('#${pid}');
    var ok = function () {
        var success = true;
        return success;
    };
    win.data('ok', ok);

</script>