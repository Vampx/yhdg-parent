<div class="tab_item" style="display: block">
    <div class="toolbar clearfix">
        <div class="float_right">

        </div>
        <h3>生活号订单消息</h3>
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
            url: "${contextPath}/security/basic/alipayfw_template_message/page.htm?sourceId=${(sourceId)!''}&SourceType=${(SourceType)!''}",
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
                        title: '源类型',
                        align: 'center',
                        field: 'sourceType',
                        width: 50,
                        formatter: function(val){
                        <#list sourceTypeEnum as e>
                            if(${e.getValue()} == val)
                            return '${e.getName()}';
                        </#list>
                        }
                    },
                    {
                        title: '手机号',
                        align: 'center',
                        field: 'mobile',
                        width: 70
                    },
                    {
                        title: '创建时间',
                        align: 'center',
                        field: 'createTime',
                        width: 50
                    },
                    {
                        title: '模板id',
                        align: 'center',
                        field: 'type',
                        width: 70
                    },
                    {
                        title: '变量',
                        align: 'center',
                        field: 'variable',
                        width: 70
                    },
                    {
                        title: '消息状态',
                        align: 'center',
                        field: 'status',
                        width: 70,
                        formatter: function(val){
                        <#list messageStatusEnum as e>
                            if(${e.getValue()} == val)
                            return '${e.getName()}';
                        </#list>
                        }
                    },
                    {
                        title: '昵称',
                        align: 'center',
                        field: 'nickname',
                        width: 70
                    },
                    {
                        title: '阅读次数',
                        align: 'center',
                        field: 'readCount',
                        width: 30
                    },
                    {
                        title: '操作',
                        align: 'status',
                        field: 'id',
                        width: 30,
                        formatter: function(val, row) {
                            var html = '<a href="javascript:view_${pid}(ID)">查看</a>';
                            return html.replace(/ID/g, row.id);
                        }
                    }
                ]
            ]
        });
    })();

    function view_${pid}(id) {
        App.dialog.show({
            css: 'width:550px;height:500px;overflow:visible;',
            title: '查看',
            href: "${contextPath}/security/basic/alipayfw_template_message/view.htm?id=" + id,
        });
    }
</script>