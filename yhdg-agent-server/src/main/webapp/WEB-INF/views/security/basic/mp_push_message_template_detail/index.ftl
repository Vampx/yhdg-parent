<div class="tab_item" style="display: block">
    <div class="toolbar clearfix">
        <div class="float_right">
        <#if editFlag==1>
            <button class="btn btn_green" style="padding:0 10px;" onclick="add_${pid}(${templateId},${appId})">新建
            </button>
        </#if>
        </div>
        <h3>推送消息模板明细</h3>
    </div>
    <div class="grid" style="height:345px;">
        <table id="page_table_box_${pid}"></table>
    </div>
</div>

<script>
    (function () {
        var win = $('#${pid}');

        $('#page_table_box_${pid}').datagrid({
            fit: true,
            width: '100%',
            height: '100%',
            striped: true,
            pagination: true,
            url: "${contextPath}/security/basic/mp_push_message_template_detail/page.htm?templateId=${templateId}",
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
                        title: '名称',
                        align: 'center',
                        field: 'keywordName',
                        width: 50
                    },
                    {
                        title: '内容',
                        align: 'center',
                        field: 'keywordValue',
                        width: 230
                    },
                    {
                        title: '颜色',
                        align: 'center',
                        field: 'color',
                        width: 30,
                        formatter: function(val, row) {
                            var html = "<input type='text' style='width: 40px; height:15px;background-color:"+val+"; text-align: center;' id='color'  >"
                            return html.replace(/ID/g, row.id);
                        }
                    },
                    {
                        title: '操作',
                        align: 'center',
                        field: 'a',
                        width: 60,
                        formatter: function(val, row) {
                            var html = '<a href="javascript:view_detail(APP_ID, \'ID\', TEMPLATE)">查看</a>';
                            if(${editFlag} == 1){
                                html += ' <a href="javascript:edit_detail(APP_ID, \'ID\', TEMPLATE)">修改</a>'
                            }
                            return html.replace(/APP_ID/g, row.appId).replace(/ID/g, row.id).replace(/TEMPLATE/g, row.templateId);
                        }
                    }
                ]
            ]
        });

        var ok = function () {
            return true;
        };

        win.data('ok', ok);
    })();


    function reload() {
        var datagrid = $('#page_table_box_${pid}');
        datagrid.datagrid('reload');
    }

    function view_detail(appId, id, templateId) {
        App.dialog.show({
            css: 'width:520px;height:330px;overflow:visible;',
            title: '查看',
            href: "${contextPath}/security/basic/mp_push_message_template_detail/view.htm?appId=" + appId + "&id=" + id + "&templateId=" + templateId,
        });
    }

    function edit_detail(appId, id, templateId) {
        App.dialog.show({
            css: 'width:520px;height:395px;',
            title: '修改',
            href: "${contextPath}/security/basic/mp_push_message_template_detail/edit.htm?appId=" + appId + "&id=" + id + "&templateId=" + templateId,
            event: {
                onClose: function() {
                    var datagrid = $('#page_table_box_${pid}');
                    datagrid.datagrid('reload');
                },
                onLoad: function() {
                }
            }
        });
    }
    function add_${pid}(templateId,appId) {
        App.dialog.show({
            css: 'width:520px;height:393px;',
            title: '新建',
            href: "${contextPath}/security/basic/mp_push_message_template_detail/add.htm?templateId=" + templateId + "&appId=" + appId,
            event: {
                onClose: function() {
                    var datagrid = $('#page_table_box_${pid}');
                    datagrid.datagrid('reload');
                },
                onLoad: function() {
                }
            }
        });
    }

</script>