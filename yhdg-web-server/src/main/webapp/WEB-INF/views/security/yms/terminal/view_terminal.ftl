<div class="tab_item" style="display: block">
    <div class="toolbar clearfix">
        <h3>车辆列表</h3>
    </div>
    <div class="grid" style="height:400px;">
        <table id="page_table_box_${pid}"></table>
    </div>
</div>

<script>
    $(function() {
        $('#page_table_box_${pid}').datagrid({
            fit: true,
            width: '100%',
            height: '100%',
            striped: true,
            pagination: true,
            url: "${contextPath}/security/terminal/page.htm?routeId=" + ${routeId},
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
                    {title: 'Id', align: 'center', field: 'id', width: 30},
                    {title: '线路名称', align: 'center', field: 'routeName', width: 30},
                    {title: '名称', align: 'center', field: 'terminalName', width: 30},
                    {title: '车牌', align: 'center', field: 'plateNum', width: 30},
                    {title: '版本', align: 'center', field: 'version', width: 30},
                    {title: '策略', align: 'center', field: 'strategyName', width: 30},
                    {title: '报站分组', align: 'center', field: 'routeGroupName', width: 30},
                    {title: '日志级别', align: 'center', field: 'logLevel', width: 20,
                        formatter:function(val,row){
                        <#list LogLevelEnum as logLevel>
                            if(val == '${logLevel.getValue()}')
                                return '${logLevel.getName()}';
                        </#list>
                        }
                    },
                    {title: '线路设置', align: 'center', field: 'routeSetting', width: 30,
                        formatter:function(val,row){
                        <#list routeSettingEnum as routeSetting>
                            if(val == '${routeSetting.getValue()}')
                                return '${routeSetting.getName()}';
                        </#list>
                        }
                    },
                    {
                        title: '操作',
                        align: 'center',
                        field: 'action',
                        width: 40,
                        formatter: function(val, row) {
                            var html = '<a href="javascript:view(\'ID\')">查看</a>';
                            return html.replace(/ID/g, row.id);
                        }
                    }
                ]
            ],
            queryParams: {
                agentId: $('#agent_id').combotree('getValue'),
                descendant: $('#descendant').combotree('getValue')
            },
            onLoadSuccess:function() {
                $('#page_table').datagrid('clearChecked');
                $('#page_table').datagrid('clearSelections');
            }
        });
    })



    function reload() {
        var datagrid = $('#page_table');
        datagrid.datagrid('reload');
    }

    function query() {
        var datagrid = $('#page_table');
        var queryName = $('#query_name').val();
        var queryValue = $('#query_value').val();
        var agentId = $('#agent_id').combotree("getValue");
        var descendant = $('#descendant').combotree("getValue");
        var isOnline = $('#is_online').combobox('getValue');
        var routeId = $('#route_id').combobox('getValue');

        var queryParams = {
            agentId: agentId,
            descendant: descendant,
            routeId: routeId,
            isOnline: isOnline
        };

        queryParams[queryName] = queryValue;
        /* var node = tree.tree('getSelected');
         if(node) {
             node = node.id || '';
             if(node.indexOf('company_') >= 0) {
                 queryParams.companyId = node.replace('company_', '');
             } else if(node.indexOf('site_') >= 0) {
                 queryParams.siteId = node.replace('site_', '');
             }
         }*/

        datagrid.datagrid('options').queryParams = queryParams;

        datagrid.datagrid('load');

        /* datagrid.datagrid('options').queryParams = {
             terminalName: terminalName,
             version: version,
             agentId: agentId,
             descendant: descendant,
             routeId: routeId,
             isOnline: isOnline
         };
         datagrid.datagrid('load');*/
    }

    function view(id) {
        App.dialog.show({
            css: 'width:770px;height:530px;',
            title: '查看',
            href: "${contextPath}/security/terminal/view.htm?id=" + id
        });
    }



</script>