<div class="popup_body">
    <div class="search">
        <div class="float_right">
            <button class="btn btn_yellow" id="query_${pid}">搜索</button>
        </div>
        <table cellpadding="0" cellspacing="0" border="0">
            <tr>
                <td align="right" style="width: 80px">换电柜编号：</td>
                <td><input id="cabinetId_${pid}" type="text" class="text"/></td>
                <td align="right" style="width: 80px">换电柜名称：</td>
                <td><input id="cabinetName_${pid}" type="text" class="text"/></td>
                <td align="right" style="width: 80px">状态：</td>
                <td>
                    <select name="StatusEnum" id="status_${pid}" class="easyui-combobox"
                            editable="false"
                            style="width: 150px; height: 28px;">
                        <option value="" >所有</option>
                    <#list StatusEnum as e>
                        <option value="${e.getValue()}" >${e.getName()}</option>
                    </#list>
                    </select>
                </td>
            </tr>
        </table>
    </div>
    <div style="width:960px; height:520px; padding-top: 6px;">
        <table id="page_table_${pid}">
        </table>
    </div>
</div>
<div class="popup_btn">
    <button class="btn btn_border" id="close_${pid}">关闭</button>
</div>
<script>
    (function() {

        var datagrid = $('#page_table_${pid}');
        datagrid.datagrid({
            fit: true,
            width: '100%',
            height: '100%',
            striped: true,
            pagination: true,
            url: "${contextPath}/security/hdg/cabinet_battery_stats/page.htm?agentId=${Session['SESSION_KEY_USER'].agentId}",
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
                        title: '运营商',
                        align: 'center',
                        field: 'agentName',
                        width: 40
                    },
                    {
                        title: '换电柜编号',
                        align: 'center',
                        field: 'cabinetId',
                        width: 40
                    },
                    {
                        title: '换电柜名称',
                        align: 'center',
                        field: 'cabinetName',
                        width: 40
                    },
                    {
                        title: '箱门电池信息',
                        align: 'center',
                        field: 'boxBatteryMessage',
                        width: 100,
                        formatter : function(value, row) {
                            return '<span title="'+row.content+'">' + value + '</span>';
                        }
                    },
                    {
                        title: '电池数',
                        align: 'center',
                        field: 'batteryNum',
                        width: 30
                    },
                    {
                        title: '状态',
                        align: 'center',
                        field: 'status',
                        width: 30,
                        formatter:function (val) {
                            if (val == 1) {
                                return "正常";
                            }else if (val == 2) {
                                return "异常";
                            }
                        }
                    },
                    {
                        title: '操作时间',
                        align: 'center',
                        field: 'createTime',
                        width: 50
                    },
                    {
                        title: '操作',
                        align: 'center',
                        field: 'action',
                        width: 30,
                        formatter: function(val, row) {
                            var html = '';
                            html += '<a href="javascript:view_battery_stats(ID)">查看</a>';
                            return html.replace(/ID/g, row.id);
                        }
                    }
                ]
            ]
        });
        $('#close_${pid}').click(function() {
            $('#${pid}').window('close');
        });
        function reload() {
            var datagrid = $('#page_table_${pid}');
            datagrid.datagrid('reload');
        }
        $('#query_${pid}').click(function() {
            query();
        });
        function query() {
            var datagrid = $('#page_table_${pid}');
            var cabinetName = $('#cabinetName_${pid}').val();
            var cabinerId = $('#cabinetId_${pid}').val();
            var status = $('#status_${pid}').combobox('getValue');
            datagrid.datagrid('options').queryParams = {
                agentId: ${Session['SESSION_KEY_USER'].agentId},
                cabinetName: cabinetName,
                status: status,
                cabinetId: cabinerId
            };
            datagrid.datagrid('load');
        }
    })();

</script>


