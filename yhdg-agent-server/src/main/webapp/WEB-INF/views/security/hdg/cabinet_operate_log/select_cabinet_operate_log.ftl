<div class="popup_body">
    <div class="search">
        <div class="float_right">
            <button class="btn btn_yellow" id="query_${pid}">搜索</button>
        </div>
        <table cellpadding="0" cellspacing="0" border="0">
            <tr>
                <td align="right" style="width: 80px">换电柜名称：</td>
                <td><input id="cabinetName_${pid}" type="text" class="text"/></td>
                <td align="right" style="width: 80px">箱号：</td>
                <td><input id="boxNum_${pid}" type="text" class="text"/></td>
                <td align="right" width="70">操作类型：</td>
                <td>
                    <select style="width:70px;" id="operate_type">
                        <option value="">所有</option>
                    <#list operateTypeEnum as e>
                        <option value="${e.getValue()}">${e.getName()}</option>
                    </#list>
                    </select>
                </td>
                <td align="right" width="70">日志类型：</td>
                <td>
                    <select style="width:70px;" id="operator_type">
                        <option value="">所有</option>
                    <#list operatorTypeEnum as e>
                        <option value="${e.getValue()}">${e.getName()}</option>
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
            url: "${contextPath}/security/hdg/cabinet_operate_log/page.htm?agentId=${Session['SESSION_KEY_USER'].agentId}",
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
                        title: '换电柜名称',
                        align: 'center',
                        field: 'cabinetName',
                        width: 40
                    },
                    {
                        title: '日志内容',
                        align: 'center',
                        field: 'content',
                        width: 80,
                        formatter : function(value, row) {
                            return '<span title="'+row.content+'">' + value + '</span>';
                        }
                    },
                    {
                        title: '箱号',
                        align: 'center',
                        field: 'boxNum',
                        width: 30
                    },
                    {
                        title: '操作类型',
                        align: 'center',
                        field: 'operateTypeName',
                        width: 30
                    },
                    {
                        title: '操作人',
                        align: 'center',
                        field: 'operator',
                        width: 30
                    },
                    {
                        title: '日志类型',
                        align: 'center',
                        field: 'operatorTypeName',
                        width: 30
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
                        <@app.has_oper perm_code='2_3_5_2'>
                            html += '<a href="javascript:view_log(ID)">查看</a>';
                        </@app.has_oper>
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
            var boxNum = $('#boxNum_${pid}').val();
            var operateType = $('#operate_type').val();
            var operatorType = $('#operator_type').val();
            datagrid.datagrid('options').queryParams = {
                cabinetName: cabinetName,
                boxNum: boxNum,
                operateType: operateType,
                operatorType: operatorType
            };
            datagrid.datagrid('load');
        }
    })();

</script>


